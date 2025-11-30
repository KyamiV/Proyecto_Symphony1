<%--  
    Document   : gestionarClasesPrincipal.jsp
    Rol        : administrador
    Función    : Panel maestro para gestión institucional de clases
    Autor      : camiv
    Trazabilidad: incluye indicadores, clases disponibles, docentes asignados, certificación, auditoría y modales
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" session="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Panel administrador | SymphonySIAS</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/estilos.css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
</head>
<body>

<jsp:include page="../fragmentos/header.jsp" />

<div class="container mt-5">
    <h3 class="text-center mb-4"><i class="fas fa-chalkboard-teacher"></i> Gestión institucional de clases</h3>

    <!-- Mensajes -->
    <c:if test="${not empty mensaje}">
        <div class="alert alert-success text-center">${mensaje}</div>
    </c:if>
    <c:if test="${not empty error}">
        <div class="alert alert-danger text-center">${error}</div>
    </c:if>

    <!-- 📊 Indicadores institucionales -->
    <div class="row text-center mb-4">
        <div class="col-md-3">
            <div class="card bg-light">
                <div class="card-body">
                    <i class="fas fa-chalkboard fa-2x text-primary"></i>
                    <h6>Clases activas</h6>
                    <p>${indicadores.clases_activas}</p>
                </div>
            </div>
        </div>
        <div class="col-md-3">
            <div class="card bg-light">
                <div class="card-body">
                    <i class="fas fa-user-graduate fa-2x text-success"></i>
                    <h6>Estudiantes inscritos</h6>
                    <p>${indicadores.estudiantes_inscritos}</p>
                </div>
            </div>
        </div>
        <div class="col-md-3">
            <div class="card bg-light">
                <div class="card-body">
                    <i class="fas fa-flag fa-2x text-warning"></i>
                    <h6>Etapas pendientes</h6>
                    <p>${indicadores.periodos_pendientes}</p>
                </div>
            </div>
        </div>
        <div class="col-md-3">
            <div class="card bg-light">
                <div class="card-body">
                    <i class="fas fa-certificate fa-2x text-danger"></i>
                    <h6>Certificados pendientes</h6>
                    <p>${indicadores.certificados_pendientes}</p>
                </div>
            </div>
        </div>
    </div>

    <!-- 🔝 Barra de navegación superior -->
    <div class="d-flex justify-content-center mb-4">
        <a href="#clasesDisponibles" class="btn btn-outline-primary mx-2"><i class="fas fa-list"></i> Clases disponibles</a>
        <a href="#clasesDocente" class="btn btn-outline-success mx-2"><i class="fas fa-user-check"></i> Docentes asignados</a>
        <a href="#certificacion" class="btn btn-outline-warning mx-2"><i class="fas fa-certificate"></i> Certificación</a>
    </div>
</div>

