/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.mysymphony.proyecto_symphony1.servlet;

/**
 * Servlet para actualizar los metadatos de una tabla institucional.
 * Rol: docente
 * Autor: Camila
 * Trazabilidad: actualiza nombre y descripción de la tabla, registra fecha_actualizacion y usuario_editor,
 *               y documenta la acción en bitácora y auditoría institucional.
 */

import com.mysymphony.proyecto_symphony1.util.Conexion;
import com.mysymphony.proyecto_symphony1.dao.AuditoriaDAO;
import com.mysymphony.proyecto_symphony1.dao.BitacoraDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/ActualizarTablaServlet")
public class ActualizarTablaServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 🔐 Validar sesión y rol docente
        HttpSession sesion = request.getSession(false);
        if (sesion == null || sesion.getAttribute("rolActivo") == null ||
            !"docente".equalsIgnoreCase((String) sesion.getAttribute("rolActivo"))) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        // 📥 Obtener parámetros del formulario
        int tablaId;
        String nuevoNombre = request.getParameter("nombre");
        String nuevaDescripcion = request.getParameter("descripcion");
        Integer idDocente = (Integer) sesion.getAttribute("idDocente");
        String rol = (String) sesion.getAttribute("rolActivo");
        String ip = request.getRemoteAddr();

        // ✅ Validar ID de tabla
        try {
            tablaId = Integer.parseInt(request.getParameter("tablaId"));
        } catch (NumberFormatException e) {
            request.setAttribute("mensaje", "❌ ID de tabla no válido.");
            request.getRequestDispatcher("/VerTablasGuardadasServlet").forward(request, response);
            return;
        }

        boolean actualizada = false;

        String sql = "UPDATE tablas_guardadas SET nombre = ?, descripcion = ?, " +
                     "fecha_actualizacion = CURRENT_DATE, usuario_editor = ? WHERE id = ?";

        try (Connection conn = Conexion.getConnection()) {
            conn.setAutoCommit(false); // 🚦 Iniciar transacción segura

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, nuevoNombre);
                ps.setString(2, nuevaDescripcion);
                ps.setInt(3, idDocente);
                ps.setInt(4, tablaId);

                actualizada = ps.executeUpdate() > 0;
            }

            if (actualizada) {
                // 📝 Registrar en bitácora institucional
                BitacoraDAO bitacoraDAO = new BitacoraDAO(conn);
                bitacoraDAO.registrarAccion("Docente actualizó metadatos de tabla ID " + tablaId,
                        String.valueOf(idDocente), rol, "Tablas guardadas");

                // 🛡️ Registrar en auditoría técnica
                Map<String, String> registro = new HashMap<>();
                registro.put("usuario", String.valueOf(idDocente));
                registro.put("rol", rol);
                registro.put("accion", "Editó los metadatos de la tabla con ID " + tablaId);
                registro.put("modulo", "Tablas guardadas");
                registro.put("id_tabla", String.valueOf(tablaId));
                registro.put("id_referencia", null);
                registro.put("ip_origen", ip);

                new AuditoriaDAO(conn).registrarAccion(registro);

                conn.commit(); // ✅ Confirmar transacción
                request.setAttribute("mensaje", "✅ Tabla actualizada correctamente.");
                System.out.println("✅ Tabla actualizada: ID=" + tablaId + ", Nombre=" + nuevoNombre);

            } else {
                conn.rollback(); // ❌ Revertir cambios si no se actualizó
                request.setAttribute("mensaje", "❌ No se pudo actualizar la tabla.");
            }

        } catch (SQLException e) {
            System.err.println("❌ Error al actualizar tabla: " + e.getMessage());
            request.setAttribute("mensaje", "❌ Error interno al actualizar tabla.");
        }

        // 🔁 Redirigir a la vista de tablas guardadas
        request.getRequestDispatcher("/VerTablasGuardadasServlet").forward(request, response);
    }
}