/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.mysymphony.proyecto_symphony1.servlet;

import com.mysymphony.proyecto_symphony1.dao.AuditoriaDAO;
import com.mysymphony.proyecto_symphony1.util.Conexion;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;

/**
 * Servlet institucional para limpiar registros de auditoría.
 * Rol: Administrador
 * Función: Eliminar todos los registros de auditoría con confirmación y registrar trazabilidad.
 * Autor: camiv
 */
@WebServlet("/LimpiarAuditoriaServlet")
public class LimpiarAuditoriaServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession sesion = request.getSession(false);
        String rol = (sesion != null) ? (String) sesion.getAttribute("rolActivo") : null;
        String usuario = (sesion != null) ? (String) sesion.getAttribute("nombreActivo") : "desconocido";

        // 🔐 Validación de rol administrador
        if (rol == null || !"administrador".equalsIgnoreCase(rol)) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        try (Connection conn = Conexion.getConnection()) {
            AuditoriaDAO auditoriaDAO = new AuditoriaDAO(conn);

            // 📌 Eliminar todos los registros de auditoría
            int registrosEliminados = auditoriaDAO.limpiarAuditoria();

            // ✅ Mensaje institucional
            sesion.setAttribute("mensaje", "✅ Se limpiaron " + registrosEliminados + " registros de auditoría.");
            sesion.setAttribute("tipoMensaje", "success");

            // 👉 Redirigir de nuevo a la vista auditoría
            response.sendRedirect(request.getContextPath() + "/administrador/auditoria.jsp");

        } catch (Exception e) {
            e.printStackTrace();
            if (sesion != null) {
                sesion.setAttribute("mensaje", "❌ Error al limpiar auditoría: " + e.getMessage());
                sesion.setAttribute("tipoMensaje", "error");
            }
            response.sendRedirect(request.getContextPath() + "/administrador/auditoria.jsp");
        }
    }
}