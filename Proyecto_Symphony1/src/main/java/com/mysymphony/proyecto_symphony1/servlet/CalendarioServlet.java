/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.mysymphony.proyecto_symphony1.servlet;

import com.mysymphony.proyecto_symphony1.dao.ClaseDAO;
import com.mysymphony.proyecto_symphony1.modelo.Clase;
import com.mysymphony.proyecto_symphony1.util.Conexion;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.*;

/**
 * Servlet para proveer eventos al calendario institucional (FullCalendar).
 * Rol: administrador
 * Autor: Camila
 * Trazabilidad: consulta clases en BD y devuelve JSON con eventos.
 */
@WebServlet("/CalendarioServlet")
public class CalendarioServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json; charset=UTF-8");

        List<Map<String, Object>> eventos = new ArrayList<>();

        try (Connection conn = Conexion.getConnection()) {
            ClaseDAO claseDAO = new ClaseDAO(conn);
            List<Clase> clases = claseDAO.listarClasesConFechas();

            for (Clase clase : clases) {
                Map<String, Object> evento = new HashMap<>();
                evento.put("id_clase", clase.getId());
                evento.put("title", clase.getNombre() + " - " + clase.getInstrumento());

                if (clase.getFechaInicio() != null) {
                    evento.put("start", clase.getFechaInicio().toString());
                }
                if (clase.getFechaFin() != null) {
                    evento.put("end", clase.getFechaFin().toString());
                }

                // extendedProps
                Map<String, Object> props = new HashMap<>();
                props.put("docente", clase.getDocenteNombre());
                props.put("etapa", clase.getEtapa());
                props.put("grupo", clase.getGrupo());
                evento.put("extendedProps", props);

                eventos.add(evento);
            }

        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> error = new HashMap<>();
            error.put("error", "No se pudieron cargar las clases: " + e.getMessage());
            eventos.add(error);
        }

        try (PrintWriter out = response.getWriter()) {
            Gson gson = new Gson();
            out.print(gson.toJson(eventos));
            out.flush();
        }
    }
}
