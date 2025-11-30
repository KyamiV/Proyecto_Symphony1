<%-- 
    Document   : editarEstudiante.jsp
    Autor      : camiv
    Created on : 15/11/2025, 6:36:04 p. m.
    Descripción: Vista del rol docente para editar datos técnicos y pedagógicos de un estudiante.
                 Requiere atributo 'estudiante' como mapa con claves: id, nombre, instrumento, nivel, etapa.
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" session="true" %>
<%@ page import="java.util.Map" %>

<%
    // 🔐 Validación de sesión y rol
    String nombre = (String) session.getAttribute("nombreActivo");
    String rol = (String) session.getAttribute("rolActivo");

    if (nombre == null || rol == null || !"docente".equalsIgnoreCase(rol)) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }

    // 📥 Datos del estudiante recibidos desde el servlet
    Map<String, String> estudiante = (Map<String, String>) request.getAttribute("estudiante");
    String mensaje = (String) request.getAttribute("mensaje");
%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Editar estudiante - SymphonySIAS</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/estilos.css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;600&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
</head>
<body>

<!-- 🧭 Encabezado institucional -->
<jsp:include page="../fragmentos/header.jsp" />

<!-- 📋 Contenedor principal -->
<div class="tabla-box">
    <h4 class="text-center mb-4"><i class="fas fa-user-edit"></i> Editar datos del estudiante</h4>

    <%-- 🧾 Mensaje informativo si existe --%>
    <% if (mensaje != null) { %>
        <div class="alert alert-info text-center">
            <%= mensaje %>
        </div>
    <% } %>

    <%-- 📝 Formulario de edición --%>
    <form method="post" action="<%= request.getContextPath() %>/EditarEstudianteServlet" class="mx-auto" style="max-width: 600px;">
        <input type="hidden" name="id" value="<%= estudiante.get("id") %>">

        <div class="mb-3">
            <label class="form-label">Nombre completo</label>
            <input type="text" class="form-control" value="<%= estudiante.get("nombre") %>" disabled>
        </div>

        <div class="mb-3">
            <label class="form-label">Instrumento</label>
            <input type="text" name="instrumento" class="form-control" value="<%= estudiante.get("instrumento") != null ? estudiante.get("instrumento") : "" %>">
        </div>

        <div class="mb-3">
            <label class="form-label">Nivel técnico</label>
            <select name="nivel" class="form-select">
                <option value="">Seleccionar</option>
                <option value="Básico" <%= "Básico".equals(estudiante.get("nivel")) ? "selected" : "" %>>Básico</option>
                <option value="Intermedio" <%= "Intermedio".equals(estudiante.get("nivel")) ? "selected" : "" %>>Intermedio</option>
                <option value="Avanzado" <%= "Avanzado".equals(estudiante.get("nivel")) ? "selected" : "" %>>Avanzado</option>
            </select>
        </div>

        <div class="mb-3">
            <label class="form-label">Etapa pedagógica</label>
            <select name="etapa" class="form-select">
                <option value="">Seleccionar</option>
                <option value="Exploración sonora" <%= "Exploración sonora".equals(estudiante.get("etapa")) ? "selected" : "" %>>Exploración sonora</option>
                <option value="Iniciación técnica" <%= "Iniciación técnica".equals(estudiante.get("etapa")) ? "selected" : "" %>>Iniciación técnica</option>
                <option value="Repertorio básico" <%= "Repertorio básico".equals(estudiante.get("etapa")) ? "selected" : "" %>>Repertorio básico</option>
                <option value="Repertorio intermedio" <%= "Repertorio intermedio".equals(estudiante.get("etapa")) ? "selected" : "" %>>Repertorio intermedio</option>
                <option value="Repertorio avanzado" <%= "Repertorio avanzado".equals(estudiante.get("etapa")) ? "selected" : "" %>>Repertorio avanzado</option>
            </select>
        </div>

        <%-- ✅ Botones de acción --%>
        <div class="text-center mt-4">
            <button type="submit" class="btn btn-primary">
                <i class="fas fa-save"></i> Guardar cambios
            </button>
            <a href="<%= request.getContextPath() %>/GestionarEstudiantesServlet" class="btn btn-secondary ms-2">
                <i class="fas fa-arrow-left"></i> Volver
            </a>
        </div>
    </form>
</div>

<!-- 📌 Pie de página -->
<jsp:include page="../fragmentos/footer.jsp" />

</body>
</html>