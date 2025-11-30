/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.mysymphony.proyecto_symphony1.servlet;

import com.mysymphony.proyecto_symphony1.dao.AuditoriaDAO;
import com.mysymphony.proyecto_symphony1.dao.BitacoraDAO;
import com.mysymphony.proyecto_symphony1.util.Conexion;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.*;
import java.util.*;

@WebServlet("/VerMensajesEstudianteServlet")
public class VerMensajesEstudianteServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Validar sesión activa y rol estudiante
        HttpSession sesion = request.getSession(false);
        String rol = (sesion != null) ? (String) sesion.getAttribute("rolActivo") : null;
        String nombre = (sesion != null) ? (String) sesion.getAttribute("nombreActivo") : "desconocido";
        Integer idEstudiante = (sesion != null) ? (Integer) sesion.getAttribute("idActivo") : null;

        if (rol == null || !"estudiante".equalsIgnoreCase(rol) || idEstudiante == null) {
            if (sesion != null) {
                sesion.setAttribute("mensaje", "⚠️ Acceso restringido: requiere rol estudiante.");
            }
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        List<Map<String, String>> mensajes = new ArrayList<>();

        // Consulta SQL para obtener mensajes dirigidos al estudiante
        String sql = "SELECT remitente, tipo, contenido, fecha_envio " +
                     "FROM mensajes WHERE id_destinatario = ? ORDER BY fecha_envio DESC";

        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idEstudiante);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Map<String, String> fila = new HashMap<>();
                    fila.put("remitente", rs.getString("remitente"));
                    fila.put("tipo", rs.getString("tipo"));
                    fila.put("contenido", rs.getString("contenido"));
                    fila.put("fecha", rs.getString("fecha_envio"));
                    mensajes.add(fila);
                }
            }

            // 📝 Auditoría institucional
            Map<String, String> registro = new HashMap<>();
            registro.put("usuario", nombre);
            registro.put("rol", rol);
            registro.put("modulo", "Mensajes");
            registro.put("accion", "Visualizó sus mensajes institucionales");
            registro.put("referencia_id", String.valueOf(idEstudiante));
            registro.put("ip_origen", request.getRemoteAddr());
            new AuditoriaDAO(conn).registrarAccion(registro);

            // 📖 Bitácora institucional
            BitacoraDAO bitacoraDAO = new BitacoraDAO(conn);
            bitacoraDAO.registrarAccion("Estudiante consultó sus mensajes institucionales",
                    nombre, rol, "Mensajes");

        } catch (SQLException e) {
            System.err.println("❌ Error al consultar mensajes: " + e.getMessage());
            request.setAttribute("mensaje", "❌ Error al consultar tus mensajes.");
        }

        // Enviar datos a la vista
        request.setAttribute("mensajes", mensajes);
        request.getRequestDispatcher("/estudiante/verMensajes.jsp").forward(request, response);
    }
}