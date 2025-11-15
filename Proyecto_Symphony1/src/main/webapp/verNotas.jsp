<%-- 
    Document   : verNotas
    Created on : 14/11/2025, 8:22:19 p. m.
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
<html>
<head>
    <title>Notas musicales</title>
    <link rel="stylesheet" href="assets/css/estilos.css">
</head>
<body>
    <div class="header">
        <span>Notas de <%= nombre %> (estudiante)</span>
        <a href="CerrarSesionServlet" class="btn-cerrar">Cerrar sesión</a>
    </div>

    <h2>Calificaciones por asignatura musical</h2>

    <p>No tienes calificaciones registradas aún. Una vez que tus docentes ingresen tus notas, aparecerán aquí.</p>

    <a href="dashboard.jsp" class="btn-volver">Volver al panel</a>

    <footer>
        © 2025 SymphonySIAS - Sistema de Información Académico
    </footer>
</body>
</html>