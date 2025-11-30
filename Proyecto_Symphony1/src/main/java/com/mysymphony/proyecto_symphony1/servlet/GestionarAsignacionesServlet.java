/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.mysymphony.proyecto_symphony1.servlet;

/**
 * Servlet maestro para gestión de asignaciones institucionales.
 * Roles: administrador, estudiante, docente
 * Autor: Camila
 *
 * Trazabilidad:
 *   - doGet: carga filtros y tablas institucionales según rol
 *   - doPost: permite asignar/desasignar estudiantes (solo administrador)
 */

import com.mysymphony.proyecto_symphony1.dao.ClaseDAO;
import com.mysymphony.proyecto_symphony1.dao.DocenteDAO;
import com.mysymphony.proyecto_symphony1.dao.EstudianteDAO;
import com.mysymphony.proyecto_symphony1.dao.AsignacionDAO;
import com.mysymphony.proyecto_symphony1.modelo.Clase;
import com.mysymphony.proyecto_symphony1.modelo.Docente;
import com.mysymphony.proyecto_symphony1.modelo.Estudiante;
import com.mysymphony.proyecto_symphony1.dto.DocenteConClaseDTO;
import com.mysymphony.proyecto_symphony1.util.Conexion;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.util.List;
import java.util.Map;

@WebServlet("/GestionarAsignacionesServlet")
public class GestionarAsignacionesServlet extends HttpServlet {

    // ✅ GET: Cargar filtros y tablas institucionales según rol
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession sesion = request.getSession(false);
        String rol = (sesion != null) ? (String) sesion.getAttribute("rolActivo") : null;

        if (rol == null) {
            request.setAttribute("mensaje", "⚠️ Debe iniciar sesión para continuar.");
            request.setAttribute("tipoMensaje", "warning");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
            return;
        }

        String destino;

        try (Connection conn = Conexion.getConnection()) {
            DocenteDAO docenteDAO = new DocenteDAO(conn);
            ClaseDAO claseDAO = new ClaseDAO(conn);
            EstudianteDAO estudianteDAO = new EstudianteDAO(conn);
            AsignacionDAO asignacionDAO = new AsignacionDAO(conn);

            switch (rol.toLowerCase()) {
                case "administrador":
                    // 📥 Listas para filtros
                    List<Docente> docentes = docenteDAO.listarDocentes();
                    List<String> instrumentos = claseDAO.listarInstrumentosDisponibles();
                    List<Clase> clases = claseDAO.listarClasesDisponiblesParaInscripcion();

                    // 📥 Tablas institucionales
                    List<Map<String, String>> asignacionesInstitucionales = asignacionDAO.listarAsignacionesInstitucionales();
                    List<Map<String, String>> estudiantesAsignados = asignacionDAO.listarEstudiantesAsignados();

                    // 📥 Estudiantes disponibles para asignar
                    List<Estudiante> estudiantesDisponibles = estudianteDAO.listarEstudiantesDisponibles();

                    // 🔗 Pasar datos al JSP
                    request.setAttribute("docentes", docentes);
                    request.setAttribute("instrumentos", instrumentos);
                    request.setAttribute("clases", clases);
                    request.setAttribute("asignacionesInstitucionales", asignacionesInstitucionales);
                    request.setAttribute("estudiantesAsignados", estudiantesAsignados);
                    request.setAttribute("estudiantesDisponibles", estudiantesDisponibles);

                    destino = "/administrador/gestionarAsignaciones.jsp";
                    break;

                case "estudiante":
                    // 📥 Asignaciones del estudiante
                    Integer idEstudiante = (Integer) sesion.getAttribute("idActivo");
                    if (idEstudiante != null) {
                        List<Map<String, String>> asignacionesEstudiante =
                                asignacionDAO.listarAsignacionesPorEstudiante(idEstudiante);
                        request.setAttribute("asignacionesEstudiante", asignacionesEstudiante);
                    } else {
                        request.setAttribute("mensaje", "⚠️ No se encontró el ID del estudiante en sesión.");
                        request.setAttribute("tipoMensaje", "warning");
                    }
                    destino = "/estudiante/verAsignaciones.jsp";
                    break;

                case "docente":
                    // 📥 Clases asignadas al docente (DTO)
                    Integer idDocente = (Integer) sesion.getAttribute("idActivo");
                    if (idDocente != null) {
                        List<DocenteConClaseDTO> clasesDocente =
                                asignacionDAO.listarClasesPorDocente(idDocente);

                        // 📥 Horarios de las clases del docente
                        List<Map<String, Object>> horariosDocente = asignacionDAO.listarHorariosPorDocente(idDocente);

                        // 🔗 Pasar datos al JSP
                        request.setAttribute("clases", clasesDocente);
                        request.setAttribute("horarios", horariosDocente);
                    } else {
                        request.setAttribute("mensaje", "⚠️ No se encontró el ID del docente en sesión.");
                        request.setAttribute("tipoMensaje", "warning");
                    }
                    destino = "/docente/verClasesDocente.jsp"; // ✅ dashboard docente con dos tablas
                    break;

                default:
                    request.setAttribute("mensaje", "⚠️ Rol no autorizado.");
                    request.setAttribute("tipoMensaje", "warning");
                    destino = "/login.jsp";
                    break;
            }

            request.getRequestDispatcher(destino).forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("mensaje", "❌ Error al cargar gestión de asignaciones: " + e.getMessage());
            request.setAttribute("tipoMensaje", "error");
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }

    // ✅ POST: Asignar estudiantes seleccionados (solo administrador)
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession sesion = request.getSession(false);
        String rol = (sesion != null) ? (String) sesion.getAttribute("rolActivo") : null;
        String usuario = (sesion != null) ? (String) sesion.getAttribute("nombreActivo") : "desconocido";

        if (rol == null || !"administrador".equalsIgnoreCase(rol)) {
            request.setAttribute("mensaje", "⚠️ Solo el administrador puede asignar estudiantes.");
            request.setAttribute("tipoMensaje", "warning");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
            return;
        }

        String mensaje;
        String tipoMensaje = "success";

        try (Connection conn = Conexion.getConnection()) {
            AsignacionDAO asignacionDAO = new AsignacionDAO(conn);

            String[] estudiantesSeleccionados = request.getParameterValues("estudiantesSeleccionados");
            String claseIdParam = request.getParameter("claseId");

            if (estudiantesSeleccionados != null && claseIdParam != null && !claseIdParam.isEmpty()) {
                int claseId = Integer.parseInt(claseIdParam);

                for (String estId : estudiantesSeleccionados) {
                    try {
                        int estudianteId = Integer.parseInt(estId);
                        asignacionDAO.asignarEstudianteAClase(estudianteId, claseId, usuario, rol);
                    } catch (NumberFormatException nfe) {
                        System.err.println("⚠️ ID de estudiante inválido: " + estId);
                    }
                }
                mensaje = "✔ Estudiantes asignados correctamente.";
            } else {
                mensaje = "⚠️ No se seleccionaron estudiantes para asignar.";
                tipoMensaje = "error";
            }

        } catch (Exception e) {
            mensaje = "❌ Error interno al asignar estudiantes: " + e.getMessage();
            tipoMensaje = "error";
        }

        sesion.setAttribute("mensaje", mensaje);
        sesion.setAttribute("tipoMensaje", tipoMensaje);
        response.sendRedirect(request.getContextPath() + "/GestionarAsignacionesServlet");
    }
}