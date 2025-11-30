/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mysymphony.proyecto_symphony1.servlet;

import com.mysymphony.proyecto_symphony1.dao.ClaseDAO;
import com.mysymphony.proyecto_symphony1.util.Conexion;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import javax.servlet.RequestDispatcher;
import java.io.IOException;
import java.sql.Connection;
import java.util.List;
import java.util.Map;

/**
 * Servlet para listar clases certificables con detalle institucional.
 * Rol: administrador
 * Autor: Camila
 * Ruta: /ListarClasesCertificablesServlet
 */
@WebServlet("/ListarClasesCertificablesServlet")
public class ListarClasesCertificablesServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession sesion = request.getSession(false);
        String rol = (sesion != null) ? (String) sesion.getAttribute("rolActivo") : null;

        // 🔐 Validación de rol administrador
        if (rol == null || !"administrador".equalsIgnoreCase(rol)) {
            if (sesion != null) {
                sesion.setAttribute("mensaje", "⚠️ Acceso restringido: requiere rol administrador.");
            }
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        try (Connection conn = Conexion.getConnection()) {
            ClaseDAO claseDAO = new ClaseDAO(conn);

            // ✅ Consultar clases con detalle en formato Map
            List<Map<String, String>> clases = claseDAO.listarClasesConDetalle();
            request.setAttribute("tablasEnviadas", clases);

            // ✅ Redirigir al JSP institucional
            RequestDispatcher dispatcher = request.getRequestDispatcher("/admin/clasesCertificables.jsp");
            dispatcher.forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "❌ Error al cargar clases certificables: " + e.getMessage());
            request.getRequestDispatcher("/admin/clasesCertificables.jsp").forward(request, response);
        }
    }
}