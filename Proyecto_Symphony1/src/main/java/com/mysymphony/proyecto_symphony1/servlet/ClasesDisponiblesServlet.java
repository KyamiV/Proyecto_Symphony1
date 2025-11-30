/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.mysymphony.proyecto_symphony1.servlet;

import com.mysymphony.proyecto_symphony1.dao.ClaseDAO;
import com.mysymphony.proyecto_symphony1.modelo.Clase;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/ClasesDisponiblesServlet")
public class ClasesDisponiblesServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Connection conn = (Connection) getServletContext().getAttribute("DBConnection");
        ClaseDAO claseDAO = new ClaseDAO(conn);

        try {
            List<Clase> clasesDisponibles = claseDAO.obtenerClasesDisponibles();
            request.setAttribute("clasesDisponibles", clasesDisponibles);
            request.getRequestDispatcher("/gestionarClasesPrincipal.jsp").forward(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "Error al cargar las clases disponibles.");
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }
}