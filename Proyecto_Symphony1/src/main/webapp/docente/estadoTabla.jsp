<%-- 
    Document   : estadoTabla
    Created on : 17/11/2025, 9:08:30 p. m.
    Author     : camiv
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" session="true" %>
<%@ page import="java.util.*" %>

<%
    String nombre = (String) session.getAttribute("nombreActivo");
    String rol = (String) session.getAttribute("rolActivo");

    if (nombre == null || rol == null || !"docente".equalsIgnoreCase(rol)) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }

    Map<String, String> estadoTabla = (Map<String, String>) request.getAttribute("estadoTabla");
    String mensaje = (String) request.getAttribute("mensaje");
    String tablaId = request.getParameter("tablaId");
%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Estado de tabla - SymphonySIAS</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css" rel="stylesheet">
    <style>
        body {
            background-color: #f4f6f9;
            font-family: 'Poppins', sans-serif;
            padding: 30px;
        }
        .estado-box {
            background: #fff;
            padding: 30px;
            border-radius: 12px;
            box-shadow: 0 4px 12px rgba(0,0,0,0.06);
            max-width: 600px;
            margin: 0 auto;
        }
    </style>
</head>
<body>

<jsp:include page="../fragmentos/header.jsp" />

<div class="estado-box text-center">
    <h4 class="text-primary mb-4"><i class="fas fa-info-circle"></i> Estado de la tabla institucional</h4>

    <% if (mensaje != null) { %>
        <div class="alert alert-warning"><%= mensaje %></div>
    <% } else if (estadoTabla != null && !estadoTabla.isEmpty()) { %>
        <p><strong>Nombre:</strong> <%= estadoTabla.get("nombre") %></p>
        <p><strong>Descripción:</strong> <%= estadoTabla.get("descripcion") %></p>
        <p><strong>Fecha de creación:</strong> <%= estadoTabla.get("fecha") %></p>
        <p><strong>¿Enviada al administrador?</strong> <%= estadoTabla.get("enviada") %></p>

        <% if ("Sí".equalsIgnoreCase(estadoTabla.get("enviada"))) { %>
            <p><strong>Fecha de envío:</strong> <%= estadoTabla.get("fecha_envio") %></p>
            <p><strong>Enviada por:</strong> <%= estadoTabla.get("enviada_por") %></p>
        <% } else { %>
            <p class="text-muted">Esta tabla aún no ha sido enviada.</p>
        <% } %>
    <% } else { %>
        <div class="alert alert-danger">❌ No se pudo cargar el estado de la tabla.</div>
    <% } %>

    <div class="mt-4 d-flex justify-content-center gap-3 flex-wrap">
        <a href="<%= request.getContextPath() %>/VerTablasGuardadasServlet" class="btn btn-outline-dark">
            <i class="fas fa-arrow-left"></i> Volver a mis tablas
        </a>
        <% if (tablaId != null) { %>
        <a href="<%= request.getContextPath() %>/VerNotasPorTablaServlet?tablaId=<%= tablaId %>" class="btn btn-outline-primary">
            <i class="fas fa-eye"></i> Ver notas registradas
        </a>
        <% } %>
    </div>
</div>

<jsp:include page="../fragmentos/footer.jsp" />

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>