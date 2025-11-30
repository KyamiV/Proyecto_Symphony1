/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.mysymphony.proyecto_symphony1.servlet;

/**
 * Servlet para eliminar una asignación docente-estudiante con trazabilidad institucional.
 * Rol: docente
 * Autor: Camila
 * Trazabilidad:
 *   - Valida sesión
 *   - Elimina asignación en BD
 *   - Registra acción en bitácora y auditoría institucional
 */

import com.mysymphony.proyecto_symphony1.dao.AuditoriaDAO;
import com.mysymphony.proyecto_symphony1.dao.BitacoraDAO;
import com.mysymphony.proyecto_symphony1.dao.AsignacionDAO;
import com.mysymphony.proyecto_symphony1.util.Conexion;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/EliminarAsignacionServlet")
public class EliminarAsignacionServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession sesion = request.getSession(false);
        String rol = (sesion != null) ? (String) sesion.getAttribute("rolActivo") : null;
        String usuario = (sesion != null) ? (String) sesion.getAttribute("nombreActivo") : "desconocido";

        // 🔐 Validar rol docente
        if (rol == null || !"docente".equalsIgnoreCase(rol) || usuario == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        String idStr = request.getParameter("id");
        boolean eliminado = false;

        if (idStr != null && !idStr.isEmpty()) {
            try {
                int idAsignacion = Integer.parseInt(idStr);

                try (Connection conn = Conexion.getConnection()) {
                    conn.setAutoCommit(false); // 🚦 Manejo de transacción
                    AsignacionDAO dao = new AsignacionDAO(conn);
                    eliminado = dao.eliminarAsignacion(idAsignacion);

                    if (eliminado) {
                        // 📝 Bitácora institucional
                        BitacoraDAO bitacoraDAO = new BitacoraDAO(conn);
                        bitacoraDAO.registrarAccion("Docente eliminó asignación ID " + idAsignacion,
                                usuario, rol, "Asignaciones");

                        // 🛡️ Auditoría técnica
                        AuditoriaDAO auditoriaDAO = new AuditoriaDAO(conn);
                        Map<String, String> registro = new HashMap<>();
                        registro.put("usuario", usuario);
                        registro.put("rol", rol);
                        registro.put("accion", "Eliminó asignación ID " + idAsignacion);
                        registro.put("modulo", "Asignaciones");
                        registro.put("referencia_id", String.valueOf(idAsignacion));
                        registro.put("ip_origen", request.getRemoteAddr());
                        auditoriaDAO.registrarAccion(registro);

                        conn.commit();
                        sesion.setAttribute("mensaje", "✅ Asignación eliminada correctamente.");
                        System.out.println("✅ Asignación eliminada: ID=" + idAsignacion + " por " + usuario);
                    } else {
                        conn.rollback();
                        sesion.setAttribute("mensaje", "⚠️ No se pudo eliminar la asignación.");
                    }
                }

            } catch (NumberFormatException e) {
                sesion.setAttribute("mensaje", "❌ ID inválido.");
                System.err.println("❌ ID inválido: " + e.getMessage());
            } catch (Exception e) {
                sesion.setAttribute("mensaje", "❌ Error al eliminar asignación.");
                e.printStackTrace();
            }
        }

        response.sendRedirect(request.getContextPath() + "/VerAsignacionesServlet");
    }
}