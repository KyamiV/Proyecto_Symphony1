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
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

/**
 * Servlet para guardar notas de estudiantes en una clase.
 * Autor: Camila
 * Flujo: recibe datos del formulario en registroNotas.jsp, actualiza BD,
 *        registra acción en Bitácora y Auditoría, y redirige al CargarNotasServlet.
 */
@WebServlet("/GuardarNotasServlet")
public class GuardarNotasServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 🔐 Validación de sesión y rol
        HttpSession session = request.getSession(false);
        String rol = (session != null) ? (String) session.getAttribute("rolActivo") : null;
        String usuario = (session != null) ? (String) session.getAttribute("nombreActivo") : "desconocido";

        if (rol == null || !"docente".equalsIgnoreCase(rol)) {
            if (session != null) {
                session.setAttribute("tipoMensaje", "warning");
                session.setAttribute("mensaje", "⚠️ Acceso restringido: requiere rol docente.");
            }
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        // 📦 Obtener ID del estudiante desde el botón "Guardar"
        String idEstudiante = request.getParameter("guardar");
        String claseId = request.getParameter("claseId");

        if (idEstudiante == null || idEstudiante.isEmpty()) {
            session.setAttribute("tipoMensaje", "danger");
            session.setAttribute("mensaje", "⚠️ Estudiante no válido.");
            response.sendRedirect(request.getContextPath() + "/CargarNotasServlet?claseId=" + claseId);
            return;
        }

        // 📋 Obtener datos del formulario enviados desde registroNotas.jsp
        String instrumento = request.getParameter("instrumento_" + idEstudiante);
        String nivel = request.getParameter("nivel_" + idEstudiante);
        String etapa = request.getParameter("etapa_" + idEstudiante);
        String progreso = request.getParameter("progreso_" + idEstudiante);
        String observaciones = request.getParameter("obs_" + idEstudiante);
        LocalDate fecha = LocalDate.now(); // Fecha actual para trazabilidad

        boolean exito = true;

        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "UPDATE estudiantes SET instrumento = ?, nivel_tecnico = ?, etapa_pedagogica = ?, progreso = ?, observaciones = ?, fecha_actualizacion = ? WHERE id = ?")) {

            // 🗄️ Asignar parámetros al UPDATE
            ps.setString(1, instrumento);
            ps.setString(2, nivel);
            ps.setString(3, etapa);
            ps.setString(4, progreso);
            ps.setString(5, observaciones);
            ps.setDate(6, Date.valueOf(fecha));
            ps.setInt(7, Integer.parseInt(idEstudiante));

            // 🚀 Ejecutar actualización
            ps.executeUpdate();

            // 🛡️ Registro en auditoría institucional
            AuditoriaDAO auditoriaDAO = new AuditoriaDAO(conn);
            Map<String, String> registro = new HashMap<>();
            registro.put("usuario", usuario);
            registro.put("rol", rol);
            registro.put("modulo", "Notas");
            registro.put("accion", "Actualizó notas del estudiante ID " + idEstudiante);
            registro.put("ip_origen", request.getRemoteAddr());
            auditoriaDAO.registrarAccion(registro);

            // 📖 Registro en bitácora institucional
            BitacoraDAO bitacoraDAO = new BitacoraDAO(conn);
            bitacoraDAO.registrarAccion("Docente actualizó notas del estudiante ID " + idEstudiante,
                    usuario, rol, "Notas");

        } catch (Exception e) {
            exito = false;
            e.printStackTrace();
            if (session != null) {
                session.setAttribute("tipoMensaje", "danger");
                session.setAttribute("mensaje", "❌ Error al guardar notas: " + e.getMessage());
            }
        }

        // 🔗 Redirigir con mensaje institucional
        if (exito) {
            session.setAttribute("tipoMensaje", "success");
            session.setAttribute("mensaje", "✔ Notas guardadas correctamente.");
        }
        response.sendRedirect(request.getContextPath() + "/CargarNotasServlet?claseId=" + claseId);
    }
}