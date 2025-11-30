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

@WebServlet("/MarcarActividadServlet")
public class MarcarActividadServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession sesion = request.getSession(false);
        String rol = (sesion != null) ? (String) sesion.getAttribute("rolActivo") : null;
        String nombre = (sesion != null) ? (String) sesion.getAttribute("nombreActivo") : "desconocido";
        Integer idEstudiante = (sesion != null) ? (Integer) sesion.getAttribute("idActivo") : null;

        if (rol == null || !"estudiante".equalsIgnoreCase(rol) || idEstudiante == null) {
            sesion.setAttribute("tipoMensaje", "warning");
            sesion.setAttribute("mensaje", "⚠️ Acceso restringido: requiere rol estudiante.");
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        String actividadIdStr = request.getParameter("actividadId");
        int actividadId;
        try {
            actividadId = Integer.parseInt(actividadIdStr);
        } catch (NumberFormatException e) {
            sesion.setAttribute("tipoMensaje", "danger");
            sesion.setAttribute("mensaje", "❌ Actividad no válida.");
            response.sendRedirect(request.getContextPath() + "/VerClasesEstudianteServlet");
            return;
        }

        try (Connection conn = Conexion.getConnection()) {
            // 📝 Actualizar estado de la actividad
            String sql = "UPDATE actividades SET estado = 'completada' WHERE id_actividad = ?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, actividadId);
                ps.executeUpdate();
            }

            // 🛡️ Auditoría institucional
            Map<String, String> registro = new HashMap<>();
            registro.put("usuario", nombre);
            registro.put("rol", rol);
            registro.put("modulo", "Actividades");
            registro.put("accion", "Marcó como completada la actividad ID " + actividadId);
            registro.put("referencia_id", String.valueOf(idEstudiante));
            registro.put("ip_origen", request.getRemoteAddr());
            new AuditoriaDAO(conn).registrarAccion(registro);

            // 📖 Bitácora institucional
            BitacoraDAO bitacoraDAO = new BitacoraDAO(conn);
            bitacoraDAO.registrarAccion("Estudiante marcó como completada la actividad ID " + actividadId,
                    nombre, rol, "Actividades");

            // ✅ Mensaje de éxito
            sesion.setAttribute("tipoMensaje", "success");
            sesion.setAttribute("mensaje", "✅ Actividad marcada como completada.");
            response.sendRedirect(request.getContextPath() + "/ActividadesEstudianteServlet?claseId=" + request.getParameter("claseId"));

        } catch (Exception e) {
            e.printStackTrace();
            sesion.setAttribute("tipoMensaje", "danger");
            sesion.setAttribute("mensaje", "❌ Error al marcar la actividad: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/ActividadesEstudianteServlet?claseId=" + request.getParameter("claseId"));
        }
    }
}