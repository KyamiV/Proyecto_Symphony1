<%--  
    Document   : registrarNotas  
    Created on : 27/11/2025  
    Author     : Camila  
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" session="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%
    String nombre = (String) session.getAttribute("nombreActivo");
    String rol = (String) session.getAttribute("rolActivo");
    if (nombre == null || !"docente".equalsIgnoreCase(rol)) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }
%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Gestión de notas por clase - SymphonySIAS</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
    <style>
        body {
            background-color: #f4f6f9;
            font-family: 'Poppins', sans-serif;
        }
        .dashboard-box {
            background: #ffffff;
            padding: 25px;
            border-radius: 10px;
            box-shadow: 0 0 10px rgba(0,0,0,0.08);
            margin-bottom: 30px;
        }
        .tabla-notas th, .tabla-notas td {
            vertical-align: middle;
            text-align: center;
        }
        .form-control-plaintext {
            border: none;
            background: transparent;
        }
        .btn-success { background-color: #198754; border-color: #198754; }
        .btn-primary { background-color: #0d6efd; border-color: #0d6efd; }
    </style>
</head>
<body class="p-4">

<jsp:include page="../fragmentos/header.jsp" />

<div class="container">
    <div class="dashboard-box">
        <h4 class="text-center text-success mb-4">
            <i class="fas fa-chalkboard-teacher"></i> Gestión de Notas por Clase
        </h4>

        <!-- 📋 Datos de la clase -->
        <div class="text-center mb-3">
            <strong><i class="fas fa-door-open"></i> Aula:</strong> ${aula} &nbsp; | &nbsp;
            <strong><i class="fas fa-clock"></i> Horario:</strong> ${horario}
        </div>

        <!-- 📢 Mensajes -->
        <c:if test="${not empty sessionScope.mensaje}">
            <div class="alert alert-${sessionScope.tipoMensaje} text-center">
                <i class="fas fa-info-circle"></i> ${sessionScope.mensaje}
            </div>
        </c:if>

        <!-- 📝 Botón para abrir modal de registro -->
        <div class="text-end mb-3">
            <button type="button" class="btn btn-success" data-bs-toggle="modal" data-bs-target="#registrarNotaModal">
                <i class="fas fa-pen"></i> Registrar Nota
            </button>
        </div>

        <!-- 🔎 Filtros -->
        <form action="${pageContext.request.contextPath}/VerNotasClaseServlet" method="get" class="row g-3 mb-4">
            <div class="col-md-5">
                <select name="competencia" class="form-select">
                    <option value="">Todas las competencias</option>
                    <option value="Principal" ${param.competencia == 'Principal' ? 'selected' : ''}>Principal</option>
                    <option value="Técnica" ${param.competencia == 'Técnica' ? 'selected' : ''}>Técnica</option>
                </select>
            </div>
            <div class="col-md-5">
                <input type="date" name="fecha" value="${param.fecha}" class="form-control" />
            </div>
            <div class="col-md-2 text-end">
                <button type="submit" class="btn btn-primary w-100">
                    <i class="fas fa-filter"></i> Filtrar
                </button>
            </div>
        </form>

        <!-- 📊 Tabla de notas -->
        <c:choose>
            <c:when test="${not empty notas}">
                <div class="table-responsive">
                    <table class="table table-bordered table-hover tabla-notas">
                        <thead class="table-success text-white">
                            <tr>
                                <th>ID Clase</th>
                                <th>Estudiante</th>
                                <th>Competencia</th>
                                <th>Nota</th>
                                <th>Observación</th>
                                <th>Fecha</th>
                                <th>Acciones</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="nota" items="${notas}">
                                <tr>
                                    <form method="post" action="${pageContext.request.contextPath}/EditarNotaClaseServlet">
                                        <td>${claseId}</td>
                                        <td>${nota.estudiante}</td>
                                        <td><input type="text" name="competencia" value="${nota.competencia}" class="form-control form-control-sm text-center" required /></td>
                                        <td><input type="number" name="nota" step="0.1" min="0" max="5" value="${nota.nota}" class="form-control form-control-sm text-center" required /></td>
                                        <td><input type="text" name="observacion" value="${nota.observacion}" class="form-control form-control-sm text-center" /></td>
                                        <td>${nota.fecha}<input type="hidden" name="fecha" value="${nota.fecha}" /></td>
                                        <td class="text-center d-flex justify-content-center gap-2">
                                            <input type="hidden" name="notaId" value="${nota.id}" />
                                            <input type="hidden" name="claseId" value="${claseId}" />
                                            <button type="submit" class="btn btn-success btn-sm"><i class="fas fa-save"></i></button>
                                    </form>
                                    <form method="post" action="${pageContext.request.contextPath}/EliminarNotaClaseServlet">
                                        <input type="hidden" name="notaId" value="${nota.id}" />
                                        <input type="hidden" name="claseId" value="${claseId}" />
                                        <button type="submit" class="btn btn-danger btn-sm"
                                                onclick="return confirm('¿Eliminar esta nota?')">
                                            <i class="fas fa-trash-alt"></i>
                                        </button>
                                    </form>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </c:when>
            <c:otherwise>
                <div class="alert alert-warning text-center">
                    <i class="fas fa-exclamation-triangle"></i> No hay notas registradas para esta clase.
                </div>
            </c:otherwise>
        </c:choose>

        <!-- 🔙 Botones globales -->
        <div class="mt-4 text-center">
            <a href="${pageContext.request.contextPath}/PanelDocenteServlet" class="btn btn-outline-secondary me-2">
                <i class="fas fa-arrow-left"></i> Volver
            </a>
            <a href="${pageContext.request.contextPath}/VerNotasDocenteServlet?claseId=${claseId}" class="btn btn-info me-2">
                <i class="fas fa-eye"></i> Ver Tabla
            </a>
            <button type="button" class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#guardarTablaModal">
                <i class="fas fa-save"></i> Guardar Tabla
            </button>
        </div>
    </div>
</div>

<!-- 📝 Modal de registro -->
<div class="modal fade" id="registrarNotaModal" tabindex="-1" aria-labelledby="registrarNotaLabel" aria-hidden="true">
  <div class="modal-dialog modal-lg modal-dialog-centered">
    <div class="modal-content">
      <form method="post" action="${pageContext.request.contextPath}/RegistrarNotaClaseServlet">
        <div class="modal-header bg-success text-white">
          <h5 class="modal-title" id="registrarNotaLabel"><i class="fas fa-pen"></i> Registrar nueva calificación</h5>
          <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"></button>
        </div>
        <div class="modal-body row g-3">
          <div class="col-md-4">
            <label class="form-label">Estudiante</label>
            <select name="idEstudiante" class="form-select" required>
              <option value="">Seleccione estudiante</option>
                            <c:forEach var="est" items="${estudiantes}">
                <option value="${est.idEstudiante}">
                  ${est.nombre} ${est.apellido} - ${est.instrumento} (${est.etapaPedagogica})
                </option>
              </c:forEach>
            </select>
          </div>

          <!-- Competencia -->
          <div class="col-md-3">
            <label class="form-label">Competencia</label>
            <input type="text" name="competencia" class="form-control" placeholder="Ej: Técnica, Ritmo..." required>
          </div>

          <!-- Nota -->
          <div class="col-md-2">
            <label class="form-label">Nota</label>
            <input type="number" name="nota" step="0.1" min="0" max="5" class="form-control" required>
          </div>

          <!-- Observación -->
          <div class="col-md-3">
            <label class="form-label">Observación</label>
            <input type="text" name="observacion" class="form-control" placeholder="Opcional">
          </div>

          <!-- Fecha -->
          <div class="col-md-3">
            <label class="form-label">Fecha</label>
            <input type="date" name="fecha" class="form-control" required>
          </div>

          <!-- Trazabilidad oculta -->
          <input type="hidden" name="claseId" value="${claseId}">
          <input type="hidden" name="instrumento" value="${instrumento}">
          <input type="hidden" name="etapa" value="${etapa}">
        </div>

        <div class="modal-footer">
          <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">
            <i class="fas fa-times"></i> Cancelar
          </button>
          <button type="submit" class="btn btn-success">
            <i class="fas fa-save"></i> Guardar Nota
          </button>
        </div>
      </form>
    </div>
  </div>
</div>

<!-- Modal para guardar tabla institucional -->
<div class="modal fade" id="guardarTablaModal" tabindex="-1" aria-labelledby="guardarTablaLabel" aria-hidden="true">
  <div class="modal-dialog modal-sm modal-dialog-centered">
    <div class="modal-content">
      <form method="post" action="${pageContext.request.contextPath}/GuardarTablaNotasServlet">
        <div class="modal-header bg-success text-white">
          <h5 class="modal-title" id="guardarTablaLabel">
            <i class="fas fa-save"></i> Guardar Tabla Institucional
          </h5>
          <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"></button>
        </div>
        <div class="modal-body">
          <input type="hidden" name="claseId" value="${claseId}">
          <div class="mb-3">
            <label class="form-label"><i class="fas fa-heading"></i> Nombre de la tabla</label>
            <input type="text" name="nombreTabla" class="form-control" placeholder="Ej: Tabla de notas 1" required>
          </div>
          <div class="mb-3">
            <label class="form-label"><i class="fas fa-comment-dots"></i> Observaciones</label>
            <textarea name="observaciones" class="form-control" rows="3" placeholder="Comentarios adicionales..."></textarea>
          </div>
        </div>
        <div class="modal-footer d-flex justify-content-between">
          <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">
            <i class="fas fa-times"></i> Cancelar
          </button>
          <button type="submit" class="btn btn-success">
            <i class="fas fa-check"></i> Guardar
          </button>
        </div>
      </form>
    </div>
  </div>
</div>

<jsp:include page="../fragmentos/footer.jsp" />
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>