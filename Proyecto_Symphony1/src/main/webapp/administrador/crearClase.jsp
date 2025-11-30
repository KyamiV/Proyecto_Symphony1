<%-- 
    Document   : CrearClase
    Created on : 16/11/2025, 10:14:32 a. m.
    Author     : camiv
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" session="true" %>
<%@ page import="java.util.List, java.util.Map" %>

<%
    String nombre = (String) session.getAttribute("nombreActivo");
    String rol = (String) session.getAttribute("rolActivo");

    if (nombre == null || rol == null || !"administrador".equalsIgnoreCase(rol)) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }

    List<Map<String, String>> docentes = (List<Map<String, String>>) request.getAttribute("docentes");
    String error = (String) request.getAttribute("error");
%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Crear clase musical | SymphonySIAS</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/estilos.css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
</head>
<body>

<jsp:include page="../fragmentos/header.jsp" />

<div class="container mt-5">
    <h3 class="text-center mb-4"><i class="fas fa-plus-circle"></i> Crear nueva clase institucional</h3>

    <% if (error != null) { %>
        <div class="alert alert-danger text-center"><%= error %></div>
    <% } %>

    <form method="post" action="<%= request.getContextPath() %>/CrearClaseServlet" class="row g-3">

        <!-- 🔹 Datos básicos -->
        <div class="col-md-6">
            <label class="form-label">Nombre de la clase</label>
            <input type="text" name="nombre" class="form-control" placeholder="Ej: Clase 12 - Violín Básico" required>
        </div>
        <div class="col-md-6">
            <label class="form-label">Instrumento</label>
            <input type="text" name="instrumento" class="form-control" placeholder="Ej: Violín" required>
        </div>

        <!-- 🔹 Etapa y docente -->
        <div class="col-md-6">
            <label class="form-label">Etapa pedagógica</label>
            <select name="etapa" class="form-select" required>
                <option value="">Seleccionar etapa</option>
                <option>Exploración sonora</option>
                <option>Iniciación técnica</option>
                <option>Repertorio básico</option>
                <option>Repertorio intermedio</option>
                <option>Repertorio avanzado</option>
            </select>
        </div>
        <div class="col-md-6">
            <label class="form-label">Docente asignado</label>
            <select name="docente" class="form-select" required>
                <option value="">Seleccionar docente</option>
                <% for (Map<String, String> docente : docentes) { %>
                    <option value="<%= docente.get("id") %>"><%= docente.get("nombre") %></option>
                <% } %>
            </select>
        </div>

        <!-- 🔹 Horario -->
        <div class="col-md-4">
            <label class="form-label">Día de la semana</label>
            <select name="dia" class="form-select" required>
                <option>Lunes</option>
                <option>Martes</option>
                <option>Miércoles</option>
                <option>Jueves</option>
                <option>Viernes</option>
                <option>Sábado</option>
            </select>
        </div>
        <div class="col-md-4">
            <label class="form-label">Hora inicio</label>
            <input type="time" name="hora_inicio" class="form-control" required>
        </div>
        <div class="col-md-4">
            <label class="form-label">Hora fin</label>
            <input type="time" name="hora_fin" class="form-control" required>
        </div>

        <!-- 🔹 Grupo y cupo -->
        <div class="col-md-6">
            <label class="form-label">Grupo</label>
            <input type="text" name="grupo" class="form-control" placeholder="Ej: Grupo A" required>
        </div>
        <div class="col-md-6">
            <label class="form-label">Cupo máximo</label>
            <input type="number" name="cupo" class="form-control" min="1" placeholder="Ej: 20" required>
        </div>

        <!-- 🔹 Fecha límite -->
        <div class="col-md-6">
            <label class="form-label">Fecha límite de inscripción</label>
            <input type="date" name="fecha_limite" class="form-control" required>
        </div>

        <!-- 🔹 Botón registrar -->
        <div class="col-12 text-end">
            <button type="submit" class="btn btn-primary">
                <i class="fas fa-save"></i> Registrar clase
            </button>
        </div>
    </form>

    <!-- 🔹 Botón volver -->
    <div class="text-end mt-4">
        <a href="<%= request.getContextPath() %>/GestionarClasesServlet" class="btn btn-secondary">
            <i class="fas fa-arrow-left"></i> Volver a clases
        </a>
    </div>
</div>

<jsp:include page="../fragmentos/footer.jsp" />

</body>
</html>