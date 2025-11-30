/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.mysymphony.proyecto_symphony1.servlet;

/**
 * Servlet para consultar las asignaciones de un docente.
 * Rol: docente
 * Autor: Camila
 * Trazabilidad: valida sesión, consulta asignaciones y registra acceso en auditoría y bitácora
 */

import com.mysymphony.proyecto_symphony1.dao.AsignacionDAO;
import com.mysymphony.proyecto_symphony1.dao.UsuarioDAO;
import com.mysymphony.proyecto_symphony1.dao.AuditoriaDAO;
import com.mysymphony.proyecto_symphony1.dao.BitacoraDAO;
import com.mysymphony.proyecto_symphony1.util.Conexion;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.util.*;

@WebServlet("/VerAsignacionesServlet")
public class VerAsignacionesServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession sesion = request.getSession(false);
        String rol = (sesion != null) ? (String) sesion.getAttribute("rolActivo") : null;
        String nombreDocente = (sesion != null) ? (String) sesion.getAttribute("nombreActivo") : null;

        if (rol == null || !"docente".equalsIgnoreCase(rol) || nombreDocente == null) {
            if (sesion != null) {
                sesion.setAttribute("mensaje", "⚠️ Acceso restringido: requiere rol docente.");
            }
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        try (Connection conn = Conexion.getConnection()) {
            UsuarioDAO usuarioDAO = new UsuarioDAO(conn);
            AsignacionDAO asignacionDAO = new AsignacionDAO(conn);
            AuditoriaDAO auditoriaDAO = new AuditoriaDAO(conn);
            BitacoraDAO bitacoraDAO = new BitacoraDAO(conn);

            int idDocente = usuarioDAO.obtenerIdPorNombre(nombreDocente);
            List<Map<String, String>> asignaciones = asignacionDAO.obtenerAsignacionesPorDocente(idDocente);

            request.setAttribute("asignaciones", asignaciones);

            // 📝 Registro en bitácora institucional
            bitacoraDAO.registrarAccion("Docente consultó sus asignaciones institucionales",
                    nombreDocente, rol, "Consulta de asignaciones");

            // 🛡️ Registro en auditoría institucional
            Map<String, String> registro = new HashMap<>();
            registro.put("usuario", nombreDocente);
            registro.put("rol", rol);
            registro.put("modulo", "Consulta de asignaciones");
            registro.put("accion", "Consultó asignaciones asignadas por el administrador");
            registro.put("referencia_id", String.valueOf(idDocente));
            registro.put("ip_origen", request.getRemoteAddr());
            auditoriaDAO.registrarAccion(registro);

        } catch (Exception e) {
            e.printStackTrace();
            sesion.setAttribute("mensaje", "❌ Error al consultar asignaciones.");
            response.sendRedirect(request.getContextPath() + "/error.jsp");
            return;
        }

        request.getRequestDispatcher("/docente/verAsignaciones.jsp").forward(request, response);
    }
}