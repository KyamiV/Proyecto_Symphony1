package com.mysymphony.proyecto_symphony1.servlet;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author camiv
 */

import com.mysymphony.proyecto_symphony1.dao.UsuarioDAO;
import com.mysymphony.proyecto_symphony1.modelo.Usuario;
import com.mysymphony.proyecto_symphony1.util.HashUtil;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;
import java.io.IOException;

@WebServlet(name = "LoginServlet", urlPatterns = {"/LoginServlet"})
public class LoginServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String correo = request.getParameter("usuario");
        String clave = request.getParameter("clave");
        String claveHash = HashUtil.sha256(clave);

        UsuarioDAO dao = new UsuarioDAO();
        Usuario usuario = dao.validar(correo, claveHash);

        if (usuario != null) {
            HttpSession session = request.getSession();
            session.setAttribute("usuarioActivo", usuario.getCorreo());
            session.setAttribute("nombreActivo", usuario.getNombre());
            session.setAttribute("rolActivo", usuario.getRol());
            response.sendRedirect("dashboard.jsp");
        } else {
            request.setAttribute("error", "Credenciales inválidas");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }
}