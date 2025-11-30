/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mysymphony.proyecto_symphony1.servlet;

import com.mysymphony.proyecto_symphony1.dao.BitacoraDAO;
import com.mysymphony.proyecto_symphony1.dao.AuditoriaDAO;
import com.mysymphony.proyecto_symphony1.util.Conexion;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/LimpiarBitacoraServlet")
public class LimpiarBitacoraServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession sesion = request.getSession(false);
        String rol = (sesion != null) ? (String) sesion.getAttribute("rolActivo") : null;
        String usuario = (sesion != null) ? (String) sesion.getAttribute("nombreActivo") : "desconocido";

        // 🔐 Validación de rol
        if (rol == null || !"administrador".equalsIgnoreCase(rol)) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        try (Connection conn = Conexion.getConnection()) {
            BitacoraDAO bitacoraDAO = new BitacoraDAO(conn);
            AuditoriaDAO auditoriaDAO = new AuditoriaDAO(conn);

            boolean exito = bitacoraDAO.limpiarBitacora();

            if (exito) {
                sesion.setAttribute("mensaje", "🗑️ Bitácora limpiada correctamente.");
                sesion.setAttribute("tipoMensaje", "success");

                // 📝 Registrar acción en auditoría
                Map<String, String> registro = new HashMap<>();
                registro.put("usuario", usuario);
                registro.put("rol", rol);
                registro.put("modulo", "Bitácora");
                registro.put("accion", "Limpiar bitácora");
                registro.put("detalle", "Se eliminaron todos los registros de bitácora.");
                registro.put("ip_origen", request.getRemoteAddr());
                auditoriaDAO.registrarAccion(registro);

            } else {
                sesion.setAttribute("mensaje", "❌ Error al limpiar la bitácora.");
                sesion.setAttribute("tipoMensaje", "error");
            }

        } catch (Exception e) {
            e.printStackTrace();
            sesion.setAttribute("mensaje", "❌ Error interno: " + e.getMessage());
            sesion.setAttribute("tipoMensaje", "error");
        }

        // 👉 Redirigir de nuevo a la vista de bitácora
        response.sendRedirect(request.getContextPath() + "/VerBitacoraServlet");
    }
}