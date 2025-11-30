<%-- 
    Document   : asignacionesDocente
    Created on : 21/11/2025, 4:35:06 p. m.
    Author     : camiv
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" session="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%
    String nombre = (String) session.getAttribute("nombreActivo");
    String rol = (String) session.getAttribute("rolActivo");

    if (nombre == null || rol == null || !"administrador".equalsIgnoreCase(rol)) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }

    String error = (String) request.getAttribute("error");
%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Asignación de docentes | SymphonySIAS</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/estilos.css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
</head>
<body>

<jsp:include page="../fragmentos/header.jsp" />

<div class="container mt-5">
    <h3 class="text-center mb-4"><i class="fas fa-chalkboard-teacher"></i> Asignación de docentes</h3>

    <% if (error != null) { %>
        <div class="alert alert-danger text-center"><%= error %></div>
    <% } %>

    <table class="table table-striped table-hover">
        <thead class="table-dark">
            <tr>
                <th>Docente</th>
                <th>Clase</th>
                <th>Instrumento</th>
                <th>Etapa</th>
                <th>Grupo</th>
                <th>Cupo</th>
                <th>Inscritos</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="fila" items="${asignaciones}">
                <tr>
                    <td>${fila.docente}</td>
                    <td>${fila.clase}</td>
                    <td>${fila.instrumento}</td>
                    <td>${fila.etapa}</td>
                    <td>${fila.grupo}</td>
                    <td>${fila.cupo}</td>
                    <td>${fila.inscritos}</td>
                </tr>
            </c:forEach>
        </tbody>
    </table>

    <!-- Botón volver -->
    <div class="text-end mt-4">
        <a href="<%= request.getContextPath() %>/GestionarClasesServlet" class="btn btn-secondary">
            <i class="fas fa-arrow-left"></i> Volver a clases
        </a>
    </div>
</div>

<jsp:include page="../fragmentos/footer.jsp" />

</body>
</html>