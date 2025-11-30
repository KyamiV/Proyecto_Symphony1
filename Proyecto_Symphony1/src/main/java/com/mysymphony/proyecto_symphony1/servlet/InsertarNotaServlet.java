/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.mysymphony.proyecto_symphony1.servlet;

/**
 * Servlet para insertar nota institucional por docente
 * Rol: docente
 * Autor: Camila
 * Trazabilidad: valida sesión, registra nota y guarda auditoría
 */

import com.mysymphony.proyecto_symphony1.dao.NotaDAO;
import com.mysymphony.proyecto_symphony1.dao.AuditoriaDAO;
import com.mysymphony.proyecto_symphony1.util.Conexion;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.*;

@WebServlet("/InsertarNotaServlet")
public class InsertarNotaServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession sesion = request.getSession(false);
        String rol = (sesion != null) ? (String) sesion.getAttribute("rolActivo") : null;
        String nombreDocente = (sesion != null) ? (String) sesion.getAttribute("nombreActivo") : "desconocido";
        int docenteId = (sesion != null) ? (Integer) sesion.getAttribute("idDocenteActivo") : 0;

        if (rol == null || !"docente".equalsIgnoreCase(rol)) {
            if (sesion != null) {
                sesion.setAttribute("mensaje", "⚠️ Acceso restringido: requiere rol docente.");
            }
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        // Obtener parámetros del formulario
        String claseIdStr = request.getParameter("claseId");   // ✅ obligatorio
        String estudianteIdStr = request.getParameter("id_Estudiante");
        String instrumento = request.getParameter("curso");    // curso = instrumento
        String etapa = request.getParameter("etapa");
        String competencia = request.getParameter("competencia"); // opcional
        String notaStr = request.getParameter("nota");
        String observaciones = request.getParameter("observaciones");
        String tablaIdStr = request.getParameter("tablaId");   // vínculo con tabla guardada

        String mensaje;

        if (claseIdStr != null && estudianteIdStr != null && instrumento != null && etapa != null && notaStr != null) {
            try {
                int claseId = Integer.parseInt(claseIdStr);
                int estudianteId = Integer.parseInt(estudianteIdStr);
                int tablaId = (tablaIdStr != null && !tablaIdStr.isEmpty()) ? Integer.parseInt(tablaIdStr) : 0;
                double nota = Double.parseDouble(notaStr);

                // Validar rango de nota institucional (0–5)
                if (nota < 0 || nota > 5) {
                    mensaje = "❌ La nota debe estar entre 0 y 5.";
                } else {
                    String fechaRegistro = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

                    try (Connection conn = Conexion.getConnection()) {
                        NotaDAO dao = new NotaDAO(conn);
                        boolean registrado = dao.registrarNotaPorClase(
                                estudianteId,
                                claseId,
                                competencia,
                                nota,
                                observaciones,
                                fechaRegistro,
                                docenteId,
                                instrumento,
                                etapa,
                                tablaId,
                                nombreDocente // registrada_por
                        );

                        if (registrado) {
                            mensaje = "✅ Nota registrada correctamente.";

                            // 📝 Registro en auditoría institucional
                            AuditoriaDAO auditoriaDAO = new AuditoriaDAO(conn);
                            Map<String, String> registro = new HashMap<>();
                            registro.put("usuario", nombreDocente + " (ID: " + docenteId + ")");
                            registro.put("rol", rol);
                            registro.put("modulo", "Registro de notas");
                            registro.put("accion", "Registró nota de estudianteId=" + estudianteId +
                                    " en claseId=" + claseId + " instrumento=" + instrumento + " etapa=" + etapa);
                            registro.put("ip_origen", request.getRemoteAddr());
                            auditoriaDAO.registrarAccion(registro);

                        } else {
                            mensaje = "❌ No se pudo registrar la nota.";
                        }
                    }
                }

            } catch (NumberFormatException e) {
                mensaje = "❌ Error al convertir la nota o el ID del estudiante.";
                e.printStackTrace();
            } catch (Exception e) {
                mensaje = "❌ Error al conectar con la base de datos: " + e.getMessage();
                e.printStackTrace();
            }

        } else {
            mensaje = "❌ Faltan datos para registrar la nota.";
        }

        request.setAttribute("mensaje", mensaje);
        request.getRequestDispatcher("/docente/registrarNotas.jsp").forward(request, response);
    }
}