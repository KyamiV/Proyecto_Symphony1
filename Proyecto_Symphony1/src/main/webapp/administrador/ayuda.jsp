<%-- 
    Document   : ayuda
    Created on : 23/11/2025, 4:15:17 a. m.
    Author     : camiv
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
    <title>Centro de Ayuda - SymphonySIAS</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/estilos.css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
</head>
<body class="bg-light">

    <!-- Fragmentos institucionales -->
    <jsp:include page="../fragmentos/header.jsp" />
    <jsp:include page="../fragmentos/sidebar.jsp" />

    <div class="container mt-5">
        <h2 class="text-center mb-4">
            <i class="fas fa-question-circle"></i> Centro de Ayuda
        </h2>

        <div class="card shadow-sm mb-4">
            <div class="card-body">
                <h4><i class="fas fa-info-circle"></i> Información general</h4>
                <p>
                    El sistema <strong>SymphonySIAS</strong> está diseñado para facilitar la gestión académica y administrativa.
                    Aquí encontrarás respuestas a las preguntas más frecuentes y guías rápidas para cada rol institucional.
                </p>
            </div>
        </div>

        <!-- Sección Administrador -->
        <div class="card shadow-sm mb-4">
            <div class="card-header bg-success text-white">
                <i class="fas fa-user-shield"></i> Rol Administrador
            </div>
            <div class="card-body">
                <ul>
                    <li>Crear y gestionar clases institucionales.</li>
                    <li>Asignar docentes y administrar horarios.</li>
                    <li>Visualizar indicadores y reportes.</li>
                    <li>Acceder a auditoría y bitácora de acciones.</li>
                </ul>
            </div>
        </div>

        <!-- Sección Docente -->
        <div class="card shadow-sm mb-4">
            <div class="card-header bg-primary text-white">
                <i class="fas fa-chalkboard-teacher"></i> Rol Docente
            </div>
            <div class="card-body">
                <ul>
                    <li>Ver clases asignadas y horarios.</li>
                    <li>Registrar notas y observaciones pedagógicas.</li>
                    <li>Enviar tablas de calificaciones al administrador.</li>
                    <li>Consultar bitácora de acciones propias.</li>
                </ul>
            </div>
        </div>

        <!-- Sección Estudiante -->
        <div class="card shadow-sm mb-4">
            <div class="card-header bg-secondary text-white">
                <i class="fas fa-user-graduate"></i> Rol Estudiante
            </div>
            <div class="card-body">
                <ul>
                    <li>Inscribirse en clases disponibles.</li>
                    <li>Consultar notas y observaciones.</li>
                    <li>Visualizar calendario institucional.</li>
                    <li>Recibir mensajes y notificaciones de docentes.</li>
                </ul>
            </div>
        </div>

        <!-- Contacto -->
        <div class="card shadow-sm">
            <div class="card-header bg-dark text-white">
                <i class="fas fa-envelope"></i> Contacto de soporte
            </div>
            <div class="card-body">
                <p>
                    Si necesitas asistencia adicional, comunícate con el área de soporte institucional:
                </p>
                <ul>
                    <li><i class="fas fa-envelope"></i> Correo: soporte@symphonysias.edu</li>
                    <li><i class="fas fa-phone"></i> Teléfono: +57 123 456 7890</li>
                    <li><i class="fas fa-clock"></i> Horario: Lunes a Viernes, 8:00 AM - 5:00 PM</li>
                </ul>
            </div>
        </div>

        <div class="text-center mt-4">
            <a href="${pageContext.request.contextPath}/index.jsp" class="btn btn-secondary">
                <i class="fas fa-arrow-left"></i> Volver al inicio
            </a>
        </div>
    </div>

    <!-- Pie institucional -->
    <jsp:include page="../fragmentos/footer.jsp" />

</body>
</html>