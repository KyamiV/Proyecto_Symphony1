/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mysymphony.proyecto_symphony1.servlet;

/**
 * Rol: Docente
 * Servlet maestro para registrar UNA nota por clase.
 * Recibe estudiante, competencia, nota, observación e información de trazabilidad.
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
        doPost(request, response); // ✅ Evita error 405
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession sesion = request.getSession(false);
        String rol = (sesion != null) ? (String) sesion.getAttribute("rolActivo") : null;

        Integer idUsuario = (sesion != null) ? (Integer) sesion.getAttribute("idUsuario") : null;
        Integer idDocente = (sesion != null) ? (Integer) sesion.getAttribute("idDocente") : null;
        String nombreDocente = (sesion != null) ? (String) sesion.getAttribute("nombreActivo") : "desconocido";

        if (rol == null || !"docente".equalsIgnoreCase(rol) || idDocente == null) {
            System.out.println("Error → Acceso restringido. Rol=" + rol + ", idDocente=" + idDocente);
            sesion.setAttribute("tipoMensaje", "danger");
            sesion.setAttribute("mensaje", "⚠️ Acceso restringido: requiere rol docente.");
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        String claseIdStr = request.getParameter("claseId");
        String idEstudianteStr = request.getParameter("idEstudiante");
        String competencia = (request.getParameter("competencia") != null) ? request.getParameter("competencia").trim() : "";
        String notaStr = request.getParameter("nota");
        String observacion = (request.getParameter("observacion") != null) ? request.getParameter("observacion").trim() : null;
        String fechaStr = request.getParameter("fecha");
        String instrumento = (request.getParameter("instrumento") != null) ? request.getParameter("instrumento").trim() : null;
        String etapa = (request.getParameter("etapa") != null) ? request.getParameter("etapa").trim() : null;

        System.out.println("Parametros recibidos -> claseId=" + claseIdStr +
                ", idEstudiante=" + idEstudianteStr +
                ", competencia=" + competencia +
                ", nota=" + notaStr +
                ", fecha=" + fechaStr +
                ", observacion=" + observacion +
                ", instrumento=" + instrumento +
                ", etapa=" + etapa);

        if (claseIdStr == null || claseIdStr.isEmpty() ||
            idEstudianteStr == null || idEstudianteStr.isEmpty() ||
            competencia.isEmpty() || notaStr == null || notaStr.isEmpty() ||
            fechaStr == null || fechaStr.isEmpty()) {

            System.out.println("Error → Faltan parámetros obligatorios.");
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
            System.out.println("Error → Formato inválido en IDs o nota: " + e.getMessage());
            sesion.setAttribute("tipoMensaje", "danger");
            sesion.setAttribute("mensaje", "⚠️ Error de formato en IDs o nota.");
            response.sendRedirect(request.getContextPath() + "/CargarNotasServlet?claseId=" + claseIdStr);
            return;
        }

        if (nota < 0 || nota > 5) {
            System.out.println("Error → Nota fuera de rango: " + nota);
            sesion.setAttribute("tipoMensaje", "warning");
            sesion.setAttribute("mensaje", "⚠️ La nota debe estar entre 0 y 5.");
            response.sendRedirect(request.getContextPath() + "/CargarNotasServlet?claseId=" + claseId);
            return;
        }

        try (Connection conn = Conexion.getConnection()) {
            if (conn == null) {
                System.out.println("Error → Conexión a BD nula.");
                sesion.setAttribute("tipoMensaje", "danger");
                sesion.setAttribute("mensaje", "❌ Error: No se pudo conectar a la base de datos.");
                response.sendRedirect(request.getContextPath() + "/CargarNotasServlet?claseId=" + claseId);
                return;
            }

            NotaDAO notaDAO = new NotaDAO(conn);

            if (!notaDAO.existeEstudiante(estudianteId)) {
                System.out.println("Advertencia → Estudiante no vinculado en BD: id=" + estudianteId);
                sesion.setAttribute("tipoMensaje", "warning");
                sesion.setAttribute("mensaje", "✔ Nota registrada, pero el estudiante no está vinculado en el sistema.");
            }

            int tablaId = notaDAO.obtenerIdTablaGuardada(claseId, idDocente);
            if (tablaId == 0) {
                tablaId = notaDAO.crearTablaGuardada(claseId, idDocente, nombreDocente);
                System.out.println("Info → Se creó nueva tabla guardada con id=" + tablaId);
            }

            String fechaCompleta = fechaStr + " 00:00:00";

            System.out.println("RegistrarNotaClaseServlet -> estudianteId=" + estudianteId +
                    ", claseId=" + claseId +
                    ", competencia=" + competencia +
                    ", nota=" + nota +
                    ", observacion=" + observacion +
                    ", fecha=" + fechaCompleta +
                    ", docenteId=" + idDocente +
                    ", usuarioId=" + idUsuario +
                    ", instrumento=" + instrumento +
                    ", etapa=" + etapa +
                    ", tablaId=" + tablaId +
                    ", registradaPor=" + nombreDocente);

            if (notaDAO.existeNotaPorClase(claseId, estudianteId, competencia)) {
                System.out.println("Error → Nota duplicada para estudiante=" + estudianteId + " en competencia=" + competencia);
                sesion.setAttribute("tipoMensaje", "warning");
                sesion.setAttribute("mensaje", "⚠️ Ya existe una nota para este estudiante en esta competencia.");
            } else {
                boolean exito = notaDAO.registrarNotaPorClase(
                        estudianteId,
                        claseId,
                        competencia,
                        nota,
                        observacion,
                        fechaCompleta,
                        idDocente,
                        instrumento,
                        etapa,
                        tablaId,
                        nombreDocente
                );

                if (exito) {
                    System.out.println("Éxito → Nota registrada correctamente.");
                    sesion.setAttribute("tipoMensaje", "success");
                    sesion.setAttribute("mensaje", "✔ Nota registrada correctamente (pendiente de consolidar tabla).");

                    Map<String, String> registro = new HashMap<>();
                    registro.put("usuario", nombreDocente + " (UsuarioID: " + idUsuario + ", DocenteID: " + idDocente + ")");
                    registro.put("rol", rol);
                    registro.put("accion", "Registró nota para estudiante " + estudianteId + " en clase " + claseId);
                    registro.put("modulo", "Registro de notas");
                    registro.put("ip_origen", request.getRemoteAddr());
                    new AuditoriaDAO(conn).registrarAccion(registro);

                    new BitacoraDAO(conn).registrarAccion(
                            "Docente registró nota para estudiante " + estudianteId + " en clase " + claseId,
                            nombreDocente, rol, "Registro de notas"
                    );
                } else {
                    System.out.println("Error → registrarNotaPorClase devolvió false.");
                    sesion.setAttribute("tipoMensaje", "danger");
                    sesion.setAttribute("mensaje", "❌ Error al registrar la nota.");
                }
            }

                } catch (Exception e) {
            System.out.println("Error → Excepción al registrar nota: " + e.getMessage());
            e.printStackTrace(); // imprime el detalle completo en Tomcat
            sesion.setAttribute("tipoMensaje", "danger");
            sesion.setAttribute("mensaje", "❌ Error al registrar nota: " + e.getMessage());
        }

        // 🔹 4. Redirigir al flujo de carga de notas
        try {
            response.sendRedirect(request.getContextPath() + "/CargarNotasServlet?claseId=" + claseId);
        } catch (Exception e) {
            System.out.println("Error → Fallo al redirigir: " + e.getMessage());
            sesion.setAttribute("tipoMensaje", "danger");
            sesion.setAttribute("mensaje", "❌ Error al redirigir después de registrar nota.");
            request.getRequestDispatcher("/fragmentos/error.jsp").forward(request, response);
        }
    }
}