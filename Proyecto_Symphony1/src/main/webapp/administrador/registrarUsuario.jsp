<%-- 
    Document   : registrarUsuario
    Created on : 26/11/2025, 9:46:38 p. m.
    Author     : camiv
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Registro de Usuario | SymphonySIAS</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/estilos.css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
</head>
<body>

<jsp:include page="../fragmentos/header.jsp"/>
<jsp:include page="../fragmentos/sidebar.jsp"/>

<div class="container mt-5">
    <h2 class="text-center mb-4"><i class="fas fa-user-plus"></i> Registro de Usuario Institucional</h2>

    <!-- Mensajes de error o éxito -->
    <c:if test="${not empty error}">
        <div class="alert alert-danger text-center">${error}</div>
    </c:if>
    <c:if test="${not empty mensaje}">
        <div class="alert alert-success text-center">${mensaje}</div>
    </c:if>

    <!-- Formulario básico -->
    <form action="<%= request.getContextPath() %>/RegistrarUsuarioServlet" method="post" class="p-4 border rounded bg-light">
        <div class="mb-3">
            <label for="nombre" class="form-label">Nombre completo:</label>
            <input type="text" id="nombre" name="nombre" class="form-control" required>
        </div>

        <div class="mb-3">
            <label for="usuario" class="form-label">Correo institucional:</label>
            <input type="email" id="usuario" name="usuario" class="form-control" required>
        </div>

        <div class="mb-3">
            <label for="clave" class="form-label">Contraseña:</label>
            <input type="password" id="clave" name="clave" class="form-control" required minlength="6">
        </div>

        <div class="mb-3">
            <label for="rol" class="form-label">Rol:</label>
            <select id="rol" name="rol" class="form-select">
                <option value="estudiante" selected>Estudiante</option>
                <option value="docente">Docente</option>
                <option value="administrador">Administrador</option>
            </select>
        </div>

        <button type="submit" class="btn btn-success w-100">
            <i class="fas fa-save"></i> Registrar
        </button>
    </form>
</div>

<jsp:include page="../fragmentos/footer.jsp"/>

</body>
</html>