<%--  
    Document   : listadoEstudiantes.jsp  
    Autor      : Camila  
    Propósito  : Vista institucional para mostrar estudiantes inscritos en una clase del docente  
    Ubicación  : /webapp/docente/listadoEstudiantes.jsp  
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" session="true" %>
<%@ page import="java.util.List, java.util.Map" %>

<%
    String nombre = (String) session.getAttribute("nombreActivo");
    String rol = (String) session.getAttribute("rolActivo");

    if (nombre == null || rol == null || !"docente".equalsIgnoreCase(rol)) {
        request.setAttribute("mensaje", "❌ Acceso restringido: requiere rol docente.");
        request.setAttribute("tipoMensaje", "danger");
        request.getRequestDispatcher("/pages/error.jsp").forward(request, response);
        return;
    }

    List<Map<String, String>> estudiantes = (List<Map<String, String>>) request.getAttribute("estudiantes");
    Map<String, String> datosClase = (Map<String, String>) request.getAttribute("datosClase");

    String mensaje = (String) request.getAttribute("mensaje");
    String tipoMensaje = (String) request.getAttribute("tipoMensaje");
%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Listado de estudiantes | SymphonySIAS</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/estilos.css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
</head>
<body class="bg-light">

    <!-- 🧭 Encabezado institucional -->
    <jsp:include page="../fragmentos/header.jsp" />

    <div class="container mt-5">
        <h3 class="mb-3">
            <i class="fas fa-users"></i> Estudiantes inscritos
            <small class="text-muted">Docente: <%= nombre %></small>
        </h3>

        <!-- 📌 Información de la clase -->
        <% if (datosClase != null) { %>
            <div class="alert alert-info">
                <strong>Clase:</strong> <%= datosClase.get("nombre") %> |
                <strong>Instrumento:</strong> <%= datosClase.get("instrumento") %> |
                <strong>Etapa:</strong> <%= datosClase.get("etapa") %> |
                <strong>Grupo:</strong> <%= datosClase.get("grupo") %> |
                <strong>Aula:</strong> <%= datosClase.get("aula") %> |
                <strong>Día:</strong> <%= datosClase.get("dia") %> |
                <strong>Horario:</strong> <%= datosClase.get("inicio") %> - <%= datosClase.get("fin") %>
            </div>
        <% } %>

        <% if (mensaje != null) { %>
            <div class="alert alert-<%= (tipoMensaje != null ? tipoMensaje : "info") %>"><%= mensaje %></div>
        <% } %>

        <% if (estudiantes != null && !estudiantes.isEmpty()) { %>
            <div class="table-responsive">
                <table class="table table-bordered table-hover align-middle">
                    <thead class="table-light text-center">
                        <tr>
                            <th>ID</th>
                            <th>Nombre</th>
                            <th>Correo</th>
                            <th>Fecha inscripción</th>
                            <th>Acciones</th>
                        </tr>
                    </thead>
                    <tbody>
                    <% for (Map<String, String> est : estudiantes) { %>
                        <tr>
                            <td><%= est.get("id") %></td>
                            <td><%= est.get("nombre") %></td>
                            <td><%= est.get("correo") %></td>
                            <td><%= est.get("fecha_inscripcion") %></td>
                            <td class="text-center">
                                <a href="<%= request.getContextPath() %>/RegistrarNotaClaseServlet?claseId=<%= request.getParameter("claseId") %>&estudianteId=<%= est.get("id") %>"
                                   class="btn btn-primary btn-sm">
                                   <i class="fas fa-pen"></i> Registrar notas
                                </a>
                            </td>
                        </tr>
                    <% } %>
                    </tbody>
                </table>
            </div>
        <% } else { %>
            <div class="alert alert-warning">⚠️ No hay estudiantes inscritos en esta clase.</div>
        <% } %>

        <div class="text-end mt-4">
            <a href="<%= request.getContextPath() %>/VerClasesDocenteServlet" class="btn btn-secondary">
                <i class="fas fa-arrow-left"></i> Volver a Mis clases
            </a>
        </div>
    </div>

    <!-- 📌 Pie de página -->
    <jsp:include page="../fragmentos/footer.jsp" />

</body>
</html>