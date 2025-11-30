/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.mysymphony.proyecto_symphony1.servlet;

import com.mysymphony.proyecto_symphony1.dao.ClaseDAO;
import com.mysymphony.proyecto_symphony1.dao.AsignacionDAO;
import com.mysymphony.proyecto_symphony1.dao.AuditoriaDAO;
import com.mysymphony.proyecto_symphony1.dao.DocenteDAO;
import com.mysymphony.proyecto_symphony1.modelo.Clase;
import com.mysymphony.proyecto_symphony1.modelo.Docente;
import com.mysymphony.proyecto_symphony1.modelo.TablaValidada;   // ✅ Import del modelo de validadas
import com.mysymphony.proyecto_symphony1.util.Conexion;
import com.mysymphony.proyecto_symphony1.dto.DocenteConClaseDTO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.util.*;

/**
 * Servlet maestro para gestión institucional de clases.
 * Rol: Administrador
 * Función: Consultar clases disponibles, docentes asignados, tablas enviadas y auditoría.
 * Autor: Camila – Mejorado por ChatGPT
 */
@WebServlet("/GestionarClasesServlet")
public class GestionarClasesServlet extends HttpServlet {

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
            DocenteDAO docenteDAO = new DocenteDAO(conn);

            // 1️⃣ Clases disponibles para inscripción (solo activas)
            List<Clase> clasesDisponibles = claseDAO.listarClasesCreadas();
            request.setAttribute("clasesDisponibles", clasesDisponibles);

            // 2️⃣ Clases con docente asignado y cantidad de inscritos
            List<DocenteConClaseDTO> clasesConInscritos = claseDAO.listarClasesConDocenteYInscritos();
            request.setAttribute("clasesConInscritos", clasesConInscritos);

            // 3️⃣ Tablas enviadas por docentes para certificación (solo pendientes)
            request.setAttribute("tablasEnviadas", asignacionDAO.obtenerTablasEnviadasPendientes());

            // 4️⃣ Tablas ya validadas (para reportes)
            List<TablaValidada> tablasValidadas = claseDAO.listarTablasValidadas();
            request.setAttribute("tablasValidadas", tablasValidadas);

            // 5️⃣ Auditoría institucional
            List<Map<String, String>> auditoria = auditoriaDAO.listarAuditoria();
            request.setAttribute("auditoria", auditoria);

            // 6️⃣ Lista de docentes para el modal de asignar docente
            List<Docente> docentes = docenteDAO.listarTodos();
            request.setAttribute("docentes", docentes);

            // 7️⃣ Indicadores institucionales
            Map<String, Integer> indicadores = new HashMap<>();
            indicadores.put("clases_activas", clasesDisponibles.size());
            indicadores.put("estudiantes_inscritos", claseDAO.contarEstudiantesInscritos());
            indicadores.put("periodos_pendientes", claseDAO.contarEtapasPendientes());
            indicadores.put("certificados_pendientes", asignacionDAO.contarCertificadosPendientes());
            request.setAttribute("indicadores", indicadores);

            // 8️⃣ Registrar acción en auditoría
            Map<String, String> registro = new HashMap<>();
            registro.put("usuario", usuario);
            registro.put("rol", rol);
            registro.put("modulo", "Gestión de clases institucionales");
            registro.put("accion", "Accedió a gestionar clases institucionales");
            registro.put("ip_origen", request.getRemoteAddr());
            auditoriaDAO.registrarAccion(registro);

            // 9️⃣ Forward al JSP principal
            request.getRequestDispatcher("/administrador/gestionarClasesPrincipal.jsp")
                   .forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            if (sesion != null) {
                sesion.setAttribute("mensaje", "❌ Error al cargar clases institucionales: " + e.getMessage());
            }
            // ⚠️ Evita bucles de redirección
            response.sendRedirect(request.getContextPath() + "/administrador/gestionarClasesPrincipal.jsp");
        }
    }
}