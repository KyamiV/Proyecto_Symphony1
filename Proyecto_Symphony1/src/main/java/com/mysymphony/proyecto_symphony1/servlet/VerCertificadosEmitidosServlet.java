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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Servlet para consultar todos los certificados emitidos institucionalmente
 * Rol: Administrador
 * Autor: Camila
 * Flujo:
 *   - Valida sesión y rol administrador
 *   - Consulta certificados emitidos desde certificados_estudiante
 *   - Registra acceso en auditoría y bitácora
 *   - Envía lista a JSP para visualización
 */
@WebServlet("/VerCertificadosEmitidosServlet")
public class VerCertificadosEmitidosServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession sesion = request.getSession(false);
        String rol = (sesion != null) ? (String) sesion.getAttribute("rolActivo") : null;
        String admin = (sesion != null) ? (String) sesion.getAttribute("nombreActivo") : "desconocido";
        Integer idAdmin = (sesion != null) ? (Integer) sesion.getAttribute("idActivo") : null;

        // 🔐 Validación de rol administrador
        if (rol == null || !"administrador".equalsIgnoreCase(rol) || idAdmin == null) {
            if (sesion != null) {
                sesion.setAttribute("mensaje", "⚠️ Acceso restringido: requiere rol administrador.");
            }
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        List<Map<String, String>> certificados = new ArrayList<>();

        try (Connection conn = Conexion.getConnection()) {
            // 📄 Consulta SQL de certificados emitidos
            String sql = "SELECT id_certificado, id_estudiante, id_clase, instrumento, etapa, " +
                         "fecha_emision, usuario_admin, estado " +
                         "FROM certificados_estudiante " +
                         "ORDER BY fecha_emision DESC";

            try (PreparedStatement ps = conn.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    Map<String, String> cert = new HashMap<>();
                    cert.put("id_certificado", String.valueOf(rs.getInt("id_certificado")));
                    cert.put("id_estudiante", String.valueOf(rs.getInt("id_estudiante")));
                    cert.put("id_clase", String.valueOf(rs.getInt("id_clase")));
                    cert.put("instrumento", rs.getString("instrumento"));
                    cert.put("etapa", rs.getString("etapa"));
                    cert.put("fecha_emision", rs.getString("fecha_emision"));
                    cert.put("usuario_admin", rs.getString("usuario_admin"));
                    cert.put("estado", rs.getString("estado"));
                    certificados.add(cert);
                }
            }

            // 📝 Auditoría institucional
            Map<String, String> registro = new HashMap<>();
            registro.put("usuario", admin);
            registro.put("rol", rol);
            registro.put("modulo", "Certificados emitidos");
            registro.put("accion", "Consultó el panel de certificados emitidos");
            registro.put("referencia_id", String.valueOf(idAdmin));
            registro.put("ip_origen", request.getRemoteAddr());
            new AuditoriaDAO(conn).registrarAccion(registro);

            // 📖 Bitácora institucional
            BitacoraDAO bitacoraDAO = new BitacoraDAO(conn);
            bitacoraDAO.registrarAccion("Administrador consultó certificados emitidos",
                    admin, rol, "Certificados emitidos");

        } catch (SQLException e) {
            System.err.println("❌ Error al consultar certificados emitidos: " + e.getMessage());
            request.setAttribute("mensaje", "❌ Error al consultar certificados emitidos.");
        }

        // 📤 Enviar lista de certificados a la vista administrativa
        request.setAttribute("certificadosEmitidos", certificados);
        request.getRequestDispatcher("/administrador/certificadosEmitidos.jsp").forward(request, response);
    }
}