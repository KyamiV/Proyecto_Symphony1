/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.mysymphony.proyecto_symphony1.servlet;

/**
 * Servlet para consultar registros de auditoría institucional con filtros
 * Autor: Camila
 * Trazabilidad: valida sesión, aplica filtros dinámicos y registra acceso en bitácora
 */

import com.mysymphony.proyecto_symphony1.util.Conexion;
import com.mysymphony.proyecto_symphony1.dao.BitacoraDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.*;
import java.util.*;

@WebServlet("/VerAuditoriaServlet")
public class VerAuditoriaServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 🔐 Validar sesión y rol administrador
        HttpSession sesion = request.getSession(false);
        String rol = (sesion != null) ? (String) sesion.getAttribute("rolActivo") : null;
        String usuario = (sesion != null) ? (String) sesion.getAttribute("nombreActivo") : "desconocido";

        if (rol == null || !"administrador".equalsIgnoreCase(rol)) {
            if (sesion != null) {
                sesion.setAttribute("mensaje", "⚠️ Acceso restringido: requiere rol administrador.");
            }
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        // 📥 Filtros recibidos desde la vista
        String usuarioFiltro = request.getParameter("usuario");
        String moduloFiltro = request.getParameter("modulo");
        String fechaFiltro = request.getParameter("fecha");

        List<Map<String, String>> auditoria = new ArrayList<>();

        try (Connection conn = Conexion.getConnection()) {

            // 📄 Construcción dinámica de la consulta SQL con filtros
            StringBuilder sql = new StringBuilder("SELECT usuario, rol, modulo, accion, fecha, ip_origen FROM auditoria WHERE 1=1");
            List<Object> valores = new ArrayList<>();

            if (usuarioFiltro != null && !usuarioFiltro.trim().isEmpty()) {
                sql.append(" AND usuario LIKE ?");
                valores.add("%" + usuarioFiltro.trim() + "%");
            }
            if (moduloFiltro != null && !moduloFiltro.trim().isEmpty()) {
                sql.append(" AND modulo LIKE ?");
                valores.add("%" + moduloFiltro.trim() + "%");
            }
            if (fechaFiltro != null && !fechaFiltro.trim().isEmpty()) {
                sql.append(" AND DATE(fecha) = ?");
                valores.add(fechaFiltro.trim());
            }

            sql.append(" ORDER BY fecha DESC");

            try (PreparedStatement ps = conn.prepareStatement(sql.toString())) {
                for (int i = 0; i < valores.size(); i++) {
                    ps.setObject(i + 1, valores.get(i));
                }

                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        Map<String, String> registro = new HashMap<>();
                        registro.put("usuario", rs.getString("usuario"));
                        registro.put("rol", rs.getString("rol"));
                        registro.put("modulo", rs.getString("modulo"));
                        registro.put("accion", rs.getString("accion"));
                        registro.put("fecha", rs.getString("fecha"));
                        registro.put("ip_origen", rs.getString("ip_origen"));
                        auditoria.add(registro);
                    }
                }
            }

            // 📝 Registrar acceso en bitácora institucional
            BitacoraDAO bitacoraDAO = new BitacoraDAO(conn);
            bitacoraDAO.registrarAccion("Administrador consultó registros de auditoría institucional",
                    usuario, rol, "Auditoría");

        } catch (SQLException e) {
            request.setAttribute("mensaje", "❌ Error al consultar la auditoría: " + e.getMessage());
        }

        // 📤 Enviar datos y filtros a la vista
        request.setAttribute("auditoria", auditoria);
        request.getRequestDispatcher("/administrador/auditoria.jsp").forward(request, response);
    }
}