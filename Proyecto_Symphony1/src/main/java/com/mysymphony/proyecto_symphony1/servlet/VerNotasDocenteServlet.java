/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.mysymphony.proyecto_symphony1.servlet;

/**
 * Servlet para visualizar las notas registradas por el docente
 * Rol: docente
 * Autor: Camila
 * Trazabilidad: valida sesión, consulta notas y registra acceso en auditoría y bitácora
 */

import com.mysymphony.proyecto_symphony1.dao.NotaDAO;
import com.mysymphony.proyecto_symphony1.dao.AuditoriaDAO;
import com.mysymphony.proyecto_symphony1.dao.BitacoraDAO;
import com.mysymphony.proyecto_symphony1.util.Conexion;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import javax.servlet.RequestDispatcher;
import java.io.IOException;
import java.sql.Connection;
import java.util.*;

@WebServlet("/VerNotasDocenteServlet")
public class VerNotasDocenteServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession sesion = request.getSession(false);
        String rol = (sesion != null) ? (String) sesion.getAttribute("rolActivo") : null;
        String docente = (sesion != null) ? (String) sesion.getAttribute("nombreActivo") : null;
        Integer idDocente = (sesion != null) ? (Integer) sesion.getAttribute("idActivo") : null;

        if (rol == null || !"docente".equalsIgnoreCase(rol) || idDocente == null) {
            if (sesion != null) {
                sesion.setAttribute("mensaje", "⚠️ Acceso restringido: requiere rol docente.");
            }
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        try (Connection conn = Conexion.getConnection()) {
            NotaDAO dao = new NotaDAO(conn);

            // ✅ Usamos siempre idDocente para mayor seguridad
            List<Map<String, String>> notas = dao.obtenerNotasPorDocenteId(idDocente);

            request.setAttribute("notas", notas);

            // 📌 Extraer tablaId y claseId si están presentes en las notas
            Integer tablaId = null;
            Integer claseId = null;
            if (notas != null && !notas.isEmpty()) {
                String tablaIdStr = notas.get(0).get("tabla_id");
                String claseIdStr = notas.get(0).get("clase_id");

                if (tablaIdStr != null && !tablaIdStr.isEmpty()) {
                    tablaId = Integer.valueOf(tablaIdStr);
                }
                if (claseIdStr != null && !claseIdStr.isEmpty()) {
                    claseId = Integer.valueOf(claseIdStr);
                }
            }

            request.setAttribute("tablaId", tablaId);
            request.setAttribute("claseId", claseId);

            // 📝 Registro en auditoría institucional
            Map<String, String> registro = new HashMap<>();
            registro.put("usuario", docente + " (ID: " + idDocente + ")");
            registro.put("rol", rol);
            registro.put("modulo", "Consulta de notas");
            registro.put("accion", "Consultó notas registradas por docente");
            registro.put("id_referencia", String.valueOf(idDocente));
            registro.put("ip_origen", request.getRemoteAddr());
            new AuditoriaDAO(conn).registrarAccion(registro);

            // 📖 Registro en bitácora institucional
            BitacoraDAO bitacoraDAO = new BitacoraDAO(conn);
            bitacoraDAO.registrarAccion("Docente consultó sus notas institucionales",
                    docente, rol, "Consulta de notas");

        } catch (Exception e) {
            e.printStackTrace();
            sesion.setAttribute("mensaje", "❌ Error al consultar notas.");
            response.sendRedirect(request.getContextPath() + "/error.jsp");
            return;
        }

        RequestDispatcher dispatcher = request.getRequestDispatcher("/docente/verNotasDocente.jsp");
        dispatcher.forward(request, response);
    }
}