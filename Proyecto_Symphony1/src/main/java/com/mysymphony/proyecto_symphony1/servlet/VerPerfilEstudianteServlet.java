/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.mysymphony.proyecto_symphony1.servlet;

import com.mysymphony.proyecto_symphony1.dao.AuditoriaDAO;
import com.mysymphony.proyecto_symphony1.dao.BitacoraDAO;
import com.mysymphony.proyecto_symphony1.util.Conexion;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.*;
import java.util.*;

@WebServlet("/VerPerfilEstudianteServlet")
public class VerPerfilEstudianteServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 🔐 Validar sesión activa y rol estudiante
        HttpSession sesion = request.getSession(false);
        String rol = (sesion != null) ? (String) sesion.getAttribute("rolActivo") : null;
        String nombreSesion = (sesion != null) ? (String) sesion.getAttribute("nombreActivo") : "desconocido";
        Integer idEstudiante = (sesion != null) ? (Integer) sesion.getAttribute("idActivo") : null;

        if (rol == null || !"estudiante".equalsIgnoreCase(rol) || idEstudiante == null) {
            if (sesion != null) {
                sesion.setAttribute("mensaje", "⚠️ Acceso restringido: requiere rol estudiante.");
            }
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        Map<String, String> perfil = new HashMap<>();

        // 📌 Consulta principal del estudiante (incluye apellido, dirección y teléfono)
        String sqlEstudiante = "SELECT nombre, apellido, correo, instrumento, direccion, telefono, etapa_pedagogica, id_usuario " +
                               "FROM estudiantes WHERE id_estudiante = ?";

        try (Connection conn = Conexion.getConnection();
             PreparedStatement psEst = conn.prepareStatement(sqlEstudiante)) {

            psEst.setInt(1, idEstudiante);

            try (ResultSet rs = psEst.executeQuery()) {
                if (rs.next()) {
                    perfil.put("nombre", rs.getString("nombre"));
                    perfil.put("apellido", rs.getString("apellido"));
                    perfil.put("correo_estudiante", rs.getString("correo"));
                    perfil.put("instrumento", rs.getString("instrumento"));
                    perfil.put("direccion", rs.getString("direccion"));   // ✅ nuevo
                    perfil.put("telefono", rs.getString("telefono"));     // ✅ nuevo
                    perfil.put("etapa_pedagogica", rs.getString("etapa_pedagogica"));
                    perfil.put("id_usuario", rs.getString("id_usuario"));
                } else {
                    request.setAttribute("mensaje", "⚠️ No se encontró información de tu perfil.");
                }
            }

            // 📌 Consulta adicional en usuarios (para sincronizar datos si faltan)
            if (perfil.get("id_usuario") != null) {
                String sqlUsuario = "SELECT correo, direccion, telefono, apellido FROM usuarios WHERE id_usuario = ?";
                try (PreparedStatement psUser = conn.prepareStatement(sqlUsuario)) {
                    psUser.setInt(1, Integer.parseInt(perfil.get("id_usuario")));
                    try (ResultSet rsUser = psUser.executeQuery()) {
                        if (rsUser.next()) {
                            perfil.put("correo_usuario", rsUser.getString("correo"));
                            // Si dirección o teléfono están vacíos en estudiantes, los tomamos de usuarios
                            if (perfil.get("direccion") == null || perfil.get("direccion").isEmpty()) {
                                perfil.put("direccion", rsUser.getString("direccion"));
                            }
                            if (perfil.get("telefono") == null || perfil.get("telefono").isEmpty()) {
                                perfil.put("telefono", rsUser.getString("telefono"));
                            }
                            // Si apellido está vacío en estudiantes, lo tomamos de usuarios
                            if (perfil.get("apellido") == null || perfil.get("apellido").isEmpty()) {
                                perfil.put("apellido", rsUser.getString("apellido"));
                            }
                        }
                    }
                }
            }

            // 🛡️ Auditoría institucional
            Map<String, String> registro = new HashMap<>();
            registro.put("usuario", nombreSesion);
            registro.put("rol", rol);
            registro.put("modulo", "Perfil");
            registro.put("accion", "Visualizó su perfil académico y de usuario");
            registro.put("referencia_id", String.valueOf(idEstudiante));
            registro.put("ip_origen", request.getRemoteAddr());
            new AuditoriaDAO(conn).registrarAccion(registro);

            // 📖 Bitácora institucional
            BitacoraDAO bitacoraDAO = new BitacoraDAO(conn);
            bitacoraDAO.registrarAccion("Estudiante consultó su perfil académico y de usuario",
                    nombreSesion, rol, "Perfil");

        } catch (SQLException e) {
            System.err.println("❌ Error al consultar perfil: " + e.getMessage());
            request.setAttribute("mensaje", "❌ Error al consultar tu perfil.");
        }

        request.setAttribute("perfil", perfil);
        request.getRequestDispatcher("/estudiante/perfilEstudiante.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // ✅ Delegamos en doGet para soportar POST sin error 405
        doGet(request, response);
    }
}