/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.mysymphony.proyecto_symphony1.servlet;

/**
 * Servlet para visualizar las observaciones del estudiante.
 * Rol: estudiante
 * Autor: Camila
 *
 * Propósito:
 *   - Validar sesión y rol estudiante
 *   - Consultar observaciones desde la BD (tabla observaciones)
 *   - Registrar acción en auditoría y bitácora institucional
 *   - Enviar datos a la vista verObservaciones.jsp
 */

import com.mysymphony.proyecto_symphony1.dao.AuditoriaDAO;
import com.mysymphony.proyecto_symphony1.dao.BitacoraDAO;
import com.mysymphony.proyecto_symphony1.util.Conexion;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.*;
import java.util.*;

@WebServlet("/VerObservacionesEstudianteServlet")
public class VerObservacionesEstudianteServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 🔐 Validar sesión activa y rol estudiante
        HttpSession sesion = request.getSession(false);
        String rol = (sesion != null) ? (String) sesion.getAttribute("rolActivo") : null;
        String nombre = (sesion != null) ? (String) sesion.getAttribute("nombreActivo") : "desconocido";
        Integer idEstudiante = (sesion != null) ? (Integer) sesion.getAttribute("idActivo") : null;

        if (rol == null || !"estudiante".equalsIgnoreCase(rol) || idEstudiante == null) {
            if (sesion != null) {
                sesion.setAttribute("mensaje", "⚠️ Acceso restringido: requiere rol estudiante.");
            }
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        // 📊 Lista para almacenar observaciones
        List<Map<String, String>> observaciones = new ArrayList<>();

        // ✅ Consulta ajustada a la tabla observaciones
        String sql = "SELECT id_observacion, instrumento, etapa_pedagogica, comentario, docente, fecha_registro, enviada " +
                     "FROM observaciones WHERE id_estudiante = ? ORDER BY fecha_registro DESC";

        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idEstudiante);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Map<String, String> fila = new HashMap<>();
                    fila.put("id_observacion", rs.getString("id_observacion"));
                    fila.put("instrumento", rs.getString("instrumento"));
                    fila.put("etapa_pedagogica", rs.getString("etapa_pedagogica"));
                    fila.put("comentario", rs.getString("comentario"));
                    fila.put("docente", rs.getString("docente"));
                    fila.put("fecha", rs.getString("fecha_registro"));
                    fila.put("enviada", rs.getBoolean("enviada") ? "Sí" : "No");
                    observaciones.add(fila);
                }
            }

            // 🛡️ Auditoría institucional
            Map<String, String> registro = new HashMap<>();
            registro.put("usuario", nombre);
            registro.put("rol", rol);
            registro.put("modulo", "Observaciones");
            registro.put("accion", "Visualizó sus observaciones institucionales");
            registro.put("referencia_id", String.valueOf(idEstudiante));
            registro.put("ip_origen", request.getRemoteAddr());
            new AuditoriaDAO(conn).registrarAccion(registro);

            // 📝 Bitácora institucional
            BitacoraDAO bitacoraDAO = new BitacoraDAO(conn);
            bitacoraDAO.registrarAccion("Estudiante consultó sus observaciones institucionales",
                    nombre, rol, "Observaciones");

        } catch (SQLException e) {
            System.err.println("❌ Error al consultar observaciones del estudiante: " + e.getMessage());
            request.setAttribute("mensaje", "❌ Error al consultar tus observaciones.");
        }

        // 📤 Enviar datos a la vista JSP
        request.setAttribute("observaciones", observaciones);
        request.getRequestDispatcher("/estudiante/verObservaciones.jsp").forward(request, response);
    }
}