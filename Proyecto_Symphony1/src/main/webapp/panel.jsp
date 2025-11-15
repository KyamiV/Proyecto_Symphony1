<%-- 
    Document   : panel
    Created on : 14/11/2025, 7:21:22 p. m.
    Author     : camiv
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<%
    String usuario = (String) session.getAttribute("usuario");
    if (usuario == null) {
        response.sendRedirect("login.jsp");
        return;
    }
%>
<html>
<head>
    <title>Panel Symphony</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="assets/css/estilos.css">
</head>
<body class="bg-light">
    <div class="container mt-5">
        <div class="text-center">
            <h2>Bienvenida, <%= usuario %> 🎉</h2>
            <p>Has iniciado sesión correctamente en SymphonySIAS.</p>
            <a href="usuarios.jsp" class="btn btn-outline-primary mt-3">Ver usuarios</a>
            <a href="listainscripciones.jsp" class="btn btn-outline-secondary mt-3">Ver inscripciones</a>
        </div>
    </div>
</body>
</html>