<%-- 
    Document   : historialEstudiante
    Created on : 14/11/2025, 10:34:42 p. m.
    Author     : camiv
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" session="true" %>
<%
    String nombre = (String) session.getAttribute("nombreActivo");
    String rol = (String) session.getAttribute("rolActivo");

    if (nombre == null || rol == null || !"administrador".equalsIgnoreCase(rol)) {
        response.sendRedirect("login.jsp");
        return;
    }

    String idEstudiante = request.getParameter("id"); // ID del estudiante a consultar
    // Aquí podrías cargar los datos del estudiante desde la base de datos usando el idEstudiante
%>
<!DOCTYPE html>
<html>
<head>
    <title>Historial académico</title>
    <link rel="stylesheet" href="assets/css/estilos.css">
</head>
<body>

    <jsp:include page="../fragmentos/header.jsp" />

    <h2>Historial académico del estudiante</h2>

    <!-- Datos del estudiante -->
    <p><strong>Nombre:</strong> Laura Gómez</p>
    <p><strong>ID:</strong> <%= idEstudiante %></p>

    <!-- Tabla de historial -->
    <table class="tabla-notas">
        <tr>
            <th>Asignatura</th>
            <th>Periodo</th>
            <th>Nota</th>
            <th>Observaciones</th>
        </tr>
        <tr>
            <td>Violín</td>
            <td>2025-I</td>
            <td>4.5</td>
            <td>Excelente progreso técnico</td>
        </tr>
        <tr>
            <td>Piano</td>
            <td>2025-I</td>
            <td>3.8</td>
            <td>Debe mejorar lectura de partituras</td>
        </tr>
        <!-- Más registros -->
    </table>

    <div style="text-align:center; margin-top:30px;">
        <a href="dashboardAdmin.jsp" class="btn-volver">Volver al panel</a>
    </div>

    <jsp:include page="../fragmentos/footer.jsp" />

</body>
</html>