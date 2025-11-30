/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mysymphony.proyecto_symphony1.servlet;

/**
 * Servlet para consultar las notas recibidas en una tabla enviada por un docente
 * Rol: administrador
 * Autor: Camila
 * Trazabilidad: valida sesión, consulta notas de la tabla y registra acceso en auditoría/bitácora
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

@WebServlet("/VerNotasRecibidasServlet")
public class VerNotasRecibidasServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 🔐 Validar sesión activa y rol administrador
        HttpSession sesion = request.getSession(false);
        String rol = (sesion != null) ? (String) sesion.getAttribute("rolActivo") : null;
        String admin = (sesion != null) ? (String) sesion.getAttribute("nombreActivo") : "desconocido";
        Integer idAdmin = (sesion != null) ? (Integer) sesion.getAttribute("idActivo") : null;

        if (rol == null || !"administrador".equalsIgnoreCase(rol) || idAdmin == null) {
            if (sesion != null) {
                sesion.setAttribute("mensaje", "⚠️ Acceso restringido: requiere rol administrador.");
            }
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        // 📥 Obtener parámetro tablaId
        String tablaIdStr = request.getParameter("tablaId");
        if (tablaIdStr == null || tablaIdStr.trim().isEmpty()) {
            sesion.setAttribute("mensaje", "❌ No se especificó la tabla.");
            response.sendRedirect(request.getContextPath() + "/VerTablasRecibidasServlet");
            return;
        }

        int tablaId;
        try {
            tablaId = Integer.parseInt(tablaIdStr);
        } catch (NumberFormatException e) {
            sesion.setAttribute("mensaje", "❌ Identificador de tabla inválido.");
            response.sendRedirect(request.getContextPath() + "/VerTablasRecibidasServlet");
            return;
        }

        Map<String, String> datosTabla = new HashMap<>();
        List<Map<String, String>> notas = new ArrayList<>();

        try (Connection conn = Conexion.getConnection()) {

            // 📄 Consulta datos de la tabla
            String sqlTabla = "SELECT t.nombre, t.descripcion, t.fecha_envio, t.validada, u.nombre AS docente " +
                              "FROM tablas_guardadas t " +
                              "JOIN usuarios u ON t.enviada_por = u.id_usuario " +
                              "WHERE t.id = ?";
            try (PreparedStatement ps = conn.prepareStatement(sqlTabla)) {
                ps.setInt(1, tablaId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        datosTabla.put("nombre", rs.getString("nombre"));
                        datosTabla.put("descripcion", rs.getString("descripcion"));
                        datosTabla.put("fecha_envio", rs.getString("fecha_envio"));
                        datosTabla.put("validada", rs.getString("validada"));
                        datosTabla.put("docente", rs.getString("docente"));
                    }
                }
            }

            // 📄 Consulta notas asociadas a la tabla
            String sqlNotas = "SELECT e.nombre AS nombre_estudiante, n.etapa, n.instrumento, n.nota, n.observacion, n.fecha " +
                              "FROM notas_clase n " +
                              "JOIN estudiantes e ON n.id_estudiante = e.id_estudiante " +
                              "WHERE n.id_tabla = ? " +
                              "ORDER BY e.nombre ASC";
            try (PreparedStatement ps = conn.prepareStatement(sqlNotas)) {
                ps.setInt(1, tablaId);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        Map<String, String> nota = new HashMap<>();
                        nota.put("nombre_estudiante", rs.getString("nombre_estudiante"));
                        nota.put("etapa", rs.getString("etapa"));
                        nota.put("instrumento", rs.getString("instrumento"));
                        nota.put("nota", rs.getString("nota"));
                        nota.put("observacion", rs.getString("observacion"));
                        nota.put("fecha", rs.getString("fecha"));
                        notas.add(nota);
                    }
                }
            }

            // 📝 Auditoría institucional
            Map<String, String> registro = new HashMap<>();
            registro.put("usuario", admin);
            registro.put("rol", rol);
            registro.put("modulo", "Notas recibidas");
            registro.put("accion", "Consultó notas de tabla enviada por docente");
            registro.put("referencia_id", String.valueOf(tablaId));
            registro.put("ip_origen", request.getRemoteAddr());
            new AuditoriaDAO(conn).registrarAccion(registro);

            // 📖 Bitácora institucional
            BitacoraDAO bitacoraDAO = new BitacoraDAO(conn);
            bitacoraDAO.registrarAccion("Administrador consultó notas recibidas de tabla " + tablaId,
                    admin, rol, "Notas recibidas");

        } catch (SQLException e) {
            System.err.println("❌ Error al consultar notas recibidas: " + e.getMessage());
            request.setAttribute("mensaje", "❌ Error al consultar notas recibidas.");
        }

        // 📤 Enviar datos a la vista
        request.setAttribute("tablaId", tablaId);
        request.setAttribute("datosTabla", datosTabla);
        request.setAttribute("notas", notas);
        request.getRequestDispatcher("/administrador/verNotasRecibidas.jsp").forward(request, response);
    }
}