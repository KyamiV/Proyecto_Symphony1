<%-- 
    Document   : verBitacora
    Created on : 17/11/2025, 6:49:18 p. m.
    Author     : camiv
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" session="true" %>
<%@ page import="java.util.*, com.mysymphony.proyecto_symphony1.dao.AuditoriaDAO" %>

<%
    // 🔐 Validación de sesión y rol autorizado
    HttpSession sesion = request.getSession();
    String rol = (String) sesion.getAttribute("rolActivo");
    String usuario = (String) sesion.getAttribute("nombreActivo");

    if (rol == null || !(rol.equalsIgnoreCase("administrador") || rol.equalsIgnoreCase("coordinador") || rol.equalsIgnoreCase("gestor"))) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }

    // 📥 Consulta de registros desde AuditoriaDAO
    AuditoriaDAO dao = new AuditoriaDAO();
    List<Map<String, String>> registros = dao.obtenerTodos();
%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Bitácora institucional | SymphonySIAS</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/estilos.css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
</head>
<body class="bg-light">

<jsp:include page="../fragmentos/header.jsp" />

<div class="container mt-5">
    <h3 class="text-center mb-4"><i class="fas fa-book"></i> Bitácora de validaciones institucionales</h3>

    <%-- 📋 Tabla de registros de auditoría --%>
    <div class="table-responsive">
        <table class="table table-bordered table-hover bg-white shadow-sm align-middle">
            <thead class="table-dark text-center">
                <tr>
                    <th><i class="fas fa-user"></i> Usuario</th>
                    <th><i class="fas fa-layer-group"></i> Módulo</th>
                    <th><i class="fas fa-clipboard-check"></i> Acción</th>
                    <th><i class="fas fa-clock"></i> Fecha y hora</th>
                </tr>
            </thead>
            <tbody>
                <% if (registros != null && !registros.isEmpty()) {
                    for (Map<String, String> fila : registros) { %>
                        <tr>
                            <td><%= fila.get("usuario") %></td>
                            <td><%= fila.get("modulo") %></td>
                            <td><%= fila.get("accion") %></td>
                            <td><%= fila.get("fecha_hora") %></td>
                        </tr>
                <%  }
                } else { %>
                    <tr>
                        <td colspan="4" class="text-center text-muted">No hay registros en la bitácora.</td>
                    </tr>
                <% } %>
            </tbody>
        </table>
    </div>

    <%-- 🔙 Botón volver --%>
    <div class="text-end mt-4">
        <a href="<%= request.getContextPath() %>/PanelAdministradorServlet" class="btn btn-secondary">
            <i class="fas fa-arrow-left"></i> Volver al panel
        </a>
    </div>
</div>

<jsp:include page="../fragmentos/footer.jsp" />
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>