/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.mysymphony.proyecto_symphony1.servlet;

import com.mysymphony.proyecto_symphony1.dto.DocenteConClaseDTO;
import com.mysymphony.proyecto_symphony1.dao.ClaseDAO;
import com.mysymphony.proyecto_symphony1.dao.AsignacionDAO;
import com.mysymphony.proyecto_symphony1.dao.AuditoriaDAO;
import com.mysymphony.proyecto_symphony1.modelo.Clase;
import com.mysymphony.proyecto_symphony1.util.Conexion;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.util.*;

/**
 * Servlet principal para gestión institucional de clases.
 * Rol: Administrador
 * Función: Cargar clases disponibles, docentes asignados, tablas enviadas y auditoría.
 * Autor: camiv
 */
@WebServlet("/GestionarClasesPrincipalServlet")
public class GestionarClasesPrincipalServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession sesion = request.getSession(false);
        String rol = (sesion != null) ? (String) sesion.getAttribute("rolActivo") : null;
        String usuario = (sesion != null) ? (String) sesion.getAttribute("nombreActivo") : "desconocido";

        // 🔐 Validación de rol administrador
        if (rol == null || !"administrador".equalsIgnoreCase(rol)) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        try (Connection conn = Conexion.getConnection()) {

            // 📦 DAOs institucionales
            ClaseDAO claseDAO = new ClaseDAO(conn);
            AsignacionDAO asignacionDAO = new AsignacionDAO(conn);
            AuditoriaDAO auditoriaDAO = new AuditoriaDAO(conn);

            // 1️⃣ Clases disponibles con inscritos reales
            List<Clase> clasesDisponibles = claseDAO.listarClasesCreadas();
            for (Clase clase : clasesDisponibles) {
                int inscritos = claseDAO.contarInscritosPorClase(clase.getIdClase());
                clase.setInscritos(inscritos);
            }
            request.setAttribute("clasesDisponibles", clasesDisponibles);


            // 2️⃣ Clases con docente asignado e inscritos reales
            List<DocenteConClaseDTO> clasesConInscritos = claseDAO.listarClasesConDocenteYInscritos();
            for (DocenteConClaseDTO dto : clasesConInscritos) {
                int inscritos = claseDAO.contarInscritosPorClase(dto.getClaseId());
                dto.setInscritos(inscritos);
            }
            request.setAttribute("clasesConInscritos", clasesConInscritos);

            // 3️⃣ Tablas enviadas por docentes para certificación
            request.setAttribute("tablasEnviadas", asignacionDAO.obtenerTablasEnviadasPendientes());

            // 4️⃣ Auditoría institucional
            List<Map<String, String>> auditoria = auditoriaDAO.listarAuditoria();
            request.setAttribute("auditoria", auditoria);

            // 5️⃣ Registrar acción en auditoría
            Map<String, String> registro = new HashMap<>();
            registro.put("usuario", usuario);
            registro.put("rol", rol);
            registro.put("modulo", "Gestión de clases institucionales");
            registro.put("accion", "Accedió a gestionar clases institucionales");
            registro.put("ip_origen", request.getRemoteAddr());
            auditoriaDAO.registrarAccion(registro);

            // 6️⃣ Forward al JSP principal
            request.getRequestDispatcher("/administrador/gestionarClasesPrincipal.jsp")
                   .forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            if (sesion != null) {
                sesion.setAttribute("mensaje", "❌ Error al cargar clases institucionales: " + e.getMessage());
            }
            // ⚠️ Importante: redirigir al servlet, no al JSP directo
            response.sendRedirect(request.getContextPath() + "/GestionarClasesServlet");
        }
    }
}