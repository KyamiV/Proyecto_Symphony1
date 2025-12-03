/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.mysymphony.proyecto_symphony1.servlet;

/**
 * Rol: Docente
 * Servlet simplificado para registrar UNA nota por clase.
 * Recibe estudiante, competencia, nota, observación y fecha desde el formulario.
 * Autor: Camila
 * Trazabilidad: valida sesión, registra nota y guarda auditoría y bitácora institucional.
 */

import com.mysymphony.proyecto_symphony1.dao.NotaDAO;
import com.mysymphony.proyecto_symphony1.dao.AuditoriaDAO;
import com.mysymphony.proyecto_symphony1.dao.BitacoraDAO;
import com.mysymphony.proyecto_symphony1.util.Conexion;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.util.*;

@WebServlet("/RegistrarNotaClaseServlet")
public class RegistrarNotaClaseServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // ✅ Evita error 405 delegando en doPost
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 🔹 1. Validación de sesión y rol
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

        // 🔹 2. Obtener parámetros del formulario
        String claseIdStr = request.getParameter("claseId");
        String idEstudianteStr = request.getParameter("idEstudiante");
        String competencia = request.getParameter("competencia");
        String notaStr = request.getParameter("nota");
        String observacion = request.getParameter("observacion");
        String fecha = request.getParameter("fecha");
        String instrumento = request.getParameter("instrumento"); // opcional desde JSP
        String etapa = request.getParameter("etapa");             // opcional desde JSP

        // Validaciones básicas
        if (claseIdStr == null || claseIdStr.isEmpty() ||
            idEstudianteStr == null || idEstudianteStr.isEmpty() ||
            competencia == null || competencia.isEmpty() ||
            notaStr == null || notaStr.isEmpty() ||
            fecha == null || fecha.isEmpty()) {

            sesion.setAttribute("tipoMensaje", "danger");
            sesion.setAttribute("mensaje", "⚠️ Faltan parámetros obligatorios.");
            response.sendRedirect(request.getContextPath() + "/CargarNotasServlet?claseId=" + (claseIdStr != null ? claseIdStr : ""));
            return;
        }

        int claseId, estudianteId;
        double nota;
        try {
            claseId = Integer.parseInt(claseIdStr);
            estudianteId = Integer.parseInt(idEstudianteStr);
            nota = Double.parseDouble(notaStr);
        } catch (NumberFormatException e) {
            sesion.setAttribute("tipoMensaje", "danger");
            sesion.setAttribute("mensaje", "⚠️ Error de formato en IDs o nota.");
            response.sendRedirect(request.getContextPath() + "/CargarNotasServlet?claseId=" + claseIdStr);
            return;
        }

        if (nota < 0 || nota > 5) {
            sesion.setAttribute("tipoMensaje", "warning");
            sesion.setAttribute("mensaje", "⚠️ La nota debe estar entre 0 y 5.");
            response.sendRedirect(request.getContextPath() + "/CargarNotasServlet?claseId=" + claseId);
            return;
        }

        // 🔹 3. Registrar nota en BD
        try (Connection conn = Conexion.getConnection()) {
            NotaDAO notaDAO = new NotaDAO(conn);

            // Validar existencia del estudiante
            if (!notaDAO.existeEstudiante(estudianteId)) {
                sesion.setAttribute("tipoMensaje", "danger");
                sesion.setAttribute("mensaje", "⚠️ El estudiante no existe en la base de datos.");
                response.sendRedirect(request.getContextPath() + "/CargarNotasServlet?claseId=" + claseId);
                return;
            }

            // 🔹 Obtener o crear tabla guardada automáticamente
            int tablaId = notaDAO.obtenerIdTablaGuardada(claseId, idDocente);
            if (tablaId == 0) {
                tablaId = notaDAO.crearTablaGuardada(claseId, idDocente, nombreDocente);
            }

            // Validar duplicado por clase/competencia
            if (notaDAO.existeNotaPorClase(claseId, estudianteId, competencia)) {
                sesion.setAttribute("tipoMensaje", "warning");
                sesion.setAttribute("mensaje", "⚠️ Ya existe una nota para este estudiante en esta competencia.");
            } else {
                boolean exito = notaDAO.registrarNotaPorClase(
                        estudianteId,
                        claseId,
                        competencia,
                        nota,
                        observacion,
                        fecha,
                        idDocente,
                        instrumento,
                        etapa,
                        tablaId,       // ✅ id_tabla válido
                        nombreDocente  // registrada_por
                );

                if (exito) {
                    sesion.setAttribute("tipoMensaje", "success");
                    sesion.setAttribute("mensaje", "✔ Nota registrada correctamente para el estudiante.");

                    // 🛡️ Auditoría institucional
                    Map<String, String> registro = new HashMap<>();
                    registro.put("usuario", nombreDocente + " (ID: " + idDocente + ")");
                    registro.put("rol", rol);
                    registro.put("accion", "Registró nota para estudiante " + estudianteId + " en clase " + claseId);
                    registro.put("modulo", "Registro de notas");
                    registro.put("ip_origen", request.getRemoteAddr());
                    new AuditoriaDAO(conn).registrarAccion(registro);

                    // 📖 Bitácora institucional
                    new BitacoraDAO(conn).registrarAccion(
                            "Docente registró nota para estudiante " + estudianteId + " en clase " + claseId,
                            nombreDocente, rol, "Registro de notas"
                    );
                } else {
                    sesion.setAttribute("tipoMensaje", "danger");
                    sesion.setAttribute("mensaje", "❌ Error al registrar la nota.");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            sesion.setAttribute("tipoMensaje", "danger");
            sesion.setAttribute("mensaje", "❌ Error al registrar nota: " + e.getMessage());
        }

        // 🔹 4. Redirigir al flujo de carga de notas
        response.sendRedirect(request.getContextPath() + "/CargarNotasServlet?claseId=" + claseId);
    }
}