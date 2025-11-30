/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.mysymphony.proyecto_symphony1.servlet;

/**
 * Servlet: ListadoEstudiantesServlet
 * Rol: Docente
 * Autor: Camila
 * Creado: 24/11/2025
 *
 * Propósito:
 *   - Mostrar los estudiantes inscritos en una clase asignada al docente.
 *   - Validar sesión y rol activo.
 *   - Consultar estudiantes desde la BD con nombre, correo y fecha de inscripción.
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
import java.util.*;

@WebServlet("/ListadoEstudiantesServlet")
public class ListadoEstudiantesServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        procesarSolicitud(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // ✅ Delegamos en doGet para soportar POST sin error 405
        procesarSolicitud(request, response);
    }

    private void procesarSolicitud(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession sesion = request.getSession(false);
        String rol = (sesion != null) ? (String) sesion.getAttribute("rolActivo") : null;
        String nombre = (sesion != null) ? (String) sesion.getAttribute("nombreActivo") : "desconocido";
        Integer idDocente = (sesion != null) ? (Integer) sesion.getAttribute("idActivo") : null;

        // 🔹 Validación de sesión y rol
        if (rol == null || !"docente".equalsIgnoreCase(rol) || idDocente == null) {
            request.setAttribute("tipoMensaje", "danger");
            request.setAttribute("mensaje", "❌ Acceso restringido: requiere rol docente.");
            request.getRequestDispatcher("/error.jsp").forward(request, response);
            return;
        }

        // 🔹 Validación de parámetro claseId
        String claseIdStr = request.getParameter("claseId");
        int claseId;
        try {
            claseId = Integer.parseInt(claseIdStr);
        } catch (NumberFormatException e) {
            request.setAttribute("tipoMensaje", "danger");
            request.setAttribute("mensaje", "❌ Clase no válida.");
            request.getRequestDispatcher("/error.jsp").forward(request, response);
            return;
        }

        List<Map<String, String>> estudiantes = new ArrayList<>();

        // ✅ Consulta ajustada: inscripciones_clase + estudiantes + usuarios
        String sql = "SELECT e.id_estudiante, u.nombre, u.correo, i.fecha_inscripcion " +
                     "FROM inscripciones_clase i " +
                     "JOIN estudiantes e ON i.id_estudiante = e.id_estudiante " +
                     "JOIN usuarios u ON e.id_usuario = u.id_usuario " +
                     "WHERE i.id_clase = ? " +
                     "ORDER BY u.nombre ASC";

        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, claseId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Map<String, String> fila = new HashMap<>();
                    fila.put("id", rs.getString("id_estudiante"));
                    fila.put("nombre", rs.getString("nombre"));
                    fila.put("correo", rs.getString("correo"));
                    fila.put("fecha_inscripcion", rs.getString("fecha_inscripcion"));
                    estudiantes.add(fila);
                }
            }

            // 🛡️ Auditoría institucional
            Map<String, String> registro = new HashMap<>();
            registro.put("usuario", String.valueOf(idDocente));
            registro.put("rol", rol);
            registro.put("modulo", "Clases");
            registro.put("accion", "Visualizó listado de estudiantes de la clase ID " + claseId);
            registro.put("id_referencia", String.valueOf(idDocente));
            registro.put("ip_origen", request.getRemoteAddr());
            new AuditoriaDAO(conn).registrarAccion(registro);

            // 📖 Bitácora institucional
            BitacoraDAO bitacoraDAO = new BitacoraDAO(conn);
            bitacoraDAO.registrarAccion(
                "Docente visualizó listado de estudiantes de la clase ID " + claseId,
                nombre, rol, "Clases"
            );

        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("tipoMensaje", "danger");
            request.setAttribute("mensaje", "❌ Error al consultar los estudiantes: " + e.getMessage());
            request.getRequestDispatcher("/error.jsp").forward(request, response);
            return;
        }

        // 📤 Enviar datos al JSP
        request.setAttribute("estudiantes", estudiantes);
        request.getRequestDispatcher("/docente/listadoEstudiantes.jsp").forward(request, response);
    }
}