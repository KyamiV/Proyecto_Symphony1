/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.mysymphony.proyecto_symphony1.servlet;

/**
 * Servlet para actualizar cupos máximos por instrumento y parámetros técnicos del sistema.
 * Rol: administrador
 * Autor: Camila
 * Trazabilidad: actualiza configuración institucional y registra cada cambio en bitácora y auditoría.
 */

import com.mysymphony.proyecto_symphony1.util.Conexion;
import com.mysymphony.proyecto_symphony1.dao.BitacoraDAO;
import com.mysymphony.proyecto_symphony1.dao.AuditoriaDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Map;

@WebServlet("/ActualizarConfiguracionServlet")
public class ActualizarConfiguracionServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession sesion = request.getSession(false);
        String rol = (sesion != null) ? (String) sesion.getAttribute("rolActivo") : null;
        String usuario = (sesion != null) ? (String) sesion.getAttribute("nombreActivo") : null;

        if (usuario == null || rol == null || !"administrador".equalsIgnoreCase(rol)) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        try (Connection conn = Conexion.getConnection()) {
            conn.setAutoCommit(false); // transacción segura

            BitacoraDAO bitacoraDAO = new BitacoraDAO(conn);
            AuditoriaDAO auditoriaDAO = new AuditoriaDAO(conn);

            // Recorremos todos los parámetros enviados
            Map<String, String[]> parametros = request.getParameterMap();

            for (Map.Entry<String, String[]> entry : parametros.entrySet()) {
                String nombre = entry.getKey();
                String valor = entry.getValue()[0];

                if (nombre.startsWith("cupo_")) {
                    // 🎯 Actualizar cupo máximo por instrumento
                    String instrumento = nombre.substring(5); // quitar "cupo_"
                    int cupo = Integer.parseInt(valor);

                    try (PreparedStatement ps = conn.prepareStatement(
                            "UPDATE instrumentos SET cupo_maximo = ? WHERE nombre = ?")) {
                        ps.setInt(1, cupo);
                        ps.setString(2, instrumento);
                        ps.executeUpdate();
                    }

                    bitacoraDAO.registrarAccion("Actualizó cupo máximo de " + instrumento + " a " + cupo,
                            usuario, rol, "configuracion");
                    auditoriaDAO.registrarAuditoria("Administrador actualizó cupo máximo de " + instrumento,
                            "Configuración", usuario);

                    System.out.println("🔧 Cupo actualizado: " + instrumento + " = " + cupo);

                } else if (nombre.startsWith("parametro_")) {
                    // ⚙️ Actualizar parámetro técnico
                    String clave = nombre.substring(10); // quitar "parametro_"
                    String nuevoValor = valor;

                    try (PreparedStatement ps = conn.prepareStatement(
                            "UPDATE configuracion_sistema SET valor = ? WHERE clave = ?")) {
                        ps.setString(1, nuevoValor);
                        ps.setString(2, clave);
                        ps.executeUpdate();
                    }

                    bitacoraDAO.registrarAccion("Modificó parámetro técnico '" + clave + "' a '" + nuevoValor + "'",
                            usuario, rol, "configuracion");
                    auditoriaDAO.registrarAuditoria("Administrador modificó parámetro técnico '" + clave + "'",
                            "Configuración", usuario);

                    System.out.println("⚙️ Parámetro actualizado: " + clave + " = " + nuevoValor);
                }
            }

            conn.commit(); // confirmar cambios
            sesion.setAttribute("mensaje", "✅ Configuración actualizada correctamente.");

        } catch (Exception e) {
            e.printStackTrace();
            try {
                Connection conn = Conexion.getConnection();
                if (conn != null) conn.rollback();
            } catch (Exception ex) {
                System.out.println("Error en rollback: " + ex.getMessage());
            }
            sesion.setAttribute("error", "❌ Error al actualizar configuración: " + e.getMessage());
        }

        // Redirigir nuevamente al servlet de configuración para recargar datos
        response.sendRedirect(request.getContextPath() + "/ConfiguracionServlet");
    }
}