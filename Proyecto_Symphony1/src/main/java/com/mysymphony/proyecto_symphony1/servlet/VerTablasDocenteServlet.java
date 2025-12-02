/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.mysymphony.proyecto_symphony1.servlet;

import com.mysymphony.proyecto_symphony1.util.Conexion;
import com.mysymphony.proyecto_symphony1.dao.TablasNotasDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.util.List;
import java.util.Map;

/**
 * Servlet para listar todas las tablas institucionales creadas por un docente.
 * Rol: docente
 * Autor: Camila
 * Trazabilidad:
 *   - Valida sesión y rol
 *   - Consulta tablas guardadas en BD
 *   - Envía resultados al JSP verTablas.jsp
 */
@WebServlet("/VerTablasDocenteServlet")
public class VerTablasDocenteServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession sesion = request.getSession(false);
        if (sesion == null || sesion.getAttribute("rolActivo") == null ||
            !"docente".equalsIgnoreCase((String) sesion.getAttribute("rolActivo"))) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        Integer idDocente = (Integer) sesion.getAttribute("idActivo");
        List<Map<String,String>> tablas = null;

        try (Connection conn = Conexion.getConnection()) {
            TablasNotasDAO dao = new TablasNotasDAO(conn);
            tablas = dao.listarTablasPorDocente(idDocente);

            if (tablas == null || tablas.isEmpty()) {
                request.setAttribute("mensaje", "⚠️ No se pudo enviar la tabla. Verifique que tenga notas registradas.");
                request.setAttribute("tipoMensaje", "warning");
            } else {
                request.setAttribute("mensaje", "✔ Tablas cargadas correctamente.");
                request.setAttribute("tipoMensaje", "success");
            }

        } catch (Exception e) {
            e.printStackTrace();
            sesion.setAttribute("mensaje", "❌ Error al cargar tablas: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/error.jsp");
            return;
        }

        request.setAttribute("tablas", tablas);
        request.getRequestDispatcher("/docente/verTablas.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Redirigir a doGet para mantener consistencia
        doGet(request, response);
    }
}