<h5 id="clasesDisponibles" class="mb-3"><i class="fas fa-list"></i> Clases disponibles para inscripción</h5>
<input type="text" id="filtroClasesDisponibles" class="form-control mb-2" placeholder="Filtrar clases disponibles...">

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<table class="table table-bordered table-striped" id="tablaClasesDisponibles">
    <thead class="table-dark text-center">
        <tr>
            <th>Clase</th>
            <th>Instrumento</th>
            <th>Etapa</th>
            <th>Grupo</th>
            <th>Cupo</th>
            <th>Inscritos</th>
            <th>Fecha límite</th>
            <th>Fecha inicio</th>
            <th>Fecha fin</th>
            <th>Acciones</th>
        </tr>
    </thead>
    <tbody>
        <c:forEach var="clase" items="${clasesDisponibles}">
            <tr>
                <td>${clase.nombre}</td>
                <td>${clase.instrumento}</td>
                <td>${clase.etapa}</td>
                <td>${clase.grupo}</td>
                <td>${clase.cupo}</td>
                <td>${clase.inscritos}</td>
                <td><fmt:formatDate value="${clase.fechaLimiteDate}" pattern="dd/MM/yyyy"/></td>
                <td><fmt:formatDate value="${clase.fechaInicioDate}" pattern="dd/MM/yyyy"/></td>
                <td><fmt:formatDate value="${clase.fechaFinDate}" pattern="dd/MM/yyyy"/></td>
                <td class="text-center">
                    <!-- Botón Editar -->
                    <a href="#" class="btn btn-sm btn-outline-primary"
                        data-bs-toggle="modal" data-bs-target="#modalEditarClase"
                        data-id="${clase.idClase}"
                        data-nombre="${clase.nombre}"
                        data-instrumento="${clase.instrumento}"
                        data-etapa="${clase.etapa}"
                        data-grupo="${clase.grupo}"
                        data-cupo="${clase.cupo}"
                        data-fechalimite="<fmt:formatDate value='${clase.fechaLimiteDate}' pattern='yyyy-MM-dd'/>"
                        data-fechainicio="<fmt:formatDate value='${clase.fechaInicioDate}' pattern='yyyy-MM-dd'/>"
                        data-fechafin="<fmt:formatDate value='${clase.fechaFinDate}' pattern='yyyy-MM-dd'/>">
                        <i class="fas fa-edit"></i> Editar
                     </a>
                    <!-- Botón Ver clase -->
                    <a href="#"
                       class="btn btn-sm btn-outline-info"
                       data-bs-toggle="modal"
                       data-bs-target="#modalVerClase"
                       data-id="${clase.idClase}"
                       data-nombre="${clase.nombre}"
                       data-instrumento="${clase.instrumento}"
                       data-etapa="${clase.etapa}"
                       data-grupo="${clase.grupo}"
                       data-cupo="${clase.cupo}"
                       data-inscritos="${clase.inscritos}"
                       data-fechalimite="${clase.fechaLimiteDate}"
                       data-fechainicio="${clase.fechaInicioDate}"
                       data-fechafin="${clase.fechaFinDate}">
                       <i class="fas fa-search"></i> Ver
                    </a>

                    <!-- Botón Eliminar -->
                    <form method="post" action="${pageContext.request.contextPath}/EliminarClaseServlet" style="display:inline;">
                        <input type="hidden" name="id" value="${clase.idClase}">
                        <button type="submit" class="btn btn-sm btn-outline-danger"
                                onclick="return confirm('¿Eliminar esta clase?');">
                            <i class="fas fa-trash"></i> Eliminar
                        </button>
                    </form>
                </td>
            </tr>
        </c:forEach>
        <c:if test="${empty clasesDisponibles}">
            <tr>
                <td colspan="10" class="text-center text-muted">No hay clases disponibles.</td>
            </tr>
        </c:if>
    </tbody>
</table>


<div class="text-start mt-3">
    <button type="button" class="btn btn-outline-success" data-bs-toggle="modal" data-bs-target="#modalRegistrarClase">
        <i class="fas fa-plus-circle"></i> Registrar nueva clase institucional
    </button>
</div>

<!-- 👥 Clases con inscritos y docente asignado -->
<h5 id="clasesDocente" class="mb-3"><i class="fas fa-user-check"></i> Docentes asignados</h5>
<input type="text" id="filtroClasesDocente" class="form-control mb-2" placeholder="Filtrar clases con docente...">

<table class="table table-bordered table-hover" id="tablaClasesDocente">
    <thead class="table-light text-center">
        <tr>
            <th>Clase</th><th>Instrumento</th><th>Etapa</th><th>Grupo</th>
            <th>Inscritos</th><th>Docente</th><th>Fecha inicio</th><th>Fecha fin</th><th>Acciones</th>
        </tr>
    </thead>
    <tbody>
        <c:forEach var="clase" items="${clasesConInscritos}">
            <tr>
                <td>${clase.claseNombre}</td>
                <td>${clase.instrumento}</td>
                <td>${clase.etapa}</td>
                <td>${clase.grupo}</td>
                <td>${clase.inscritos}</td>
                <td>${clase.docenteNombre}</td>
                <td>${clase.fechaInicio}</td>
                <td>${clase.fechaFin}</td>
                <td class="text-center">
                    <a href="#" class="btn btn-sm btn-outline-primary"
                        data-bs-toggle="modal" data-bs-target="#modalEditarClase"
                        data-id="${clase.claseId}"
                        data-nombre="${clase.claseNombre}"
                        data-instrumento="${clase.instrumento}"
                        data-fechainicio="${clase.fechaInicio}"
                        data-fechafin="${clase.fechaFin}"
                        data-fechalimite="${clase.fechaLimite}">
                         <i class="fas fa-edit"></i> Editar
                     </a>
                                        <form method="post" action="${pageContext.request.contextPath}/DesasignarDocenteServlet" style="display:inline;">
                        <input type="hidden" name="idClase" value="${clase.idClase}">
                        <button type="submit" class="btn btn-sm btn-outline-danger"
                                onclick="return confirm('¿Desasignar docente de esta clase?');">
                            <i class="fas fa-user-times"></i> Desasignar
                        </button>
                    </form>
                </td>
            </tr>
                </c:forEach>

        <c:if test="${empty clasesConInscritos}">
            <tr>
                <td colspan="9" class="text-center text-muted">
                    No hay clases con docentes asignados.
                </td>
            </tr>
        </c:if>
    </tbody>
