/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mysymphony.proyecto_symphony1.servlet;

import com.mysymphony.proyecto_symphony1.dao.AuditoriaDAO;
import com.mysymphony.proyecto_symphony1.dao.BitacoraDAO;
import com.mysymphony.proyecto_symphony1.util.Conexion;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/ActualizarColorEstudianteServlet")
public class ActualizarColorEstudianteServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession sesion = request.getSession(false);
        String rol = (sesion != null) ? (String) sesion.getAttribute("rolActivo") : null;
        String nombreSesion = (sesion != null) ? (String) sesion.getAttribute("nombreActivo") : "desconocido";
        Integer idEstudiante = (sesion != null) ? (Integer) sesion.getAttribute("idActivo") : null;

        if (rol == null || !"estudiante".equalsIgnoreCase(rol) || idEstudiante == null) {
            sesion.setAttribute("tipoMensaje", "warning");
            sesion.setAttribute("mensaje", "⚠️ Acceso restringido: requiere rol estudiante.");
            response.sendRedirect(request.getContextPath() + "/VerPerfilEstudianteServlet");
            return;
        }

        // 📥 Parámetro recibido desde el formulario
        String colorHex = request.getParameter("color_hex");

        try (Connection conn = Conexion.getConnection()) {
            // 📝 Actualizar color en la tabla estudiantes
            String sql = "UPDATE estudiantes SET color_hex = ? WHERE id_estudiante = ?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, colorHex);
                ps.setInt(2, idEstudiante);
                ps.executeUpdate();
            }

            // 🛡️ Auditoría institucional
            Map<String, String> registro = new HashMap<>();
            registro.put("usuario", nombreSesion);
            registro.put("rol", rol);
            registro.put("modulo", "Perfil");
            registro.put("accion", "Actualizó su color institucional a " + colorHex);
            registro.put("referencia_id", String.valueOf(idEstudiante));
            registro.put("ip_origen", request.getRemoteAddr());
            new AuditoriaDAO(conn).registrarAccion(registro);

            // 📖 Bitácora institucional
            BitacoraDAO bitacoraDAO = new BitacoraDAO(conn);
            bitacoraDAO.registrarAccion("Estudiante cambió su color institucional",
                    nombreSesion, rol, "Perfil");

            // ✅ Mensaje de éxito
            sesion.setAttribute("tipoMensaje", "success");
            sesion.setAttribute("mensaje", "✅ Color actualizado correctamente.");
            response.sendRedirect(request.getContextPath() + "/VerPerfilEstudianteServlet");

        } catch (Exception e) {
            e.printStackTrace();
            sesion.setAttribute("tipoMensaje", "danger");
            sesion.setAttribute("mensaje", "❌ Error al actualizar tu color: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/VerPerfilEstudianteServlet");
        }
    }
}