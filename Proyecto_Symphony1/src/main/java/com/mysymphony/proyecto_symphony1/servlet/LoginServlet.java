
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.mysymphony.proyecto_symphony1.servlet;

/**
 * Servlet para validar credenciales y redirigir según el rol
 * Autor: Camila
 * Trazabilidad: valida usuario, crea sesión, redirige por rol y registra auditoría
 */

import com.mysymphony.proyecto_symphony1.dao.UsuarioDAO;
import com.mysymphony.proyecto_symphony1.dao.AuditoriaDAO;
import com.mysymphony.proyecto_symphony1.modelo.Usuario;
import com.mysymphony.proyecto_symphony1.util.Conexion;
import com.mysymphony.proyecto_symphony1.util.HashUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "LoginServlet", urlPatterns = {"/LoginServlet"})
public class LoginServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String correo = request.getParameter("usuario");
        String clave = request.getParameter("clave");

        // Validación básica
        if (correo == null || clave == null || correo.trim().isEmpty() || clave.trim().isEmpty()) {
            request.setAttribute("error", "⚠️ Debes ingresar correo y contraseña.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
            return;
        }

        correo = correo.trim().toLowerCase();

        // Hash de contraseña
        String claveHash = HashUtil.hashPassword(clave);

        try (Connection conn = Conexion.getConnection()) {

            UsuarioDAO dao = new UsuarioDAO(conn);
            Usuario usuario = dao.validarUsuario(correo, claveHash);

            if (usuario == null) {
                request.setAttribute("error", "❌ Credenciales inválidas.");
                request.getRequestDispatcher("login.jsp").forward(request, response);
                return;
            }

            // Validación estado del usuario
            if (usuario.getEstado() != null && !"activo".equalsIgnoreCase(usuario.getEstado())) {

                // Auditoría
                Map<String, String> reg = new HashMap<>();
                reg.put("usuario", usuario.getCorreo());
                reg.put("rol", usuario.getRol());
                reg.put("modulo", "Inicio de sesión");
                reg.put("accion", "Intento de acceso con cuenta inactiva");
                reg.put("ip_origen", request.getRemoteAddr());
                new AuditoriaDAO(conn).registrarAccion(reg);

                request.setAttribute("error", "❌ Credenciales inválidas.");
                request.getRequestDispatcher("login.jsp").forward(request, response);
                return;
            }

            // Evitar fijación de sesión
            request.getSession().invalidate();
            HttpSession session = request.getSession(true);

            // Datos de sesión
            session.setAttribute("usuarioActivo", usuario.getCorreo());
            session.setAttribute("nombreActivo", usuario.getNombre());
            session.setAttribute("rolActivo", usuario.getRol());

            // 🔑 Ajuste clave: guardar siempre el ID correcto según rol
            switch (usuario.getRol().toLowerCase()) {
                case "estudiante":
                    session.setAttribute("idActivo", usuario.getIdEstudiante()); // ID real de estudiante
                    break;
                case "docente":
                    session.setAttribute("idActivo", usuario.getIdDocente());    // ID real de docente
                    break;
                case "administrador":
                    session.setAttribute("idActivo", usuario.getIdUsuario());   // ID genérico de usuario
                    break;
            }

            // Auditoría de inicio exitoso
            Map<String, String> registro = new HashMap<>();
            registro.put("usuario", usuario.getNombre());
            registro.put("rol", usuario.getRol());
            registro.put("modulo", "Inicio de sesión");
            registro.put("accion", "Inicio de sesión exitoso");
            registro.put("ip_origen", request.getRemoteAddr());
            new AuditoriaDAO(conn).registrarAccion(registro);

            // Redirección según rol
            switch (usuario.getRol().toLowerCase()) {
                case "estudiante":
                    response.sendRedirect("PanelEstudianteServlet");
                    break;
                case "docente":
                    response.sendRedirect("PanelDocenteServlet");
                    break;
                case "administrador":
                    response.sendRedirect("PanelAdministradorServlet");
                    break;
                default:
                    Map<String, String> reg2 = new HashMap<>();
                    reg2.put("usuario", usuario.getCorreo());
                    reg2.put("rol", usuario.getRol());
                    reg2.put("modulo", "Inicio de sesión");
                    reg2.put("accion", "Rol no reconocido: " + usuario.getRol());
                    reg2.put("ip_origen", request.getRemoteAddr());
                    new AuditoriaDAO(conn).registrarAccion(reg2);

                    response.sendRedirect("login.jsp?error=rol");
            }

        } catch (Exception e) {
            System.err.println("❌ Error en LoginServlet: " + e.getMessage());
            request.setAttribute("error", "❌ Error interno en el servidor.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }
}