/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.mysymphony.proyecto_symphony1.servlet;

/**
 * Servlet para registrar inscripciones institucionales
 * Rol: administrador
 * Autor: Camila
 * Trazabilidad: recibe datos, registra inscripción y envía mensaje a la vista
 */

import com.mysymphony.proyecto_symphony1.dao.InscripcionDAO;
import com.mysymphony.proyecto_symphony1.dao.AuditoriaDAO;
import com.mysymphony.proyecto_symphony1.util.Conexion;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "InscripcionServlet", urlPatterns = {"/InscripcionServlet"})
public class InscripcionServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession sesion = request.getSession(false);
        String usuario = (sesion != null) ? (String) sesion.getAttribute("nombreActivo") : "desconocido";
        String rol = (sesion != null) ? (String) sesion.getAttribute("rolActivo") : null;

        // 🔐 Validación de rol administrador
        if (rol == null || !"administrador".equalsIgnoreCase(rol)) {
            if (sesion != null) {
                sesion.setAttribute("mensaje", "⚠️ Acceso restringido: requiere rol administrador.");
            }
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        // 📥 Parámetros del formulario
        String fechaStr = request.getParameter("fecha_inscripcion");
        String claseIdStr = request.getParameter("id_clase");
        String idEstudianteStr = request.getParameter("id_estudiante");

        String mensaje;

        try (Connection conn = Conexion.getConnection()) {
            InscripcionDAO dao = new InscripcionDAO(conn);

            // ✅ Construir Map con los campos reales
            Map<String,Object> insc = new HashMap<>();

            // Convertir fecha a LocalDate
            try {
                if (fechaStr != null && !fechaStr.isEmpty()) {
                    insc.put("fecha_inscripcion", LocalDate.parse(fechaStr));
                }
            } catch (Exception e) {
                mensaje = "❌ Fecha inválida. Use formato yyyy-MM-dd. Causa: " + e.getMessage();
                request.setAttribute("mensaje", mensaje);
                request.getRequestDispatcher("inscripcion.jsp").forward(request, response);
                return;
            }

            try {
                if (claseIdStr != null && !claseIdStr.isEmpty()) {
                    insc.put("id_clase", Integer.parseInt(claseIdStr));
                }
                if (idEstudianteStr != null && !idEstudianteStr.isEmpty()) {
                    insc.put("id_estudiante", Integer.parseInt(idEstudianteStr));
                }
            } catch (NumberFormatException e) {
                mensaje = "❌ Datos inválidos en clase o estudiante. Causa: " + e.getMessage();
                request.setAttribute("mensaje", mensaje);
                request.getRequestDispatcher("inscripcion.jsp").forward(request, response);
                return;
            }

            boolean registrado = dao.registrar(insc);

            if (registrado) {
                mensaje = "✅ Inscripción registrada correctamente";

                // 📝 Registro en auditoría institucional
                AuditoriaDAO auditoriaDAO = new AuditoriaDAO(conn);
                Map<String, String> registro = new HashMap<>();
                registro.put("usuario", usuario);
                registro.put("rol", rol);
                registro.put("modulo", "Gestión de inscripciones");
                registro.put("accion", "Registró inscripción en clase ID " + claseIdStr +
                                       " para estudiante ID " + idEstudianteStr);
                registro.put("ip_origen", request.getRemoteAddr());
                auditoriaDAO.registrarAccion(registro);

            } else {
                mensaje = "❌ Error al registrar inscripción. Esto ocurre porque la inserción en la BD no se ejecutó.";
            }

        } catch (Exception e) {
            e.printStackTrace();
            mensaje = "❌ Error al conectar con la base de datos: " + e.getMessage() +
                      ". Esto ocurre por un problema en la conexión o en la consulta SQL.";
        }

        request.setAttribute("mensaje", mensaje);
        request.getRequestDispatcher("inscripcion.jsp").forward(request, response);
    }
}