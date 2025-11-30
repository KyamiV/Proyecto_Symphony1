/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mysymphony.proyecto_symphony1.servlet;

/**
 * Servlet: VerClasesDocenteServlet
 * Rol: Docente
 * Autor: Camila
 * Creado: 24/11/2025
 *
 * Propósito:
 *   - Mostrar las clases asignadas al docente activo.
 *   - Validar sesión y rol activo.
 *   - Consultar clases desde la BD con datos de instrumento, etapa, grupo y horario.
 *   - Registrar acción en Auditoría y Bitácora institucional.
 *
 * Trazabilidad:
 *   - Recibe idDocente desde sesión.
 *   - Consulta tabla clases vinculada al docente vía ClaseDAO.
 *   - Envía atributo 'clases' al JSP verClasesDocente.jsp.
 *   - Registra acceso en AuditoriaDAO y BitacoraDAO.
 */

import com.mysymphony.proyecto_symphony1.dao.AuditoriaDAO;
import com.mysymphony.proyecto_symphony1.dao.BitacoraDAO;
import com.mysymphony.proyecto_symphony1.dao.ClaseDAO;
import com.mysymphony.proyecto_symphony1.dto.DocenteConClaseDTO;
import com.mysymphony.proyecto_symphony1.util.Conexion;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.util.*;

@WebServlet("/VerClasesDocenteServlet")
public class VerClasesDocenteServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        procesarSolicitud(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        procesarSolicitud(request, response);
    }

    private void procesarSolicitud(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession sesion = request.getSession(false);
        String rol = (sesion != null) ? (String) sesion.getAttribute("rolActivo") : null;
        String nombre = (sesion != null) ? (String) sesion.getAttribute("nombreActivo") : "desconocido";
        Integer idDocente = (sesion != null) ? (Integer) sesion.getAttribute("idActivo") : null;

        // 🔹 Validación de sesión y rol
        if (rol == null || !"docente".equalsIgnoreCase(rol) || idDocente == null) {
            request.setAttribute("tipoMensaje", "danger");
            request.setAttribute("mensaje", "❌ Acceso restringido: requiere rol docente.");
            request.getRequestDispatcher("/error.jsp").forward(request, response);
            return;
        }

        List<DocenteConClaseDTO> clases = new ArrayList<>();

        try (Connection conn = Conexion.getConnection()) {
            ClaseDAO dao = new ClaseDAO(conn);
            // ⚠️ Ajusta tu ClaseDAO para que devuelva List<DocenteConClaseDTO>
            clases = dao.listarClasesPorDocente(idDocente);

            // 🛡️ Auditoría institucional
            Map<String, String> registro = new HashMap<>();
            registro.put("usuario", String.valueOf(idDocente));
            registro.put("rol", rol);
            registro.put("modulo", "Clases");
            registro.put("accion", "Visualizó sus clases asignadas");
            registro.put("id_referencia", String.valueOf(idDocente));
            registro.put("ip_origen", request.getRemoteAddr());
            new AuditoriaDAO(conn).registrarAccion(registro);

            // 📖 Bitácora institucional
            BitacoraDAO bitacoraDAO = new BitacoraDAO(conn);
            bitacoraDAO.registrarAccion(
                "Docente consultó sus clases asignadas",
                nombre, rol, "Clases"
            );

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("tipoMensaje", "danger");
            request.setAttribute("mensaje", "❌ Error al consultar tus clases: " + e.getMessage());
            request.getRequestDispatcher("/error.jsp").forward(request, response);
            return;
        }

        // 📤 Enviar datos al JSP
        request.setAttribute("clases", clases);
        request.getRequestDispatcher("/docente/verClasesDocente.jsp").forward(request, response);
    }
}