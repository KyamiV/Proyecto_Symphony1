/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.mysymphony.proyecto_symphony1.servlet;

import com.mysymphony.proyecto_symphony1.dao.AsignacionDAO;
import com.mysymphony.proyecto_symphony1.dao.AuditoriaDAO;
import com.mysymphony.proyecto_symphony1.util.Conexion;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

/**
 * Servlet institucional para emisión de certificados.
 * Rol: Administrador
 * Función: Emitir certificados pendientes y registrar trazabilidad.
 * Autor: camiv
 */
@WebServlet("/EmitirCertificadosServlet")
public class EmitirCertificadosServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession sesion = request.getSession(false);
        String rol = (sesion != null) ? (String) sesion.getAttribute("rolActivo") : null;

        if (rol == null || !"administrador".equalsIgnoreCase(rol)) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        request.getRequestDispatcher("/administrador/emitirCertificados.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession sesion = request.getSession(false);
        String rol = (sesion != null) ? (String) sesion.getAttribute("rolActivo") : null;
        String usuario = (sesion != null) ? (String) sesion.getAttribute("nombreActivo") : "desconocido";

        if (rol == null || !"administrador".equalsIgnoreCase(rol)) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        try (Connection conn = Conexion.getConnection()) {
            AsignacionDAO asignacionDAO = new AsignacionDAO(conn);
            AuditoriaDAO auditoriaDAO = new AuditoriaDAO(conn);

            int certificadosEmitidos = asignacionDAO.emitirCertificadosPendientes(usuario);

            Map<String, String> registro = new HashMap<>();
            registro.put("usuario", usuario);
            registro.put("rol", rol);
            registro.put("modulo", "Certificados");
            registro.put("accion", "Emitir certificados");
            registro.put("detalle", "Se emitieron " + certificadosEmitidos + " certificados pendientes.");
            registro.put("ip_origen", request.getRemoteAddr());

            auditoriaDAO.registrarAccion(registro);

            sesion.setAttribute("mensaje", "✅ Se emitieron " + certificadosEmitidos + " certificados correctamente.");
            sesion.setAttribute("tipoMensaje", "success");

            // Volver a la vista de emisión de certificados
            request.getRequestDispatcher("/administrador/emitirCertificados.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            if (sesion != null) {
                sesion.setAttribute("mensaje", "❌ Error al emitir certificados: " + e.getMessage());
                sesion.setAttribute("tipoMensaje", "error");
            }
            request.getRequestDispatcher("/administrador/emitirCertificados.jsp").forward(request, response);
        }
    }
}