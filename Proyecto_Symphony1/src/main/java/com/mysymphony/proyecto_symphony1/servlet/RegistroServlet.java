/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.mysymphony.proyecto_symphony1.servlet;

/**
 * Servlet para registrar nuevos usuarios institucionales
 * Rol por defecto: estudiante
 * Autor: Camila
 * Trazabilidad: valida campos, requisitos de contraseña, registra usuario,
 * guarda auditoría y bitácora, inserta en tablas docentes/estudiantes
 */

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
import java.sql.SQLException;

@WebServlet("/RegistroServlet")
public class RegistroServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String correo = request.getParameter("correo");
        String nombre = request.getParameter("nombre");
        String clave  = request.getParameter("clave");
        String rol    = request.getParameter("rol");

        try (Connection conn = Conexion.getConnection()) {
            UsuarioDAO dao = new UsuarioDAO(conn);

            // 🔎 Validar si el correo ya existe
            Usuario existente = dao.buscarPorCorreo(correo);
            if (existente != null) {
                request.setAttribute("error", "⚠️ El correo ya está registrado.");
                request.getRequestDispatcher("registro.jsp").forward(request, response);
                return;
            }

            // 📝 Crear nuevo usuario con hash de contraseña
            Usuario nuevo = new Usuario();
            nuevo.setCorreo(correo);
            nuevo.setNombre(nombre);
            nuevo.setClave(HashUtil.hashPassword(clave)); // cifrado seguro

            // ✅ Validar rol contra los valores permitidos en la BD
            String rolSeguro;
            if ("administrador".equalsIgnoreCase(rol) ||
                "docente".equalsIgnoreCase(rol) ||
                "estudiante".equalsIgnoreCase(rol)) {
                rolSeguro = rol.toLowerCase();
            } else {
                rolSeguro = "estudiante"; // valor por defecto
            }
            nuevo.setRol(rolSeguro);

            // 📌 Registrar usuario (el DAO ya inserta en estudiantes/docentes según rol)
            boolean registrado = dao.registrar(nuevo, nuevo.getClave());

            if (registrado) {
                int idUsuario = nuevo.getIdUsuario(); // ✅ ID asignado en el DAO

                // 🛡️ Auditoría institucional
                AuditoriaDAO auditoriaDAO = new AuditoriaDAO(conn);
                auditoriaDAO.registrarAuditoria(
                        "Registro de nuevo usuario: " + nombre,
                        "RegistroServlet",
                        correo
                );

                // 📝 Bitácora institucional
                BitacoraDAO bitacoraDAO = new BitacoraDAO(conn);
                bitacoraDAO.registrarAccion(
                        "Registro de usuario con rol " + nuevo.getRol(),
                        correo,              // usuario
                        nuevo.getRol(),      // rol
                        "RegistroServlet"    // módulo
                );

                request.setAttribute("mensaje", "✅ Usuario registrado correctamente.");
                request.getRequestDispatcher("login.jsp").forward(request, response);

            } else {
                request.setAttribute("error", "❌ Error al registrar usuario.");
                request.getRequestDispatcher("registro.jsp").forward(request, response);
            }

        } catch (SQLException e) {
            System.err.println("❌ Error en RegistroServlet: " + e.getMessage());
            if (e.getMessage() != null && e.getMessage().contains("Duplicate entry")) {
                request.setAttribute("error", "⚠️ El correo ya existe en el sistema.");
            } else {
                request.setAttribute("error", "❌ Error interno al registrar usuario.");
            }
            request.getRequestDispatcher("registro.jsp").forward(request, response);
        }
    }
}