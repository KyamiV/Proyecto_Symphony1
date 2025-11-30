/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.mysymphony.proyecto_symphony1.servlet;

/**
 * Servlet para mostrar el panel del estudiante con métricas institucionales.
 * Rol: estudiante
 * Autor: Camila
 * Trazabilidad: valida sesión, consulta métricas, envía notas y registra acceso en auditoría y bitácora.
 */

import com.mysymphony.proyecto_symphony1.dao.NotaDAO;
import com.mysymphony.proyecto_symphony1.dao.ClaseDAO;
import com.mysymphony.proyecto_symphony1.dao.AuditoriaDAO;
import com.mysymphony.proyecto_symphony1.dao.BitacoraDAO;
import com.mysymphony.proyecto_symphony1.util.Conexion;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.util.*;

@WebServlet("/PanelEstudianteServlet")
public class PanelEstudianteServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 🔐 Validación de sesión y rol
        HttpSession sesion = request.getSession(false);
        String rol = (sesion != null) ? (String) sesion.getAttribute("rolActivo") : null;
        String nombre = (sesion != null) ? (String) sesion.getAttribute("nombreActivo") : "desconocido";
        Integer estudianteId = (sesion != null) ? (Integer) sesion.getAttribute("idActivo") : null;

        if (rol == null || !"estudiante".equalsIgnoreCase(rol) || estudianteId == null) {
            if (sesion != null) {
                sesion.setAttribute("mensaje", "⚠️ Acceso restringido: requiere rol estudiante.");
            }
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        try (Connection conn = Conexion.getConnection()) {
            // 🧩 DAOs institucionales
            NotaDAO notaDAO = new NotaDAO(conn);
            ClaseDAO claseDAO = new ClaseDAO(conn);

            // 📊 Métricas institucionales
            int totalNotas = notaDAO.contarPorEstudiante(estudianteId);
            int totalClases = claseDAO.contarAsignadas(estudianteId);
            int totalCertificados = notaDAO.contarCertificadosEmitidos(estudianteId);

            // 📥 Consulta de notas para verNotas.jsp
            List<Map<String, String>> notas = notaDAO.obtenerNotasPorEstudiante(estudianteId);

            // 📤 Envío de atributos a la vista
            request.setAttribute("totalNotasEstudiante", totalNotas);
            request.setAttribute("totalClasesInscritas", totalClases);
            request.setAttribute("totalCertificados", totalCertificados);
            request.setAttribute("notas", notas);

            // 📝 Registro en bitácora institucional
            BitacoraDAO bitacoraDAO = new BitacoraDAO(conn);
            bitacoraDAO.registrarAccion("Estudiante accedió al panel institucional",
                    nombre, rol, "Panel estudiante");

            // 🛡️ Registro en auditoría institucional
            Map<String, String> registro = new HashMap<>();
            registro.put("usuario", nombre);
            registro.put("rol", rol);
            registro.put("modulo", "Panel Estudiante");
            registro.put("accion", "Accedió al panel principal");
            registro.put("ip_origen", request.getRemoteAddr());
            new AuditoriaDAO(conn).registrarAccion(registro);

        } catch (Exception e) {
            // ⚠️ Manejo de errores
            System.err.println("❌ Error al cargar métricas del panel estudiante: " + e.getMessage());
            if (sesion != null) {
                sesion.setAttribute("mensaje", "❌ Error al cargar el panel del estudiante: " + e.getMessage());
            }
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        // 🎯 Redirección a la vista principal del estudiante
        request.getRequestDispatcher("/estudiante/estudiante.jsp").forward(request, response);
    }
}