/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.mysymphony.proyecto_symphony1.servlet;

import com.mysymphony.proyecto_symphony1.dao.NotaDAO;
import com.mysymphony.proyecto_symphony1.util.Conexion;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import javax.servlet.RequestDispatcher;
import java.io.IOException;
import java.sql.*;
import java.util.*;

@WebServlet("/RegistrarNotaServlet")
public class RegistrarNotaServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HttpSession sesion = request.getSession();
        String rol = (String) sesion.getAttribute("rolActivo");
        String docente = (String) sesion.getAttribute("nombreActivo");

        if (rol == null || !"docente".equalsIgnoreCase(rol)) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        String idEstudiante = request.getParameter("idEstudiante");
        String notaStr = request.getParameter("nota");
        String observaciones = request.getParameter("obs");
        String fecha = request.getParameter("fecha");

        String mensaje = "";

        try {
            double nota = Double.parseDouble(notaStr);

            String nombreEstudiante = null;

            String sql = "SELECT nombre FROM usuarios WHERE id_usuario = ? AND rol = 'estudiante'";
            try (Connection con = Conexion.getConnection();
                 PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setString(1, idEstudiante);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        nombreEstudiante = rs.getString("nombre");
                    }
                }
            }

            if (nombreEstudiante != null) {
                NotaDAO dao = new NotaDAO();
                boolean registrado = dao.registrarNota(nombreEstudiante, "Instrumento no definido", nota, fecha);

                mensaje = registrado
                        ? "✅ Nota registrada correctamente."
                        : "❌ No se pudo guardar la nota.";
            } else {
                mensaje = "❌ El estudiante no está registrado como usuario.";
            }

        } catch (NumberFormatException e) {
            mensaje = "❌ Error al convertir la nota.";
            e.printStackTrace();
        } catch (Exception e) {
            mensaje = "❌ Error al registrar la nota.";
            e.printStackTrace();
        }

        // Recargar lista de estudiantes
        NotaDAO dao = new NotaDAO();
        List<Map<String, String>> estudiantes = dao.obtenerEstudiantesRegistrados();
        request.setAttribute("estudiantes", estudiantes);
        request.setAttribute("mensaje", mensaje);

        RequestDispatcher dispatcher = request.getRequestDispatcher("/docente/registrarNotas.jsp");
        dispatcher.forward(request, response);
    }
}