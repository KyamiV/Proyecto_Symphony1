<%-- 
    Document   : asignarEstudiantes
    Created on : 15/11/2025, 7:06:54 p. m.
    Author     : camiv
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    HttpSession sesion = request.getSession();
    String rol = (String) sesion.getAttribute("rolActivo");
    String nombreDocente = (String) sesion.getAttribute("nombreActivo");

    if (rol == null || !"docente".equalsIgnoreCase(rol) || nombreDocente == null) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }

    String idAsignacion = request.getParameter("id");
    String nombreEstudiante = request.getParameter("nombre");
    String instrumentoActual = request.getParameter("instrumento");
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Editar asignación</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/estilos/estilos.css">
</head>
<body>
    <h2>Editar instrumento asignado</h2>

    <form action="${pageContext.request.contextPath}/EditarAsignacionServlet" method="post">
        <input type="hidden" name="idAsignacion" value="<%= idAsignacion %>">

        <p><strong>Estudiante:</strong> <%= nombreEstudiante %></p>

        <label for="instrumento">Instrumento:</label>
        <input type="text" name="instrumento" value="<%= instrumentoActual %>" required>

        <button type="submit">Guardar cambios</button>
    </form>

    <p><a href="${pageContext.request.contextPath}/VerAsignacionesServlet">← Volver a asignaciones</a></p>

    <%
        String mensaje = (String) request.getAttribute("mensaje");
        if (mensaje != null) {
    %>
        <div class="mensaje"><%= mensaje %></div>
    <%
        }
    %>
</body>
</html>