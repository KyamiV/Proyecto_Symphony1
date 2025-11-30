/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.mysymphony.proyecto_symphony1.servlet;

import com.mysymphony.proyecto_symphony1.util.Conexion;
import com.mysymphony.proyecto_symphony1.dao.AsignacionDAO;
import com.mysymphony.proyecto_symphony1.dao.BitacoraDAO;
import com.mysymphony.proyecto_symphony1.dao.AuditoriaDAO;
import com.mysymphony.proyecto_symphony1.dto.DocenteConClaseDTO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

/**
 * Servlet para cargar la vista institucional de asignaciones.
 * Rol: administrador
 * Autor: Camila
 * Trazabilidad:
 *   - Consulta asignaciones activas desde BD
 *   - Envía datos a la vista JSP
 *   - Registra acción en bitácora y auditoría institucional
 */
@WebServlet("/CargarAsignacionVistaServlet")
public class CargarAsignacionVistaServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession sesion = request.getSession(false);
        String rol = (sesion != null) ? (String) sesion.getAttribute("rolActivo") : null;
        String usuario = (sesion != null) ? (String) sesion.getAttribute("nombreActivo") : "desconocido";

        // 🔐 Validar rol administrador
        if (rol == null || !"administrador".equalsIgnoreCase(rol)) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        try (Connection conn = Conexion.getConnection()) {
            AsignacionDAO asignacionDAO = new AsignacionDAO(conn);

            // 📊 Consultar asignaciones institucionales
            List<DocenteConClaseDTO> asignaciones = asignacionDAO.obtenerAsignacionesInstitucionales();

            // 📤 Enviar datos a la vista JSP
            request.setAttribute("asignaciones", asignaciones);

            // 📝 Registrar en bitácora institucional
            BitacoraDAO bitacoraDAO = new BitacoraDAO(conn);
            bitacoraDAO.registrarAccion("Administrador cargó vista institucional de asignaciones",
                    usuario, rol, "Asignaciones");

            // 🛡️ Registrar en auditoría técnica
            Map<String, String> registro = new HashMap<>();
            registro.put("usuario", usuario);
            registro.put("rol", rol);
            registro.put("modulo", "Asignaciones");
            registro.put("accion", "Cargó vista institucional de asignaciones");
            new AuditoriaDAO(conn).registrarAccion(registro);

            // 🔁 Redirigir a la vista JSP
            request.getRequestDispatcher("/administrador/verAsignaciones.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            sesion.setAttribute("mensaje", "❌ Error al cargar asignaciones institucionales.");
            response.sendRedirect(request.getContextPath() + "/administrador/gestionarAsignaciones.jsp");
        }
    }
}