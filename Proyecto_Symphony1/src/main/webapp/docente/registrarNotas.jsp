<%-- 
    Document   : registrarNotas
    Created on : 14/11/2025, 10:32:08 p. m.
    Author     : camiv
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" session="true" %>
<%@ page import="java.util.*, java.text.SimpleDateFormat" %>
<%
    String nombre = (String) session.getAttribute("nombreActivo");
    String rol = (String) session.getAttribute("rolActivo");

    if (nombre == null || rol == null || !"docente".equalsIgnoreCase(rol)) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }

    List<Map<String, String>> estudiantes = (List<Map<String, String>>) request.getAttribute("estudiantes");
    String fechaActual = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Registrar calificaciones - SymphonySIAS</title>
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
        .btn-volver {
            background-color: #6c757d;
            color: white;
            padding: 10px 20px;
            border-radius: 6px;
            text-decoration: none;
            font-weight: 500;
        }
        .btn-volver:hover {
            background-color: #5a6268;
        }
        .table th, .table td {
            white-space: nowrap;
            vertical-align: middle;
        }
    </style>
</head>
<body>

<jsp:include page="../fragmentos/header.jsp" />

<div class="tabla-box">
    <h4 class="text-center mb-4">Registrar calificaciones</h4>

    <% if (request.getAttribute("mensaje") != null) { %>
        <div class="alert alert-info text-center">
            <%= request.getAttribute("mensaje") %>
        </div>
    <% } %>

    <div class="table-responsive">
        <table class="table table-bordered align-middle">
            <thead class="table-light">
                <tr>
                    <th>Estudiante</th>
                    <th>Instrumento</th>
                    <th>Etapa</th>
                    <th>Nota</th>
                    <th>Observaciones</th>
                    <th>Fecha</th>
                    <th>Guardar</th>
                </tr>
            </thead>
            <tbody>
                <% if (estudiantes != null && !estudiantes.isEmpty()) {
                       for (Map<String, String> est : estudiantes) { %>
                    <tr>
                        <td><%= est.get("nombre") %></td>
                        <td><%= est.get("instrumento") %></td>
                        <td><%= est.get("etapa") %></td>
                        <td>
                            <form method="post" action="<%= request.getContextPath() %>/RegistrarNotaServlet">
                                <input type="number" name="nota" step="0.1" min="0" max="5" class="form-control" required>
                        </td>
                        <td>
                                <input type="text" name="obs" class="form-control">
                        </td>
                        <td>
                                <input type="hidden" name="fecha" value="<%= fechaActual %>">
                                <%= fechaActual %>
                        </td>
                        <td>
                                <input type="hidden" name="idEstudiante" value="<%= est.get("id") %>">
                                <input type="hidden" name="instrumento" value="<%= est.get("instrumento") %>">
                                <input type="hidden" name="etapa" value="<%= est.get("etapa") %>">
                                <button type="submit" class="btn btn-primary btn-sm">
                                    <i class="fas fa-save"></i> Guardar
                                </button>
                            </form>
                        </td>
                    </tr>
                <% } } else { %>
                    <tr>
                        <td colspan="7" class="text-center text-muted">No hay estudiantes asignados.</td>
                    </tr>
                <% } %>
            </tbody>
        </table>
    </div>

    <div class="mt-4 text-start">
        <a href="<%= request.getContextPath() %>/DashboardServlet" class="btn-volver">
            <i class="fas fa-arrow-left"></i> Volver al panel docente
        </a>
    </div>
</div>

<jsp:include page="../fragmentos/footer.jsp" />

</body>
</html>