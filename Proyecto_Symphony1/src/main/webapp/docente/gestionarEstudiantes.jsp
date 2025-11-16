<%-- 
    Document   : gestionarEstudiantes
    Created on : 15/11/2025, 6:34:50 p. m.
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

    List<Map<String, String>> estudiantes = (List<Map<String, String>>) request.getAttribute("estudiantes");
    String mensaje = (String) request.getAttribute("mensaje");
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Gestión de estudiantes - SymphonySIAS</title>
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
        .tabla-box {
            background: #ffffff;
            padding: 25px;
            border-radius: 10px;
            box-shadow: 0 0 10px rgba(0,0,0,0.08);
            max-width: 100%;
            margin: 0 auto;
        }
        .btn-editar {
            background-color: #007bff;
            color: white;
            padding: 6px 12px;
            border-radius: 6px;
            font-size: 14px;
            text-decoration: none;
        }
        .btn-editar:hover {
            background-color: #0056b3;
        }
        .btn-registrar {
            background-color: #28a745;
            color: white;
            padding: 8px 16px;
            border-radius: 6px;
            font-weight: 500;
            text-decoration: none;
        }
        .btn-registrar:hover {
            background-color: #218838;
        }
    </style>
</head>
<body>

<jsp:include page="../fragmentos/header.jsp" />

<div class="tabla-box">
    <h4 class="text-center mb-4"><i class="fas fa-users"></i> Gestión de estudiantes</h4>

    <% if (mensaje != null) { %>
        <div class="alert alert-info text-center">
            <%= mensaje %>
        </div>
    <% } %>

    <div class="mb-3 text-end">
        <a href="<%= request.getContextPath() %>/RegistrarEstudianteServlet" class="btn-registrar">
            <i class="fas fa-user-plus"></i> Registrar nuevo estudiante
        </a>
    </div>

    <div class="table-responsive">
        <table class="table table-bordered table-striped align-middle">
            <thead class="table-dark">
                <tr>
                    <th>Nombre</th>
                    <th>Instrumento</th>
                    <th>Nivel técnico</th>
                    <th>Etapa pedagógica</th>
                    <th>Acciones</th>
                </tr>
            </thead>
            <tbody>
                <% if (estudiantes != null && !estudiantes.isEmpty()) {
                       for (Map<String, String> est : estudiantes) { %>
                    <tr>
                        <td><%= est.get("nombre") %></td>
                        <td><%= est.get("instrumento") != null ? est.get("instrumento") : "No asignado" %></td>
                        <td><%= est.get("nivel") != null ? est.get("nivel") : "No definido" %></td>
                        <td><%= est.get("etapa") != null ? est.get("etapa") : "No definida" %></td>
                        <td>
                            <a href="<%= request.getContextPath() %>/EditarEstudianteServlet?id=<%= est.get("id") %>" class="btn-editar">
                                <i class="fas fa-edit"></i> Editar
                            </a>
                        </td>
                    </tr>
                <% } } else { %>
                    <tr>
                        <td colspan="5" class="text-center text-muted">No hay estudiantes registrados con rol estudiante.</td>
                    </tr>
                <% } %>
            </tbody>
        </table>
    </div>

    <div class="mt-4 text-start">
        <a href="<%= request.getContextPath() %>/DashboardServlet" class="btn btn-secondary">
            <i class="fas fa-arrow-left"></i> Volver al panel docente
        </a>
    </div>
</div>

<jsp:include page="../fragmentos/footer.jsp" />

</body>
</html>