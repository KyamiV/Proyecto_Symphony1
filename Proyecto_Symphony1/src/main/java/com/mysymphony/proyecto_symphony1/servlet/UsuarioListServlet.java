/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mysymphony.proyecto_symphony1.servlet;

import com.mysymphony.proyecto_symphony1.dao.UsuarioDAO;
import com.mysymphony.proyecto_symphony1.modelo.Usuario;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "UsuarioListServlet", urlPatterns = {"/UsuarioListServlet"})
public class UsuarioListServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        String rol = (session != null) ? (String) session.getAttribute("rolActivo") : null;

        if (rol == null || !rol.equalsIgnoreCase("coordinador académico")) {
            response.sendRedirect("login.jsp");
            return;
        }

        UsuarioDAO dao = new UsuarioDAO();
        List<Usuario> usuarios = dao.listarTodos();
        request.setAttribute("usuarios", usuarios);
        request.getRequestDispatcher("usuarios.jsp").forward(request, response);
    }
}