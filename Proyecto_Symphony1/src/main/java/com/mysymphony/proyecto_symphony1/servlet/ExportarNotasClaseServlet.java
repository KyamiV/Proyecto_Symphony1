/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.mysymphony.proyecto_symphony1.servlet;

/**
 * Servlet para exportar notas por clase en formato Excel con trazabilidad institucional.
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

@WebServlet("/ExportarNotasClaseServlet")
public class ExportarNotasClaseServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession sesion = request.getSession(false);
        String rol = (sesion != null) ? (String) sesion.getAttribute("rolActivo") : null;
        String usuario = (sesion != null) ? (String) sesion.getAttribute("nombreActivo") : "desconocido";

        // 🔐 Validar rol docente
        if (rol == null || !"docente".equalsIgnoreCase(rol)) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        try {
            int claseId = Integer.parseInt(request.getParameter("claseId"));

            try (Connection conn = Conexion.getConnection()) {
                NotaDAO dao = new NotaDAO(conn);

                // ✅ Obtener notas como modelo Nota
                List<Nota> notas = dao.obtenerNotasPorClase(claseId);

                Workbook workbook = new XSSFWorkbook();
                Sheet sheet = workbook.createSheet("Notas por clase");

                // 🧩 Encabezado institucional
                Row header = sheet.createRow(0);
                String[] columnas = {"Estudiante", "Competencia", "Nota", "Observación", "Fecha"};
                for (int i = 0; i < columnas.length; i++) {
                    Cell cell = header.createCell(i);
                    cell.setCellValue(columnas[i]);
                }

                // 📄 Cuerpo del archivo
                int fila = 1;
                for (Nota nota : notas) {
                    Row row = sheet.createRow(fila++);
                    row.createCell(0).setCellValue(nota.getEstudiante());
                    row.createCell(1).setCellValue(nota.getCompetencia());
                    row.createCell(2).setCellValue(nota.getNota());
                    row.createCell(3).setCellValue(nota.getObservacion());
                    row.createCell(4).setCellValue(
                        nota.getFecha() != null ? nota.getFecha().toString() : ""
                    );
                }

                // 📝 Bitácora institucional
                new BitacoraDAO(conn).registrarAccion(
                        "Docente exportó notas de clase ID " + claseId,
                        usuario, rol, "Notas por clase");

                // 🛡️ Auditoría técnica
                Map<String, String> registro = new HashMap<>();
                registro.put("usuario", usuario);
                registro.put("rol", rol);
                registro.put("modulo", "Notas por clase");
                registro.put("accion", "Exportó notas de la clase con ID " + claseId);
                new AuditoriaDAO(conn).registrarAccion(registro);

                // 📤 Configuración de respuesta
                response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
                response.setHeader("Content-Disposition", "attachment; filename=notas_clase_" + claseId + ".xlsx");

                try (OutputStream out = response.getOutputStream()) {
                    workbook.write(out);
                }
                workbook.close();

                System.out.println("✅ Exportación realizada: clase ID=" + claseId + " por " + usuario);
            }

        } catch (Exception e) {
            e.printStackTrace();
            sesion.setAttribute("mensaje", "❌ Error al exportar notas.");
            response.sendRedirect(request.getContextPath() + "/PanelDocenteServlet");
        }
    }
}