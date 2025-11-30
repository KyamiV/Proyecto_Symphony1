/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mysymphony.proyecto_symphony1.servlet;

import com.mysymphony.proyecto_symphony1.util.Conexion;
import com.mysymphony.proyecto_symphony1.dao.ObservacionesDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.util.List;
import java.util.Map;

@WebServlet("/VerObservacionesServlet")
public class VerObservacionesServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 🔐 Validar sesión y rol docente
        HttpSession sesion = request.getSession(false);
        if (sesion == null || sesion.getAttribute("rolActivo") == null ||
                !"docente".equalsIgnoreCase((String) sesion.getAttribute("rolActivo"))) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        // 🧠 Obtener nombre del docente desde sesión (para filtrar observaciones)
        String nombreDocente = (String) sesion.getAttribute("nombreActivo");

        // 🧠 Obtener ID del docente desde sesión (para listar estudiantes asignados)
        Object idObj = sesion.getAttribute("idActivo");
        Integer idDocente = null;
        if (idObj != null) {
            if (idObj instanceof Integer) {
                idDocente = (Integer) idObj;
            } else {
                try {
                    idDocente = Integer.parseInt(idObj.toString());
                } catch (NumberFormatException e) {
                    idDocente = null;
                }
            }
        }

        try (Connection conn = Conexion.getConnection()) {
            ObservacionesDAO obsDAO = new ObservacionesDAO(conn);

            // 🔎 Estudiantes asignados al docente
            List<Map<String, String>> estudiantes = (idDocente != null)
                    ? obsDAO.listarEstudiantesPorDocente(idDocente)
                    : List.of();
            request.setAttribute("estudiantes", estudiantes);

            // 📋 Observaciones registradas por el docente
            List<Map<String, String>> observaciones = (nombreDocente != null && !nombreDocente.isBlank())
                    ? obsDAO.listarObservacionesPorDocente(nombreDocente)
                    : List.of();
            request.setAttribute("observaciones", observaciones);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("mensaje", "❌ Error al cargar observaciones: " + e.getMessage());
        }

        // 👉 Redirigir al JSP institucional
        request.getRequestDispatcher("/docente/observaciones.jsp").forward(request, response);
    }
}