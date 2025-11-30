<%-- 
    Document   : listarInscripciones
    Created on : 24/11/2025, 3:16:34 a. m.
    Author     : camiv
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" session="true" %>
<%@ page import="java.util.List" %>
<%@ page import="com.mysymphony.proyecto_symphony1.modelo.Inscripcion" %>

<%
    // 🔐 Validación de sesión y rol
    String nombre = (String) session.getAttribute("nombreActivo");
    String rol = (String) session.getAttribute("rolActivo");

    if (nombre == null || rol == null || !"administrador".equalsIgnoreCase(rol)) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }

    // 📥 Datos recibidos desde el servlet
    List<Inscripcion> inscripciones = (List<Inscripcion>) request.getAttribute("inscripciones");
    String error = (String) request.getAttribute("error");
%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Listado de inscripciones | SymphonySIAS</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/estilos.css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
</head>
<body>

<jsp:include page="../fragmentos/header.jsp" />

<div class="container mt-5">
    <h3 class="text-center mb-4"><i class="fas fa-user-check"></i> Listado de inscripciones</h3>

    <%-- 🧾 Mensaje de error si existe --%>
    <% if (error != null) { %>
        <div class="alert alert-danger text-center"><%= error %></div>
    <% } %>

    <%-- 📅 Tabla de inscripciones --%>
    <% if (inscripciones != null && !inscripciones.isEmpty()) { %>
        <div class="table-responsive">
            <table class="table table-bordered text-center align-middle">
                <thead class="table-dark">
                    <tr>
                        <th>ID Inscripción</th>
                        <th>ID Estudiante</th>
                        <th>Nombre Estudiante</th>
                        <th>ID Clase</th>
                        <th>Nombre Clase</th>
                        <th>Fecha inscripción</th>
                        <th>Acción</th>
                    </tr>
                </thead>
                <tbody>
                    <% for (Inscripcion i : inscripciones) { %>
                        <tr>
                            <td><%= i.getId() %></td>
                            <td><%= i.getIdEstudiante() %></td>
                            <td><%= i.getNombreEstudiante() %></td>
                            <td><%= i.getClaseId() %></td>
                            <td><%= i.getNombreClase() %></td>
                            <td><%= (i.getFechaInscripcion() != null ? i.getFechaInscripcion().toString() : "") %></td>
                            <td>
                                <form method="post" action="<%= request.getContextPath() %>/EliminarInscripcionServlet">
                                    <input type="hidden" name="id_inscripcion" value="<%= i.getId() %>">
                                    <button type="submit" class="btn btn-danger btn-sm">
                                        <i class="fas fa-trash-alt"></i> Eliminar
                                    </button>
                                </form>
                            </td>
                        </tr>
                    <% } %>
                </tbody>
            </table>
        </div>
    <% } else { %>
        <div class="alert alert-warning text-center">No hay inscripciones registradas.</div>
    <% } %>

    <%-- 🔙 Botón volver al panel --%>
    <div class="text-end mt-4">
        <a href="<%= request.getContextPath() %>/panelAdministrador.jsp" class="btn btn-secondary">
            <i class="fas fa-arrow-left"></i> Volver al panel
        </a>
    </div>
</div>

<jsp:include page="../fragmentos/footer.jsp" />

</body>
</html>