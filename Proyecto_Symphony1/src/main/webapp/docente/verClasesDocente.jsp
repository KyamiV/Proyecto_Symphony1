<%--  
    Document   : verClasesDocente  
    Created on : 16/11/2025, 10:11:18 a. m.  
    Author     : camiv  
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" session="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%
    // 🔐 Validación de sesión y rol docente
    String nombre = (String) session.getAttribute("nombreActivo");
    String rol = (String) session.getAttribute("rolActivo");
    Integer idDocente = (Integer) session.getAttribute("idActivo");

    if (nombre == null || rol == null || !"docente".equalsIgnoreCase(rol) || idDocente == null) {
        request.setAttribute("mensaje", "❌ Acceso restringido: requiere rol docente.");
        request.setAttribute("tipoMensaje", "danger");
        request.getRequestDispatcher("/pages/error.jsp").forward(request, response);
        return;
    }
%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Mis clases | SymphonySIAS</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/estilos.css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
</head>
<body class="bg-light">

    <!-- 📂 Encabezado institucional -->
    <jsp:include page="../fragmentos/header.jsp" />

    <div class="container mt-5">
        <h3 class="mb-3">
            <i class="fas fa-chalkboard"></i> Mis clases
            <small class="text-muted">Docente: <%= nombre %></small>
        </h3>

        <!-- 📢 Mensajes institucionales -->
        <c:if test="${not empty mensaje}">
            <div class="alert alert-${tipoMensaje != null ? tipoMensaje : 'info'}">${mensaje}</div>
        </c:if>

        <!-- 📋 Tabla de clases asignadas -->
        <c:choose>
            <c:when test="${not empty clases}">
                <div class="card shadow p-4 mb-4 bg-white rounded-4">
                    <h5 class="mb-3 text-center"><i class="fas fa-chalkboard"></i> Clases asignadas</h5>
                    <div class="table-responsive">
                        <table class="table table-bordered table-hover align-middle">
                            <thead class="table-light text-center">
                                <tr>
                                    <th>Clase</th>
                                    <th>Instrumento</th>
                                    <th>Etapa</th>
                                    <th>Grupo</th>
                                    <th>Cupo</th>
                                    <th>Estado</th>
                                    <th>Acciones</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="clase" items="${clases}">
                                    <tr>
                                        <td>${clase.claseNombre}</td>
                                        <td>${clase.instrumento}</td>
                                        <td>${clase.etapa}</td>
                                        <td>${clase.grupo}</td>
                                        <td>${clase.cupo}</td>
                                        <td>${clase.estadoAsignacion}</td>
                                        <td class="text-center">
                                            <a href="${pageContext.request.contextPath}/RegistrarNotaClaseServlet?claseId=${clase.claseId}" 
                                               class="btn btn-primary btn-sm">
                                                <i class="fas fa-pen"></i> Registrar notas
                                            </a>
                                            <a href="${pageContext.request.contextPath}/ListadoEstudiantesServlet?claseId=${clase.claseId}" 
                                               class="btn btn-secondary btn-sm">
                                                <i class="fas fa-users"></i> Ver estudiantes
                                            </a>
                                            <a href="${pageContext.request.contextPath}/VerNotasPorTablaServlet?claseId=${clase.claseId}" 
                                               class="btn btn-outline-dark btn-sm">
                                                <i class="fas fa-table"></i> Tablas
                                            </a>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </div>
            </c:when>
            <c:otherwise>
                <div class="alert alert-warning">⚠️ No tienes clases asignadas actualmente.</div>
            </c:otherwise>
        </c:choose>

        <!-- 📅 Tabla de horarios institucionales -->
        <c:choose>
            <c:when test="${not empty horarios}">
                <div class="card shadow p-4 mb-4 bg-white rounded-4">
                    <h5 class="mb-3 text-center"><i class="fas fa-clock"></i> Horarios de mis clases</h5>
                    <div class="table-responsive">
                        <table class="table table-striped text-center align-middle">
                            <thead class="table-dark">
                                <tr>
                                    <th>Clase</th>
                                    <th>Instrumento</th>
                                    <th>Etapa</th>
                                    <th>Grupo</th>
                                    <th>Cupo</th>
                                    <th>Inscritos</th>
                                    <th>Día</th>
                                    <th>Fecha</th>
                                    <th>Inicio</th>
                                    <th>Fin</th>
                                    <th>Aula</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="h" items="${horarios}">
                                    <tr>
                                        <td>${h['nombre_clase']}</td>
                                        <td>${h['instrumento']}</td>
                                        <td>${h['etapa']}</td>
                                        <td>${h['grupo']}</td>
                                        <td>${h['cupo']}</td>
                                        <td>${h['inscritos']}</td>
                                        <td>${h['dia']}</td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${not empty h['fecha']}">
                                                    <fmt:formatDate value="${h['fecha']}" pattern="dd MMM yyyy"/>
                                                </c:when>
                                                <c:otherwise>-</c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td>${h['inicio']}</td>
                                        <td>${h['fin']}</td>
                                        <td><c:out value="${h['aula'] != null ? h['aula'] : '-'}"/></td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </div>
            </c:when>
            <c:otherwise>
                <div class="alert alert-info">ℹ️ No hay horarios registrados para tus clases.</div>
            </c:otherwise>
        </c:choose>

        <!-- 🔙 Botón volver al panel -->
        <div class="text-end mt-4">
            <a href="${pageContext.request.contextPath}/PanelDocenteServlet" class="btn btn-secondary">
                <i class="fas fa-arrow-left"></i> Volver al panel
            </a>
        </div>
    </div>

    <!-- 📌 Pie de página -->
    <jsp:include page="../fragmentos/footer.jsp" />

</body>
</html>