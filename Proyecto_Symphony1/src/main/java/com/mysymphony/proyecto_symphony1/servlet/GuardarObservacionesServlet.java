/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mysymphony.proyecto_symphony1.servlet;

import java.io.IOException;
import java.sql.*;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/GuardarObservacionesServlet")
public class GuardarObservacionesServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        String rol = (session != null) ? (String) session.getAttribute("rolActivo") : null;

        if (rol == null || !"docente".equalsIgnoreCase(rol)) {
            response.sendRedirect("login.jsp");
            return;
        }

        boolean exito = true;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/sias_db", "root", "")) {

                for (Map.Entry<String, String[]> entry : request.getParameterMap().entrySet()) {
                    String param = entry.getKey();

                    if (param.startsWith("obs_")) {
                        String clave = param.substring(4);
                        String estudiante = request.getParameter("estudiante_" + clave);
                        String observacion = request.getParameter(param);

                        if (estudiante != null && observacion != null && !observacion.trim().isEmpty()) {
                            try (PreparedStatement ps = conn.prepareStatement(
                                    "INSERT INTO observaciones (nombre_estudiante, observacion, fecha) VALUES (?, ?, NOW())")) {
                                ps.setString(1, estudiante);
                                ps.setString(2, observacion);
                                ps.executeUpdate();
                            }
                        }
                    }
                }

            }

        } catch (Exception e) {
            exito = false;
            e.printStackTrace();
        }

        if (exito) {
            response.sendRedirect("observaciones.jsp?exito=true");
        } else {
            response.sendRedirect("observaciones.jsp?error=true");
        }
    }
}