/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.mysymphony.proyecto_symphony1.servlet;

import com.mysymphony.proyecto_symphony1.util.Conexion;
import com.mysymphony.proyecto_symphony1.dao.CertificadoDAO;
import com.mysymphony.proyecto_symphony1.dao.BitacoraDAO;
import com.mysymphony.proyecto_symphony1.dao.AuditoriaDAO;
import com.mysymphony.proyecto_symphony1.dao.TablaDAO;
import com.mysymphony.proyecto_symphony1.dto.CertificadoDTO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Servlet para emitir certificados institucionales
 * Rol: Administrador
 * Autor: Camila
 * Flujo:
 *   - Recibe id_tabla desde JSP
 *   - Obtiene datos de clase/docente asociados
 *   - Inserta certificados en certificados_estudiante (uno por cada estudiante inscrito)
 *   - Registra acción en bitácora y auditoría
 */
@WebServlet("/CertificarServlet")
public class CertificarServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response); // permitir GET
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession sesion = request.getSession(false);
        String rol = (sesion != null) ? (String) sesion.getAttribute("rolActivo") : null;
        String usuario = (sesion != null) ? (String) sesion.getAttribute("nombreActivo") : "desconocido";

        // 🔐 Validar rol administrador
        if (rol == null || !"administrador".equalsIgnoreCase(rol)) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        // 📥 Validar parámetro
        String idTablaParam = request.getParameter("tablaId");
        if (idTablaParam == null || idTablaParam.trim().isEmpty()) {
            sesion.setAttribute("mensaje", "❌ No se especificó la tabla.");
            response.sendRedirect(request.getContextPath() + "/VerTablasValidadasServlet");
            return;
        }

        Connection conn = null;
        try {
            conn = Conexion.getConnection();
            conn.setAutoCommit(false);

            int idTabla = Integer.parseInt(idTablaParam);

            TablaDAO tablaDAO = new TablaDAO(conn);

            // 🚫 Verificar si ya está certificada
            if (tablaDAO.yaCertificada(idTabla)) {
                sesion.setAttribute("mensaje", "⚠️ Esta tabla ya fue certificada previamente.");
                response.sendRedirect(request.getContextPath() + "/VerTablasValidadasServlet");
                return;
            }

            // 🔎 Obtener datos reales de la tabla/clase
            Map<String,String> datos = tablaDAO.obtenerDatosTabla(idTabla);
            int idClase = Integer.parseInt(datos.get("id_clase"));
            int idDocente = Integer.parseInt(datos.get("id_docente"));
            String instrumento = datos.get("instrumento");
            String etapa = datos.get("etapa");

            // 🔎 Obtener estudiantes inscritos en la tabla
            List<Integer> estudiantes = tablaDAO.obtenerEstudiantesPorTabla(idTabla);

            CertificadoDAO certDAO = new CertificadoDAO(conn);
            boolean ok = true;

            for (int idEstudiante : estudiantes) {
                CertificadoDTO cert = new CertificadoDTO();
                cert.setIdTabla(idTabla);
                cert.setIdClase(idClase);
                cert.setIdDocente(idDocente);
                cert.setIdEstudiante(idEstudiante); // 👈 obligatorio
                cert.setInstrumento(instrumento);
                cert.setEtapa(etapa);
                cert.setEstado("Emitido");
                cert.setUsuarioAdmin(usuario);

                // 🔹 Construir ruta del certificado PDF
                String rutaCertificado = "certificados/certificado_" + idEstudiante + "_" + idClase + ".pdf";
                cert.setUrlCertificado(rutaCertificado);

                if (!certDAO.emitirCertificado(cert)) {
                    ok = false;
                    break;
                }
            }

            if (ok) {
                // Bitácora
                BitacoraDAO bitacoraDAO = new BitacoraDAO(conn);
                bitacoraDAO.registrarAccion("Administrador emitió certificados de tabla ID " + idTabla,
                        usuario, rol, "Certificación");

                // Auditoría
                Map<String, String> registro = new HashMap<>();
                registro.put("usuario", usuario);
                registro.put("rol", rol);
                registro.put("modulo", "Certificación");
                registro.put("accion", "Emitió certificados de tabla ID " + idTabla);
                new AuditoriaDAO(conn).registrarAccion(registro);

                conn.commit();
                sesion.setAttribute("mensaje", "✅ Certificados emitidos correctamente.");
            } else {
                sesion.setAttribute("mensaje", "❌ No se pudieron emitir todos los certificados.");
                conn.rollback();
            }

        } catch (Exception e) {
            e.printStackTrace();
            sesion.setAttribute("mensaje", "❌ Error al certificar: " + e.getMessage());
            try {
                if (conn != null) conn.rollback();
            } catch (Exception ex) {
                System.err.println("⚠️ Error en rollback: " + ex.getMessage());
            }
        } finally {
            try {
                if (conn != null) conn.close();
            } catch (Exception ignore) {}
        }

        response.sendRedirect(request.getContextPath() + "/VerTablasValidadasServlet");
    }
}