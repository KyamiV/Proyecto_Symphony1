<%-- 
    Document   : soporte
    Created on : 4/12/2025, 5:18:05 a. m.
    Author     : camiv
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Soporte institucional - SymphonySIAS</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/estilos.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
    <style>
        body { font-family: 'Poppins', sans-serif; background-color: #f8f9fa; padding: 40px; }
        .soporte-box { background: #ffffff; padding: 30px; border-radius: 10px;
                       box-shadow: 0 0 10px rgba(0,0,0,0.1); max-width: 600px; margin: 0 auto; text-align: center; }
        .soporte-icon { font-size: 3rem; color: #0d6efd; margin-bottom: 20px; }
    </style>
</head>
<body>
    <div class="soporte-box">
        <img src="<%= request.getContextPath() %>/assets/img/logo.png" alt="Logo SymphonySIAS" style="height: 60px;" class="mb-3">
        <div class="soporte-icon"><i class="fas fa-headset"></i></div>
        <h2>Soporte institucional</h2>
        <p class="mt-3">Si tienes problemas con el sistema, comunícate a través de los siguientes canales:</p>
        
        <ul class="list-unstyled mt-4">
            <li><i class="fas fa-phone"></i> Línea nacional: <strong>01-8000-123-456</strong></li>
            <li><i class="fas fa-phone-alt"></i> Bogotá: <strong>(1) 234-5678</strong></li>
            <li><i class="fas fa-envelope"></i> Correo institucional: <strong>soporte@symphony.edu</strong></li>
            <li><i class="fas fa-comments"></i> Chat institucional: <a href="<%= request.getContextPath() %>" class="text-decoration-none">Acceder al chat</a></li>
        </ul>

        <div class="mt-4">
            <a href="<%= request.getContextPath() %>/login.jsp" class="btn btn-primary">
                <i class="fas fa-arrow-left"></i> Volver al inicio
            </a>
        </div>
    </div>
</body>
</html>