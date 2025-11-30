<%-- 
    Document   : emitirCertificados
    Created on : 23/11/2025, 2:27:15 a. m.
    Author     : camiv
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
    <title>Emisión de Certificados - SymphonySIAS</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/estilos.css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
</head>
<body class="bg-light">

    <!-- Header y menú institucional -->
    <jsp:include page="../fragmentos/header.jsp" />
    <jsp:include page="../fragmentos/sidebar.jsp" />

    <div class="container mt-5">
        <h3 class="text-center mb-4">
            <i class="fas fa-certificate"></i> Emisión de Certificados
        </h3>

        <!-- Mensajes institucionales -->
        <c:if test="${not empty sessionScope.mensaje}">
            <div class="alert 
                <c:choose>
                    <c:when test="${sessionScope.tipoMensaje == 'success'}">alert-success</c:when>
                    <c:when test="${sessionScope.tipoMensaje == 'error'}">alert-danger</c:when>
                    <c:otherwise>alert-info</c:otherwise>
                </c:choose>
                text-center">
                ${sessionScope.mensaje}
            </div>
            <c:remove var="mensaje" scope="session"/>
            <c:remove var="tipoMensaje" scope="session"/>
        </c:if>

        <!-- Botón emitir certificados -->
        <div class="text-center mb-4">
            <form action="${pageContext.request.contextPath}/EmitirCertificadosServlet" method="post">
                <button type="submit" class="btn btn-success">
                    <i class="fas fa-check-circle"></i> Emitir certificados pendientes
                </button>
            </form>
        </div>

        <!-- Tabla de certificados -->
        <table class="table table-bordered table-striped">
            <thead class="table-success">
                <tr>
                    <th>ID Certificado</th>
                    <th>Estudiante</th>
                    <th>Clase</th>
                    <th>Fecha</th>
                    <th>Estado</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="cert" items="${certificados}">
                    <tr>
                        <td>${cert.id}</td>
                        <td>${cert.estudiante}</td>
                        <td>${cert.clase}</td>
                        <td>${cert.fecha}</td>
                        <td>
                            <c:choose>
                                <c:when test="${cert.estado == 'emitido'}">
                                    <span class="badge bg-success">Emitido</span>
                                </c:when>
                                <c:otherwise>
                                    <span class="badge bg-warning">Pendiente</span>
                                </c:otherwise>
                            </c:choose>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>

    <!-- Footer institucional -->
    <jsp:include page="../fragmentos/footer.jsp" />

</body>
</html>