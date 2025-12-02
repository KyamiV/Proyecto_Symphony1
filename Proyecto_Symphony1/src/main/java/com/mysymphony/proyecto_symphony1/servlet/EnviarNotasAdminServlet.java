/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.mysymphony.proyecto_symphony1.servlet;

/**
 * Servlet para enviar una tabla institucional de notas al administrador con trazabilidad.
 * Rol: docente
 * Autor: Camila
 * Trazabilidad:
 *   - Valida sesión
 *   - Verifica notas y estado
 *   - Marca como enviada
 *   - Registra acción en bitácora y auditoría institucional
 */

import com.mysymphony.proyecto_symphony1.util.Conexion;
import com.mysymphony.proyecto_symphony1.dao.TablasNotasDAO;
import com.mysymphony.proyecto_symphony1.dao.NotaDAO;
import com.mysymphony.proyecto_symphony1.dao.BitacoraDAO;
import com.mysymphony.proyecto_symphony1.dao.AuditoriaDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/EnviarNotasAdminServlet")
public class EnviarNotasAdminServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession sesion = request.getSession(false);
        if (sesion == null || sesion.getAttribute("rolActivo") == null ||
            !"docente".equalsIgnoreCase((String) sesion.getAttribute("rolActivo"))) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        // Validar que el docente esté en sesión
        Integer idDocente = (Integer) sesion.getAttribute("idActivo");
        String nombreDocente = (String) sesion.getAttribute("nombreActivo");
        String rol = (String) sesion.getAttribute("rolActivo");

        if (idDocente == null) {
            sesion.setAttribute("mensaje", "❌ No se encontró el docente en sesión.");
            response.sendRedirect(request.getContextPath() + "/VerTablasDocenteServlet");
            return;
        }

        // Validar parámetro tablaId
        String tablaIdStr = request.getParameter("tablaId");
        if (tablaIdStr == null || tablaIdStr.trim().isEmpty()) {
            sesion.setAttribute("mensaje", "❌ No se especificó la tabla.");
            response.sendRedirect(request.getContextPath() + "/VerTablasDocenteServlet");
            return;
        }

        int tablaId;
        try {
            tablaId = Integer.parseInt(tablaIdStr);
        } catch (NumberFormatException e) {
            sesion.setAttribute("mensaje", "❌ El identificador de la tabla no es válido.");
            response.sendRedirect(request.getContextPath() + "/VerTablasDocenteServlet");
            return;
        }

        try (Connection conn = Conexion.getConnection()) {
            TablasNotasDAO tablasDAO = new TablasNotasDAO(conn);
            NotaDAO notaDAO = new NotaDAO(conn);

            // Contar notas asociadas a la tabla
            int cantidadNotas = notaDAO.contarNotasPorTabla(tablaId);

            if (cantidadNotas == 0) {
                sesion.setAttribute("mensaje", "⚠️ La tabla no tiene notas registradas.");
            } else {
                boolean enviado = tablasDAO.marcarTablaComoEnviada(tablaId, idDocente);
                if (enviado) {
                    sesion.setAttribute("mensaje", "✅ La tabla fue enviada correctamente al administrador con "
                            + cantidadNotas + " notas registradas.");

                    // 📖 Bitácora institucional
                    new BitacoraDAO(conn).registrarAccion(
                        "Docente envió tabla " + tablaId + " al administrador con " + cantidadNotas + " notas",
                        nombreDocente, rol, "Envío de tablas"
                    );

                    // 🛡️ Auditoría institucional
                    Map<String, String> registro = new HashMap<>();
                    registro.put("usuario", nombreDocente + " (ID: " + idDocente + ")");
                    registro.put("rol", rol);
                    registro.put("modulo", "Envío de tablas");
                    registro.put("accion", "Envió tabla " + tablaId + " al administrador con " + cantidadNotas + " notas");
                    registro.put("ip_origen", request.getRemoteAddr());
                    new AuditoriaDAO(conn).registrarAccion(registro);

                } else {
                    sesion.setAttribute("mensaje", "⚠️ No se pudo enviar la tabla.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            sesion.setAttribute("mensaje", "❌ Error al enviar la tabla: " + e.getMessage());
        }

        response.sendRedirect(request.getContextPath() + "/VerTablasDocenteServlet");
    }
}