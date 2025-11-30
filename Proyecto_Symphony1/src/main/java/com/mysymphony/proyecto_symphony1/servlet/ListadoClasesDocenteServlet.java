/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.mysymphony.proyecto_symphony1.servlet;

/**
 * Servlet para listar las clases asignadas al docente actual
 * Rol: docente
 * Autor: Camila
 * Trazabilidad: consulta asignaciones desde asignaciones_docente y carga datos de clases
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

@WebServlet("/ListadoClasesDocenteServlet")
public class ListadoClasesDocenteServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession sesion = request.getSession(false);
        String rol = (sesion != null) ? (String) sesion.getAttribute("rolActivo") : null;
        String nombreDocente = (sesion != null) ? (String) sesion.getAttribute("nombreActivo") : null;

        if (rol == null || !"docente".equalsIgnoreCase(rol)) {
            if (sesion != null) {
                sesion.setAttribute("mensaje", "⚠️ Acceso restringido: requiere rol docente.");
            }
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        List<Map<String, String>> clases = new ArrayList<>();

        try (Connection conn = Conexion.getConnection()) {

            // 🔎 Obtener ID del docente desde su nombre (tabla usuarios)
            String sqlDocente = "SELECT id FROM usuarios WHERE nombre = ? AND rol = 'docente'";
            int idDocente = -1;

            try (PreparedStatement ps = conn.prepareStatement(sqlDocente)) {
                ps.setString(1, nombreDocente);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    idDocente = rs.getInt("id");
                }
            }

            if (idDocente > 0) {
                // 🔹 Consultar clases asignadas al docente
                String sql = "SELECT c.id, c.nombre_clase, c.instrumento, c.etapa, c.cupo, c.inicio, c.fin, c.dia, c.aula " +
                             "FROM asignaciones_docente ad " +
                             "JOIN clases c ON ad.id_clase = c.id " +
                             "WHERE ad.id_docente = ? ORDER BY c.nombre_clase";

                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setInt(1, idDocente);
                    ResultSet rs = ps.executeQuery();

                    while (rs.next()) {
                        Map<String, String> fila = new HashMap<>();
                        fila.put("id", rs.getString("id"));
                        fila.put("nombre", rs.getString("nombre_clase"));
                        fila.put("instrumento", rs.getString("instrumento"));
                        fila.put("etapa", rs.getString("etapa"));
                        fila.put("cupo", rs.getString("cupo"));
                        fila.put("inicio", rs.getString("inicio"));
                        fila.put("fin", rs.getString("fin"));
                        fila.put("dia", rs.getString("dia"));
                        fila.put("aula", rs.getString("aula"));
                        clases.add(fila);
                    }
                }

                // 📝 Registro en bitácora institucional
                BitacoraDAO bitacoraDAO = new BitacoraDAO(conn);
                bitacoraDAO.registrarAccion("Docente accedió a listado de clases asignadas",
                        nombreDocente, rol, "Clases asignadas");

                // 🛡️ Registro en auditoría técnica
                Map<String, String> registro = new HashMap<>();
                registro.put("usuario", nombreDocente);
                registro.put("rol", rol);
                registro.put("modulo", "Clases asignadas");
                registro.put("accion", "Accedió a listado de clases asignadas");
                registro.put("ip_origen", request.getRemoteAddr());
                new AuditoriaDAO(conn).registrarAccion(registro);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "❌ Error al cargar clases: " + e.getMessage());
        }

        // 📦 Enviar lista de clases a la vista
        request.setAttribute("clases", clases);
        request.getRequestDispatcher("/docente/listadoClases.jsp").forward(request, response);
    }
}