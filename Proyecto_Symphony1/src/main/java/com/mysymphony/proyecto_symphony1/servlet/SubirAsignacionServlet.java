/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.mysymphony.proyecto_symphony1.servlet;

/**
 * Servlet para registrar la subida institucional de asignaciones al docente.
 * Autor: Camila
 * Trazabilidad: valida sesión, registra asignaciones, documenta en bitácora y auditoría.
 */

import com.mysymphony.proyecto_symphony1.dao.AsignacionDAO;
import com.mysymphony.proyecto_symphony1.dao.BitacoraDAO;
import com.mysymphony.proyecto_symphony1.dao.AuditoriaDAO;
import com.mysymphony.proyecto_symphony1.util.Conexion;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;

@WebServlet("/SubirAsignacionServlet")
public class SubirAsignacionServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession sesion = request.getSession();
        String rol = (String) sesion.getAttribute("rolActivo");
        String usuario = (String) sesion.getAttribute("nombreActivo");

        if (rol == null || !"administrador".equalsIgnoreCase(rol)) {
            sesion.setAttribute("mensaje", "⚠️ Acceso restringido: requiere rol administrador.");
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        String docenteIdStr = request.getParameter("docenteId");
        String instrumento = request.getParameter("instrumento");
        String[] estudiantes = request.getParameterValues("estudiantes");

        if (docenteIdStr == null || instrumento == null || estudiantes == null ||
            docenteIdStr.isEmpty() || instrumento.isEmpty() || estudiantes.length == 0) {
            sesion.setAttribute("mensaje", "⚠️ Datos incompletos para subir la asignación.");
            response.sendRedirect(request.getContextPath() + "/AsignarEstudiantesServlet");
            return;
        }

        int docenteId = Integer.parseInt(docenteIdStr);
        String fecha = LocalDate.now().toString();

        int exitosos = 0;
        int duplicados = 0;

        try (Connection con = Conexion.getConnection()) {
            con.setAutoCommit(false);

            AsignacionDAO dao = new AsignacionDAO(con);
            BitacoraDAO bitacoraDAO = new BitacoraDAO(con);
            AuditoriaDAO auditoriaDAO = new AuditoriaDAO(con);

            for (String estIdStr : estudiantes) {
                try {
                    int estudianteId = Integer.parseInt(estIdStr);
                    String etapa = request.getParameter("etapa_" + estudianteId);

                    if (etapa == null || etapa.isEmpty()) {
                        continue;
                    }

                    boolean yaExiste = dao.existeAsignacion(docenteId, estudianteId, instrumento);
                    if (yaExiste) {
                        duplicados++;
                        continue;
                    }

                    boolean registrado = dao.registrarAsignacion(docenteId, estudianteId, instrumento, etapa, fecha);
                    if (registrado) {
                        exitosos++;

                        // Bitácora institucional
                        bitacoraDAO.registrarAccion("Asignación registrada: estudiante " + estudianteId +
                                " → docente " + docenteId + " (" + instrumento + " - " + etapa + ")",
                                usuario, rol, "Asignaciones");
                    }

                } catch (Exception e) {
                    System.err.println("❌ Error al procesar estudianteId=" + estIdStr + ": " + e.getMessage());
                }
            }

            // Auditoría institucional
            String accion = (exitosos > 0)
                    ? "Subida de " + exitosos + " asignaciones al docente ID " + docenteId + " (" + instrumento + ")"
                    : "Intento fallido de subida de asignaciones";
            auditoriaDAO.registrarAuditoria(accion, "Asignaciones", usuario);

            con.commit();

        } catch (SQLException e) {
            System.err.println("❌ Error al registrar asignaciones: " + e.getMessage());
            sesion.setAttribute("mensaje", "❌ Error al registrar asignaciones: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/AsignarEstudiantesServlet?docenteId=" + docenteId + "&instrumento=" + instrumento);
            return;
        }

        // Mensaje final
        if (exitosos > 0) {
            String mensaje = "✔ Se subieron " + exitosos + " asignaciones correctamente.";
            if (duplicados > 0) {
                mensaje += " ⚠️ Se omitieron " + duplicados + " por duplicado.";
            }
            sesion.setAttribute("mensaje", mensaje);
        } else {
            sesion.setAttribute("mensaje", "⚠️ No se registraron asignaciones. Verifica los datos o posibles duplicados.");
        }

        response.sendRedirect(request.getContextPath() + "/AsignarEstudiantesServlet?docenteId=" + docenteId + "&instrumento=" + instrumento);
    }
}