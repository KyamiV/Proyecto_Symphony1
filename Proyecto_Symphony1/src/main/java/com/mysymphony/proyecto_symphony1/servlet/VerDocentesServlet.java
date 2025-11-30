/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.mysymphony.proyecto_symphony1.servlet;

import com.mysymphony.proyecto_symphony1.util.Conexion;
import com.mysymphony.proyecto_symphony1.dao.BitacoraDAO;
import com.mysymphony.proyecto_symphony1.dao.AuditoriaDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.*;
import java.util.*;

/**
 * Servlet institucional para visualizar las clases asignadas a un docente específico.
 * Autor: Camila
 * Flujo: valida rol administrador, recibe idDocente y envía clases a verDocentes.jsp.
 */
@WebServlet("/VerDocentesServlet")
public class VerDocentesServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession sesion = request.getSession(false);
        String rol = (sesion != null) ? (String) sesion.getAttribute("rolActivo") : null;
        String admin = (sesion != null) ? (String) sesion.getAttribute("nombreActivo") : "desconocido";

        // 🔐 Solo rol administrador
        if (rol == null || !"administrador".equalsIgnoreCase(rol)) {
            request.setAttribute("mensaje", "⚠️ Acceso restringido: requiere rol administrador.");
            request.setAttribute("tipoMensaje", "warning");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
            return;
        }

        String idParam = request.getParameter("idDocente");
        if (idParam == null || idParam.isEmpty()) {
            request.setAttribute("mensaje", "❌ Error: parámetro 'idDocente' vacío.");
            request.setAttribute("tipoMensaje", "danger");
            request.getRequestDispatcher("/administrador/gestionarClasesPrincipal.jsp").forward(request, response);
            return;
        }

        int idDocente = Integer.parseInt(idParam);
        List<Map<String,Object>> clasesAsignadas = new ArrayList<>();

        try (Connection conn = Conexion.getConnection()) {
            // Consulta principal con conteos
            String sql = "SELECT c.id_clase, c.nombre_clase, c.instrumento, c.etapa, c.grupo, c.cupo, " +
                         "COUNT(ic.id_estudiante) AS inscritos, " +
                         "COUNT(CASE WHEN n.estado = 'validado' THEN 1 END) AS validados, " +
                         "COUNT(CASE WHEN n.estado = 'certificado' THEN 1 END) AS certificados, " +
                         "c.dia_semana, c.hora_inicio, c.hora_fin, c.estado " +
                         "FROM clases c " +
                         "LEFT JOIN inscripciones_clase ic ON c.id_clase = ic.id_clase " +
                         "LEFT JOIN notas n ON c.id_clase = n.id_clase " +
                         "WHERE c.id_docente = ? " +
                         "GROUP BY c.id_clase, c.nombre_clase, c.instrumento, c.etapa, c.grupo, c.cupo, " +
                         "c.dia_semana, c.hora_inicio, c.hora_fin, c.estado";

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, idDocente);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        Map<String,Object> fila = new HashMap<>();
                        int idClase = rs.getInt("id_clase");

                        fila.put("id_Clase", idClase);
                        fila.put("nombreClase", rs.getString("nombre_clase"));
                        fila.put("instrumento", rs.getString("instrumento"));
                        fila.put("etapa", rs.getString("etapa"));
                        fila.put("grupo", rs.getString("grupo"));
                        fila.put("cupo", rs.getInt("cupo"));
                        fila.put("inscritos", rs.getInt("inscritos"));
                        fila.put("validados", rs.getInt("validados"));
                        fila.put("certificados", rs.getInt("certificados"));
                        fila.put("diaSemana", rs.getString("dia_semana"));
                        fila.put("horaInicio", rs.getString("hora_inicio"));
                        fila.put("horaFin", rs.getString("hora_fin"));
                        fila.put("estado", rs.getString("estado"));

                        // 🔹 Subconsulta estudiantes validados
                        List<String> estudiantesValidados = new ArrayList<>();
                        String sqlValidados = "SELECT e.nombre FROM notas n " +
                                              "JOIN estudiantes e ON n.id_estudiante = e.id_estudiante " +
                                              "WHERE n.estado = 'validado' AND n.id_clase = ?";
                        try (PreparedStatement psVal = conn.prepareStatement(sqlValidados)) {
                            psVal.setInt(1, idClase);
                            try (ResultSet rsVal = psVal.executeQuery()) {
                                while (rsVal.next()) {
                                    estudiantesValidados.add(rsVal.getString("nombre"));
                                }
                            }
                        }

                        // 🔹 Subconsulta estudiantes certificados
                        List<String> estudiantesCertificados = new ArrayList<>();
                        String sqlCertificados = "SELECT e.nombre FROM notas n " +
                                                 "JOIN estudiantes e ON n.id_estudiante = e.id_estudiante " +
                                                 "WHERE n.estado = 'certificado' AND n.id_clase = ?";
                        try (PreparedStatement psCert = conn.prepareStatement(sqlCertificados)) {
                            psCert.setInt(1, idClase);
                            try (ResultSet rsCert = psCert.executeQuery()) {
                                while (rsCert.next()) {
                                    estudiantesCertificados.add(rsCert.getString("nombre"));
                                }
                            }
                        }

                        fila.put("estudiantesValidados", estudiantesValidados);
                        fila.put("estudiantesCertificados", estudiantesCertificados);

                        clasesAsignadas.add(fila);
                    }
                }
            }

            // 📝 Bitácora institucional
            new BitacoraDAO(conn).registrarAccion(
                    "Administrador consultó clases asignadas al docente ID " + idDocente,
                    admin, rol, "Gestión de docentes"
            );

            // 🛡️ Auditoría institucional
            Map<String, String> registro = new HashMap<>();
            registro.put("usuario", admin + " (ID: " + sesion.getAttribute("idActivo") + ")");
            registro.put("rol", rol);
            registro.put("modulo", "Gestión de docentes");
            registro.put("accion", "Consultó clases asignadas al docente ID " + idDocente);
            registro.put("referencia_id", String.valueOf(idDocente));
            registro.put("ip_origen", request.getRemoteAddr());
            new AuditoriaDAO(conn).registrarAccion(registro);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("mensaje", "❌ Error al consultar clases del docente: " + e.getMessage());
            request.setAttribute("tipoMensaje", "danger");
            request.getRequestDispatcher("/administrador/gestionarClasesPrincipal.jsp").forward(request, response);
            return;
        }

        // 📤 Enviar datos a la vista institucional
        request.setAttribute("idDocente", idDocente);
        request.setAttribute("clasesAsignadas", clasesAsignadas);
        request.setAttribute("totalClases", clasesAsignadas.size());
        request.getRequestDispatcher("/administrador/verDocentes.jsp").forward(request, response);
    }
}