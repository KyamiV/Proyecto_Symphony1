/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.mysymphony.proyecto_symphony1.servlet;

import com.mysymphony.proyecto_symphony1.dao.AsignacionDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/EditarAsignacionServlet")
public class EditarAsignacionServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession sesion = request.getSession();
        String rol = (String) sesion.getAttribute("rolActivo");

        if (rol == null || !"docente".equalsIgnoreCase(rol)) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        try {
            int idAsignacion = Integer.parseInt(request.getParameter("idAsignacion"));
            String nuevoInstrumento = request.getParameter("instrumento");

            AsignacionDAO dao = new AsignacionDAO();
            boolean actualizado = dao.actualizarInstrumentoAsignado(idAsignacion, nuevoInstrumento);

            if (actualizado) {
                request.setAttribute("mensaje", "Instrumento actualizado correctamente.");
            } else {
                request.setAttribute("mensaje", "No se pudo actualizar el instrumento.");
            }

        } catch (Exception e) {
            request.setAttribute("mensaje", "Error al procesar la edición: " + e.getMessage());
        }

        request.getRequestDispatcher("/docente/verAsignaciones.jsp").forward(request, response);
    }
}