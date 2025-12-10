/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mysymphony.proyecto_symphony1.servlet;

import com.mysymphony.proyecto_symphony1.util.Conexion;
import com.mysymphony.proyecto_symphony1.dao.AuditoriaDAO;
import com.mysymphony.proyecto_symphony1.dao.BitacoraDAO;
import com.mysymphony.proyecto_symphony1.dao.TablaDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Servlet para validar una tabla institucional enviada por docentes
 * Rol: Administrador
 * Autor: Camila
 */
@WebServlet("/ValidarTablaServlet")
public class ValidarTablaServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response); // Permitir GET delegando en POST
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession sesion = request.getSession(false);
        String rol = (sesion != null) ? (String) sesion.getAttribute("rolActivo") : null;
        String usuario = (sesion != null) ? (String) sesion.getAttribute("nombreActivo") : "desconocido";
        Integer idAdmin = (sesion != null) ? (Integer) sesion.getAttribute("idActivo") : null;

        // 🔐 Validación de rol administrador
        if (rol == null || !"administrador".equalsIgnoreCase(rol) || idAdmin == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        // 📥 Validar parámetro tablaId
        String tablaIdStr = request.getParameter("tablaId");
        if (tablaIdStr == null || tablaIdStr.trim().isEmpty()) {
            sesion.setAttribute("mensaje", "❌ No se especificó la tabla a validar.");
            response.sendRedirect(request.getContextPath() + "/VerTablasRecibidasServlet");
            return;
        }

        int idTabla;
        try {
            idTabla = Integer.parseInt(tablaIdStr);
        } catch (NumberFormatException e) {
            sesion.setAttribute("mensaje", "❌ Identificador de tabla inválido.");
            response.sendRedirect(request.getContextPath() + "/VerTablasRecibidasServlet");
            return;
        }

        try (Connection conn = Conexion.getConnection()) {
            TablaDAO tablaDAO = new TablaDAO(conn);

            // 🚫 Verificar si ya está validada
            if (tablaDAO.yaValidada(idTabla)) {
                sesion.setAttribute("mensaje", "⚠️ Esta tabla ya fue validada previamente.");
                response.sendRedirect(request.getContextPath() + "/VerTablasValidadasServlet");
                return;
            }

            // 1️⃣ Actualizar la tabla en tablas_guardadas como validada
            String sqlUpdateTabla = "UPDATE tablas_guardadas " +
                                    "SET validada = 'Sí', estado = 'validada', usuario_validador = ?, " +
                                    "fecha_validacion = NOW(), fecha_actualizacion = NOW() " +
                                    "WHERE id = ?";
            try (PreparedStatement ps = conn.prepareStatement(sqlUpdateTabla)) {
                ps.setString(1, usuario); // administrador que valida
                ps.setInt(2, idTabla);
                ps.executeUpdate();
            }

            // 2️⃣ Auditoría institucional
            Map<String, String> registro = new HashMap<>();
            registro.put("usuario", usuario);
            registro.put("rol", rol);
            registro.put("modulo", "Certificación");
            registro.put("accion", "Validó tabla institucional con ID " + idTabla);
            registro.put("referencia_id", String.valueOf(idAdmin));
            registro.put("ip_origen", request.getRemoteAddr());
            new AuditoriaDAO(conn).registrarAccion(registro);

            // 3️⃣ Bitácora institucional
            BitacoraDAO bitacoraDAO = new BitacoraDAO(conn);
            bitacoraDAO.registrarAccion("Administrador validó tabla institucional con ID " + idTabla,
                    usuario, rol, "Certificación");

            // 4️⃣ Redirigir al panel de tablas validadas
            sesion.setAttribute("mensaje", "✅ Tabla validada correctamente.");
            response.sendRedirect(request.getContextPath() + "/VerTablasValidadasServlet");

        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("mensaje", "❌ Error al validar la tabla: " + e.getMessage());
            request.getRequestDispatcher("/administrador/tablasRecibidas.jsp").forward(request, response);
        }
    }
}