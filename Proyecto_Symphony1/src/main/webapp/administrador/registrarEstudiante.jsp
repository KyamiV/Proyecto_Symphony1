<%-- 
    Document   : registrarEstudiante
    Created on : 16/11/2025, 7:30:08 p. m.
    Author     : camiv
    Descripción: Vista para registrar un nuevo estudiante institucional.
                 Requiere rol 'administrador' y muestra formulario con nombre, instrumento y etapa.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" session="true" %>
<%
    // 🔐 Validación de sesión y rol
    String rol = (String) session.getAttribute("rolActivo");
    if (rol == null || !"administrador".equalsIgnoreCase(rol)) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }

    // 📥 Mensaje de sesión si existe
    String mensaje = (String) session.getAttribute("mensaje");
    if (mensaje != null) {
        session.removeAttribute("mensaje");
    }
%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Registrar Estudiante - SymphonySIAS</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/estilos.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
</head>
<body class="bg-light">

    <!-- 🔹 Encabezado institucional -->
    <jsp:include page="../fragmentos/header.jsp" />

    <!-- 🔹 Menú lateral institucional -->
    <jsp:include page="../fragmentos/sidebar.jsp" />

    <main class="container mt-4">
        <h3 class="text-primary mb-4">
            <i class="fas fa-user-plus"></i> Registrar nuevo estudiante
        </h3>

        <%-- 🧾 Mensaje de resultado si existe --%>
        <% if (mensaje != null) { %>
            <div class="alert alert-info text-center"><%= mensaje %></div>
        <% } %>

        <%-- 📝 Formulario de registro --%>
        <form action="<%= request.getContextPath() %>/RegistrarEstudianteServlet" method="post" class="row g-3">
            <div class="col-md-6">
                <label class="form-label">Nombre completo</label>
                <input type="text" name="nombre" class="form-control" required>
            </div>
            <div class="col-md-3">
                <label class="form-label">Instrumento</label>
                <input type="text" name="instrumento" class="form-control" required>
            </div>
            <div class="col-md-3">
                <label class="form-label">Etapa pedagógica</label>
                <select name="etapa" class="form-select" required>
                    <option value="">Seleccione</option>
                    <option value="Exploración sonora">Exploración sonora</option>
                    <option value="Iniciación técnica">Iniciación técnica</option>
                    <option value="Repertorio básico">Repertorio básico</option>
                    <option value="Repertorio intermedio">Repertorio intermedio</option>
                    <option value="Repertorio avanzado">Repertorio avanzado</option>
                </select>
            </div>
            <div class="col-12 text-center mt-3">
                <button type="submit" class="btn btn-success">
                    <i class="fas fa-save"></i> Registrar estudiante
                </button>
                <a href="<%= request.getContextPath() %>/PanelAdministradorServlet" class="btn btn-secondary ms-2">
                    <i class="fas fa-arrow-left"></i> Volver al panel
                </a>
            </div>
        </form>
    </main>

    <!-- 🔹 Pie institucional -->
    <jsp:include page="../fragmentos/footer.jsp" />

    <!-- Bootstrap JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>