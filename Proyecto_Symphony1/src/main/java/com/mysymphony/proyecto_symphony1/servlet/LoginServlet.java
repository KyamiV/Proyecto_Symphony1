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

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet(name = "LoginServlet", urlPatterns = {"/LoginServlet"})
public class LoginServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String correo = request.getParameter("usuario"); // ← coincide con name="usuario" en login.jsp
        String clave = request.getParameter("clave");

        if (correo == null || clave == null || correo.isEmpty() || clave.isEmpty()) {
            request.setAttribute("error", "⚠️ Debes ingresar correo y contraseña.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
            return;
        }

        String claveHash = HashUtil.sha256(clave);

        System.out.println("📨 Correo ingresado: " + correo);
        System.out.println("🔐 Hash generado: " + claveHash);

        UsuarioDAO dao = new UsuarioDAO();
        Usuario usuario = dao.validar(correo, claveHash);

        if (usuario != null) {
            System.out.println("✅ Usuario autenticado: " + usuario.getCorreo());

            HttpSession session = request.getSession();
            session.setAttribute("usuarioActivo", usuario.getCorreo());
            session.setAttribute("nombreActivo", usuario.getNombre());
            session.setAttribute("rolActivo", usuario.getRol());

            // ✅ Redirigir según el rol
            switch (usuario.getRol().toLowerCase()) {
                case "estudiante":
                    response.sendRedirect("estudiante/estudiante.jsp");
                    break;
                case "docente":
                    response.sendRedirect("dashboard.jsp");
                    break;
                case "administrador":
                    response.sendRedirect("administrador/adminNotas.jsp");
                    break;
                default:
                    response.sendRedirect("login.jsp?error=rol");
            }

        } else {
            System.out.println("❌ Usuario no encontrado o credenciales incorrectas.");
            request.setAttribute("error", "❌ Credenciales inválidas. Intenta nuevamente.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }
}