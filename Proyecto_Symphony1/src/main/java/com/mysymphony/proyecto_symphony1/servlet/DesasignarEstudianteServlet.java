/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.mysymphony.proyecto_symphony1.servlet;

/**
 * Servlet para desasignar un estudiante de un docente por instrumento.
 * Rol: administrador
 * Autor: Camila
 * Trazabilidad:
 *   - Valida sesión
 *   - Elimina asignación
 *   - Registra acción en bitácora y auditoría institucional
 */

import com.mysymphony.proyecto_symphony1.util.Conexion;
import com.mysymphony.proyecto_symphony1.dao.BitacoraDAO;
import com.mysymphony.proyecto_symphony1.dao.AuditoriaDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/DesasignarEstudianteServlet")
public class DesasignarEstudianteServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession sesion = request.getSession(false);
        String rol = (sesion != null) ? (String) sesion.getAttribute("rolActivo") : null;
        String usuario = (sesion != null) ? (String) sesion.getAttribute("nombreActivo") : "desconocido";

        // 🔐 Validar rol administrador
        if (rol == null || !"administrador".equalsIgnoreCase(rol)) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        String docenteId = request.getParameter("docenteId");
        String estudianteId = request.getParameter("idEstudiante");
        String instrumento = request.getParameter("instrumento");

        if (docenteId == null || estudianteId == null || instrumento == null) {
            sesion.setAttribute("mensaje", "❌ Faltan datos para desasignar el estudiante.");
            response.sendRedirect(request.getContextPath() + "/AsignarEstudiantesServlet");
            return;
        }

        try (Connection conn = Conexion.getConnection()) {
            conn.setAutoCommit(false);

            // 1️⃣ Eliminar asignación
            try (PreparedStatement ps = conn.prepareStatement(
                    "DELETE FROM asignaciones_docente WHERE id_docente = ? AND id_estudiante = ? AND instrumento = ?")) {
                ps.setInt(1, Integer.parseInt(docenteId));
                ps.setInt(2, Integer.parseInt(estudianteId));
                ps.setString(3, instrumento);
                ps.executeUpdate();
            }

            // 2️⃣ Registrar en bitácora institucional
            BitacoraDAO bitacoraDAO = new BitacoraDAO(conn);
            bitacoraDAO.registrarAccion("Administrador desasignó estudiante ID " + estudianteId +
                    " del docente ID " + docenteId + " (" + instrumento + ")", usuario, rol, "Asignaciones");

            // 3️⃣ Auditoría institucional
            Map<String, String> registro = new HashMap<>();
            registro.put("usuario", usuario);
            registro.put("rol", rol);
            registro.put("modulo", "Asignaciones");
            registro.put("accion", "Desasignación del estudiante ID " + estudianteId +
                    " del docente ID " + docenteId + " (" + instrumento + ")");
            new AuditoriaDAO(conn).registrarAccion(registro);

            conn.commit();
            sesion.setAttribute("mensaje", "✅ Estudiante desasignado correctamente.");
            System.out.println("✅ Estudiante ID=" + estudianteId + " desasignado del docente ID=" + docenteId + " por " + usuario);

        } catch (SQLException e) {
            e.printStackTrace();
            sesion.setAttribute("mensaje", "❌ Error al desasignar estudiante: " + e.getMessage());
            try (Connection conn = Conexion.getConnection()) {
                conn.rollback();
            } catch (Exception ex) {
                System.err.println("⚠️ Error en rollback: " + ex.getMessage());
            }
        }

        response.sendRedirect(request.getContextPath() + "/AsignarEstudiantesServlet?docenteId=" + docenteId + "&instrumento=" + instrumento);
    }
}