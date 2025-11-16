/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.mysymphony.proyecto_symphony1.servlet;

/**
 * Servlet para consultar las asignaciones de un docente.
 * Autor: camiv
 */

import com.mysymphony.proyecto_symphony1.dao.AsignacionDAO;
import com.mysymphony.proyecto_symphony1.dao.UsuarioDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.*;

@WebServlet("/VerAsignacionesServlet")
public class VerAsignacionesServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession sesion = request.getSession();
        String rol = (String) sesion.getAttribute("rolActivo");
        String nombreDocente = (String) sesion.getAttribute("nombreActivo");

        if (rol == null || !"docente".equalsIgnoreCase(rol) || nombreDocente == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        UsuarioDAO usuarioDAO = new UsuarioDAO();
        int idDocente = usuarioDAO.obtenerIdPorNombre(nombreDocente);

        AsignacionDAO dao = new AsignacionDAO();
        List<Map<String, String>> asignaciones = dao.obtenerAsignacionesPorDocente(idDocente);

        request.setAttribute("asignaciones", asignaciones);
        request.getRequestDispatcher("/docente/verAsignaciones.jsp").forward(request, response);
    }
}