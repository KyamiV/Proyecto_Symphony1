/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.mysymphony.proyecto_symphony1.servlet;

/**
 * Servlet para listar inscripciones institucionales con filtro opcional
 * Rol: administrador
 * Autor: Camila
 * Trazabilidad: consulta desde InscripcionDAO, valida rol y registra acción en auditoría
 */

import com.mysymphony.proyecto_symphony1.dao.InscripcionDAO;
import com.mysymphony.proyecto_symphony1.dao.AuditoriaDAO;
import com.mysymphony.proyecto_symphony1.util.Conexion;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "InscripcionListServlet", urlPatterns = {"/InscripcionListServlet"})
public class InscripcionListServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession sesion = request.getSession(false);
        String rol = (sesion != null) ? (String) sesion.getAttribute("rolActivo") : null;
        String usuario = (sesion != null) ? (String) sesion.getAttribute("nombreActivo") : "desconocido";

        // 🔐 Validación de rol
        if (rol == null || !"administrador".equalsIgnoreCase(rol)) {
            String mensajeError = "⚠️ Acceso restringido: requiere rol administrador. " +
                                  "Esto ocurre porque el usuario actual no tiene permisos suficientes.";
            System.err.println(mensajeError);
            if (sesion != null) {
                sesion.setAttribute("mensaje", mensajeError);
            }
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        String filtro = request.getParameter("filtro");
        List<Map<String,Object>> lista;

        try (Connection conn = Conexion.getConnection()) {
            InscripcionDAO dao = new InscripcionDAO(conn);

            // ✅ Consultar inscripciones con o sin filtro
            if (filtro != null && !filtro.trim().isEmpty()) {
                System.out.println("DEBUG Servlet: aplicando filtro -> " + filtro);
                lista = dao.buscar(filtro.trim()); // debe existir buscar(String filtro) en tu DAO
            } else {
                System.out.println("DEBUG Servlet: sin filtro, listando todas las inscripciones");
                lista = dao.listar();
            }

            if (lista.isEmpty()) {
                String mensajeInfo = "ℹ️ Aviso: no se encontraron inscripciones. " +
                                     "Esto ocurre porque la base de datos no tiene registros que coincidan con el filtro.";
                System.out.println(mensajeInfo);
                request.setAttribute("mensajeInfo", mensajeInfo);
            }

            request.setAttribute("inscripciones", lista);

            // 📝 Registro en auditoría institucional
            AuditoriaDAO auditoriaDAO = new AuditoriaDAO(conn);
            Map<String, String> registro = new HashMap<>();
            registro.put("usuario", usuario);
            registro.put("rol", rol);
            registro.put("modulo", "Inscripciones");
            registro.put("accion", (filtro != null && !filtro.trim().isEmpty())
                    ? "Consultó inscripciones con filtro '" + filtro + "'"
                    : "Consultó todas las inscripciones");
            registro.put("ip_origen", request.getRemoteAddr());
            auditoriaDAO.registrarAccion(registro);

        } catch (Exception e) {
            String mensajeError = "❌ Error al consultar inscripciones: " + e.getMessage() +
                                  ". Esto ocurre por un problema en la conexión a la base de datos o en la consulta SQL.";
            System.err.println(mensajeError);
            e.printStackTrace();
            if (sesion != null) {
                sesion.setAttribute("mensaje", mensajeError);
            }
            response.sendRedirect(request.getContextPath() + "/panelAdministrador.jsp");
            return;
        }

        // ✅ Redirigir a la vista institucional
        request.getRequestDispatcher("listaInscripciones.jsp").forward(request, response);
    }
}