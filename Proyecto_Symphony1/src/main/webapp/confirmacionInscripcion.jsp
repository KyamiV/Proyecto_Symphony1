<%-- 
    Document   : confirmacionInscripcion
    Created on : 14/11/2025, 9:07:17 p. m.
    Author     : camiv
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String nombre = (String) request.getAttribute("nombreEstudiante");
    String[] asignaturas = (String[]) request.getAttribute("asignaturasSeleccionadas");
%>
<!DOCTYPE html>
<html>
<head>
    <title>Confirmación de inscripción musical</title>
    <link rel="stylesheet" href="assets/css/estilos.css">
</head>
<body>
    <div class="header">
        <span>Inscripción completada para <%= nombre %></span>
        <a href="dashboard.jsp" class="btn-volver">Volver al panel</a>
    </div>

    <h2>Asignaturas musicales inscritas:</h2>
    <ul>
        <% if (asignaturas != null) {
            for (String a : asignaturas) { %>
                <li><%= a %></li>
        <% } } else { %>
            <li>No se seleccionaron asignaturas.</li>
        <% } %>
    </ul>

    <footer>
        © 2025 SymphonySIAS - Sistema de Información Académico
    </footer>
</body>
</html>