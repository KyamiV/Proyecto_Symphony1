/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.mysymphony.proyecto_symphony1.servlet;

/**
 * Servlet para gestionar los horarios de clases institucionales.
 * Rol: administrador
 * Autor: Camila
 * Trazabilidad: permite consultar, registrar, actualizar y eliminar horarios por clase
 */

import com.mysymphony.proyecto_symphony1.util.Conexion;
import com.mysymphony.proyecto_symphony1.dao.AuditoriaDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.*;
import java.util.*;

@WebServlet("/GestionarHorariosServlet")
public class GestionarHorariosServlet extends HttpServlet {

    // 🔍 Consulta de clases y horarios
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession sesion = request.getSession(false);
        String rol = (sesion != null) ? (String) sesion.getAttribute("rolActivo") : null;
        String usuario = (sesion != null) ? (String) sesion.getAttribute("nombreActivo") : "desconocido";

        if (rol == null || !"administrador".equalsIgnoreCase(rol)) {
            if (sesion != null) {
                sesion.setAttribute("mensaje", "⚠️ Acceso restringido: requiere rol administrador.");
                sesion.setAttribute("tipoMensaje", "danger");
            }
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        String claseId = request.getParameter("clase");
        List<Map<String, String>> clases = new ArrayList<>();
        List<Map<String, String>> horarios = new ArrayList<>();

        try (Connection conn = Conexion.getConnection()) {

            // 🧩 Cargar lista de clases
            try (PreparedStatement ps = conn.prepareStatement(
                    "SELECT id_clase, nombre_clase FROM clases ORDER BY nombre_clase");
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Map<String, String> c = new HashMap<>();
                    c.put("id_clase", rs.getString("id_clase"));
                    c.put("nombre", rs.getString("nombre_clase"));
                    clases.add(c);
                }
            }

            // 📅 Cargar horarios si hay clase seleccionada
            if (claseId != null && !claseId.isEmpty()) {
                try {
                    int idClase = Integer.parseInt(claseId);
                    String sql = "SELECT id_horario, dia_semana, fecha, hora_inicio, hora_fin, aula " +
                                 "FROM horarios_clase WHERE clase_id = ?";
                    try (PreparedStatement ps = conn.prepareStatement(sql)) {
                        ps.setInt(1, idClase);
                        try (ResultSet rs = ps.executeQuery()) {
                            while (rs.next()) {
                                Map<String, String> h = new HashMap<>();
                                h.put("id_horario", rs.getString("id_horario"));
                                h.put("dia", rs.getString("dia_semana"));
                                h.put("fecha", rs.getString("fecha")); // ✅ fecha exacta
                                h.put("inicio", rs.getString("hora_inicio"));
                                h.put("fin", rs.getString("hora_fin"));
                                h.put("aula", rs.getString("aula"));
                                horarios.add(h);
                            }
                        }
                    }
                } catch (NumberFormatException e) {
                    request.setAttribute("error", "⚠️ ID de clase inválido.");
                }
            }

            // 📝 Registro en auditoría institucional
            AuditoriaDAO auditoriaDAO = new AuditoriaDAO(conn);
            Map<String, String> registro = new HashMap<>();
            registro.put("usuario", usuario);
            registro.put("rol", rol);
            registro.put("modulo", "Horarios de clase");
            registro.put("accion", "Consultó horarios institucionales");
            registro.put("ip_origen", request.getRemoteAddr());
            auditoriaDAO.registrarAccion(registro);

        } catch (SQLException e) {
            request.setAttribute("error", "❌ Error al consultar horarios: " + e.getMessage());
        }

