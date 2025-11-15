<%-- 
    Document   : reportes
    Created on : 14/11/2025, 8:32:22 p. m.
    Author     : camiv
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String nombre = (String) session.getAttribute("nombreActivo");
    String rol = (String) session.getAttribute("rolActivo");

    if (nombre == null || rol == null || !"coordinador académico".equalsIgnoreCase(rol)) {
        response.sendRedirect("login.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Reportes Académicos</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="assets/css/estilos.css">
</head>
<body class="container mt-5">
    <h2>Bienvenida, <%= nombre %> (<%= rol %>)</h2>
    <p>Desde aquí puedes generar reportes de rendimiento académico, asistencia y distribución por curso.</p>

    <div class="alert alert-warning mt-4">
        <i class="fas fa-chart-bar"></i> Esta sección está en construcción. Pronto podrás generar reportes desde aquí.
    </div>
</body>
</html>