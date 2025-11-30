/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.mysymphony.proyecto_symphony1.servlet;

/**
 * Servlet para mostrar el formulario de registro de clases y cargar la tabla con clases existentes.
 * Rol: administrador
 * Autor: Camila
 * Trazabilidad: valida sesión, consulta todas las clases desde ClaseDAO y envía a registrarClase.jsp
 */

import com.mysymphony.proyecto_symphony1.dao.ClaseDAO;
import com.mysymphony.proyecto_symphony1.dao.BitacoraDAO;
import com.mysymphony.proyecto_symphony1.dao.AuditoriaDAO;
import com.mysymphony.proyecto_symphony1.modelo.Clase;
import com.mysymphony.proyecto_symphony1.util.Conexion;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/MostrarFormularioClaseServlet")
public class MostrarFormularioClaseServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 🔐 Validación de sesión y rol
        HttpSession sesion = request.getSession(false);
        String rol = (sesion != null) ? (String) sesion.getAttribute("rolActivo") : null;
        String usuario = (sesion != null) ? (String) sesion.getAttribute("nombreActivo") : "desconocido";

        if (rol == null || !"administrador".equalsIgnoreCase(rol)) {
            if (sesion != null) {
                sesion.setAttribute("mensaje", "⚠️ Acceso restringido: requiere rol administrador.");
            }
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        try (Connection conn = Conexion.getConnection()) {
            ClaseDAO dao = new ClaseDAO(conn);

            // 📋 Obtener todas las clases registradas
            List<Clase> clasesRegistradas = dao.listarClasesDisponiblesParaInscripcion();
            request.setAttribute("clasesRegistradas", clasesRegistradas);


            // 📝 Registro en bitácora institucional
            BitacoraDAO bitacoraDAO = new BitacoraDAO(conn);
            bitacoraDAO.registrarAccion("Administrador accedió al formulario de registro de clases",
                    usuario, rol, "Gestión de clases");

            // 🛡️ Registro en auditoría técnica
            Map<String, String> registro = new HashMap<>();
            registro.put("usuario", usuario);
            registro.put("rol", rol);
            registro.put("modulo", "Gestión de clases");
            registro.put("accion", "Accedió al formulario de registro de clases");
            registro.put("ip_origen", request.getRemoteAddr());
            new AuditoriaDAO(conn).registrarAccion(registro);

        } catch (Exception e) {
            request.setAttribute("error", "❌ Error al cargar clases: " + e.getMessage());
        }

        // ➡️ Redirigir al JSP institucional
        request.getRequestDispatcher("/administrador/registrarClase.jsp").forward(request, response);
    }
}