/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.mysymphony.proyecto_symphony1.servlet;

import com.mysymphony.proyecto_symphony1.dao.NotaDAO;
import com.mysymphony.proyecto_symphony1.dao.AuditoriaDAO;
import com.mysymphony.proyecto_symphony1.dao.BitacoraDAO;
import com.mysymphony.proyecto_symphony1.util.Conexion;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import javax.servlet.RequestDispatcher;
import java.io.IOException;
import java.sql.Connection;
import java.time.LocalDate;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

/**
 * Servlet encargado de actualizar notas de estudiantes.
 * Rol: docente
 * Autor: Camila
 * Trazabilidad: registra cambios en bitácora y auditoría institucional.
 */
@WebServlet("/ActualizarNotaServlet")
public class ActualizarNotaServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 🔐 Validar sesión y rol docente
        HttpSession sesion = request.getSession();
        String rol = (String) sesion.getAttribute("rolActivo");
        int docenteId = (sesion != null) ? (Integer) sesion.getAttribute("idDocenteActivo") : 0;
        String docenteNombre = (sesion != null) ? (String) sesion.getAttribute("nombreActivo") : "desconocido";

        if (rol == null || !"docente".equalsIgnoreCase(rol) || docenteId == 0) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        // 📥 Obtener parámetros del formulario
        String notaIdStr = request.getParameter("notaId");       // clave primaria
        String estudianteIdStr = request.getParameter("idEstudiante");
        String instrumento = request.getParameter("instrumento");
        String etapa = request.getParameter("etapa");
        String competencia = request.getParameter("competencia");
        String notaStr = request.getParameter("nota");
        String observacion = request.getParameter("observacion"); // singular
        String fechaStr = request.getParameter("fecha");

        String mensaje = "❌ Faltan datos para actualizar la nota.";

        try (Connection conn = Conexion.getConnection()) {
            NotaDAO dao = new NotaDAO(conn);

            if (notaIdStr != null && !notaIdStr.isEmpty() &&
                estudianteIdStr != null && !estudianteIdStr.isEmpty() &&
                instrumento != null && !instrumento.isEmpty() &&
                etapa != null && !etapa.isEmpty() &&
                competencia != null && !competencia.isEmpty() &&
                notaStr != null && !notaStr.isEmpty() &&
                fechaStr != null && !fechaStr.isEmpty()) {

                try {
                    int notaId = Integer.parseInt(notaIdStr);
                    int estudianteId = Integer.parseInt(estudianteIdStr);
                    double notaValor = Double.parseDouble(notaStr);
                    LocalDate fechaNota = LocalDate.parse(fechaStr);

                    boolean actualizado = dao.actualizarNotaPorId(
                            notaId, competencia, notaValor, observacion, fechaNota
                    );

                    mensaje = actualizado
                            ? "✅ Nota actualizada correctamente."
                            : "❌ No se pudo actualizar la nota.";

                    if (actualizado) {
                        // 📝 Bitácora institucional
                        BitacoraDAO bitacoraDAO = new BitacoraDAO(conn);
                        bitacoraDAO.registrarAccion(
                                "Nota actualizada de estudianteId=" + estudianteId +
                                " en " + instrumento + " (" + etapa + ")",
                                docenteNombre, rol, "Gestión de Notas"
                        );

                        // 🛡️ Auditoría institucional
                        AuditoriaDAO auditoriaDAO = new AuditoriaDAO(conn);
                        Map<String, String> registro = new HashMap<>();
                        registro.put("usuario", String.valueOf(docenteId));
                        registro.put("rol", rol);
                        registro.put("modulo", "Gestión de Notas");
                        registro.put("accion", "Docente actualizó nota id=" + notaId +
                                " de estudianteId=" + estudianteId);
                        registro.put("ip_origen", request.getRemoteAddr());
                        auditoriaDAO.registrarAccion(registro);

                        System.out.println("✅ Nota actualizada: notaId=" + notaId +
                                ", estudianteId=" + estudianteId +
                                ", instrumento=" + instrumento +
                                ", etapa=" + etapa +
                                ", competencia=" + competencia +
                                ", nota=" + notaValor +
                                ", fecha=" + fechaNota);
                    }

                } catch (NumberFormatException e) {
                    mensaje = "❌ Error al convertir la nota o el ID.";
                    e.printStackTrace();
                }
            }

            // 📊 Refrescar listado de notas del docente
            List<Map<String, String>> notas = dao.obtenerNotasPorDocenteId(docenteId);
            request.setAttribute("mensaje", mensaje);
            request.setAttribute("notas", notas);

        } catch (Exception e) {
            mensaje = "❌ Error al conectar con la base de datos.";
            System.err.println("❌ Error en ActualizarNotaServlet: " + e.getMessage());
            e.printStackTrace();
        }

        RequestDispatcher dispatcher = request.getRequestDispatcher("/docente/verNotasDocente.jsp");
        dispatcher.forward(request, response);
    }
}