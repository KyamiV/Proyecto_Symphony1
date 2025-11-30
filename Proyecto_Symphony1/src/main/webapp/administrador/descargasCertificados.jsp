<%-- 
    Document   : descargasCertificados
    Created on : 19/11/2025, 12:06:42 p. m.
    Author     : camiv
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
    List<Map<String, String>> descargas = (List<Map<String, String>>) request.getAttribute("descargas");
    String mensaje = (String) request.getAttribute("mensaje");

    // 📦 Filtros recibidos
    String filtroUsuario = request.getParameter("usuario") != null ? request.getParameter("usuario") : "";
    String filtroInstrumento = request.getParameter("instrumento") != null ? request.getParameter("instrumento") : "";
    String filtroFecha = request.getParameter("fecha") != null ? request.getParameter("fecha") : "";
%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Descargas de certificados - SymphonySIAS</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/estilos.css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
    <style>
        body {
            font-family: 'Poppins', sans-serif;
            background-color: #f4f6f9;
            padding: 30px;
        }
        .tabla-descargas th {
            background-color: #0d6efd;
            color: white;
        }
        .btn-volver {
            background-color: #6c757d;
            color: white;
            padding: 8px 16px;
            border-radius: 6px;
            text-decoration: none;
        }
        .btn-volver:hover {
            background-color: #5a6268;
        }
    </style>
</head>
<body>

<!-- 🧭 Encabezado institucional -->
<jsp:include page="../fragmentos/header.jsp" />

<!-- 📋 Contenedor principal -->
<div class="container mt-4">
    <h4 class="mb-3 text-center"><i class="fas fa-download"></i> Descargas de certificados</h4>
    <p class="text-center text-muted">Consulta de certificados descargados por los estudiantes.</p>

    <!-- 🔍 Filtros de búsqueda -->
    <form method="get" action="<%= request.getContextPath() %>/VerDescargasCertificadosServlet" class="row g-3 mb-4 justify-content-center">
        <div class="col-md-3">
            <input type="text" name="usuario" class="form-control" placeholder="Filtrar por estudiante" value="<%= filtroUsuario %>">
        </div>
        <div class="col-md-3">
            <input type="text" name="instrumento" class="form-control" placeholder="Filtrar por instrumento" value="<%= filtroInstrumento %>">
        </div>
        <div class="col-md-3">
            <input type="date" name="fecha" class="form-control" value="<%= filtroFecha %>">
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
    <% } else if (descargas == null || descargas.isEmpty()) { %>
        <div class="alert alert-warning text-center">
            <i class="fas fa-exclamation-circle"></i> No hay descargas registradas aún.
        </div>
    <% } else { %>

        <!-- 📑 Tabla de descargas -->
        <div class="table-responsive mt-4">
            <table class="table table-bordered table-hover tabla-descargas align-middle">
                <thead class="text-center">
                    <tr>
                        <th>Estudiante</th>
                        <th>Instrumento</th>
                        <th>Etapa</th>
                        <th>Fecha</th>
                        <th>IP origen</th>
                    </tr>
                </thead>
                <tbody>
                    <% for (Map<String, String> registro : descargas) {
                           String accion = registro.get("accion");
                           String instrumento = "", etapa = "";

                           if (accion != null && accion.startsWith("Descargó certificado de ")) {
                               String[] partes = accion.replace("Descargó certificado de ", "").split(" - ");
                               if (partes.length == 2) {
                                   instrumento = partes[0];
                                   etapa = partes[1];
                               }
                           }
                    %>
                        <tr>
                            <td><%= registro.get("usuario") %></td>
                            <td><%= instrumento %></td>
                            <td><%= etapa %></td>
                            <td class="text-center"><%= registro.get("fecha") %></td>
                            <td class="text-center"><%= registro.get("ip_origen") %></td>
                        </tr>
                    <% } %>
                </tbody>
            </table>
        </div>
    <% } %>

    <!-- 🔙 Botón de regreso al panel -->
    <div class="mt-4 text-end">
        <a href="<%= request.getContextPath() %>/PanelAdministradorServlet" class="btn-volver">
            <i class="fas fa-arrow-left"></i> Volver al panel
        </a>
    </div>
</div>

<!-- 📌 Pie de página -->
<jsp:include page="../fragmentos/footer.jsp" />

</body>
</html>