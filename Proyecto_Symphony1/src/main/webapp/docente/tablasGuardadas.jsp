<%-- 
    Document   : tablasGuardadas
    Created on : 17/11/2025, 12:36:59 p. m.
    Author     : camiv
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" session="true" %>
<%@ page import="java.util.*" %>

<%
    // 🔐 Validar sesión y rol docente
    String nombre = (String) session.getAttribute("nombreActivo");
    String rol = (String) session.getAttribute("rolActivo");

    if (nombre == null || rol == null || !"docente".equalsIgnoreCase(rol)) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }

    // 📦 Recibir lista de tablas institucionales desde el servlet
    List<Map<String, String>> tablas = (List<Map<String, String>>) request.getAttribute("tablas");
%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Tablas guardadas - SymphonySIAS</title>

    <!-- 🎨 Estilos institucionales -->
    <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/estilos.css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;600&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">

    <style>
        body {
            font-family: 'Poppins', sans-serif;
            background-color: #f4f6f9;
            padding: 30px;
        }
        .tabla-box {
            background: #ffffff;
            padding: 30px;
            border-radius: 12px;
            box-shadow: 0 4px 12px rgba(0,0,0,0.06);
            max-width: 100%;
            margin: 0 auto;
        }
        .table th, .table td {
            vertical-align: middle;
            white-space: nowrap;
        }
    </style>
</head>
<body>

<jsp:include page="../fragmentos/header.jsp" />

<div class="tabla-box">
    <h4 class="text-center mb-4"><i class="fas fa-table"></i> Tablas guardadas</h4>

    <!-- 📢 Mostrar mensaje institucional si existe -->
    <% if (request.getAttribute("mensaje") != null) { %>
        <div class="alert alert-info text-center">
            <%= request.getAttribute("mensaje") %>
        </div>
    <% } %>

    <!-- ⚠️ Mostrar aviso si no hay tablas -->
    <% if (tablas == null || tablas.isEmpty()) { %>
        <div class="alert alert-warning text-center">
            ⚠️ No hay tablas guardadas aún.
        </div>
    <% } else { %>

        <!-- 📊 Mostrar tabla institucional -->
        <div class="table-responsive">
            <table class="table table-hover table-bordered">
                <thead class="table-secondary text-center">
                    <tr>
                        <th>Nombre</th>
                        <th>Descripción</th>
                        <th>Clase</th>
                        <th>Fecha creación</th>
                        <th>Última edición</th>
                        <th>Registrada por</th>
                        <th>Editada por</th>
                        <th>Estado</th>
                        <th>Fecha de envío</th>
                        <th>Acciones</th>
                    </tr>
                </thead>
                <tbody>
                    <% for (Map<String, String> tabla : tablas) {
                           String id = tabla.get("id");
                           String nombreTabla = tabla.get("nombre");
                           String descripcion = tabla.get("descripcion");
                           String clase = tabla.getOrDefault("clase", "—");
                           String fecha = tabla.get("fecha");
                           String fechaActualizacion = tabla.getOrDefault("fecha_actualizacion", "—");
                           String registradaPor = tabla.getOrDefault("registrada_por", "—");
                           String usuarioEditor = tabla.getOrDefault("usuario_editor", "—");
                           String enviada = tabla.get("enviada");
                           String fechaEnvio = tabla.get("fecha_envio");
                    %>
                    <tr>
                        <td><%= nombreTabla %></td>
                        <td><%= descripcion %></td>
                        <td><%= clase %></td>
                        <td><%= fecha %></td>
                        <td><%= fechaActualizacion %></td>
                        <td><%= registradaPor %></td>
                        <td><%= usuarioEditor %></td>
                        <td class="text-center">
                            <%= "Sí".equals(enviada) ? "<span class='text-success'>✅ Enviada</span>" : "<span class='text-warning'>⏳ No enviada</span>" %>
                        </td>
                        <td><%= fechaEnvio %></td>
                        <td class="text-center">
                            <!-- 🔍 Ver notas -->
                            <a href="<%= request.getContextPath() %>/VerNotasPorTablaServlet?tablaId=<%= id %>" class="btn btn-sm btn-outline-info me-1">
                                <i class="fas fa-eye"></i> Ver notas
                            </a>

                            <!-- 📤 Exportar tabla -->
                            <a href="<%= request.getContextPath() %>/ExportarTablaNotasServlet?tablaId=<%= id %>" class="btn btn-sm btn-outline-primary me-1">
                                <i class="fas fa-file-export"></i> Exportar
                            </a>

                            <!-- 📩 Enviar al administrador si no ha sido enviada -->
                            <% if (!"Sí".equals(enviada)) { %>
                                <a href="<%= request.getContextPath() %>/EnviarNotasAdminServlet?tablaId=<%= id %>" class="btn btn-sm btn-outline-success me-1">
                                    <i class="fas fa-paper-plane"></i> Enviar
                                </a>
                            <% } else { %>
                                <span class="text-muted"><i class="fas fa-lock"></i></span>
                            <% } %>

                            <!-- 🗑️ Eliminar tabla -->
                            <a href="<%= request.getContextPath() %>/EliminarTablaServlet?tablaId=<%= id %>" class="btn btn-sm btn-outline-danger" onclick="return confirm('¿Estás seguro de eliminar esta tabla?');">
                                <i class="fas fa-trash"></i> Eliminar
                            </a>
                        </td>
                    </tr>
                    <% } %>
                </tbody>
            </table>
        </div>
    <% } %>

    <!-- 🔙 Botón para volver al panel docente -->
    <div class="mt-4 text-center">
        <a href="<%= request.getContextPath() %>/PanelDocenteServlet" class="btn btn-outline-secondary">
            <i class="fas fa-arrow-left"></i> Volver al panel docente
        </a>
    </div>
</div>

<jsp:include page="../fragmentos/footer.jsp" />

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>