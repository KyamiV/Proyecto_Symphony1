/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mysymphony.proyecto_symphony1.servlet;

import com.mysymphony.proyecto_symphony1.dao.DocenteDAO;
import com.mysymphony.proyecto_symphony1.modelo.Docente;
import com.mysymphony.proyecto_symphony1.util.Conexion;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import javax.servlet.RequestDispatcher;
import java.io.IOException;
import java.sql.Connection;

/**
 * Servlet: PerfilDocenteServlet
 * Rol: Docente
 * Autor: camiv
 *
 * Propósito:
 *   - Mostrar y permitir edición del perfil del docente.
 *   - Validar sesión y rol activo.
 *   - Usar DocenteDAO para actualizar datos personales (excepto fechaIngreso y estado).
 *   - Enviar datos al JSP perfilDocente.jsp.
 */
@WebServlet("/PerfilDocenteServlet")
public class PerfilDocenteServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession sesion = request.getSession(false);
        String rol = (sesion != null) ? (String) sesion.getAttribute("rolActivo") : null;
        Integer idDocente = (sesion != null) ? (Integer) sesion.getAttribute("idActivo") : null;

        // 🔐 Validación de rol docente
        if (rol == null || !"docente".equalsIgnoreCase(rol) || idDocente == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        try (Connection conn = Conexion.getConnection()) {
            DocenteDAO dao = new DocenteDAO(conn);
            Docente docente = dao.obtenerPorId(idDocente);

            request.setAttribute("docentePerfil", docente);

            RequestDispatcher dispatcher = request.getRequestDispatcher("docente/perfilDocente.jsp");
            dispatcher.forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            sesion.setAttribute("mensaje", "❌ Error al cargar perfil: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/PanelDocenteServlet");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession sesion = request.getSession(false);
        Integer idDocente = (sesion != null) ? (Integer) sesion.getAttribute("idActivo") : null;

        if (idDocente == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        try (Connection conn = Conexion.getConnection()) {
            DocenteDAO dao = new DocenteDAO(conn);

            // 📌 Construir objeto Docente con datos del formulario
            Docente d = new Docente();
            d.setId(idDocente);
            d.setNombre(request.getParameter("nombre"));
            d.setApellido(request.getParameter("apellido"));
            d.setCorreo(request.getParameter("correo"));
            d.setTelefono(request.getParameter("telefono"));
            d.setDireccion(request.getParameter("direccion"));

            // ⚠️ No se actualizan fechaIngreso ni estado (institucionales)
            d.setNivelTecnico(request.getParameter("nivel_tecnico")); // ✅ editable

            // 📦 Actualizar en BD (solo datos permitidos)
            dao.actualizarPerfilDocente(d);

            sesion.setAttribute("mensaje", "✅ Perfil actualizado correctamente.");
            response.sendRedirect(request.getContextPath() + "/PerfilDocenteServlet");

        } catch (Exception e) {
            e.printStackTrace();
            sesion.setAttribute("mensaje", "❌ Error al actualizar perfil: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/PerfilDocenteServlet");
        }
    }
}