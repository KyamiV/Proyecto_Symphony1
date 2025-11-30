<%-- 
    Document   : certificado rol administrador
    Created on : 19/11/2025, 11:50:19 a. m.
    Author     : camiv
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.Map" %>

<%
    // Validación de sesión y rol administrador
    String nombre = (String) session.getAttribute("nombreActivo");
    String rol = (String) session.getAttribute("rolActivo");

    if (nombre == null || rol == null || !"administrador".equalsIgnoreCase(rol)) {
        response.sendRedirect("../login.jsp");
        return;
    }

    // Recuperar el mapa de datos del certificado desde el servlet
    Map<String, String> certificado = (Map<String, String>) request.getAttribute("certificado");

    // 🚫 Si no hay datos, redirigir a la vista de tablas validadas
    if (certificado == null) {
        response.sendRedirect("tablasValidadas.jsp");
        return;
    }
%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Certificado institucional - SymphonySIAS</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;600&display=swap" rel="stylesheet">
    <style>
        body { font-family: 'Poppins', sans-serif; background-color: #ffffff; padding: 60px; }
        .certificado-box { border: 2px solid #198754; padding: 40px; border-radius: 12px; max-width: 900px; margin: 0 auto; background-color: #fefefe; }
        .certificado-header { text-align: center; margin-bottom: 40px; }
        .certificado-header img { max-height: 80px; }
        .certificado-titulo { font-size: 1.8rem; font-weight: 600; color: #198754; margin-top: 20px; }
        .certificado-cuerpo { font-size: 1.1rem; line-height: 1.8; margin-top: 30px; }
        .certificado-datos { margin-top: 30px; }
        .firma { margin-top: 60px; text-align: right; font-weight: 600; }
        .btn-volver { margin-top: 40px; display: block; text-align: center; }
        .footer { margin-top: 30px; text-align: center; font-size: 0.9rem; color: #6c757d; }
    </style>
</head>
<body>

<div class="certificado-box">
    <div class="certificado-header">
        <img src="../assets/img/logo.png" alt="Logo SymphonySIAS">
        <div class="certificado-titulo">Certificado institucional de evaluación musical</div>
    </div>

    <div class="certificado-cuerpo">
        Se certifica que el docente <strong><%= certificado.get("docente") %></strong> 
        ha registrado y validado satisfactoriamente la tabla institucional 
        <strong><%= certificado.get("nombre_tabla") %></strong> correspondiente a la clase 
        <strong><%= certificado.get("clase") %></strong>.

        <div class="certificado-datos">
            <ul>
                <li><strong>Descripción:</strong> <%= certificado.get("descripcion") %></li>
                <li><strong>Fecha de envío:</strong> <%= certificado.get("fecha_envio") %></li>
                <li><strong>Fecha de validación:</strong> <%= certificado.get("fecha_validacion") %></li>
                <li><strong>Cantidad de notas:</strong> <%= certificado.get("cantidad_notas") %></li>
                <li><strong>Promedio general:</strong> <%= certificado.get("promedio_notas") %></li>
                <li><strong>Fecha de emisión:</strong> <%= certificado.get("fecha_emision") %></li>
            </ul>
        </div>

        Este certificado forma parte del proceso institucional de trazabilidad y evaluación académica de SymphonySIAS.
    </div>

    <div class="firma">
        ___________________________<br>
        Coordinación Académica<br>
        SymphonySIAS
    </div>

    <div class="btn-volver">
        <a href="tablasValidadas.jsp" class="btn btn-outline-secondary">
            <i class="fas fa-arrow-left"></i> Volver a tablas validadas
        </a>
    </div>

    <div class="footer">
        Documento generado automáticamente por SymphonySIAS - <%= new java.util.Date() %>
    </div>
</div>

</body>
</html>