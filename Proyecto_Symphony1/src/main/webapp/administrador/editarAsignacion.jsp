<%-- 
    Document   : editarAsignacion
    Created on : 15/11/2025
    Author     : camiv
    Descripción: Vista del rol docente para editar el instrumento asignado a un estudiante.
                 Recibe parámetros desde la tabla de asignaciones y envía datos al servlet de actualización.
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
    String mensaje = request.getParameter("mensaje");
%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Editar instrumento asignado</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/estilos.css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
</head>
<body class="bg-light">
 <jsp:include page="../fragmentos/sidebar.jsp" />
<jsp:include page="../fragmentos/header.jsp" />

<div class="container mt-5">
    <h3 class="text-center mb-4"><i class="fas fa-pen-to-square"></i> Editar instrumento para <strong><%= nombreEstudiante %></strong></h3>

    <% if (mensaje != null) { %>
        <div class="alert alert-info text-center"><%= mensaje %></div>
    <% } %>

    <form action="<%= request.getContextPath() %>/ActualizarInstrumentoServlet" method="post" class="border rounded p-4 bg-white shadow-sm">
        <input type="hidden" name="idAsignacion" value="<%= idAsignacion %>">

        <div class="mb-3">
            <label class="form-label">🎼 Instrumento actual:</label>
            <input type="text" class="form-control" value="<%= instrumentoActual %>" disabled>
        </div>

        <div class="mb-3">
            <label for="nuevoInstrumento" class="form-label">🎹 Nuevo instrumento:</label>
            <input type="text" class="form-control" name="nuevoInstrumento" id="nuevoInstrumento" required>
        </div>

        <div class="d-flex justify-content-between">
            <button type="submit" class="btn btn-primary">
                <i class="fas fa-save"></i> Actualizar
            </button>
            <a href="<%= request.getContextPath() %>/VerAsignacionesServlet" class="btn btn-secondary">
                <i class="fas fa-arrow-left"></i> Volver a asignaciones
            </a>
        </div>
    </form>
</div>

<jsp:include page="../fragmentos/footer.jsp" />

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>