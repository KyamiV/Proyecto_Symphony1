/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.mysymphony.proyecto_symphony1.servlet;

/**
 * Servlet institucional para visualizar la lista de usuarios, docentes y estudiantes registrados.
 * Acceso exclusivo para el rol administrador.
 * Integra con la vista: /administrador/verUsuarios.jsp
 * Autor: Camila
 * Trazabilidad: valida sesión, consulta usuarios, docentes y estudiantes y registra acceso en auditoría y bitácora
 */

import com.mysymphony.proyecto_symphony1.dao.UsuarioDAO;
import com.mysymphony.proyecto_symphony1.dao.AuditoriaDAO;
import com.mysymphony.proyecto_symphony1.dao.BitacoraDAO;
import com.mysymphony.proyecto_symphony1.modelo.Usuario;
import com.mysymphony.proyecto_symphony1.util.Conexion;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.*;
import java.util.*;

@WebServlet("/VerUsuariosServlet")
public class VerUsuariosServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession sesion = request.getSession(false);
        String rol = (sesion != null) ? (String) sesion.getAttribute("rolActivo") : null;
        String usuario = (sesion != null) ? (String) sesion.getAttribute("nombreActivo") : "desconocido";
        Integer idAdmin = (sesion != null) ? (Integer) sesion.getAttribute("idActivo") : null;

        // 🔒 Validación de acceso institucional
        if (rol == null || !"administrador".equalsIgnoreCase(rol) || idAdmin == null) {
            if (sesion != null) {
                sesion.setAttribute("mensaje", "⚠️ Acceso restringido: requiere rol administrador.");
            }
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        try (Connection conn = Conexion.getConnection()) {
            // ▸ Consulta institucional de usuarios registrados
            UsuarioDAO dao = new UsuarioDAO(conn);
            List<Usuario> listaUsuarios = dao.listarTodos();
            request.setAttribute("listaUsuarios", listaUsuarios);

            // ▸ Consulta institucional de docentes
            List<Map<String,String>> listaDocentes = new ArrayList<>();
            String sqlDocentes = "SELECT d.id_docente, d.nombre, d.apellido, d.correo, d.estado, " +
                                 "GROUP_CONCAT(DISTINCT c.instrumento) AS instrumentos, " +
                                 "COUNT(c.id_clase) AS clases_asignadas " +
                                 "FROM docentes d " +
                                 "LEFT JOIN clases c ON d.id_docente = c.id_docente " +
                                 "GROUP BY d.id_docente, d.nombre, d.apellido, d.correo, d.estado";
            try (PreparedStatement ps = conn.prepareStatement(sqlDocentes);
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Map<String,String> fila = new HashMap<>();
                    fila.put("id", String.valueOf(rs.getInt("id_docente")));
                    fila.put("nombre", rs.getString("nombre") + " " + rs.getString("apellido"));
                    fila.put("correo", rs.getString("correo"));
                    fila.put("instrumento", rs.getString("instrumentos")); // instrumentos concatenados desde clases
                    fila.put("clases_asignadas", String.valueOf(rs.getInt("clases_asignadas")));
                    fila.put("estado", rs.getString("estado"));
                    listaDocentes.add(fila);
                }
            }
            request.setAttribute("listaDocentes", listaDocentes);

            // ▸ Consulta institucional de estudiantes
            List<Map<String,String>> listaEstudiantes = new ArrayList<>();
            String sqlEstudiantes = "SELECT e.id_estudiante, e.nombre, e.correo, e.instrumento, e.etapa_pedagogica, " +
                                    "COALESCE(SUM(CASE WHEN c.estado = 'activa' THEN 1 ELSE 0 END),0) AS clases_activas, " +
                                    "COALESCE(SUM(CASE WHEN c.estado = 'certificada' THEN 1 ELSE 0 END),0) AS clases_certificadas " +
                                    "FROM estudiantes e " +
                                    "LEFT JOIN inscripciones_clase ic ON e.id_estudiante = ic.id_estudiante " +
                                    "LEFT JOIN clases c ON ic.id_clase = c.id_clase " +
                                    "GROUP BY e.id_estudiante, e.nombre, e.correo, e.instrumento, e.etapa_pedagogica";
            try (PreparedStatement ps = conn.prepareStatement(sqlEstudiantes);
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Map<String,String> fila = new HashMap<>();
                    fila.put("id", String.valueOf(rs.getInt("id_estudiante")));
                    fila.put("nombre", rs.getString("nombre"));
                    fila.put("correo", rs.getString("correo"));
                    fila.put("instrumento", rs.getString("instrumento"));
                    fila.put("etapa", rs.getString("etapa_pedagogica"));
                    fila.put("clases_activas", String.valueOf(rs.getInt("clases_activas")));
                    fila.put("clases_certificadas", String.valueOf(rs.getInt("clases_certificadas")));
                    listaEstudiantes.add(fila);
                }
            }
            request.setAttribute("listaEstudiantes", listaEstudiantes);

            // ▸ Registro en auditoría institucional
            Map<String, String> registro = new HashMap<>();
            registro.put("usuario", usuario);
            registro.put("rol", rol);
            registro.put("modulo", "Gestión de usuarios");
            registro.put("accion", "Consultó listado completo de usuarios, docentes y estudiantes institucionales");
            registro.put("referencia_id", String.valueOf(idAdmin));
            registro.put("ip_origen", request.getRemoteAddr());
            new AuditoriaDAO(conn).registrarAccion(registro);

            // ▸ Registro en bitácora institucional
            BitacoraDAO bitacoraDAO = new BitacoraDAO(conn);
            bitacoraDAO.registrarAccion(
                    "Administrador consultó listado de usuarios, docentes y estudiantes institucionales",
                    usuario, rol, "Gestión de usuarios"
            );

        } catch (Exception e) {
            e.printStackTrace();
            if (sesion != null) {
                sesion.setAttribute("mensaje", "❌ Error al consultar usuarios, docentes o estudiantes.");
            }
            // 🔴 Redirección a página de error institucional
            response.sendRedirect(request.getContextPath() + "/webpages/error.jsp");
            return;
        }

        // ▸ Envío de datos a la vista institucional
        request.getRequestDispatcher("/administrador/verUsuarios.jsp").forward(request, response);
    }
}