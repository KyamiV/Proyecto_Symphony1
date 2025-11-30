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
import java.util.HashMap;
import java.util.Map;

/**
 * Servlet para actualizar estado de certificados y moverlos al panel de gestión de clases principal.
 * Rol: Administrador
 * Autor: Camila
 */
@WebServlet("/ActualizarCertificadoServlet")
public class ActualizarCertificadoServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

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

        String idCertificadoStr = request.getParameter("id_certificado");

        try (Connection conn = Conexion.getConnection()) {

            // 1️⃣ Actualizar estado en certificados_estudiante
            String sqlUpdate = "UPDATE certificados_estudiante SET estado = 'Emitido' WHERE id_certificado = ?";
            try (PreparedStatement ps = conn.prepareStatement(sqlUpdate)) {
                ps.setInt(1, Integer.parseInt(idCertificadoStr));
                ps.executeUpdate();
            }

            // 2️⃣ Insertar en tabla de gestión de clases principal (ejemplo: tablas_validadas)
            String sqlInsert = "INSERT INTO tablas_validadas (id_certificado, id_clase, id_estudiante, fecha_validacion, usuario_admin) " +
                               "SELECT id_certificado, id_clase, id_estudiante, CURRENT_DATE, usuario_admin " +
                               "FROM certificados_estudiante WHERE id_certificado = ?";
            try (PreparedStatement ps = conn.prepareStatement(sqlInsert)) {
                ps.setInt(1, Integer.parseInt(idCertificadoStr));
                ps.executeUpdate();
            }

            // 3️⃣ Auditoría institucional
            Map<String, String> registro = new HashMap<>();
            registro.put("usuario", admin);
            registro.put("rol", rol);
            registro.put("modulo", "Certificados");
            registro.put("accion", "Certificado ID " + idCertificadoStr + " emitido y trasladado a gestión de clases");
            registro.put("referencia_id", idCertificadoStr);
            registro.put("ip_origen", request.getRemoteAddr());
            new AuditoriaDAO(conn).registrarAccion(registro);

            // 4️⃣ Bitácora institucional
            BitacoraDAO bitacoraDAO = new BitacoraDAO(conn);
            bitacoraDAO.registrarAccion("Administrador emitió y trasladó certificado ID " + idCertificadoStr,
                    admin, rol, "Certificados");

            sesion.setAttribute("mensaje", "✅ Certificado emitido y trasladado correctamente.");
            response.sendRedirect(request.getContextPath() + "/GestionarClasesPrincipalServlet");

        } catch (Exception e) {
            e.printStackTrace();
            sesion.setAttribute("mensaje", "❌ Error al actualizar certificado: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/GestionarClasesPrincipalServlet");
        }
    }
}