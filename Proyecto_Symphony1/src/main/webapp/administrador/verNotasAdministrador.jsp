<%-- 
    Document   : verNotasAdministrador.jsp
    Rol        : administrador
    Función    : Visualizar y exportar notas musicales por instrumento, docente y etapa
    Autor      : camiv
    Trazabilidad: incluye filtros, tabla de resultados, exportación y validación de sesión
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" session="true" %>
<%@ page import="java.util.List, java.util.Map" %>

<%
    // Validación de sesión y rol
    String nombre = (String) session.getAttribute("nombreActivo");
    String rol = (String) session.getAttribute("rolActivo");

    if (nombre == null || rol == null || !"administrador".equalsIgnoreCase(rol)) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }

    // Datos recibidos desde el servlet
    List<Map<String, String>> notas = (List<Map<String, String>>) request.getAttribute("notas");
    String error = (String) request.getAttribute("error");

    // Filtros actuales
    String instrumentoActual = request.getParameter("instrumento");
    String docenteActual = request.getParameter("docente");
    String etapaActual = request.getParameter("etapa");
%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Notas por instrumento | SymphonySIAS</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/estilos.css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
</head>
<body>

<jsp:include page="../fragmentos/header.jsp" />

<div class="container mt-5">
    <h3 class="text-center mb-4"><i class="fas fa-music"></i> Notas musicales por instrumento</h3>

    <!-- Filtros -->
    <form method="get" action="<%= request.getContextPath() %>/NotasAdministradorServlet" class="row g-3 mb-4">
        <div class="col-md-3">
            <select name="instrumento" class="form-select">
                <option value="">Todos los instrumentos</option>
                <%
                    String[] instrumentos = {"Piano", "Violín", "Guitarra", "Flauta", "Batería", "Canto"};
                    for (String inst : instrumentos) {
                %>
                    <option value="<%= inst %>" <%= inst.equals(instrumentoActual) ? "selected" : "" %>><%= inst %></option>
                <% } %>
            </select>
        </div>
        <div class="col-md-3">
            <input type="text" name="docente" class="form-control" placeholder="Filtrar por docente..." value="<%= (docenteActual != null) ? docenteActual : "" %>">
        </div>
        <div class="col-md-3">
            <select name="etapa" class="form-select">
                <option value="">Todas las etapas</option>
                <%
                    String[] etapas = {"Inicial", "Intermedia", "Avanzada"};
                    for (String etapa : etapas) {
                %>
                    <option value="<%= etapa %>" <%= etapa.equals(etapaActual) ? "selected" : "" %>><%= etapa %></option>
                <% } %>
            </select>
        </div>
        <div class="col-md-3 text-end">
            <button type="submit" class="btn btn-primary">
                <i class="fas fa-filter"></i> Filtrar
            </button>
        </div>
    </form>

    <!-- Exportar a Excel -->
    <form method="post" action="<%= request.getContextPath() %>/ExportarNotasCSVServlet" class="mb-4 text-end">
        <input type="hidden" name="instrumento" value="<%= instrumentoActual %>">
        <input type="hidden" name="docente" value="<%= docenteActual %>">
        <input type="hidden" name="etapa" value="<%= etapaActual %>">
        <button type="submit" class="btn btn-success">
            <i class="fas fa-file-excel"></i> Exportar a Excel
        </button>
    </form>

    <!-- Mensajes -->
    <% if (error != null) { %>
        <div class="alert alert-danger text-center"><%= error %></div>
    <% } else if (notas == null || notas.isEmpty()) { %>
        <div class="alert alert-info text-center">
            No hay notas registradas para los filtros seleccionados.
        </div>
    <% } else { %>
        <div class="alert alert-secondary text-center mb-3">
            <strong>Total de registros:</strong> <%= notas.size() %>
        </div>

        <!-- Tabla de resultados -->
        <div class="table-responsive mb-5">
            <table class="table table-bordered table-hover align-middle">
                <thead class="table-dark text-center">
                    <tr>
                        <th>Estudiante</th>
                        <th>Instrumento</th>
                        <th>Etapa</th>
                        <th>Nota</th>
                        <th>Observaciones</th>
                        <th>Docente</th>
                        <th>Fecha</th>
                    </tr>
                </thead>
                <tbody>
                    <% for (Map<String, String> fila : notas) { %>
                        <tr>
                            <td><%= fila.get("estudiante") %></td>
                            <td><%= fila.get("instrumento") %></td>
                            <td><%= fila.get("etapa") %></td>
                            <td><%= fila.get("nota") %></td>
                            <td><%= fila.get("observaciones") %></td>
                            <td><%= fila.get("docente") %></td>
                            <td><%= fila.get("fecha") %></td>
                        </tr>
                    <% } %>
                </tbody>
            </table>
        </div>
    <% } %>

    <!-- Volver -->
    <div class="mt-4 text-end">
        <a href="<%= request.getContextPath() %>/PanelAdministradorServlet" class="btn btn-secondary">
            <i class="fas fa-arrow-left"></i> Volver al panel
        </a>
    </div>
</div>

<jsp:include page="../fragmentos/footer.jsp" />

</body>
</html>