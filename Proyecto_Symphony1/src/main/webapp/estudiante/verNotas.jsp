<%--  
    Document   : verNotas.jsp
    Created on : 14/11/2025, 8:22:19 p. m.
    Autor      : camiv
    Rol        : estudiante
    Propósito  : Visualizar calificaciones musicales por etapa e instrumento
    Trazabilidad: recibe atributo 'notas' desde PanelEstudianteServlet, validado con auditoría institucional
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" session="true" %>
<%@ page import="java.util.List, java.util.Map" %>

<%
    String nombre = (String) session.getAttribute("nombreActivo");
    String rol = (String) session.getAttribute("rolActivo");

    if (nombre == null || rol == null || !"estudiante".equalsIgnoreCase(rol)) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }

    String mensaje = (String) request.getAttribute("mensaje");
%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Mis calificaciones - SymphonySIAS</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;600&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/estilos.css">

    <style>
        body { font-family: 'Poppins', sans-serif; background-color: #f4f6f9; }
        .dashboard-box { background: #ffffff; padding: 25px; border-radius: 12px; box-shadow: 0 4px 12px rgba(0,0,0,0.08); max-width: 1100px; margin: 30px auto; }
        .dashboard-title { font-size: 1.6rem; font-weight: 600; color: #198754; }
        .btn-volver { background-color: #198754; color: white; padding: 10px 20px; border-radius: 6px; text-decoration: none; font-weight: 500; }
        .btn-volver:hover { background-color: #157347; color: #fff; }
    </style>
</head>
<body class="bg-light">

    <!-- 📂 Menú lateral institucional -->
    <jsp:include page="../fragmentos/sidebar.jsp" />

    <!-- 🧭 Encabezado institucional -->
    <jsp:include page="../fragmentos/header.jsp" />

    <!-- Contenido principal -->
    <div class="container dashboard-box">
        <div class="d-flex justify-content-between align-items-center mb-4">
            <div>
                <div class="dashboard-title"><i class="fas fa-user-graduate"></i> Mis calificaciones</div>
                <div><strong><%= nombre %></strong> (estudiante)</div>
            </div>
        </div>

        <div class="dashboard-body">
            <div class="text-center mt-3">
                <h4 class="mb-3">Calificaciones por clase registrada</h4>

                <% if (mensaje != null) { %>
                    <div class="alert alert-info text-center"><%= mensaje %></div>
                <% } %>

                <!-- 🚧 Aviso de construcción -->
                <div class="alert alert-secondary text-center mt-4">
                    <i class="fas fa-tools"></i> Esta sección está en construcción.  
                    <br>Pronto podrás visualizar tus calificaciones musicales por etapa e instrumento aquí.
                </div>

                <!-- 🔙 Botón de regreso -->
                <div class="mt-4 text-start">
                    <a href="<%= request.getContextPath() %>/estudiante/estudiante.jsp" class="btn-volver" aria-label="Volver al panel de estudiante">
                        <i class="fas fa-arrow-left"></i> Volver al panel
                    </a>
                </div>
            </div>
        </div>
    </div>

    <!-- 📌 Pie de página institucional -->
    <jsp:include page="../fragmentos/footer.jsp" />

</body>
</html>