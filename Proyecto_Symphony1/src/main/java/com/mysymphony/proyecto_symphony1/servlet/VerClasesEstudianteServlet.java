/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.mysymphony.proyecto_symphony1.servlet;

/**
 * Servlet: VerClasesEstudianteServlet
 * Rol: Estudiante
 * Autor: Camila
 * Creado: 24/11/2025
 *
 * Propósito:
 *   - Visualizar todas las clases creadas por el administrador.
 *   - Validar sesión y rol activo.
 *   - Consultar clases desde la BD con docente, instrumento, etapa, día inicio, día fin y fecha límite.
 *   - Separar clases disponibles y clases inscritas.
 *   - Registrar acción en Auditoría y Bitácora institucional.
 *
 * Trazabilidad:
 *   - Recibe idEstudiante desde sesión.
 *   - Consulta tabla clases con LEFT JOIN a inscripciones_clase.
 *   - Envía atributos 'clasesDisponibles' y 'clasesInscritas' al JSP verClasesEstudiante.jsp.
 *   - Registra acceso en AuditoríaDAO y BitacoraDAO.
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

@WebServlet("/VerClasesEstudianteServlet")
public class VerClasesEstudianteServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
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

        List<Map<String, String>> clasesDisponibles = new ArrayList<>();
        List<Map<String, String>> clasesInscritas = new ArrayList<>();

        // 📊 Consulta de clases disponibles
        String sqlDisponibles = "SELECT c.id_clase, c.nombre_clase, c.instrumento, c.etapa, " +
                "c.fecha_inicio, c.fecha_fin, c.fecha_limite, " +
                "u.nombre AS nombre_docente " +
                "FROM clases c " +
                "LEFT JOIN usuarios u ON c.id_docente = u.id_usuario " +
                "WHERE c.id_clase NOT IN (SELECT id_clase FROM inscripciones_clase WHERE id_estudiante = ?) " +
                "ORDER BY c.etapa, c.nombre_clase";

        // 📊 Consulta de clases inscritas con cupo e inscritos
        String sqlInscritas = "SELECT c.id_clase, c.nombre_clase, c.instrumento, c.etapa, " +
                "c.fecha_inicio, c.fecha_fin, c.fecha_limite, " +
                "u.nombre AS nombre_docente, " +
                "c.cupo, " +
                "(SELECT COUNT(*) FROM inscripciones_clase WHERE id_clase = c.id_clase) AS inscritos " +
                "FROM clases c " +
                "INNER JOIN inscripciones_clase ic ON c.id_clase = ic.id_clase " +
                "LEFT JOIN usuarios u ON c.id_docente = u.id_usuario " +
                "WHERE ic.id_estudiante = ? " +
                "ORDER BY c.etapa, c.nombre_clase";

        try (Connection conn = Conexion.getConnection()) {
            // 📊 Clases disponibles
            try (PreparedStatement ps = conn.prepareStatement(sqlDisponibles)) {
                ps.setInt(1, idEstudiante);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        Map<String, String> fila = new HashMap<>();
                        fila.put("id_clase", rs.getString("id_clase"));
                        fila.put("nombre_clase", rs.getString("nombre_clase"));
                        fila.put("instrumento", rs.getString("instrumento"));
                        fila.put("docente", rs.getString("nombre_docente"));
                        fila.put("etapa", rs.getString("etapa"));
                        fila.put("fecha_inicio", rs.getString("fecha_inicio"));
                        fila.put("fecha_fin", rs.getString("fecha_fin"));
                        fila.put("fecha_limite", rs.getString("fecha_limite"));
                        clasesDisponibles.add(fila);
                    }
                }
            }

            // 📊 Clases inscritas
            try (PreparedStatement ps = conn.prepareStatement(sqlInscritas)) {
                ps.setInt(1, idEstudiante);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        Map<String, String> fila = new HashMap<>();
                        fila.put("id_clase", rs.getString("id_clase"));
                        fila.put("nombre_clase", rs.getString("nombre_clase"));
                        fila.put("instrumento", rs.getString("instrumento"));
                        fila.put("docente", rs.getString("nombre_docente"));
                        fila.put("etapa", rs.getString("etapa"));
                        fila.put("fecha_inicio", rs.getString("fecha_inicio"));
                        fila.put("fecha_fin", rs.getString("fecha_fin"));
                        fila.put("fecha_limite", rs.getString("fecha_limite"));
                        fila.put("cupo", rs.getString("cupo"));
                        fila.put("inscritos", rs.getString("inscritos"));
                        clasesInscritas.add(fila);
                    }
                }
            }

            // 🛡️ Auditoría institucional
            Map<String, String> registro = new HashMap<>();
            registro.put("usuario", nombre);
            registro.put("rol", rol);
            registro.put("modulo", "Clases");
            registro.put("accion", "Visualizó listado de clases disponibles e inscritas");
            registro.put("referencia_id", String.valueOf(idEstudiante));
            registro.put("ip_origen", request.getRemoteAddr());
            new AuditoriaDAO(conn).registrarAccion(registro);

            // 📖 Bitácora institucional
            BitacoraDAO bitacoraDAO = new BitacoraDAO(conn);
            bitacoraDAO.registrarAccion("Estudiante visualizó listado de clases disponibles e inscritas",
                    nombre, rol, "Clases");

        } catch (SQLException e) {
            System.err.println("❌ Error al consultar clases: " + e.getMessage());
            request.setAttribute("mensaje", "❌ Error al consultar las clases.");
        }

        // 📤 Enviar datos al JSP
        request.setAttribute("clasesDisponibles", clasesDisponibles);
        request.setAttribute("clasesInscritas", clasesInscritas);
        request.getRequestDispatcher("/estudiante/verClasesEstudiante.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // ✅ Delegamos en doGet para soportar POST sin error 405
        doGet(request, response);
    }
}