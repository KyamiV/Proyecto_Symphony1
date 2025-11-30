/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.mysymphony.proyecto_symphony1.servlet;

/**
 * Servlet para visualizar las notas del estudiante por clase.
 * Rol: estudiante
 * Autor: Camila
 *
 * Propósito:
 *   - Validar sesión y rol estudiante
 *   - Consultar notas desde la BD con JOIN a clases
 *   - Registrar acción en auditoría y bitácora institucional
 *   - Enviar datos a la vista verNotas.jsp
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

@WebServlet("/VerNotasEstudianteServlet")
public class VerNotasEstudianteServlet extends HttpServlet {

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

        // 📊 Lista para almacenar notas
        List<Map<String, String>> notas = new ArrayList<>();

        // 📊 Consulta SQL con JOIN a la tabla clases
        String sql = "SELECT n.id_nota, n.id_clase, c.nombre_clase, c.instrumento, c.etapa, " +
                     "n.nota, n.fecha_registro " +
                     "FROM notas n " +
                     "JOIN clases c ON n.id_clase = c.id_clase " +
                     "WHERE n.id_estudiante = ? " +
                     "ORDER BY n.fecha_registro DESC";

        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idEstudiante);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Map<String, String> fila = new HashMap<>();
                    fila.put("id_nota", rs.getString("id_nota"));
                    fila.put("id_clase", rs.getString("id_clase"));
                    fila.put("nombre_clase", rs.getString("nombre_clase"));
                    fila.put("instrumento", rs.getString("instrumento") != null ? rs.getString("instrumento") : "—");
                    fila.put("etapa", rs.getString("etapa") != null ? rs.getString("etapa") : "—");
                    fila.put("nota", rs.getString("nota"));
                    fila.put("fecha", rs.getString("fecha_registro"));
                    notas.add(fila);
                }
            }

            // 🛡️ Auditoría institucional
            Map<String, String> registro = new HashMap<>();
            registro.put("usuario", nombre);
            registro.put("rol", rol);
            registro.put("modulo", "Notas");
            registro.put("accion", "Visualizó sus notas por clase");
            registro.put("referencia_id", String.valueOf(idEstudiante));
            registro.put("ip_origen", request.getRemoteAddr());
            new AuditoriaDAO(conn).registrarAccion(registro);

            // 📖 Bitácora institucional
            BitacoraDAO bitacoraDAO = new BitacoraDAO(conn);
            bitacoraDAO.registrarAccion("Estudiante consultó sus notas por clase",
                    nombre, rol, "Notas");

        } catch (SQLException e) {
            System.err.println("❌ Error al consultar notas del estudiante: " + e.getMessage());
            request.setAttribute("mensaje", "❌ Error al consultar tus calificaciones.");
        }

        // 📤 Enviar datos a la vista JSP
        request.setAttribute("notas", notas);
        request.getRequestDispatcher("/estudiante/verNotas.jsp").forward(request, response);
    }
}