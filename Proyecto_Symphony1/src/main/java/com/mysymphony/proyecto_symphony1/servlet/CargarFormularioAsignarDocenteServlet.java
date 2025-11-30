/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.mysymphony.proyecto_symphony1.servlet;

import com.mysymphony.proyecto_symphony1.util.Conexion;
import com.mysymphony.proyecto_symphony1.dao.BitacoraDAO;
import com.mysymphony.proyecto_symphony1.dao.AuditoriaDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

/**
 * Servlet para cargar el formulario institucional de asignar docente.
 * Rol: administrador
 * Autor: Camila
 * Trazabilidad:
 *   - Valida rol administrador
 *   - Redirige al JSP con el formulario
 *   - Registra acción en bitácora y auditoría institucional
 */
@WebServlet("/CargarFormularioAsignarDocenteServlet")
public class CargarFormularioAsignarDocenteServlet extends HttpServlet {

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
            // 📝 Registrar en bitácora institucional
            BitacoraDAO bitacoraDAO = new BitacoraDAO(conn);
            bitacoraDAO.registrarAccion("Administrador cargó formulario de asignar docente",
                    usuario, rol, "Asignación de Docentes");

            // 🛡️ Registrar en auditoría técnica
            Map<String, String> registro = new HashMap<>();
            registro.put("usuario", usuario);
            registro.put("rol", rol);
            registro.put("modulo", "Asignación de Docentes");
            registro.put("accion", "Cargó formulario institucional de asignar docente");
            new AuditoriaDAO(conn).registrarAccion(registro);

            System.out.println("✅ Formulario de asignar docente cargado por: " + usuario);

        } catch (Exception e) {
            System.err.println("❌ Error al registrar carga de formulario: " + e.getMessage());
        }

        // 🔁 Redirige al JSP que contiene el formulario
        request.getRequestDispatcher("/administrador/formularioAsignarDocente.jsp").forward(request, response);
    }
}