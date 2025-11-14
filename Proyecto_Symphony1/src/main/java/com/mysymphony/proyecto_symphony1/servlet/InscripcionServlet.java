/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mysymphony.proyecto_symphony1.servlet;

import com.mysymphony.proyecto_symphony1.dao.InscripcionDAO;
import com.mysymphony.proyecto_symphony1.modelo.Inscripcion;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "InscripcionServlet", urlPatterns = {"/InscripcionServlet"})
public class InscripcionServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String estudiante = request.getParameter("estudiante");
        String programa = request.getParameter("programa");
        String fecha = request.getParameter("fecha");

        Inscripcion insc = new Inscripcion(estudiante, programa, fecha);
        InscripcionDAO dao = new InscripcionDAO();
        boolean registrado = dao.registrar(insc);

        if (registrado) {
            request.setAttribute("mensaje", "Inscripción registrada correctamente");
        } else {
            request.setAttribute("mensaje", "Error al registrar inscripción");
        }

        request.getRequestDispatcher("inscripcion.jsp").forward(request, response);
    }
}