/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.mysymphony.proyecto_symphony1.servlet;

/**
 * Servlet para exportar asignaciones de estudiantes a docentes por instrumento en formato Excel.
 * Rol: administrador
 * Autor: Camila
 * Trazabilidad:
 *   - Valida sesión
 *   - Exporta datos a Excel
 *   - Registra acción en bitácora y auditoría institucional
 */

import com.mysymphony.proyecto_symphony1.util.Conexion;
import com.mysymphony.proyecto_symphony1.dao.AuditoriaDAO;
import com.mysymphony.proyecto_symphony1.dao.BitacoraDAO;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.*;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/ExportarAsignacionesServlet")
public class ExportarAsignacionesServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {

        // 🔐 Validación de sesión y rol
        HttpSession sesion = request.getSession(false);
        String rol = (sesion != null) ? (String) sesion.getAttribute("rolActivo") : null;
        String usuario = (sesion != null) ? (String) sesion.getAttribute("nombreActivo") : "desconocido";

        if (rol == null || !"administrador".equalsIgnoreCase(rol)) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        // 📥 Parámetros requeridos
        String docenteId = request.getParameter("docenteId");
        String instrumento = request.getParameter("instrumento");

        if (docenteId == null || instrumento == null || docenteId.isEmpty() || instrumento.isEmpty()) {
            sesion.setAttribute("mensaje", "❌ Faltan parámetros para exportar.");
            response.sendRedirect(request.getContextPath() + "/AsignarEstudiantesServlet");
            return;
        }

        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT u.nombre AS estudiante, a.instrumento, a.fecha_asignacion " +
                     "FROM asignaciones_docente a " +
                     "JOIN usuarios u ON a.id_estudiante = u.id " +
                     "WHERE a.id_docente = ? AND a.instrumento = ?")) {

            ps.setInt(1, Integer.parseInt(docenteId));
            ps.setString(2, instrumento);

            try (ResultSet rs = ps.executeQuery();
                 Workbook workbook = new XSSFWorkbook()) {

                // 📊 Crear hoja y encabezado
                Sheet sheet = workbook.createSheet("Asignaciones");
                Row header = sheet.createRow(0);
                header.createCell(0).setCellValue("Estudiante");
                header.createCell(1).setCellValue("Instrumento");
                header.createCell(2).setCellValue("Fecha de asignación");

                // 📄 Llenar filas con datos
                int rowNum = 1;
                while (rs.next()) {
                    Row row = sheet.createRow(rowNum++);
                    row.createCell(0).setCellValue(rs.getString("estudiante"));
                    row.createCell(1).setCellValue(rs.getString("instrumento"));
                    row.createCell(2).setCellValue(rs.getString("fecha_asignacion"));
                }

                // 📤 Configurar respuesta HTTP
                response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
                response.setHeader("Content-Disposition", 
                        "attachment; filename=asignaciones_docente_" + docenteId + ".xlsx");

                // 📝 Bitácora institucional
                BitacoraDAO bitacoraDAO = new BitacoraDAO(conn);
                bitacoraDAO.registrarAccion("Administrador exportó asignaciones del docente ID " + docenteId,
                        usuario, rol, "Asignaciones");

                // 🛡️ Auditoría técnica
                Map<String, String> registro = new HashMap<>();
                registro.put("usuario", usuario);
                registro.put("rol", rol);
                registro.put("modulo", "Asignaciones");
                registro.put("accion", "Exportación de asignaciones del docente ID " + docenteId + " (" + instrumento + ")");
                new AuditoriaDAO(conn).registrarAccion(registro);

                // 📦 Enviar archivo
                try (OutputStream out = response.getOutputStream()) {
                    workbook.write(out);
                }

                System.out.println("✅ Exportación realizada: docente ID=" + docenteId + ", instrumento=" + instrumento);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            sesion.setAttribute("mensaje", "❌ Error al generar el archivo: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/AsignarEstudiantesServlet");
        }
    }
}