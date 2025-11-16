<%-- 
    Document   : verNotasDocente
    Created on : 14/11/2025, 10:30:00 p. m.
    Author     : camiv
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" session="true" %>
<%@ page import="java.util.List, java.util.Map" %>
<%
    String nombre = (String) session.getAttribute("nombreActivo");
    String rol = (String) session.getAttribute("rolActivo");

    if (nombre == null || rol == null || !"docente".equalsIgnoreCase(rol)) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }

    List<Map<String, String>> notas = (List<Map<String, String>>) request.getAttribute("notas");
    String curso = (String) request.getAttribute("curso");
    String mensaje = (String) request.getAttribute("mensaje");
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Consultar notas registradas - SymphonySIAS</title>
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
        .dashboard-box {
            background: #ffffff;
            padding: 25px;
            border-radius: 10px;
            box-shadow: 0 0 10px rgba(0,0,0,0.08);
            max-width: 100%;
            margin: 0 auto;
        }
        .tabla-notas th, .tabla-notas td {
            vertical-align: middle;
            text-align: center;
        }
    </style>
</head>
<body>

<jsp:include page="../fragmentos/header.jsp" />

<div class="dashboard-box">
    <h4 class="text-center mb-4"><i class="fas fa-chart-bar"></i> Consultar notas registradas</h4>

    <% if (curso != null) { %>
        <div class="mb-3 text-center">
            <span class="badge bg-secondary">Curso actual: <%= curso %></span>
        </div>
    <% } %>

    <% if (mensaje != null) { %>
        <div class="alert alert-success text-center" style="max-width: 700px; margin: 0 auto;">
            <%= mensaje %>
        </div>
    <% } %>

    <% if (notas == null || notas.isEmpty()) { %>
        <div class="alert alert-info text-center" style="max-width: 700px; margin: 0 auto;">
            <p class="mb-1">No hay notas registradas aún.</p>
            <p class="mb-0">Una vez que se registre el avance de los estudiantes, aparecerá aquí.</p>
        </div>
    <% } else { %>
        <table class="table table-bordered table-striped tabla-notas">
            <thead class="table-dark">
                <tr>
                    <th>Estudiante</th>
                    <th>Nota</th>
                    <th>Fecha de registro</th>
                </tr>
            </thead>
            <tbody>
                <% for (Map<String, String> fila : notas) { %>
                    <tr>
                        <td><%= fila.get("nombre_estudiante") %></td>
                        <td><%= fila.get("nota") %></td>
                        <td><%= fila.get("fecha_registro") %></td>
                    </tr>
                <% } %>
            </tbody>
        </table>
    <% } %>

    <div class="mt-4 text-start">
        <a href="<%= request.getContextPath() %>/DashboardServlet" class="btn btn-secondary">
            <i class="fas fa-arrow-left"></i> Volver al panel docente
        </a>
    </div>
</div>

<jsp:include page="../fragmentos/footer.jsp" />

</body>
</html>