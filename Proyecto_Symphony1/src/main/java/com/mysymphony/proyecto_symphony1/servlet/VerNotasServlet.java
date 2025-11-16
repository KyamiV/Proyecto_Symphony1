/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.mysymphony.proyecto_symphony1.servlet;

import com.mysymphony.proyecto_symphony1.util.Conexion;

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

        HttpSession sesion = request.getSession();
        String nombre = (String) sesion.getAttribute("nombreActivo");
        String rol = (String) sesion.getAttribute("rolActivo");

        // Validación de sesión y rol docente
        if (nombre == null || rol == null || !"docente".equalsIgnoreCase(rol)) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        // Curso fijo por ahora (puedes hacerlo dinámico más adelante)
        String curso = "Matemáticas 9°";
        List<Map<String, String>> notas = new ArrayList<>();

        String sql = "SELECT nombre_estudiante, nota, fecha_registro FROM notas WHERE curso = ?";

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

        } catch (SQLException e) {
            System.err.println("Error al consultar las notas: " + e.getMessage());
            request.setAttribute("error", "❌ Error al consultar las notas: " + e.getMessage());
        }

        // Enviar datos a la vista
        request.setAttribute("curso", curso);
        request.setAttribute("notas", notas);
        request.getRequestDispatcher("/docente/verNotasDocente.jsp").forward(request, response);
    }
}