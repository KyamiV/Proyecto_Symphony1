<%-- 
    Document   : crearInstrumento.jsp
    Created on : 15/11/2025, 7:55:32 p. m.
    Author     : camiv
    Descripción: Vista del rol administrador para registrar nuevos instrumentos musicales en SymphonySIAS.
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" session="true" %>

<%
    // 🔐 Validación de sesión y rol
    String nombre = (String) session.getAttribute("nombreActivo");
    String rol = (String) session.getAttribute("rolActivo");

    if (nombre == null || rol == null || !"administrador".equalsIgnoreCase(rol)) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }

    // 📥 Mensajes institucionales
    String mensaje = (String) request.getAttribute("mensaje");
    String error = (String) request.getAttribute("error");
%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Registrar instrumento musical - SymphonySIAS</title>
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
            max-width: 600px;
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
<body>

<!-- 🧭 Encabezado institucional -->
<jsp:include page="../fragmentos/header.jsp" />

<div class="form-box">
    <h4 class="text-center mb-4"><i class="fas fa-music"></i> Registrar nuevo instrumento musical</h4>

    <% if (mensaje != null) { %>
        <div class="alert alert-success text-center"><%= mensaje %></div>
    <% } else if (error != null) { %>
        <div class="alert alert-danger text-center"><%= error %></div>
    <% } %>

    <form action="<%= request.getContextPath() %>/RegistrarInstrumentoServlet" method="post">
        <div class="mb-3">
            <label for="nombreInstrumento" class="form-label">Nombre del instrumento:</label>
            <input type="text" class="form-control" id="nombreInstrumento" name="nombreInstrumento" placeholder="Ej: Piano, Violín, Guitarra" required>
        </div>

        <div class="mb-3">
            <label for="descripcion" class="form-label">Descripción:</label>
            <textarea class="form-control" id="descripcion" name="descripcion" rows="3" placeholder="Breve descripción del instrumento..." required></textarea>
        </div>

        <div class="d-flex justify-content-between">
            <button type="submit" class="btn btn-primary">
                <i class="fas fa-save"></i> Registrar instrumento
            </button>
            <a href="<%= request.getContextPath() %>/PanelAdministradorServlet" class="btn-volver">
                <i class="fas fa-arrow-left"></i> Volver al panel
            </a>
        </div>
    </form>
</div>

<!-- 📌 Pie de página -->
<jsp:include page="../fragmentos/footer.jsp" />
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>

</body>
</html>