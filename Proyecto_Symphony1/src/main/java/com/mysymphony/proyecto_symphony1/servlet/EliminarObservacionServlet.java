/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mysymphony.proyecto_symphony1.servlet;

/**
 * Rol: Docente
 * Servlet para eliminar observaciones pedagógicas.
 * Recibe el idObservacion desde el formulario de la tabla.
 * Autor: Camila
 * Trazabilidad: valida sesión, elimina observación y guarda auditoría y bitácora institucional.
 */

import com.mysymphony.proyecto_symphony1.dao.ObservacionesDAO;
import com.mysymphony.proyecto_symphony1.dao.AuditoriaDAO;
import com.mysymphony.proyecto_symphony1.dao.BitacoraDAO;
import com.mysymphony.proyecto_symphony1.util.Conexion;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.util.*;

@WebServlet("/EliminarObservacionServlet")
public class EliminarObservacionServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession sesion = request.getSession(false);
        String rol = (sesion != null) ? (String) sesion.getAttribute("rolActivo") : null;
        Integer idDocente = (sesion != null) ? (Integer) sesion.getAttribute("idActivo") : null;
        String nombreDocente = (sesion != null) ? (String) sesion.getAttribute("nombreActivo") : "desconocido";

        if (rol == null || !"docente".equalsIgnoreCase(rol) || idDocente == null) {
            request.setAttribute("tipoMensaje", "danger");
            request.setAttribute("mensaje", "⚠️ Acceso restringido: requiere rol docente.");
            request.getRequestDispatcher("/error.jsp").forward(request, response);
            return;
        }

        String idObsStr = request.getParameter("idObservacion");
        int idObservacion = 0;
        try {
            idObservacion = Integer.parseInt(idObsStr);
        } catch (NumberFormatException e) {
            sesion.setAttribute("tipoMensaje", "danger");
            sesion.setAttribute("mensaje", "⚠️ ID de observación inválido.");
            response.sendRedirect(request.getContextPath() + "/ObservacionesServlet");
            return;
        }

        try (Connection conn = Conexion.getConnection()) {
            ObservacionesDAO obsDAO = new ObservacionesDAO(conn);
            boolean exito = obsDAO.eliminarObservacion(idObservacion);

            if (exito) {
                sesion.setAttribute("tipoMensaje", "success");
                sesion.setAttribute("mensaje", "✔ Observación eliminada exitosamente.");

                // 🛡️ Auditoría institucional
                Map<String, String> registro = new HashMap<>();
                registro.put("usuario", nombreDocente + " (ID: " + idDocente + ")");
                registro.put("rol", rol);
                registro.put("accion", "Eliminó observación con ID " + idObservacion);
                registro.put("modulo", "Gestión de observaciones");
                registro.put("ip_origen", request.getRemoteAddr());
                new AuditoriaDAO(conn).registrarAccion(registro);

                // 📖 Bitácora institucional
                new BitacoraDAO(conn).registrarAccion(
                        "Docente eliminó observación con ID " + idObservacion,
                        nombreDocente, rol, "Gestión de observaciones"
                );
            } else {
                sesion.setAttribute("tipoMensaje", "danger");
                sesion.setAttribute("mensaje", "❌ No se pudo eliminar la observación.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            sesion.setAttribute("tipoMensaje", "danger");
            sesion.setAttribute("mensaje", "❌ Error al eliminar observación: " + e.getMessage());
        }

        response.sendRedirect(request.getContextPath() + "/VerObservacionesServlet");
    }
}