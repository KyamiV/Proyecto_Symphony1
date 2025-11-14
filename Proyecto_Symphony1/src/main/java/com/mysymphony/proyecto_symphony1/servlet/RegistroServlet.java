/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.mysymphony.proyecto_symphony1.servlet;

import com.mysymphony.proyecto_symphony1.dao.UsuarioDAO;
import com.mysymphony.proyecto_symphony1.modelo.Usuario;
import com.mysymphony.proyecto_symphony1.util.HashUtil;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "RegistroServlet", urlPatterns = {"/RegistroServlet"})
public class RegistroServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String correo = request.getParameter("correo");
        String nombre = request.getParameter("nombre");
        String clave = request.getParameter("clave");
        String rol = request.getParameter("rol");

        if (correo == null || nombre == null || clave == null || rol == null ||
            correo.isEmpty() || nombre.isEmpty() || clave.isEmpty() || rol.isEmpty()) {
            request.setAttribute("mensaje", "Todos los campos son obligatorios.");
            request.getRequestDispatcher("registro.jsp").forward(request, response);
            return;
        }

        String claveHash = HashUtil.sha256(clave);
        Usuario nuevo = new Usuario(correo, nombre, rol);
        UsuarioDAO dao = new UsuarioDAO();
        boolean registrado = dao.registrar(nuevo, claveHash);

        if (registrado) {
            request.setAttribute("mensaje", "Registro exitoso. Inicia sesión.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        } else {
            request.setAttribute("mensaje", "Error al registrar usuario");
            request.getRequestDispatcher("registro.jsp").forward(request, response);
        }
    }
}