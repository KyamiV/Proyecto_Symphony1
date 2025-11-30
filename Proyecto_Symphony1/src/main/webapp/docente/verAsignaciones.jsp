<%-- 
    Document   : verAsignaciones
    Created on : 15/11/2025, 7:08:46 p. m.
    Author     : camiv
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.*" %>

<%
    HttpSession sesion = request.getSession();
    String rol = (String) sesion.getAttribute("rolActivo");
    String nombreDocente = (String) sesion.getAttribute("nombreActivo");

    if (rol == null || !"docente".equalsIgnoreCase(rol) || nombreDocente == null) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }

    List<Map<String, String>> asignaciones = (List<Map<String, String>>) request.getAttribute("asignaciones");
    String mensaje = (String) request.getAttribute("mensaje");
%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Estudiantes asignados | SymphonySIAS</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/estilos.css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
    <link rel="stylesheet" href="https://cdn.datatables.net/1.13.6/css/dataTables.bootstrap5.min.css">
</head>
<body class="bg-light">

<jsp:include page="../fragmentos/header.jsp" />

<div class="container mt-5">
    <h3 class="text-center mb-4"><i class="fas fa-users"></i> Estudiantes asignados a <strong><%= nombreDocente %></strong></h3>

    <% if (mensaje != null) { %>
        <div class="alert alert-info text-center"><%= mensaje %></div>
    <% } %>

    <% if (asignaciones == null || asignaciones.isEmpty()) { %>
        <div class="alert alert-warning text-center">
            No tienes estudiantes asignados aún. El administrador debe realizar la asignación.
        </div>
    <% } else { %>
        <div class="table-responsive mb-4">
            <table id="tablaAsignaciones" class="table table-bordered table-hover align-middle">
                <thead class="table-primary text-center">
                    <tr>
                        <th>👤 Estudiante</th>
                        <th>🎼 Instrumento</th>
                        <th>📅 Fecha</th>
                        <th>⚙️ Acción</th>
                    </tr>
                </thead>
                <tbody>
                    <% for (Map<String, String> fila : asignaciones) { %>
                        <tr>
                            <td><%= fila.get("estudiante") %></td>
                            <td><%= fila.get("instrumento") %></td>
                            <td><%= fila.get("fecha") %></td>
                            <td class="text-center">
                                <form action="<%= request.getContextPath() %>/docente/editarAsignacion.jsp" method="get" style="display:inline-block;">
                                    <input type="hidden" name="id" value="<%= fila.get("id") %>">
                                    <input type="hidden" name="nombre" value="<%= fila.get("estudiante") %>">
                                    <input type="hidden" name="instrumento" value="<%= fila.get("instrumento") %>">
                                    <button type="submit" class="btn btn-outline-primary btn-sm">
                                        <i class="fas fa-edit"></i> Editar
                                    </button>
                                </form>
                                <form action="<%= request.getContextPath() %>/EliminarAsignacionServlet" method="post" style="display:inline-block;" onsubmit="return confirm('¿Eliminar esta asignación?');">
                                    <input type="hidden" name="id" value="<%= fila.get("id") %>">
                                    <button type="submit" class="btn btn-outline-danger btn-sm">
                                        <i class="fas fa-trash-alt"></i> Eliminar
                                    </button>
                                </form>
                            </td>
                        </tr>
                    <% } %>
                </tbody>
            </table>
        </div>
    <% } %>

    <div class="text-end">
        <a href="<%= request.getContextPath() %>/LoginServlet" class="btn btn-secondary">
            <i class="fas fa-arrow-left"></i> Volver al panel
        </a>
    </div>
</div>

<jsp:include page="../fragmentos/footer.jsp" />

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
<script src="https://cdn.datatables.net/1.13.6/js/jquery.dataTables.min.js"></script>
<script src="https://cdn.datatables.net/1.13.6/js/dataTables.bootstrap5.min.js"></script>
<script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
<script>
    $(document).ready(function () {
        $('#tablaAsignaciones').DataTable({
            language: {
                url: 'https://cdn.datatables.net/plug-ins/1.13.6/i18n/es-ES.json'
            }
        });
    });
</script>
</body>
</html>