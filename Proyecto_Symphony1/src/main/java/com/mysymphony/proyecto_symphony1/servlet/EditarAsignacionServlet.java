/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.mysymphony.proyecto_symphony1.servlet;

/**
 * Servlet para editar una asignación institucional.
 * Rol: docente
 * Autor: Camila
 * Trazabilidad:
 *   - Valida sesión
 *   - Edita asignación en BD
 *   - Registra acción en bitácora y auditoría
 *   - Envía resultado a la vista verAsignaciones.jsp
 */

import com.mysymphony.proyecto_symphony1.dao.AsignacionDAO;
import com.mysymphony.proyecto_symphony1.dao.BitacoraDAO;
import com.mysymphony.proyecto_symphony1.dao.AuditoriaDAO;
import com.mysymphony.proyecto_symphony1.util.Conexion;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/EditarAsignacionServlet")
public class EditarAsignacionServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession sesion = request.getSession();
        String rol = (String) sesion.getAttribute("rolActivo");
        String usuario = (String) sesion.getAttribute("nombreActivo");

        // 🔐 Validar rol docente
        if (rol == null || !"docente".equalsIgnoreCase(rol)) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        try {
            int idAsignacion = Integer.parseInt(request.getParameter("idAsignacion"));
            String nuevoInstrumento = request.getParameter("instrumento");
            int nuevaClaseId = Integer.parseInt(request.getParameter("claseId"));
            String nuevaFecha = request.getParameter("fecha");

            try (Connection conn = Conexion.getConnection()) {
                AsignacionDAO dao = new AsignacionDAO(conn);
                boolean actualizado = dao.editarAsignacion(idAsignacion, nuevoInstrumento, nuevaClaseId, nuevaFecha);

                if (actualizado) {
                    sesion.setAttribute("mensaje", "✅ Asignación editada correctamente.");

                    // 📝 Bitácora institucional
                    BitacoraDAO bitacoraDAO = new BitacoraDAO(conn);
                    bitacoraDAO.registrarAccion("Docente editó asignación ID " + idAsignacion,
                            usuario, rol, "Asignaciones");

                    // 🛡️ Auditoría técnica
                    Map<String, String> registro = new HashMap<>();
                    registro.put("usuario", usuario);
                    registro.put("rol", rol);
                    registro.put("modulo", "Asignaciones");
                    registro.put("accion", "Editó asignación ID " + idAsignacion +
                            " con instrumento " + nuevoInstrumento +
                            ", clase ID " + nuevaClaseId +
                            " y fecha " + nuevaFecha);
                    new AuditoriaDAO(conn).registrarAccion(registro);

                } else {
                    sesion.setAttribute("mensaje", "⚠️ No se pudo editar la asignación.");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            sesion.setAttribute("mensaje", "❌ Error al procesar la edición: " + e.getMessage());
        }

        request.getRequestDispatcher("/docente/verAsignaciones.jsp").forward(request, response);
    }
}