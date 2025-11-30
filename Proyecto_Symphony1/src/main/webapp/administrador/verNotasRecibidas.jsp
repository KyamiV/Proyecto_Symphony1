<%-- 
    Document   : verNotasRecibidas
    Created on : 17/11/2025, 1:51:52 p. m.
    Author     : camiv
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

    List<Map<String, String>> notas = (List<Map<String, String>>) request.getAttribute("notas");
    Map<String, String> datosTabla = (Map<String, String>) request.getAttribute("datosTabla");
    Integer tablaId = (Integer) request.getAttribute("tablaId");

    String nombreTabla = datosTabla != null ? datosTabla.get("nombre") : "";
    String descripcion = datosTabla != null ? datosTabla.get("descripcion") : "";
    String fechaEnvio = datosTabla != null ? datosTabla.get("fecha_envio") : "";
    String docente = datosTabla != null ? datosTabla.get("docente") : "";
    String validada = datosTabla != null ? datosTabla.get("validada") : "false";
%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Notas recibidas - SymphonySIAS</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/estilos.css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;600&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
    <style>
        body { font-family: 'Poppins', sans-serif; background-color: #f4f6f9; padding: 30px; }
        .tabla-box { background: #ffffff; padding: 30px; border-radius: 12px; box-shadow: 0 4px 12px rgba(0,0,0,0.06); max-width: 100%; margin: 0 auto; }
        .table th, .table td { vertical-align: middle; text-align: center; }
    </style>
</head>
<body>

<jsp:include page="../fragmentos/header.jsp" />

<div class="tabla-box">
    <h4 class="text-center mb-4"><i class="fas fa-eye"></i> Notas recibidas</h4>

    <div class="mb-4 text-center">
        <h5><i class="fas fa-table"></i> <%= nombreTabla %></h5>
        <p><strong>Descripción:</strong> <%= descripcion %></p>
        <p><strong>Docente:</strong> <i class="fas fa-user-tie text-secondary me-1"></i> <%= docente %></p>
        <p><strong>Fecha de envío:</strong> <%= fechaEnvio %></p>
        <p><strong>Estado de validación:</strong>
            <%= "true".equals(validada) ? "✅ Validada" : "⏳ Pendiente" %>
        </p>
    </div>

    <% if (notas == null || notas.isEmpty()) { %>
        <div class="alert alert-info text-center">
            🚧 Módulo en construcción: aún no se han terminado las notas de esta tabla.
        </div>
    <% } else { %>
        <div class="table-responsive">
            <table class="table table-hover table-bordered">
                <thead class="table-secondary">
                    <tr>
                        <th>Estudiante</th>
                        <th>Etapa</th>
                        <th>Instrumento</th>
                        <th>Nota</th>
                        <th>Observación</th>
                        <th>Fecha</th>
                    </tr>
                </thead>
                <tbody>
                    <% for (Map<String, String> nota : notas) {
                           String estudiante = nota.get("nombre_estudiante");
                           String etapa = nota.get("etapa");
                           String instrumento = nota.get("instrumento");
                           String notaValor = nota.get("nota");
                           String observacion = nota.get("observacion");
                           String fecha = nota.get("fecha");
                    %>
                    <tr>
                        <td><i class="fas fa-user-graduate text-secondary me-1"></i> <%= estudiante %></td>
                        <td><%= etapa %></td>
                        <td><%= instrumento %></td>
                        <td><%= notaValor %></td>
                        <td><%= observacion %></td>
                        <td><%= fecha %></td>
                    </tr>
                    <% } %>
                </tbody>
            </table>
        </div>
    <% } %>

    <div class="mt-4 d-flex justify-content-center gap-3 flex-wrap">
        <a href="<%= request.getContextPath() %>/VerTablasRecibidasServlet" class="btn btn-outline-secondary">
            <i class="fas fa-arrow-left"></i> Volver a tablas recibidas
        </a>
        <a href="<%= request.getContextPath() %>/ExportarTablaNotasServlet?tablaId=<%= tablaId %>" class="btn btn-outline-info">
            <i class="fas fa-file-export"></i> Exportar tabla
        </a>
        <% if (!"true".equals(validada)) { %>
            <a href="<%= request.getContextPath() %>/ValidarTablaServlet?tablaId=<%= tablaId %>" class="btn btn-outline-success"
               onclick="return confirm('¿Deseas validar esta tabla como revisada?');">
                <i class="fas fa-check-circle"></i> Validar tabla
            </a>
        <% } %>
    </div>
</div>

<jsp:include page="../fragmentos/footer.jsp" />

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>