<%-- 
    Document   : registro
    Created on : 13/11/2025, 7:00:08 p. m.
    Author     : camiv
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>SymphonySIAS | Registro</title>

    <link rel="stylesheet" href="assets/css/estilos.css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <script src="https://kit.fontawesome.com/a076d05399.js" crossorigin="anonymous"></script>

    <style>
        body {
            background: linear-gradient(to right, #f8f9fa, #e3f2fd);
            font-family: 'Poppins', sans-serif;
        }
        .register-box {
            max-width: 470px;
            margin: auto;
            background: #fff;
            border-radius: 12px;
            box-shadow: 0 0 15px rgba(0,0,0,0.1);
            padding: 30px;
        }
        .form-control:focus { box-shadow: none; border-color: #0d6efd; }
        .btn-success { background-color: #198754; border: none; }
        .btn-success:hover { background-color: #157347; }
        .logo-img { height: 100px; transition: transform .3s; }
        .logo-img:hover { transform: scale(1.07); }
        .toggle-icon {
            position: absolute;
            top: 50%; right: 15px;
            transform: translateY(-50%);
            cursor: pointer;
            color: #6c757d;
        }
        .requirements { font-size: .9em; text-align: left; margin-top: 8px; padding-left: 5px; }
        .requirements li { color: #dc3545; }
        .requirements li.valid { color: #198754; }
    </style>
</head>

<body>

<div class="register-box text-center mt-5">
    <img src="assets/img/logo.png" alt="Logo SymphonySIAS" class="img-fluid logo-img mb-3">

    <h4 class="text-primary fw-bold mb-4">
        <i class="fas fa-user-plus me-2"></i> Crear cuenta de estudiante
    </h4>

    <form action="RegistroServlet" method="post" autocomplete="off">

        <!-- Nombre -->
        <div class="form-floating mb-3">
            <input type="text" name="nombre" id="nombre" class="form-control"
                   placeholder="Nombre completo" required>
            <label for="nombre">Nombre completo</label>
        </div>

        <!-- Correo institucional -->
        <div class="form-floating mb-3">
            <input type="email" name="correo" id="correo" class="form-control"
                   placeholder="Correo institucional" required
                   pattern="^[a-zA-Z0-9._%+-]+@symphony\\.edu$"
                   title="Debe ser un correo institucional terminado en @symphony.edu">
            <label for="correo">Correo institucional</label>
        </div>

        <!-- Contraseña -->
        <div class="form-floating mb-3 position-relative">
            <input type="password" name="clave" id="clave" class="form-control"
                   placeholder="Contraseña" required>
            <label for="clave">Contraseña</label>
            <i class="fas fa-eye toggle-icon" id="toggleClave"></i>
        </div>

        <!-- Requisitos de contraseña -->
        <ul class="requirements" id="reqList">
            <li id="reqLength">Mínimo 8 caracteres</li>
            <li id="reqUpper">Al menos una mayúscula</li>
            <li id="reqNumber">Al menos un número</li>
            <li id="reqSpecial">Al menos un carácter especial (!@#$%^&*)</li>
        </ul>

        <!-- Rol fijo: estudiante -->
        <input type="hidden" name="rol" value="estudiante">

        <!-- Botón -->
        <button type="submit" class="btn btn-success w-100 py-2">
            <i class="fas fa-check-circle me-1"></i> Registrarse como estudiante
        </button>

        <!-- Mensajes -->
        <c:if test="${not empty mensaje}">
            <div class="alert alert-warning mt-3 mb-0">${mensaje}</div>
        </c:if>

        <c:if test="${not empty error}">
            <div class="alert alert-danger mt-3 mb-0">${error}</div>
        </c:if>

        <p class="mt-4 text-muted">
            ¿Ya tienes cuenta?
            <a href="login.jsp" class="text-decoration-none fw-semibold">Volver al login</a>
        </p>
    </form>
</div>

<jsp:include page="fragmentos/footer.jsp" />

<script>
    // Mostrar / ocultar contraseña
    const toggle = document.getElementById('toggleClave');
    const input = document.getElementById('clave');

    toggle.addEventListener('click', () => {
        const type = input.type === 'password' ? 'text' : 'password';
        input.type = type;
        toggle.classList.toggle('fa-eye-slash');
    });

    // Validación dinámica de contraseña
    const reqLength = document.getElementById('reqLength');
    const reqUpper  = document.getElementById('reqUpper');
    const reqNumber = document.getElementById('reqNumber');
    const reqSpecial = document.getElementById('reqSpecial');

    input.addEventListener('input', () => {
        const val = input.value;
        reqLength.classList.toggle('valid', val.length >= 8);
        reqUpper.classList.toggle('valid', /[A-Z]/.test(val));
        reqNumber.classList.toggle('valid', /\d/.test(val));
        reqSpecial.classList.toggle('valid', /[!@#$%^&*]/.test(val));
    });
</script>

</body>
</html>