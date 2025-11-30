/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.mysymphony.proyecto_symphony1.servlet;

import com.mysymphony.proyecto_symphony1.util.Conexion;
import com.mysymphony.proyecto_symphony1.dao.NotaDAO;
import com.mysymphony.proyecto_symphony1.dao.ClaseDAO;
import com.mysymphony.proyecto_symphony1.dao.AuditoriaDAO;
import com.mysymphony.proyecto_symphony1.dao.BitacoraDAO;
import com.mysymphony.proyecto_symphony1.modelo.Nota;
import com.mysymphony.proyecto_symphony1.modelo.Estudiante;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import javax.servlet.RequestDispatcher;
import java.io.IOException;
import java.sql.Connection;
import java.util.*;

@WebServlet("/VerNotasClaseServlet")
public class VerNotasClaseServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession sesion = request.getSession(false);
        String rol = (sesion != null) ? (String) sesion.getAttribute("rolActivo") : null;
        String nombreDocente = (sesion != null) ? (String) sesion.getAttribute("nombreActivo") : "desconocido";
        Integer idDocente = (sesion != null) ? (Integer) sesion.getAttribute("idActivo") : null;

        // 🔐 Validar rol docente
        if (rol == null || !"docente".equalsIgnoreCase(rol) || idDocente == null) {
            sesion.setAttribute("tipoMensaje", "warning");
            sesion.setAttribute("mensaje", "⚠️ Acceso restringido: requiere rol docente.");
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        String claseIdStr = request.getParameter("claseId");
        int claseId;
        try {
            claseId = Integer.parseInt(claseIdStr);
        } catch (Exception e) {
            sesion.setAttribute("tipoMensaje", "danger");
            sesion.setAttribute("mensaje", "⚠️ Clase no válida.");
            response.sendRedirect(request.getContextPath() + "/error.jsp");
            return;
        }

        try (Connection conn = Conexion.getConnection()) {
            NotaDAO notaDAO = new NotaDAO(conn);
            ClaseDAO claseDAO = new ClaseDAO(conn);

            // 📋 Consultar notas de la clase
            List<Nota> notas = notaDAO.obtenerNotasPorClase(claseId);

            // 🔹 Consultar estudiantes inscritos en la clase
            List<Estudiante> estudiantes = notaDAO.obtenerEstudiantesPorClase(claseId);

            // 📑 Obtener datos completos de la clase
            Map<String, String> datosClase = claseDAO.obtenerDatosClase(claseId);
            String nombreClase = datosClase.getOrDefault("nombre", "Sin nombre");
            String aula = datosClase.getOrDefault("aula", "Sin aula");
            String instrumento = datosClase.getOrDefault("instrumento", "Sin instrumento");
            String etapa = datosClase.getOrDefault("etapa", "Sin etapa");
            String horario = (datosClase.get("dia") != null ? datosClase.get("dia") : "") + " " +
                             (datosClase.get("inicio") != null ? datosClase.get("inicio") : "") + " - " +
                             (datosClase.get("fin") != null ? datosClase.get("fin") : "");

            // 📖 Bitácora
            new BitacoraDAO(conn).registrarAccion(
                    "Docente consultó notas de la clase ID " + claseId,
                    nombreDocente, rol, "Notas por clase");

            // 🛡️ Auditoría
            Map<String, String> registro = new HashMap<>();
            registro.put("usuario", nombreDocente + " (ID: " + idDocente + ")");
            registro.put("rol", rol);
            registro.put("modulo", "Notas por clase");
            registro.put("accion", "Consultó notas de la clase ID " + claseId);
            registro.put("ip_origen", request.getRemoteAddr());
            new AuditoriaDAO(conn).registrarAccion(registro);

            // 📤 Enviar datos al JSP
            request.setAttribute("notas", notas);
            request.setAttribute("estudiantes", estudiantes);
            request.setAttribute("claseId", claseId);
            request.setAttribute("nombreClase", nombreClase);
            request.setAttribute("aula", aula);
            request.setAttribute("instrumento", instrumento);
            request.setAttribute("etapa", etapa);
            request.setAttribute("horario", horario);

            // ⚠️ Si no manejas tablas guardadas, no envíes tablaId
            // request.setAttribute("tablaId", null);

            // 👉 Despachar al JSP institucional correcto
            RequestDispatcher dispatcher = request.getRequestDispatcher("/docente/registrarNotas.jsp");
            dispatcher.forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            sesion.setAttribute("tipoMensaje", "danger");
            sesion.setAttribute("mensaje", "❌ Error al cargar notas de la clase: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/error.jsp");
        }
    }
}