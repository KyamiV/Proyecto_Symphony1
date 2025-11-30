/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.mysymphony.proyecto_symphony1.servlet;

/**
 * Servlet para actualizar una nota por clase con trazabilidad institucional.
 * Rol: docente
 * Autor: Camila
 * Trazabilidad:
 *   - Valida sesión
 *   - Actualiza nota en BD
 *   - Registra acción en bitácora y auditoría institucional
 *   - Retorna al JSP registrarNotas.jsp con datos actualizados
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
import java.io.IOException;
import java.sql.Connection;
import java.util.*;

@WebServlet("/EditarNotaClaseServlet")
public class EditarNotaClaseServlet extends HttpServlet {

    // ✅ Soporte GET → delega en POST
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
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

        try {
            int notaId = Integer.parseInt(request.getParameter("notaId"));
            int claseId = Integer.parseInt(request.getParameter("claseId"));
            double nuevaNota = Double.parseDouble(request.getParameter("nota"));
            String observacion = request.getParameter("observacion");
            String fecha = request.getParameter("fecha");

            // 📏 Validar rango de nota
            if (nuevaNota < 0 || nuevaNota > 5) {
                request.setAttribute("mensaje", "⚠️ La nota debe estar entre 0 y 5.");
                request.setAttribute("tipoMensaje", "warning");
                request.getRequestDispatcher("/docente/registrarNotas.jsp").forward(request, response);
                return;
            }

            try (Connection conn = Conexion.getConnection()) {
                conn.setAutoCommit(false); // 🚦 Manejo de transacción
                NotaDAO notaDAO = new NotaDAO(conn);
                boolean actualizado = notaDAO.actualizarNotaPorClase(notaId, nuevaNota, observacion, fecha);

                if (actualizado) {
                    // 📖 Bitácora institucional
                    new BitacoraDAO(conn).registrarAccion(
                            "Docente actualizó nota ID " + notaId + " a " + nuevaNota,
                            nombreDocente, rol, "Notas por clase");

                    // 🛡️ Auditoría institucional
                    Map<String, String> registro = new HashMap<>();
                    registro.put("usuario", nombreDocente + " (ID: " + idDocente + ")");
                    registro.put("rol", rol);
                    registro.put("accion", "Actualizó nota con ID " + notaId + " a " + nuevaNota);
                    registro.put("modulo", "Notas por clase");
                    registro.put("referencia_id", String.valueOf(notaId));
                    registro.put("ip_origen", request.getRemoteAddr());
                    new AuditoriaDAO(conn).registrarAccion(registro);

                    conn.commit(); // ✅ Confirmar transacción
                    request.setAttribute("mensaje", "✔ Nota actualizada correctamente.");
                    request.setAttribute("tipoMensaje", "success");
                    System.out.println("✅ Nota actualizada: ID=" + notaId + ", Nueva=" + nuevaNota + " por docente " + nombreDocente);

                } else {
                    conn.rollback(); // ❌ Revertir si no se actualizó
                    request.setAttribute("mensaje", "⚠️ No se pudo actualizar la nota.");
                    request.setAttribute("tipoMensaje", "warning");
                }

                // 🔹 Recargar datos de la clase y notas para el JSP
                ClaseDAO claseDAO = new ClaseDAO(conn);
                Map<String, String> datosClase = claseDAO.obtenerDatosClase(claseId);
                String nombreClase = datosClase.getOrDefault("nombre", "Sin nombre");
                String aula = datosClase.getOrDefault("aula", "Sin aula");
                String horario = (datosClase.get("dia") != null ? datosClase.get("dia") : "") + " " +
                                 (datosClase.get("inicio") != null ? datosClase.get("inicio") : "") + " - " +
                                 (datosClase.get("fin") != null ? datosClase.get("fin") : "");

                List<Estudiante> estudiantes = notaDAO.obtenerEstudiantesPorClase(claseId);
                List<Nota> notas = notaDAO.obtenerNotasPorClase(claseId);

                request.setAttribute("claseId", claseId);
                request.setAttribute("nombreClase", nombreClase);
                request.setAttribute("aula", aula);
                request.setAttribute("horario", horario);
                request.setAttribute("estudiantes", estudiantes);
                request.setAttribute("notas", notas);

                // 🔹 Forward directo al JSP registrarNotas.jsp
                request.getRequestDispatcher("/docente/registrarNotas.jsp").forward(request, response);

            } catch (Exception e) {
                request.setAttribute("mensaje", "❌ Error al conectar con la base de datos.");
                request.setAttribute("tipoMensaje", "danger");
                e.printStackTrace();
                request.getRequestDispatcher("/docente/registrarNotas.jsp").forward(request, response);
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("mensaje", "❌ Error al actualizar la nota.");
            request.setAttribute("tipoMensaje", "danger");
            request.getRequestDispatcher("/docente/registrarNotas.jsp").forward(request, response);
        }
    }
}