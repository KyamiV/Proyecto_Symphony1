/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.mysymphony.proyecto_symphony1.servlet;

import com.mysymphony.proyecto_symphony1.dao.ClaseDAO;
import com.mysymphony.proyecto_symphony1.dao.AuditoriaDAO;
import com.mysymphony.proyecto_symphony1.util.Conexion;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * Servlet para actualizar fechas de clases desde el calendario institucional.
 * Rol: administrador
 * Autor: Camila
 * Trazabilidad: actualiza BD y registra acción en auditoría.
 */
@WebServlet("/ActualizarCalendarioServlet")
public class ActualizarCalendarioServlet extends HttpServlet {

    private static final DateTimeFormatter ISO_FORMATTER = DateTimeFormatter.ISO_DATE_TIME;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json; charset=UTF-8");
        Map<String, Object> resultado = new HashMap<>();

        // Obtener usuario de sesión
        HttpSession session = request.getSession(false);
        String usuario = (session != null && session.getAttribute("usuario") != null)
                ? session.getAttribute("usuario").toString()
                : "desconocido";

        try (BufferedReader reader = request.getReader();
             Connection conn = Conexion.getConnection()) {

            // Parsear JSON recibido desde FullCalendar
            JsonObject json = JsonParser.parseReader(reader).getAsJsonObject();
            int idClase = json.get("id_clase").getAsInt();
            String start = json.get("start").getAsString();
            String end = json.has("end") ? json.get("end").getAsString() : null;

            // Convertir fechas ISO-8601 a LocalDateTime
            LocalDateTime fechaInicio = LocalDateTime.parse(start, ISO_FORMATTER);
            LocalDateTime fechaFin = (end != null) ? LocalDateTime.parse(end, ISO_FORMATTER)
                    : fechaInicio.plusHours(1);

            // Actualizar clase en la base de datos
            ClaseDAO claseDAO = new ClaseDAO(conn);
            boolean actualizado = claseDAO.actualizarFechasClase(idClase, fechaInicio, fechaFin);

            if (actualizado) {
                // Registrar acción en auditoría
                AuditoriaDAO auditoriaDAO = new AuditoriaDAO(conn);
                auditoriaDAO.registrarAccion(crearRegistroAuditoria(
                        usuario,
                        "administrador",
                        "Actualizó fechas de clase ID " + idClase,
                        idClase
                ));

                resultado.put("success", true);
                resultado.put("message", "Clase actualizada correctamente");
            } else {
                resultado.put("success", false);
                resultado.put("message", "No se pudo actualizar la clase");
            }

        } catch (JsonParseException e) {
            resultado.put("success", false);
            resultado.put("message", "JSON inválido: " + e.getMessage());
        } catch (SQLException e) {
            resultado.put("success", false);
            resultado.put("message", "Error en la base de datos: " + e.getMessage());
        } catch (Exception e) {
            resultado.put("success", false);
            resultado.put("message", "Error en el servidor: " + e.getMessage());
        }

        // Enviar respuesta JSON
        try (PrintWriter out = response.getWriter()) {
            out.print(new Gson().toJson(resultado));
            out.flush();
        }
    }

    /**
     * Helper para crear registro de auditoría
     */
    private Map<String, String> crearRegistroAuditoria(String usuario, String rol, String accion, int tablaId) {
        Map<String, String> registro = new HashMap<>();
        registro.put("usuario", usuario);
        registro.put("rol", rol);
        registro.put("accion", accion);
        registro.put("modulo", "Calendario");
        registro.put("tabla_id", String.valueOf(tablaId));
        registro.put("referencia_id", null);
        return registro;
    }
}
