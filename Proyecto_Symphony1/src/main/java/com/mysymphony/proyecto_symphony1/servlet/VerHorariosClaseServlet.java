/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.mysymphony.proyecto_symphony1.servlet;

/**
 * Servlet para que el docente consulte los horarios de sus clases asignadas.
 * Rol: docente
 * Autor: Camila
 * Trazabilidad: permite visualizar horarios institucionales por clase
 */

import com.mysymphony.proyecto_symphony1.util.Conexion;
import com.mysymphony.proyecto_symphony1.dao.AuditoriaDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.*;
import java.util.*;

@WebServlet("/VerHorariosClaseServlet")
public class VerHorariosClaseServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession sesion = request.getSession(false);
        String rol = (sesion != null) ? (String) sesion.getAttribute("rolActivo") : null;
        String usuario = (sesion != null) ? (String) sesion.getAttribute("nombreActivo") : "desconocido";
        Integer idDocente = (sesion != null) ? (Integer) sesion.getAttribute("idUsuario") : null;

        if (rol == null || !"docente".equalsIgnoreCase(rol)) {
            if (sesion != null) {
                sesion.setAttribute("mensaje", "⚠️ Acceso restringido: requiere rol docente.");
                sesion.setAttribute("tipoMensaje", "danger");
            }
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        List<Map<String, Object>> clasesConHorarios = new ArrayList<>();

        try (Connection conn = Conexion.getConnection()) {
            // 🧩 Consultar clases asignadas al docente
            String sqlClases = "SELECT c.id_clase, c.nombre_clase, c.instrumento, c.etapa " +
                               "FROM clases c " +
                               "JOIN clases_asignadas ca ON c.id_clase = ca.id_clase " +
                               "WHERE ca.id_docente = ?";
            try (PreparedStatement ps = conn.prepareStatement(sqlClases)) {
                ps.setInt(1, idDocente);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        Map<String, Object> clase = new HashMap<>();
                        int idClase = rs.getInt("id_clase");
                        clase.put("id_clase", idClase);
                        clase.put("nombre", rs.getString("nombre_clase"));
                        clase.put("instrumento", rs.getString("instrumento"));
                        clase.put("etapa", rs.getString("etapa"));

                        // 📅 Consultar horarios de esa clase
                        List<Map<String, String>> horarios = new ArrayList<>();
                        String sqlHorarios = "SELECT dia_semana, hora_inicio, hora_fin, aula " +
                                             "FROM horarios_clase WHERE clase_id = ?";
                        try (PreparedStatement psH = conn.prepareStatement(sqlHorarios)) {
                            psH.setInt(1, idClase);
                            try (ResultSet rsH = psH.executeQuery()) {
                                while (rsH.next()) {
                                    Map<String, String> h = new HashMap<>();
                                    h.put("dia", rsH.getString("dia_semana"));
                                    h.put("inicio", rsH.getString("hora_inicio"));
                                    h.put("fin", rsH.getString("hora_fin"));
                                    h.put("aula", rsH.getString("aula"));
                                    horarios.add(h);
                                }
                            }
                        }
                        clase.put("horarios", horarios);
                        clasesConHorarios.add(clase);
                    }
                }
            }

            // 📝 Auditoría institucional
            AuditoriaDAO auditoriaDAO = new AuditoriaDAO(conn);
            Map<String, String> registro = new HashMap<>();
            registro.put("usuario", usuario);
            registro.put("rol", rol);
            registro.put("modulo", "Horarios de clase");
            registro.put("accion", "Docente consultó horarios de sus clases");
            registro.put("ip_origen", request.getRemoteAddr());
            auditoriaDAO.registrarAccion(registro);

        } catch (SQLException e) {
            request.setAttribute("error", "❌ Error al consultar horarios: " + e.getMessage());
        }

        // 📤 Enviar datos a la vista
        request.setAttribute("clasesConHorarios", clasesConHorarios);
        request.getRequestDispatcher("/docente/verHorariosClase.jsp").forward(request, response);
    }
}