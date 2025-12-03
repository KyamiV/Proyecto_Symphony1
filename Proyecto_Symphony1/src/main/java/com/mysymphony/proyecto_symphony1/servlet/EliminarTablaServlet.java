/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mysymphony.proyecto_symphony1.servlet;

/**
 * Servlet para eliminar una tabla institucional de notas
 * Rol: docente o administrador
 * Autor: Camila
 * Trazabilidad:
 *   - Valida sesión y rol
 *   - Elimina tabla y notas asociadas
 *   - Registra acción en auditoría y bitácora institucional
 */

import com.mysymphony.proyecto_symphony1.util.Conexion;
import com.mysymphony.proyecto_symphony1.dao.AuditoriaDAO;
import com.mysymphony.proyecto_symphony1.dao.BitacoraDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/EliminarTablaServlet")
public class EliminarTablaServlet extends HttpServlet {

    // 🔹 Delegar POST a GET para evitar error 405
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession sesion = request.getSession(false);
        String rol = (sesion != null) ? (String) sesion.getAttribute("rolActivo") : null;
        String usuario = (sesion != null) ? (String) sesion.getAttribute("nombreActivo") : "desconocido";
        Integer idActivo = (sesion != null) ? (Integer) sesion.getAttribute("idActivo") : null;

        // 🔐 Validación de sesión y rol (docente o administrador)
        if (rol == null || idActivo == null ||
            (!"docente".equalsIgnoreCase(rol) && !"administrador".equalsIgnoreCase(rol))) {
            request.setAttribute("mensaje", "⚠️ Acceso restringido: requiere rol docente o administrador.");
            request.setAttribute("tipoMensaje", "danger");
            request.getRequestDispatcher("/fragmentos/error.jsp").forward(request, response);
            return;
        }

        int tablaId;
        try {
            tablaId = Integer.parseInt(request.getParameter("tablaId")); // 👈 unificado con JSP
        } catch (NumberFormatException e) {
            request.setAttribute("mensaje", "⚠️ ID de tabla inválido.");
            request.setAttribute("tipoMensaje", "danger");
            request.getRequestDispatcher("/fragmentos/error.jsp").forward(request, response);
            return;
        }

        try (Connection conn = Conexion.getConnection()) {
            conn.setAutoCommit(false);

            // 🔹 1. Eliminar notas asociadas (tabla correcta: notas_clase)
            try (PreparedStatement psNotas = conn.prepareStatement(
                    "DELETE FROM notas_clase WHERE id_tabla = ?")) {
                psNotas.setInt(1, tablaId);
                psNotas.executeUpdate();
            }

            // 🔹 2. Eliminar tabla
            String sqlDelete = "DELETE FROM tablas_guardadas WHERE id = ?";
            if ("docente".equalsIgnoreCase(rol)) {
                sqlDelete += " AND id_docente = ?";
            }

            try (PreparedStatement psTabla = conn.prepareStatement(sqlDelete)) {
                psTabla.setInt(1, tablaId);
                if ("docente".equalsIgnoreCase(rol)) {
                    psTabla.setInt(2, idActivo);
                }
                int filas = psTabla.executeUpdate();

                if (filas > 0) {
                    // 📝 Auditoría
                    AuditoriaDAO auditoriaDAO = new AuditoriaDAO(conn);
                    Map<String, String> registro = new HashMap<>();
                    registro.put("usuario", String.valueOf(idActivo));
                    registro.put("rol", rol);
                    registro.put("modulo", "Tablas guardadas");
                    registro.put("accion", "Eliminó tabla institucional ID " + tablaId);
                    registro.put("id_referencia", String.valueOf(tablaId));
                    registro.put("ip_origen", request.getRemoteAddr());
                    auditoriaDAO.registrarAccion(registro);

                    // 📖 Bitácora
                    BitacoraDAO bitacoraDAO = new BitacoraDAO(conn);
                    bitacoraDAO.registrarAccion(rol + " eliminó tabla institucional ID " + tablaId,
                            usuario, rol, "Tablas guardadas");

                    conn.commit();
                    request.setAttribute("mensaje", "✔ Tabla eliminada correctamente.");
                    request.setAttribute("tipoMensaje", "success");
                } else {
                    conn.rollback();
                    request.setAttribute("mensaje", "⚠️ No se encontró la tabla o no pertenece al usuario.");
                    request.setAttribute("tipoMensaje", "warning");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("mensaje", "❌ Error al eliminar la tabla: " + e.getMessage());
            request.setAttribute("tipoMensaje", "danger");
            request.getRequestDispatcher("/fragmentos/error.jsp").forward(request, response);
            return;
        }

        // 🔹 Redirigir según rol
        if ("administrador".equalsIgnoreCase(rol)) {
            request.getRequestDispatcher("/VerTablasValidadasServlet").forward(request, response);
        } else {
            request.getRequestDispatcher("/VerTablasDocenteServlet").forward(request, response);
        }
    }
}