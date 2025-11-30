/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.mysymphony.proyecto_symphony1.servlet;

/**
 * Servlet para cargar el panel del rol administrador
 * Valida sesión, rol y registra acceso en auditoría institucional
 * Incluye métricas institucionales para el dashboard
 * Autor: Camila
 */

import com.mysymphony.proyecto_symphony1.dao.AuditoriaDAO;
import com.mysymphony.proyecto_symphony1.dao.BitacoraDAO;
import com.mysymphony.proyecto_symphony1.dao.UsuarioDAO;
import com.mysymphony.proyecto_symphony1.dao.AsignacionDAO;
import com.mysymphony.proyecto_symphony1.dao.NotaDAO;
import com.mysymphony.proyecto_symphony1.util.Conexion;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/PanelAdministradorServlet")
public class PanelAdministradorServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession sesion = request.getSession(false);
        String rol = (sesion != null) ? (String) sesion.getAttribute("rolActivo") : null;
        Integer idAdministrador = (sesion != null) ? (Integer) sesion.getAttribute("idActivo") : null;
        String nombreAdministrador = (sesion != null) ? (String) sesion.getAttribute("nombreActivo") : null;

        // 🔐 Validación de sesión y rol
        if (rol == null || !"administrador".equalsIgnoreCase(rol) || idAdministrador == null) {
            if (sesion != null) {
                sesion.setAttribute("mensaje", "⚠️ Acceso restringido: requiere rol administrador.");
            }
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        try (Connection conn = Conexion.getConnection()) {
            // 🧩 DAOs con conexión
            UsuarioDAO usuarioDAO = new UsuarioDAO(conn);
            AsignacionDAO asignacionDAO = new AsignacionDAO(conn);
            NotaDAO notaDAO = new NotaDAO(conn);

            // 📊 Métricas institucionales
            request.setAttribute("totalUsuarios", usuarioDAO.contarTodos());
            request.setAttribute("totalAsignaciones", asignacionDAO.contarTodasAsignadas());
            request.setAttribute("totalNotas", notaDAO.contarNotasDeClasesAsignadas());
            request.setAttribute("totalDocentesActivos", usuarioDAO.contarDocentesActivos());

            // 📝 Registro en bitácora
            new BitacoraDAO(conn).registrarAccion(
                    "Administrador accedió al panel institucional",
                    nombreAdministrador, rol, "Panel administrador");

            // 🛡️ Registro en auditoría
            new AuditoriaDAO(conn).registrarAccion(crearRegistro(
                    nombreAdministrador, idAdministrador, rol,
                    "Accedió al Panel administrador", request.getRemoteAddr()));

        } catch (Exception e) {
            e.printStackTrace();
            if (sesion != null) {
                sesion.setAttribute("mensaje", "❌ Error al cargar el panel administrador: " + e.getMessage());
            }

            try (Connection conn = Conexion.getConnection()) {
                new AuditoriaDAO(conn).registrarAccion(crearRegistro(
                        nombreAdministrador, idAdministrador, rol,
                        "Error al cargar el Panel administrador", request.getRemoteAddr()));
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            response.sendRedirect(request.getContextPath() + "/error.jsp");
            return;
        }

        // 📤 Redirigir a vista del panel
        request.getRequestDispatcher("/administrador/panelAdministrador.jsp").forward(request, response);
    }

    private Map<String, String> crearRegistro(String usuario, Integer id, String rol,
                                              String accion, String ip) {
        Map<String, String> registro = new HashMap<>();
        registro.put("usuario", usuario + " (ID: " + id + ")");
        registro.put("rol", rol);
        registro.put("accion", accion);
        registro.put("modulo", "Panel administrador");
        registro.put("referencia_id", null);
        registro.put("tabla_id", null);
        registro.put("ip_origen", ip);
        return registro;
    }
}