/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mysymphony.proyecto_symphony1.servlet;

/**
 *
 * @author camiv
 */

import com.mysymphony.proyecto_symphony1.dao.AsignacionDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/AsignarEstudianteServlet")
public class AsignarEstudianteServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Obtener parámetros del formulario
        int idDocente = Integer.parseInt(request.getParameter("idDocente"));
        int idEstudiante = Integer.parseInt(request.getParameter("idEstudiante"));
        String instrumento = request.getParameter("instrumento");

        // Registrar asignación
        AsignacionDAO dao = new AsignacionDAO();
        dao.asignarEstudiante(idDocente, idEstudiante, instrumento);

        // Redirigir al panel docente
        response.sendRedirect(request.getContextPath() + "/DashboardServlet");
    }
}