/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.mysymphony.proyecto_symphony1.servlet;

/**
 * Servlet para guardar tabla de notas institucional por clase
 * y redirigir al listado de tablas guardadas del docente.
 * Autor: Camila
 * Trazabilidad: inserta tabla en BD y registra acción en auditoría y bitácora institucional.
 */

import com.mysymphony.proyecto_symphony1.util.Conexion;
import com.mysymphony.proyecto_symphony1.dao.AuditoriaDAO;
import com.mysymphony.proyecto_symphony1.dao.BitacoraDAO;
import com.mysymphony.proyecto_symphony1.dao.ClaseDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/GuardarTablaNotasServlet")
public class GuardarTablaNotasServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 🔹 1. Validación de sesión y rol
        HttpSession sesion = request.getSession(false);
        String rol = (sesion != null) ? (String) sesion.getAttribute("rolActivo") : null;
        String usuario = (sesion != null) ? (String) sesion.getAttribute("nombreActivo") : "desconocido";
        Integer idDocente = (sesion != null) ? (Integer) sesion.getAttribute("idActivo") : null;

        if (rol == null || !"docente".equalsIgnoreCase(rol) || idDocente == null) {
            sesion.setAttribute("tipoMensaje", "danger");
            sesion.setAttribute("mensaje", "⚠️ Acceso restringido: requiere rol docente.");
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        try {
            // 🔹 2. Captura de parámetros enviados desde el modal en registrarNotas.jsp
            String claseIdParam = request.getParameter("claseId");
            String nombreTabla = request.getParameter("nombreTabla");
            String observaciones = request.getParameter("observaciones");

            // Validaciones
            if (claseIdParam == null || claseIdParam.trim().isEmpty()) {
                sesion.setAttribute("tipoMensaje", "warning");
                sesion.setAttribute("mensaje", "⚠️ Debe seleccionar una clase antes de guardar la tabla.");
                response.sendRedirect(request.getContextPath() + "/docente/registrarNotas.jsp");
                return;
            }
            int idClase = Integer.parseInt(claseIdParam);

            if (nombreTabla == null || nombreTabla.trim().isEmpty()) {
                sesion.setAttribute("tipoMensaje", "warning");
                sesion.setAttribute("mensaje", "⚠️ El nombre de la tabla es obligatorio.");
                response.sendRedirect(request.getContextPath() + "/docente/registrarNotas.jsp");
                return;
            }

            int tablaId = -1;

            // 🔹 3. Inserción en la tabla institucional "tablas_guardadas"
            try (Connection conn = Conexion.getConnection()) {

                // Obtener datos completos de la clase
                ClaseDAO claseDAO = new ClaseDAO(conn);
                Map<String, String> datosClase = claseDAO.obtenerDatosClase(idClase);

                String aula = datosClase.get("aula");
                String dia = datosClase.get("dia"); // si lo tienes en la tabla clases_asignadas
                String instrumento = datosClase.get("instrumento");
                String etapa = datosClase.get("etapa");

                String sql = "INSERT INTO tablas_guardadas " +
                        "(id_clase, id_docente, nombre, descripcion, aula, dia, registrada_por, usuario_editor, fecha, enviada, validada, fecha_actualizacion) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, CURRENT_DATE, FALSE, 'No', CURRENT_TIMESTAMP)";

                try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                    ps.setInt(1, idClase);
                    ps.setInt(2, idDocente);
                    ps.setString(3, nombreTabla);
                    ps.setString(4, observaciones);
                    ps.setString(5, aula);
                    ps.setString(6, dia);
                    ps.setString(7, usuario); // registrada_por
                    ps.setString(8, usuario); // usuario_editor

                    if (ps.executeUpdate() > 0) {
                        try (ResultSet rs = ps.getGeneratedKeys()) {
                            if (rs.next()) {
                                tablaId = rs.getInt(1);
                            }
                        }
                    }

                    // 📝 Auditoría institucional
                    AuditoriaDAO auditoriaDAO = new AuditoriaDAO(conn);
                    Map<String, String> registro = new HashMap<>();
                    registro.put("usuario", usuario + " (ID: " + idDocente + ")");
                    registro.put("rol", rol);
                    registro.put("modulo", "Notas");
                    registro.put("accion", "Guardó tabla de notas para clase ID " + idClase);
                    registro.put("id_tabla", String.valueOf(tablaId));
                    registro.put("ip_origen", request.getRemoteAddr());
                    auditoriaDAO.registrarAccion(registro);

                    // 📖 Bitácora institucional
                    BitacoraDAO bitacoraDAO = new BitacoraDAO(conn);
                    bitacoraDAO.registrarAccion("Docente guardó tabla de notas: " + nombreTabla,
                            usuario, rol, "Notas");
                }
            }

            // 🔹 4. Redirección al flujo de tablas guardadas
            if (tablaId != -1) {
                sesion.setAttribute("tipoMensaje", "success");
                sesion.setAttribute("mensaje", "✔ Tabla guardada correctamente.");
                response.sendRedirect(request.getContextPath() + "/VerTablasDocenteServlet");
            } else {
                sesion.setAttribute("tipoMensaje", "danger");
                sesion.setAttribute("mensaje", "❌ No se pudo guardar la tabla.");
                response.sendRedirect(request.getContextPath() + "/docente/registrarNotas.jsp");
            }

        } catch (Exception e) {
            e.printStackTrace();
            sesion.setAttribute("tipoMensaje", "danger");
            sesion.setAttribute("mensaje", "❌ Error inesperado: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/error.jsp");
        }
    }
}