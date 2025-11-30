<%-- 
    Document   : verInscritosClase
    Created on : 19/11/2025, 11:09:32 a. m.
    Author     : camiv
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" session="true" %>
<%@ page import="java.util.*" %>

<%
    HttpSession sesion = request.getSession(false);
    String nombre = (sesion != null) ? (String) sesion.getAttribute("nombreActivo") : null;
    String rol = (sesion != null) ? (String) sesion.getAttribute("rolActivo") : null;

    if (nombre == null || rol == null || !"administrador".equalsIgnoreCase(rol)) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }

    List<Map<String, String>> inscritos = (List<Map<String, String>>) request.getAttribute("inscritos");
    String claseId = (String) request.getAttribute("claseId");
%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Estudiantes inscritos | SymphonySIAS</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/estilos.css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
</head>
<body>

<jsp:include page="../fragmentos/header.jsp" />

<div class="container mt-5">
    <h3>
        <i class="fas fa-users"></i> 
        Estudiantes inscritos en <%= request.getAttribute("claseNombre") %> (ID <%= claseId %>)
    </h3>

    <% String mensaje = (String) request.getAttribute("mensaje"); %>
    <% if (mensaje != null) { %>
        <div class="alert alert-warning text-center mt-3"><%= mensaje %></div>
    <% } %>

    <div class="table-responsive mt-4">
        <table class="table table-bordered table-striped table-hover table-sm align-middle">
            <thead class="table-dark text-center">
                <tr>
                    <th>ID</th>
                    <th>Nombre</th>
                    <th>Correo</th>
                    <th>Teléfono</th>
                    <!-- Ejemplo de columna extra -->
                    <%-- <th>Estado académico</th> --%>
                </tr>
            </thead>
            <tbody>
                <% if (inscritos == null || inscritos.isEmpty()) { %>
                    <tr>
                        <td colspan="4" class="text-center text-muted">
                            No hay estudiantes inscritos en esta clase.
                        </td>
                    </tr>
                <% } else {
                    for (Map<String, String> estudiante : inscritos) { %>
                        <tr>
                            <td><%= estudiante.get("id") %></td>
                            <td><%= estudiante.get("nombre") %></td>
                            <td><%= estudiante.get("correo") %></td>
                            <td><%= estudiante.get("telefono") %></td>
                            <%-- <td><%= estudiante.get("estado") %></td> --%>
                        </tr>
                    <% }
                } %>
            </tbody>
        </table>
    </div>

    <div class="mt-4 text-end">
        <a href="<%= request.getContextPath() %>/GestionarClasesServlet" class="btn btn-secondary">
            <i class="fas fa-arrow-left"></i> Volver a clases
        </a>
    </div>
</div>

<jsp:include page="../fragmentos/footer.jsp" />

</body>
</html>