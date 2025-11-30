/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.mysymphony.proyecto_symphony1.servlet;

import com.mysymphony.proyecto_symphony1.dao.ConfiguracionDAO;
import com.mysymphony.proyecto_symphony1.dao.InstrumentoDAO;
import com.mysymphony.proyecto_symphony1.modelo.Instrumento; 
import com.mysymphony.proyecto_symphony1.util.Conexion;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

@WebServlet("/ConfiguracionServlet")
public class ConfiguracionServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try (Connection conn = Conexion.getConnection()) {
            ConfiguracionDAO dao = new ConfiguracionDAO(conn);
            InstrumentoDAO instrumentoDAO = new InstrumentoDAO(conn);

            // Cargar parámetros técnicos
            Map<String, String> parametros = dao.listarParametros();
            request.setAttribute("parametrosTecnicos", parametros);

            // Cargar etapas pedagógicas
            List<String> etapas = dao.listarEtapas();
            request.setAttribute("etapas", etapas);

            // Cargar roles y colores (Map rol → colorHex)
            Map<String, String> roles = dao.listarRoles();
            request.setAttribute("rolesActivos", roles);

            // Cargar instrumentos
            List<Instrumento> instrumentos = instrumentoDAO.listarInstrumentos();
            request.setAttribute("instrumentos", instrumentos);

            // Redirigir al JSP
            request.getRequestDispatcher("/administrador/configuracion.jsp").forward(request, response);

        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "Error al cargar configuración: " + e.getMessage());
            request.getRequestDispatcher("/administrador/configuracion.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String accion = request.getParameter("accion");

        try (Connection conn = Conexion.getConnection()) {
            ConfiguracionDAO dao = new ConfiguracionDAO(conn);
            InstrumentoDAO instrumentoDAO = new InstrumentoDAO(conn);

            // =========================
            // INSTRUMENTOS
            // =========================
            if ("listarInstrumentos".equals(accion)) {
                List<Instrumento> instrumentos = instrumentoDAO.listarInstrumentos();
                request.setAttribute("instrumentos", instrumentos);
                request.setAttribute("mensaje", "Instrumentos listados correctamente.");
            }

            if ("agregarInstrumento".equals(accion)) {
                String nombre = request.getParameter("nombreInstrumento");
                int cupo = Integer.parseInt(request.getParameter("cupoInstrumento"));
                instrumentoDAO.insertarInstrumento(nombre, cupo);
                request.setAttribute("mensaje", "Instrumento agregado correctamente.");
            }

            if ("editarInstrumento".equals(accion)) {
                String nombre = request.getParameter("nombreInstrumento");
                int nuevoCupo = Integer.parseInt(request.getParameter("nuevoCupo"));
                instrumentoDAO.actualizarCupoInstrumento(nombre, nuevoCupo);
                request.setAttribute("mensaje", "Instrumento actualizado correctamente.");
            }

            if ("eliminarInstrumento".equals(accion)) {
                String nombre = request.getParameter("nombreInstrumento");
                instrumentoDAO.eliminarInstrumento(nombre);
                request.setAttribute("mensaje", "Instrumento eliminado correctamente.");
            }

            // =========================
            // ETAPAS
            // =========================
            if ("agregarEtapa".equals(accion)) {
                String nuevaEtapa = request.getParameter("nuevaEtapa");
                String descripcion = request.getParameter("descripcionEtapa");
                dao.insertarEtapa(nuevaEtapa, descripcion);
                request.setAttribute("mensaje", "Etapa agregada correctamente.");
            }

            if ("editarEtapa".equals(accion)) {
                String etapaOriginal = request.getParameter("etapaOriginal");
                String nuevaEtapa = request.getParameter("nuevaEtapaNombre");
                String nuevaDescripcion = request.getParameter("nuevaDescripcion");
                dao.actualizarEtapa(etapaOriginal, nuevaEtapa, nuevaDescripcion);
                request.setAttribute("mensaje", "Etapa actualizada correctamente.");
            }

            if ("eliminarEtapa".equals(accion)) {
                String etapaEliminar = request.getParameter("etapaEliminar");
                dao.eliminarEtapa(etapaEliminar);
                request.setAttribute("mensaje", "Etapa eliminada correctamente.");
            }

            // =========================
            // ROLES Y COLORES
            // =========================
            if ("asignarColor".equals(accion)) {
                String rol = request.getParameter("rolColor");
                String colorHex = request.getParameter("colorRol");
                dao.asignarColorRol(rol, colorHex);
                request.setAttribute("mensaje", "Color asignado correctamente al rol.");
            }

            if ("insertarRol".equals(accion)) {
                String rol = request.getParameter("nuevoRol");
                String colorHex = request.getParameter("colorRol");
                dao.insertarRol(rol, colorHex);
                request.setAttribute("mensaje", "Rol agregado correctamente.");
            }

            if ("eliminarRol".equals(accion)) {
                String rol = request.getParameter("rolEliminar");
                dao.eliminarRol(rol);
                request.setAttribute("mensaje", "Rol eliminado correctamente.");
            }

            // =========================
            // RECARGAR DATOS
            // =========================
            Map<String, String> parametros = dao.listarParametros();
            request.setAttribute("parametrosTecnicos", parametros);

            List<String> etapas = dao.listarEtapas();
            request.setAttribute("etapas", etapas);

            Map<String, String> roles = dao.listarRoles();
            request.setAttribute("rolesActivos", roles);

            List<Instrumento> instrumentos = instrumentoDAO.listarInstrumentos();
            request.setAttribute("instrumentos", instrumentos);

            request.getRequestDispatcher("/administrador/configuracion.jsp").forward(request, response);

        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "Error al procesar acción: " + e.getMessage());
            request.getRequestDispatcher("/administrador/configuracion.jsp").forward(request, response);
        }
    }
}