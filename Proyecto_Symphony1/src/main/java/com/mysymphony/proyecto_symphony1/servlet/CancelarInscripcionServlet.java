/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mysymphony.proyecto_symphony1.servlet;

/**
 * Servlet: CancelarInscripcionServlet
 * Rol: Estudiante
 * Autor: Camila
 * Creado: 24/11/2025
 *
 * Propósito:
 *   - Permitir al estudiante cancelar su inscripción en una clase.
 *   - Validar sesión y rol activo.
 *   - Verificar si la clase ya alcanzó el cupo máximo.
 *   - Si está llena, impedir la cancelación y redirigir a actividades.
 *   - Registrar acción en Auditoría y Bitácora institucional.
 */

import com.mysymphony.proyecto_symphony1.dao.AuditoriaDAO;
import com.mysymphony.proyecto_symphony1.dao.BitacoraDAO;
import com.mysymphony.proyecto_symphony1.util.Conexion;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/CancelarInscripcionServlet")
public class CancelarInscripcionServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // ✅ Soportamos GET para evitar el error 405
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

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

        String claseIdStr = request.getParameter("claseId");
        int claseId;
        try {
            claseId = Integer.parseInt(claseIdStr);
        } catch (NumberFormatException e) {
            request.setAttribute("mensaje", "❌ Clase no válida.");
            request.getRequestDispatcher("/VerClasesEstudianteServlet").forward(request, response);
            return;
        }

        try (Connection conn = Conexion.getConnection()) {
            // 📊 Verificar cupo y número de inscritos
            String sqlCheck = "SELECT c.cupo, COUNT(ic.id_estudiante) AS inscritos " +
                              "FROM clases c " +
                              "LEFT JOIN inscripciones_clase ic ON c.id_clase = ic.id_clase " +
                              "WHERE c.id_clase = ? " +
                              "GROUP BY c.cupo";
            int cupo = 0;
            int inscritos = 0;

            try (PreparedStatement ps = conn.prepareStatement(sqlCheck)) {
                ps.setInt(1, claseId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        cupo = rs.getInt("cupo");
                        inscritos = rs.getInt("inscritos");
                    }
                }
            }

            if (inscritos >= cupo) {
                // Clase llena → no se puede cancelar, redirigir a actividades
                sesion.setAttribute("mensaje", "⚠️ Clase asignada, no puedes cancelar. Revisa tus actividades.");
                response.sendRedirect(request.getContextPath() + "/ActividadesEstudianteServlet?claseId=" + claseId);
                return;
            }

            // 🗑️ Cancelar inscripción
            String sqlDelete = "DELETE FROM inscripciones_clase WHERE id_estudiante = ? AND id_clase = ?";
            try (PreparedStatement ps = conn.prepareStatement(sqlDelete)) {
                ps.setInt(1, idEstudiante);
                ps.setInt(2, claseId);
                ps.executeUpdate();
            }

            // 🛡️ Auditoría institucional
            Map<String, String> registro = new HashMap<>();
            registro.put("usuario", nombre);
            registro.put("rol", rol);
            registro.put("modulo", "Inscripción");
            registro.put("accion", "Canceló inscripción en clase ID " + claseId);
            registro.put("ip_origen", request.getRemoteAddr());
            new AuditoriaDAO(conn).registrarAccion(registro);

            // 📖 Bitácora institucional
            BitacoraDAO bitacoraDAO = new BitacoraDAO(conn);
            bitacoraDAO.registrarAccion("Estudiante canceló inscripción en clase ID " + claseId,
                    nombre, rol, "Inscripción");

            // ✅ Mensaje de éxito
            request.setAttribute("mensaje", "✅ Inscripción cancelada correctamente.");
            request.getRequestDispatcher("/VerClasesEstudianteServlet").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("mensaje", "❌ Error al cancelar inscripción: " + e.getMessage());
            request.getRequestDispatcher("/VerClasesEstudianteServlet").forward(request, response);
        }
    }
}