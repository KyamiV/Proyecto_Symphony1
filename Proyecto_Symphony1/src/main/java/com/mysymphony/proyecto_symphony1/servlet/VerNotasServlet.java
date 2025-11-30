/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.mysymphony.proyecto_symphony1.servlet;

/**
 * Servlet para visualizar notas registradas por curso
 * Rol: docente
 * Autor: Camila
 * Trazabilidad: valida sesión, consulta notas y registra acceso en auditoría y bitácora
 */

import com.mysymphony.proyecto_symphony1.util.Conexion;
import com.mysymphony.proyecto_symphony1.dao.AuditoriaDAO;
import com.mysymphony.proyecto_symphony1.dao.BitacoraDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.*;
import java.util.*;

@WebServlet("/VerNotasServlet")
public class VerNotasServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession sesion = request.getSession(false);

        if (sesion == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        String nombre = (String) sesion.getAttribute("nombreActivo");
        String rol = (String) sesion.getAttribute("rolActivo");

        if (nombre == null || rol == null || !"docente".equalsIgnoreCase(rol)) {
            sesion.setAttribute("mensaje", "⚠️ Acceso restringido: requiere rol docente.");
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        // Curso dinámico recibido como parámetro
        String curso = request.getParameter("curso");
        if (curso == null || curso.trim().isEmpty()) {
            curso = "Matemáticas 9°"; // valor por defecto
        }

        List<Map<String, String>> notas = new ArrayList<>();

        String sql = "SELECT nombre_estudiante, nota, fecha_registro " +
                     "FROM notas WHERE curso = ? ORDER BY fecha_registro DESC";

        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, curso);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Map<String, String> fila = new HashMap<>();
                    fila.put("nombre_estudiante", rs.getString("nombre_estudiante"));
                    fila.put("nota", rs.getString("nota"));
                    fila.put("fecha_registro", rs.getString("fecha_registro"));
                    notas.add(fila);
                }
            }

            // 📝 Auditoría institucional
            Map<String, String> registro = new HashMap<>();
            registro.put("usuario", nombre);
            registro.put("rol", rol);
            registro.put("modulo", "Consulta de notas");
            registro.put("accion", "Consultó notas del curso " + curso);
            registro.put("ip_origen", request.getRemoteAddr());
            new AuditoriaDAO(conn).registrarAccion(registro);

            // 📖 Bitácora institucional
            BitacoraDAO bitacoraDAO = new BitacoraDAO(conn);
            bitacoraDAO.registrarAccion("Docente consultó notas del curso institucional " + curso,
                    nombre, rol, "Consulta de notas");

        } catch (SQLException e) {
            System.err.println("❌ Error al consultar las notas: " + e.getMessage());
            request.setAttribute("error", "❌ Error al consultar las notas.");
        }

        // Enviar datos a la vista
        request.setAttribute("curso", curso);
        request.setAttribute("notas", notas);
        request.getRequestDispatcher("/docente/verNotasDocente.jsp").forward(request, response);
    }
}