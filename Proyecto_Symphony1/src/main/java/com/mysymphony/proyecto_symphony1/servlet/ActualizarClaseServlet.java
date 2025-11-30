/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.mysymphony.proyecto_symphony1.servlet;

import com.mysymphony.proyecto_symphony1.dao.ClaseDAO;
import com.mysymphony.proyecto_symphony1.dao.BitacoraDAO;
import com.mysymphony.proyecto_symphony1.dao.AuditoriaDAO;
import com.mysymphony.proyecto_symphony1.util.Conexion;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

@WebServlet("/ActualizarClaseServlet")
public class ActualizarClaseServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 🔐 Validar sesión y rol
        HttpSession session = request.getSession();
        String rol = (String) session.getAttribute("rolActivo");
        String usuario = (String) session.getAttribute("nombreActivo");

        if (usuario == null || rol == null || !"administrador".equalsIgnoreCase(rol)) {
            response.sendRedirect("login.jsp");
            return;
        }

        // 📥 Obtener parámetros
        String idStr = request.getParameter("id");
        String nombre = request.getParameter("nombreClase");
        String instrumento = request.getParameter("instrumento");
        String etapa = request.getParameter("etapa");
        String grupo = request.getParameter("grupo");
        String cupoStr = request.getParameter("cupo");
        String fechaLimiteStr = request.getParameter("fecha_limite");
        String fechaInicioStr = request.getParameter("fecha_inicio");
        String fechaFinStr = request.getParameter("fecha_fin");

        // 🧼 Limpiar y validar
        grupo = grupo != null ? grupo.trim() : "";

        if (idStr == null || nombre == null || instrumento == null || etapa == null || grupo.isEmpty() ||
            cupoStr == null || fechaLimiteStr == null || fechaInicioStr == null || fechaFinStr == null ||
            idStr.isEmpty() || nombre.isEmpty() || instrumento.isEmpty() || etapa.isEmpty() ||
            cupoStr.isEmpty() || fechaLimiteStr.isEmpty() || fechaInicioStr.isEmpty() || fechaFinStr.isEmpty()) {

            request.setAttribute("error", "Todos los campos son obligatorios.");
            request.getRequestDispatcher("editarClase.jsp").forward(request, response);
            return;
        }

        int id, cupo;
        LocalDate fechaLimite;
        LocalDateTime fechaInicio;
        LocalDateTime fechaFin;

        try {
            id = Integer.parseInt(idStr);
            cupo = Integer.parseInt(cupoStr);
            fechaLimite = LocalDate.parse(fechaLimiteStr);
            fechaInicio = LocalDate.parse(fechaInicioStr).atStartOfDay();
            fechaFin = LocalDate.parse(fechaFinStr).atStartOfDay();

            if (cupo <= 0) {
                throw new NumberFormatException("El cupo debe ser mayor a 0.");
            }
            if (fechaFin.isBefore(fechaInicio)) {
                throw new DateTimeParseException("La fecha fin no puede ser anterior a la fecha de inicio.", fechaFinStr, 0);
            }
            if (!fechaLimite.isBefore(fechaInicio.toLocalDate())) {
                throw new DateTimeParseException("La fecha límite debe ser anterior a la fecha de inicio.", fechaLimiteStr, 0);
            }

        } catch (Exception e) {
            request.setAttribute("error", "Datos inválidos: " + e.getMessage());
            request.getRequestDispatcher("editarClase.jsp").forward(request, response);
            return;
        }

        // 🔌 Conexión
        Connection conn = Conexion.getConnection();
        if (conn == null) {
            request.setAttribute("error", "No se pudo conectar a la base de datos.");
            request.getRequestDispatcher("editarClase.jsp").forward(request, response);
            return;
        }

        try {
            // 🧱 Actualizar clase
            ClaseDAO claseDAO = new ClaseDAO(conn);
            boolean actualizada = claseDAO.actualizarClase(id, nombre, instrumento, etapa, grupo, cupo, fechaInicio, fechaFin, fechaLimite);

            if (actualizada) {
                // 📝 Bitácora
                BitacoraDAO bitacoraDAO = new BitacoraDAO(conn);
                bitacoraDAO.registrarAccion("Clase actualizada: " + nombre + " | Grupo: " + grupo, usuario, rol, "clases");

                // 🛡️ Auditoría
                AuditoriaDAO auditoriaDAO = new AuditoriaDAO(conn);
                auditoriaDAO.registrarAuditoria("Administrador actualizó clase '" + nombre + "' en grupo '" + grupo + "'", "Clases", usuario);

                session.setAttribute("mensaje", "✔ Clase actualizada correctamente.");
            } else {
                request.setAttribute("error", "No se pudo actualizar la clase.");
            }

        } catch (Exception e) {
            request.setAttribute("error", "Error interno: " + e.getMessage());
        } finally {
            try { conn.close(); } catch (Exception e) { System.out.println("Error al cerrar conexión: " + e.getMessage()); }
        }

        // 🔁 Redirigir
        response.sendRedirect("GestionarClasesServlet");
    }
}