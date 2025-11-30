/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.mysymphony.proyecto_symphony1.servlet;

import com.mysymphony.proyecto_symphony1.dao.ClaseDAO;
import com.mysymphony.proyecto_symphony1.util.Conexion;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.util.Map;

@WebServlet("/VerClaseServlet")
public class VerClaseServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // ✅ Validar parámetro id
        String idParam = request.getParameter("id");
        if (idParam == null || !idParam.matches("\\d+")) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Parámetro 'id' inválido");
            return;
        }
        int idClase = Integer.parseInt(idParam);

        try (Connection conn = Conexion.getConnection()) {
            ClaseDAO claseDAO = new ClaseDAO(conn);

            // ✅ Consultar detalle como Map
            Map<String, Object> detalle = claseDAO.obtenerDetalleExtendido(idClase);

            // ✅ Configurar respuesta JSON
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Cache-Control", "no-store");
            response.setHeader("Pragma", "no-cache");

            // ✅ Usar Gson para convertir Map a JSON
            com.google.gson.Gson gson = new com.google.gson.GsonBuilder().setPrettyPrinting().create();
            String json = gson.toJson(detalle);
            response.getWriter().write(json);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error al obtener detalle de clase");
        }
    }
}