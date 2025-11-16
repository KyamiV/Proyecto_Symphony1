/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.mysymphony.proyecto_symphony1.servlet;

/**
 * Panel principal del docente con indicadores institucionales.
 * Autor: camiv
 */

import com.mysymphony.proyecto_symphony1.dao.NotaDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.*;

@WebServlet("/DashboardServlet")
public class DashboardServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession sesion = request.getSession();
        String nombre = (String) sesion.getAttribute("nombreActivo");
        String rol = (String) sesion.getAttribute("rolActivo");

        // Validación de sesión y rol
        if (nombre == null || rol == null || !"docente".equalsIgnoreCase(rol)) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        // Obtener notas registradas por el docente
        NotaDAO dao = new NotaDAO();
        List<Map<String, String>> notas = dao.obtenerNotasPorDocente(nombre);

        // Calcular indicadores únicos
        Set<String> estudiantesUnicos = new HashSet<>();
        Set<String> instrumentosUnicos = new HashSet<>();

        for (Map<String, String> fila : notas) {
            String estudiante = fila.get("estudiante");
            String instrumento = fila.get("instrumento");
            if (estudiante != null && !estudiante.isEmpty()) {
                estudiantesUnicos.add(estudiante);
            }
            if (instrumento != null && !instrumento.isEmpty()) {
                instrumentosUnicos.add(instrumento);
            }
        }

        // Enviar datos a la vista
        request.setAttribute("totalNotas", notas.size());
        request.setAttribute("totalEstudiantes", estudiantesUnicos.size());
        request.setAttribute("totalInstrumentos", instrumentosUnicos.size());
        request.setAttribute("iconoRol", "fas fa-chalkboard-teacher");
        request.setAttribute("claseBoton", "btn-docente");

        // Redirigir a la vista ubicada en /webpages/
        request.getRequestDispatcher("/dashboard.jsp").forward(request, response);
    }
}