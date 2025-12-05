<%-- 
    Document   : error.jsp
    Created on : 13/11/2025, 6:59:53 p. m.
    Autor      : camiv
    Propósito  : Mostrar errores institucionales con estilo visual claro y trazabilidad funcional
    Trazabilidad: recibe atributo 'mensaje' desde cualquier servlet que redirija por error
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String mensaje = (String) request.getAttribute("mensaje");
    String error = (String) request.getAttribute("error");
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Error institucional - SymphonySIAS</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/estilos.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
    <style>
        body { font-family: 'Poppins', sans-serif; background-color: #f8d7da; padding: 40px; }
        .error-box { background: #ffffff; padding: 30px; border-radius: 10px;
                     box-shadow: 0 0 10px rgba(0,0,0,0.1); max-width: 700px; margin: 0 auto; text-align: center; }
        .error-icon { font-size: 3rem; color: #dc3545; margin-bottom: 20px; }
        .btn-volver { background-color: #dc3545; color: white; padding: 10px 20px; border-radius: 6px;
                      text-decoration: none; font-weight: 500; }
        .btn-volver:hover { background-color: #bb2d3b; }
    </style>
</head>
<body>
    <div class="error-box" role="alert">
        <img src="<%= request.getContextPath() %>/assets/img/logo.png" alt="Logo SymphonySIAS" style="height: 60px;" class="mb-3">
        <div class="error-icon"><i class="fas fa-exclamation-triangle"></i></div>
        <h2>Error institucional</h2>
        <p class="mt-3">
            <%= (error != null) ? error : (mensaje != null ? mensaje : "Ha ocurrido un error inesperado. Por favor intenta nuevamente.") %>
        </p>
        <div class="mt-4">
            <a href="<%= request.getContextPath() %>/login.jsp" class="btn-volver">
                <i class="fas fa-arrow-left"></i> Volver al inicio
            </a>
        </div>
        <p class="mt-3">
            Si el problema persiste, contacta 
            <a href="<%= request.getContextPath() %>/fragmentos/soporte.jsp" class="text-decoration-none fw-semibold">
                soporte institucional
            </a>.
        </p>
    </div>
</body>
</html>