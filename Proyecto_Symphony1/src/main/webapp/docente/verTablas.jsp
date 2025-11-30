<%-- 
    Document   : verTablasEnviadas
    Created on : 29/11/2025, 9:26:08 p. m.
    Author     : camiv
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List, java.util.Map" %>

<%
    String nombre = (String) session.getAttribute("nombreActivo");
    String rol = (String) session.getAttribute("rolActivo");

    if (nombre == null || rol == null || !"docente".equalsIgnoreCase(rol)) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }

    List<Map<String,String>> tablas = (List<Map<String,String>>) request.getAttribute("tablas");
    String mensaje = (String) session.getAttribute("mensaje");
    session.removeAttribute("mensaje");
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Tablas de notas - SymphonySIAS</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/estilos.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
    <style>
        body { font-family: 'Poppins', sans-serif; background-color: #f4f6f9; padding: 30px; }
        .dashboard-box { background: #ffffff; padding: 25px; border-radius: 10px; box-shadow: 0 0 10px rgba(0,0,0,0.08); }
        .tabla-tablas th, .tabla-tablas td { vertical-align: middle; text-align: center; }
    </style>
</head>
<body>

<jsp:include page="../fragmentos/header.jsp" />

<div class="dashboard-box">
    <h4 class="text-center mb-4"><i class="fas fa-table"></i> Tablas institucionales de notas</h4>

    <% if (mensaje != null) { %>
        <div class="alert alert-info text-center" style="max-width: 700px; margin: 0 auto;">
            <%= mensaje %>
        </div>
    <% } %>

    <div class="table-responsive">
        <table class="table table-bordered table-striped tabla-tablas">
            <thead class="table-dark">
                <tr>
                    <th>Clase</th>
                    <th>Nombre tabla</th>
                    <th>Descripción</th>
                    <th>Fecha</th>
                    <th>Estado</th>
                    <th>Acciones</th>
                </tr>
            </thead>
            <tbody>
                <% if (tablas != null && !tablas.isEmpty()) {
                       for (Map<String,String> tabla : tablas) { %>
                <tr>
                    <td><%= tabla.get("clase") %></td>
                    <td><%= tabla.get("nombre") %></td>
                    <td><%= tabla.get("descripcion") %></td>
                    <td><%= tabla.get("fecha") %></td>
                    <td>
                        <% if ("Sí".equals(tabla.get("enviada"))) { %>
                            <span class="badge bg-success">Enviada</span>
                        <% } else { %>
                            <span class="badge bg-warning text-dark">Pendiente</span>
                        <% } %>
                    </td>
                    <td>
                        <!-- Botón Ver notas -->
                        <a href="<%= request.getContextPath() %>/VerNotasPorTablaServlet?tablaId=<%= tabla.get("id") %>" 
                           class="btn btn-sm btn-info">
                           <i class="fas fa-eye"></i> Ver notas
                        </a>
                        <!-- Botón Enviar al admin solo si está pendiente -->
                        <% if ("No".equals(tabla.get("enviada"))) { %>
                            <form action="<%= request.getContextPath() %>/EnviarNotasAdminServlet" method="post" style="display:inline;">
                                <input type="hidden" name="tablaId" value="<%= tabla.get("id") %>"/>
                                <button type="submit" class="btn btn-sm btn-primary">
                                    <i class="fas fa-paper-plane"></i> Enviar al admin
                                </button>
                            </form>
                        <% } %>
                        <!-- Botón Eliminar tabla -->
                        <form action="<%= request.getContextPath() %>/EliminarTablaServlet" method="post" style="display:inline;" 
                              onsubmit="return confirm('¿Está seguro de eliminar esta tabla?');">
                            <input type="hidden" name="tablaId" value="<%= tabla.get("id") %>"/>
                            <button type="submit" class="btn btn-sm btn-danger">
                                <i class="fas fa-trash"></i> Eliminar
                            </button>
                        </form>
                    </td>
                </tr>
                <% } } else { %>
                <tr>
                    <td colspan="6" class="text-center">No hay tablas registradas.</td>
                </tr>
                <% } %>
            </tbody>
        </table>
    </div>

    <div class="mt-4 text-start">
        <a href="<%= request.getContextPath() %>/PanelDocenteServlet" class="btn btn-outline-primary">
            <i class="fas fa-arrow-left"></i> Volver al panel docente
        </a>
    </div>
</div>

<jsp:include page="../fragmentos/footer.jsp" />

</body>
</html>