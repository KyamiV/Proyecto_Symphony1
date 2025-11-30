/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mysymphony.proyecto_symphony1.servlet;

import com.mysymphony.proyecto_symphony1.dao.BitacoraDAO;
import com.mysymphony.proyecto_symphony1.dao.AuditoriaDAO;
import com.mysymphony.proyecto_symphony1.util.Conexion;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * Servlet institucional para visualizar registros de bitácora.
 * Rol: Administrador / Coordinador
 * Función: Consultar acciones registradas en la bitácora y enviarlas al JSP.
 * Autor: camiv
 */
@WebServlet("/VerBitacoraServlet")
public class VerBitacoraServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        final HttpSession sesion = request.getSession(false);
        final String rol = (sesion != null) ? (String) sesion.getAttribute("rolActivo") : null;
        final String usuario = (sesion != null) ? (String) sesion.getAttribute("nombreActivo") : "desconocido";

        // 🔐 Validación de rol administrador o coordinador
        if (rol == null || !(rol.equalsIgnoreCase("administrador") || rol.equalsIgnoreCase("coordinador"))) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        try (Connection conn = Conexion.getConnection()) {
            BitacoraDAO bitacoraDAO = new BitacoraDAO(conn);
            AuditoriaDAO auditoriaDAO = new AuditoriaDAO(conn);

            // 📌 Consultar todos los registros de bitácora
            List<Map<String, String>> registros = bitacoraDAO.obtenerTodos();
            request.setAttribute("bitacora", registros);

            // 📝 Registrar acción en auditoría
            Map<String, String> registro = new HashMap<>();
            registro.put("usuario", usuario);
            registro.put("rol", rol);
            registro.put("modulo", "Bitácora");
            registro.put("accion", "Ver bitácora");
            registro.put("detalle", "Se consultaron " + registros.size() + " registros de bitácora.");
            registro.put("ip_origen", request.getRemoteAddr());

            auditoriaDAO.registrarAccion(registro);

            // 👉 Forward al JSP institucional
            request.getRequestDispatcher("/administrador/bitacora.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            if (sesion != null) {
                sesion.setAttribute("mensaje", "❌ Error al consultar bitácora: " + e.getMessage());
                sesion.setAttribute("tipoMensaje", "error");
            }
            response.sendRedirect(request.getContextPath() + "/administrador/bitacora.jsp");
        }
    }
}