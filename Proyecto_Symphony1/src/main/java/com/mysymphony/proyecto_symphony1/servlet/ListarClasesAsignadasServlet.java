/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.mysymphony.proyecto_symphony1.servlet;

// Importación de la clase DAO que gestiona consultas sobre clases
import com.mysymphony.proyecto_symphony1.dao.ClaseDAO;
import com.mysymphony.proyecto_symphony1.dao.BitacoraDAO;
import com.mysymphony.proyecto_symphony1.dao.AuditoriaDAO;
import com.mysymphony.proyecto_symphony1.dto.DocenteConClaseDTO;
// Importación de la clase utilitaria para obtener la conexión a la base de datos
import com.mysymphony.proyecto_symphony1.util.Conexion;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import javax.servlet.RequestDispatcher;
import java.io.IOException;
import java.sql.Connection;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * Servlet para listar clases que ya tienen docente asignado y estudiantes inscritos.
 * Rol: administrador
 * Autor: Camila
 * Ruta: /ListarClasesAsignadasServlet
 */
@WebServlet("/ListarClasesAsignadasServlet")
public class ListarClasesAsignadasServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession sesion = request.getSession(false);
        String rol = (sesion != null) ? (String) sesion.getAttribute("rolActivo") : null;
        String usuario = (sesion != null) ? (String) sesion.getAttribute("nombreActivo") : "desconocido";

        if (rol == null || !"administrador".equalsIgnoreCase(rol)) {
            if (sesion != null) {
                sesion.setAttribute("mensaje", "⚠️ Acceso restringido: requiere rol administrador.");
            }
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        try (Connection conn = Conexion.getConnection()) {
            ClaseDAO claseDAO = new ClaseDAO(conn);

            // ✅ Consultar clases con docente y estudiantes inscritos
            List<DocenteConClaseDTO> clasesConInscritos = claseDAO.listarClasesConDocenteYInscritos();
            request.setAttribute("clasesConInscritos", clasesConInscritos);

            // 📝 Registro en bitácora institucional
            BitacoraDAO bitacoraDAO = new BitacoraDAO(conn);
            bitacoraDAO.registrarAccion("Administrador consultó listado de clases asignadas con inscritos",
                    usuario, rol, "Clases asignadas");

            // 🛡️ Registro en auditoría técnica
            Map<String, String> registro = new HashMap<>();
            registro.put("usuario", usuario);
            registro.put("rol", rol);
            registro.put("modulo", "Clases asignadas");
            registro.put("accion", "Consultó listado de clases asignadas con inscritos");
            registro.put("ip_origen", request.getRemoteAddr());
            new AuditoriaDAO(conn).registrarAccion(registro);

            RequestDispatcher dispatcher = request.getRequestDispatcher("/admin/clasesAsignadas.jsp");
            dispatcher.forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "❌ Error al cargar clases asignadas: " + e.getMessage());
            request.getRequestDispatcher("/administrador/clasesAsignadas.jsp").forward(request, response);
        }
    }
}