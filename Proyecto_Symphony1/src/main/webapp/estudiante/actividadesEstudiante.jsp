<%--  
    Document   : ActividadesEstudiante  
    Created on : 24/11/2025, 3:31:32 p. m.  
    Author     : camiv  
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" session="true" %>
<%@ page import="java.util.List, java.util.Map" %>

<%
    // 🔐 Validación de sesión y rol
    String nombre = (String) session.getAttribute("nombreActivo");
    String rol = (String) session.getAttribute("rolActivo");

    if (nombre == null || rol == null || !"estudiante".equalsIgnoreCase(rol)) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }

    // 📦 Datos recibidos desde el servlet
    List<Map<String, String>> actividades = (List<Map<String, String>>) request.getAttribute("actividades");
    String mensaje = (String) request.getAttribute("mensaje");
%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Actividades - SymphonySIAS</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;600&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/estilos.css">
</head>
<body class="bg-light">

    <!-- 📂 Menú lateral institucional -->
    <jsp:include page="../fragmentos/sidebar.jsp" />

    <!-- 🧭 Encabezado institucional -->
    <jsp:include page="../fragmentos/header.jsp" />

    <!-- Contenedor principal -->
    <div class="container dashboard-box">
        <div class="d-flex justify-content-between align-items-center mb-4">
            <div>
                <div class="dashboard-title"><i class="fas fa-tasks"></i> Actividades</div>
                <div><strong><%= nombre %></strong> (estudiante)</div>
            </div>
            <img src="<%= request.getContextPath() %>/assets/img/logo.png" alt="Logo SymphonySIAS" style="max-height:70px;">
        </div>

        <!-- 📄 Cuerpo principal -->
        <div class="dashboard-body">

            <% if (mensaje != null) { %>
                <div class="alert alert-info text-center mt-3"><%= mensaje %></div>
            <% } %>

            <h4 class="text-center mt-3">Actividades asignadas por el docente</h4>

            <!-- 🚧 Aviso de construcción -->
            <div class="alert alert-secondary text-center mt-4">
                <i class="fas fa-tools"></i> Esta sección está en construcción.  
                <br>Pronto podrás ver y gestionar tus actividades aquí.
            </div>

            <!-- 🔙 Botón de regreso -->
            <div class="mt-4 text-start">
                <a href="<%= request.getContextPath() %>/VerClasesEstudianteServlet" class="btn btn-success">
                    <i class="fas fa-arrow-left"></i> Volver a mis clases
                </a>
            </div>
        </div>
    </div>

    <!-- 📌 Pie de página institucional -->
    <jsp:include page="../fragmentos/footer.jsp" />

</body>
</html>