</table>

<!-- Botón para abrir el modal de asignar docente -->


<div class="text-center mt-3">
    <button type="button" class="btn btn-outline-success px-4"
            data-bs-toggle="modal"
            data-bs-target="#modalAsignarDocente">
        <i class="fas fa-plus"></i> Asignar docente
    </button>
</div>

<!-- 📜 Certificados emitidos -->
<h3 id="certificadosEmitidos" class="mt-5 mb-3">
    <i class="fas fa-file-alt"></i> Certificados emitidos
</h3>

<!-- 📊 Tabla de certificados emitidos -->
<div class="scroll-table mb-4">
    <table class="table table-bordered table-hover align-middle">
        <thead class="table-light text-center">
            <tr>
                <th>ID Certificado</th>
                <th>ID Estudiante</th>
                <th>ID Clase</th>
                <th>Instrumento</th>
                <th>Etapa</th>
                <th>Fecha emisión</th>
                <th>Usuario admin</th>
                <th>Estado</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="cert" items="${certificadosEmitidos}">
                <tr>
                    <td class="text-center">${cert.id_certificado}</td>
                    <td class="text-center">${cert.id_estudiante}</td>
                    <td class="text-center">${cert.id_clase}</td>
                    <td>${cert.instrumento}</td>
                    <td>${cert.etapa}</td>
                    <td>${cert.fecha_emision}</td>
                    <td>${cert.usuario_admin}</td>
                    <td class="text-center">
                        <c:choose>
                            <c:when test="${cert.estado eq 'Emitido'}">
                                <span class="text-success">
                                    <i class="fas fa-check-circle"></i> Emitido
                                </span>
                            </c:when>
                            <c:when test="${cert.estado eq 'Anulado'}">
                                <span class="text-danger">
                                    <i class="fas fa-times-circle"></i> Anulado
                                </span>
                            </c:when>
                            <c:otherwise>
                                <span class="text-muted">${cert.estado}</span>
                            </c:otherwise>
                        </c:choose>
                    </td>
                </tr>
            </c:forEach>

            <c:if test="${empty certificadosEmitidos}">
                <tr>
                    <td colspan="8" class="text-center text-muted">
                        No hay certificados emitidos aún.
                    </td>
                </tr>
            </c:if>
        </tbody>
    </table>
</div>

<!-- 🔙 Botón institucional -->
<div class="text-end mt-4">
    <a href="${pageContext.request.contextPath}/PanelAdministradorServlet" class="btn btn-secondary">
        <i class="fas fa-arrow-left"></i> Volver al panel
    </a>
