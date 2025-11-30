/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.mysymphony.proyecto_symphony1.servlet;

/**
 * Servlet para listar estudiantes registrados y mostrar sus datos musicales.
 * Rol: docente
 * Autor: Camila
 * Trazabilidad: valida sesión, consulta estudiantes y registra en auditoría institucional.
 */

import com.mysymphony.proyecto_symphony1.util.Conexion;
import com.mysymphony.proyecto_symphony1.dao.AuditoriaDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.*;
import java.util.*;

@WebServlet("/GestionarEstudiantesServlet")
public class GestionarEstudiantesServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession sesion = request.getSession();
        String nombre = (String) sesion.getAttribute("nombreActivo");
        String rol = (String) sesion.getAttribute("rolActivo");

        if (nombre == null || rol == null || !"docente".equalsIgnoreCase(rol)) {
            if (sesion != null) {
                sesion.setAttribute("mensaje", "⚠️ Acceso restringido: requiere rol docente.");
            }
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        List<Map<String, String>> estudiantes = new ArrayList<>();

        String sql = "SELECT u.id, u.nombre, e.instrumento, e.nivel_tecnico, e.etapa " +
                     "FROM usuarios u LEFT JOIN estudiantes e ON u.id = e.id_usuario " +
                     "WHERE u.rol = 'estudiante'";

        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Map<String, String> est = new HashMap<>();
                est.put("id", rs.getString("id"));
                est.put("nombre", rs.getString("nombre"));
                est.put("instrumento", rs.getString("instrumento"));
                est.put("nivel", rs.getString("nivel_tecnico"));
                est.put("etapa", rs.getString("etapa"));
                estudiantes.add(est);
            }

            // ✅ Registro de trazabilidad institucional
            AuditoriaDAO auditoriaDAO = new AuditoriaDAO(con);
            Map<String, String> registro = new HashMap<>();
            registro.put("usuario", nombre);
            registro.put("rol", rol);
            registro.put("modulo", "Estudiantes");
            registro.put("accion", "Consulta de estudiantes registrados");
            registro.put("ip_origen", request.getRemoteAddr());
            auditoriaDAO.registrarAccion(registro);

        } catch (SQLException e) {
            System.err.println("❌ Error al cargar estudiantes: " + e.getMessage());
            request.setAttribute("error", "❌ Error al cargar estudiantes: " + e.getMessage());
        }

        request.setAttribute("estudiantes", estudiantes);
        request.getRequestDispatcher("/docente/gestionarEstudiantes.jsp").forward(request, response);
    }
}