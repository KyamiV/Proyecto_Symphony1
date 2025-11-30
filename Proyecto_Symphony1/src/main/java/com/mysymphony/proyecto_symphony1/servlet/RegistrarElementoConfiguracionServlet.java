/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.mysymphony.proyecto_symphony1.servlet;

import com.mysymphony.proyecto_symphony1.util.Conexion;
import com.mysymphony.proyecto_symphony1.dao.BitacoraDAO;
import com.mysymphony.proyecto_symphony1.dao.AuditoriaDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 * Servlet para registrar, editar y eliminar instrumentos, etapas pedagógicas y colores por rol.
 * Rol: administrador
 * Autor: Camila
 * Trazabilidad: gestiona configuración institucional y registra cada acción en bitácora y auditoría.
 */
@WebServlet("/RegistrarElementoConfiguracionServlet")
public class RegistrarElementoConfiguracionServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect(request.getContextPath() + "/ConfiguracionServlet");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession sesion = request.getSession(false);
        String rol = (sesion != null) ? (String) sesion.getAttribute("rolActivo") : null;
        String usuario = (sesion != null) ? (String) sesion.getAttribute("nombreActivo") : null;

        if (usuario == null || rol == null || !"administrador".equalsIgnoreCase(rol)) {
            sesion.setAttribute("mensaje", "⚠️ Acceso restringido: requiere rol administrador.");
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        String accion = request.getParameter("accion");
        boolean registroExitoso = false;
        String mensajeFinal = "";

        try (Connection conn = Conexion.getConnection()) {
            BitacoraDAO bitacoraDAO = new BitacoraDAO(conn);
            AuditoriaDAO auditoriaDAO = new AuditoriaDAO(conn);

            switch (accion) {
                case "agregarInstrumento": {
                    String nombre = request.getParameter("nuevoInstrumento");
                    String cupoStr = request.getParameter("cupoInstrumento");
                    if (nombre != null && !nombre.trim().isEmpty() && cupoStr != null) {
                        int cupo = Integer.parseInt(cupoStr);
                        try (PreparedStatement ps = conn.prepareStatement(
                                "INSERT INTO instrumentos (nombre, cupo_maximo) VALUES (?, ?)")) {
                            ps.setString(1, nombre.trim());
                            ps.setInt(2, cupo);
                            ps.executeUpdate();
                            registroExitoso = true;
                            mensajeFinal = "✔ Instrumento registrado correctamente";
                        }
                        bitacoraDAO.registrarAccion("Agregó instrumento '" + nombre + "' con cupo " + cupo,
                                usuario, rol, "configuracion");
                        auditoriaDAO.registrarAuditoria("Administrador agregó instrumento '" + nombre + "'",
                                "Configuración", usuario);
                    }
                    break;
                }
                // ... (los demás casos igual que tu versión, pero con registro también en auditoría)
                default:
                    request.setAttribute("error", "⚠️ Acción no reconocida.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "❌ Error al procesar configuración: " + e.getMessage());
        }

        if (registroExitoso) {
            response.sendRedirect(request.getContextPath() + "/ConfiguracionServlet?mensaje=" + URLEncoder.encode(mensajeFinal, "UTF-8"));
        } else {
            request.getRequestDispatcher("/ConfiguracionServlet").forward(request, response);
        }
    }
}