/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.mysymphony.proyecto_symphony1.servlet;

/**
 * Servlet: PanelDocenteServlet
 * Rol: docente
 * Autor: Camila
 * Creado: 27/11/2025
 *
 * Propósito:
 *   - Mostrar el panel del docente con sus clases asignadas y métricas reales.
 *   - Validar sesión y rol activo.
 *   - Consultar clases, métricas e indicadores desde la BD.
 *   - Registrar acceso en Auditoría y Bitácora institucional.
 */

import com.mysymphony.proyecto_symphony1.dao.ClaseDAO;
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

@WebServlet("/PanelDocenteServlet")
public class PanelDocenteServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        procesarSolicitud(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // ✅ Delegamos en doGet para evitar error 405
        procesarSolicitud(request, response);
    }

    private void procesarSolicitud(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 🔐 Validación de sesión activa
        HttpSession sesion = request.getSession(false);
        if (sesion == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        // 🎯 Recuperar atributos de sesión
        String rol = (String) sesion.getAttribute("rolActivo");
        Integer idDocente = (Integer) sesion.getAttribute("idActivo");
        String nombreDocente = (String) sesion.getAttribute("nombreActivo");

        // 🔒 Validar rol y autenticación
        if (rol == null || !"docente".equalsIgnoreCase(rol) || idDocente == null) {
            sesion.setAttribute("tipoMensaje", "warning");
            sesion.setAttribute("mensaje", "⚠️ Acceso restringido: requiere rol docente.");
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        try (Connection conn = Conexion.getConnection()) {
            // 🧩 Instanciar DAOs con conexión activa
            ClaseDAO claseDAO = new ClaseDAO(conn);
            NotaDAO notaDAO = new NotaDAO(conn);

            // 📦 Consultar clases asignadas al docente
            List<Map<String, String>> clases = claseDAO.obtenerClasesDetalladasPorDocente(idDocente);
            int totalClases = (clases != null) ? clases.size() : 0;

            // 🧮 Consultar métricas reales del docente
            int totalNotas = notaDAO.contarNotasRegistradas(idDocente);

            // 📝 Registro en bitácora institucional
            new BitacoraDAO(conn).registrarAccion(
                    "Docente accedió al panel institucional",
                    nombreDocente, rol, "Panel docente");

            // 🛡️ Registro en auditoría institucional
            Map<String, String> registro = new HashMap<>();
            registro.put("usuario", nombreDocente + " (ID: " + idDocente + ")");
            registro.put("rol", rol);
            registro.put("modulo", "Panel docente");
            registro.put("accion", "Accedió al panel con clases y métricas");
            registro.put("ip_origen", request.getRemoteAddr());
            new AuditoriaDAO(conn).registrarAccion(registro);

            // 📤 Enviar datos a la vista JSP
            request.setAttribute("nombreDocente", nombreDocente);
            request.setAttribute("clases", clases);
            request.setAttribute("totalClases", totalClases);
            request.setAttribute("totalNotas", totalNotas);

            // 🎯 Redirigir a la vista institucional del docente
            request.getRequestDispatcher("/docente/dashboard.jsp").forward(request, response);

        } catch (Exception e) {
            // ❌ Manejo de errores y redirección segura
            e.printStackTrace();
            sesion.setAttribute("tipoMensaje", "danger");
            sesion.setAttribute("mensaje", "❌ Error al cargar el panel docente: " + e.getMessage());

            // Registrar error en auditoría
            try (Connection conn = Conexion.getConnection()) {
                Map<String, String> registroError = new HashMap<>();
                registroError.put("usuario", (nombreDocente != null ? nombreDocente : "desconocido") +
                        " (ID: " + (idDocente != null ? idDocente : "N/A") + ")");
                registroError.put("rol", (rol != null ? rol : "N/A"));
                registroError.put("modulo", "Panel docente");
                registroError.put("accion", "Error al cargar el Panel docente");
                registroError.put("ip_origen", request.getRemoteAddr());
                new AuditoriaDAO(conn).registrarAccion(registroError);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            response.sendRedirect(request.getContextPath() + "/error.jsp");
        }
    }
}