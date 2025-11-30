/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mysymphony.proyecto_symphony1.servlet;

import com.mysymphony.proyecto_symphony1.util.Conexion;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.*;

/**
 * Servlet institucional para exportar una tabla validada en formato Excel (XLSX) con estilos.
 * Autor: Camila
 * Flujo: valida rol administrador, recibe id de la tabla y exporta datos completos.
 */
@WebServlet("/ExportarTablaValidadaServlet")
public class ExportarTablaValidadaServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession sesion = request.getSession(false);
        String rol = (sesion != null) ? (String) sesion.getAttribute("rolActivo") : null;

        // 🔐 Solo rol administrador
        if (rol == null || !"administrador".equalsIgnoreCase(rol)) {
            request.setAttribute("mensaje", "⚠️ Acceso restringido: requiere rol administrador.");
            request.setAttribute("tipoMensaje", "warning");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
            return;
        }

        // ✅ Ajuste: recibir parámetro como "tablaId"
        String idParam = request.getParameter("tablaId");
        if (idParam == null || idParam.isEmpty()) {
            request.setAttribute("mensaje", "❌ Error: parámetro 'tablaId' vacío.");
            request.setAttribute("tipoMensaje", "danger");
            request.getRequestDispatcher("/administrador/tablasValidadas.jsp").forward(request, response);
            return;
        }

        int idTabla = Integer.parseInt(idParam);

        try (Connection conn = Conexion.getConnection()) {
            String sqlMeta = "SELECT c.id_clase, c.nombre_clase, c.instrumento, c.etapa, c.grupo, c.cupo, " +
                             "c.fecha_inicio, c.fecha_fin, c.estado, u.nombre AS docente, t.fecha_validacion " +
                             "FROM tablas_guardadas t " +
                             "JOIN clases c ON t.id_clase = c.id_clase " +
                             "JOIN usuarios u ON t.id_docente = u.id_usuario " +
                             "WHERE t.id = ? AND t.validada = 'Sí'";

            try (PreparedStatement psMeta = conn.prepareStatement(sqlMeta)) {
                psMeta.setInt(1, idTabla);
                try (ResultSet rsMeta = psMeta.executeQuery()) {
                    if (rsMeta.next()) {
                        // 📤 Configurar respuesta como XLSX
                        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
                        response.setHeader("Content-Disposition", "attachment; filename=tabla_validada_" + idTabla + ".xlsx");

                        try (Workbook workbook = new XSSFWorkbook(); OutputStream out = response.getOutputStream()) {
                            // 🎨 Estilos institucionales
                            CellStyle headerStyle = workbook.createCellStyle();
                            Font headerFont = workbook.createFont();
                            headerFont.setBold(true);
                            headerFont.setColor(IndexedColors.WHITE.getIndex());
                            headerStyle.setFont(headerFont);
                            headerStyle.setFillForegroundColor(IndexedColors.DARK_GREEN.getIndex());
                            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                            headerStyle.setAlignment(HorizontalAlignment.CENTER);
                            headerStyle.setBorderBottom(BorderStyle.THIN);
                            headerStyle.setBorderTop(BorderStyle.THIN);
                            headerStyle.setBorderLeft(BorderStyle.THIN);
                            headerStyle.setBorderRight(BorderStyle.THIN);

                            CellStyle dataStyle = workbook.createCellStyle();
                            dataStyle.setBorderBottom(BorderStyle.THIN);
                            dataStyle.setBorderTop(BorderStyle.THIN);
                            dataStyle.setBorderLeft(BorderStyle.THIN);
                            dataStyle.setBorderRight(BorderStyle.THIN);

                            // 🔹 Hoja 1: Metadatos
                            Sheet sheetMeta = workbook.createSheet("Metadatos");
                            Row headerMeta = sheetMeta.createRow(0);
                            String[] headersMeta = {"ID Clase","Nombre Clase","Instrumento","Etapa","Grupo","Cupo","Fecha Inicio","Fecha Fin","Estado","Docente","Fecha Validación"};
                            for (int i = 0; i < headersMeta.length; i++) {
                                Cell cell = headerMeta.createCell(i);
                                cell.setCellValue(headersMeta[i]);
                                cell.setCellStyle(headerStyle);
                            }
                            Row dataMeta = sheetMeta.createRow(1);
                            dataMeta.createCell(0).setCellValue(rsMeta.getInt("id_clase"));
                            dataMeta.createCell(1).setCellValue(rsMeta.getString("nombre_clase"));
                            dataMeta.createCell(2).setCellValue(rsMeta.getString("instrumento"));
                            dataMeta.createCell(3).setCellValue(rsMeta.getString("etapa"));
                            dataMeta.createCell(4).setCellValue(rsMeta.getString("grupo"));
                            dataMeta.createCell(5).setCellValue(rsMeta.getInt("cupo"));
                            dataMeta.createCell(6).setCellValue(rsMeta.getDate("fecha_inicio").toString());
                            dataMeta.createCell(7).setCellValue(rsMeta.getDate("fecha_fin").toString());
                            dataMeta.createCell(8).setCellValue(rsMeta.getString("estado"));
                            dataMeta.createCell(9).setCellValue(rsMeta.getString("docente"));
                            dataMeta.createCell(10).setCellValue(rsMeta.getDate("fecha_validacion").toString());
                            for (int i = 0; i < headersMeta.length; i++) {
                                dataMeta.getCell(i).setCellStyle(dataStyle);
                                sheetMeta.autoSizeColumn(i);
                            }

                            // 🔹 Hoja 2: Notas
                            Sheet sheetNotas = workbook.createSheet("Notas");
                            Row headerNotas = sheetNotas.createRow(0);
                            String[] headersNotas = {"ID Estudiante","Nombre Estudiante","Nota"};
                            for (int i = 0; i < headersNotas.length; i++) {
                                Cell cell = headerNotas.createCell(i);
                                cell.setCellValue(headersNotas[i]);
                                cell.setCellStyle(headerStyle);
                            }

                            String sqlNotas = "SELECT e.id_estudiante, e.nombre AS estudiante, n.nota " +
                                              "FROM notas_clase n " +
                                              "JOIN estudiantes e ON n.id_estudiante = e.id_estudiante " +
                                              "WHERE n.id_tabla = ?";
                            try (PreparedStatement psNotas = conn.prepareStatement(sqlNotas)) {
                                psNotas.setInt(1, idTabla);
                                try (ResultSet rsNotas = psNotas.executeQuery()) {
                                    int rowIndex = 1;
                                    while (rsNotas.next()) {
                                        Row row = sheetNotas.createRow(rowIndex++);
                                        Cell c1 = row.createCell(0);
                                        c1.setCellValue(rsNotas.getInt("id_estudiante"));
                                        c1.setCellStyle(dataStyle);

                                        Cell c2 = row.createCell(1);
                                        c2.setCellValue(rsNotas.getString("estudiante"));
                                        c2.setCellStyle(dataStyle);

                                        Cell c3 = row.createCell(2);
                                        c3.setCellValue(rsNotas.getDouble("nota"));
                                        c3.setCellStyle(dataStyle);
                                    }
                                }
                            }

                            for (int i = 0; i < headersNotas.length; i++) {
                                sheetNotas.autoSizeColumn(i);
                            }

                            workbook.write(out);
                        }
                    } else {
                        request.setAttribute("mensaje", "⚠️ No se encontró la tabla validada con ID " + idTabla);
                        request.setAttribute("tipoMensaje", "warning");
                        request.getRequestDispatcher("/administrador/tablasValidadas.jsp").forward(request, response);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("mensaje", "❌ Error al exportar tabla validada: " + e.getMessage());
            request.setAttribute("tipoMensaje", "danger");
            request.getRequestDispatcher("/administrador/tablasValidadas.jsp").forward(request, response);
        }
    }
}