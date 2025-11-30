/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.mysymphony.proyecto_symphony1.servlet;

/**
 * Servlet institucional para visualizar certificados del estudiante.
 * Rol: estudiante
 * Autor: Camila
 *
 * Propósito:
 *   - Validar sesión y rol estudiante
 *   - Consultar certificados desde BD (tabla certificados_estudiante)
 *   - Registrar acción en bitácora y auditoría institucional
 *   - Enviar datos a la vista certificados.jsp
 */

import com.mysymphony.proyecto_symphony1.dao.AuditoriaDAO;
import com.mysymphony.proyecto_symphony1.dao.BitacoraDAO;
import com.mysymphony.proyecto_symphony1.util.Conexion;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.*;
import java.util.*;

@WebServlet("/CertificadosEstudianteServlet")
public class CertificadosEstudianteServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 🔐 Validar sesión activa y rol estudiante
        HttpSession sesion = request.getSession(false);
        if (sesion == null || !"estudiante".equalsIgnoreCase((String) sesion.getAttribute("rolActivo"))) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        // 📥 Obtener datos de sesión
        String nombre = (String) sesion.getAttribute("nombreActivo");
        String rol = (String) sesion.getAttribute("rolActivo");
        Integer estudianteId = (Integer) sesion.getAttribute("idActivo"); // 👈 id del estudiante en sesión

        // 📊 Lista para almacenar certificados
        List<Map<String, String>> certificados = new ArrayList<>();

        // ✅ Consulta ajustada a la tabla real certificados_estudiante
        String sql = "SELECT id_certificado, id_clase, instrumento, etapa, fecha_emision, estado, url_certificado, usuario_admin " +
                     "FROM certificados_estudiante WHERE id_estudiante = ? ORDER BY fecha_emision DESC";

        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, estudianteId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Map<String, String> fila = new HashMap<>();
                    fila.put("id_certificado", rs.getString("id_certificado"));
                    fila.put("id_clase", rs.getString("id_clase"));
                    fila.put("instrumento", rs.getString("instrumento"));
                    fila.put("etapa", rs.getString("etapa"));
                    fila.put("fecha", rs.getString("fecha_emision"));
                    fila.put("estado", rs.getString("estado"));
                    fila.put("url", rs.getString("url_certificado"));
                    fila.put("usuario_admin", rs.getString("usuario_admin"));
                    certificados.add(fila);
                }
            }

            System.out.println("✅ Certificados consultados: " + certificados.size() + " para estudiante " + nombre);

            // 📝 Registro en bitácora institucional
            BitacoraDAO bitacoraDAO = new BitacoraDAO(conn);
            bitacoraDAO.registrarAccion("Estudiante visualizó sus certificados",
                    nombre, rol, "Certificados");

            // 🛡️ Registro en auditoría técnica
            Map<String, String> registro = new HashMap<>();
            registro.put("usuario", nombre);
            registro.put("rol", rol);
            registro.put("modulo", "Certificados");
            registro.put("accion", "Visualizar certificados");
            registro.put("referencia_id", String.valueOf(estudianteId));
            registro.put("ip_origen", request.getRemoteAddr());
            new AuditoriaDAO(conn).registrarAccion(registro);

        } catch (SQLException e) {
            System.err.println("❌ Error al consultar certificados: " + e.getMessage());
            sesion.setAttribute("mensaje", "❌ Error al consultar tus certificados.");
        }

        // 📤 Enviar datos a la vista JSP
        request.setAttribute("certificados", certificados);
        request.getRequestDispatcher("/estudiante/certificados.jsp").forward(request, response);
    }
}