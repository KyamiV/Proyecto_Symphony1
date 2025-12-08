/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.mysymphony.proyecto_symphony1.servlet;

/**
 * Servlet para consultar todas las tablas institucionales enviadas por docentes al administrador
 * Rol: administrador
 * Autor: Camila
 * Trazabilidad: valida sesión, consulta tablas y registra acceso en auditoría y bitácora
 */

import com.mysymphony.proyecto_symphony1.util.Conexion;
import com.mysymphony.proyecto_symphony1.dao.AuditoriaDAO;
import com.mysymphony.proyecto_symphony1.dao.BitacoraDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.*;
import java.util.*;

@WebServlet("/VerTablasRecibidasServlet")
public class VerTablasRecibidasServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        //  Validar sesión activa y rol administrador
        HttpSession sesion = request.getSession(false);
        String rol = (sesion != null) ? (String) sesion.getAttribute("rolActivo") : null;
        String admin = (sesion != null) ? (String) sesion.getAttribute("nombreActivo") : "desconocido";
        Integer idAdmin = (sesion != null) ? (Integer) sesion.getAttribute("idActivo") : null;

        if (rol == null || !"administrador".equalsIgnoreCase(rol) || idAdmin == null) {
            if (sesion != null) {
                sesion.setAttribute("mensaje", "⚠️ Acceso restringido: requiere rol administrador.");
            }
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        List<Map<String, String>> tablas = new ArrayList<>();

        try (Connection conn = Conexion.getConnection()) {

            //  Consulta SQL desde tablas_guardadas con estado enviada y NO validadas
            String sql = "SELECT t.id, t.nombre, t.descripcion, t.fecha_envio, " +
                         "COALESCE(c.nombre_clase, 'Sin clase') AS clase, " +
                         "COALESCE(u.nombre, 'Sin docente') AS docente " +
                         "FROM tablas_guardadas t " +
                         "LEFT JOIN clases c ON t.id_clase = c.id_clase " +
                         "LEFT JOIN usuarios u ON t.id_docente = u.id_usuario " +
                         "WHERE t.enviada = 1 AND (t.validada IS NULL OR t.validada = 'No') " +
                         "ORDER BY t.fecha_envio DESC";

            try (PreparedStatement ps = conn.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    Map<String, String> tabla = new HashMap<>();
                    tabla.put("id", String.valueOf(rs.getInt("id")));
                    tabla.put("nombre", rs.getString("nombre"));
                    tabla.put("descripcion", rs.getString("descripcion"));
                    tabla.put("fecha_envio", rs.getString("fecha_envio"));
                    tabla.put("clase", rs.getString("clase"));
                    tabla.put("docente", rs.getString("docente"));
                    tablas.add(tabla);
                }
            }

            //  Auditoría institucional
            Map<String, String> registro = new HashMap<>();
            registro.put("usuario", admin);
            registro.put("rol", rol);
            registro.put("modulo", "Tablas recibidas");
            registro.put("accion", "Consultó el panel de tablas enviadas por docentes");
            registro.put("referencia_id", String.valueOf(idAdmin));
            registro.put("ip_origen", request.getRemoteAddr());
            new AuditoriaDAO(conn).registrarAccion(registro);

            //  Bitácora institucional
            BitacoraDAO bitacoraDAO = new BitacoraDAO(conn);
            bitacoraDAO.registrarAccion("Administrador consultó tablas institucionales recibidas de docentes",
                    admin, rol, "Tablas recibidas");

        } catch (SQLException e) {
            System.err.println("❌ Error al consultar tablas recibidas: " + e.getMessage());
            request.setAttribute("mensaje", "❌ Error al consultar tablas recibidas.");
        }

        //  Enviar lista de tablas a la vista administrativa
        request.setAttribute("tablasRecibidas", tablas);
        request.getRequestDispatcher("/administrador/tablasRecibidas.jsp").forward(request, response);
    }
}