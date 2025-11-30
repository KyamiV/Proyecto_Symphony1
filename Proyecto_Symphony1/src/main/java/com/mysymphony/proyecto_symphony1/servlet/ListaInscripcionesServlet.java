/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.mysymphony.proyecto_symphony1.servlet;

/**
 * Servlet institucional para listar inscripciones de clases.
 * Rol: Administrador
 * Función: Consultar inscripciones registradas en la tabla inscripciones_clase y enviarlas al JSP.
 * Autor: camiv
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

@WebServlet("/ListaInscripcionesServlet")
public class ListaInscripcionesServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession sesion = request.getSession(false);
        String rol = (sesion != null) ? (String) sesion.getAttribute("rolActivo") : null;
        String usuario = (sesion != null) ? (String) sesion.getAttribute("nombreActivo") : "desconocido";

        // 🔐 Validación de rol administrador
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

        try (Connection conn = Conexion.getConnection()) {
            InscripcionDAO inscripcionDAO = new InscripcionDAO(conn);
            AuditoriaDAO auditoriaDAO = new AuditoriaDAO(conn);

            // 📌 Consultar todas las inscripciones desde inscripciones_clase
            List<Map<String,Object>> inscripciones = inscripcionDAO.listar();
            request.setAttribute("inscripciones", inscripciones);

            if (inscripciones.isEmpty()) {
                String mensajeInfo = "ℹ️ Aviso: no se encontraron inscripciones. " +
                                     "Esto ocurre porque la base de datos no tiene registros.";
                System.out.println(mensajeInfo);
                request.setAttribute("mensajeInfo", mensajeInfo);
            }

            // 📝 Registrar acción en auditoría
            Map<String, String> registro = new HashMap<>();
            registro.put("usuario", usuario + " (ID: " + sesion.getAttribute("idActivo") + ")");
            registro.put("rol", rol);
            registro.put("modulo", "Inscripciones");
            registro.put("accion", "Listar inscripciones");
            registro.put("detalle", "Se consultaron " + inscripciones.size() + " inscripciones de la tabla institucional.");
            registro.put("ip_origen", request.getRemoteAddr());

            auditoriaDAO.registrarAccion(registro);

            // 👉 Forward al JSP institucional
            request.getRequestDispatcher("/administrador/listarInscripciones.jsp").forward(request, response);

        } catch (Exception e) {
            String mensajeError = "❌ Error al listar inscripciones: " + e.getMessage() +
                                  ". Esto ocurre por un problema en la conexión a la base de datos o en la consulta SQL.";
            System.err.println(mensajeError);
            e.printStackTrace();
            if (sesion != null) {
                sesion.setAttribute("mensaje", mensajeError);
                sesion.setAttribute("tipoMensaje", "error");
            }
            response.sendRedirect(request.getContextPath() + "/panelAdministrador.jsp");
        }
    }
}