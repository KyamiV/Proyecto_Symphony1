<%-- 
    Document   : index.jsp
    Created on : 13/11/2025, 8:11:22 p. m.
    Autor      : camiv
    Propósito  : Redirigir automáticamente al login institucional de SymphonySIAS
    Trazabilidad: punto de entrada raíz del sistema, sin sesión activa. Redirige a login.jsp con fallback visual.
--%>

<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Redireccionando...</title>
    <meta http-equiv="refresh" content="2;URL=login.jsp">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
    <link rel="stylesheet" href="assets/css/estilos.css">
    <style>
        body {
            font-family: 'Poppins', sans-serif;
            background-color: #f4f6f9;
            text-align: center;
            padding: 100px;
        }
        .redirect-box {
            background: #ffffff;
            padding: 40px;
            border-radius: 10px;
            box-shadow: 0 0 10px rgba(0,0,0,0.08);
            display: inline-block;
        }
        .redirect-box i {
            font-size: 3rem;
            color: #0d6efd;
            margin-bottom: 15px;
        }
    </style>
</head>
<body>
    <div class="redirect-box">
        <i class="fas fa-spinner fa-spin"></i>
        <h2 class="mt-3">Redireccionando al sistema institucional...</h2>
        <p class="mt-2">Si no eres redirigido automáticamente, haz click <a href="login.jsp">aquí</a>.</p>
    </div>
</body>
</html>