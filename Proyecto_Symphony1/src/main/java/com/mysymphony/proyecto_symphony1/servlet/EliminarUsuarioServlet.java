/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.mysymphony.proyecto_symphony1.servlet;

/**
 * Servlet institucional para eliminar usuarios desde el panel administrador.
 * Rol: administrador
 * Autor: Camila
 * Trazabilidad:
 *   - Valida sesión
 *   - Elimina usuario en BD
 *   - Registra acción en bitácora y auditoría institucional
 *   - Redirige con mensaje
 */
import com.mysymphony.proyecto_symphony1.dao.UsuarioDAO;
import com.mysymphony.proyecto_symphony1.dao.AuditoriaDAO;
import com.mysymphony.proyecto_symphony1.dao.BitacoraDAO;
import com.mysymphony.proyecto_symphony1.util.Conexion;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/EliminarUsuarioServlet")
public class EliminarUsuarioServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession sesion = request.getSession(false);
        String rol = (sesion != null) ? (String) sesion.getAttribute("rolActivo") : null;
        String admin = (sesion != null) ? (String) sesion.getAttribute("nombreActivo") : "desconocido";

        // 🔐 Validación de sesión y rol
        if (rol == null || !"administrador".equalsIgnoreCase(rol)) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        int id;
        try {
            id = Integer.parseInt(request.getParameter("id"));
        } catch (NumberFormatException e) {
            sesion.setAttribute("mensaje", "⚠️ ID de usuario no válido.");
            response.sendRedirect("VerUsuariosServlet");
            return;
        }

        try (Connection conn = Conexion.getConnection()) {
            conn.setAutoCommit(false); // 🚦 Manejo de transacción
            UsuarioDAO dao = new UsuarioDAO(conn);

            // ⚠️ Aquí podrías ampliar para eliminar en cascada en estudiantes/docentes si aplica
            boolean eliminado = dao.eliminar(id);

            // 📝 Registro en bitácora institucional
            BitacoraDAO bitacoraDAO = new BitacoraDAO(conn);
            bitacoraDAO.registrarAccion("Administrador eliminó usuario ID " + id,
                    admin, rol, "Gestión de usuarios");

            // 🛡️ Registro en auditoría técnica
            Map<String, String> registro = new HashMap<>();
            registro.put("usuario", admin + " (ID: " + sesion.getAttribute("idActivo") + ")");
            registro.put("rol", rol);
            registro.put("modulo", "Gestión de usuarios");
            registro.put("referencia_id", String.valueOf(id));

            if (eliminado) {
                registro.put("accion", "Eliminó usuario institucional con ID " + id);
                new AuditoriaDAO(conn).registrarAccion(registro);
                sesion.setAttribute("mensaje", "🗑️ Usuario eliminado correctamente.");
                conn.commit();
                System.out.println("✅ Usuario eliminado: ID=" + id + " por " + admin);
            } else {
                registro.put("accion", "Intentó eliminar usuario ID " + id + " pero no se encontró o falló.");
                new AuditoriaDAO(conn).registrarAccion(registro);
                sesion.setAttribute("mensaje", "⚠️ No se pudo eliminar el usuario.");
                conn.rollback();
            }

        } catch (Exception e) {
            e.printStackTrace();
            sesion.setAttribute("mensaje", "❌ Error al eliminar usuario.");
        }

        // 📤 Redirección al listado actualizado
        response.sendRedirect("VerUsuariosServlet");
    }
}