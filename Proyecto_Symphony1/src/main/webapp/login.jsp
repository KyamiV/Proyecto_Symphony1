<%-- 
    Página    : login.jsp
    Autor     : camiv
    Propósito : Acceso institucional al sistema SymphonySIAS
    Flujo     : -> LoginServlet (validación, sesión y redirección)
--%>

<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Ingreso - SymphonySIAS</title>

    <!-- Bootstrap -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">

    <!-- Iconos -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">

    <!-- Estilos globales -->
    <link rel="stylesheet" href="assets/css/estilos.css">

    <style>
        body {
            font-family: 'Poppins', sans-serif;
            background: linear-gradient(to right, #eef2f3, #d9e8ff);
            padding: 40px;
        }
        .login-box {
            background: #ffffff;
            padding: 35px;
            border-radius: 12px;
            box-shadow: 0 0 15px rgba(0,0,0,0.12);
            max-width: 480px;
            margin: auto;
        }
        .login-title {
            font-size: 1.7rem;
            font-weight: 600;
            margin-bottom: 20px;
        }
        .btn-ingresar {
            background-color: #0d6efd;
            color: white;
            font-weight: 500;
            transition: 0.2s ease;
        }
        .btn-ingresar:hover {
            background-color: #0b5ed7;
        }
        .logo-symphony {
            max-height: 90px;
            margin-bottom: 15px;
        }
    </style>
</head>

<body>

<div class="login-box text-center mt-4">

    <img src="assets/img/logo.png" alt="Logo SymphonySIAS" class="logo-symphony">

    <div class="login-title text-primary">
        <i class="fas fa-university me-2"></i> Ingreso a SymphonySIAS
    </div>

    <form action="LoginServlet" method="post" class="text-start">
        
        <!-- Usuario -->
        <div class="mb-3">
            <label for="usuario" class="form-label">Correo institucional</label>
            <input type="email"
                   class="form-control"
                   name="usuario"
                   id="usuario"
                   placeholder="usuario@symphony.edu"
                   required
                   aria-label="Usuario institucional">
        </div>

        <!-- Contraseña -->
        <div class="mb-3">
            <label for="clave" class="form-label">Contraseña</label>
            <input type="password"
                   class="form-control"
                   name="clave"
                   id="clave"
                   placeholder="••••••••"
                   required
                   autocomplete="off"
                   aria-label="Contraseña">
        </div>

        <button type="submit" class="btn btn-ingresar w-100 py-2">
            <i class="fas fa-sign-in-alt me-1"></i> Entrar
        </button>
    </form>

    <!-- Alertas -->
    <c:if test="${not empty error}">
        <div class="alert alert-danger mt-3">${error}</div>
    </c:if>

    <c:if test="${not empty mensaje}">
        <div class="alert alert-info mt-3">${mensaje}</div>
    </c:if>

    <!-- Enlace a registro -->
    <p class="mt-4">
        <small class="text-muted">
            ¿No tienes cuenta?
            <a href="registro.jsp" class="fw-semibold text-decoration-none">Crear cuenta</a>
        </small>
    </p>

</div>

<!-- Footer institucional -->
<jsp:include page="fragmentos/footer.jsp" />

</body>
</html>
