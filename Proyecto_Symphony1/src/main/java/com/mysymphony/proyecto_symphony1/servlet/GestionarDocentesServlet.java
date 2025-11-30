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
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.util.List;

/**
 * Servlet maestro para gestión institucional de docentes.
 * Rol: Administrador
 * Función: Listar, registrar, actualizar, editar y eliminar docentes.
 * Autor: camiv
 *
 * Trazabilidad:
 *   - Valida rol administrador.
 *   - Usa DocenteDAO para operaciones CRUD.
 *   - Envía lista y mensajes al JSP gestionarDocentes.jsp.
 */
@WebServlet("/GestionarDocentesServlet")
public class GestionarDocentesServlet extends HttpServlet {

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
            DocenteDAO docenteDAO = new DocenteDAO(conn);

            // 📦 Obtener lista de docentes
            List<Docente> docentes = docenteDAO.listarTodos();
            request.setAttribute("docentes", docentes);

            // 👉 Forward al JSP principal de gestión de docentes
            request.getRequestDispatcher("administrador/gestionarDocentes.jsp")
                   .forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            if (sesion != null) {
                sesion.setAttribute("mensaje", "❌ Error al cargar docentes: " + e.getMessage());
            }
            response.sendRedirect(request.getContextPath() + "/panelAdministrador.jsp");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession sesion = request.getSession(false);
        String rol = (sesion != null) ? (String) sesion.getAttribute("rolActivo") : null;

        if (rol == null || !"administrador".equalsIgnoreCase(rol)) {
            if (sesion != null) {
                sesion.setAttribute("mensaje", "⚠️ Acceso restringido: requiere rol administrador.");
            }
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        String accion = request.getParameter("accion");

        try (Connection conn = Conexion.getConnection()) {
            DocenteDAO docenteDAO = new DocenteDAO(conn);

            if ("registrar".equalsIgnoreCase(accion)) {
                // 📌 Registrar nuevo docente
                Docente d = new Docente();
                d.setNombre(request.getParameter("nombre"));
                d.setApellido(request.getParameter("apellido"));
                d.setCorreo(request.getParameter("correo"));
                d.setTelefono(request.getParameter("telefono"));
                d.setDireccion(request.getParameter("direccion"));
                d.setNivelTecnico(request.getParameter("nivel_tecnico")); // ✅ nuevo campo

                String fechaIngresoParam = request.getParameter("fecha_ingreso");
                if (fechaIngresoParam != null && !fechaIngresoParam.isEmpty()) {
                    d.setFechaIngreso(Date.valueOf(fechaIngresoParam));
                }

                d.setEstado(request.getParameter("estado"));

                docenteDAO.insertarDocente(d);
                sesion.setAttribute("mensaje", "✅ Docente registrado correctamente.");

                response.sendRedirect(request.getContextPath() + "/GestionarDocentesServlet");

            } else if ("editar".equalsIgnoreCase(accion)) {
                // 📌 Cargar datos para edición
                int idDocente = Integer.parseInt(request.getParameter("id_docente"));
                Docente docenteEditar = docenteDAO.obtenerPorId(idDocente);
                request.setAttribute("docenteEditar", docenteEditar);

                // Forward al JSP principal, que abrirá el modal
                request.getRequestDispatcher("administrador/gestionarDocentes.jsp")
                       .forward(request, response);
                return; // ⚠️ Importante: detener aquí para no hacer el redirect

            } else if ("actualizar".equalsIgnoreCase(accion)) {
                // 📌 Actualizar docente existente
                Docente d = new Docente();
                d.setId(Integer.parseInt(request.getParameter("id_docente")));
                d.setNombre(request.getParameter("nombre"));
                d.setApellido(request.getParameter("apellido"));
                d.setCorreo(request.getParameter("correo"));
                d.setTelefono(request.getParameter("telefono"));
                d.setDireccion(request.getParameter("direccion"));
                d.setNivelTecnico(request.getParameter("nivel_tecnico")); // ✅ nuevo campo

                String fechaIngresoParam = request.getParameter("fecha_ingreso");
                if (fechaIngresoParam != null && !fechaIngresoParam.isEmpty()) {
                    d.setFechaIngreso(Date.valueOf(fechaIngresoParam));
                }

                d.setEstado(request.getParameter("estado"));

                docenteDAO.actualizarDocente(d);
                sesion.setAttribute("mensaje", "✅ Docente actualizado correctamente.");

                response.sendRedirect(request.getContextPath() + "/GestionarDocentesServlet");

            } else if ("eliminar".equalsIgnoreCase(accion)) {
                // 📌 Eliminar docente
                int idDocente = Integer.parseInt(request.getParameter("id_docente"));
                docenteDAO.eliminarDocente(idDocente);
                sesion.setAttribute("mensaje", "🗑️ Docente eliminado correctamente.");

                response.sendRedirect(request.getContextPath() + "/GestionarDocentesServlet");
            }

        } catch (Exception e) {
            e.printStackTrace();
            if (sesion != null) {
                sesion.setAttribute("mensaje", "❌ Error en gestión de docentes: " + e.getMessage());
            }
            response.sendRedirect(request.getContextPath() + "/panelAdministrador.jsp");
        }
    }
}