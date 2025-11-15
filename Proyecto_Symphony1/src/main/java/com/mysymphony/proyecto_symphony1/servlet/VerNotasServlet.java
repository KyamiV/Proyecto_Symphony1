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
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession sesion = request.getSession();
        String nombre = (String) sesion.getAttribute("nombreActivo");
        String rol = (String) sesion.getAttribute("rolActivo");

        if (nombre == null || rol == null || !"docente".equalsIgnoreCase(rol)) {
            response.sendRedirect("login.jsp");
            return;
        }

        String curso = "Matemáticas 9°"; // Puedes hacerlo dinámico más adelante
        List<Map<String, String>> notas = new ArrayList<>();

        try (Connection conn = Conexion.getConexion()) {
            PreparedStatement ps = conn.prepareStatement("SELECT nombre_estudiante, nota, fecha_registro FROM notas WHERE curso = ?");
            ps.setString(1, curso);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Map<String, String> fila = new HashMap<>();
                fila.put("nombre", rs.getString("nombre_estudiante"));
                fila.put("nota", rs.getString("nota"));
                fila.put("fecha", rs.getString("fecha_registro"));
                notas.add(fila);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        request.setAttribute("curso", curso);
        request.setAttribute("notas", notas);
        request.getRequestDispatcher("assets/verNotas.jsp").forward(request, response);
    }
}