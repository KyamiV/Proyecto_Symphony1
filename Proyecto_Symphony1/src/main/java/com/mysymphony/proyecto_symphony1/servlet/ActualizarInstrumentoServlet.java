/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.mysymphony.proyecto_symphony1.servlet;

/**
 * Servlet para actualizar el instrumento asignado a un estudiante.
 * Rol: docente
 * Autor: Camila
 * Trazabilidad: actualiza asignación y registra en bitácora y auditoría.
 */

import com.mysymphony.proyecto_symphony1.dao.AsignacionDAO;
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

@WebServlet("/ActualizarInstrumentoServlet")
public class ActualizarInstrumentoServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession sesion = request.getSession();
        String rol = (String) sesion.getAttribute("rolActivo");
        String usuario = (String) sesion.getAttribute("nombreActivo");

        if (rol == null || !"docente".equalsIgnoreCase(rol) || usuario == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        try (Connection conn = Conexion.getConnection()) {
            int idAsignacion = Integer.parseInt(request.getParameter("idAsignacion"));
            String nuevoInstrumento = request.getParameter("nuevoInstrumento");

            AsignacionDAO dao = new AsignacionDAO(conn);
            boolean actualizado = dao.actualizarInstrumentoAsignado(idAsignacion, nuevoInstrumento);

            if (actualizado) {
                // 📝 Bitácora institucional
                BitacoraDAO bitacoraDAO = new BitacoraDAO(conn);
                bitacoraDAO.registrarAccion(
                        "Instrumento actualizado a '" + nuevoInstrumento + "' en asignación ID " + idAsignacion,
                        usuario, rol, "Asignaciones"
                );

                // 🛡️ Auditoría técnica
                AuditoriaDAO auditoriaDAO = new AuditoriaDAO(conn);
                Map<String, String> registro = new HashMap<>();
                registro.put("usuario", usuario);
                registro.put("rol", rol);
                registro.put("modulo", "Asignaciones");
                registro.put("accion", "Docente actualizó instrumento en asignación ID " + idAsignacion);
                registro.put("ip_origen", request.getRemoteAddr());
                auditoriaDAO.registrarAccion(registro);

                System.out.println("✅ Instrumento actualizado en asignación ID=" + idAsignacion + " → " + nuevoInstrumento);
            }

            String mensaje = actualizado
                    ? "✅ Instrumento actualizado correctamente."
                    : "⚠️ No se pudo actualizar el instrumento.";

            response.sendRedirect(request.getContextPath() + "/VerAsignacionesServlet?mensaje=" +
                    java.net.URLEncoder.encode(mensaje, "UTF-8"));

        } catch (NumberFormatException e) {
            sesion.setAttribute("error", "❌ ID de asignación inválido.");
            response.sendRedirect(request.getContextPath() + "/VerAsignacionesServlet");
        } catch (Exception e) {
            sesion.setAttribute("error", "❌ Error interno: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/VerAsignacionesServlet");
        }
    }
}