</div>            
     <!-- 🪟 Modal para registrar clase -->
     <div class="modal fade" id="modalRegistrarClase" tabindex="-1" aria-hidden="true">
  <div class="modal-dialog modal-lg modal-dialog-centered modal-dialog-scrollable">
    <div class="modal-content">

      <!-- Header azul -->
      <div class="modal-header justify-content-center bg-primary text-white border-bottom">
        <h5 class="modal-title"><i class="fas fa-plus-circle"></i> Registrar nueva clase institucional</h5>
        <button type="button" class="btn-close btn-close-white position-absolute end-0 me-3" data-bs-dismiss="modal"></button>
      </div>

      <!-- Body -->
      <div class="modal-body px-4 py-3 bg-light">
        <form action="<%= request.getContextPath() %>/RegistrarClaseServlet" method="post" class="needs-validation" novalidate>

          <!-- Nombre e instrumento -->
          <div class="row mb-3">
            <div class="col-md-6">
              <label class="form-label">Nombre de la clase</label>
              <input type="text" name="nombreClase" class="form-control" required>
            </div>
            <div class="col-md-6">
              <label class="form-label">Instrumento</label>
              <input type="text" name="instrumento" class="form-control" required>
            </div>
          </div>

          <!-- Etapa y grupo -->
          <div class="row mb-3">
            <div class="col-md-6">
              <label class="form-label">Etapa</label>
              <input type="text" name="etapa" class="form-control" required>
            </div>
            <div class="col-md-6">
              <label class="form-label">Grupo</label>
              <input type="text" name="grupo" class="form-control" required>
            </div>
          </div>

          <!-- Cupo y fecha límite -->
          <div class="row mb-3">
            <div class="col-md-6">
              <label class="form-label">Cupo</label>
              <input type="number" name="cupo" class="form-control" min="1" required>
            </div>
            <div class="col-md-6">
              <label class="form-label">Fecha límite inscripción</label>
              <input type="date" name="fecha_limite" class="form-control" required>
            </div>
          </div>

          <!-- Fechas de inicio y fin -->
          <div class="row mb-3">
            <div class="col-md-6">
              <label class="form-label">Fecha inicio</label>
              <input type="date" name="fecha_inicio" class="form-control" required>
            </div>
            <div class="col-md-6">
              <label class="form-label">Fecha fin</label>
              <input type="date" name="fecha_fin" class="form-control" required>
            </div>
          </div>

          <!-- Botones -->
          <div class="text-center mt-4">
            <button type="submit" class="btn btn-primary px-4 shadow-sm rounded-pill">
              <i class="fas fa-save"></i> Guardar clase
            </button>
            <button type="button" class="btn btn-secondary px-4 shadow-sm rounded-pill" data-bs-dismiss="modal">
              <i class="fas fa-times"></i> Cancelar
            </button>
          </div>

        </form>
      </div>

    </div>
  </div>
</div>

<!-- 🪟 Modal para asignar docente -->
<div class="modal fade" id="modalAsignarDocente" tabindex="-1" aria-hidden="true">
  <div class="modal-dialog modal-lg modal-dialog-centered modal-dialog-scrollable">
    <div class="modal-content">
      <div class="modal-header justify-content-center">
        <h5 class="modal-title"><i class="fas fa-user-plus"></i> Asignar docente</h5>
        <button type="button" class="btn-close position-absolute end-0 me-3" data-bs-dismiss="modal"></button>
      </div>
      <div class="modal-body px-4 py-3 bg-white">
        <form action="<%= request.getContextPath() %>/AsignarDocenteServlet" method="post">
          <div class="row mb-3">
            <div class="col-md-6">
              <label class="form-label">Clase</label>
              <select name="claseId" class="form-select" required>
                <c:forEach var="clase" items="${clasesDisponibles}">
                  <option value="${clase.id}">${clase.nombre} - ${clase.instrumento}</option>
                </c:forEach>
              </select>
            </div>
            <div class="col-md-6">
              <label class="form-label">Docente</label>
              <select name="docenteId" class="form-select" required>
                <c:forEach var="docente" items="${docentes}">
                  <option value="${docente.id}">${docente.nombre}</option>
                </c:forEach>
              </select>
            </div>
          </div>
          <div class="text-center mt-4">
            <button type="submit" class="btn btn-success"><i class="fas fa-user-check"></i> Asignar</button>
            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal"><i class="fas fa-times"></i> Cancelar</button>
          </div>
        </form>
      </div>
    </div>
  </div>
</div>

<!-- 🪟 Modal para editar clase -->
    <!-- Modal Editar Clase -->
