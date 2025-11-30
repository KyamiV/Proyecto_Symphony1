/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.mysymphony.proyecto_symphony1.servlet;

/**
 * Panel principal del docente con indicadores institucionales.
 * Autor: Camila
 * Trazabilidad:
 *   - Valida sesión y rol
 *   - Consulta notas del docente
 *   - Calcula indicadores institucionales
 *   - Registra acción en bitácora y auditoría
 *   - Envía datos a la vista dashboard.jsp
 */

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

@WebServlet("/DashboardServlet")
public class DashboardServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession sesion = request.getSession(false);
        String nombre = (sesion != null) ? (String) sesion.getAttribute("nombreActivo") : null;
        String rol = (sesion != null) ? (String) sesion.getAttribute("rolActivo") : null;
        Integer idDocente = (sesion != null) ? (Integer) sesion.getAttribute("idActivo") : null;

        // 🔐 Validación de sesión y rol
        if (nombre == null || rol == null || !"docente".equalsIgnoreCase(rol) || idDocente == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        try (Connection conn = Conexion.getConnection()) {
            NotaDAO dao = new NotaDAO(conn);
            List<Map<String, String>> notas = dao.obtenerNotasPorDocenteId(idDocente);

            // 📊 Calcular indicadores únicos
            Set<String> estudiantesUnicos = new HashSet<>();
            Set<String> instrumentosUnicos = new HashSet<>();

            for (Map<String, String> fila : notas) {
                String estudiante = fila.get("estudiante");
                String instrumento = fila.get("instrumento");
                if (estudiante != null && !estudiante.isEmpty()) {
                    estudiantesUnicos.add(estudiante);
                }
                if (instrumento != null && !instrumento.isEmpty()) {
                    instrumentosUnicos.add(instrumento);
                }
            }

            // 📝 Registro en bitácora institucional
            BitacoraDAO bitacoraDAO = new BitacoraDAO(conn);
            bitacoraDAO.registrarAccion("Docente accedió al panel principal con indicadores institucionales",
                    String.valueOf(idDocente), rol, "Panel Docente");

            // 🛡️ Registro en auditoría técnica
            Map<String, String> registro = new HashMap<>();
            registro.put("usuario", String.valueOf(idDocente));
            registro.put("rol", rol);
            registro.put("modulo", "Panel Docente");
            registro.put("accion", "Accedió al panel principal con indicadores institucionales");
            new AuditoriaDAO(conn).registrarAccion(registro);

            // 📤 Enviar datos a la vista
            request.setAttribute("totalNotas", notas.size());
            request.setAttribute("totalEstudiantes", estudiantesUnicos.size());
            request.setAttribute("totalInstrumentos", instrumentosUnicos.size());
            request.setAttribute("iconoRol", "fas fa-chalkboard-teacher");
            request.setAttribute("claseBoton", "btn-docente");

            System.out.println("✅ Panel docente cargado: " +
                    notas.size() + " notas, " +
                    estudiantesUnicos.size() + " estudiantes únicos, " +
                    instrumentosUnicos.size() + " instrumentos únicos.");

        } catch (Exception e) {
            e.printStackTrace();
            sesion.setAttribute("mensaje", "❌ Error al cargar el panel docente.");
            response.sendRedirect(request.getContextPath() + "/PanelDocenteServlet");
            return;
        }

        // 🔁 Redirigir a la vista ubicada en /webpages/
        request.getRequestDispatcher("/dashboard.jsp").forward(request, response);
    }
}