/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.mysymphony.proyecto_symphony1.servlet;

/**
 * Servlet institucional para cargar los datos de un usuario a editar.
 * Acceso exclusivo para el rol administrador.
 * Autor: Camila
 * Trazabilidad:
 *   - Valida rol administrador
 *   - Consulta datos del usuario
 *   - Registra acción en bitácora y auditoría institucional
 *   - Envía datos a la vista editarUsuario.jsp
 */

import com.mysymphony.proyecto_symphony1.dao.UsuarioDAO;
import com.mysymphony.proyecto_symphony1.modelo.Usuario;
import com.mysymphony.proyecto_symphony1.dao.BitacoraDAO;
import com.mysymphony.proyecto_symphony1.dao.AuditoriaDAO;
import com.mysymphony.proyecto_symphony1.util.Conexion;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/CargarUsuarioEditarServlet")
public class CargarUsuarioEditarServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession sesion = request.getSession(false);
        String rol = (sesion != null) ? (String) sesion.getAttribute("rolActivo") : null;
        String usuarioActivo = (sesion != null) ? (String) sesion.getAttribute("nombreActivo") : "desconocido";

        // 🔐 Validar rol administrador
        if (rol == null || !"administrador".equalsIgnoreCase(rol)) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        // 📥 Validar parámetro id
        String idParam = request.getParameter("id");
        if (idParam == null) {
            sesion.setAttribute("mensaje", "⚠️ ID no especificado.");
            response.sendRedirect("VerUsuariosServlet");
            return;
        }

        int id;
        try {
            id = Integer.parseInt(idParam);
        } catch (NumberFormatException e) {
            sesion.setAttribute("mensaje", "⚠️ ID inválido.");
            response.sendRedirect("VerUsuariosServlet");
            return;
        }

        try (Connection conn = Conexion.getConnection()) {
            UsuarioDAO dao = new UsuarioDAO(conn);
            Usuario usuario = dao.obtenerPorId(id);

            if (usuario != null) {
                // 📝 Registro en bitácora institucional
                BitacoraDAO bitacoraDAO = new BitacoraDAO(conn);
                bitacoraDAO.registrarAccion("Administrador cargó usuario para edición (ID " + id + ")",
                        usuarioActivo, rol, "Gestión de usuarios");

                // 🛡️ Registro en auditoría técnica
                Map<String, String> registro = new HashMap<>();
                registro.put("usuario", usuarioActivo);
                registro.put("rol", rol);
                registro.put("modulo", "Gestión de usuarios");
                registro.put("accion", "Cargó usuario para edición con ID " + id);
                new AuditoriaDAO(conn).registrarAccion(registro);

                System.out.println("✅ [" + rol + "] " + usuarioActivo +
                        " cargó usuario para edición: ID=" + id + ", Nombre=" + usuario.getNombre());

                request.setAttribute("usuarioEditar", usuario);
                request.getRequestDispatcher("/administrador/editarUsuario.jsp").forward(request, response);

            } else {
                sesion.setAttribute("mensaje", "⚠️ Usuario no encontrado.");
                response.sendRedirect("VerUsuariosServlet");
            }

        } catch (Exception e) {
            e.printStackTrace();
            sesion.setAttribute("mensaje", "❌ Error al cargar usuario.");
            response.sendRedirect("VerUsuariosServlet");
        }
    }
}