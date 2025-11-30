/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.mysymphony.proyecto_symphony1.servlet;

import com.mysymphony.proyecto_symphony1.dao.BitacoraDAO;
import com.mysymphony.proyecto_symphony1.dao.AuditoriaDAO;
import com.mysymphony.proyecto_symphony1.util.Conexion;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "LogoutServlet", urlPatterns = {"/LogoutServlet"})
public class LogoutServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        String usuario = (session != null) ? (String) session.getAttribute("nombreActivo") : "desconocido";
        String rol = (session != null) ? (String) session.getAttribute("rolActivo") : "desconocido";

        try (Connection conn = Conexion.getConnection()) {
            if (session != null) {
                // 📝 Registro en bitácora institucional
                BitacoraDAO bitacoraDAO = new BitacoraDAO(conn);
                bitacoraDAO.registrarAccion("Cerró sesión en el sistema", usuario, rol, "Logout");

                // 🛡️ Registro en auditoría técnica
                Map<String, String> registro = new HashMap<>();
                registro.put("usuario", usuario);
                registro.put("rol", rol);
                registro.put("modulo", "Logout");
                registro.put("accion", "Cerró sesión en el sistema");
                new AuditoriaDAO(conn).registrarAccion(registro);

                // Invalidar sesión
                session.invalidate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Limpiar cookie JSESSIONID
        Cookie cookie = new Cookie("JSESSIONID", "");
        cookie.setMaxAge(0);
        cookie.setPath("/"); // más seguro que usar contextPath
        response.addCookie(cookie);

        // Redirigir al login
        response.sendRedirect(request.getContextPath() + "/login.jsp");
    }
}