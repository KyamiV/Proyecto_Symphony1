/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.mysymphony.proyecto_symphony1.servlet;

import com.mysymphony.proyecto_symphony1.dao.ClaseDAO;
import com.mysymphony.proyecto_symphony1.dao.AuditoriaDAO;
import com.mysymphony.proyecto_symphony1.dao.BitacoraDAO;
import com.mysymphony.proyecto_symphony1.util.Conexion;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;

/**
 * Servlet encargado de registrar una nueva clase institucional.
 * Valida sesión, registra en BD, documenta en bitácora y auditoría.
 * Autor: Camila – Mejorado por ChatGPT
 */
@WebServlet("/RegistrarClaseServlet")
public class RegistrarClaseServlet extends HttpServlet {

    private static final String VIEW_ERROR = "administrador/gestionarClasesPrincipal.jsp";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Redirige siempre al panel de gestión
        response.sendRedirect("GestionarClasesServlet");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        /*---------------------------------------------------------
         *  1️⃣ Validación de sesión y rol
         *---------------------------------------------------------*/
        HttpSession session = request.getSession(false);

        if (session == null ||
            !"administrador".equalsIgnoreCase(String.valueOf(session.getAttribute("rolActivo")))) {

            if (session != null)
                session.setAttribute("mensaje", "⚠️ Acceso restringido. Requiere rol administrador.");

            response.sendRedirect("login.jsp");
            return;
        }

        String usuarioActivo = (String) session.getAttribute("nombreActivo");
        String rolActivo = (String) session.getAttribute("rolActivo");


        /*---------------------------------------------------------
         *  2️⃣ Capturar y normalizar parámetros
         *---------------------------------------------------------*/
        String nombreClase     = trim(request.getParameter("nombreClase"));
        String instrumento     = trim(request.getParameter("instrumento"));
        String etapa           = trim(request.getParameter("etapa"));
        String grupo           = trim(request.getParameter("grupo"));
        String cupoStr         = trim(request.getParameter("cupo"));
        String fechaLimiteStr  = trim(request.getParameter("fecha_limite"));
        String fechaInicioStr  = trim(request.getParameter("fecha_inicio"));
        String fechaFinStr     = trim(request.getParameter("fecha_fin"));

        // Validación obligatoria
        if (nombreClase.isEmpty() || instrumento.isEmpty() || etapa.isEmpty() || grupo.isEmpty() ||
            cupoStr.isEmpty() || fechaLimiteStr.isEmpty() || fechaInicioStr.isEmpty() || fechaFinStr.isEmpty()) {

            request.setAttribute("error", "⚠️ Todos los campos son obligatorios.");
            request.getRequestDispatcher(VIEW_ERROR).forward(request, response);
            return;
        }

        // Validación de longitud
        if (nombreClase.length() > 100 || instrumento.length() > 50 || etapa.length() > 50 || grupo.length() > 50) {
            request.setAttribute("error", "⚠️ Los campos exceden la longitud permitida.");
            request.getRequestDispatcher(VIEW_ERROR).forward(request, response);
            return;
        }

        /*---------------------------------------------------------
         *  3️⃣ Validación de cupo y fechas
         *---------------------------------------------------------*/
        int cupo;
        LocalDateTime fechaInicio, fechaFin;
        LocalDate fechaLimite;

