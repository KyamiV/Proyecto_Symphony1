/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.mysymphony.proyecto_symphony1.servlet;

/**
 * Servlet para cargar los datos de una tabla institucional para edición.
 * Autor: Camila
 * Trazabilidad:
 *   - Valida rol docente
 *   - Consulta datos de la tabla guardada
 *   - Registra acción en bitácora y auditoría institucional
 *   - Envía datos a la vista editarTabla.jsp
 */

import com.mysymphony.proyecto_symphony1.util.Conexion;
import com.mysymphony.proyecto_symphony1.dao.BitacoraDAO;
import com.mysymphony.proyecto_symphony1.dao.AuditoriaDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/CargarTablaEditarServlet")
public class CargarTablaEditarServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession sesion = request.getSession(false);
        String rol = (sesion != null) ? (String) sesion.getAttribute("rolActivo") : null;
        String usuario = (sesion != null) ? (String) sesion.getAttribute("nombreActivo") : "desconocido";

        // 🔐 Validar rol docente
        if (rol == null || !"docente".equalsIgnoreCase(rol)) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        // 📥 Validar parámetro tablaId
        String tablaIdParam = request.getParameter("tablaId");
        if (tablaIdParam == null) {
            sesion.setAttribute("mensaje", "⚠️ No se especificó la tabla.");
            response.sendRedirect(request.getContextPath() + "/VerTablasGuardadasServlet");
            return;
        }

        int tablaId;
        try {
            tablaId = Integer.parseInt(tablaIdParam);
        } catch (NumberFormatException e) {
            sesion.setAttribute("mensaje", "❌ ID de tabla no válido.");
            response.sendRedirect(request.getContextPath() + "/VerTablasGuardadasServlet");
            return;
        }

        Map<String, String> tabla = new HashMap<>();

        String sql = "SELECT t.id, t.nombre, t.descripcion, t.fecha, t.fecha_actualizacion, " +
                     "t.registrada_por, t.usuario_editor, c.nombre AS clase " +
                     "FROM tablas_guardadas t " +
                     "JOIN clases c ON t.id_clase = c.id " +
                     "WHERE t.id = ?";

        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, tablaId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    tabla.put("id", String.valueOf(rs.getInt("id")));
                    tabla.put("nombre", rs.getString("nombre"));
                    tabla.put("descripcion", rs.getString("descripcion"));
                    tabla.put("fecha", rs.getString("fecha"));
                    tabla.put("fecha_actualizacion", rs.getString("fecha_actualizacion") != null ? rs.getString("fecha_actualizacion") : "—");
                    tabla.put("registrada_por", rs.getString("registrada_por") != null ? rs.getString("registrada_por") : "—");
                    tabla.put("usuario_editor", rs.getString("usuario_editor") != null ? rs.getString("usuario_editor") : "—");
                    tabla.put("clase", rs.getString("clase"));

                    // 📝 Registro en bitácora institucional
                    BitacoraDAO bitacoraDAO = new BitacoraDAO(conn);
                    bitacoraDAO.registrarAccion("Docente cargó tabla para edición (ID " + tablaId + ")",
                            usuario, rol, "Tablas institucionales");

                    // 🛡️ Registro en auditoría técnica
                    Map<String, String> registro = new HashMap<>();
                    registro.put("usuario", usuario);
                    registro.put("rol", rol);
                    registro.put("modulo", "Tablas institucionales");
                    registro.put("accion", "Cargó tabla para edición con ID " + tablaId);
                    new AuditoriaDAO(conn).registrarAccion(registro);

                    System.out.println("✅ Tabla cargada para edición: ID=" + tablaId + ", Nombre=" + tabla.get("nombre"));
                } else {
                    sesion.setAttribute("mensaje", "⚠️ No se encontró la tabla especificada.");
                    response.sendRedirect(request.getContextPath() + "/VerTablasGuardadasServlet");
                    return;
                }
            }

        } catch (SQLException e) {
            System.err.println("❌ Error al cargar tabla para edición: " + e.getMessage());
            sesion.setAttribute("mensaje", "❌ Error al cargar tabla para edición.");
            response.sendRedirect(request.getContextPath() + "/VerTablasGuardadasServlet");
            return;
        }

        // 📤 Enviar datos a la vista
        request.setAttribute("tabla", tabla);
        request.getRequestDispatcher("/docente/editarTabla.jsp").forward(request, response);
    }
}