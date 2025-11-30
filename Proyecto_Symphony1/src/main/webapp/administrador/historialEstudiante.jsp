<%-- 
    Document   : historialEstudiante
    Created on : 14/11/2025, 10:34:42 p. m.
    Author     : camiv
    Descripción: Vista del rol administrador para consultar el historial académico de un estudiante.
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" session="true" %>
<%@ page import="java.util.*" %>

<%
    // Validación de sesión y rol
    String nombre = (String) session.getAttribute("nombreActivo");
    String rol = (String) session.getAttribute("rolActivo");

    if (nombre == null || rol == null || !"administrador".equalsIgnoreCase(rol)) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }

    // Parámetros y atributos recibidos
    String idEstudiante = request.getParameter("id");
    String nombreEstudiante = (String) request.getAttribute("nombreEstudiante");
    List<Map<String, String>> historial = (List<Map<String, String>>) request.getAttribute("historial");
%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Historial académico - SymphonySIAS</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/estilos.css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
    <style>
        body {
            font-family: 'Poppins', sans-serif;
            background-color: #f4f6f9;
            padding: 30px;
        }
        .tabla-box {
            background: #ffffff;
            padding: 30px;
            border-radius: 12px;
            box-shadow: 0 4px 12px rgba(0,0,0,0.06);
            max-width: 100%;
            margin: 0 auto;
        }
        .table th, .table td {
            vertical-align: middle;
        }
    </style>
</head>
<body>

<jsp:include page="../fragmentos/header.jsp" />

<div class="tabla-box">
    <h4 class="text-center mb-4"><i class="fas fa-graduation-cap"></i> Historial académico del estudiante</h4>

    <div class="mb-4">
        <p><strong>👤 Nombre:</strong> <%= nombreEstudiante %></p>
        <p><strong>🆔 ID:</strong> <%= idEstudiante %></p>
    </div>

    <% if (historial == null || historial.isEmpty()) { %>
        <div class="alert alert-warning text-center">
            ⚠️ No hay registros académicos disponibles para este estudiante.
        </div>
    <% } else { %>
        <div class="table-responsive">
            <table class="table table-hover table-bordered">
                <thead class="table-secondary text-center">
                    <tr>
                        <th>Asignatura</th>
                        <th>Periodo</th>
                        <th>Nota</th>
                        <th>Observaciones</th>
                    </tr>
                </thead>
                <tbody>
                    <% for (Map<String, String> fila : historial) { %>
                        <tr>
                            <td><%= fila.get("asignatura") %></td>
                            <td><%= fila.get("periodo") %></td>
                            <td><%= fila.get("nota") %></td>
                            <td><%= fila.get("observacion") %></td>
                        </tr>
                    <% } %>
                </tbody>
            </table>
        </div>
    <% } %>

    <div class="mt-4 text-end">
        <a href="<%= request.getContextPath() %>/PanelAdministradorServlet" class="btn btn-secondary">
            <i class="fas fa-arrow-left"></i> Volver al panel
        </a>
    </div>
</div>

<jsp:include page="../fragmentos/footer.jsp" />
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>

</body>
</html>