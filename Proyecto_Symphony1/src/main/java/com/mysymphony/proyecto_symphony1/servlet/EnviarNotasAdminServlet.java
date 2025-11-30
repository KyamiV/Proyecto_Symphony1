/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.mysymphony.proyecto_symphony1.servlet;
/**
 * Servlet para enviar una tabla institucional de notas al administrador con trazabilidad.
 * Rol: docente
 * Autor: Camila
 * Trazabilidad:
 *   - Valida sesión
 *   - Verifica notas y estado
 *   - Marca como enviada
 *   - Registra acción en bitácora y auditoría institucional
 */

import com.mysymphony.proyecto_symphony1.util.Conexion;
import com.mysymphony.proyecto_symphony1.dao.TablasNotasDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;

@WebServlet("/EnviarNotasAdminServlet")
public class EnviarNotasAdminServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession sesion = request.getSession(false);
        if (sesion == null || sesion.getAttribute("rolActivo") == null ||
            !"docente".equalsIgnoreCase((String) sesion.getAttribute("rolActivo"))) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        // Validar que el docente esté en sesión
        Integer idDocente = (Integer) sesion.getAttribute("idActivo");
        if (idDocente == null) {
            sesion.setAttribute("mensaje", "❌ No se encontró el docente en sesión.");
            response.sendRedirect(request.getContextPath() + "/VerTablasDocenteServlet");
            return;
        }

        // Validar parámetro tablaId
        String tablaIdStr = request.getParameter("tablaId");
        if (tablaIdStr == null || tablaIdStr.trim().isEmpty()) {
            sesion.setAttribute("mensaje", "❌ No se especificó la tabla.");
            response.sendRedirect(request.getContextPath() + "/VerTablasDocenteServlet");
            return;
        }

        int tablaId;
        try {
            tablaId = Integer.parseInt(tablaIdStr);
        } catch (NumberFormatException e) {
            sesion.setAttribute("mensaje", "❌ El identificador de la tabla no es válido.");
            response.sendRedirect(request.getContextPath() + "/VerTablasDocenteServlet");
            return;
        }

        boolean enviado = false;
        try (Connection conn = Conexion.getConnection()) {
            TablasNotasDAO dao = new TablasNotasDAO(conn);
            enviado = dao.marcarTablaComoEnviada(tablaId, idDocente);
        } catch (Exception e) {
            e.printStackTrace();
            sesion.setAttribute("mensaje", "❌ Error al enviar la tabla: " + e.getMessage());
        }

        if (enviado) {
            sesion.setAttribute("mensaje", "✅ La tabla fue enviada correctamente al administrador.");
        } else {
            sesion.setAttribute("mensaje", "⚠️ No se pudo enviar la tabla. Verifique que tenga notas registradas.");
        }

        response.sendRedirect(request.getContextPath() + "/VerTablasDocenteServlet");
    }
}