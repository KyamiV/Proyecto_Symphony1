/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.mysymphony.proyecto_symphony1.servlet;

/**
 * Servlet para eliminar una nota por clase con trazabilidad institucional.
 * Rol: docente
 * Autor: Camila
 * Trazabilidad:
 *   - Valida sesión
 *   - Elimina nota en BD
 *   - Registra acción en bitácora y auditoría institucional
 *   - Retorna a registrarNotas.jsp con datos actualizados
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

@WebServlet("/EliminarNotaClaseServlet")
public class EliminarNotaClaseServlet extends HttpServlet {

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
            conn.setAutoCommit(false); // 🚦 Manejo de transacción
            NotaDAO dao = new NotaDAO(conn);
            boolean exito = dao.eliminarNotaPorClase(notaId);

            if (exito) {
                request.setAttribute("mensaje", "🗑️ Nota eliminada correctamente.");
                request.setAttribute("tipoMensaje", "success");

                // 📖 Bitácora institucional
                new BitacoraDAO(conn).registrarAccion(
                        "Docente eliminó nota ID " + notaId + " en clase " + claseId,
                        nombreDocente, rol, "Gestión de notas");

                // 🛡️ Auditoría institucional
                Map<String, String> registro = new HashMap<>();
                registro.put("usuario", nombreDocente + " (ID: " + idDocente + ")");
                registro.put("rol", rol);
                registro.put("accion", "Eliminó nota con ID " + notaId + " en clase " + claseId);
                registro.put("modulo", "Gestión de notas");
                registro.put("referencia_id", String.valueOf(notaId));
                registro.put("ip_origen", request.getRemoteAddr());
                new AuditoriaDAO(conn).registrarAccion(registro);

                conn.commit(); // ✅ Confirmar transacción
                System.out.println("✅ Nota eliminada: ID=" + notaId + " en clase " + claseId + " por docente " + nombreDocente);

            } else {
                conn.rollback(); // ❌ Revertir si no se eliminó
                request.setAttribute("mensaje", "❌ Error al eliminar la nota.");
                request.setAttribute("tipoMensaje", "danger");
            }

            // 🔄 Recargar datos para registrarNotas.jsp
            ClaseDAO claseDAO = new ClaseDAO(conn);
            Map<String, String> datosClase = claseDAO.obtenerDatosClase(claseId);
            String nombreClase = datosClase.getOrDefault("nombre", "Sin nombre");
            String aula = datosClase.getOrDefault("aula", "Sin aula");
            String horario = (datosClase.get("dia") != null ? datosClase.get("dia") : "") + " " +
                             (datosClase.get("inicio") != null ? datosClase.get("inicio") : "") + " - " +
                             (datosClase.get("fin") != null ? datosClase.get("fin") : "");

            List<Estudiante> estudiantes = dao.obtenerEstudiantesPorClase(claseId);
            List<Nota> notas = dao.obtenerNotasPorClase(claseId);

            request.setAttribute("claseId", claseId);
            request.setAttribute("nombreClase", nombreClase);
            request.setAttribute("aula", aula);
            request.setAttribute("horario", horario);
            request.setAttribute("estudiantes", estudiantes);
            request.setAttribute("notas", notas);

            // 🔹 Forward directo a registrarNotas.jsp
            request.getRequestDispatcher("/docente/registrarNotas.jsp").forward(request, response);

        } catch (Exception e) {
            request.setAttribute("mensaje", "❌ Error al conectar con la base de datos.");
            request.setAttribute("tipoMensaje", "danger");
            e.printStackTrace();
            request.getRequestDispatcher("/docente/registrarNotas.jsp").forward(request, response);
        }
    }
}