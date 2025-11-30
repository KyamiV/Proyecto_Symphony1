/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.mysymphony.proyecto_symphony1.servlet;

/**
 * Servlet para exportar notas filtradas por instrumento y etapa en formato Excel con trazabilidad institucional.
 * Rol: docente
 * Autor: Camila
 * Trazabilidad:
 *   - Valida sesión
 *   - Aplica filtros
 *   - Exporta a Excel
 *   - Registra acción en bitácora y auditoría institucional
 */

import com.mysymphony.proyecto_symphony1.dao.NotaDAO;
import com.mysymphony.proyecto_symphony1.dao.AuditoriaDAO;
import com.mysymphony.proyecto_symphony1.dao.BitacoraDAO;
import com.mysymphony.proyecto_symphony1.util.Conexion;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.util.*;

@WebServlet("/ExportarNotasServlet")
public class ExportarNotasServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession sesion = request.getSession(false);
        String rol = (sesion != null) ? (String) sesion.getAttribute("rolActivo") : null;
        int docenteId = (sesion != null) ? (Integer) sesion.getAttribute("idDocenteActivo") : 0;
        String docenteNombre = (sesion != null) ? (String) sesion.getAttribute("nombreActivo") : "desconocido";

        // 🔐 Validar rol docente
        if (rol == null || !"docente".equalsIgnoreCase(rol) || docenteId == 0) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        String instrumento = request.getParameter("instrumento");
        String etapa = request.getParameter("etapa");

        try (Connection conn = Conexion.getConnection()) {
            NotaDAO dao = new NotaDAO(conn);
            List<Map<String, String>> notas = dao.obtenerNotasFiltradas(docenteId, instrumento, etapa);

            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Notas Musicales");

            // 🧩 Encabezado institucional
            Row header = sheet.createRow(0);
            String[] columnas = {"Estudiante", "Instrumento", "Etapa", "Nota", "Observacion", "Fecha"};
            for (int i = 0; i < columnas.length; i++) {
                Cell cell = header.createCell(i);
                cell.setCellValue(columnas[i]);
            }

            // 📄 Datos
            int fila = 1;
            for (Map<String, String> nota : notas) {
                Row row = sheet.createRow(fila++);
                row.createCell(0).setCellValue(nota.get("estudiante"));
                row.createCell(1).setCellValue(nota.get("instrumento"));
                row.createCell(2).setCellValue(nota.get("etapa"));
                row.createCell(3).setCellValue(nota.get("nota"));
                row.createCell(4).setCellValue(nota.get("observacion"));
                row.createCell(5).setCellValue(nota.get("fecha"));
            }

            // 📐 Ajuste automático de columnas
            for (int i = 0; i < columnas.length; i++) {
                sheet.autoSizeColumn(i);
            }

            // 📝 Bitácora institucional
            BitacoraDAO bitacoraDAO = new BitacoraDAO(conn);
            bitacoraDAO.registrarAccion("Docente exportó notas filtradas por instrumento '" + instrumento + "' y etapa '" + etapa + "'",
                    docenteNombre, rol, "Exportación de notas");

            // 🛡️ Auditoría técnica
            Map<String, String> registro = new HashMap<>();
            registro.put("usuario", String.valueOf(docenteId));
            registro.put("rol", rol);
            registro.put("modulo", "Exportación de notas");
            registro.put("accion", "Exportó notas filtradas por instrumento '" + instrumento + "' y etapa '" + etapa + "'");
            new AuditoriaDAO(conn).registrarAccion(registro);

            // 📤 Configuración de respuesta
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=notas_musicales.xlsx");

            try (OutputStream out = response.getOutputStream()) {
                workbook.write(out);
            }
            workbook.close();

            System.out.println("✅ Exportación realizada: instrumento=" + instrumento + ", etapa=" + etapa + " por " + docenteNombre);

        } catch (Exception e) {
            e.printStackTrace();
            sesion.setAttribute("mensaje", "❌ Error al exportar las notas.");
            response.sendRedirect(request.getContextPath() + "/PanelDocenteServlet");
        }
    }
}