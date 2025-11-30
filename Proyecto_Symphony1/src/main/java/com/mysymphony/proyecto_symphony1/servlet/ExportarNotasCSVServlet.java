/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.mysymphony.proyecto_symphony1.servlet;

/**
 * Servlet para exportar notas musicales filtradas en formato Excel.
 * Rol: administrador
 * Autor: Camila
 * Trazabilidad:
 *   - Valida sesión
 *   - Aplica filtros
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
import java.io.IOException;
import java.io.OutputStream;
import java.sql.*;
import java.util.*;

@WebServlet("/ExportarNotasCSServlet") // 🔄 Nombre ajustado para reflejar formato real
public class ExportarNotasCSVServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 🔐 Validación de sesión y rol
        HttpSession sesion = request.getSession(false);
        String rol = (sesion != null) ? (String) sesion.getAttribute("rolActivo") : null;
        String usuario = (sesion != null) ? (String) sesion.getAttribute("nombreActivo") : "desconocido";

        if (rol == null || !"administrador".equalsIgnoreCase(rol)) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        // 📥 Parámetros de filtro
        String docente = request.getParameter("docente");
        String etapa = request.getParameter("etapa");
        String instrumento = request.getParameter("instrumento");

        List<Map<String, String>> notas = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
            "SELECT estudiante, instrumento, etapa, nota, observaciones, docente, fecha_registro " +
            "FROM notas_musicales WHERE 1=1"
        );
        List<String> filtros = new ArrayList<>();

        // 🎯 Aplicar filtros dinámicos
        if (docente != null && !docente.trim().isEmpty()) {
            sql.append(" AND docente LIKE ?");
            filtros.add("%" + docente.trim() + "%");
        }
        if (etapa != null && !etapa.trim().isEmpty()) {
            sql.append(" AND etapa = ?");
            filtros.add(etapa.trim());
        }
        if (instrumento != null && !instrumento.trim().isEmpty()) {
            sql.append(" AND instrumento = ?");
            filtros.add(instrumento.trim());
        }
        sql.append(" ORDER BY estudiante, instrumento, etapa");

        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < filtros.size(); i++) {
                ps.setString(i + 1, filtros.get(i));
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Map<String, String> fila = new HashMap<>();
                    fila.put("estudiante", rs.getString("estudiante"));
                    fila.put("instrumento", rs.getString("instrumento"));
                    fila.put("etapa", rs.getString("etapa"));
                    fila.put("nota", rs.getString("nota"));
                    fila.put("observaciones", rs.getString("observaciones"));
                    fila.put("docente", rs.getString("docente"));
                    fila.put("fecha", rs.getString("fecha_registro"));
                    notas.add(fila);
                }
            }

            // 📝 Bitácora institucional
            BitacoraDAO bitacoraDAO = new BitacoraDAO(conn);
            bitacoraDAO.registrarAccion("Administrador exportó notas musicales filtradas",
                    usuario, rol, "Notas");

            // 🛡️ Auditoría técnica
            Map<String, String> registro = new HashMap<>();
            registro.put("usuario", usuario);
            registro.put("rol", rol);
            registro.put("modulo", "Notas");
            registro.put("accion", "Exportación de notas musicales filtradas");
            new AuditoriaDAO(conn).registrarAccion(registro);

        } catch (SQLException e) {
            e.printStackTrace();
            sesion.setAttribute("mensaje", "❌ Error al consultar notas para exportar: " + e.getMessage());
        }

        // 📊 Crear archivo Excel
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Notas Musicales");

        // 🎨 Estilo institucional para encabezado
        CellStyle estiloEncabezado = workbook.createCellStyle();
        Font fuente = workbook.createFont();
        fuente.setBold(true);
        fuente.setFontHeightInPoints((short) 12);
        estiloEncabezado.setFont(fuente);
        estiloEncabezado.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
        estiloEncabezado.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        // 🧩 Encabezado
        Row header = sheet.createRow(0);
        String[] columnas = {"Estudiante", "Instrumento", "Etapa", "Nota", "Observaciones", "Docente", "Fecha"};
        for (int i = 0; i < columnas.length; i++) {
            Cell cell = header.createCell(i);
            cell.setCellValue(columnas[i]);
            cell.setCellStyle(estiloEncabezado);
        }

        // 📄 Datos
        int fila = 1;
        for (Map<String, String> nota : notas) {
            Row row = sheet.createRow(fila++);
            row.createCell(0).setCellValue(nota.get("estudiante"));
            row.createCell(1).setCellValue(nota.get("instrumento"));
            row.createCell(2).setCellValue(nota.get("etapa"));
            row.createCell(3).setCellValue(nota.get("nota"));
            row.createCell(4).setCellValue(nota.get("observaciones"));
            row.createCell(5).setCellValue(nota.get("docente"));
            row.createCell(6).setCellValue(nota.get("fecha"));
        }

        // 📐 Ajuste automático de columnas
        for (int i = 0; i < columnas.length; i++) {
            sheet.autoSizeColumn(i);
        }

        // 📤 Configuración de respuesta HTTP
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=notas_musicales_admin.xlsx");

        try (OutputStream out = response.getOutputStream()) {
            workbook.write(out);
        }
        workbook.close();
    }
}