<%-- 
    Document   : listadoClases
    Created on : 20/11/2025, 8:13:26 p. m.
    Author     : camiv
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" session="true" %>
<%@ page import="java.util.*" %>
<%@ page import="com.mysymphony.proyecto_symphony1.modelo.Clase" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="es">
<body class="bg-light">

<!-- 🍔 Botón hamburguesa -->
<button class="hamburguesa-btn" onclick="toggleSidebar()">
    <i class="fas fa-bars"></i>
</button>

<!-- 📂 Menú lateral institucional -->
<jsp:include page="/fragmentos/sidebar.jsp" />

<!-- 🧭 Encabezado institucional -->
<jsp:include page="/fragmentos/header.jsp" />

<!-- 📋 Contenido principal -->
<div class="container mt-5">
    <h4 class="text-center mb-4">
        <i class="fas fa-chalkboard-teacher"></i> Clases asignadas al docente
    </h4>

    <c:if test="${not empty clases}">
        <div class="card shadow p-4 mb-4 bg-white rounded-4">
            <p class="text-center text-muted">
                Total clases recibidas: <c:out value="${clases.size()}" />
            </p>
            <div class="table-responsive">
                <table class="tabla-notas">
                    <thead>
                        <tr>
                            <th>Capacidad</th>
                            <th>Horario</th>
                            <th>Día</th>
                            <th>Aula</th>
                            <th>Etapa</th>
                            <th>Instrumento</th>
                            <th>Nombre</th>
                            <th>ID</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="clase" items="${clases}">
                            <tr>
                                <td>${clase.cupo}</td>
                                <td>${clase.inicio} - ${clase.fin}</td>
                                <td>${clase.dia}</td>
                                <td>${clase.aula}</td>
                                <td>${clase.etapa}</td>
                                <td>${clase.instrumento}</td>
                                <td>${clase.nombre}</td>
                                <td>${clase.id}</td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </c:if>

    <c:if test="${empty clases}">
        <div class="alert alert-warning text-center">
            ⚠️ No tienes clases asignadas actualmente.
        </div>
    </c:if>

    <div class="text-center mt-4">
        <a href="<%= request.getContextPath() %>/PanelDocenteServlet" class="btn btn-outline-primary rounded-pill">
            <i class="fas fa-arrow-left"></i> Volver al panel
        </a>
    </div>
</div>

<!-- 📌 Pie de página -->
<jsp:include page="/fragmentos/footer.jsp" />

<!-- ⚙️ Scripts -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
<script>
    function toggleSidebar() {
        document.getElementById("sidebar").classList.toggle("show");
    }
</script>

</body>
</html>