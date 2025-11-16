/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.mysymphony.proyecto_symphony1.servlet;

import com.mysymphony.proyecto_symphony1.dao.UsuarioDAO;
import com.mysymphony.proyecto_symphony1.modelo.Usuario;
import com.mysymphony.proyecto_symphony1.util.HashUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet(name = "RegistroServlet", urlPatterns = {"/RegistroServlet"})
public class RegistroServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String correo = request.getParameter("correo");
        String nombre = request.getParameter("nombre");
        String clave = request.getParameter("clave");
        String rol = request.getParameter("rol"); // puede venir oculto o como select

        if (correo == null || nombre == null || clave == null ||
            correo.isEmpty() || nombre.isEmpty() || clave.isEmpty()) {

            request.setAttribute("error", "⚠️ Todos los campos son obligatorios.");
            request.getRequestDispatcher("registro.jsp").forward(request, response);
            return;
        }

        UsuarioDAO dao = new UsuarioDAO();
        Usuario existente = dao.buscarPorCorreo(correo);

        if (existente != null) {
            request.setAttribute("error", "❌ El correo ya está registrado.");
            request.getRequestDispatcher("registro.jsp").forward(request, response);
            return;
        }

        String claveHash = HashUtil.sha256(clave);

        Usuario nuevo = new Usuario();
        nuevo.setCorreo(correo);
        nuevo.setNombre(nombre);
        nuevo.setRol(rol != null ? rol : "estudiante"); // por defecto estudiante

        boolean registrado = dao.registrar(nuevo, claveHash);

        if (registrado) {
            request.setAttribute("mensaje", "✅ Registro exitoso. Ahora puedes iniciar sesión.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        } else {
            request.setAttribute("error", "❌ Error al registrar. Intenta nuevamente.");
            request.getRequestDispatcher("registro.jsp").forward(request, response);
        }
    }
}