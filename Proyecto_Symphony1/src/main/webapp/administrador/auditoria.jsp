<%-- 
    Document   : auditoria
    Created on : 14/11/2025, 8:36:39 p. m.
    Author     : camiv
    Descripción: Vista institucional del rol administrador para consultar registros de auditoría.
                 Requiere atributo 'auditoria' como lista de mapas con claves: usuario, rol, módulo, acción, fecha.
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" session="true" %>
<%@ page import="java.util.List, java.util.Map" %>

<%
    // 🔐 Validación de sesión y rol
    String nombre = (String) session.getAttribute("nombreActivo");
    String rol = (String) session.getAttribute("rolActivo");

    if (nombre == null || rol == null || !"administrador".equalsIgnoreCase(rol)) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }

    // 📥 Datos recibidos desde el servlet
    List<Map<String, String>> auditoria = (List<Map<String, String>>) request.getAttribute("auditoria");
    String mensaje = (String) request.getAttribute("mensaje");

    // 📦 Filtros recibidos
    String filtroUsuario = request.getParameter("usuario") != null ? request.getParameter("usuario") : "";
    String filtroModulo = request.getParameter("modulo") != null ? request.getParameter("modulo") : "";
    String filtroInicio = request.getParameter("fechaInicio") != null ? request.getParameter("fechaInicio") : "";
    String filtroFin = request.getParameter("fechaFin") != null ? request.getParameter("fechaFin") : "";
%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Auditoría institucional - SymphonySIAS</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/estilos.css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
    <style>
        /* 🎨 Scroll para tabla de auditoría */
        .scroll-auditoria {
            max-height: 400px; /* altura fija */
            overflow-y: auto;  /* activa scroll vertical */
        }
    </style>
</head>
<body>

<!-- 🧭 Encabezado institucional -->
<jsp:include page="../fragmentos/header.jsp" />

<!-- 📋 Contenedor principal -->
<div class="container mt-4">
    <h4 class="mb-3 text-center"><i class="fas fa-shield-alt"></i> Auditoría institucional</h4>
    <p class="text-center text-muted">Consulta de acciones registradas por los usuarios del sistema.</p>

    <!-- 🔍 Filtros de búsqueda -->
    <form method="get" action="<%= request.getContextPath() %>/FiltrarAuditoriaServlet" class="row g-3 mb-4 justify-content-center">
        <div class="col-md-3">
            <input type="text" name="usuario" class="form-control" placeholder="Filtrar por usuario" value="<%= filtroUsuario %>">
        </div>
        <div class="col-md-3">
            <input type="text" name="modulo" class="form-control" placeholder="Filtrar por módulo" value="<%= filtroModulo %>">
        </div>
        <div class="col-md-3">
            <input type="date" name="fechaInicio" class="form-control" value="<%= filtroInicio %>">
        </div>
        <div class="col-md-3">
            <input type="date" name="fechaFin" class="form-control" value="<%= filtroFin %>">
        </div>
        <div class="col-md-2">
            <button type="submit" class="btn btn-outline-primary w-100">
                <i class="fas fa-filter"></i> Aplicar filtros
            </button>
        </div>
    </form>

    <!-- 📢 Mensaje institucional si existe -->
    <% if (mensaje != null) { %>
        <div class="alert alert-info text-center"><%= mensaje %></div>
    <% } else if (auditoria == null || auditoria.isEmpty()) { %>
        <div class="alert alert-warning text-center">
            <i class="fas fa-exclamation-circle"></i> No hay registros de auditoría disponibles.
        </div>
    <% } else { %>

        <!-- 📑 Tabla de registros de auditoría con scroll -->
        <div class="table-responsive mt-4 scroll-auditoria">
            <table class="table table-bordered table-hover tabla-auditoria align-middle">
                <thead class="text-center table-success">
                    <tr>
                        <th>Usuario</th>
                        <th>Rol</th>
                        <th>Módulo</th>
                        <th>Acción</th>
                        <th>Detalle</th>
                        <th>Fecha</th>
                    </tr>
                </thead>
                <tbody>
                    <% for (Map<String, String> registro : auditoria) { %>
                        <tr>
                            <td><%= registro.get("usuario") %></td>
                            <td class="text-capitalize"><%= registro.get("rol") %></td>
                            <td><%= registro.get("modulo") %></td>
                            <td><%= registro.get("accion") %></td>
                            <td><%= registro.get("detalle") %></td>
                            <td class="text-center"><%= registro.get("fecha") %></td>
                        </tr>
                    <% } %>
                </tbody>
            </table>
        </div>
    <% } %>

    <!-- 🔘 Botones institucionales -->
    <div class="mt-4 d-flex justify-content-between">
        <!-- Botón de regreso al panel -->
        <a href="<%= request.getContextPath() %>/PanelAdministradorServlet" class="btn btn-outline-secondary">
            <i class="fas fa-arrow-left"></i> Volver al panel
        </a>

        <!-- Botón limpiar auditoría con confirmación -->
        <form method="post" action="<%= request.getContextPath() %>/LimpiarAuditoriaServlet" style="display:inline;">
            <button type="submit" class="btn btn-outline-danger"
                    onclick="return confirm('¿Estás seguro de limpiar la auditoría?');">
                <i class="fas fa-trash"></i> Limpiar auditoría
            </button>
        </form>
    </div>
</div>

<!-- 📌 Pie de página -->
<jsp:include page="../fragmentos/footer.jsp" />

</body>
</html>