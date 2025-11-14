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
    <link rel="stylesheet" href="assets/adminlte/plugins/bootstrap/css/bootstrap.min.css">
    <link rel="stylesheet" href="assets/adminlte/plugins/fontawesome-free/css/all.min.css">
    <style>
       body {
        background-color: #f4f6f9;
        font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
        display: flex;
        align-items: center;
        justify-content: center;
        min-height: 100vh;
    }

    .register-box {
        background: rgba(255, 255, 255, 0.92);
        padding: 40px 30px;
        border-radius: 12px;
        box-shadow: 0 0 30px rgba(0,0,0,0.1);
        width: 100%;
        max-width: 480px;
        text-align: center;
        overflow: hidden;
    }

    .register-box img {
        height: 80px;
        margin-bottom: 20px;
        display: block;
        margin-left: auto;
        margin-right: auto;
    }

    h5 {
        font-size: 1.3rem;
        font-weight: 600;
        color: #007bff;
        margin-bottom: 25px;
    }

    .form-label {
        font-weight: 500;
        margin-bottom: 6px;
        color: #343a40;
        display: block;
        text-align: left;
    }

    .form-control {
        font-size: 1rem;
        padding: 12px 15px;
        border-radius: 8px;
        border: 1px solid #ced4da;
        width: 100%;
        box-sizing: border-box;
    }

    .form-group {
        position: relative;
        margin-bottom: 20px;
    }

    .form-group i {
        position: absolute;
        top: 50%;
        left: 15px;
        transform: translateY(-50%);
        color: #6c757d;
    }

    .btn-block {
        font-size: 1rem;
        padding: 12px;
        border-radius: 8px;
        background-color: #28a745;
        color: white;
        border: none;
        transition: background-color 0.3s ease;
    }

    .btn-block:hover {
        background-color: #218838;
    }

    .text-muted {
        font-size: 0.9rem;
    }

    .text-muted a {
        color: #007bff;
        text-decoration: none;
    }

    .text-muted a:hover {
        text-decoration: underline;
    }
    </style>
</head>
<body>
    <div class="register-box text-center">
        <img src="assets/adminlte/img/LogoSymphonySIAS.png" alt="Logo SymphonySIAS" class="img-fluid mb-3" style="height:120px;">
        <h5 class="text-primary mb-4">
            <i class="fas fa-user-plus"></i> Registro de nuevo usuario
        </h5>

        <form action="RegistroServlet" method="post" autocomplete="off">
            <div class="form-group">
                <input type="text" name="nombre" class="form-control" placeholder="Nombre completo" required>
            </div>
            <div class="form-group">
                <input type="email" name="correo" class="form-control" placeholder="Correo electrónico" required>
            </div>
            <div class="form-group">
                <input type="password" name="clave" class="form-control" placeholder="Contraseña" required>
            </div>
            <div class="form-group">
                <select name="rol" class="form-control" required>
                    <option value="">Seleccione un rol</option>
                    <option value="est">Estudiante</option>
                    <option value="doc">Docente</option>
                    <option value="coord">Coordinador</option>
                    <option value="dir">Director</option>
                    <option value="admin">Administrador</option>
                    <option value="auxadmin">Auxiliar administrativo</option>
                    <option value="auxcont">Auxiliar contable</option>
                </select>
            </div>
            <button type="submit" class="btn btn-block btn-success">Registrarse</button>

            <% if (request.getAttribute("mensaje") != null) { %>
                <div class="alert alert-warning mt-3">
                    <%= request.getAttribute("mensaje") %>
                </div>
            <% } %>

            <p class="mt-4 text-muted">
                ¿Ya tienes cuenta? <a href="login.jsp">Volver al login</a>
            </p>
        </form>
    </div>
</body>
</html>