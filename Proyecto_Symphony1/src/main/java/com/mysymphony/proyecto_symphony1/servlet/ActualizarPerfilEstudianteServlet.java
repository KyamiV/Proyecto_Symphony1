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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/ActualizarPerfilEstudianteServlet")
public class ActualizarPerfilEstudianteServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession sesion = request.getSession(false);
        String rol = (sesion != null) ? (String) sesion.getAttribute("rolActivo") : null;
        String nombreSesion = (sesion != null) ? (String) sesion.getAttribute("nombreActivo") : "desconocido";
        Integer idEstudiante = (sesion != null) ? (Integer) sesion.getAttribute("idActivo") : null;

        if (rol == null || !"estudiante".equalsIgnoreCase(rol) || idEstudiante == null) {
            if (sesion != null) {
                sesion.setAttribute("tipoMensaje", "warning");
                sesion.setAttribute("mensaje", "⚠️ Acceso restringido: requiere rol estudiante.");
            }
            response.sendRedirect(request.getContextPath() + "/VerPerfilEstudianteServlet");
            return;
        }

        // 📥 Parámetros recibidos desde el formulario
        String nombre = request.getParameter("nombre");
        String apellido = request.getParameter("apellido"); // ✅ nuevo campo
        String correoEstudiante = request.getParameter("correo_estudiante");
        String instrumento = request.getParameter("instrumento");
        String etapaPedagogica = request.getParameter("etapa_pedagogica");
        String direccion = request.getParameter("direccion");
        String telefono = request.getParameter("telefono");
        String correoUsuario = request.getParameter("correo");

        try (Connection conn = Conexion.getConnection()) {

            // 🔎 Obtener id_usuario asociado al estudiante
            int usuarioId = -1;
            String sqlUsuarioId = "SELECT id_usuario FROM estudiantes WHERE id_estudiante = ?";
            try (PreparedStatement ps = conn.prepareStatement(sqlUsuarioId)) {
                ps.setInt(1, idEstudiante);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        usuarioId = rs.getInt("id_usuario");
                    }
                }
            }

            // 📝 Validación mínima
            if (correoEstudiante == null || correoEstudiante.trim().isEmpty()) {
                throw new Exception("El correo institucional del estudiante no puede estar vacío.");
            }
            if (correoUsuario == null || correoUsuario.trim().isEmpty()) {
                throw new Exception("El correo de usuario no puede estar vacío.");
            }

            // 📝 Actualizar tabla estudiantes
            String sqlEstudiante = "UPDATE estudiantes SET nombre=?, apellido=?, correo=?, instrumento=?, direccion=?, telefono=?, etapa_pedagogica=? WHERE id_estudiante=?";
            try (PreparedStatement ps = conn.prepareStatement(sqlEstudiante)) {
                ps.setString(1, nombre);
                ps.setString(2, apellido);
                ps.setString(3, correoEstudiante);
                ps.setString(4, instrumento);
                ps.setString(5, direccion);
                ps.setString(6, telefono);
                ps.setString(7, etapaPedagogica);
                ps.setInt(8, idEstudiante);
                ps.executeUpdate();
            }

            // 📝 Actualizar tabla usuarios (correo, apellido, dirección y teléfono)
            if (usuarioId != -1) {
                String sqlUsuario = "UPDATE usuarios SET correo=?, apellido=?, direccion=?, telefono=? WHERE id_usuario=?";
                try (PreparedStatement ps = conn.prepareStatement(sqlUsuario)) {
                    ps.setString(1, correoUsuario);
                    ps.setString(2, apellido);
                    ps.setString(3, direccion);
                    ps.setString(4, telefono);
                    ps.setInt(5, usuarioId);
                    ps.executeUpdate();
                }
            }

            // 🛡️ Auditoría institucional
            Map<String, String> registro = new HashMap<>();
            registro.put("usuario", nombreSesion);
            registro.put("rol", rol);
            registro.put("modulo", "Perfil");
            registro.put("accion", "Actualizó su perfil académico y de usuario");
            registro.put("referencia_id", String.valueOf(idEstudiante));
            registro.put("ip_origen", request.getRemoteAddr());
            new AuditoriaDAO(conn).registrarAccion(registro);

            // 📖 Bitácora institucional
            BitacoraDAO bitacoraDAO = new BitacoraDAO(conn);
            bitacoraDAO.registrarAccion("Estudiante actualizó su perfil académico y de usuario",
                    nombreSesion, rol, "Perfil");

            // ✅ Mensaje de éxito
            sesion.setAttribute("tipoMensaje", "success");
            sesion.setAttribute("mensaje", "✅ Perfil actualizado correctamente.");
            response.sendRedirect(request.getContextPath() + "/VerPerfilEstudianteServlet");

        } catch (Exception e) {
            e.printStackTrace();
            sesion.setAttribute("tipoMensaje", "danger");
            sesion.setAttribute("mensaje", "❌ Error al actualizar tu perfil: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/VerPerfilEstudianteServlet");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Delegamos en doPost para soportar GET y evitar error 405
        doPost(request, response);
    }
}