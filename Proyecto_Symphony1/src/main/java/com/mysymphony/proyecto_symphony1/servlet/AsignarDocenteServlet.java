/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.mysymphony.proyecto_symphony1.servlet;

/**
 * Servlet maestro para asignar docentes a clases institucionales.
 * Rol: administrador
 * Autor: Camila
 *
 * Trazabilidad:
 *   - doGet: carga formulario dinámico con listas de docentes y clases
 *   - doPost: registra asignación en BD, bitácora y auditoría institucional
 */

import com.mysymphony.proyecto_symphony1.dao.ClaseDAO;
import com.mysymphony.proyecto_symphony1.dao.BitacoraDAO;
import com.mysymphony.proyecto_symphony1.dao.AuditoriaDAO;
import com.mysymphony.proyecto_symphony1.modelo.Clase;
import com.mysymphony.proyecto_symphony1.modelo.Docente;
import com.mysymphony.proyecto_symphony1.util.Conexion;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/AsignarDocenteServlet")
public class AsignarDocenteServlet extends HttpServlet {

    // ✅ GET: carga listas de clases y docentes para el formulario
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession sesion = request.getSession(false);
        String rol = (sesion != null) ? (String) sesion.getAttribute("rolActivo") : null;

        if (rol == null || !"administrador".equalsIgnoreCase(rol)) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        try (Connection conn = Conexion.getConnection()) {
            ClaseDAO claseDAO = new ClaseDAO(conn);

            // 📥 Listar docentes y clases disponibles
            List<Docente> docentes = claseDAO.listarDocentes();
            List<Clase> clasesDisponibles = claseDAO.listarClasesDisponiblesParaInscripcion();

            request.setAttribute("docentes", docentes);
            request.setAttribute("clasesDisponibles", clasesDisponibles);

            request.getRequestDispatcher("/administrador/fragmentoAsignarDocente.jsp").forward(request, response);

        } catch (Exception e) {
            response.setContentType("text/plain; charset=UTF-8");
            response.getWriter().write("❌ Error interno al cargar formulario: " + e.getMessage());
        }
    }

    // ✅ POST: asigna docente a clase con trazabilidad
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession sesion = request.getSession(false);
        String rol = (sesion != null) ? (String) sesion.getAttribute("rolActivo") : null;
        String usuario = (sesion != null) ? (String) sesion.getAttribute("nombreActivo") : "desconocido";

        if (rol == null || !"administrador".equalsIgnoreCase(rol)) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        String mensaje;

        try {
            // 📥 Lectura tolerante de parámetros
            String docenteParam = request.getParameter("idDocente");
            if (docenteParam == null) docenteParam = request.getParameter("docenteId");

            String claseParam = request.getParameter("idClase");
            if (claseParam == null) claseParam = request.getParameter("claseId");

            if (docenteParam == null || docenteParam.isEmpty() ||
                claseParam == null || claseParam.isEmpty()) {
                sesion.setAttribute("mensaje", "❌ Error: parámetros incompletos.");
                response.sendRedirect(request.getContextPath() + "/GestionarAsignacionesServlet");
                return;
            }

            int idDocente = Integer.parseInt(docenteParam);
            int idClase = Integer.parseInt(claseParam);

            try (Connection conn = Conexion.getConnection()) {
                ClaseDAO claseDAO = new ClaseDAO(conn);

                if (claseDAO.estaClaseYaAsignada(idClase)) {
                    mensaje = "⚠️ Esta clase ya tiene un docente asignado.";
                } else {
                    // ✅ Usar el método con trazabilidad
                    boolean asignado = claseDAO.asignarDocenteConTrazabilidad(idDocente, idClase, usuario, rol);
                    mensaje = asignado ? "✔ Docente asignado correctamente." : "❌ Error al asignar docente.";

                    if (asignado) {
                        // 📌 Registrar en bitácora institucional
                        BitacoraDAO bitacoraDAO = new BitacoraDAO(conn);
                        bitacoraDAO.registrarAccion(
                            "Administrador asignó docente ID " + idDocente + " a la clase ID " + idClase,
                            usuario, rol, "Asignaciones"
                        );

                        // 📌 Registrar en auditoría técnica
                        Map<String, String> registro = new HashMap<>();
                        registro.put("usuario", usuario);
                        registro.put("rol", rol);
                        registro.put("modulo", "Asignaciones");
                        registro.put("accion", "Asignación de docente ID " + idDocente + " a clase ID " + idClase);
                        new AuditoriaDAO(conn).registrarAccion(registro);
                    }
                }
            }

        } catch (Exception e) {
            mensaje = "❌ Error interno: " + e.getMessage();
        }

        sesion.setAttribute("mensaje", mensaje);
        response.sendRedirect(request.getContextPath() + "/GestionarAsignacionesServlet");
    }
}