        // 📤 Enviar datos a la vista
        request.setAttribute("clases", clases);
        request.setAttribute("horarios", horarios);
        request.setAttribute("claseSeleccionada", claseId);
        request.getRequestDispatcher("/administrador/gestionarHorarios.jsp").forward(request, response);
    }

    // 📝 Registro, actualización o eliminación de horario
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession sesion = request.getSession();
        String claseId = request.getParameter("clase_id");
        String accion = request.getParameter("accion"); // puede ser "actualizar", "eliminar" o null (insertar)
        String idHorario = request.getParameter("id_horario");

        try (Connection conn = Conexion.getConnection()) {
            int filas = 0;

            if ("eliminar".equalsIgnoreCase(accion) && idHorario != null) {
                // 🗑️ Eliminar horario
                try (PreparedStatement ps = conn.prepareStatement("DELETE FROM horarios_clase WHERE id_horario = ?")) {
                    ps.setInt(1, Integer.parseInt(idHorario));
                    filas = ps.executeUpdate();
                }
                sesion.setAttribute("mensaje", (filas > 0) ? "✔ Horario eliminado correctamente." : "❌ No se encontró el horario a eliminar.");
                sesion.setAttribute("tipoMensaje", (filas > 0) ? "success" : "danger");

            } else if ("actualizar".equalsIgnoreCase(accion) && idHorario != null) {
                // ✏️ Actualizar horario existente
                String dia = request.getParameter("dia");
                String fecha = request.getParameter("fecha");
                String inicio = request.getParameter("hora_inicio");
                String fin = request.getParameter("hora_fin");
                String aula = request.getParameter("aula");

                String sql = "UPDATE horarios_clase SET dia_semana=?, fecha=?, hora_inicio=?, hora_fin=?, aula=? WHERE id_horario=?";
                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setString(1, dia);
                    ps.setDate(2, java.sql.Date.valueOf(fecha));
                    ps.setString(3, inicio);
                    ps.setString(4, fin);
                    ps.setString(5, aula);
                    ps.setInt(6, Integer.parseInt(idHorario));
                    filas = ps.executeUpdate();
                }
                sesion.setAttribute("mensaje", (filas > 0) ? "✔ Horario actualizado correctamente." : "❌ No se pudo actualizar el horario.");
                sesion.setAttribute("tipoMensaje", (filas > 0) ? "success" : "danger");

            } else {
                // ➕ Insertar nuevo horario por clase
                String dia = request.getParameter("dia");
                String fecha = request.getParameter("fecha");
                String inicio = request.getParameter("hora_inicio");
                String fin = request.getParameter("hora_fin");
                String aula = request.getParameter("aula");

                String sql = "INSERT INTO horarios_clase (clase_id, dia_semana, fecha, hora_inicio, hora_fin, aula) VALUES (?, ?, ?, ?, ?, ?)";
                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setInt(1, Integer.parseInt(claseId));
                    ps.setString(2, dia);
                    ps.setDate(3, java.sql.Date.valueOf(fecha));
                    ps.setString(4, inicio);
                    ps.setString(5, fin);
                    ps.setString(6, aula);
                    filas = ps.executeUpdate();
                }
                sesion.setAttribute("mensaje", (filas > 0) ? "✔ Horario registrado correctamente." : "❌ No se pudo registrar el horario.");
                sesion.setAttribute("tipoMensaje", (filas > 0) ? "success" : "danger");
            }

            // 📝 Registro en auditoría institucional
            AuditoriaDAO auditoriaDAO = new AuditoriaDAO(conn);
            Map<String, String> registro = new HashMap<>();
            registro.put("usuario", (String) sesion.getAttribute("nombreActivo"));
            registro.put("rol", (String) sesion.getAttribute("rolActivo"));
            registro.put("modulo", "Horarios de clase");
            registro.put("accion", (accion != null ? accion : "insertar") + " horario para clase ID " + claseId);
            registro.put("ip_origen", request.getRemoteAddr());
            auditoriaDAO.registrarAccion(registro);

        } catch (SQLException e) {
            sesion.setAttribute("mensaje", "❌ Error al gestionar horario: " + e.getMessage());
            sesion.setAttribute("tipoMensaje", "danger");
        }

        // 🔄 Redirección con persistencia de selección
        response.sendRedirect(request.getContextPath() + "/GestionarHorariosServlet?clase=" + claseId);
    }
}