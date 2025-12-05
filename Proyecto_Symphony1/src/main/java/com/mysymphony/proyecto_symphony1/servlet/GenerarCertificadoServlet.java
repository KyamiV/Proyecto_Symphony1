/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.mysymphony.proyecto_symphony1.servlet;

/**
 * Servlet para generar certificado institucional de una tabla validada
 * Rol: administrador
 * Autor: Camila
 * Trazabilidad: consulta tabla validada, métricas de notas y registro en auditoría institucional
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

@WebServlet("/GenerarCertificadoServlet")
public class GenerarCertificadoServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 🔐 Validar sesión y rol administrador
        HttpSession sesion = request.getSession(false);
        if (sesion == null || sesion.getAttribute("rolActivo") == null ||
            !"administrador".equalsIgnoreCase((String) sesion.getAttribute("rolActivo"))) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        // 📥 Obtener parámetro tablaId
        String tablaIdParam = request.getParameter("tablaId");
        if (tablaIdParam == null) {
            request.setAttribute("mensaje", "❌ No se especificó la tabla.");
            request.setAttribute("tipoMensaje", "danger");
            request.getRequestDispatcher("/adminitrador/tablasValidadas.jsp").forward(request, response);
            return;
        }

        int tablaId = Integer.parseInt(tablaIdParam);
        Map<String, String> certificado = new HashMap<>();

        try (Connection conn = Conexion.getConnection()) {

            // 📄 Consulta principal de tabla validada
            String sql = "SELECT t.nombre, t.descripcion, t.fecha_envio, t.fecha_validacion, " +
                         "       c.nombre_clase AS clase, u.nombre AS docente " +
                         "FROM tablas_certificacion t " +
                         "JOIN clases c ON t.id_clase = c.id_clase " +
                         "JOIN usuarios u ON t.id_estudiante = u.id " +
                         "WHERE t.id_certificacion = ? AND t.estado = 'validada'";

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, tablaId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        certificado.put("nombre_tabla", rs.getString("nombre"));
                        certificado.put("descripcion", rs.getString("descripcion"));
                        certificado.put("fecha_envio", rs.getString("fecha_envio"));
                        certificado.put("fecha_validacion", rs.getString("fecha_validacion"));
                        certificado.put("clase", rs.getString("clase"));
                        certificado.put("docente", rs.getString("docente"));
                    }
                }
            }

            // 📊 Subconsulta para promedio y cantidad de notas
            String sqlNotas = "SELECT COUNT(*) AS cantidad, AVG(nota) AS promedio " +
                              "FROM notas WHERE id_clase = (SELECT id_clase FROM tablas_certificacion WHERE id_certificacion = ?)";
            try (PreparedStatement psNotas = conn.prepareStatement(sqlNotas)) {
                psNotas.setInt(1, tablaId);
                try (ResultSet rsNotas = psNotas.executeQuery()) {
                    if (rsNotas.next()) {
                        certificado.put("cantidad_notas", String.valueOf(rsNotas.getInt("cantidad")));
                        certificado.put("promedio_notas", String.format("%.2f", rsNotas.getDouble("promedio")));
                    }
                }
            }

            // 📝 Registrar acción en auditoría
            String admin = (String) sesion.getAttribute("nombreActivo");
            Map<String, String> registro = new HashMap<>();
            registro.put("usuario", admin);
            registro.put("rol", "administrador");
            registro.put("modulo", "Certificación institucional");
            registro.put("accion", "Generó certificado de la tabla validada con ID " + tablaId);
            registro.put("ip_origen", request.getRemoteAddr());

            new AuditoriaDAO(conn).registrarAccion(registro);

            // 📖 Bitácora institucional
            new BitacoraDAO(conn).registrarAccion(
                    "Administrador generó certificado de tabla validada ID " + tablaId,
                    admin, "administrador", "Certificación");

        } catch (SQLException e) {
            System.err.println("❌ Error al generar certificado: " + e.getMessage());
            sesion.setAttribute("mensaje", "❌ Error al generar certificado.");
            sesion.setAttribute("tipoMensaje", "danger");
            response.sendRedirect(request.getContextPath() + "/administrador/tablasValidadas.jsp");
            return;
        }

        // 🔎 Validación de salida JSON para Postman
        String acceptHeader = request.getHeader("Accept");
        if (acceptHeader != null && acceptHeader.contains("application/json")) {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            StringBuilder json = new StringBuilder("{");
            for (Map.Entry<String, String> entry : certificado.entrySet()) {
                json.append("\"").append(entry.getKey()).append("\":\"").append(entry.getValue()).append("\",");
            }
            if (json.charAt(json.length() - 1) == ',') {
                json.deleteCharAt(json.length() - 1);
            }
            json.append("}");

            response.getWriter().write(json.toString());
            return; // 👈 salir para no redirigir
        }

        // 👉 Si no es JSON (ej. navegador), redirigir a la vista JSP
        request.setAttribute("certificado", certificado);
        request.getRequestDispatcher("/administrador/certificado.jsp").forward(request, response);
    }
}