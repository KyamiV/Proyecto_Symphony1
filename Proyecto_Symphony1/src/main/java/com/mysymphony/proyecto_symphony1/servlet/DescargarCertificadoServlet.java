/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.mysymphony.proyecto_symphony1.servlet;

/**
 * Servlet para generar y entregar certificados PDF en tiempo real.
 * Rol: estudiante
 * Autor: Camila
 * Trazabilidad:
 *   - Valida sesión y rol
 *   - Registra acción en bitácora y auditoría institucional
 *   - Genera PDF con GeneradorCertificado y lo envía al navegador
 */

import com.mysymphony.proyecto_symphony1.util.Conexion;
import com.mysymphony.proyecto_symphony1.dao.AuditoriaDAO;
import com.mysymphony.proyecto_symphony1.dao.BitacoraDAO;
import com.mysymphony.proyecto_symphony1.util.GeneradorCertificado;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/DescargarCertificadoServlet")
public class DescargarCertificadoServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 🔐 Validar sesión y rol estudiante
        HttpSession sesion = request.getSession(false);
        if (sesion == null || sesion.getAttribute("rolActivo") == null ||
            !"estudiante".equalsIgnoreCase((String) sesion.getAttribute("rolActivo"))) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        // 📥 Parámetros recibidos
        String instrumento = request.getParameter("instrumento");
        String etapa = request.getParameter("etapa");

        if (instrumento == null || etapa == null) {
            sesion.setAttribute("mensaje", "⚠️ Faltan datos para generar el certificado.");
            response.sendRedirect(request.getContextPath() + "/estudiante/certificados.jsp");
            return;
        }

        // 📝 Registrar acción en bitácora y auditoría
        String nombreEstudiante = (String) sesion.getAttribute("nombreActivo");
        String ip = request.getRemoteAddr();

        try (Connection conn = Conexion.getConnection()) {
            BitacoraDAO bitacoraDAO = new BitacoraDAO(conn);
            bitacoraDAO.registrarAccion("Estudiante descargó certificado de " + instrumento + " - " + etapa,
                    nombreEstudiante, "estudiante", "Certificados");

            Map<String, String> registro = new HashMap<>();
            registro.put("usuario", nombreEstudiante);
            registro.put("rol", "estudiante");
            registro.put("modulo", "Certificados");
            registro.put("accion", "Descargó certificado de " + instrumento + " - " + etapa);
            registro.put("ip_origen", ip);

            AuditoriaDAO auditoriaDAO = new AuditoriaDAO(conn);
            auditoriaDAO.registrarAccion(registro);

            System.out.println("✅ Certificado generado: " + instrumento + " - " + etapa + " por " + nombreEstudiante);

        } catch (Exception e) {
            e.printStackTrace();
            sesion.setAttribute("mensaje", "❌ Error al registrar descarga de certificado.");
            response.sendRedirect(request.getContextPath() + "/estudiante/certificados.jsp");
            return;
        }

        // 📤 Generar y enviar PDF al navegador
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=certificado_symphony.pdf");

        // 📦 Ruta del logo institucional
        String logoPath = getServletContext().getRealPath("/assets/img/logo.png");

        GeneradorCertificado.generarCertificado(
                nombreEstudiante,
                instrumento,
                etapa,
                java.time.LocalDate.now().toString(),
                response.getOutputStream(),
                logoPath
        );
    }
}