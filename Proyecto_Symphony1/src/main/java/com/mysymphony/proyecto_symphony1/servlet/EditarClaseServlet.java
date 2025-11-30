/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.mysymphony.proyecto_symphony1.servlet;

/**
 * Servlet para editar clases institucionales.
 * Rol: administrador
 * Autor: Camila
 * Trazabilidad:
 *   - Recibe datos
 *   - Actualiza clase en BD
 *   - Registra acción en bitácora y auditoría institucional
 */

import com.mysymphony.proyecto_symphony1.dao.ClaseDAO;
import com.mysymphony.proyecto_symphony1.dao.AuditoriaDAO;
import com.mysymphony.proyecto_symphony1.dao.BitacoraDAO;
import com.mysymphony.proyecto_symphony1.modelo.Clase;
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

@WebServlet("/EditarClaseServlet")
public class EditarClaseServlet extends HttpServlet {

    // ✅ GET: Cargar datos de clase para edición
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession sesion = request.getSession(false);
        String rol = (sesion != null) ? (String) sesion.getAttribute("rolActivo") : null;

        if (rol == null || !"administrador".equalsIgnoreCase(rol)) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        int id;
        try {
            id = Integer.parseInt(request.getParameter("id"));
        } catch (NumberFormatException e) {
            if (sesion != null) {
                sesion.setAttribute("mensaje", "❌ ID de clase inválido.");
            }
            response.sendRedirect(request.getContextPath() + "/GestionarClasesServlet");
            return;
        }

        try (Connection conn = Conexion.getConnection()) {
            ClaseDAO dao = new ClaseDAO(conn);
            Clase clase = dao.obtenerClasePorId(id);

            if (clase != null) {
                request.setAttribute("clase", clase);
                request.getRequestDispatcher("/administrador/editarClase.jsp").forward(request, response);
            } else {
                if (sesion != null) {
                    sesion.setAttribute("mensaje", "❌ Clase no encontrada.");
                }
                response.sendRedirect(request.getContextPath() + "/GestionarClasesServlet");
            }

        } catch (Exception e) {
            if (sesion != null) {
                sesion.setAttribute("mensaje", "❌ Error al consultar clase.");
            }
            response.sendRedirect(request.getContextPath() + "/GestionarClasesServlet");
            e.printStackTrace();
        }
    }

    // ✅ POST: Actualizar clase en base de datos
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

        int id;
        try {
            id = Integer.parseInt(request.getParameter("id"));
        } catch (NumberFormatException e) {
            if (sesion != null) {
                sesion.setAttribute("mensaje", "❌ ID inválido.");
            }
            response.sendRedirect(request.getContextPath() + "/GestionarClasesServlet");
            return;
        }

        String nombre = request.getParameter("nombreClase");
        String instrumento = request.getParameter("instrumento");
        String etapa = request.getParameter("etapa");
        String grupo = request.getParameter("grupo");
        String cupoStr = request.getParameter("cupo");
        String fechaLimiteStr = request.getParameter("fecha_limite");
        String fechaInicioStr = request.getParameter("fecha_inicio");
        String fechaFinStr = request.getParameter("fecha_fin");

        nombre = (nombre != null) ? nombre.trim() : "";
        instrumento = (instrumento != null) ? instrumento.trim() : "";
        etapa = (etapa != null) ? etapa.trim() : "";
        grupo = (grupo != null) ? grupo.trim() : "";

        int cupo = 0;
        LocalDate fechaLimite = null;
        LocalDateTime fechaInicio = null;
        LocalDateTime fechaFin = null;

        try {
            // Validar cupo
            if (cupoStr != null && !cupoStr.isEmpty()) {
                cupo = Integer.parseInt(cupoStr);
                if (cupo <= 0) {
                    throw new NumberFormatException("El cupo debe ser mayor a 0.");
                }
            } else {
                throw new NumberFormatException("El cupo no puede estar vacío.");
            }

            // Validar fechas
            if (fechaLimiteStr != null && !fechaLimiteStr.isEmpty()) {
                fechaLimite = LocalDate.parse(fechaLimiteStr);
            } else {
                throw new DateTimeParseException("La fecha límite es obligatoria.", fechaLimiteStr, 0);
            }

            if (fechaInicioStr != null && !fechaInicioStr.isEmpty()) {
                fechaInicio = LocalDate.parse(fechaInicioStr).atStartOfDay();
            } else {
                throw new DateTimeParseException("La fecha inicio es obligatoria.", fechaInicioStr, 0);
            }

            if (fechaFinStr != null && !fechaFinStr.isEmpty()) {
                fechaFin = LocalDate.parse(fechaFinStr).atStartOfDay();
            } else {
                throw new DateTimeParseException("La fecha fin es obligatoria.", fechaFinStr, 0);
            }

            // Validaciones de lógica
            if (fechaFin.isBefore(fechaInicio)) {
                throw new DateTimeParseException("La fecha fin no puede ser anterior a la fecha de inicio.", fechaFinStr, 0);
            }
            if (!fechaLimite.isBefore(fechaInicio.toLocalDate())) {
                throw new DateTimeParseException("La fecha límite debe ser anterior a la fecha de inicio.", fechaLimiteStr, 0);
            }

        } catch (Exception e) {
            if (sesion != null) {
                sesion.setAttribute("mensaje", "❌ Datos inválidos: " + e.getMessage());
            }
            response.sendRedirect(request.getContextPath() + "/GestionarClasesServlet");
            return;
        }

        String mensaje;

        try (Connection conn = Conexion.getConnection()) {
            conn.setAutoCommit(false);
            ClaseDAO dao = new ClaseDAO(conn);
            boolean actualizado = dao.actualizarClase(id, nombre, instrumento, etapa, grupo, cupo, fechaInicio, fechaFin, fechaLimite);

            if (actualizado) {
                mensaje = "✔ Clase actualizada correctamente.";

                new BitacoraDAO(conn).registrarAccion(
                        "Administrador editó clase ID " + id + " (" + nombre + ")",
                        usuario, rol, "Gestión de Clases");

                Map<String, String> registro = new HashMap<>();
                registro.put("usuario", usuario);
                registro.put("rol", rol);
                registro.put("modulo", "Gestión de Clases");
                registro.put("accion", "Editó la clase con ID " + id + " (" + nombre + ")");
                new AuditoriaDAO(conn).registrarAccion(registro);

                conn.commit();
                System.out.println("✅ Clase actualizada: ID=" + id + ", Nombre=" + nombre + " por " + usuario);

            } else {
                conn.rollback();
                mensaje = "❌ Error al actualizar la clase.";
            }

        } catch (Exception e) {
            mensaje = "❌ Error al conectar con la base de datos.";
            e.printStackTrace();
        }

        if (sesion != null) {
            sesion.setAttribute("mensaje", mensaje);
        }
        response.sendRedirect(request.getContextPath() + "/GestionarClasesServlet");
    }
}