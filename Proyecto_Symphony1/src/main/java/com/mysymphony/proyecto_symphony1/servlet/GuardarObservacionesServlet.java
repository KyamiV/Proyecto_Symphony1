/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.mysymphony.proyecto_symphony1.servlet;

import com.mysymphony.proyecto_symphony1.util.Conexion;
import com.mysymphony.proyecto_symphony1.dao.AuditoriaDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/GuardarObservacionesServlet")
public class GuardarObservacionesServlet extends HttpServlet {

    // 👉 Manejo de GET para evitar el error 405
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Si alguien entra por GET, lo redirigimos al servlet de visualización
        response.sendRedirect(request.getContextPath() + "/VerObservacionesServlet");
    }

    // 👉 Manejo de POST para guardar observaciones
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        String rol = (session != null) ? (String) session.getAttribute("rolActivo") : null;
        String usuario = (session != null) ? (String) session.getAttribute("nombreActivo") : "desconocido";

        if (rol == null || !"docente".equalsIgnoreCase(rol)) {
            if (session != null) {
                session.setAttribute("mensaje", "⚠️ Acceso restringido: requiere rol docente.");
            }
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        boolean exito = true;

        // 📥 Recibir parámetros del formulario
        String estudianteId = request.getParameter("idEstudiante");
        String instrumento = request.getParameter("instrumento");
        String etapa = request.getParameter("etapa_pedagogica");
        String comentario = request.getParameter("comentario");
        String enviar = request.getParameter("enviar"); // checkbox del formulario
        boolean enviada = (enviar != null && enviar.equalsIgnoreCase("on"));

        try (Connection conn = Conexion.getConnection()) {

            if (estudianteId != null && comentario != null && !comentario.trim().isEmpty()) {
                try (PreparedStatement ps = conn.prepareStatement(
                        "INSERT INTO observaciones (id_estudiante, instrumento, etapa_pedagogica, comentario, docente, fecha_registro, enviada) " +
                        "VALUES (?, ?, ?, ?, ?, NOW(), ?)")) {
                    ps.setInt(1, Integer.parseInt(estudianteId));
                    ps.setString(2, instrumento);
                    ps.setString(3, etapa);
                    ps.setString(4, comentario);
                    ps.setString(5, usuario);
                    ps.setBoolean(6, enviada);
                    ps.executeUpdate();
                }
            }

            // 📝 Registro en auditoría institucional
            AuditoriaDAO auditoriaDAO = new AuditoriaDAO(conn);
            Map<String, String> registro = new HashMap<>();
            registro.put("usuario", usuario);
            registro.put("rol", rol);
            registro.put("modulo", "Observaciones");
            registro.put("accion", "Registró observación para estudiante ID=" + estudianteId +
                    (enviada ? " y la envió al estudiante" : ""));
            registro.put("ip_origen", request.getRemoteAddr());
            auditoriaDAO.registrarAccion(registro);

            session.setAttribute("mensaje", "✅ Observación registrada correctamente." +
                    (enviada ? " Se envió al estudiante." : ""));

        } catch (Exception e) {
            exito = false;
            e.printStackTrace();
            if (session != null) {
                session.setAttribute("mensaje", "❌ Error al guardar observación: " + e.getMessage());
            }
        }

        // 🔁 Redirigir al servlet que carga datos frescos
        response.sendRedirect(request.getContextPath() + "/VerObservacionesServlet");
    }
}