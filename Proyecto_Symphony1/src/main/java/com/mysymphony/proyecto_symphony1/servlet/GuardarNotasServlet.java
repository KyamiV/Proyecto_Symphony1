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

@WebServlet("/GuardarNotasServlet")
public class GuardarNotasServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String curso = request.getParameter("curso");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/sias_db", "root", "");

            for (Map.Entry<String, String[]> entry : request.getParameterMap().entrySet()) {
                String param = entry.getKey();

                if (param.startsWith("nota_")) {
                    String clave = param.substring(5);
                    String estudiante = request.getParameter("estudiante_" + clave);
                    String notaStr = request.getParameter(param);

                    if (estudiante != null && notaStr != null && !notaStr.isEmpty()) {
                        double nota = Double.parseDouble(notaStr);

                        PreparedStatement ps = conn.prepareStatement(
                            "INSERT INTO notas (nombre_estudiante, curso, nota) VALUES (?, ?, ?)"
                        );
                        ps.setString(1, estudiante);
                        ps.setString(2, curso);
                        ps.setDouble(3, nota);
                        ps.executeUpdate();
                        ps.close();
                    }
                }
            }

            conn.close();
            response.sendRedirect("registroNotas.jsp?exito=true");

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("registroNotas.jsp?error=true");
        }
    }
}