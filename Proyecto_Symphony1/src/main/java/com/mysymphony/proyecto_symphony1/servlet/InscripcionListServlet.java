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
import java.util.List;

@WebServlet(name = "InscripcionListServlet", urlPatterns = {"/InscripcionListServlet"})
public class InscripcionListServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String filtro = request.getParameter("filtro");
        InscripcionDAO dao = new InscripcionDAO();
        List<Inscripcion> lista;

        if (filtro != null && !filtro.trim().isEmpty()) {
            lista = dao.buscar(filtro);
        } else {
            lista = dao.listar();
        }

        request.setAttribute("inscripciones", lista);
        request.getRequestDispatcher("listaInscripciones.jsp").forward(request, response);
    }
}