<div class="modal fade" id="modalEditarClase" tabindex="-1" aria-hidden="true">
  <div class="modal-dialog modal-lg modal-dialog-centered modal-dialog-scrollable">
    <div class="modal-content">

      <!-- Header azul -->
      <div class="modal-header justify-content-center bg-primary text-white border-bottom">
        <h5 class="modal-title"><i class="fas fa-edit"></i> Editar clase institucional</h5>
        <button type="button" class="btn-close btn-close-white position-absolute end-0 me-3" data-bs-dismiss="modal"></button>
      </div>

      <!-- Body -->
      <div class="modal-body px-4 py-3 bg-light">
        <form action="<%= request.getContextPath() %>/EditarClaseServlet" method="post" class="needs-validation" novalidate>
          <input type="hidden" name="id" id="editarClaseId">

          <!-- Clase e Instrumento -->
          <div class="row mb-3">
            <div class="col-md-6">
              <label class="form-label">Clase</label>
              <input type="text" name="nombreClase" id="editarNombreClase" class="form-control" required>
            </div>
            <div class="col-md-6">
              <label class="form-label">Instrumento</label>
              <input type="text" name="instrumento" id="editarInstrumento" class="form-control" required>
            </div>
          </div>

          <!-- Etapa y Grupo -->
          <div class="row mb-3">
            <div class="col-md-6">
              <label class="form-label">Etapa</label>
              <input type="text" name="etapa" id="editarEtapa" class="form-control" required>
            </div>
            <div class="col-md-6">
              <label class="form-label">Grupo</label>
              <input type="text" name="grupo" id="editarGrupo" class="form-control" required>
            </div>
          </div>

          <!-- Cupo y Fecha límite -->
          <div class="row mb-3">
            <div class="col-md-6">
              <label class="form-label">Cupo</label>
              <input type="number" name="cupo" id="editarCupo" class="form-control" min="1" required>
            </div>
            <div class="col-md-6">
              <label class="form-label">Fecha límite inscripción</label>
              <input type="date" name="fecha_limite" id="editarFechaLimite" class="form-control" required>
            </div>
          </div>

          <!-- Fechas inicio y fin -->
          <div class="row mb-3">
            <div class="col-md-6">
              <label class="form-label">Fecha inicio</label>
              <input type="date" name="fecha_inicio" id="editarFechaInicio" class="form-control" required>
            </div>
            <div class="col-md-6">
              <label class="form-label">Fecha fin</label>
              <input type="date" name="fecha_fin" id="editarFechaFin" class="form-control" required>
            </div>
          </div>

          <!-- Botones -->
          <div class="text-center mt-4">
            <button type="submit" class="btn btn-primary px-4 shadow-sm rounded-pill">
              <i class="fas fa-save"></i> Guardar cambios
            </button>
            <button type="button" class="btn btn-secondary px-4 shadow-sm rounded-pill" data-bs-dismiss="modal">
              <i class="fas fa-times"></i> Cancelar
            </button>
          </div>
        </form>
      </div>

    </div>
  </div>
</div>
          
