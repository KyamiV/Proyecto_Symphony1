/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.mysymphony.proyecto_symphony1.servlet;

/**
 * Servlet institucional para editar usuarios desde el panel administrador.
 * GET: muestra formulario editarUsuario.jsp
 * POST: actualiza datos en BD, registra acción y redirige con mensaje.
 * Autor: Camila
 */

import com.mysymphony.proyecto_symphony1.dao.UsuarioDAO;
import com.mysymphony.proyecto_symphony1.dao.AuditoriaDAO;
import com.mysymphony.proyecto_symphony1.dao.BitacoraDAO;
import com.mysymphony.proyecto_symphony1.modelo.Usuario;
import com.mysymphony.proyecto_symphony1.util.Conexion;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/EditarUsuarioServlet")
public class EditarUsuarioServlet extends HttpServlet {

    // ✅ GET: mostrar formulario editarUsuario.jsp
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession sesion = request.getSession(false);
        String rol = (sesion != null) ? (String) sesion.getAttribute("rolActivo") : null;

        if (rol == null || !"administrador".equalsIgnoreCase(rol)) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        int id;
        try {
            id = Integer.parseInt(request.getParameter("idUsuario"));
        } catch (NumberFormatException e) {
            if (sesion != null) {
                sesion.setAttribute("mensaje", "⚠️ ID de usuario no válido.");
            }
            response.sendRedirect("VerUsuariosServlet");
            return;
        }

        try (Connection conn = Conexion.getConnection()) {
            UsuarioDAO dao = new UsuarioDAO(conn);
            Usuario usuario = dao.obtenerPorId(id);
            request.setAttribute("usuarioEditar", usuario);
            request.getRequestDispatcher("/administrador/editarUsuario.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            if (sesion != null) {
                sesion.setAttribute("mensaje", "❌ Error al cargar datos de usuario.");
            }
            response.sendRedirect("VerUsuariosServlet");
        }
    }

    // ✅ POST: procesar actualización
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession sesion = request.getSession(false);
        String rol = (sesion != null) ? (String) sesion.getAttribute("rolActivo") : null;
        String admin = (sesion != null) ? (String) sesion.getAttribute("nombreActivo") : "desconocido";

        if (rol == null || !"administrador".equalsIgnoreCase(rol)) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        int id = Integer.parseInt(request.getParameter("idUsuario"));
        String nombre = request.getParameter("nombre");
        String correo = request.getParameter("correo");
        String rolNuevo = request.getParameter("rol");
        String estado = request.getParameter("estado");

        boolean actualizado = false;
        String mensaje;

        try (Connection conn = Conexion.getConnection()) {
            UsuarioDAO dao = new UsuarioDAO(conn);
            Usuario u = new Usuario();
            u.setIdUsuario(id);
            u.setNombre(nombre);
            u.setCorreo(correo);
            u.setRol(rolNuevo);
            u.setEstado(estado);

            actualizado = dao.actualizar(u);

            // Bitácora
            BitacoraDAO bitacoraDAO = new BitacoraDAO(conn);
            bitacoraDAO.registrarAccion(
                    "Administrador editó usuario ID " + id,
                    admin,
                    rol,
                    "Gestión de usuarios"
            );

            // Auditoría
            Map<String, String> registro = new HashMap<>();
            registro.put("usuario", admin + " (ID: " + sesion.getAttribute("idActivo") + ")");
            registro.put("rol", rol);
            registro.put("modulo", "Gestión de usuarios");
            registro.put("referencia_id", String.valueOf(id));

            if (actualizado) {
                registro.put("accion", "Editó usuario institucional con ID " + id);
                new AuditoriaDAO(conn).registrarAccion(registro);
                mensaje = "✅ Usuario actualizado correctamente.";
            } else {
                registro.put("accion", "Intentó editar usuario ID " + id + " pero no se actualizó.");
                new AuditoriaDAO(conn).registrarAccion(registro);
                mensaje = "⚠️ No se pudo actualizar el usuario.";
            }

        } catch (Exception e) {
            e.printStackTrace();
            mensaje = "❌ Error al actualizar usuario.";
        }

        // 🔎 Validación de salida JSON para Postman
        String acceptHeader = request.getHeader("Accept");
        if (acceptHeader != null && acceptHeader.contains("application/json")) {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            if (actualizado) {
                response.getWriter().write("{\"status\":\"ok\",\"mensaje\":\"" + mensaje + "\",\"usuario\":\"" + correo + "\",\"rol\":\"" + rolNuevo + "\"}");
            } else {
                response.getWriter().write("{\"status\":\"error\",\"mensaje\":\"" + mensaje + "\"}");
            }
            return; // 👈 importante: salir para no redirigir
        }

        // 👉 Si no es JSON (ej. navegador), redirigir normalmente
        if (sesion != null) {
            sesion.setAttribute("mensaje", mensaje);
        }
        response.sendRedirect("VerUsuariosServlet");
    }
}