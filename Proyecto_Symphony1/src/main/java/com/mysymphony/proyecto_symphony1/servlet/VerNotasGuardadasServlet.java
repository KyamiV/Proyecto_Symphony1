/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.mysymphony.proyecto_symphony1.servlet;

import com.mysymphony.proyecto_symphony1.util.Conexion;
import com.mysymphony.proyecto_symphony1.dao.AuditoriaDAO;
import com.mysymphony.proyecto_symphony1.dao.BitacoraDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.*;
import java.util.*;

@WebServlet("/VerNotasGuardadasServlet")
public class VerNotasGuardadasServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession sesion = request.getSession(false);
        String rol = (sesion != null) ? (String) sesion.getAttribute("rolActivo") : null;
        String usuario = (sesion != null) ? (String) sesion.getAttribute("nombreActivo") : "desconocido";

        if (rol == null || !"docente".equalsIgnoreCase(rol)) {
            if (sesion != null) {
                sesion.setAttribute("mensaje", "⚠️ Acceso restringido: requiere rol docente.");
            }
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        String tablaIdParam = request.getParameter("tablaId");
        int tablaId;
        try {
            tablaId = Integer.parseInt(tablaIdParam);
        } catch (Exception e) {
            request.setAttribute("mensaje", "❌ No se especificó una tabla válida.");
            request.getRequestDispatcher("/docente/tablasGuardadas.jsp").forward(request, response);
            return;
        }

        List<Map<String, String>> notas = new ArrayList<>();

        try (Connection conn = Conexion.getConnection()) {
            String sql = "SELECT n.id_estudiante, u.nombre AS estudiante, u.etapa_pedagogica AS etapa, " +
                         "n.nota, n.observacion, n.fecha " +
                         "FROM notas_clase n " +
                         "JOIN usuarios u ON n.id_estudiante = u.id " +
                         "WHERE n.id_tabla = ? " +
                         "ORDER BY u.nombre ASC";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, tablaId);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        Map<String, String> nota = new HashMap<>();
                        nota.put("estudiante", rs.getString("estudiante"));
                        nota.put("etapa", rs.getString("etapa"));
                        nota.put("nota", rs.getString("nota"));
                        nota.put("observacion", rs.getString("observacion"));
                        nota.put("fecha", rs.getString("fecha"));
                        notas.add(nota);
                    }
                }
            }

            // 📝 Auditoría institucional
            Map<String, String> registro = new HashMap<>();
            registro.put("usuario", usuario);
            registro.put("rol", rol);
            registro.put("modulo", "Notas guardadas");
            registro.put("accion", "Consultó notas guardadas de la tabla con ID " + tablaId);
            registro.put("referencia_id", String.valueOf(tablaId));
            registro.put("ip_origen", request.getRemoteAddr());
            new AuditoriaDAO(conn).registrarAccion(registro);

            // 📖 Bitácora institucional
            BitacoraDAO bitacoraDAO = new BitacoraDAO(conn);
            bitacoraDAO.registrarAccion("Docente consultó notas guardadas de la tabla institucional con ID " + tablaId,
                    usuario, rol, "Notas guardadas");

        } catch (SQLException e) {
            System.err.println("❌ Error al consultar notas guardadas: " + e.getMessage());
            request.setAttribute("mensaje", "❌ Error al consultar notas guardadas.");
        }

        request.setAttribute("notas", notas);
        request.getRequestDispatcher("/docente/verNotasGuardadas.jsp").forward(request, response);
    }
}