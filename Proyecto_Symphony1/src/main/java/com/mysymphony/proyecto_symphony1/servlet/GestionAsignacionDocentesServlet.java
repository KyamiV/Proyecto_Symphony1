/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.mysymphony.proyecto_symphony1.servlet;

/**
 * Servlet para gestionar y visualizar la asignación de docentes a clases.
 * Rol: administrador
 * Autor: Camila
 * Trazabilidad: consulta clases con docentes asignados, inscritos y horario
 */

import com.mysymphony.proyecto_symphony1.util.Conexion;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.*;
import java.util.*;

@WebServlet("/GestionAsignacionDocentesServlet")
public class GestionAsignacionDocentesServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession sesion = request.getSession(false);
        if (sesion == null || !"administrador".equalsIgnoreCase((String) sesion.getAttribute("rolActivo"))) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        List<Map<String, String>> clasesConInscritos = new ArrayList<>();

        // 🔹 Ajusta nombres de tablas/columnas según tu BD real
        String sql = "SELECT c.id AS id_clase, c.nombre_clase, c.instrumento, c.etapa, c.grupo, " +
                     "u.nombre AS nombre_docente, c.dia_semana, c.hora_inicio, c.hora_fin, " +
                     "COUNT(i.id_estudiante) AS inscritos " +
                     "FROM asignaciones_docente ad " +
                     "JOIN usuarios u ON ad.id_docente = u.id " +
                     "JOIN clases c ON ad.clase_id = c.id " +
                     "LEFT JOIN inscripciones i ON i.clase_id = c.id " +
                     "GROUP BY c.id, c.nombre_clase, c.instrumento, c.etapa, c.grupo, " +
                     "u.nombre, c.dia_semana, c.hora_inicio, c.hora_fin";

        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Map<String, String> clase = new HashMap<>();
                clase.put("id_clase", rs.getString("id_clase"));
                clase.put("nombre_clase", rs.getString("nombre_clase"));
                clase.put("instrumento", rs.getString("instrumento"));
                clase.put("etapa", rs.getString("etapa"));
                clase.put("grupo", rs.getString("grupo"));
                clase.put("nombre_docente", rs.getString("nombre_docente"));
                clase.put("dia_semana", rs.getString("dia_semana"));
                clase.put("hora_inicio", rs.getString("hora_inicio"));
                clase.put("hora_fin", rs.getString("hora_fin"));
                clase.put("inscritos", rs.getString("inscritos"));
                clasesConInscritos.add(clase);
            }

        } catch (SQLException e) {
            request.setAttribute("error", "❌ Error al cargar asignaciones: " + e.getMessage());
        }

        request.setAttribute("clasesConInscritos", clasesConInscritos);
        request.getRequestDispatcher("/administrador/asignacionesDocentes.jsp").forward(request, response);
    }
}