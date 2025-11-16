/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mysymphony.proyecto_symphony1.filtro;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.*;

@WebFilter(filterName = "SessionFilter", urlPatterns = {
    "/dashboard.jsp",
    "/estudiante/*",
    "/docente/*",
    "/usuarios.jsp",
    "/reportes.jsp",
    "/configuracion.jsp",
    "/auditoria.jsp",
    "/listadoEstudiantes.jsp",
    "/listainscripciones.jsp",
    "/confirmacionInscripcion.jsp",
    "/panel.jsp",
    "/registroNotas.jsp",
    "/verEstudiantes.jsp",
    "/verObservaciones.jsp"
})
public class SessionFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // No se requiere configuración inicial
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        HttpSession session = req.getSession(false);

        boolean sesionActiva = (session != null &&
                                session.getAttribute("nombreActivo") != null &&
                                session.getAttribute("rolActivo") != null);

        if (sesionActiva) {
            chain.doFilter(request, response); // continúa con la solicitud
        } else {
            res.sendRedirect(req.getContextPath() + "/login.jsp"); // redirige si no hay sesión
        }
    }

    @Override
    public void destroy() {
        // No se requiere limpieza final
    }
}