/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.mysymphony.proyecto_symphony1.servlet;

/**
 * Servlet para editar o registrar datos musicales del estudiante.
 * Rol: docente
 * Autor: Camila
 * Trazabilidad:
 *   - Valida sesión
 *   - Inserta o actualiza datos musicales
 *   - Registra acción en bitácora y auditoría institucional
 *   - Envía datos a la vista editarEstudiante.jsp
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

@WebServlet("/EditarEstudianteServlet")
public class EditarEstudianteServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession sesion = request.getSession();
        String nombre = (String) sesion.getAttribute("nombreActivo");
        String rol = (String) sesion.getAttribute("rolActivo");

        if (nombre == null || rol == null || !"docente".equalsIgnoreCase(rol)) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        String id = request.getParameter("id");
        String instrumento = request.getParameter("instrumento");
        String nivel = request.getParameter("nivel");
        String etapa = request.getParameter("etapa");

        String mensaje = "";
        String accion = "";

        try (Connection con = Conexion.getConnection()) {
            con.setAutoCommit(false); // 🚦 Manejo de transacción

            // Verificar si el estudiante ya tiene registro
            String sqlCheck = "SELECT COUNT(*) FROM estudiantes WHERE id_usuario = ?";
            boolean existe;
            try (PreparedStatement psCheck = con.prepareStatement(sqlCheck)) {
                psCheck.setString(1, id);
                try (ResultSet rs = psCheck.executeQuery()) {
                    existe = rs.next() && rs.getInt(1) > 0;
                }
            }

            if (existe) {
                // Actualizar datos
                String sqlUpdate = "UPDATE estudiantes SET instrumento = ?, nivel_tecnico = ?, etapa = ? WHERE id_usuario = ?";
                try (PreparedStatement psUpdate = con.prepareStatement(sqlUpdate)) {
                    psUpdate.setString(1, instrumento);
                    psUpdate.setString(2, nivel);
                    psUpdate.setString(3, etapa);
                    psUpdate.setString(4, id);
                    psUpdate.executeUpdate();
                    mensaje = "✅ Datos actualizados correctamente.";
                    accion = "Actualización de datos musicales del estudiante";
                }
            } else {
                // Insertar nuevo registro
                String sqlInsert = "INSERT INTO estudiantes (id_usuario, instrumento, nivel_tecnico, etapa) VALUES (?, ?, ?, ?)";
                try (PreparedStatement psInsert = con.prepareStatement(sqlInsert)) {
                    psInsert.setString(1, id);
                    psInsert.setString(2, instrumento);
                    psInsert.setString(3, nivel);
                    psInsert.setString(4, etapa);
                    psInsert.executeUpdate();
                    mensaje = "✅ Datos registrados correctamente.";
                    accion = "Registro inicial de datos musicales del estudiante";
                }
            }

            // 📝 Bitácora institucional
            BitacoraDAO bitacoraDAO = new BitacoraDAO(con);
            bitacoraDAO.registrarAccion(accion + " (ID: " + id + ")", nombre, rol, "Estudiantes");

            // 🛡️ Auditoría técnica
            Map<String, String> registro = new HashMap<>();
            registro.put("usuario", nombre);
            registro.put("rol", rol);
            registro.put("modulo", "Estudiantes");
            registro.put("accion", accion + " (ID: " + id + ")");
            new AuditoriaDAO(con).registrarAccion(registro);

            con.commit(); // ✅ Confirmar transacción

            // Obtener datos actualizados
            String sqlDatos = "SELECT u.id_usuario, u.nombre, e.instrumento, e.nivel_tecnico, e.etapa " +
                              "FROM usuarios u LEFT JOIN estudiantes e ON u.id_usuario = e.id_usuario WHERE u.id_usuario = ?";
            try (PreparedStatement psDatos = con.prepareStatement(sqlDatos)) {
                psDatos.setString(1, id);
                try (ResultSet rsDatos = psDatos.executeQuery()) {
                    Map<String, String> estudiante = new HashMap<>();
                    if (rsDatos.next()) {
                        estudiante.put("id", rsDatos.getString("id_usuario"));
                        estudiante.put("nombre", rsDatos.getString("nombre"));
                        estudiante.put("instrumento", rsDatos.getString("instrumento"));
                        estudiante.put("nivel", rsDatos.getString("nivel_tecnico"));
                        estudiante.put("etapa", rsDatos.getString("etapa"));
                    }
                    request.setAttribute("estudiante", estudiante);
                    request.setAttribute("mensaje", mensaje);
                    request.getRequestDispatcher("/docente/editarEstudiante.jsp").forward(request, response);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            sesion.setAttribute("mensaje", "❌ Error al actualizar los datos: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/GestionarEstudiantesServlet");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String id = request.getParameter("id");
        Map<String, String> estudiante = new HashMap<>();

        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(
                 "SELECT u.id_usuario, u.nombre, e.instrumento, e.nivel_tecnico, e.etapa " +
                 "FROM usuarios u LEFT JOIN estudiantes e ON u.id_usuario = e.id_usuario WHERE u.id_usuario = ?")) {

            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    estudiante.put("id", rs.getString("id_usuario"));
                    estudiante.put("nombre", rs.getString("nombre"));
                    estudiante.put("instrumento", rs.getString("instrumento"));
                    estudiante.put("nivel", rs.getString("nivel_tecnico"));
                    estudiante.put("etapa", rs.getString("etapa"));
                }
            }

            request.setAttribute("estudiante", estudiante);
            request.getRequestDispatcher("/docente/editarEstudiante.jsp").forward(request, response);

        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/GestionarEstudiantesServlet");
        }
    }
}