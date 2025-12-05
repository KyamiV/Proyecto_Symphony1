<%-- 
    Document   : verEstudiante
    Created on : 25/11/2025, 3:38:44 p. m.
    Author     : camiv
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Clases inscritas del estudiante</title>
    <!-- Bootstrap -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- FontAwesome -->
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
    <style>
        body { font-family: 'Poppins', sans-serif; background-color: #f4f6f9; }
        .titulo-panel { background-color: #198754; color: white; padding: 15px; border-radius: 0 0 12px 12px;
                        box-shadow: 0 4px 10px rgba(0,0,0,0.1); text-align: center; }
        table thead th { position: sticky; top: 0; background: #e9ecef; }
    </style>
</head>
<body class="bg-light">

    <!-- ✅ Header institucional -->
    <jsp:include page="../fragmentos/header.jsp" />
    <jsp:include page="../fragmentos/sidebar.jsp" />

    <main class="container mt-4">
        <!-- Título -->
        <div class="titulo-panel mb-4">
            <h2>
                📚 Clases inscritas del estudiante ID: ${idEstudiante}
            </h2>
        </div>

        <!-- ✅ Mensajes institucionales -->
        <c:if test="${not empty mensaje}">
            <div class="alert alert-${tipoMensaje}">
                ${mensaje}
            </div>
        </c:if>

        <!-- ✅ Tabla de clases inscritas -->
        <c:choose>
            <c:when test="${not empty inscripciones}">
                <div class="table-responsive">
                    <table class="table table-bordered table-hover align-middle">
                        <thead class="table-success text-center">
                            <tr>
                                <th>ID Clase</th>
                                <th>Nombre de la clase</th>
                                <th>Instrumento</th>
                                <th>Etapa</th>
                                <th>Grupo</th>
                                <th>Estado</th>
                                <th>Fecha inscripción</th>
                                <th>Nota</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="fila" items="${inscripciones}">
                                <tr>
                                    <td class="text-center">${fila.idClase}</td>
                                    <td>${fila.nombreClase}</td>
                                    <td>${fila.instrumento}</td>
                                    <td class="text-center">${fila.etapaClase}</td>
                                    <td class="text-center">${fila.grupo}</td>
                                    <td class="text-center">${fila.estadoClase}</td>
                                    <td class="text-center">${fila.fechaInscripcion}</td>
                                    <td class="text-center">${fila.nota}</td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
                <div class="mt-2 text-muted">
                    Total de clases inscritas: ${totalInscritos}
                </div>
            </c:when>
            <c:otherwise>
                <div class="alert alert-warning">
                    <i class="fas fa-exclamation-circle"></i> El estudiante no tiene clases inscritas.
                </div>
            </c:otherwise>
        </c:choose>

        <!-- ✅ Botón volver -->
        <div class="mt-3">
            <a href="PanelAdministradorServlet" class="btn btn-outline-secondary">
                <i class="fas fa-arrow-left"></i> Volver al panel administrador
            </a>
        </div>
    </main>

    <!-- ✅ Footer institucional -->
    <jsp:include page="../fragmentos/footer.jsp" />

    <!-- Bootstrap JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>