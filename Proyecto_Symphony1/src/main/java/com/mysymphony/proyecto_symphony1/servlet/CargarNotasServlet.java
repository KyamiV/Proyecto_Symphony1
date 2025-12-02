/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.mysymphony.proyecto_symphony1.servlet;

/**
 * Servlet: CargarNotasServlet
 * Rol: Docente
 * Autor: Camila
 * Creado: 27/11/2025
 *
 * Propósito:
 *   - Cargar la vista registrarNotas.jsp con estudiantes y datos de clase.
 *   - Escalar trazabilidad institucional y preparar datos para edición de notas por clase.
 *   - Validar sesión y rol activo.
 *   - Registrar acción en Auditoría y Bitácora institucional.
 */

import com.mysymphony.proyecto_symphony1.dao.NotaDAO;
import com.mysymphony.proyecto_symphony1.dao.ClaseDAO;
import com.mysymphony.proyecto_symphony1.dao.AuditoriaDAO;
import com.mysymphony.proyecto_symphony1.dao.BitacoraDAO;
import com.mysymphony.proyecto_symphony1.modelo.Estudiante;
import com.mysymphony.proyecto_symphony1.modelo.Nota;
import com.mysymphony.proyecto_symphony1.util.Conexion;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import javax.servlet.RequestDispatcher;
import java.io.IOException;
import java.sql.Connection;
import java.util.*;

@WebServlet("/CargarNotasServlet")
public class CargarNotasServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        procesarSolicitud(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // ✅ Delegamos en doGet para evitar error 405 (Method Not Allowed)
        procesarSolicitud(request, response);
    }

    private void procesarSolicitud(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession sesion = request.getSession(false);
        String rol = (sesion != null) ? (String) sesion.getAttribute("rolActivo") : null;
        Integer idDocente = (sesion != null) ? (Integer) sesion.getAttribute("idActivo") : null;
        String nombreDocente = (sesion != null) ? (String) sesion.getAttribute("nombreActivo") : "desconocido";

        if (rol == null || !"docente".equalsIgnoreCase(rol) || idDocente == null) {
            request.setAttribute("tipoMensaje", "danger");
            request.setAttribute("mensaje", "❌ Acceso restringido: requiere rol docente.");
            request.getRequestDispatcher("/fragmentos/error.jsp").forward(request, response);
            return;
        }

        String claseIdStr = request.getParameter("claseId");
        if (claseIdStr == null || claseIdStr.isEmpty()) {
            claseIdStr = (sesion != null && sesion.getAttribute("claseId") != null)
                         ? sesion.getAttribute("claseId").toString()
                         : null;
        }

        if (claseIdStr == null || claseIdStr.isEmpty()) {
            request.setAttribute("tipoMensaje", "warning");
            request.setAttribute("mensaje", "⚠️ Clase no especificada.");
            request.getRequestDispatcher("/fragmentos/error.jsp").forward(request, response);
            return;
        }

        int claseId;
        try {
            claseId = Integer.parseInt(claseIdStr);
        } catch (NumberFormatException e) {
            request.setAttribute("tipoMensaje", "danger");
            request.setAttribute("mensaje", "⚠️ Clase no válida.");
            request.getRequestDispatcher("/fragmentos/error.jsp").forward(request, response);
            return;
        }

        sesion.setAttribute("claseId", claseId);
        request.setAttribute("claseId", claseId);

        try (Connection conn = Conexion.getConnection()) {
            NotaDAO notaDAO = new NotaDAO(conn);
            ClaseDAO claseDAO = new ClaseDAO(conn);

            List<Estudiante> estudiantes = notaDAO.obtenerEstudiantesPorClase(claseId);
            List<Nota> notas = notaDAO.obtenerNotasPorClase(claseId);

            Map<String, String> datosClase = claseDAO.obtenerDatosClase(claseId);
            String nombreClase = datosClase.getOrDefault("nombre", "Sin nombre");
            String aula = datosClase.getOrDefault("aula", "Sin aula");
            String horario = (datosClase.get("dia") != null ? datosClase.get("dia") : "") + " " +
                             (datosClase.get("inicio") != null ? datosClase.get("inicio") : "") + " - " +
                             (datosClase.get("fin") != null ? datosClase.get("fin") : "");

            Object mensaje = sesion.getAttribute("mensaje");
            Object tipoMensaje = sesion.getAttribute("tipoMensaje");
            if (mensaje != null) {
                request.setAttribute("mensaje", mensaje);
                request.setAttribute("tipoMensaje", tipoMensaje != null ? tipoMensaje : "info");
                sesion.removeAttribute("mensaje");
                sesion.removeAttribute("tipoMensaje");
            }

            new BitacoraDAO(conn).registrarAccion(
                    "Docente accedió a registrar notas en clase " + nombreClase,
                    nombreDocente, rol, "Notas por clase");

            Map<String, String> registro = new HashMap<>();
            registro.put("usuario", String.valueOf(idDocente));
            registro.put("rol", rol);
            registro.put("modulo", "Notas por clase");
            registro.put("accion", "Accedió a registrar notas en clase " + nombreClase);
            registro.put("ip_origen", request.getRemoteAddr());
            new AuditoriaDAO(conn).registrarAccion(registro);

            request.setAttribute("estudiantes", estudiantes);
            request.setAttribute("notas", notas);
            request.setAttribute("nombreClase", nombreClase);
            request.setAttribute("aula", aula);
            request.setAttribute("horario", horario);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("tipoMensaje", "danger");
            request.setAttribute("mensaje", "❌ Error al cargar datos de clase: " + e.getMessage());
            request.getRequestDispatcher("/fragmentos/error.jsp").forward(request, response);
            return;
        }

        RequestDispatcher dispatcher = request.getRequestDispatcher("/docente/registrarNotas.jsp");
        dispatcher.forward(request, response);
    }
}