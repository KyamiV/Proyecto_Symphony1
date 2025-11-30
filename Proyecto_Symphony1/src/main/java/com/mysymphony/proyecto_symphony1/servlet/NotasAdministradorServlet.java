/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.mysymphony.proyecto_symphony1.servlet;

/**
 * Servlet para consultar notas musicales con filtros por docente, etapa e instrumento.
 * Rol: administrador
 * Función: Visualizar y exportar resultados desde la vista verNotasAdministrador.jsp
 * Autor: Camila
 * Trazabilidad: incluye validación de sesión, filtros dinámicos, consulta SQL segura y envío a vista
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

@WebServlet("/NotasAdministradorServlet")
public class NotasAdministradorServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // 🔐 Validación de sesión y rol
        HttpSession sesion = request.getSession(false);
        String rol = (sesion != null) ? (String) sesion.getAttribute("rolActivo") : null;
        String usuario = (sesion != null) ? (String) sesion.getAttribute("nombreActivo") : "desconocido";

        if (rol == null || !"administrador".equalsIgnoreCase(rol.trim())) {
            if (sesion != null) {
                sesion.setAttribute("mensaje", "⚠️ Acceso restringido: requiere rol administrador.");
            }
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        // 📥 Parámetros de filtro
        String docenteFiltro = request.getParameter("docente");
        String etapaFiltro = request.getParameter("etapa");
        String instrumentoFiltro = request.getParameter("instrumento");

        List<Map<String, String>> notas = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
            "SELECT estudiante, instrumento, etapa, nota, observaciones, docente, fecha_registro " +
            "FROM notas_musicales WHERE 1=1"
        );
        List<String> filtros = new ArrayList<>();

        // 🎯 Aplicar filtros dinámicos
        if (docenteFiltro != null && !docenteFiltro.trim().isEmpty()) {
            sql.append(" AND docente LIKE ?");
            filtros.add("%" + docenteFiltro.trim() + "%");
        }

        if (etapaFiltro != null && !etapaFiltro.trim().isEmpty()) {
            sql.append(" AND etapa = ?");
            filtros.add(etapaFiltro.trim());
        }

        if (instrumentoFiltro != null && !instrumentoFiltro.trim().isEmpty()) {
            sql.append(" AND instrumento = ?");
            filtros.add(instrumentoFiltro.trim());
        }

        sql.append(" ORDER BY estudiante, instrumento, etapa");

        // 📡 Consulta a base de datos
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

            // 📝 Registro en bitácora institucional
            BitacoraDAO bitacoraDAO = new BitacoraDAO(conn);
            bitacoraDAO.registrarAccion("Administrador consultó notas con filtros: docente=" + docenteFiltro +
                    ", etapa=" + etapaFiltro + ", instrumento=" + instrumentoFiltro,
                    usuario, rol, "Notas musicales");

            // 🛡️ Registro en auditoría técnica
            Map<String, String> registro = new HashMap<>();
            registro.put("usuario", usuario);
            registro.put("rol", rol);
            registro.put("modulo", "Notas musicales");
            registro.put("accion", "Consultó notas con filtros aplicados");
            registro.put("ip_origen", request.getRemoteAddr());
            new AuditoriaDAO(conn).registrarAccion(registro);

        } catch (SQLException e) {
            System.err.println("❌ Error al consultar notas: " + e.getMessage());
            request.setAttribute("error", "❌ No se pudieron cargar las notas.");
        }

        // 📤 Enviar resultados a la vista
        request.setAttribute("notas", notas);
        request.setAttribute("docenteFiltro", docenteFiltro);
        request.setAttribute("etapaFiltro", etapaFiltro);
        request.setAttribute("instrumentoFiltro", instrumentoFiltro);

        request.getRequestDispatcher("/administrador/verNotasAdministrador.jsp").forward(request, response);
    }
}