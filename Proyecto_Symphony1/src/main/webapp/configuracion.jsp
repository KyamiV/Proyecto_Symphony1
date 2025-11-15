<%-- 
    Document   : configuracion
    Created on : 14/11/2025, 8:35:57 p. m.
    Author     : camiv
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String nombre = (String) session.getAttribute("nombreActivo");
    String rol = (String) session.getAttribute("rolActivo");

    if (nombre == null || rol == null || !"administrador".equalsIgnoreCase(rol)) {
        response.sendRedirect("login.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Configuración del Sistema</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="assets/css/estilos.css">
</head>
<body class="container mt-5">
    <h2>Bienvenida, <%= nombre %> (<%= rol %>)</h2>
    <p>Desde aquí puedes modificar parámetros generales del sistema, como roles, claves maestras y más.</p>

    <div class="alert alert-secondary mt-4">
        <i class="fas fa-cogs"></i> Esta sección está en construcción. Pronto podrás configurar el sistema desde aquí.
    </div>
</body>
</html>