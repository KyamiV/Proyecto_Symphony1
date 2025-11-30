/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.mysymphony.proyecto_symphony1.servlet;

/**
 * Servlet para exportar notas de una clase en formato Excel con trazabilidad institucional.
 * Rol: docente
 * Autor: Camila
 * Trazabilidad:
 *   - Valida sesión
 *   - Consulta notas por clase
 *   - Exporta a Excel
 *   - Registra acción en bitácora y auditoría institucional
 */

import com.mysymphony.proyecto_symphony1.dao.NotaDAO;
import com.mysymphony.proyecto_symphony1.dao.AuditoriaDAO;
import com.mysymphony.proyecto_symphony1.dao.BitacoraDAO;
import com.mysymphony.proyecto_symphony1.util.Conexion;
import com.mysymphony.proyecto_symphony1.modelo.Nota;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.util.*;

@WebServlet("/ExportarTablaNotasServlet")
public class ExportarTablaNotasServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {

        HttpSession sesion = request.getSession(false);
        String rol = (sesion != null) ? (String) sesion.getAttribute("rolActivo") : null;
        String usuario = (sesion != null) ? (String) sesion.getAttribute("nombreActivo") : "desconocido";
        Integer idDocente = (sesion != null) ? (Integer) sesion.getAttribute("idActivo") : null;

        // 🔐 Validar rol docente
        if (rol == null || !"docente".equalsIgnoreCase(rol) || idDocente == null) {
            request.setAttribute("mensaje", "⚠️ Acceso restringido: requiere rol docente.");
            request.setAttribute("tipoMensaje", "danger");
            request.getRequestDispatcher("/pages/error.jsp").forward(request, response);
            return;
        }

        int claseId;
        try {
            claseId = Integer.parseInt(request.getParameter("claseId"));
        } catch (NumberFormatException e) {
            request.setAttribute("mensaje", "⚠️ ID de clase inválido.");
            request.setAttribute("tipoMensaje", "danger");
            request.getRequestDispatcher("/pages/error.jsp").forward(request, response);
            return;
        }

        try (Connection conn = Conexion.getConnection()) {
            NotaDAO notaDAO = new NotaDAO(conn);
            List<Nota> notas = notaDAO.obtenerNotasPorClase(claseId);

            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Notas Clase " + claseId);

            // 🧩 Encabezado institucional
            Row header = sheet.createRow(0);
            String[] columnas = {"Estudiante", "Competencia", "Etapa", "Instrumento", "Nota", "Observación", "Fecha"};
            for (int i = 0; i < columnas.length; i++) {
                Cell cell = header.createCell(i);
                cell.setCellValue(columnas[i]);
            }

            // 📄 Cuerpo de datos
            int fila = 1;
            for (Nota nota : notas) {
                Row row = sheet.createRow(fila++);
                row.createCell(0).setCellValue(nota.getEstudiante() != null ? nota.getEstudiante() : "");
                row.createCell(1).setCellValue(nota.getCompetencia() != null ? nota.getCompetencia() : "");
                row.createCell(2).setCellValue(nota.getEtapa() != null ? nota.getEtapa() : "");
                row.createCell(3).setCellValue(nota.getInstrumento() != null ? nota.getInstrumento() : "");
                row.createCell(4).setCellValue(nota.getNota());
                row.createCell(5).setCellValue(nota.getObservacion() != null ? nota.getObservacion() : "");
                row.createCell(6).setCellValue(nota.getFecha() != null ? nota.getFecha().toString() : "");
            }

            // 📐 Ajustar ancho de columnas
            for (int i = 0; i < columnas.length; i++) {
                sheet.autoSizeColumn(i);
            }

            // 📝 Bitácora institucional
            new BitacoraDAO(conn).registrarAccion(
                    "Docente exportó notas de clase ID " + claseId,
                    usuario, rol, "Exportación de notas");

            // 🛡️ Auditoría técnica
            Map<String, String> registro = new HashMap<>();
            registro.put("usuario", String.valueOf(idDocente));
            registro.put("rol", rol);
            registro.put("modulo", "Exportación de notas");
            registro.put("accion", "Exportó notas de la clase con ID " + claseId);
            registro.put("referencia_id", String.valueOf(claseId));
            registro.put("ip_origen", request.getRemoteAddr());
            new AuditoriaDAO(conn).registrarAccion(registro);

            // 📤 Configurar respuesta HTTP
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=notas_clase_" + claseId + ".xlsx");

            try (OutputStream out = response.getOutputStream()) {
                workbook.write(out);
            }
            workbook.close();

            System.out.println("✅ Exportación realizada: clase ID=" + claseId + " por " + usuario);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("mensaje", "❌ Error al exportar notas: " + e.getMessage());
            request.setAttribute("tipoMensaje", "danger");
            request.getRequestDispatcher("/pages/error.jsp").forward(request, response);
        }
    }
}