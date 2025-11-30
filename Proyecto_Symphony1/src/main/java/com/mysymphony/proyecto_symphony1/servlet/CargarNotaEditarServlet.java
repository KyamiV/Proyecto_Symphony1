/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mysymphony.proyecto_symphony1.servlet;

/**
 * Servlet para cargar los datos de una nota antes de editarla.
 * Rol: docente
 * Autor: Camila
 * Trazabilidad:
 *   - Valida sesión
 *   - Consulta nota en BD
 *   - Envía datos a la vista de edición
 *   - Registra acceso en auditoría y bitácora institucional
 */

import com.mysymphony.proyecto_symphony1.dao.NotaDAO;
import com.mysymphony.proyecto_symphony1.dao.AuditoriaDAO;
import com.mysymphony.proyecto_symphony1.dao.BitacoraDAO;
import com.mysymphony.proyecto_symphony1.util.Conexion;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import javax.servlet.RequestDispatcher;
import java.io.IOException;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/CargarNotaEditarServlet")
public class CargarNotaEditarServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {

        HttpSession sesion = request.getSession(false);
        String rol = (sesion != null) ? (String) sesion.getAttribute("rolActivo") : null;
        Integer idDocente = (sesion != null) ? (Integer) sesion.getAttribute("idActivo") : null;
        String nombreDocente = (sesion != null) ? (String) sesion.getAttribute("nombreActivo") : "desconocido";

        // 🔐 Validar rol docente
        if (rol == null || !"docente".equalsIgnoreCase(rol) || idDocente == null) {
            request.setAttribute("mensaje", "⚠️ Acceso restringido: requiere rol docente.");
            request.setAttribute("tipoMensaje", "danger");
            request.getRequestDispatcher("/error.jsp").forward(request, response);
            return;
        }

        int notaId;
        int claseId;
        try {
            notaId = Integer.parseInt(request.getParameter("notaId"));
            claseId = Integer.parseInt(request.getParameter("claseId"));
        } catch (NumberFormatException e) {
            request.setAttribute("mensaje", "⚠️ Parámetros inválidos.");
            request.setAttribute("tipoMensaje", "danger");
            request.getRequestDispatcher("/error.jsp").forward(request, response);
            return;
        }

        try (Connection conn = Conexion.getConnection()) {
            NotaDAO notaDAO = new NotaDAO(conn);
            Map<String, String> nota = notaDAO.obtenerNotaPorId(notaId);

            if (nota != null && !nota.isEmpty()) {
                // 📤 Enviar datos a la vista
                request.setAttribute("nota", nota);
                request.setAttribute("claseId", claseId);

                // 📝 Auditoría institucional
                Map<String, String> registro = new HashMap<>();
                registro.put("usuario", String.valueOf(idDocente));
                registro.put("rol", rol);
                registro.put("accion", "Consultó nota ID " + notaId + " para edición");
                registro.put("modulo", "Notas por clase");
                registro.put("referencia_id", String.valueOf(notaId));
                registro.put("ip_origen", request.getRemoteAddr());
                new AuditoriaDAO(conn).registrarAccion(registro);

                // 📖 Bitácora institucional
                BitacoraDAO bitacoraDAO = new BitacoraDAO(conn);
                bitacoraDAO.registrarAccion("Docente cargó nota ID " + notaId + " para edición",
                        nombreDocente, rol, "Notas por clase");

                RequestDispatcher dispatcher = request.getRequestDispatcher("/docente/editarNota.jsp");
                dispatcher.forward(request, response);

            } else {
                request.setAttribute("mensaje", "⚠️ No se encontró la nota solicitada.");
                request.setAttribute("tipoMensaje", "warning");
                request.getRequestDispatcher("/error.jsp").forward(request, response);
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("mensaje", "❌ Error al cargar la nota: " + e.getMessage());
            request.setAttribute("tipoMensaje", "danger");
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }
}