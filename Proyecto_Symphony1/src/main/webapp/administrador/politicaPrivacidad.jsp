<%-- 
    Document   : politicaPrivacidad
    Created on : 23/11/2025, 4:14:31 a. m.
    Author     : camiv
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
    <title>Política de Privacidad - SymphonySIAS</title>
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
            <i class="fas fa-shield-alt"></i> Política de Privacidad
        </h2>

        <div class="card shadow-sm">
            <div class="card-body">
                <p>
                    En <strong>SymphonySIAS</strong> valoramos y protegemos la información personal de nuestros usuarios.
                    Esta política describe cómo recopilamos, usamos y protegemos los datos en el marco de la gestión institucional.
                </p>

                <h4><i class="fas fa-database"></i> Recopilación de datos</h4>
                <p>
                    Recopilamos únicamente la información necesaria para la gestión académica y administrativa,
                    incluyendo datos de identificación, contacto y trazabilidad de acciones realizadas en el sistema.
                </p>

                <h4><i class="fas fa-user-lock"></i> Uso de la información</h4>
                <p>
                    Los datos se utilizan exclusivamente para fines institucionales: inscripción de estudiantes,
                    asignación de docentes, certificación académica y auditoría de procesos.
                </p>

                <h4><i class="fas fa-lock"></i> Protección y seguridad</h4>
                <p>
                    Implementamos medidas técnicas y organizativas para garantizar la seguridad de la información,
                    evitando accesos no autorizados, pérdidas o alteraciones.
                </p>

                <h4><i class="fas fa-users-cog"></i> Derechos de los usuarios</h4>
                <p>
                    Los usuarios pueden solicitar la actualización, corrección o eliminación de sus datos personales
                    conforme a la normativa vigente de protección de datos.
                </p>

                <h4><i class="fas fa-balance-scale"></i> Cumplimiento normativo</h4>
                <p>
                    SymphonySIAS cumple con las leyes nacionales de protección de datos y se compromete a mantener
                    la transparencia en el manejo de la información institucional.
                </p>

                <div class="text-center mt-4">
                    <a href="${pageContext.request.contextPath}/index.jsp" class="btn btn-secondary">
                        <i class="fas fa-arrow-left"></i> Volver al inicio
                    </a>
                </div>
            </div>
        </div>
    </div>

    <!-- Pie institucional -->
    <jsp:include page="../fragmentos/footer.jsp" />

</body>
</html>