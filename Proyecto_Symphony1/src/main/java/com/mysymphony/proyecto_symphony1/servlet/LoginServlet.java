
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
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Evita error 405: redirige siempre al formulario de login
        response.sendRedirect("login.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String correo = request.getParameter("usuario");
        String clave = request.getParameter("clave");

        // Validación básica
        if (correo == null || clave == null || correo.trim().isEmpty() || clave.trim().isEmpty()) {
            manejarRespuesta(request, response, false, "⚠️ Debes ingresar correo y contraseña.", null);
            return;
        }

        correo = correo.trim().toLowerCase();

        // Hash de contraseña
        String claveHash = HashUtil.hashPassword(clave);

        try (Connection conn = Conexion.getConnection()) {

            if (conn == null) {
                manejarRespuesta(request, response, false, "❌ Error: No se pudo conectar a la base de datos.", null);
                return;
            }

            UsuarioDAO dao = new UsuarioDAO(conn);
            Usuario usuario = dao.validarUsuario(correo, claveHash);

            if (usuario == null) {
                manejarRespuesta(request, response, false, "❌ Credenciales inválidas.", null);
                return;
            }

            // Validación estado del usuario
            if (usuario.getEstado() != null && !"activo".equalsIgnoreCase(usuario.getEstado())) {
                Map<String, String> reg = new HashMap<>();
                reg.put("usuario", usuario.getCorreo());
                reg.put("rol", usuario.getRol());
                reg.put("modulo", "Inicio de sesión");
                reg.put("accion", "Intento de acceso con cuenta inactiva");
                reg.put("ip_origen", request.getRemoteAddr());
                new AuditoriaDAO(conn).registrarAccion(reg);

                manejarRespuesta(request, response, false, "❌ Cuenta inactiva.", null);
                return;
            }

            // Evitar fijación de sesión
            request.getSession().invalidate();
            HttpSession session = request.getSession(true);

            // Datos de sesión comunes
            session.setAttribute("usuarioActivo", usuario.getCorreo());
            session.setAttribute("nombreActivo", usuario.getNombre());
            session.setAttribute("rolActivo", usuario.getRol());

            // Guardar IDs según rol
            switch (usuario.getRol().toLowerCase()) {
                case "estudiante":
                    session.setAttribute("idActivo", usuario.getIdEstudiante());
                    session.setAttribute("idEstudiante", usuario.getIdEstudiante());
                    session.setAttribute("idUsuario", usuario.getIdUsuario());
                    break;
                case "docente":
                    session.setAttribute("idActivo", usuario.getIdDocente());   // usado por servlets
                    session.setAttribute("idDocente", usuario.getIdDocente()); // FK real en tabla docentes
                    session.setAttribute("idUsuario", usuario.getIdUsuario()); // útil para auditoría
                    break;
                case "administrador":
                    session.setAttribute("idActivo", usuario.getIdUsuario());
                    session.setAttribute("idUsuario", usuario.getIdUsuario());
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

            // Manejo de respuesta según rol
            manejarRespuesta(request, response, true, "Inicio de sesión exitoso", usuario);

        } catch (Exception e) {
            System.err.println("❌ Error en LoginServlet: " + e.getMessage());
            manejarRespuesta(request, response, false, "❌ Error interno en el servidor.", null);
        }
    }

    /**
     * Método auxiliar para manejar respuesta en JSP o JSON según el header Accept
     */
    private void manejarRespuesta(HttpServletRequest request, HttpServletResponse response,
                                  boolean exito, String mensaje, Usuario usuario) throws IOException, ServletException {
        String acceptHeader = request.getHeader("Accept");
        boolean quiereJson = acceptHeader != null && acceptHeader.contains("application/json");

        if (quiereJson) {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            StringBuilder json = new StringBuilder("{");
            json.append("\"status\":\"").append(exito ? "ok" : "error").append("\",");
            json.append("\"mensaje\":\"").append(mensaje).append("\"");

            if (usuario != null) {
                json.append(",\"usuario\":\"").append(usuario.getCorreo()).append("\",");
                json.append("\"rol\":\"").append(usuario.getRol()).append("\"");
            }
            json.append("}");

            response.getWriter().write(json.toString());
        } else {
            if (exito && usuario != null) {
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
                        response.sendRedirect("login.jsp?error=rol");
                }
            } else {
                request.setAttribute("error", mensaje);
                request.getRequestDispatcher("login.jsp").forward(request, response);
            }
        }
    }
}