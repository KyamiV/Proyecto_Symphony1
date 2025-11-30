/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mysymphony.proyecto_symphony1.servlet;

/**
 * Servlet para gestionar la configuración del docente en SymphonySIAS
 * Rol: docente
 * Autor: Camila
 * Funcionalidad:
 *   - Valida sesión y rol
 *   - Recibe preferencias desde configuracion.jsp
 *   - Guarda en BD (tabla configuracion_docente) usando ConfiguracionDocenteDAO
 *   - Actualiza sesión
 *   - Registra acción en bitácora y auditoría institucional
 */

import com.mysymphony.proyecto_symphony1.dao.AuditoriaDAO;
import com.mysymphony.proyecto_symphony1.dao.BitacoraDAO;
import com.mysymphony.proyecto_symphony1.dao.ConfiguracionDocenteDAO;
import com.mysymphony.proyecto_symphony1.util.Conexion;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/ConfiguracionDocenteServlet")
public class ConfiguracionDocenteServlet extends HttpServlet {

    // =========================
    // GET → Mostrar configuración
    // =========================
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession sesion = request.getSession(false);
        if (sesion == null || sesion.getAttribute("rolActivo") == null ||
                !"docente".equalsIgnoreCase((String) sesion.getAttribute("rolActivo"))) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        Integer idDocente = (Integer) sesion.getAttribute("idActivo");

        try (Connection conn = Conexion.getConnection()) {
            ConfiguracionDocenteDAO configDAO = new ConfiguracionDocenteDAO(conn);

            if (configDAO.existeConfiguracion(idDocente)) {
                Map<String, Object> config = configDAO.obtenerConfiguracion(idDocente);
                sesion.setAttribute("temaOscuro", config.get("temaOscuro"));
                sesion.setAttribute("mostrarIndicadores", config.get("mostrarIndicadores"));
                sesion.setAttribute("idioma", config.get("idioma"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            sesion.setAttribute("mensaje", "❌ Error al cargar configuración: " + e.getMessage());
        }

        // Redirigir al JSP de configuración
        response.sendRedirect(request.getContextPath() + "/docente/configuracion.jsp");
    }

    // =========================
    // POST → Guardar configuración
    // =========================
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession sesion = request.getSession(false);
        if (sesion == null || sesion.getAttribute("rolActivo") == null ||
                !"docente".equalsIgnoreCase((String) sesion.getAttribute("rolActivo"))) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        Integer idDocente = (Integer) sesion.getAttribute("idActivo");
        String usuario = (String) sesion.getAttribute("nombreActivo");

        // 📥 Recibir parámetros desde configuracion.jsp
        boolean temaOscuro = request.getParameter("temaOscuro") != null;
        boolean mostrarIndicadores = request.getParameter("mostrarIndicadores") != null;
        String idioma = request.getParameter("idioma");

        try (Connection conn = Conexion.getConnection()) {
            ConfiguracionDocenteDAO configDAO = new ConfiguracionDocenteDAO(conn);

            // 🔎 Insertar o actualizar según exista configuración
            if (configDAO.existeConfiguracion(idDocente)) {
                configDAO.actualizarConfiguracion(idDocente, temaOscuro, mostrarIndicadores, idioma);
            } else {
                configDAO.insertarConfiguracion(idDocente, temaOscuro, mostrarIndicadores, idioma);
            }

            // 💾 Guardar también en sesión
            sesion.setAttribute("temaOscuro", temaOscuro);
            sesion.setAttribute("mostrarIndicadores", mostrarIndicadores);
            sesion.setAttribute("idioma", idioma);

            // 📝 Registrar acción en bitácora y auditoría institucional
            BitacoraDAO bitacoraDAO = new BitacoraDAO(conn);
            bitacoraDAO.registrarAccion("Docente actualizó configuración",
                    usuario, "docente", "Configuración");

            Map<String, String> registro = new HashMap<>();
            registro.put("usuario", usuario + " (ID: " + idDocente + ")");
            registro.put("rol", "docente");
            registro.put("modulo", "Configuración");
            registro.put("accion", "Actualizó preferencias: temaOscuro=" + temaOscuro +
                    ", mostrarIndicadores=" + mostrarIndicadores +
                    ", idioma=" + idioma);
            registro.put("ip_origen", request.getRemoteAddr());

            new AuditoriaDAO(conn).registrarAccion(registro);

        } catch (Exception e) {
            e.printStackTrace();
            sesion.setAttribute("mensaje", "❌ Error al guardar configuración: " + e.getMessage());
        }

        // 📢 Mensaje institucional
        sesion.setAttribute("mensaje", "✅ Configuración guardada correctamente.");

        // 🔁 Redirigir de nuevo a la vista de configuración
        response.sendRedirect(request.getContextPath() + "/docente/configuracion.jsp");
    }
}