<%-- 
    Document   : cargarNotas
    Created on : 24/11/2025, 9:14:23 p. m.
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
    String mensaje = (String) session.getAttribute("mensaje");
    session.removeAttribute("mensaje");

    int claseId = Integer.parseInt(request.getParameter("claseId"));
    int tablaId = Integer.parseInt(request.getParameter("tablaId"));
%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Registrar notas por clase - SymphonySIAS</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css" rel="stylesheet">
    <style>
        body { font-family: 'Poppins', sans-serif; background-color: #f4f6f9; }
        .dashboard-box { background: #fff; padding: 25px; border-radius: 12px; box-shadow: 0 4px 12px rgba(0,0,0,0.08); margin: 30px auto; max-width: 1100px; }
        .dashboard-title { font-size: 1.6rem; font-weight: 600; color: #198754; }
        .btn-guardar { background-color: #198754; color: #fff; font-weight: 600; }
        .btn-guardar:hover { background-color: #157347; }
    </style>
</head>
<body>

<jsp:include page="../fragmentos/sidebar.jsp" />
<jsp:include page="../fragmentos/header.jsp" />

<div class="container dashboard-box">
    <div class="d-flex justify-content-between align-items-center mb-4">
        <div>
            <div class="dashboard-title"><i class="fas fa-pen"></i> Registrar notas por clase</div>
            <div><strong><%= nombre %></strong> (docente)</div>
        </div>
    </div>

    <% if (mensaje != null) { %>
        <div class="alert alert-info text-center"><%= mensaje %></div>
    <% } %>

    <% if (estudiantes == null || estudiantes.isEmpty()) { %>
        <div class="alert alert-warning text-center">
            <i class="fas fa-user-slash"></i> No hay estudiantes inscritos en esta clase.
        </div>
    <% } else { %>
        <form action="<%= request.getContextPath() %>/RegistrarNotaClaseServlet" method="post">
            <input type="hidden" name="claseId" value="<%= claseId %>">
            <input type="hidden" name="tablaId" value="<%= tablaId %>">

            <div class="mb-3">
                <label class="form-label">Nombre de la tabla institucional</label>
                <input type="text" name="nombreTabla" class="form-control" required>
            </div>
            <div class="mb-3">
                <label class="form-label">Descripción de la tabla</label>
                <textarea name="descripcionTabla" class="form-control" rows="2"></textarea>
            </div>

            <div class="table-responsive mt-4">
                <table class="table table-bordered table-hover align-middle text-center">
                    <thead class="table-success">
                        <tr>
                            <th>Estudiante</th>
                            <th>Etapa</th>
                            <th>Instrumento</th>
                            <th>Nota (0-5)</th>
                            <th>Fecha</th>
                            <th>Observación</th>
                        </tr>
                    </thead>
                    <tbody>
                        <% for (Map<String, String> est : estudiantes) { %>
                            <tr>
                                <td><%= est.get("nombre") %></td>
                                <td>
                                    <input type="text" name="etapa_<%= est.get("id_estudiante") %>" class="form-control" required>
                                </td>
                                <td>
                                    <input type="text" name="instrumento_<%= est.get("id_estudiante") %>" class="form-control" required>
                                </td>
                                <td>
                                    <input type="number" step="0.1" min="0" max="5" name="nota_<%= est.get("id_estudiante") %>" class="form-control" required>
                                </td>
                                <td>
                                    <input type="date" name="fecha_<%= est.get("id_estudiante") %>" class="form-control" required>
                                </td>
                                <td>
                                    <input type="text" name="obs_<%= est.get("id_estudiante") %>" class="form-control">
                                </td>
                            </tr>
                        <% } %>
                    </tbody>
                </table>
            </div>

            <div class="text-center mt-4">
                <button type="submit" class="btn btn-guardar"><i class="fas fa-save"></i> Guardar notas</button>
            </div>
        </form>
    <% } %>

    <div class="mt-4 text-start">
        <a href="<%= request.getContextPath() %>/PanelDocenteServlet" class="btn btn-secondary">
            <i class="fas fa-arrow-left"></i> Volver al panel docente
        </a>
    </div>
</div>

<jsp:include page="../fragmentos/footer.jsp" />

</body>
</html>