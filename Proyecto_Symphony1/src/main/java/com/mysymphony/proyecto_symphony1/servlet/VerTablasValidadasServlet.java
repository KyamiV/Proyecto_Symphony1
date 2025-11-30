/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.mysymphony.proyecto_symphony1.servlet;

import com.mysymphony.proyecto_symphony1.util.Conexion;
import com.mysymphony.proyecto_symphony1.dao.AuditoriaDAO;
import com.mysymphony.proyecto_symphony1.dao.BitacoraDAO;
import com.mysymphony.proyecto_symphony1.dao.TablaDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

/**
 * Servlet para consultar todas las tablas institucionales validadas con trazabilidad completa
 * Rol: Administrador
 * Autor: Camila
 */
@WebServlet("/VerTablasValidadasServlet")
public class VerTablasValidadasServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession sesion = request.getSession(false);
        String rol = (sesion != null) ? (String) sesion.getAttribute("rolActivo") : null;
        String admin = (sesion != null) ? (String) sesion.getAttribute("nombreActivo") : "desconocido";
        Integer idAdmin = (sesion != null) ? (Integer) sesion.getAttribute("idActivo") : null;

        // 🔐 Validación de rol administrador
        if (rol == null || !"administrador".equalsIgnoreCase(rol) || idAdmin == null) {
            if (sesion != null) {
                sesion.setAttribute("mensaje", "⚠️ Acceso restringido: requiere rol administrador.");
            }
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        // 📥 Filtros de búsqueda
        String docenteFiltro = request.getParameter("docente");
        String desde = request.getParameter("desde");
        String hasta = request.getParameter("hasta");

        List<Map<String, String>> tablasValidadas = new ArrayList<>();

        try (Connection conn = Conexion.getConnection()) {

            // 📄 Convertir fechas si existen
            Date fechaDesdeSQL = null;
            Date fechaHastaSQL = null;
            try {
                if (desde != null && !desde.isEmpty()) {
                    LocalDate fechaDesde = LocalDate.parse(desde);
                    fechaDesdeSQL = Date.valueOf(fechaDesde);
                }
                if (hasta != null && !hasta.isEmpty()) {
                    LocalDate fechaHasta = LocalDate.parse(hasta);
                    fechaHastaSQL = Date.valueOf(fechaHasta);
                }
            } catch (Exception e) {
                System.err.println("❌ Error al convertir fechas: " + e.getMessage());
            }

            // 📊 Usar TablaDAO para listar tablas validadas
            TablaDAO tablaDAO = new TablaDAO(conn);
            tablasValidadas = tablaDAO.listarTablasValidadas(docenteFiltro, fechaDesdeSQL, fechaHastaSQL);

            // 📝 Auditoría institucional
            Map<String, String> registro = new HashMap<>();
            registro.put("usuario", admin);
            registro.put("rol", rol);
            registro.put("modulo", "Tablas validadas");
            registro.put("accion", "Consultó el panel de tablas institucionales validadas");
            registro.put("referencia_id", String.valueOf(idAdmin));
            registro.put("ip_origen", request.getRemoteAddr());
            new AuditoriaDAO(conn).registrarAccion(registro);

            // 📖 Bitácora institucional
            BitacoraDAO bitacoraDAO = new BitacoraDAO(conn);
            bitacoraDAO.registrarAccion("Administrador consultó tablas institucionales validadas",
                    admin, rol, "Tablas validadas");

        } catch (Exception e) {
            System.err.println("❌ Error al consultar tablas validadas: " + e.getMessage());
            request.setAttribute("mensaje", "❌ Error al consultar tablas validadas.");
        }

        // 📤 Enviar lista de tablas a la vista administrativa
        request.setAttribute("tablasValidadas", tablasValidadas);
        request.getRequestDispatcher("/administrador/tablasValidadas.jsp").forward(request, response);
    }
}