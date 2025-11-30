<%-- 
    Document   : asignarClase.jsp
    Rol        : administrador
    Created on : 19/11/2025, 7:30:38 p. m.
    Autor      : camiv
    Propósito  : Asignar clases institucionales a docentes y visualizar inscritos
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Asignar clase a docente</title>
    <!-- Estilos institucionales -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/estilos.css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
</head>
<body class="bg-light">

    <!-- Encabezado institucional -->
    <jsp:include page="../fragmentos/header.jsp" />

    <div class="container mt-5">
        <h3 class="text-center mb-4"><i class="fas fa-chalkboard-teacher"></i> Asignación institucional de clases</h3>

        <!-- Mensaje institucional si existe -->
        <c:if test="${not empty mensaje}">
            <div class="alert alert-info text-center"><i class="fas fa-info-circle"></i> ${mensaje}</div>
        </c:if>

        <!-- Formulario para asignar clase a docente -->
        <form method="post" action="AsignarClaseADocenteServlet" class="border rounded p-4 bg-white shadow-sm mb-5">
            <div class="mb-3">
                <label for="claseId" class="form-label">🎼 Clase disponible:</label>
                <select name="claseId" id="claseId" class="form-select" required>
                    <option value="">-- Selecciona una clase --</option>
                    <c:forEach var="clase" items="${clasesDisponibles}">
                        <option value="${clase.id}">
                            ${clase.nombre} (${clase.instrumento}) - Etapa ${clase.etapa}
                        </option>
                    </c:forEach>
                </select>
            </div>

            <div class="mb-3">
                <label for="docenteId" class="form-label">👤 Docente:</label>
                <select name="docenteId" id="docenteId" class="form-select" required>
                    <option value="">-- Selecciona un docente --</option>
                    <c:forEach var="docente" items="${docentes}">
                        <option value="${docente.id}">${docente.nombre}</option>
                    </c:forEach>
                </select>
            </div>

            <div class="d-flex justify-content-between">
                <button type="submit" class="btn btn-success">
                    <i class="fas fa-check-circle"></i> Asignar clase
                </button>
                <a href="panelAdministrador.jsp" class="btn btn-secondary">
                    <i class="fas fa-arrow-left"></i> Volver al panel
                </a>
            </div>
        </form>

        <!-- Tabla de clases ya asignadas con inscritos -->
        <h4 class="mb-3"><i class="fas fa-users"></i> Clases con docente asignado e inscritos</h4>
        <div class="table-responsive">
            <table class="table table-bordered table-striped align-middle">
                <thead class="table-dark text-center">
                    <tr>
                        <th>Nombre</th>
                        <th>Instrumento</th>
                        <th>Etapa</th>
                        <th>Grupo</th>
                        <th>Inscritos</th>
                        <th>Docente</th>
                    </tr>
                </thead>
                <tbody>
                    <c:choose>
                        <%-- Si hay clases asignadas con inscritos --%>
                        <c:when test="${not empty clasesConDocente}">
                            <c:forEach var="clase" items="${clasesConDocente}">
                                <tr>
                                    <td>${clase.nombre}</td>
                                    <td>${clase.instrumento}</td>
                                    <td>${clase.etapa}</td>
                                    <td>${clase.grupo}</td>
                                    <td class="text-center">${clase.inscritos}</td>
                                    <td>${clase.docente}</td>
                                    <td>
                                    <form action="DesasignarDocenteServlet" method="post" onsubmit="return confirm('¿Estás seguro de desasignar al docente?');">
                                        <input type="hidden" name="idClase" value="${clase.id_clase}">
                                        <button type="submit" class="btn btn-danger btn-sm">❌ Desasignar</button>
                                        </form>
                                    </td>
                                </tr>
                            </c:forEach>
                        </c:when>
                        <%-- Si no hay clases asignadas --%>
                        <c:otherwise>
                            <tr>
                                <td colspan="6" class="text-center text-muted">No hay clases asignadas con inscritos.</td>
                            </tr>
                        </c:otherwise>
                    </c:choose>
                </tbody>
            </table>
        </div>
    </div>

    <!-- Pie de página institucional -->
    <jsp:include page="../fragmentos/footer.jsp" />

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>