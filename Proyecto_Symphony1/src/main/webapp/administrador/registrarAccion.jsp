<%-- 
    Document   : registrarAccion
    Created on : 17/11/2025, 6:22:52 p. m.
    Author     : camiv
    Descripción: Vista institucional para registrar acciones manuales, validaciones y seguimientos administrativos en SymphonySIAS.
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" session="true" %>

<%
    HttpSession sesion = request.getSession();
    String rol = (String) sesion.getAttribute("rolActivo");
    String usuario = (String) sesion.getAttribute("nombreActivo");

    if (rol == null || !(rol.equalsIgnoreCase("administrador") || rol.equalsIgnoreCase("coordinador"))) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }

    String mensaje = request.getParameter("mensaje");
%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Registrar acción institucional - SymphonySIAS</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/estilos.css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
    <style>
        body {
            font-family: 'Poppins', sans-serif;
            background-color: #f4f6f9;
            padding: 30px;
        }
        .form-box {
            background-color: #ffffff;
            padding: 30px;
            border-radius: 12px;
            box-shadow: 0 4px 12px rgba(0,0,0,0.06);
            max-width: 700px;
            margin: 0 auto;
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
<body class="bg-light">

<jsp:include page="../fragmentos/header.jsp" />
<jsp:include page="../fragmentos/sidebar.jsp" />

<div class="form-box">
    <h4 class="text-center mb-4"><i class="fas fa-clipboard-check"></i> Registrar acción institucional</h4>

    <% if (mensaje != null) { %>
        <div class="alert alert-info text-center"><%= mensaje %></div>
    <% } %>

    <form action="<%= request.getContextPath() %>/RegistrarAccionAdminServlet" method="post">
        <input type="hidden" name="usuario" value="<%= usuario %>">
        <input type="hidden" name="rol" value="<%= rol %>">
        <input type="hidden" name="tabla_id" value="">
        <input type="hidden" name="referencia_id" value="">
        <input type="hidden" name="ip_origen" value="<%= request.getRemoteAddr() %>">

        <div class="mb-3">
            <label class="form-label">📂 Área revisada:</label>
            <select name="modulo" class="form-select" required>
                <option value="">Selecciona una opción</option>
                <option value="Notas">Notas</option>
                <option value="Asignaciones">Asignaciones</option>
                <option value="Usuarios">Usuarios</option>
                <option value="Exportaciones">Exportaciones</option>
                <option value="Seguimiento">Seguimiento</option>
                <option value="Certificaciones">Certificaciones</option>
            </select>
        </div>

        <div class="mb-3">
            <label class="form-label">🗒️ Validación realizada:</label>
            <textarea name="accion" class="form-control" rows="3" placeholder="Ej. Validé las notas de la clase 3A antes de enviarlas" required></textarea>
        </div>

        <div class="d-flex justify-content-between">
            <button type="submit" class="btn btn-success">
                <i class="fas fa-check"></i> Registrar validación
            </button>
            <a href="<%= request.getContextPath() %>/PanelAdministradorServlet" class="btn-volver">
                <i class="fas fa-arrow-left"></i> Volver al panel
            </a>
        </div>
    </form>
</div>

<jsp:include page="../fragmentos/footer.jsp" />
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>

</body>
</html>