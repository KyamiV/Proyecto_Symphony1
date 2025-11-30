/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mysymphony.proyecto_symphony1.servlet;

import com.mysymphony.proyecto_symphony1.dao.UsuarioDAO;
import com.mysymphony.proyecto_symphony1.dao.AuditoriaDAO;
import com.mysymphony.proyecto_symphony1.dao.BitacoraDAO;
import com.mysymphony.proyecto_symphony1.modelo.Usuario;
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

/**
 * Servlet institucional para registrar docentes desde el panel administrador.
 * Autor: Camila
 * Flujo: valida rol administrador, inserta en BD, registra en bitácora y auditoría.
 */
@WebServlet("/RegistrarDocenteServlet")
public class RegistrarDocenteServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession sesion = request.getSession(false);
        String rol = (sesion != null) ? (String) sesion.getAttribute("rolActivo") : null;
        String admin = (sesion != null) ? (String) sesion.getAttribute("nombreActivo") : "desconocido";

        // 🔐 Validación de rol administrador
        if (rol == null || !"administrador".equalsIgnoreCase(rol)) {
            if (sesion != null) {
                sesion.setAttribute("mensaje", "⚠️ Acceso restringido: requiere rol administrador.");
            }
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        // 📥 Parámetros del formulario
        String nombre = request.getParameter("nombre");
        String correo = request.getParameter("correo");
        String clave = request.getParameter("clave");
        String especialidad = request.getParameter("especialidad");

        String mensaje;

        try (Connection conn = Conexion.getConnection()) {
            UsuarioDAO dao = new UsuarioDAO(conn);
            Usuario nuevo = new Usuario();
            nuevo.setNombre(nombre);
            nuevo.setCorreo(correo);
            nuevo.setRol("docente");
            nuevo.setInstrumento(especialidad); // se usa como especialidad

            // ⚠️ Seguridad: cifrar clave antes de guardar
            String claveHash = HashUtil.hashPassword(clave);

            boolean registrado = dao.registrar(nuevo, claveHash);

            // 🛡️ Auditoría institucional
            Map<String, String> registro = new HashMap<>();
            registro.put("usuario", admin + " (ID: " + sesion.getAttribute("idActivo") + ")");
            registro.put("rol", rol);
            registro.put("modulo", "Gestión de docentes");

            if (registrado) {
                registro.put("accion", "Registró nuevo docente institucional: " + nombre);
                registro.put("detalle", "Correo: " + correo + ", Especialidad: " + especialidad);
                new AuditoriaDAO(conn).registrarAccion(registro);

                // 📝 Bitácora institucional
                BitacoraDAO bitacoraDAO = new BitacoraDAO(conn);
                bitacoraDAO.registrarAccion(
                        "Administrador registró docente institucional: " + nombre,
                        admin,
                        rol,
                        "Gestión de docentes"
                );

                mensaje = "✅ Docente registrado correctamente.";
            } else {
                registro.put("accion", "Intentó registrar docente pero falló.");
                registro.put("detalle", "Correo: " + correo);
                new AuditoriaDAO(conn).registrarAccion(registro);
                mensaje = "⚠️ No se pudo registrar el docente.";
            }

        } catch (Exception e) {
            e.printStackTrace();
            mensaje = "❌ Error al registrar docente.";
        }

        if (sesion != null) {
            sesion.setAttribute("mensaje", mensaje);
        }
        response.sendRedirect("VerUsuariosServlet"); // o a un listado de docentes
    }
}