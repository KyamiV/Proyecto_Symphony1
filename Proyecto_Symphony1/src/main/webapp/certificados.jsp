<%-- 
    Document   : certificados
    Created on : 14/11/2025, 8:24:34 p. m.
    Author     : camiv
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String nombre = (String) session.getAttribute("nombreActivo");
    String rol = (String) session.getAttribute("rolActivo");

    if (nombre == null || rol == null || !"estudiante".equalsIgnoreCase(rol)) {
        response.sendRedirect("login.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Mis certificados</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link rel="stylesheet" href="assets/css/estilos.css">
</head>
<body class="container mt-5">

    <!-- Logo institucional -->
    <div class="text-end mb-3">
        <img src="assets/img/logo_symphonysias.png" alt="Logo SymphonySIAS" height="60">
    </div>

    <!-- Encabezado -->
    <div class="header mb-4">
        <span>Certificados de <%= nombre %> (estudiante)</span>
        <a href="CerrarSesionServlet" class="btn-cerrar">Cerrar sesión</a>
    </div>

    <!-- Contenido -->
    <h2 class="text-center">Visualización de certificados musicales</h2>
    <p class="text-center">Desde aquí podrás visualizar y descargar tus certificados académicos.</p>

    <div class="alert alert-success mt-4 text-center">
        <i class="fas fa-file-alt"></i> Esta sección está en construcción. Pronto podrás ver tus certificados aquí.
    </div>

    <div class="text-center mt-3">
        <a href="dashboard.jsp" class="btn-volver">Volver al panel</a>
    </div>

    <!-- Pie de página -->
    <footer class="text-center mt-5 bg-dark text-white p-3">
        © 2025 SymphonySIAS - Sistema de Información Académico Musical
    </footer>

</body>
</html>