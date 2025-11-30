/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mysymphony.proyecto_symphony1.servlet;

import com.mysymphony.proyecto_symphony1.util.Conexion;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.*;
import java.util.*;

@WebServlet("/VerEstudiantesServlet")
public class VerEstudiantesServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession sesion = request.getSession(false);
        String rol = (sesion != null) ? (String) sesion.getAttribute("rolActivo") : null;

        // 🔐 Solo rol administrador
        if (rol == null || !"administrador".equalsIgnoreCase(rol)) {
            request.setAttribute("mensaje", "⚠️ Acceso restringido: requiere rol administrador.");
            request.setAttribute("tipoMensaje", "warning");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
            return;
        }

        String idParam = request.getParameter("idEstudiante");
        if (idParam == null || idParam.isEmpty()) {
            request.setAttribute("mensaje", "❌ Error: parámetro 'idEstudiante' vacío.");
            request.setAttribute("tipoMensaje", "danger");
            request.getRequestDispatcher("/administrador/gestionarClasesPrincipal.jsp").forward(request, response);
            return;
        }

        int idEstudiante = Integer.parseInt(idParam);
        List<Map<String,String>> clasesInscritas = new ArrayList<>();

        try (Connection conn = Conexion.getConnection()) {
            // ✅ Consulta filtrada por estudiante (sin observacion)
            String sql = "SELECT e.id_estudiante, e.nombre, e.correo, u.estado, e.etapa_pedagogica, " +
                        "c.id_clase, c.nombre_clase, c.instrumento, c.etapa AS etapa_clase, c.grupo, c.estado AS estado_clase, " +
                        "ic.fecha_inscripcion, n.nota " +
                        "FROM estudiantes e " +
                        "JOIN usuarios u ON e.id_usuario = u.id_usuario " +   // ✅ corregido
                        "LEFT JOIN inscripciones_clase ic ON e.id_estudiante = ic.id_estudiante " +
                        "LEFT JOIN clases c ON ic.id_clase = c.id_clase " +
                        "LEFT JOIN notas n ON e.id_estudiante = n.id_estudiante AND c.id_clase = n.id_clase " +
                        "WHERE e.id_estudiante = ?";

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, idEstudiante);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        Map<String,String> fila = new HashMap<>();
                        fila.put("idEstudiante", String.valueOf(rs.getInt("id_estudiante")));
                        fila.put("nombreEstudiante", rs.getString("nombre"));
                        fila.put("correoEstudiante", rs.getString("correo"));
                        fila.put("estado", rs.getString("estado"));
                        fila.put("etapa", rs.getString("etapa_pedagogica"));
                        fila.put("idClase", String.valueOf(rs.getInt("id_clase")));
                        fila.put("nombreClase", rs.getString("nombre_clase"));
                        fila.put("instrumento", rs.getString("instrumento"));
                        fila.put("etapaClase", rs.getString("etapa_clase"));
                        fila.put("grupo", rs.getString("grupo"));
                        fila.put("estadoClase", rs.getString("estado_clase"));
                        fila.put("fechaInscripcion", rs.getString("fecha_inscripcion"));
                        fila.put("nota", rs.getString("nota"));
                        // ❌ observacion eliminado porque no existe en la tabla
                        clasesInscritas.add(fila);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("mensaje", "❌ Error al consultar clases del estudiante: " + e.getMessage());
            request.setAttribute("tipoMensaje", "danger");
            request.getRequestDispatcher("/error.jsp").forward(request, response);
            return;
        }

        // 📤 Enviar datos a la vista institucional
        request.setAttribute("idClase", idEstudiante);
        request.setAttribute("inscripciones", clasesInscritas);
        request.setAttribute("totalInscritos", clasesInscritas.size());
        request.getRequestDispatcher("/administrador/verEstudiantes.jsp").forward(request, response);
    }
}