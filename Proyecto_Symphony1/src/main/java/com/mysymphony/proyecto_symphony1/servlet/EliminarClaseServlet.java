/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.mysymphony.proyecto_symphony1.servlet;

/**
 * Servlet institucional para eliminar clases desde el panel administrador.
 * Rol: administrador
 * Autor: Camila
 * Trazabilidad:
 *   - Valida sesión
 *   - Elimina clase en BD
 *   - Registra acción en bitácora y auditoría institucional
 */

import com.mysymphony.proyecto_symphony1.dao.ClaseDAO;
import com.mysymphony.proyecto_symphony1.dao.AuditoriaDAO;
import com.mysymphony.proyecto_symphony1.dao.BitacoraDAO;
import com.mysymphony.proyecto_symphony1.modelo.Clase;
import com.mysymphony.proyecto_symphony1.util.Conexion;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/EliminarClaseServlet")
public class EliminarClaseServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession sesion = request.getSession(false);
        String rol = (sesion != null) ? (String) sesion.getAttribute("rolActivo") : null;
        String usuario = (sesion != null) ? (String) sesion.getAttribute("nombreActivo") : "desconocido";

        // Validación de rol
        if (rol == null || !"administrador".equalsIgnoreCase(rol)) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        String idParam = request.getParameter("id");
        if (idParam == null || idParam.trim().isEmpty()) {
            sesion.setAttribute("mensaje", "❌ ID de clase inválido.");
            response.sendRedirect(request.getContextPath() + "/AsignarEstudiantesServlet");
            return;
        }

        int id;
        try {
            id = Integer.parseInt(idParam);
        } catch (NumberFormatException e) {
            sesion.setAttribute("mensaje", "❌ ID de clase inválido.");
            response.sendRedirect(request.getContextPath() + "/AsignarEstudiantesServlet");
            return;
        }

        String mensaje;

        try (Connection conn = Conexion.getConnection()) {
            conn.setAutoCommit(false); // Manejo de transacción
            ClaseDAO dao = new ClaseDAO(conn);

            // Obtener clase antes de eliminar para auditoría
            Clase clase = dao.obtenerClasePorId(id);

            boolean eliminado = dao.eliminarClase(id);

            if (eliminado) {
                mensaje = "✔ Clase eliminada correctamente.";

                // Bitácora institucional
                BitacoraDAO bitacoraDAO = new BitacoraDAO(conn);
                bitacoraDAO.registrarAccion("Administrador eliminó clase ID " + id,
                        usuario, rol, "Gestión de Clases");

                // Auditoría técnica
                Map<String, String> registro = new HashMap<>();
                registro.put("usuario", usuario);
                registro.put("rol", rol);
                registro.put("modulo", "Gestión de Clases");
                registro.put("accion", "Eliminó la clase con ID " + id +
                        (clase != null ? " (Nombre: " + clase.getNombre() + ")" : ""));
                new AuditoriaDAO(conn).registrarAccion(registro);

                conn.commit(); // Confirmar transacción
                System.out.println("✅ Clase eliminada: ID=" + id + " por " + usuario);

            } else {
                conn.rollback(); // Revertir si no se eliminó
                mensaje = "❌ No se pudo eliminar la clase.";
            }

        } catch (Exception e) {
            mensaje = "❌ Error al eliminar la clase: " + e.getMessage();
            e.printStackTrace();
        }

        sesion.setAttribute("mensaje", mensaje);
        response.sendRedirect(request.getContextPath() + "/GestionarClasesPrincipalServlet");
    }
}
