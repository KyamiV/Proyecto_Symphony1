/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.mysymphony.proyecto_symphony1.servlet;

/**
 * Servlet para registrar una nueva clase musical institucional.
 * Rol: administrador
 * Autor: Camila
 * Trazabilidad:
 *   - Carga docentes
 *   - Valida sesión
 *   - Registra clase
 *   - Asigna docente
 *   - Registra acción en bitácora y auditoría institucional
 */

import com.mysymphony.proyecto_symphony1.util.Conexion;
import com.mysymphony.proyecto_symphony1.dao.BitacoraDAO;
import com.mysymphony.proyecto_symphony1.dao.AuditoriaDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.*;
import java.util.*;

@WebServlet("/CrearClaseServlet")
public class CrearClaseServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession sesion = request.getSession(false);
        if (sesion == null || !"administrador".equalsIgnoreCase((String) sesion.getAttribute("rolActivo"))) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        List<Map<String, String>> docentes = new ArrayList<>();

        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT id, nombre FROM usuarios WHERE rol = 'docente'");
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Map<String, String> docente = new HashMap<>();
                docente.put("id", rs.getString("id"));
                docente.put("nombre", rs.getString("nombre"));
                docentes.add(docente);
            }

        } catch (SQLException e) {
            request.setAttribute("error", "❌ Error al cargar docentes: " + e.getMessage());
        }

        request.setAttribute("docentes", docentes);
        request.getRequestDispatcher("/administrador/crearClase.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession sesion = request.getSession();
        String usuario = (String) sesion.getAttribute("nombreActivo");
        String rol = (String) sesion.getAttribute("rolActivo");

        String nombre = request.getParameter("nombre");
        String instrumento = request.getParameter("instrumento");
        String etapa = request.getParameter("etapa");
        String grupo = request.getParameter("grupo");
        String docenteId = request.getParameter("docente");
        String dia = request.getParameter("dia");
        String inicio = request.getParameter("hora_inicio");
        String fin = request.getParameter("hora_fin");
        String cupoStr = request.getParameter("cupo");
        String fechaLimite = request.getParameter("fecha_limite");

        // Validación de campos obligatorios
        if (nombre == null || instrumento == null || etapa == null || docenteId == null ||
            dia == null || inicio == null || fin == null || grupo == null || cupoStr == null || fechaLimite == null ||
            nombre.isEmpty() || instrumento.isEmpty() || etapa.isEmpty() || docenteId.isEmpty() ||
            grupo.isEmpty() || cupoStr.isEmpty() || fechaLimite.isEmpty()) {

            request.setAttribute("error", "❌ Todos los campos obligatorios deben estar completos.");
            doGet(request, response);
            return;
        }

        Connection conn = null;
        try {
            conn = Conexion.getConnection();
            conn.setAutoCommit(false);

            int cupo = Integer.parseInt(cupoStr);
            int claseId = 0;

            // 1️⃣ Insertar clase y obtener ID generado
            String sqlClase = "INSERT INTO clases (nombre_clase, instrumento, etapa, grupo, cupo, fecha_limite, estado, fecha_creacion) " +
                              "VALUES (?, ?, ?, ?, ?, ?, 'activa', CURRENT_DATE)";
            try (PreparedStatement ps = conn.prepareStatement(sqlClase, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, nombre);
                ps.setString(2, instrumento);
                ps.setString(3, etapa);
                ps.setString(4, grupo);
                ps.setInt(5, cupo);

                if (fechaLimite != null && !fechaLimite.isEmpty()) {
                    ps.setDate(6, java.sql.Date.valueOf(fechaLimite));
                } else {
                    ps.setNull(6, java.sql.Types.DATE);
                }

                ps.executeUpdate();

                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    claseId = rs.getInt(1);
                }
            }

            if (docenteId == null || docenteId.isEmpty() || claseId <= 0) {
                throw new SQLException("❌ Error en IDs de clase o docente.");
            }

            // 2️⃣ Insertar asignación docente
            String sqlAsignacion = "INSERT INTO asignaciones_docente (id_docente, clase_id, fecha_asignacion) VALUES (?, ?, CURRENT_DATE)";
            try (PreparedStatement psAsig = conn.prepareStatement(sqlAsignacion)) {
                psAsig.setInt(1, Integer.parseInt(docenteId));
                psAsig.setInt(2, claseId);
                psAsig.executeUpdate();
            }

            // 3️⃣ Registrar en bitácora institucional
            BitacoraDAO bitacoraDAO = new BitacoraDAO(conn);
            bitacoraDAO.registrarAccion("Administrador creó clase '" + nombre + "' y asignó docente ID " + docenteId,
                    usuario, rol, "Clases");

            // 4️⃣ Registrar en auditoría técnica
            Map<String, String> registro = new HashMap<>();
            registro.put("usuario", usuario);
            registro.put("rol", rol);
            registro.put("modulo", "Clases");
            registro.put("accion", "Creación de clase '" + nombre + "' con instrumento " + instrumento +
                                   ", etapa " + etapa + ", grupo " + grupo + " y cupo " + cupo +
                                   " asignada al docente ID " + docenteId);
            new AuditoriaDAO(conn).registrarAccion(registro);

            conn.commit();
            sesion.setAttribute("mensaje", "✅ Clase y asignación de docente registradas correctamente.");
            response.sendRedirect(request.getContextPath() + "/AsignarEstudiantesServlet");

        } catch (SQLException e) {
            e.printStackTrace();
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    System.err.println("⚠️ Error en rollback: " + ex.getMessage());
                }
            }
            request.setAttribute("error", "❌ Error al registrar la clase y asignar docente: " + e.getMessage());
            doGet(request, response);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException ex) {
                    System.err.println("⚠️ Error al cerrar conexión: " + ex.getMessage());
                }
            }
        }
    }
}