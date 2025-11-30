/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.mysymphony.proyecto_symphony1.servlet;

/**
 * Servlet para inscribir al estudiante en una clase institucional.
 * Rol: estudiante
 * Autor: Camila
 * Trazabilidad: valida sesión, registra inscripción, evita duplicados y audita acción
 */

import com.mysymphony.proyecto_symphony1.util.Conexion;
import com.mysymphony.proyecto_symphony1.dao.AuditoriaDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/InscripcionClaseServlet")
public class InscripcionClaseServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession sesion = request.getSession(false);
        String rol = (sesion != null) ? (String) sesion.getAttribute("rolActivo") : null;
        String estudianteNombre = (sesion != null) ? (String) sesion.getAttribute("nombreActivo") : null;
        Integer idEstudiante = (sesion != null) ? (Integer) sesion.getAttribute("idActivo") : null;

        if (rol == null || !"estudiante".equalsIgnoreCase(rol) || idEstudiante == null) {
            sesion.setAttribute("tipoMensaje", "warning");
            sesion.setAttribute("mensaje", "⚠️ Acceso restringido: requiere rol estudiante.");
            response.sendRedirect(request.getContextPath() + "/VerClasesEstudianteServlet");
            return;
        }

        String claseIdStr = request.getParameter("claseId");
        int claseId;
        try {
            claseId = Integer.parseInt(claseIdStr);
        } catch (NumberFormatException e) {
            sesion.setAttribute("tipoMensaje", "danger");
            sesion.setAttribute("mensaje", "❌ Clase no válida.");
            response.sendRedirect(request.getContextPath() + "/VerClasesEstudianteServlet");
            return;
        }

        try (Connection conn = Conexion.getConnection()) {

            String validarSQL = "SELECT COUNT(*) FROM inscripciones_clase WHERE id_estudiante = ? AND id_clase = ?";
            try (PreparedStatement psVal = conn.prepareStatement(validarSQL)) {
                psVal.setInt(1, idEstudiante);
                psVal.setInt(2, claseId);
                try (ResultSet rs = psVal.executeQuery()) {
                    if (rs.next() && rs.getInt(1) > 0) {
                        sesion.setAttribute("tipoMensaje", "warning");
                        sesion.setAttribute("mensaje", "⚠️ Ya estás inscrito en esta clase.");
                        response.sendRedirect(request.getContextPath() + "/VerClasesEstudianteServlet");
                        return;
                    }
                }
            }

            String insertarSQL = "INSERT INTO inscripciones_clase (id_estudiante, id_clase, fecha_inscripcion) VALUES (?, ?, CURRENT_TIMESTAMP)";
            try (PreparedStatement psIns = conn.prepareStatement(insertarSQL)) {
                psIns.setInt(1, idEstudiante);
                psIns.setInt(2, claseId);
                psIns.executeUpdate();
            }

            Map<String, String> registro = new HashMap<>();
            registro.put("usuario", estudianteNombre);
            registro.put("rol", rol);
            registro.put("modulo", "Inscripción");
            registro.put("accion", "Se inscribió en clase ID " + claseId);
            registro.put("ip_origen", request.getRemoteAddr());
            new AuditoriaDAO(conn).registrarAccion(registro);

            sesion.setAttribute("tipoMensaje", "success");
            sesion.setAttribute("mensaje", "✅ Inscripción realizada correctamente.");
            response.sendRedirect(request.getContextPath() + "/VerClasesEstudianteServlet");

        } catch (Exception e) {
            e.printStackTrace();
            sesion.setAttribute("tipoMensaje", "danger");
            sesion.setAttribute("mensaje", "❌ Error al realizar la inscripción: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/VerClasesEstudianteServlet");
        }
    }
}