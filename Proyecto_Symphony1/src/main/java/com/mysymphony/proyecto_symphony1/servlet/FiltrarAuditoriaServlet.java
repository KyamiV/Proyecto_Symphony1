/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.mysymphony.proyecto_symphony1.servlet;

import com.mysymphony.proyecto_symphony1.dao.AuditoriaDAO;
import com.mysymphony.proyecto_symphony1.modelo.Auditoria;
import com.mysymphony.proyecto_symphony1.util.Conexion;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Servlet institucional para filtrar registros de auditoría por rango de fechas.
 * Rol: Administrador
 * Autor: camiv
 */
@WebServlet("/FiltrarAuditoriaServlet")
public class FiltrarAuditoriaServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession sesion = request.getSession(false);
        String rol = (sesion != null) ? (String) sesion.getAttribute("rolActivo") : null;
        String usuario = (sesion != null) ? (String) sesion.getAttribute("nombreActivo") : "desconocido";

        // 🔐 Validación de rol administrador
        if (rol == null || !"administrador".equalsIgnoreCase(rol)) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        // 📌 Parámetros de fecha
        String fechaInicio = request.getParameter("fechaInicio");
        String fechaFin = request.getParameter("fechaFin");

        try (Connection conn = Conexion.getConnection()) {
            AuditoriaDAO auditoriaDAO = new AuditoriaDAO(conn);

            // 📊 Obtener registros filtrados
            List<Auditoria> registros = auditoriaDAO.filtrarPorFechas(fechaInicio, fechaFin);

            // 📝 Registrar acción en auditoría con Map institucional
            Map<String, String> registro = new HashMap<>();
            registro.put("usuario", usuario);
            registro.put("rol", rol);
            registro.put("modulo", "Auditoría");
            registro.put("accion", "Filtrar auditoría");
            registro.put("detalle", "Filtro aplicado desde " + fechaInicio + " hasta " + fechaFin);
            registro.put("tabla_id", null);
            registro.put("referencia_id", null);

            auditoriaDAO.registrarAccion(registro);

            // Guardar resultados en request para mostrar en JSP
            request.setAttribute("registrosAuditoria", registros);
            request.setAttribute("fechaInicio", fechaInicio);
            request.setAttribute("fechaFin", fechaFin);

            // Redirigir a la vista auditoria.jsp
            request.getRequestDispatcher("/administrador/auditoria.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            if (sesion != null) {
                sesion.setAttribute("mensaje", "❌ Error al filtrar auditoría: " + e.getMessage());
            }
            response.sendRedirect(request.getContextPath() + "/panelAdministrador.jsp");
        }
    }
}