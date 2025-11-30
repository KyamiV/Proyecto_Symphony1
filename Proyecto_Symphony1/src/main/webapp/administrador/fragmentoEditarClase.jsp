<%-- 
    Document   : fragmentoEditarClase
    Created on : 19/11/2025, 4:14:11 p. m.
    Author     : camiv
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.mysymphony.proyecto_symphony1.modelo.Clase" %>
<%@ page import="java.time.format.DateTimeFormatter" %>

<%
    Clase clase = (Clase) request.getAttribute("clase");

    if (clase == null) {
%>
        <div class="alert alert-danger text-center my-3">
            ❌ No se pudo cargar la información de la clase.
        </div>
<%
        return;
    }

    // Asegurar formato correcto para inputs tipo date
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    String fechaLimite = (clase.getFechaLimite() != null)
            ? clase.getFechaLimite().format(formatter)
            : "";

    String fechaInicio = (clase.getFechaInicio() != null)
            ? clase.getFechaInicio().toLocalDate().format(formatter)
            : "";

    String fechaFin = (clase.getFechaFin() != null)
            ? clase.getFechaFin().toLocalDate().format(formatter)
            : "";
%>

<form method="post"
      action="<%= request.getContextPath() %>/EditarClaseServlet"
      class="row g-3 px-2 needs-validation"
      novalidate>

    <input type="hidden" name="idClase" value="<%= clase.getId() %>">

    <div class="col-md-4">
        <label class="form-label">Nombre de la clase</label>
        <input type="text" name="nombreClase" class="form-control"
               value="<%= clase.getNombre() %>" required>
    </div>

    <div class="col-md-3">
        <label class="form-label">Instrumento</label>
        <input type="text" name="instrumento" class="form-control"
               value="<%= clase.getInstrumento() %>" required>
    </div>

    <div class="col-md-3">
        <label class="form-label">Etapa pedagógica</label>
        <input type="text" name="etapa" class="form-control"
               value="<%= clase.getEtapa() %>" required>
    </div>

    <div class="col-md-2">
        <label class="form-label">Grupo</label>
        <input type="text" name="grupo" class="form-control"
               value="<%= clase.getGrupo() %>" required>
    </div>

    <div class="col-md-2">
        <label class="form-label">Cupo</label>
        <input type="number" name="cupo" class="form-control"
               value="<%= clase.getCupo() %>" min="1" required>
    </div>

    <div class="col-md-3">
        <label class="form-label">Fecha límite de inscripción</label>
        <input type="date" name="fecha_limite" class="form-control"
               value="<%= fechaLimite %>" required>
    </div>

    <div class="col-md-3">
        <label class="form-label">Fecha de inicio</label>
        <input type="date" name="fecha_inicio" class="form-control"
               value="<%= fechaInicio %>" required>
    </div>

    <div class="col-md-3">
        <label class="form-label">Fecha de finalización</label>
        <input type="date" name="fecha_fin" class="form-control"
               value="<%= fechaFin %>" required>
    </div>

    <div class="col-12 text-center mt-4 d-flex justify-content-center gap-3 flex-wrap">
        <button type="submit" class="btn btn-primary px-4">
            <i class="fas fa-save"></i> Actualizar clase
        </button>

        <button type="button" class="btn btn-secondary px-4" data-bs-dismiss="modal">
            <i class="fas fa-times"></i> Cancelar
        </button>
    </div>
</form>
