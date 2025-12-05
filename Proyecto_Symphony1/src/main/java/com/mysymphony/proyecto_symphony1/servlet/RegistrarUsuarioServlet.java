/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.mysymphony.proyecto_symphony1.servlet;

import com.mysymphony.proyecto_symphony1.dao.UsuarioDAO;
import com.mysymphony.proyecto_symphony1.dao.AuditoriaDAO;
import com.mysymphony.proyecto_symphony1.dao.BitacoraDAO;
import com.mysymphony.proyecto_symphony1.modelo.Usuario;
import com.mysymphony.proyecto_symphony1.util.Conexion;
import com.mysymphony.proyecto_symphony1.util.HashUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Servlet para registrar usuarios institucionales.
 * Rol requerido: administrador
 * Autor: Camila
 * Trazabilidad: registra en BD, bitácora y auditoría institucional.
 */
@WebServlet("/RegistrarUsuarioServlet")
public class RegistrarUsuarioServlet extends HttpServlet {

    // ✅ GET: mostrar formulario de registro
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession sesion = request.getSession(false);
        String rol = (sesion != null) ? (String) sesion.getAttribute("rolActivo") : null;

        // 🔒 Validación de rol administrador
        if (rol == null || !"administrador".equalsIgnoreCase(rol)) {
            if (sesion != null) {
                sesion.setAttribute("mensaje", "⚠️ Acceso restringido: requiere rol administrador.");
            }
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        // Forward al formulario JSP institucional
        request.getRequestDispatcher("/administrador/registrarUsuario.jsp").forward(request, response);
    }

    // ✅ POST: procesar registro de usuario
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession sesion = request.getSession(false);
        String rol = (sesion != null) ? (String) sesion.getAttribute("rolActivo") : null;
        String admin = (sesion != null) ? (String) sesion.getAttribute("nombreActivo") : "desconocido";

        // 🔒 Validación de rol administrador
        if (rol == null || !"administrador".equalsIgnoreCase(rol)) {
            if (sesion != null) {
                sesion.setAttribute("mensaje", "⚠️ Acceso restringido: requiere rol administrador.");
            }
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        // 📥 Parámetros básicos del formulario
        String nombre = request.getParameter("nombre");
        String correo = request.getParameter("usuario");
        String clave = request.getParameter("clave");
        String rolNuevo = request.getParameter("rol");

        String mensaje;

        try (Connection conn = Conexion.getConnection()) {
            UsuarioDAO dao = new UsuarioDAO(conn);
            Usuario nuevo = new Usuario();
            nuevo.setNombre(nombre);
            nuevo.setCorreo(correo);

            // ⚠️ Seguridad: cifrar clave antes de guardar
            String claveHash = HashUtil.hashPassword(clave);
            nuevo.setClave(claveHash);

            // Rol validado, por defecto estudiante
            if ("administrador".equalsIgnoreCase(rolNuevo) ||
                "docente".equalsIgnoreCase(rolNuevo) ||
                "estudiante".equalsIgnoreCase(rolNuevo)) {
                nuevo.setRol(rolNuevo.toLowerCase());
            } else {
                nuevo.setRol("estudiante");
            }

            boolean registrado = dao.registrarBasico(nuevo);

            // 🛡️ Auditoría institucional
            Map<String, String> registro = new HashMap<>();
            registro.put("usuario", admin + " (ID: " + sesion.getAttribute("idActivo") + ")");
            registro.put("rol", rol);
            registro.put("modulo", "Gestión de usuarios");

            if (registrado) {
                registro.put("accion", "Registró nuevo usuario institucional: " + nombre + " (" + nuevo.getRol() + ")");
                registro.put("detalle", "Correo: " + correo);
                new AuditoriaDAO(conn).registrarAccion(registro);

                // 📝 Bitácora institucional
                BitacoraDAO bitacoraDAO = new BitacoraDAO(conn);
                bitacoraDAO.registrarAccion(
                        "Administrador registró usuario institucional: " + nombre,
                        admin,
                        rol,
                        "Gestión de usuarios"
                );

                // ✅ Crear entrada en docentes si el rol es docente
                if ("docente".equalsIgnoreCase(nuevo.getRol())) {
                    String[] partes = nuevo.getNombre().split(" ", 2);
                    String nombreDocente = partes[0];
                    String apellidoDocente = (partes.length > 1) ? partes[1] : "";

                    String sqlDocente = "INSERT INTO docentes (id_usuario, nombre, apellido, correo, especialidad, fecha_ingreso, direccion, telefono, estado) " +
                                        "VALUES (?, ?, ?, ?, ?, NOW(), ?, ?, ?)";

                    try (PreparedStatement ps = conn.prepareStatement(sqlDocente)) {
                        ps.setInt(1, dao.obtenerUltimoId()); // 🔑 id_usuario recién creado en usuarios
                        ps.setString(2, nombreDocente);
                        ps.setString(3, apellidoDocente);
                        ps.setString(4, nuevo.getCorreo());
                        ps.setString(5, ""); // especialidad opcional, vacío por ahora
                        ps.setString(6, ""); // dirección opcional
                        ps.setString(7, ""); // teléfono opcional
                        ps.setString(8, "activo");
                        ps.executeUpdate();
                    } catch (SQLException e) {
                        System.err.println("❌ Error al crear entrada en docentes: " + e.getMessage());
                    }
                }

                mensaje = "✅ Usuario registrado correctamente. El usuario podrá completar su perfil al ingresar.";
            } else {
                registro.put("accion", "Intentó registrar usuario pero falló.");
                registro.put("detalle", "Correo: " + correo);
                new AuditoriaDAO(conn).registrarAccion(registro);
                mensaje = "⚠️ No se pudo registrar el usuario.";
            }

        } catch (Exception e) {
            e.printStackTrace();
            mensaje = "❌ Error al registrar usuario.";
        }
      

        // ✅ Respuesta JSON si la petición viene de Postman
        String acceptHeader = request.getHeader("Accept");
        if (acceptHeader != null && acceptHeader.contains("application/json")) {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("{\"status\":\"ok\",\"mensaje\":\"" + mensaje + "\"}");
            return; // 👈 importante: salir para no redirigir
        }

        // 👉 Si no es JSON (ej. navegador), redirigir normalmente
        if (sesion != null) {
            sesion.setAttribute("mensaje", mensaje);
        }
        response.sendRedirect("VerUsuariosServlet");
    }
}