<%-- 
    Document   : adminNotas
    Created on : 14/11/2025, 10:32:53 p. m.
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
%>
<!DOCTYPE html>
<html>
<head>
    <title>Gestión de notas</title>
    <link rel="stylesheet" href="assets/css/estilos.css">
</head>
<body>

    <jsp:include page="../fragmentos/header.jsp" />

    <h2>Notas registradas en SymphonySIAS</h2>

    <!-- Filtros -->
    <form method="get" action="adminNotas.jsp">
        <label for="docente">Docente:</label>
        <input type="text" name="docente" placeholder="Nombre del docente">

        <label for="asignatura">Asignatura:</label>
        <input type="text" name="asignatura" placeholder="Ej: Violín">

        <label for="estudiante">Estudiante:</label>
        <input type="text" name="estudiante" placeholder="Nombre del estudiante">

        <button type="submit" class="btn-enviar">Filtrar</button>
    </form>

    <!-- Tabla de notas -->
    <table class="tabla-notas">
        <tr>
            <th>Estudiante</th>
            <th>Asignatura</th>
            <th>Nota</th>
            <th>Docente</th>
            <th>Acciones</th>
        </tr>
        <tr>
            <td>Laura Gómez</td>
            <td>Violín</td>
            <td>4.5</td>
            <td>Prof. Martínez</td>
            <td><a href="EnviarNotaServlet?id=123" class="btn-enviar">Enviar</a></td>
        </tr>
        <!-- Más registros -->
    </table>

    <div style="text-align:center; margin-top:30px;">
        <a href="dashboardAdmin.jsp" class="btn-volver">Volver al panel</a>
    </div>

    <jsp:include page="../fragmentos/footer.jsp" />

</body>
</html>