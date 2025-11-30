<%-- 
    Document   : estudiante.jsp
    Rol        : Estudiante
    Autor      : Camila
    Trazabilidad: Panel principal con indicadores, accesos rápidos y validación de sesión.
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" session="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:choose>
    <c:when test="${empty sessionScope.nombreActivo or empty sessionScope.rolActivo or sessionScope.rolActivo ne 'estudiante'}">
        <c:redirect url="${pageContext.request.contextPath}/login.jsp"/>
    </c:when>
</c:choose>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Panel Estudiante - SymphonySIAS</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;600&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/estilos.css">

    <style>
        body {
            font-family: 'Poppins', sans-serif;
        }
        .dashboard-box {
            background: #fff;
            border-radius: 12px;
            padding: 2rem;
            box-shadow: 0 4px 12px rgba(0,0,0,0.08);
        }
        .indicador-box {
            background: #f8f9fa;
            border-radius: 10px;
            padding: 1rem;
            box-shadow: inset 0 2px 6px rgba(0,0,0,0.05);
            transition: transform 0.2s ease;
        }
        .indicador-box:hover {
            transform: translateY(-3px);
        }
        .btn-dashboard {
            display: inline-block;
            background: #198754;
            color: #fff;
            padding: 1rem;
            border-radius: 10px;
            text-decoration: none;
            font-weight: 600;
            transition: background 0.3s ease;
        }
        .btn-dashboard:hover {
            background: #145c32;
            color: #fff;
        }
        .alert-info {
            border-radius: 10px;
            font-weight: 500;
        }
    </style>
</head>
<body class="bg-light">

    <!-- 📂 Menú lateral institucional -->
    <jsp:include page="../fragmentos/sidebar.jsp" />

    <!-- 🧭 Encabezado institucional -->
    <jsp:include page="../fragmentos/header.jsp" />

    <!-- Contenido principal -->
    <div class="container dashboard-box mt-4">

        <!-- Mensajes institucionales -->
        <c:if test="${not empty sessionScope.mensaje}">
            <div class="alert alert-info text-center mb-4">
                <i class="fas fa-info-circle"></i> ${sessionScope.mensaje}
            </div>
        </c:if>

        <!-- Indicadores -->
        <div class="row text-center mb-4">
            <div class="col-md-4">
                <div class="indicador-box">
                    <h6><i class="fas fa-chalkboard text-info"></i> Clases inscritas</h6>
                    <p class="fs-4 text-primary"><c:out value="${totalClasesInscritas}" default="0"/></p>
                </div>
            </div>
            <div class="col-md-4">
                <div class="indicador-box">
                    <h6><i class="fas fa-book text-success"></i> Notas registradas</h6>
                    <p class="fs-4 text-success"><c:out value="${totalNotasEstudiante}" default="0"/></p>
                </div>
            </div>
            <div class="col-md-4">
                <div class="indicador-box">
                    <h6><i class="fas fa-file-alt text-warning"></i> Certificados emitidos</h6>
                    <p class="fs-4 text-warning"><c:out value="${totalCertificados}" default="0"/></p>
                </div>
            </div>
        </div>

        <!-- Accesos rápidos -->
        <div class="row text-center mb-4">
            <div class="col-md-3 mb-3">
                <a href="${pageContext.request.contextPath}/estudiante/verNotas.jsp" class="btn-dashboard w-100">
                    <i class="fas fa-book"></i> Ver mis notas
                </a>
            </div>
            <div class="col-md-3 mb-3">
                <a href="${pageContext.request.contextPath}/VerClasesEstudianteServlet" class="btn-dashboard w-100">
                    <i class="fas fa-edit"></i> Inscribirme en cursos
                </a>
            </div>
            <div class="col-md-3 mb-3">
                <a href="${pageContext.request.contextPath}/estudiante/certificados.jsp" class="btn-dashboard w-100">
                    <i class="fas fa-file-alt"></i> Mis certificados
                </a>
            </div>
            <!-- 🔗 Nuevo módulo institucional -->
            <div class="col-md-3 mb-3">
                <a href="${pageContext.request.contextPath}/ActividadesEstudianteServlet" class="btn-dashboard w-100">
                    <i class="fas fa-tasks"></i> Mis actividades
                </a>
            </div>
        </div>

        <!-- Botón de cierre de sesión -->
        <div class="text-end mt-4">
            <a href="${pageContext.request.contextPath}/CerrarSesionServlet" class="btn btn-outline-secondary">
                <i class="fas fa-sign-out-alt"></i> Cerrar sesión
            </a>
        </div>
    </div>

    <!-- 📌 Pie de página -->
    <jsp:include page="../fragmentos/footer.jsp" />

</body>
</html>