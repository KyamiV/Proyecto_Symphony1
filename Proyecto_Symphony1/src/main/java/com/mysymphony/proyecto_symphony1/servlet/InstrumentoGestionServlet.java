/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.mysymphony.proyecto_symphony1.servlet;

import com.mysymphony.proyecto_symphony1.dao.BitacoraDAO;
import com.mysymphony.proyecto_symphony1.dao.InstrumentoDAO;
import com.mysymphony.proyecto_symphony1.dao.AuditoriaDAO;
import com.mysymphony.proyecto_symphony1.util.Conexion;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.util.*;

@WebServlet("/InstrumentoGestionServlet")
public class InstrumentoGestionServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession sesion = request.getSession(false);
        String rol = (sesion != null) ? (String) sesion.getAttribute("rolActivo") : null;
        String usuario = (sesion != null) ? (String) sesion.getAttribute("nombreActivo") : null;

        if (usuario == null || rol == null || !"administrador".equalsIgnoreCase(rol)) {
            if (sesion != null) {
                sesion.setAttribute("mensaje", "⚠️ Acceso restringido: requiere rol administrador.");
            }
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        try (Connection conn = Conexion.getConnection()) {
            InstrumentoDAO instrumentoDAO = new InstrumentoDAO(conn);
            BitacoraDAO bitacoraDAO = new BitacoraDAO(conn);
            AuditoriaDAO auditoriaDAO = new AuditoriaDAO(conn);

            // Agregar nuevo instrumento
            String nuevoInstrumento = request.getParameter("nuevoInstrumento");
            String cupoStr = request.getParameter("cupoMaximo");

            if (nuevoInstrumento != null && cupoStr != null) {
                try {
                    int cupo = Integer.parseInt(cupoStr);
                    instrumentoDAO.insertarInstrumento(nuevoInstrumento.trim(), cupo);

                    // Bitácora
                    bitacoraDAO.registrarAccion("Agregó instrumento: " + nuevoInstrumento + " con cupo " + cupo,
                            usuario, rol, "configuracion");

                    // Auditoría
                    Map<String, String> registro = new HashMap<>();
                    registro.put("usuario", usuario);
                    registro.put("rol", rol);
                    registro.put("modulo", "Instrumentos");
                    registro.put("accion", "Agregó instrumento: " + nuevoInstrumento + " con cupo " + cupo);
                    registro.put("ip_origen", request.getRemoteAddr());
                    auditoriaDAO.registrarAccion(registro);

                } catch (NumberFormatException e) {
                    request.setAttribute("error", "❌ Cupo inválido: " + cupoStr);
                }
            }

            // Actualizar cupos existentes
            Map<String, String[]> parametros = request.getParameterMap();
            for (String key : parametros.keySet()) {
                if (key.startsWith("nuevoCupo_")) {
                    String nombreInstrumento = key.replace("nuevoCupo_", "").trim();
                    String[] valores = parametros.get(key);
                    if (valores != null && valores.length > 0) {
                        try {
                            int nuevoCupo = Integer.parseInt(valores[0]);
                            instrumentoDAO.actualizarCupoInstrumento(nombreInstrumento, nuevoCupo);

                            // Bitácora
                            bitacoraDAO.registrarAccion("Actualizó cupo de " + nombreInstrumento + " a " + nuevoCupo,
                                    usuario, rol, "configuracion");

                            // Auditoría
                            Map<String, String> registro = new HashMap<>();
                            registro.put("usuario", usuario);
                            registro.put("rol", rol);
                            registro.put("modulo", "Instrumentos");
                            registro.put("accion", "Actualizó cupo de " + nombreInstrumento + " a " + nuevoCupo);
                            registro.put("ip_origen", request.getRemoteAddr());
                            auditoriaDAO.registrarAccion(registro);

                        } catch (NumberFormatException e) {
                            request.setAttribute("error", "❌ Cupo inválido para " + nombreInstrumento);
                        }
                    }
                }
            }

        } catch (Exception e) {
            request.setAttribute("error", "❌ Error al gestionar instrumentos: " + e.getMessage());
            e.printStackTrace();
        }

        response.sendRedirect(request.getContextPath() + "/ConfiguracionServlet");
    }
}