        try {
            // Cupo
            cupo = Integer.parseInt(cupoStr);
            if (cupo <= 0)
                throw new IllegalArgumentException("El cupo debe ser mayor que cero.");

            // Fechas
            fechaInicio = LocalDate.parse(fechaInicioStr).atStartOfDay();
            fechaFin    = LocalDate.parse(fechaFinStr).atStartOfDay();
            fechaLimite = LocalDate.parse(fechaLimiteStr);

            // Validaciones de lógica
            if (fechaInicio.isBefore(LocalDateTime.now()))
                throw new IllegalArgumentException("La fecha de inicio no puede ser anterior a hoy.");

            if (fechaFin.isBefore(fechaInicio))
                throw new IllegalArgumentException("La fecha fin debe ser posterior a la fecha de inicio.");

            if (!fechaLimite.isBefore(fechaInicio.toLocalDate()))
                throw new IllegalArgumentException("La fecha límite debe ser anterior a la fecha de inicio.");

        } catch (NumberFormatException e) {
            request.setAttribute("error", "❌ Cupo inválido: " + e.getMessage());
            request.getRequestDispatcher(VIEW_ERROR).forward(request, response);
            return;
        } catch (DateTimeParseException | IllegalArgumentException e) {
            request.setAttribute("error", "❌ Fechas inválidas: " + e.getMessage());
            request.getRequestDispatcher(VIEW_ERROR).forward(request, response);
            return;
        }

        /*---------------------------------------------------------
         *  4️⃣ Registro en BD con transacción
         *---------------------------------------------------------*/
        try (Connection conn = Conexion.getConnection()) {

            if (conn == null) {
                request.setAttribute("error", "❌ No se pudo conectar a la base de datos.");
                request.getRequestDispatcher(VIEW_ERROR).forward(request, response);
                return;
            }

            conn.setAutoCommit(false);
            ClaseDAO claseDAO = new ClaseDAO(conn);

            // Validación de duplicados
            if (claseDAO.existeClase(nombreClase, grupo)) {
                request.setAttribute("error", "⚠️ Ya existe una clase con ese nombre y grupo.");
                request.getRequestDispatcher(VIEW_ERROR).forward(request, response);
                return;
            }

            boolean registrada = claseDAO.registrarClase(
                    nombreClase, instrumento, etapa, grupo, cupo,
                    fechaInicio, fechaFin, fechaLimite
            );

            if (!registrada) {
                conn.rollback();
                request.setAttribute("error", "❌ Error al registrar la clase. Intenta nuevamente.");
                request.getRequestDispatcher(VIEW_ERROR).forward(request, response);
                return;
            }

            /*---------------------------------------------------------
             *  5️⃣ Registrar bitácora y auditoría
             *---------------------------------------------------------*/
            BitacoraDAO bitacoraDAO = new BitacoraDAO(conn);
            bitacoraDAO.registrarAccion(
                    "Clase registrada: '" + nombreClase + "' (Grupo " + grupo + ")",
                    usuarioActivo,
                    rolActivo,
                    "Gestión de Clases"
            );

            AuditoriaDAO auditoriaDAO = new AuditoriaDAO(conn);
            Map<String, String> registro = new HashMap<>();
            registro.put("usuario", usuarioActivo);
            registro.put("rol", rolActivo);
            registro.put("modulo", "Gestión de Clases");
            registro.put("accion", "Registró clase '" + nombreClase + "' (Grupo " + grupo + ")");
            registro.put("ip_origen", request.getRemoteAddr());

            auditoriaDAO.registrarAccion(registro);

            // Confirmar transacción
            conn.commit();

            /*---------------------------------------------------------
             *  6️⃣ Éxito
             *---------------------------------------------------------*/
            session.setAttribute("mensaje", "✔ Clase registrada exitosamente.");
            System.out.println("✅ Clase registrada: " + nombreClase + " (Grupo " + grupo + ") por " + usuarioActivo);
            response.sendRedirect("GestionarClasesServlet");

        } catch (Exception e) {
            // Manejo de errores con rollback
            try {
                Connection conn = Conexion.getConnection();
                if (conn != null) conn.rollback();
            } catch (Exception rollbackEx) {
                rollbackEx.printStackTrace();
            }

            request.setAttribute("error", "Error interno: " + e.getMessage());
            request.getRequestDispatcher(VIEW_ERROR).forward(request, response);
            e.printStackTrace();
        }
    }

    /** Utilidad: evita NPE y limpia espacios */
    private String trim(String s) {
        return (s == null) ? "" : s.trim();
    }
}