<%-- 
    Document   : verClases
    Created on : 19/11/2025, 7:04:52 p. m.
    Author     : camiv
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" session="true" %>
<%@ page import="java.util.*" %>

<%
    HttpSession sesion = request.getSession(false);
    String nombre = (sesion != null) ? (String) sesion.getAttribute("nombreActivo") : null;
    String rol = (sesion != null) ? (String) sesion.getAttribute("rolActivo") : null;

    if (nombre == null || rol == null || !"administrador".equalsIgnoreCase(rol)) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }

    Map<String, Object> clase = (Map<String, Object>) request.getAttribute("clase");
    String error = (String) request.getAttribute("error");
%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Detalles de clase | SymphonySIAS</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/estilos.css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
</head>
<body>

<jsp:include page="../fragmentos/header.jsp" />

<div class="container mt-5">
    <h3><i class="fas fa-search"></i> Detalles de la clase</h3>

    <% if (error != null) { %>
        <div class="alert alert-danger text-center"><%= error %></div>
    <% } else if (clase != null && !clase.isEmpty()) { %>
        <table class="table table-bordered mt-4">
            <tr><th>ID</th><td><%= clase.get("id") %></td></tr>
            <tr><th>Nombre</th><td><%= clase.get("nombre_clase") %></td></tr>
            <tr><th>Instrumento</th><td><%= clase.get("instrumento") %></td></tr>
            <tr><th>Etapa</th><td><%= clase.get("etapa") %></td></tr>
            <tr><th>Grupo</th><td><%= clase.get("grupo") %></td></tr>
            <tr><th>Cupo</th><td><%= clase.get("cupo") %></td></tr>
            <tr><th>Fecha límite</th><td><%= clase.get("fecha_limite") %></td></tr>
            <tr><th>Estado</th><td><%= clase.get("estado") %></td></tr>
            <tr><th>Fecha creación</th><td><%= clase.get("fecha_creacion") %></td></tr>
            <tr><th>Fecha actualización</th><td><%= clase.get("fecha_actualizacion") %></td></tr>
            <tr><th>Horario</th><td><%= clase.get("dia_semana") %> de <%= clase.get("hora_inicio") %> a <%= clase.get("hora_fin") %></td></tr>
            <tr><th>Aula</th><td><%= clase.get("aula") %></td></tr>
            <tr><th>Docente ID</th><td><%= clase.get("id_docente") %></td></tr>
            <tr><th>Usuario editor</th><td><%= clase.get("usuario_editor") %></td></tr>
        </table>
    <% } else { %>
        <div class="alert alert-warning text-center">No se encontró información de la clase.</div>
    <% } %>

    <div class="mt-4 text-end">
        <a href="<%= request.getContextPath() %>/GestionarClasesServlet" class="btn btn-secondary">
            <i class="fas fa-arrow-left"></i> Volver a gestión de clases
        </a>
    </div>
</div>

<jsp:include page="../fragmentos/footer.jsp" />

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>