<!-- 🪟 Modal Ver Clase (información extendida) -->
<div class="modal fade" id="modalVerClase" tabindex="-1" aria-hidden="true">
  <div class="modal-dialog modal-xl modal-dialog-centered modal-dialog-scrollable">
    <div class="modal-content">

      <!-- Header azul institucional -->
      <div class="modal-header justify-content-center bg-info text-white">
        <h5 class="modal-title"><i class="fas fa-search"></i> Información extendida de clase</h5>
        <button type="button" class="btn-close btn-close-white position-absolute end-0 me-3" data-bs-dismiss="modal"></button>
      </div>

      <!-- Body -->
      <div class="modal-body px-4 py-3 bg-light">

        <!-- 🧑‍🏫 Docente asignado -->
        <h6 class="mb-3"><i class="fas fa-chalkboard-teacher"></i> Docente asignado</h6>
        <div class="row mb-2">
          <div class="col-md-6"><strong>Nombre:</strong> <span id="verDocenteNombre"></span></div>
          <div class="col-md-6"><strong>Instrumento:</strong> <span id="verDocenteInstrumento"></span></div>
        </div>
        <div class="row mb-2">
          <div class="col-md-6"><strong>Horario asignado:</strong> <span id="verHorarioAsignado"></span></div>
          <div class="col-md-6"><strong>Clases activas del docente:</strong> <span id="verClasesActivasDocente"></span></div>
        </div>

        <!-- 📊 Indicadores de clase -->
        <h6 class="mt-4 mb-3"><i class="fas fa-chart-bar"></i> Indicadores académicos</h6>
        <div class="row mb-2">
          <div class="col-md-6"><strong>Porcentaje de cupo ocupado:</strong> <span id="verPorcentajeOcupado"></span></div>
          <div class="col-md-6"><strong>Estudiantes con tabla enviada:</strong> <span id="verTablasEnviadas"></span></div>
        </div>
        <div class="row mb-2">
          <div class="col-md-6"><strong>Estudiantes aprobados:</strong> <span id="verEstudiantesAprobados"></span></div>
          <div class="col-md-6"><strong>Estudiantes que finalizaron etapa:</strong> <span id="verFinalizadosEtapa"></span></div>
        </div>

        <!-- 🧾 Historial y trazabilidad -->
        <h6 class="mt-4 mb-3"><i class="fas fa-history"></i> Trazabilidad institucional</h6>
        <div class="row mb-2">
          <div class="col-md-6"><strong>Fecha de creación:</strong> <span id="verFechaCreacionClase"></span></div>
          <div class="col-md-6"><strong>Usuario creador:</strong> <span id="verUsuarioCreadorClase"></span></div>
        </div>
        <div class="row mb-2">
          <div class="col-md-12"><strong>Última acción registrada:</strong> <span id="verUltimaAccionAuditoria"></span></div>
        </div>

        <!-- 📁 Estado académico -->
        <h6 class="mt-4 mb-3"><i class="fas fa-clipboard-check"></i> Estado académico</h6>
        <div class="row mb-2">
          <div class="col-md-6"><strong>Etapas completadas:</strong> <span id="verEtapasCompletadas"></span></div>
          <div class="col-md-6"><strong>Estado actual:</strong> <span id="verEstadoAcademico"></span></div>
        </div>
        <div class="row mb-2">
          <div class="col-md-6"><strong>¿Tabla enviada por docente?</strong> <span id="verTablaEnviada"></span></div>
          <div class="col-md-6"><strong>¿Validada por administrador?</strong> <span id="verValidadaAdministrador"></span></div>
        </div>

        <!-- 👥 Detalles de estudiantes -->
        <h6 class="mt-4 mb-3"><i class="fas fa-user-graduate"></i> Estudiantes inscritos</h6>
        <div class="table-responsive">
          <table class="table table-sm table-striped" id="tablaEstudiantesClaseExtendida">
            <thead class="table-light">
              <tr>
                <th>Nombre</th>
                <th>Correo</th>
                <th>Etapa actual</th>
                <th>¿Tiene notas?</th>
                <th>Condición</th>
              </tr>
            </thead>
            <tbody id="tablaEstudiantesClaseExtendidaBody">
              <!-- Filas dinámicas cargadas por JS -->
            </tbody>
          </table>
        </div>

      </div>
    </div>
  </div>
</div>

<!-- SCRIPT -->
<!-- Script para cargar detalle extendido de clase -->
<script>
function cargarDetalleClase(idClase) {
  fetch('VerClaseServlet?id=' + idClase)
    .then(response => response.json())
    .then(data => {
      // 🧑‍🏫 Docente
      document.getElementById("verDocenteNombre").textContent        = data.nombreDocente;
      document.getElementById("verDocenteInstrumento").textContent   = data.instrumentoDocente;
      document.getElementById("verHorarioAsignado").textContent      = data.horario;
      document.getElementById("verClasesActivasDocente").textContent = data.totalClasesDocente;

      // 📊 Indicadores
      document.getElementById("verPorcentajeOcupado").textContent    = data.porcentajeOcupado + "%";
      document.getElementById("verTablasEnviadas").textContent       = data.tablasEnviadas;
      document.getElementById("verEstudiantesAprobados").textContent = data.estudiantesAprobados;
      document.getElementById("verFinalizadosEtapa").textContent     = data.estudiantesFinalizaronEtapa;

      // 🧾 Historial
      document.getElementById("verFechaCreacionClase").textContent   = data.fechaCreacionClase;
      document.getElementById("verUsuarioCreadorClase").textContent  = data.usuarioCreador;
      document.getElementById("verUltimaAccionAuditoria").textContent= data.ultimaAccionAuditoria;

      // 📁 Estado académico
      document.getElementById("verEtapasCompletadas").textContent    = data.etapasCompletadas;
      document.getElementById("verEstadoAcademico").textContent      = data.estadoAcademico;
      document.getElementById("verTablaEnviada").textContent         = data.tablaEnviadaPorDocente ? "Sí" : "No";
      document.getElementById("verValidadaAdministrador").textContent= data.validadaPorAdministrador ? "Sí" : "No";

      // 👥 Estudiantes inscritos
      const tbody = document.getElementById("tablaEstudiantesClaseExtendidaBody");
      tbody.innerHTML = "";
      if (!data.estudiantesInscritos || data.estudiantesInscritos.length === 0) {
        tbody.innerHTML = '<tr><td colspan="5" class="text-center text-muted">No hay estudiantes inscritos</td></tr>';
      } else {
        data.estudiantesInscritos.forEach(est => {
          const fila = `<tr>
            <td>${est.nombre}</td>
            <td>${est.correo}</td>
            <td>${est.etapaActual}</td>
            <td>${est.tieneNotas ? "Sí" : "No"}</td>
            <td>${est.condicion || ""}</td>
          </tr>`;
          tbody.insertAdjacentHTML("beforeend", fila);
        });
      }
    })
    .catch(err => console.error("❌ Error cargando detalle de clase:", err));
}

