/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.mysymphony.proyecto_symphony1.servlet;

import com.mysymphony.proyecto_symphony1.dao.InscripcionDAO;
import com.mysymphony.proyecto_symphony1.modelo.Inscripcion;
import com.mysymphony.proyecto_symphony1.util.Conexion;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.util.List;

/**
 * Servlet institucional para visualizar estudiantes inscritos en una clase desde la tabla de certificación.
 * Autor: camiv
 * Flujo: recibe idClase desde el botón del JSP, valida rol administrador,
 * consulta DAO y envía datos a verEstudiantes.jsp.
 */
@WebServlet("/VerEstudiantesClaseServlet")
public class VerEstudiantesClaseServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession sesion = request.getSession(false);
        String rol = (sesion != null) ? (String) sesion.getAttribute("rolActivo") : null;

        // 🔐 Validación de rol administrador
        if (rol == null || !"administrador".equalsIgnoreCase(rol)) {
            request.setAttribute("mensaje", "⚠️ Acceso restringido: requiere rol administrador.");
            request.setAttribute("tipoMensaje", "warning");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
            return;
        }

        // 📌 Validación de parámetro idClase
        String idParam = request.getParameter("idClase");
        if (idParam == null || idParam.isEmpty()) {
            request.setAttribute("mensaje", "❌ Error: parámetro 'idClase' vacío.");
            request.setAttribute("tipoMensaje", "danger");
            request.getRequestDispatcher("/administrador/gestionarClasesPrincipal.jsp").forward(request, response);
            return;
        }

        int idClase = Integer.parseInt(idParam);

        try (Connection conn = Conexion.getConnection()) {
            InscripcionDAO inscripcionDAO = new InscripcionDAO(conn);

            // 🔎 Consultar inscripciones con JOIN a usuarios para traer estado
            List<Inscripcion> inscripciones = inscripcionDAO.listarInscripcionesPorClase(idClase);
            int totalInscritos = inscripciones.size();

            // 📤 Enviar datos a la vista
            request.setAttribute("idClase", idClase);
            request.setAttribute("inscripciones", inscripciones);
            request.setAttribute("totalInscritos", totalInscritos);

            request.getRequestDispatcher("/administrador/verEstudiantes.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("mensaje", "❌ Error al cargar estudiantes: " + e.getMessage());
            request.setAttribute("tipoMensaje", "danger");
            request.getRequestDispatcher("/administrador/gestionarClasesPrincipal.jsp").forward(request, response);
        }
    }
}