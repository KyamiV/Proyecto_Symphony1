<%-- 
    Document   : registro
    Created on : 13/11/2025, 7:00:08 p. m.
    Author     : camiv
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>SymphonySIAS | Registro</title>
    <link rel="stylesheet" href="assets/css/estilos.css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <script src="https://kit.fontawesome.com/a076d05399.js" crossorigin="anonymous"></script>
</head>
<body>
    <div class="register-box text-center mt-5">
        <img src="assets/adminlte/img/LogoSymphonySIAS.png" alt="Logo SymphonySIAS" class="img-fluid mb-3" style="height:120px;">
        <h5 class="text-primary mb-4">
            <i class="fas fa-user-plus"></i> Registro de nuevo usuario
        </h5>

        <form action="RegistroServlet" method="post" autocomplete="off" class="px-4">
            <div class="form-group mb-3">
                <input type="text" name="nombre" class="form-control" placeholder="Nombre completo" required>
            </div>
            <div class="form-group mb-3">
                <input type="email" name="correo" class="form-control" placeholder="Correo electrónico" required>
            </div>
            <div class="form-group mb-3">
                <input type="password" name="clave" class="form-control" placeholder="Contraseña" required>
            </div>
            <div class="form-group mb-4">
                <select name="rol" class="form-control" required>
                    <option value="">Seleccione un rol</option>
                    <option value="estudiante">Estudiante</option>
                    <option value="docente">Docente</option>
                    <option value="coordinador">Coordinador</option>
                    <option value="director">Director</option>
                    <option value="administrador">Administrador</option>
                    <option value="auxadmin">Auxiliar administrativo</option>
                    <option value="auxcont">Auxiliar contable</option>
                </select>
            </div>
            <button type="submit" class="btn btn-success w-100">Registrarse</button>

            <% if (request.getAttribute("mensaje") != null) { %>
                <div class="alert alert-warning mt-3">
                    <%= request.getAttribute("mensaje") %>
                </div>
            <% } %>

            <% if (request.getAttribute("error") != null) { %>
                <div class="alert alert-danger mt-3">
                    <%= request.getAttribute("error") %>
                </div>
            <% } %>

            <p class="mt-4 text-muted">
                ¿Ya tienes cuenta? <a href="login.jsp">Volver al login</a>
            </p>
        </form>
    </div>
</body>
</html>
