/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.mysymphony.proyecto_symphony1.servlet;

/**
 * Servlet para consultar estudiantes inscritos en una clase institucional.
 * Rol: administrador
 * Autor: Camila
 * Trazabilidad: recibe claseId, consulta inscripciones, registra auditoría y bitácora, y envía a vista
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

@WebServlet("/VerInscritosClaseServlet")
public class VerInscritosClaseServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession sesion = request.getSession(false);
        String rol = (sesion != null) ? (String) sesion.getAttribute("rolActivo") : null;
        String usuario = (sesion != null) ? (String) sesion.getAttribute("nombreActivo") : "desconocido";

        if (rol == null || !"administrador".equalsIgnoreCase(rol)) {
            if (sesion != null) {
                sesion.setAttribute("mensaje", "⚠️ Acceso restringido: requiere rol administrador.");
            }
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        String claseIdParam = request.getParameter("id");
        int claseId;
        try {
            claseId = Integer.parseInt(claseIdParam);
        } catch (Exception e) {
            sesion.setAttribute("mensaje", "❌ Clase no especificada o ID inválido.");
            response.sendRedirect(request.getContextPath() + "/GestionarClasesServlet");
            return;
        }

        List<Map<String, String>> inscritos = new ArrayList<>();
        String claseNombre = "";

        try (Connection conn = Conexion.getConnection()) {

            // 🔎 Obtener nombre de la clase
            try (PreparedStatement psClase = conn.prepareStatement(
                    "SELECT nombre_clase FROM clases WHERE id = ?")) {
                psClase.setInt(1, claseId);
                try (ResultSet rsClase = psClase.executeQuery()) {
                    if (rsClase.next()) {
                        claseNombre = rsClase.getString("nombre_clase");
                    }
                }
            }

            // 📥 Consultar inscritos
            try (PreparedStatement ps = conn.prepareStatement(
                    "SELECT e.id, e.nombre, e.correo, e.telefono " +
                    "FROM inscripciones_clase ic " +
                    "JOIN usuarios e ON ic.id_estudiante = e.id " +
                    "WHERE ic.id_clase = ? ORDER BY e.nombre ASC")) {

                ps.setInt(1, claseId);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        Map<String, String> estudiante = new HashMap<>();
                        estudiante.put("id", rs.getString("id"));
                        estudiante.put("nombre", rs.getString("nombre"));
                        estudiante.put("correo", rs.getString("correo"));
                        estudiante.put("telefono", rs.getString("telefono"));
                        inscritos.add(estudiante);
                    }
                }
            }

            // 📝 Auditoría institucional
            Map<String, String> registro = new HashMap<>();
            registro.put("usuario", usuario);
            registro.put("rol", rol);
            registro.put("modulo", "Gestión de clases");
            registro.put("accion", "Consultó inscritos en clase ID " + claseId);
            registro.put("referencia_id", String.valueOf(claseId));
            registro.put("ip_origen", request.getRemoteAddr());
            new AuditoriaDAO(conn).registrarAccion(registro);

            // 📖 Bitácora institucional
            BitacoraDAO bitacoraDAO = new BitacoraDAO(conn);
            bitacoraDAO.registrarAccion("Administrador consultó inscritos de la clase institucional con ID " + claseId,
                    usuario, rol, "Gestión de clases");

            // 📤 Envío a la vista
            request.setAttribute("inscritos", inscritos);
            request.setAttribute("claseId", claseId);
            request.setAttribute("claseNombre", claseNombre);
            request.getRequestDispatcher("/administrador/verInscritosClase.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            sesion.setAttribute("mensaje", "❌ Error al consultar inscritos.");
            response.sendRedirect(request.getContextPath() + "/GestionarClasesServlet");
        }
    }
}