// Activar carga automática al abrir modal
document.addEventListener("DOMContentLoaded", () => {
  const modal = document.getElementById("modalVerClase");
  modal.addEventListener("show.bs.modal", function (event) {
    const button = event.relatedTarget;
    const idClase = button.getAttribute("data-id");
    cargarDetalleClase(idClase);
  });
});
</script>

<!-- Script para poblar el modal de edición -->
<script>
document.addEventListener('DOMContentLoaded', function () {
  const modalEditar = document.getElementById('modalEditarClase');

  modalEditar.addEventListener('show.bs.modal', function (event) {
    const button = event.relatedTarget;

    // Función para limpiar fechas con hora
    const limpiarFecha = (valor) => valor?.split(' ')[0] ?? '';

    document.getElementById('editarClaseId').value       = button.getAttribute('data-id') || '';
    document.getElementById('editarNombreClase').value   = button.getAttribute('data-nombre') || '';
    document.getElementById('editarInstrumento').value   = button.getAttribute('data-instrumento') || '';
    document.getElementById('editarEtapa').value         = button.getAttribute('data-etapa') || '';
    document.getElementById('editarGrupo').value         = button.getAttribute('data-grupo') || '';
    document.getElementById('editarCupo').value          = button.getAttribute('data-cupo') || '';
    document.getElementById('editarFechaLimite').value   = limpiarFecha(button.getAttribute('data-fechalimite'));
    document.getElementById('editarFechaInicio').value   = limpiarFecha(button.getAttribute('data-fechainicio'));
    document.getElementById('editarFechaFin').value      = limpiarFecha(button.getAttribute('data-fechafin'));
  });
});
</script>

<!-- Scripts generales -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/panelAdministrador.js"></script>

<!-- Filtros de tablas y modal ver clase -->
<script>
function filtrarTabla(inputId, tablaId) {
  const input = document.getElementById(inputId);
  const filter = input.value.toLowerCase();
  const rows = document.querySelectorAll(`#${tablaId} tbody tr`);
  rows.forEach(row => {
    row.style.display = row.textContent.toLowerCase().includes(filter) ? '' : 'none';
  });
}

document.addEventListener('DOMContentLoaded', function () {
  // 🔍 Filtros dinámicos
  document.getElementById('filtroClasesDisponibles')
          .addEventListener('keyup', () => filtrarTabla('filtroClasesDisponibles','tablaClasesDisponibles'));
  document.getElementById('filtroClasesDocente')
          .addEventListener('keyup', () => filtrarTabla('filtroClasesDocente','tablaClasesDocente'));
  document.getElementById('filtroCertificacion')
          .addEventListener('keyup', () => filtrarTabla('filtroCertificacion','tablaCertificacion'));
  document.getElementById('filtroAuditoria')
          .addEventListener('keyup', () => filtrarTabla('filtroAuditoria','tablaAuditoria'));
});
</script>


</body>
</html>