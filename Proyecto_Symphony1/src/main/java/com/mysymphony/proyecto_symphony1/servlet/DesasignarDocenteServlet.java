/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.mysymphony.proyecto_symphony1.servlet;

/**
 * Servlet para desasignar un docente de una clase.
 * Rol: administrador
 * Autor: Camila
 * 
 * Trazabilidad:
 *   - Valida sesión y rol
 *   - Elimina asignación en BD
 *   - Registra acción en bitácora y auditoría institucional
 */

import com.mysymphony.proyecto_symphony1.dao.ClaseDAO;
import com.mysymphony.proyecto_symphony1.dao.AuditoriaDAO;
import com.mysymphony.proyecto_symphony1.dao.BitacoraDAO;
import com.mysymphony.proyecto_symphony1.util.Conexion;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/DesasignarDocenteServlet")
public class DesasignarDocenteServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession sesion = request.getSession(false);
        String rol = (sesion != null) ? (String) sesion.getAttribute("rolActivo") : null;
        String usuario = (sesion != null) ? (String) sesion.getAttribute("nombreActivo") : "desconocido";

        // 🔐 Validar rol administrador
        if (rol == null || !"administrador".equalsIgnoreCase(rol)) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        try {
            // ✅ Validar parámetro idClase
            String idClaseParam = request.getParameter("idClase");
            if (idClaseParam == null || idClaseParam.isEmpty()) {
                sesion.setAttribute("mensaje", "❌ Parámetro idClase no recibido.");
                response.sendRedirect(request.getContextPath() + "/GestionarAsignacionesServlet");
                return;
            }

            int idClase = Integer.parseInt(idClaseParam);

            try (Connection conn = Conexion.getConnection()) {
                ClaseDAO dao = new ClaseDAO(conn);
                boolean ok = dao.desasignarDocenteDeClase(idClase);

                if (ok) {
                    sesion.setAttribute("mensaje", "✅ Docente desasignado correctamente.");

                    // 📝 Registro en bitácora institucional
                    BitacoraDAO bitacoraDAO = new BitacoraDAO(conn);
                    bitacoraDAO.registrarAccion(
                        "Administrador desasignó docente de clase ID " + idClase,
                        usuario, rol, "Gestión de clases"
                    );

                    // 🛡️ Auditoría institucional
                    Map<String, String> registro = new HashMap<>();
                    registro.put("usuario", usuario);
                    registro.put("rol", rol);
                    registro.put("modulo", "Gestión de clases");
                    registro.put("accion", "Desasignó docente de clase ID " + idClase);
                    registro.put("referencia_id", String.valueOf(idClase));
                    registro.put("ip_origen", request.getRemoteAddr());
                    new AuditoriaDAO(conn).registrarAccion(registro);

                    System.out.println("✅ Docente desasignado de clase ID=" + idClase + " por " + usuario);

                } else {
                    sesion.setAttribute("mensaje", "⚠️ No se pudo desasignar el docente.");
                }
            }

        } catch (Exception e) {
            sesion.setAttribute("mensaje", "❌ Error al desasignar docente: " + e.getMessage());
            e.printStackTrace();
        }

        // 📤 Redirigir al panel de gestión de asignaciones
        response.sendRedirect(request.getContextPath() + "/GestionarAsignacionesServlet");
    }
}