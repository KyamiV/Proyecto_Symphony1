/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mysymphony.proyecto_symphony1.servlet;

/**
 * Servlet: ListarEstudiantesClaseServlet
 * Rol: Administrador / Docente
 * Autor: Camila
 * Creado: 29/11/2025
 *
 * Propósito:
 *   - Listar estudiantes inscritos en una clase específica.
 *   - Validar sesión y rol activo (administrador o docente).
 *   - Consultar estudiantes desde la BD usando EstudianteDAO.
 *   - Responder en formato JSON para integración con vistas dinámicas.
 */

import com.mysymphony.proyecto_symphony1.dao.EstudianteDAO;
import com.mysymphony.proyecto_symphony1.modelo.Estudiante;
import com.mysymphony.proyecto_symphony1.util.Conexion;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.util.List;

@WebServlet("/ListarEstudiantesClaseServlet")
public class ListarEstudiantesClaseServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession sesion = request.getSession(false);
        String rol = (sesion != null) ? (String) sesion.getAttribute("rolActivo") : null;

        // 🔐 Validar rol administrador o docente
        if (rol == null || (!"administrador".equalsIgnoreCase(rol) && !"docente".equalsIgnoreCase(rol))) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Acceso denegado");
            return;
        }

        // 📌 Validar parámetro id de clase
        int idClase;
        try {
            idClase = Integer.parseInt(request.getParameter("id"));
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID de clase inválido");
            return;
        }

        response.setContentType("application/json;charset=UTF-8");

        try (Connection conn = Conexion.getConnection()) {
            EstudianteDAO dao = new EstudianteDAO(conn);
            List<Estudiante> estudiantes = dao.listarPorClase(idClase);

            // 📤 Convertir lista a JSON
            String json = new Gson().toJson(estudiantes);
            response.getWriter().write(json);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error al consultar estudiantes");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // ✅ Delegar en doGet para soportar POST sin error 405
        doGet(request, response);
    }
}