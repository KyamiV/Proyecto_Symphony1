<%-- 
    Document   : formularioAsignarDocente
    Created on : 21/11/2025, 10:41:32 p. m.
    Author     : camiv
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" session="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%
    // 🔐 Validación de sesión y rol
    String rol = (String) session.getAttribute("rolActivo");
    if (rol == null || !"administrador".equalsIgnoreCase(rol)) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }
%>

<div class="container">
    <form action="${pageContext.request.contextPath}/AsignarDocenteServlet" method="post" class="row g-3">
        
        <!-- Combo de docentes -->
        <div class="col-md-6">
            <label class="form-label">Docente</label>
            <select name="docenteId" class="form-select" required>
                <option value="">Seleccione docente</option>
                <c:forEach var="docente" items="${docentes}">
                    <option value="${docente.id}">${docente.nombre}</option>
                </c:forEach>
            </select>
        </div>

        <!-- Combo de clases -->
        <div class="col-md-6">
            <label class="form-label">Clase</label>
            <select name="claseId" class="form-select" required>
                <option value="">Seleccione clase</option>
                <c:forEach var="clase" items="${clases}">
                    <option value="${clase.id}">${clase.nombreClase}</option>
                </c:forEach>
            </select>
        </div>

        <!-- Instrumento -->
        <div class="col-md-4">
            <label class="form-label">Instrumento</label>
            <input type="text" name="instrumento" class="form-control" required>
        </div>

        <!-- Horario inicio -->
        <div class="col-md-4">
            <label class="form-label">Horario inicio</label>
            <input type="time" name="horaInicio" class="form-control" required>
        </div>

        <!-- Horario fin -->
        <div class="col-md-4">
            <label class="form-label">Horario fin</label>
            <input type="time" name="horaFin" class="form-control" required>
        </div>

        <!-- Aula -->
        <div class="col-md-12">
            <label class="form-label">Aula</label>
            <input type="text" name="aula" class="form-control" required>
        </div>

        <!-- Botón -->
        <div class="col-12 text-center mt-3">
            <button type="submit" class="btn btn-success">
                <i class="fas fa-check"></i> Asignar docente
            </button>
        </div>
    </form>
</div>