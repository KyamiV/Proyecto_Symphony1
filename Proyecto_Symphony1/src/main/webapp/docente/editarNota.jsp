<%-- 
    Document   : editarNota
    Created on : 15/11/2025, 12:08:35 p. m.
    Author     : camiv
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" session="true" %>
<%@ page import="java.util.List" %>
<%
    String nombre = (String) session.getAttribute("nombreActivo");
    String rol = (String) session.getAttribute("rolActivo");

    if (nombre == null || rol == null || !"docente".equalsIgnoreCase(rol)) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }

    // Parámetros recibidos
    String estudiante = request.getParameter("estudiante");
    String instrumento = request.getParameter("instrumento");
    String etapa = request.getParameter("etapa");
    String nota = request.getParameter("nota");
    String observaciones = request.getParameter("observaciones");
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Editar nota - SymphonySIAS</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/estilos.css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;600&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
</head>
<body>

<jsp:include page="../fragmentos/header.jsp" />

<div class="dashboard-box">
    <h4 class="text-center mb-4"><i class="fas fa-edit"></i> Editar calificación</h4>

    <form action="<%= request.getContextPath() %>/ActualizarNotaServlet" method="post" class="form-box">
        <input type="hidden" name="estudiante" value="<%= estudiante %>">
        <input type="hidden" name="instrumento" value="<%= instrumento %>">
        <input type="hidden" name="etapa" value="<%= etapa %>">

        <div class="mb-3">
            <label>Estudiante</label>
            <input type="text" class="form-control" value="<%= estudiante %>" readonly>
        </div>
        <div class="mb-3">
            <label>Instrumento</label>
            <input type="text" class="form-control" value="<%= instrumento %>" readonly>
        </div>
        <div class="mb-3">
            <label>Etapa pedagógica</label>
            <input type="text" class="form-control" value="<%= etapa %>" readonly>
        </div>
        <div class="mb-3">
            <label>Nota</label>
            <input type="number" name="nota" class="form-control" step="0.1" min="1" max="5" value="<%= nota %>" required>
        </div>
        <div class="mb-3">
            <label>Observaciones</label>
            <textarea name="observaciones" class="form-control" rows="3"><%= observaciones %></textarea>
        </div>
        <div class="text-end">
            <button type="submit" class="btn-enviar"><i class="fas fa-save"></i> Guardar cambios</button>
        </div>
    </form>

    <div class="mt-4 text-start">
        <a href="<%= request.getContextPath() %>/VerNotasServlet" class="btn-volver">
            <i class="fas fa-arrow-left"></i> Volver al listado de notas
        </a>
    </div>
</div>

<jsp:include page="../fragmentos/footer.jsp" />

</body>
</html>