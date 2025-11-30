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
import java.util.*;

@WebServlet("/RegistrarAccionAdminServlet")
public class RegistrarAccionAdminServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        System.out.println("📥 Ingreso al servlet RegistrarAccionAdminServlet");

        HttpSession sesion = request.getSession(false);
        String rol = (sesion != null) ? (String) sesion.getAttribute("rolActivo") : null;
        String usuario = (sesion != null) ? (String) sesion.getAttribute("nombreActivo") : null;

        List<String> rolesPermitidos = Arrays.asList("administrador", "coordinador", "gestor");

        if (rol == null || usuario == null || !rolesPermitidos.contains(rol.toLowerCase())) {
            System.out.println("❌ Acceso denegado: rol o usuario inválido");
            if (sesion != null) {
                sesion.setAttribute("mensaje", "⚠️ Acceso restringido: requiere rol administrador, coordinador o gestor.");
            }
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        String modulo = request.getParameter("modulo");
        String accion = request.getParameter("accion");

        System.out.println("🔎 Datos recibidos:");
        System.out.println("Usuario: " + usuario);
        System.out.println("Rol: " + rol);
        System.out.println("Módulo: " + modulo);
        System.out.println("Acción: " + accion);

        if (modulo != null && accion != null && !modulo.isEmpty() && !accion.isEmpty()) {
            try (Connection conn = Conexion.getConnection()) {
                BitacoraDAO bitacoraDAO = new BitacoraDAO(conn);
                boolean registrada = bitacoraDAO.registrarAccion(accion, usuario, rol, modulo);

                // Registro adicional en auditoría institucional
                Map<String, String> registro = new HashMap<>();
                registro.put("usuario", usuario);
                registro.put("rol", rol);
                registro.put("modulo", modulo);
                registro.put("accion", accion);
                registro.put("ip_origen", request.getRemoteAddr());
                new AuditoriaDAO(conn).registrarAccion(registro);

                if (registrada) {
                    System.out.println("✅ Acción registrada correctamente en bitácora");
                    response.sendRedirect(request.getContextPath() + "/administrador/registrarAccion.jsp?mensaje=Acción registrada correctamente.");
                } else {
                    System.out.println("⚠️ No se pudo registrar la acción");
                    response.sendRedirect(request.getContextPath() + "/administrador/registrarAccion.jsp?mensaje=No se pudo registrar la acción.");
                }
            } catch (Exception e) {
                System.err.println("❌ Error al registrar acción: " + e.getMessage());
                e.printStackTrace();
                response.sendRedirect(request.getContextPath() + "/administrador/registrarAccion.jsp?mensaje=Error al registrar la acción.");
            }
        } else {
            System.out.println("⚠️ Campos incompletos en el formulario");
            response.sendRedirect(request.getContextPath() + "/administrador/registrarAccion.jsp?mensaje=Debes completar todos los campos.");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect(request.getContextPath() + "/administrador/registrarAccion.jsp");
    }
}