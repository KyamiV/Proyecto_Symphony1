<%-- 
    Document   : editarTabla
    Created on : 18/11/2025, 10:24:28 a. m.
    Author     : camiv
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" session="true" %>
<%@ page import="java.util.*" %>

<%
    String nombre = (String) session.getAttribute("nombreActivo");
    String rol = (String) session.getAttribute("rolActivo");

    if (nombre == null || rol == null || !"docente".equalsIgnoreCase(rol)) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }

    Map<String, String> tabla = (Map<String, String>) request.getAttribute("tabla");
    if (tabla == null) {
        response.sendRedirect(request.getContextPath() + "/VerTablasGuardadasServlet");
        return;
    }

    String id = tabla.get("id");
    String nombreTabla = tabla.get("nombre");
    String descripcion = tabla.get("descripcion");
    String clase = tabla.getOrDefault("clase", "—");
    String fecha = tabla.getOrDefault("fecha", "—");
    String fechaActualizacion = tabla.getOrDefault("fecha_actualizacion", "—");
    String usuarioEditor = tabla.getOrDefault("usuario_editor", "—");
%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Editar tabla - SymphonySIAS</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/estilos.css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;600&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
    <style>
        body {
            font-family: 'Poppins', sans-serif;
            background-color: #f4f6f9;
            padding: 30px;
        }
        .form-box {
            background: #ffffff;
            padding: 30px;
            border-radius: 12px;
            box-shadow: 0 4px 12px rgba(0,0,0,0.06);
            max-width: 600px;
            margin: 0 auto;
        }
    </style>
</head>
<body>

<jsp:include page="../fragmentos/header.jsp" />

<div class="form-box">
    <h4 class="text-center mb-4"><i class="fas fa-pen-to-square"></i> Editar tabla institucional</h4>

    <form action="<%= request.getContextPath() %>/ActualizarTablaServlet" method="post">
        <input type="hidden" name="tablaId" value="<%= id %>">

        <div class="mb-3">
            <label class="form-label">Nombre de la tabla</label>
            <input type="text" name="nombre" class="form-control" value="<%= nombreTabla %>" required>
        </div>

        <div class="mb-3">
            <label class="form-label">Descripción</label>
            <textarea name="descripcion" class="form-control" rows="3" required><%= descripcion %></textarea>
        </div>

        <div class="mb-3">
            <label class="form-label">Clase asociada</label>
            <input type="text" class="form-control" value="<%= clase %>" disabled>
        </div>

        <div class="mb-3">
            <label class="form-label">Fecha de creación</label>
            <input type="text" class="form-control" value="<%= fecha %>" disabled>
        </div>

        <div class="mb-3">
            <label class="form-label">Última edición</label>
            <input type="text" class="form-control" value="<%= fechaActualizacion %> por <%= usuarioEditor %>" disabled>
        </div>

        <div class="d-flex justify-content-between mt-4">
            <a href="<%= request.getContextPath() %>/VerTablasGuardadasServlet" class="btn btn-outline-secondary">
                <i class="fas fa-arrow-left"></i> Cancelar
            </a>
            <button type="submit" class="btn btn-primary">
                <i class="fas fa-save"></i> Guardar cambios
            </button>
        </div>
    </form>
</div>

<jsp:include page="../fragmentos/footer.jsp" />

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>