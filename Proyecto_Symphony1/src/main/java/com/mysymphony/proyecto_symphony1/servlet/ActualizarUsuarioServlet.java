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
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/ActualizarUsuarioServlet")
public class ActualizarUsuarioServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 🔐 Validar sesión activa y rol estudiante
        HttpSession sesion = request.getSession(false);
        String rol = (sesion != null) ? (String) sesion.getAttribute("rolActivo") : null;
        String nombreSesion = (sesion != null) ? (String) sesion.getAttribute("nombreActivo") : "desconocido";

        if (rol == null || !"estudiante".equalsIgnoreCase(rol)) {
            if (sesion != null) {
                sesion.setAttribute("mensaje", "⚠️ Acceso restringido: requiere rol estudiante.");
            }
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        // 📥 Datos recibidos del formulario
        String usuarioId = request.getParameter("usuario_id");
        String correo = request.getParameter("correo");
        String clave = request.getParameter("contrasena");
        String claveConfirm = request.getParameter("contrasena_confirm");

        boolean actualizado = false;

        // Validación de contraseñas
        if (clave == null || claveConfirm == null || !clave.equals(claveConfirm)) {
            request.setAttribute("mensaje", "⚠️ Las contraseñas no coinciden.");
            request.getRequestDispatcher("/VerPerfilEstudianteServlet").forward(request, response);
            return;
        }

        // 🔐 Hash automático de la contraseña antes de guardar
        String claveHash = hashPassword(clave);

        // 📊 Actualización en la tabla usuarios
        String sql = "UPDATE usuarios SET correo = ?, clave = ? WHERE id_usuario = ?";

        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, correo);
            ps.setString(2, claveHash);
            ps.setInt(3, Integer.parseInt(usuarioId));

            int filas = ps.executeUpdate();
            actualizado = (filas > 0);

            // 📝 Auditoría institucional
            Map<String, String> registro = new HashMap<>();
            registro.put("usuario", nombreSesion);
            registro.put("rol", rol);
            registro.put("modulo", "Usuarios");
            registro.put("accion", "Actualizó sus datos de cuenta (correo/clave)");
            registro.put("referencia_id", usuarioId);
            registro.put("ip_origen", request.getRemoteAddr());
            new AuditoriaDAO(conn).registrarAccion(registro);

            // 📖 Bitácora institucional
            BitacoraDAO bitacoraDAO = new BitacoraDAO(conn);
            bitacoraDAO.registrarAccion("Estudiante actualizó sus datos de cuenta",
                    nombreSesion, rol, "Usuarios");

        } catch (SQLException e) {
            System.err.println("❌ Error al actualizar usuario: " + e.getMessage());
            request.setAttribute("mensaje", "❌ Error al actualizar tus datos.");
        }

        // 📤 Mensaje de confirmación
        if (actualizado) {
            request.setAttribute("mensaje", "✅ Tus datos de cuenta fueron actualizados correctamente.");
        } else {
            request.setAttribute("mensaje", "⚠️ No se pudo actualizar tu información.");
        }

        // Redirigir de nuevo al perfil
        request.getRequestDispatcher("/VerPerfilEstudianteServlet").forward(request, response);
    }

    // Método para aplicar hash SHA-256 a la contraseña
    private String hashPassword(String password) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes(java.nio.charset.StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            throw new RuntimeException("Error aplicando hash de contraseña", e);
        }
    }
}