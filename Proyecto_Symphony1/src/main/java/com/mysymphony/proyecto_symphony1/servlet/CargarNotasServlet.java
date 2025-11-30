/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.mysymphony.proyecto_symphony1.servlet;

/**
 * Servlet: CargarNotasServlet
 * Rol: Docente
 * Autor: Camila
 * Creado: 27/11/2025
 *
 * Propósito:
 *   - Cargar la vista registrarNotas.jsp con estudiantes y datos de clase.
 *   - Escalar trazabilidad institucional y preparar datos para edición de notas por clase.
 *   - Validar sesión y rol activo.
 *   - Registrar acción en Auditoría y Bitácora institucional.
 */

import com.mysymphony.proyecto_symphony1.dao.NotaDAO;
import com.mysymphony.proyecto_symphony1.dao.ClaseDAO;
import com.mysymphony.proyecto_symphony1.dao.AuditoriaDAO;
import com.mysymphony.proyecto_symphony1.dao.BitacoraDAO;
import com.mysymphony.proyecto_symphony1.modelo.Estudiante;
import com.mysymphony.proyecto_symphony1.modelo.Nota;
import com.mysymphony.proyecto_symphony1.util.Conexion;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import javax.servlet.RequestDispatcher;
import java.io.IOException;
import java.sql.Connection;
import java.util.*;

@WebServlet("/CargarNotasServlet")
public class CargarNotasServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        procesarSolicitud(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // ✅ Delegamos en doGet para evitar error 405 (Method Not Allowed)
        procesarSolicitud(request, response);
    }

    /**
     * Método central que procesa tanto GET como POST.
     * Valida sesión, carga datos de clase y estudiantes, registra trazabilidad y
     * envía la información a la vista registrarNotas.jsp.
     */
    private void procesarSolicitud(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 🔹 1. Validación de sesión y rol
        HttpSession sesion = request.getSession(false);
        String rol = (sesion != null) ? (String) sesion.getAttribute("rolActivo") : null;
        Integer idDocente = (sesion != null) ? (Integer) sesion.getAttribute("idActivo") : null;
        String nombreDocente = (sesion != null) ? (String) sesion.getAttribute("nombreActivo") : "desconocido";

        if (rol == null || !"docente".equalsIgnoreCase(rol) || idDocente == null) {
            request.setAttribute("tipoMensaje", "danger");
            request.setAttribute("mensaje", "❌ Acceso restringido: requiere rol docente.");
            request.getRequestDispatcher("/error.jsp").forward(request, response);
            return;
        }

        // 🔹 2. Validar parámetro claseId
        String claseIdStr = request.getParameter("claseId");

        // Si no viene en request, intentar recuperar de sesión
        if (claseIdStr == null || claseIdStr.isEmpty()) {
            claseIdStr = (sesion != null && sesion.getAttribute("claseId") != null)
                         ? sesion.getAttribute("claseId").toString()
                         : null;
        }

        // Si sigue siendo nulo → error institucional
        if (claseIdStr == null || claseIdStr.isEmpty()) {
            request.setAttribute("tipoMensaje", "warning");
            request.setAttribute("mensaje", "⚠️ Clase no especificada.");
            request.getRequestDispatcher("/error.jsp").forward(request, response);
            return;
        }

        // Convertir a entero
        int claseId;
        try {
            claseId = Integer.parseInt(claseIdStr);
        } catch (NumberFormatException e) {
            request.setAttribute("tipoMensaje", "danger");
            request.setAttribute("mensaje", "⚠️ Clase no válida.");
            request.getRequestDispatcher("/error.jsp").forward(request, response);
            return;
        }

        // Guardar en sesión para futuros redirects
        sesion.setAttribute("claseId", claseId);

        // También guardar en request para el JSP actual
        request.setAttribute("claseId", claseId);

        try (Connection conn = Conexion.getConnection()) {
            // 🔹 3. DAOs para obtener datos
            NotaDAO notaDAO = new NotaDAO(conn);
            ClaseDAO claseDAO = new ClaseDAO(conn);

            // Estudiantes inscritos en la clase (modelo Estudiante)
            List<Estudiante> estudiantes = notaDAO.obtenerEstudiantesPorClase(claseId);
            // Notas ya registradas en la clase
            List<Nota> notas = notaDAO.obtenerNotasPorClase(claseId);

            // Datos de la clase
            Map<String, String> datosClase = claseDAO.obtenerDatosClase(claseId);
            String nombreClase = datosClase.getOrDefault("nombre", "Sin nombre");
            String aula = datosClase.getOrDefault("aula", "Sin aula");
            String horario = (datosClase.get("dia") != null ? datosClase.get("dia") : "") + " " +
                             (datosClase.get("inicio") != null ? datosClase.get("inicio") : "") + " - " +
                             (datosClase.get("fin") != null ? datosClase.get("fin") : "");

            // 🔹 4. Mensajes institucionales (se trasladan de sesión a request)
            Object mensaje = sesion.getAttribute("mensaje");
            Object tipoMensaje = sesion.getAttribute("tipoMensaje");
            if (mensaje != null) {
                request.setAttribute("mensaje", mensaje);
                request.setAttribute("tipoMensaje", tipoMensaje != null ? tipoMensaje : "info");
                sesion.removeAttribute("mensaje");
                sesion.removeAttribute("tipoMensaje");
            }

            // 🔹 5. Registro en Bitácora institucional
            new BitacoraDAO(conn).registrarAccion(
                    "Docente accedió a registrar notas en clase " + nombreClase,
                    nombreDocente, rol, "Notas por clase");

            // 🔹 6. Registro en Auditoría institucional
            Map<String, String> registro = new HashMap<>();
            registro.put("usuario", String.valueOf(idDocente));
            registro.put("rol", rol);
            registro.put("modulo", "Notas por clase");
            registro.put("accion", "Accedió a registrar notas en clase " + nombreClase);
            registro.put("ip_origen", request.getRemoteAddr());
            new AuditoriaDAO(conn).registrarAccion(registro);

            // 🔹 7. Enviar datos a la vista JSP
            request.setAttribute("estudiantes", estudiantes);
            request.setAttribute("notas", notas);
            request.setAttribute("nombreClase", nombreClase);
            request.setAttribute("aula", aula);
            request.setAttribute("horario", horario);

        } catch (Exception e) {
            // Manejo de errores
            e.printStackTrace();
            request.setAttribute("tipoMensaje", "danger");
            request.setAttribute("mensaje", "❌ Error al cargar datos de clase: " + e.getMessage());
            request.getRequestDispatcher("/error.jsp").forward(request, response);
            return;
        }

        // 🔹 8. Forward a la vista registrarNotas.jsp
        RequestDispatcher dispatcher = request.getRequestDispatcher("/docente/registrarNotas.jsp");
        dispatcher.forward(request, response);
    }
}