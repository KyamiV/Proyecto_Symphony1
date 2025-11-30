<%-- 
    Document   : registrarClase
    Created on : 19/11/2025, 1:23:35 p. m.
    Author     : camiv
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" session="true" %>
<%@ page import="java.util.List" %>

<%
    // 🔐 Validación de sesión y rol
    String nombre = (String) session.getAttribute("nombreActivo");
    String rol = (String) session.getAttribute("rolActivo");

    if (nombre == null || rol == null || !"administrador".equalsIgnoreCase(rol)) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }

    String mensaje = (String) session.getAttribute("mensaje");
    String error = (String) request.getAttribute("error");
    if (mensaje != null) session.removeAttribute("mensaje");

    List<String> instrumentos = (List<String>) request.getAttribute("instrumentos");
    List<String> etapas = (List<String>) request.getAttribute("etapas");
%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Registrar clase | SymphonySIAS</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/estilos.css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
</head>
<body>

<jsp:include page="../fragmentos/header.jsp" />

<div class="container mt-5">
    <h3 class="text-center mb-4"><i class="fas fa-plus-circle"></i> Registrar nueva clase institucional</h3>

    <% if (mensaje != null) { %>
        <div class="alert alert-success text-center"><%= mensaje %></div>
    <% } %>
    <% if (error != null) { %>
        <div class="alert alert-danger text-center"><%= error %></div>
    <% } %>

    <form method="post" action="<%= request.getContextPath() %>/RegistrarClaseServlet" class="row g-3 px-2">
        <div class="col-md-4">
            <label class="form-label">Nombre de la clase</label>
            <input type="text" name="nombreClase" class="form-control" required>
        </div>

        <div class="col-md-3">
            <label class="form-label">Instrumento</label>
            <select name="instrumento" class="form-select" required>
                <option value="">Selecciona instrumento</option>
                <% if (instrumentos != null) {
                    for (String inst : instrumentos) { %>
                        <option value="<%= inst %>"><%= inst %></option>
                <%  }
                } %>
            </select>
        </div>

        <div class="col-md-3">
            <label class="form-label">Etapa pedagógica</label>
            <select name="etapa" class="form-select" required>
                <option value="">Selecciona etapa</option>
                <% if (etapas != null) {
                    for (String etapa : etapas) { %>
                        <option value="<%= etapa %>"><%= etapa %></option>
                <%  }
                } %>
            </select>
        </div>

        <div class="col-md-2">
            <label class="form-label">Grupo</label>
            <input type="text" name="grupo" class="form-control" required>
        </div>

        <div class="col-md-2">
            <label class="form-label">Cupo</label>
            <input type="number" name="cupo" class="form-control" min="1" required>
        </div>

        <div class="col-md-3">
            <label class="form-label">Fecha límite de inscripción</label>
            <input type="date" name="fecha_limite" class="form-control" required>
        </div>

        <div class="col-12 text-center mt-4 d-flex justify-content-center gap-3 flex-wrap">
            <button type="submit" class="btn btn-success px-4">
                <i class="fas fa-save"></i> Registrar clase
            </button>
            <a href="<%= request.getContextPath() %>/AsignarEstudiantesServlet" class="btn btn-secondary px-4">
                <i class="fas fa-arrow-left"></i> Volver a asignación
            </a>
        </div>
    </form>
</div>

<jsp:include page="../fragmentos/footer.jsp" />

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>