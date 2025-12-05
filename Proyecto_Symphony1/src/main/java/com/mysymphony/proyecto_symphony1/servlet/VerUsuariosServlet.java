/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.mysymphony.proyecto_symphony1.servlet;

/**
 * Servlet institucional para visualizar, editar y eliminar usuarios, docentes y estudiantes registrados.
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

        // 📌 Parámetros de acción (editar/eliminar)
        String accion = request.getParameter("accion");
        String idParam = request.getParameter("idUsuario");

        List<Usuario> listaUsuarios = new ArrayList<>();
        List<Map<String,String>> listaDocentes = new ArrayList<>();
        List<Map<String,String>> listaEstudiantes = new ArrayList<>();

        try (Connection conn = Conexion.getConnection()) {
            UsuarioDAO dao = new UsuarioDAO(conn);

            if (accion != null && idParam != null) {
                int idUsuario = Integer.parseInt(idParam);

                if ("eliminar".equalsIgnoreCase(accion)) {
                    dao.eliminarUsuario(idUsuario);
                    request.setAttribute("mensaje", "✅ Usuario eliminado correctamente.");
                    request.setAttribute("tipoMensaje", "success");

                    Map<String, String> registro = new HashMap<>();
                    registro.put("usuario", usuario);
                    registro.put("rol", rol);
                    registro.put("modulo", "Gestión de usuarios");
                    registro.put("accion", "Eliminó usuario con ID " + idUsuario);
                    registro.put("referencia_id", String.valueOf(idAdmin));
                    registro.put("ip_origen", request.getRemoteAddr());
                    new AuditoriaDAO(conn).registrarAccion(registro);

                    BitacoraDAO bitacoraDAO = new BitacoraDAO(conn);
                    bitacoraDAO.registrarAccion(
                            "Administrador eliminó usuario institucional con ID " + idUsuario,
                            usuario, rol, "Gestión de usuarios"
                    );
                }

                if ("editar".equalsIgnoreCase(accion)) {
                    String nuevoEstado = request.getParameter("estado");
                    dao.actualizarEstadoUsuario(idUsuario, nuevoEstado);
                    request.setAttribute("mensaje", "✅ Usuario actualizado correctamente.");
                    request.setAttribute("tipoMensaje", "info");

                    Map<String, String> registro = new HashMap<>();
                    registro.put("usuario", usuario);
                    registro.put("rol", rol);
                    registro.put("modulo", "Gestión de usuarios");
                    registro.put("accion", "Editó usuario con ID " + idUsuario + " cambiando estado a " + nuevoEstado);
                    registro.put("referencia_id", String.valueOf(idAdmin));
                    registro.put("ip_origen", request.getRemoteAddr());
                    new AuditoriaDAO(conn).registrarAccion(registro);

                    BitacoraDAO bitacoraDAO = new BitacoraDAO(conn);
                    bitacoraDAO.registrarAccion(
                            "Administrador editó usuario institucional con ID " + idUsuario,
                            usuario, rol, "Gestión de usuarios"
                    );
                }
            }

            // ▸ Consulta institucional de usuarios registrados
            listaUsuarios = dao.listarTodos();

            // ▸ Consulta institucional de docentes
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
                    fila.put("instrumento", rs.getString("instrumentos"));
                    fila.put("clases_asignadas", String.valueOf(rs.getInt("clases_asignadas")));
                    fila.put("estado", rs.getString("estado"));
                    listaDocentes.add(fila);
                }
            }

            // ▸ Consulta institucional de estudiantes
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

        } catch (Exception e) {
            e.printStackTrace();
            if (sesion != null) {
                sesion.setAttribute("mensaje", "❌ Error al consultar usuarios, docentes o estudiantes.");
            }
            response.sendRedirect(request.getContextPath() + "/fragmentos/error.jsp");
            return;
        }

        // 🔎 Validación de salida JSON para Postman
        String acceptHeader = request.getHeader("Accept");
        if (acceptHeader != null && acceptHeader.contains("application/json")) {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            Map<String,Object> resultado = new HashMap<>();
            resultado.put("usuarios", listaUsuarios);
            resultado.put("docentes", listaDocentes);
            resultado.put("estudiantes", listaEstudiantes);

            StringBuilder json = new StringBuilder("{");

            // usuarios
            json.append("\"usuarios\":[");
            for (Usuario u : listaUsuarios) {
                json.append("{\"id\":").append(u.getIdUsuario())
                    .append(",\"nombre\":\"").append(u.getNombre())
                    .append("\",\"correo\":\"").append(u.getCorreo())
                    .append("\",\"rol\":\"").append(u.getRol())
                    .append("\",\"estado\":\"").append(u.getEstado()).append("\"},");
            }
            if (json.charAt(json.length()-1) == ',') json.deleteCharAt(json.length()-1);
            json.append("],");

                        // docentes
            json.append("\"docentes\":[");
            for (Map<String,String> d : listaDocentes) {
                json.append("{\"id\":").append(d.get("id"))
                    .append(",\"nombre\":\"").append(d.get("nombre"))
                    .append("\",\"correo\":\"").append(d.get("correo"))
                    .append("\",\"instrumento\":\"").append(d.get("instrumento"))
                    .append("\",\"clases_asignadas\":\"").append(d.get("clases_asignadas"))
                    .append("\",\"estado\":\"").append(d.get("estado")).append("\"},");
            }
            if (json.charAt(json.length()-1) == ',') json.deleteCharAt(json.length()-1);
            json.append("],");

            // estudiantes
            json.append("\"estudiantes\":[");
            for (Map<String,String> e : listaEstudiantes) {
                json.append("{\"id\":").append(e.get("id"))
                    .append(",\"nombre\":\"").append(e.get("nombre"))
                    .append("\",\"correo\":\"").append(e.get("correo"))
                    .append("\",\"instrumento\":\"").append(e.get("instrumento"))
                    .append("\",\"etapa\":\"").append(e.get("etapa"))
                    .append("\",\"clases_activas\":\"").append(e.get("clases_activas"))
                    .append("\",\"clases_certificadas\":\"").append(e.get("clases_certificadas")).append("\"},");
            }
            if (json.charAt(json.length()-1) == ',') json.deleteCharAt(json.length()-1);
            json.append("]}");

            response.getWriter().write(json.toString());
            return; // 👈 salir para no redirigir
        }

        // 👉 Si no es JSON (ej. navegador), enviar datos a la vista JSP
        request.setAttribute("listaUsuarios", listaUsuarios);
        request.setAttribute("listaDocentes", listaDocentes);
        request.setAttribute("listaEstudiantes", listaEstudiantes);
        request.getRequestDispatcher("/administrador/verUsuarios.jsp").forward(request, response);
    }
}