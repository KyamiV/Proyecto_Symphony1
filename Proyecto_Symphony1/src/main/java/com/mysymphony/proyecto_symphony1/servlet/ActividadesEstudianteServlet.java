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

@WebServlet("/ActividadesEstudianteServlet")
public class ActividadesEstudianteServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String claseIdStr = request.getParameter("claseId");
        int claseId = (claseIdStr != null) ? Integer.parseInt(claseIdStr) : 0;

        List<Map<String,String>> actividades = new ArrayList<>();

        String sql = "SELECT id_actividad, nombre_actividad, descripcion, fecha_entrega " +
                     "FROM actividades WHERE id_clase = ? ORDER BY fecha_entrega ASC";

        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, claseId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Map<String,String> fila = new HashMap<>();
                    fila.put("id_actividad", String.valueOf(rs.getInt("id_actividad")));
                    fila.put("nombre", rs.getString("nombre_actividad"));
                    fila.put("descripcion", rs.getString("descripcion"));
                    fila.put("fecha_entrega", rs.getDate("fecha_entrega").toString());
                    actividades.add(fila);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("mensaje", "❌ Error al cargar actividades: " + e.getMessage());
        }

        request.setAttribute("actividades", actividades);
        request.getRequestDispatcher("/estudiante/actividadesEstudiante.jsp").forward(request, response);
    }
}