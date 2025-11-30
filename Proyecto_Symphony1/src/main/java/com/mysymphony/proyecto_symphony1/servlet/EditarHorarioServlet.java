/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.mysymphony.proyecto_symphony1.servlet;

/**
 * Servlet para actualizar el horario de una clase institucional.
 * Rol: administrador
 * Autor: Camila
 * Trazabilidad:
 *   - Valida sesión
 *   - Actualiza horario
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

@WebServlet("/EditarHorarioServlet")
public class EditarHorarioServlet extends HttpServlet {
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
            int idClase = Integer.parseInt(request.getParameter("idClase"));
            String diaSemana = request.getParameter("dia_semana");
            String horaInicio = request.getParameter("hora_inicio");
            String horaFin = request.getParameter("hora_fin");

            try (Connection conn = Conexion.getConnection()) {
                conn.setAutoCommit(false); // 🚦 Manejo de transacción
                ClaseDAO dao = new ClaseDAO(conn);
                boolean ok = dao.actualizarHorarioClase(idClase, diaSemana, horaInicio, horaFin);

                if (ok) {
                    sesion.setAttribute("mensaje", "✅ Horario actualizado correctamente.");

                    // 📝 Bitácora institucional
                    BitacoraDAO bitacoraDAO = new BitacoraDAO(conn);
                    bitacoraDAO.registrarAccion("Administrador actualizó horario de clase ID " + idClase,
                            usuario, rol, "Gestión de clases");

                    // 🛡️ Auditoría técnica
                    Map<String, String> registro = new HashMap<>();
                    registro.put("usuario", usuario);
                    registro.put("rol", rol);
                    registro.put("modulo", "Gestión de clases");
                    registro.put("accion", "Actualizó horario de clase ID " + idClase +
                                           " a " + diaSemana + " " + horaInicio + "-" + horaFin);
                    new AuditoriaDAO(conn).registrarAccion(registro);

                    conn.commit(); // ✅ Confirmar transacción
                    System.out.println("✅ Horario actualizado: Clase ID=" + idClase +
                            " -> " + diaSemana + " " + horaInicio + "-" + horaFin);

                } else {
                    conn.rollback(); // ❌ Revertir si no se actualizó
                    sesion.setAttribute("mensaje", "⚠️ No se pudo actualizar el horario.");
                }
            }

        } catch (Exception e) {
            sesion.setAttribute("mensaje", "❌ Error al actualizar horario.");
            e.printStackTrace();
        }

        response.sendRedirect(request.getContextPath() + "/AsignarEstudiantesServlet");
    }
}