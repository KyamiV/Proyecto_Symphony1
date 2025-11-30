/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.mysymphony.proyecto_symphony1.servlet;

/**
 * Servlet para visualizar el estado de una tabla institucional de notas con trazabilidad
 * Rol: docente
 * Autor: Camila
 * Trazabilidad: valida sesión, consulta estado y registra acceso en auditoría y bitácora
 */

import com.mysymphony.proyecto_symphony1.dao.TablasNotasDAO;
import com.mysymphony.proyecto_symphony1.dao.AuditoriaDAO;
import com.mysymphony.proyecto_symphony1.dao.BitacoraDAO;
import com.mysymphony.proyecto_symphony1.util.Conexion;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.util.*;

@WebServlet("/VerEstadoTablaServlet")
public class VerEstadoTablaServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession sesion = request.getSession(false);
        String rol = (sesion != null) ? (String) sesion.getAttribute("rolActivo") : null;
        Integer idDocente = (sesion != null) ? (Integer) sesion.getAttribute("idDocente") : null;

        if (rol == null || !"docente".equalsIgnoreCase(rol) || idDocente == null) {
            if (sesion != null) {
                sesion.setAttribute("mensaje", "⚠️ Acceso restringido: requiere rol docente.");
            }
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        String tablaIdParam = request.getParameter("tablaId");
        if (tablaIdParam == null) {
            request.setAttribute("mensaje", "⚠️ No se especificó la tabla.");
            request.getRequestDispatcher("/VerTablasGuardadasServlet").forward(request, response);
            return;
        }

        int tablaId;
        try {
            tablaId = Integer.parseInt(tablaIdParam);
            if (tablaId <= 0) {
                throw new NumberFormatException("ID inválido");
            }
        } catch (NumberFormatException e) {
            request.setAttribute("mensaje", "❌ El ID de tabla no es válido.");
            request.getRequestDispatcher("/VerTablasGuardadasServlet").forward(request, response);
            return;
        }

        try (Connection conn = Conexion.getConnection()) {
            TablasNotasDAO dao = new TablasNotasDAO(conn);
            Map<String, String> estadoTabla = dao.obtenerEstadoTabla(tablaId);

            if (estadoTabla == null || estadoTabla.isEmpty()) {
                request.setAttribute("mensaje", "⚠️ No se encontró información de la tabla.");
            } else {
                request.setAttribute("estadoTabla", estadoTabla);

                // 🔹 Registrar acción en auditoría
                Map<String, String> registro = new HashMap<>();
                registro.put("usuario", String.valueOf(idDocente));
                registro.put("rol", rol);
                registro.put("modulo", "Tablas guardadas");
                registro.put("accion", "Consultó el estado de la tabla con ID " + tablaId);
                registro.put("tabla_id", String.valueOf(tablaId));
                registro.put("referencia_id", String.valueOf(idDocente));
                registro.put("ip_origen", request.getRemoteAddr());

                AuditoriaDAO auditoriaDAO = new AuditoriaDAO(conn);
                auditoriaDAO.registrarAccion(registro);

                // 📖 Registrar acción en bitácora institucional
                BitacoraDAO bitacoraDAO = new BitacoraDAO(conn);
                bitacoraDAO.registrarAccion("Docente consultó estado de tabla institucional con ID " + tablaId,
                        String.valueOf(idDocente), rol, "Tablas guardadas");
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("mensaje", "❌ Error al consultar el estado de la tabla.");
        }

        request.getRequestDispatcher("/docente/estadoTabla.jsp").forward(request, response);
    }
}