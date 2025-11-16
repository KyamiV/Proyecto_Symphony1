/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.mysymphony.proyecto_symphony1.servlet;

import com.mysymphony.proyecto_symphony1.dao.NotaDAO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import javax.servlet.RequestDispatcher;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet("/ActualizarNotaServlet") // ← Esto es opcional si usas web.xml, pero recomendable
public class ActualizarNotaServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession sesion = request.getSession();
        String docente = (String) sesion.getAttribute("nombreActivo");

        String estudiante = request.getParameter("estudiante");
        String instrumento = request.getParameter("instrumento");
        String etapa = request.getParameter("etapa");
        String notaStr = request.getParameter("nota");
        String observaciones = request.getParameter("observaciones");

        double nota = 0;
        String mensaje = "";

        try {
            nota = Double.parseDouble(notaStr);
        } catch (NumberFormatException e) {
            mensaje = "❌ Error al convertir la nota.";
        }

        NotaDAO dao = new NotaDAO();
        boolean actualizado = dao.actualizarNota(estudiante, instrumento, etapa, nota, observaciones);

        if (actualizado) {
            mensaje = "✅ Nota actualizada correctamente.";
        } else {
            mensaje = "❌ No se pudo actualizar la nota.";
        }

        List<Map<String, String>> notas = dao.obtenerNotasPorDocente(docente);
        request.setAttribute("mensaje", mensaje);
        request.setAttribute("notas", notas);

        RequestDispatcher dispatcher = request.getRequestDispatcher("/docente/verNotasDocente.jsp");
        dispatcher.forward(request, response);
    }
}