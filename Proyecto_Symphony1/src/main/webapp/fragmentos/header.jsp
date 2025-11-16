<%-- 
    Document   : header
    Created on : 14/11/2025, 10:12:19 p. m.
    Author     : camiv
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<%
    String nombre = (String) session.getAttribute("nombreActivo");
    String rol = (String) session.getAttribute("rolActivo");

    if (nombre == null || rol == null) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }

    String claseHeader = "";
    String iconoRol = "fas fa-user";

    if ("estudiante".equalsIgnoreCase(rol)) {
        claseHeader = "dashboard-estudiante";
        iconoRol = "fas fa-user-graduate";
    } else if ("docente".equalsIgnoreCase(rol)) {
        claseHeader = "dashboard-docente";
        iconoRol = "fas fa-chalkboard-teacher";
    } else if ("coordinador académico".equalsIgnoreCase(rol)) {
        claseHeader = "dashboard-coordinador";
        iconoRol = "fas fa-user-cog";
    } else if ("administrador".equalsIgnoreCase(rol)) {
        claseHeader = "dashboard-admin";
        iconoRol = "fas fa-tools";
    }
%>
<div class="container-fluid p-3 <%= claseHeader %> text-white d-flex justify-content-between align-items-center">
    <div>
        <h4><i class="<%= iconoRol %>"></i> SymphonySIAS</h4>
        <small>Bienvenida, <strong><%= nombre %></strong> (<%= rol %>)</small>
    </div>
    <img src="<%= request.getContextPath() %>/assets/img/logo.png" alt="Logo SymphonySIAS" style="height:60px;">
</div>