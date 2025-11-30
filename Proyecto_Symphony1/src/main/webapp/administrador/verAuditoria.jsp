<%-- 
    Document   : verAuditoria
    Created on : 17/11/2025, 5:54:44 p. m.
    Author     : camiv
    Descripción: Vista institucional del rol administrador para consultar registros de auditoría.
                 Incluye filtros, conteo por módulo, tabla dinámica y exportación a Excel.
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" session="true" %>
<%@ page import="java.util.*" %>

<%
    HttpSession sesion = request.getSession();
    String rol = (String) sesion.getAttribute("rolActivo");
    String nombreAdmin = (String) sesion.getAttribute("nombreActivo");

    if (rol == null || !"administrador".equalsIgnoreCase(rol)) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }

    List<Map<String, String>> registrosAuditoria = (List<Map<String, String>>) request.getAttribute("registrosAuditoria");
    String filtro = (String) request.getAttribute("filtro");
    String usuarioFiltro = (String) request.getAttribute("usuarioFiltro");
    String fechaInicio = (String) request.getAttribute("fechaInicio");
    String fechaFin = (String) request.getAttribute("fechaFin");
    Map<String, Integer> conteoPorModulo = (Map<String, Integer>) request.getAttribute("conteoPorModulo");
%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Auditoría institucional | SymphonySIAS</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/estilos.css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
    <link rel="stylesheet" href="https://cdn.datatables.net/1.13.6/css/dataTables.bootstrap5.min.css">
</head>
<body class="bg-light">

<jsp:include page="../fragmentos/header.jsp" />

<div class="container mt-5">
    <h3 class="text-center mb-4"><i class="fas fa-shield-alt"></i> Auditoría institucional</h3>

    <%-- Conteo por módulo --%>
    <% if (conteoPorModulo != null && !conteoPorModulo.isEmpty()) { %>
        <div class="mb-4">
            <h5>📊 Validaciones registradas por módulo:</h5>
            <ul class="list-group">
                <% for (Map.Entry<String, Integer> entry : conteoPorModulo.entrySet()) { %>
                    <li class="list-group-item d-flex justify-content-between align-items-center">
                        <%= entry.getKey() %>
                        <span class="badge bg-primary rounded-pill"><%= entry.getValue() %></span>
                    </li>
                <% } %>
            </ul>
        </div>
    <% } %>

    <%-- Formulario de filtros --%>
    <form action="<%= request.getContextPath() %>/VerAuditoriaServlet" method="get" class="row g-3 mb-4">
        <div class="col-md-4">
            <input type="text" name="filtro" class="form-control" placeholder="Buscar por palabra clave..." value="<%= (filtro != null) ? filtro : "" %>">
        </div>
        <div class="col-md-3">
            <input type="text" name="usuarioFiltro" class="form-control" placeholder="Filtrar por usuario..." value="<%= (usuarioFiltro != null) ? usuarioFiltro : "" %>">
        </div>
        <div class="col-md-2">
            <input type="date" name="fechaInicio" class="form-control" value="<%= (fechaInicio != null) ? fechaInicio : "" %>">
        </div>
        <div class="col-md-2">
            <input type="date" name="fechaFin" class="form-control" value="<%= (fechaFin != null) ? fechaFin : "" %>">
        </div>
        <div class="col-md-1 d-grid">
            <button type="submit" class="btn btn-primary"><i class="fas fa-filter"></i></button>
        </div>
    </form>

    <%-- Tabla de auditoría --%>
    <% if (registrosAuditoria == null || registrosAuditoria.isEmpty()) { %>
        <div class="alert alert-warning text-center">
            No se encontraron registros de auditoría.
        </div>
    <% } else { %>
        <div class="alert alert-info text-center">
            Se encontraron <strong><%= registrosAuditoria.size() %></strong> registros de auditoría.
        </div>

        <div class="text-end mb-3">
            <button id="btnExportarExcel" class="btn btn-success">
                <i class="fas fa-file-excel"></i> Exportar a Excel
            </button>
        </div>

        <div class="table-responsive mb-4">
            <table id="tablaAuditoria" class="table table-bordered table-hover align-middle">
                <thead class="table-success text-center">
                    <tr>
                        <th>👤 Usuario</th>
                        <th>🎭 Rol</th>
                        <th>🧩 Módulo</th>
                        <th>📝 Acción</th>
                        <th>🆔 Tabla</th>
                        <th>🔗 Referencia</th>
                        <th>🌐 IP</th>
                        <th>📅 Fecha</th>
                    </tr>
                </thead>
                <tbody>
                    <% for (Map<String, String> fila : registrosAuditoria) { %>
                        <tr>
                            <td><%= fila.get("usuario") %></td>
                            <td><%= fila.get("rol") != null ? fila.get("rol") : "—" %></td>
                            <td><%= fila.get("modulo") %></td>
                            <td><%= fila.get("accion") %></td>
                            <td><%= fila.get("tabla_id") != null ? fila.get("tabla_id") : "—" %></td>
                            <td><%= fila.get("referencia_id") != null ? fila.get("referencia_id") : "—" %></td>
                            <td><%= fila.get("ip_origen") != null ? fila.get("ip_origen") : "—" %></td>
                            <td><%= fila.get("fecha_hora") %></td>
                        </tr>
                    <% } %>
                </tbody>
            </table>
        </div>
    <% } %>

    <div class="text-end">
        <a href="<%= request.getContextPath() %>/PanelAdministradorServlet" class="btn btn-secondary">
            <i class="fas fa-arrow-left"></i> Volver al panel
        </a>
    </div>
</div>

<jsp:include page="../fragmentos/footer.jsp" />

<script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
<script src="https://cdn.datatables.net/1.13.6/js/jquery.dataTables.min.js"></script>
<script src="https://cdn.datatables.net/1.13.6/js/dataTables.bootstrap5.min.js"></script>
<script>
    $(document).ready(function () {
        $('#tablaAuditoria').DataTable({
            language: {
                url: 'https://cdn.datatables.net/plug-ins/1.13.6/i18n/es-ES.json'
            },
            order: [[7, 'desc']]
        });
    });

    document.getElementById("btnExportarExcel").addEventListener("click", function () {
        let tabla = document.getElementById("tablaAuditoria");
        let tablaHTML = tabla.outerHTML.replace(/ /g, '%20');

        let nombreArchivo = 'auditoria';
        if ('<%= filtro %>') nombreArchivo += '_filtro_' + '<%= filtro %>';
        if ('<%= usuarioFiltro %>') nombreArchivo += '_usuario_' + '<%= usuarioFiltro %>';
        if ('<%= fechaInicio %>' && '<%= fechaFin %>') nombreArchivo += '_rango_' + '<%= fechaInicio %>' + '_a_' + '<%= fechaFin %>';
        nombreArchivo += '.xls';

        let enlace = document.createElement("a");
        enlace.href = 'data:application/vnd.ms-excel,' + tablaHTML;
        enlace.download = nombreArchivo;
        enlace.click();
    });
</script>
</body>
</html>