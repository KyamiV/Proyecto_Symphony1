<%-- 
    Document   : fragmentoAsignarDocente
    Created on : 19/11/2025, 4:43:09 p. m.
    Author     : camiv
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Asignar docente - SymphonySIAS</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/estilos.css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
</head>
<body class="bg-light">

    <!-- 🧭 Encabezado institucional -->
    <jsp:include page="../fragmentos/header.jsp" />

    <!-- 📑 Menú lateral -->
    <jsp:include page="../fragmentos/sidebar.jsp" />

    <!-- 📋 Contenedor principal -->
    <div class="container mt-4">
        <h4 class="mb-3 text-center">
            <i class="fas fa-chalkboard-teacher"></i> Asignación de docentes a clases
        </h4>
        <p class="text-center text-muted">
            Selecciona un docente y una clase para completar la asignación institucional.
        </p>

        <!-- Formulario de asignación -->
        <form method="post" action="${pageContext.request.contextPath}/AsignarDocenteServlet" class="row g-3 px-2">
            <!-- Selección de docente -->
            <div class="col-md-6">
                <label class="form-label">Seleccionar docente</label>
                <select name="idDocente" class="form-select" required aria-label="Seleccionar docente">
                    <option value="">-- Selecciona --</option>
                    <c:choose>
                        <c:when test="${not empty docentes}">
                            <c:forEach var="d" items="${docentes}">
                                <option value="${d.id}">${d.nombre}</option>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <option disabled>❌ No hay docentes disponibles</option>
                        </c:otherwise>
                    </c:choose>
                </select>
            </div>

            <!-- Selección de clase -->
            <div class="col-md-6">
                <label class="form-label">Seleccionar clase</label>
                <select name="idClase" class="form-select" required aria-label="Seleccionar clase">
                    <option value="">-- Selecciona --</option>
                    <c:choose>
                        <c:when test="${not empty clases}">
                            <c:forEach var="c" items="${clases}">
                                <option value="${c.id}">${c.nombre} - ${c.instrumento}</option>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <option disabled>❌ No hay clases disponibles</option>
                        </c:otherwise>
                    </c:choose>
                </select>
            </div>

            <!-- Botones -->
            <div class="col-12 text-center mt-4 d-flex justify-content-center gap-3 flex-wrap">
                <button type="submit" class="btn btn-success px-4">
                    <i class="fas fa-check-circle"></i> Asignar docente
                </button>
                <a href="${pageContext.request.contextPath}/gestionarClasesPrincipal.jsp" class="btn btn-secondary px-4">
                    <i class="fas fa-times"></i> Cancelar
                </a>
            </div>
        </form>
    </div>

    <!-- 📌 Pie de página -->
    <jsp:include page="../fragmentos/footer.jsp" />

</body>
</html>