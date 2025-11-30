<%-- 
    Document   : fragmentoRegistrarClase
    Created on : 19/11/2025, 4:12:09 p. m.
    Author     : camiv
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    String mensaje = (String) request.getAttribute("mensaje");
    String error = (String) request.getAttribute("error");
%>

<!-- Mensajes de respuesta -->
<% if (mensaje != null) { %>
    <div class="alert alert-success text-center"><%= mensaje %></div>
<% } %>

<% if (error != null) { %>
    <div class="alert alert-danger text-center"><%= error %></div>
<% } %>

<form method="post"
      action="<%= request.getContextPath() %>/RegistrarClaseServlet"
      class="row g-3 px-2 needs-validation"
      novalidate>

    <div class="col-md-4">
        <label class="form-label">Nombre de la clase</label>
        <input type="text" name="nombreClase" class="form-control" required>
    </div>

    <div class="col-md-3">
        <label class="form-label">Instrumento</label>
        <input type="text" name="instrumento" class="form-control" required>
    </div>

    <div class="col-md-3">
        <label class="form-label">Etapa pedagógica</label>
        <input type="text" name="etapa" class="form-control" required>
    </div>

    <div class="col-md-2">
        <label class="form-label">Grupo</label>
        <input type="text" name="grupo" class="form-control" required>
    </div>

    <div class="col-md-2">
        <label class="form-label">Cupo</label>
        <input type="number" name="cupo" class="form-control" min="1" required>
    </div>

    <div class="col-md-3">
        <label class="form-label">Fecha límite de inscripción</label>
        <input type="date" name="fecha_limite" class="form-control" required>
    </div>

    <div class="col-md-3">
        <label class="form-label">Fecha inicio</label>
        <input type="date" name="fecha_inicio" class="form-control" required>
    </div>

    <div class="col-md-3">
        <label class="form-label">Fecha fin</label>
        <input type="date" name="fecha_fin" class="form-control" required>
    </div>

    <div class="col-12 text-center mt-4 d-flex justify-content-center gap-3 flex-wrap">
        <button type="submit" class="btn btn-success px-4">
            <i class="fas fa-save"></i> Registrar clase
        </button>

        <button type="button" class="btn btn-secondary px-4" data-bs-dismiss="modal">
            <i class="fas fa-times"></i> Cancelar
        </button>
    </div>

</form>
