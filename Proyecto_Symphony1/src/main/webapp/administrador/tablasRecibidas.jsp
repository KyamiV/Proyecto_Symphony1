<%-- 
    Document   : tablasRecibidas
    Created on : 17/11/2025, 1:45:38 p. m.
    Author     : camiv
    Descripción: Vista institucional del rol administrador para consultar tablas enviadas por docentes.
                 Requiere atributo 'tablasRecibidas' como lista de mapas con datos clave.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" session="true" %>
<%@ page import="java.util.*" %>

<%
    String nombre = (String) session.getAttribute("nombreActivo");
    String rol = (String) session.getAttribute("rolActivo");

    if (nombre == null || rol == null || !"administrador".equalsIgnoreCase(rol)) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }

    List<Map<String, String>> tablasRecibidas = (List<Map<String, String>>) request.getAttribute("tablasRecibidas");
    String mensaje = (String) request.getAttribute("mensaje");
%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Tablas recibidas - SymphonySIAS</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/estilos.css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;600&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
    <style>
        body { font-family: 'Poppins', sans-serif; background-color: #f4f6f9; padding: 30px; }
        .tabla-box { background: #ffffff; padding: 25px; border-radius: 10px; box-shadow: 0 0 10px rgba(0,0,0,0.08); }
        .table th, .table td { vertical-align: middle; text-align: center; }
    </style>
</head>
<body>

    <!-- 🔹 Encabezado institucional -->
    <jsp:include page="../fragmentos/header.jsp" />

    <div class="tabla-box">
        <h4 class="text-center mb-4"><i class="fas fa-inbox"></i> Tablas recibidas de docentes</h4>

        <% if (mensaje != null) { %>
            <div class="alert alert-info text-center"><%= mensaje %></div>
        <% } %>

        <% if (tablasRecibidas == null || tablasRecibidas.isEmpty()) { %>
            <div class="alert alert-warning text-center">
                ⚠️ No hay tablas enviadas por docentes.
            </div>
        <% } else { %>
            <div class="table-responsive">
                <table class="table table-bordered table-striped">
                    <thead class="table-dark">
                        <tr>
                            <th>ID</th>
                            <th>Nombre tabla</th>
                            <th>Descripción</th>
                            <th>Docente</th>
                            <th>Fecha de envío</th>
                            <th>Acciones</th>
                        </tr>
                    </thead>
                    <tbody>
                        <% for (Map<String, String> tabla : tablasRecibidas) { %>
                        <tr>
                            <td><%= tabla.get("id") %></td>
                            <td><%= tabla.get("nombre") %></td>
                            <td><%= tabla.get("descripcion") %></td>
                            <td><i class="fas fa-user-tie text-secondary me-1"></i> <%= tabla.get("docente") %></td>
                            <td><%= tabla.get("fecha_envio") %></td>
                            <td>
                                <!-- Ver notas -->
                                <a href="<%= request.getContextPath() %>/VerNotasRecibidasServlet?tablaId=<%= tabla.get("id") %>" 
                                   class="btn btn-sm btn-info">
                                   <i class="fas fa-eye"></i> Ver notas
                                </a>
                                <!-- Exportar -->
                                <a href="<%= request.getContextPath() %>/ExportarTablaNotasServlet?tablaId=<%= tabla.get("id") %>" 
                                   class="btn btn-sm btn-outline-secondary">
                                   <i class="fas fa-file-export"></i> Exportar
                                </a>
                                <!-- Validar -->
                                <a href="<%= request.getContextPath() %>/ValidarTablaServlet?tablaId=<%= tabla.get("id") %>" 
                                   class="btn btn-sm btn-success"
                                   onclick="return confirm('¿Deseas validar esta tabla como revisada?');">
                                   <i class="fas fa-check-circle"></i> Validar
                                </a>
                            </td>
                        </tr>
                        <% } %>
                    </tbody>
                </table>
            </div>
        <% } %>

        <!-- 🔙 Botón de regreso -->
        <div class="mt-4 text-start">
            <a href="<%= request.getContextPath() %>/PanelAdminServlet" class="btn btn-outline-primary">
                <i class="fas fa-arrow-left"></i> Volver al panel administrador
            </a>
        </div>
    </div>

    <!-- 🔹 Pie institucional -->
    <jsp:include page="../fragmentos/footer.jsp" />

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>