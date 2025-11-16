/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mysymphony.proyecto_symphony1.servlet;

import com.mysymphony.proyecto_symphony1.dao.NotaDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/InsertarNotaServlet")
public class InsertarNotaServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession sesion = request.getSession();
        String rol = (String) sesion.getAttribute("rolActivo");
        String docente = (String) sesion.getAttribute("nombreActivo");

        if (rol == null || !"docente".equalsIgnoreCase(rol)) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        // Obtener parámetros del formulario
        String estudiante = request.getParameter("nombreEstudiante");
        String instrumento = request.getParameter("curso"); // curso = instrumento
        String etapa = request.getParameter("etapa");
        String notaStr = request.getParameter("nota");
        String observaciones = request.getParameter("observaciones");

        if (estudiante != null && instrumento != null && etapa != null && notaStr != null) {
            try {
                double nota = Double.parseDouble(notaStr);

                NotaDAO dao = new NotaDAO();
                boolean registrado = dao.registrarNota(estudiante, instrumento, etapa, nota, observaciones, docente);

                if (registrado) {
                    request.setAttribute("mensaje", "✅ Nota registrada correctamente.");
                } else {
                    request.setAttribute("mensaje", "❌ No se pudo registrar la nota.");
                }
            } catch (NumberFormatException e) {
                request.setAttribute("mensaje", "❌ Error al convertir la nota.");
                e.printStackTrace();
            }
        } else {
            request.setAttribute("mensaje", "❌ Faltan datos para registrar la nota.");
        }

        request.getRequestDispatcher("/docente/registrarNotas.jsp").forward(request, response);
    }
}