/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.mysymphony.proyecto_symphony1.servlet;

/**
 * Servlet para visualizar notas registradas en una tabla guardada o directamente por clase
 * Rol: docente
 * Autor: Camila
 * Trazabilidad: valida sesión, consulta notas y clase asociada, registra acceso en auditoría y bitácora
 */

import com.mysymphony.proyecto_symphony1.dao.NotaDAO;
import com.mysymphony.proyecto_symphony1.dao.ClaseDAO;
import com.mysymphony.proyecto_symphony1.dao.TablaDAO;
import com.mysymphony.proyecto_symphony1.dao.AuditoriaDAO;
import com.mysymphony.proyecto_symphony1.dao.BitacoraDAO;
import com.mysymphony.proyecto_symphony1.modelo.Nota;
import com.mysymphony.proyecto_symphony1.util.Conexion;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import javax.servlet.RequestDispatcher;
import java.io.IOException;
import java.sql.Connection;
import java.util.*;

@WebServlet("/VerNotasPorTablaServlet")
public class VerNotasPorTablaServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession sesion = request.getSession(false);
        String rol = (sesion != null) ? (String) sesion.getAttribute("rolActivo") : null;
        Integer idDocente = (sesion != null) ? (Integer) sesion.getAttribute("idActivo") : null;
        String usuario = (sesion != null) ? (String) sesion.getAttribute("nombreActivo") : "desconocido";

        // 🔐 Validación de sesión y rol
        if (rol == null || !"docente".equalsIgnoreCase(rol) || idDocente == null) {
            sesion.setAttribute("tipoMensaje", "danger");
            sesion.setAttribute("mensaje", "⚠️ Acceso restringido: requiere rol docente.");
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        int tablaId = -1;
        int claseId = -1;

        try {
            String tablaIdParam = request.getParameter("tablaId");
            String claseIdParam = request.getParameter("claseId");

            if (tablaIdParam != null) {
                tablaId = Integer.parseInt(tablaIdParam);
                if (tablaId <= 0) throw new NumberFormatException("ID de tabla inválido");
            }

            if (claseIdParam != null) {
                claseId = Integer.parseInt(claseIdParam);
                if (claseId <= 0) throw new NumberFormatException("ID de clase inválido");
            }

        } catch (NumberFormatException e) {
            sesion.setAttribute("tipoMensaje", "danger");
            sesion.setAttribute("mensaje", "⚠️ Parámetro no válido.");
            response.sendRedirect(request.getContextPath() + "/PanelDocenteServlet");
            return;
        }

        try (Connection conn = Conexion.getConnection()) {
            NotaDAO notaDAO = new NotaDAO(conn);
            ClaseDAO claseDAO = new ClaseDAO(conn);
            TablaDAO tablaDAO = new TablaDAO(conn);
            AuditoriaDAO auditoriaDAO = new AuditoriaDAO(conn);
            BitacoraDAO bitacoraDAO = new BitacoraDAO(conn);

            // 📑 Si viene tablaId, obtener clase asociada
            if (tablaId != -1 && claseId == -1) {
                claseId = tablaDAO.obtenerClasePorTabla(tablaId);
                if (claseId == -1) {
                    sesion.setAttribute("tipoMensaje", "danger");
                    sesion.setAttribute("mensaje", "⚠️ Clase no válida asociada a la tabla.");
                    response.sendRedirect(request.getContextPath() + "/VerTablasDocenteServlet");
                    return;
                }
            }

            // 📑 Obtener notas registradas en la clase
            List<Nota> notas = notaDAO.obtenerNotasPorClase(claseId);

            // 📑 Obtener datos de la clase
            Map<String, String> datosClase = claseDAO.obtenerDatosClase(claseId);

            // 📑 Obtener estado de la tabla guardada (si existe tablaId)
            Map<String, String> estadoTabla = (tablaId != -1) ? tablaDAO.obtenerEstadoTabla(tablaId) : null;

            // 📤 Enviar datos a la vista
            request.setAttribute("notas", notas);
            request.setAttribute("claseId", claseId);
            request.setAttribute("tablaId", tablaId);
            request.setAttribute("datosClase", datosClase);
            request.setAttribute("estadoTabla", estadoTabla);

            // 📝 Auditoría institucional
            Map<String, String> registro = new HashMap<>();
            registro.put("usuario", usuario + " (ID: " + idDocente + ")");
            registro.put("rol", rol);
            registro.put("modulo", "Consulta de notas por tabla");
            registro.put("accion", "Consultó notas de la clase ID " + claseId +
                    (tablaId != -1 ? " (tabla ID " + tablaId + ")" : ""));
            registro.put("ip_origen", request.getRemoteAddr());
            auditoriaDAO.registrarAccion(registro);

            // 📖 Bitácora institucional
            bitacoraDAO.registrarAccion("Docente consultó notas de la clase ID " + claseId +
                            (tablaId != -1 ? " (tabla ID " + tablaId + ")" : ""),
                    usuario, rol, "Consulta de notas por tabla");

        } catch (Exception e) {
            e.printStackTrace();
            sesion.setAttribute("tipoMensaje", "danger");
            sesion.setAttribute("mensaje", "❌ Error al consultar notas: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/error.jsp");
            return;
        }

        RequestDispatcher dispatcher = request.getRequestDispatcher("/docente/verNotasPorTabla.jsp");
        dispatcher.forward(request, response);
    }
}