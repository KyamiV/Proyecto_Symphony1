<%--  
    Document   : gestionarDocentes  
    Created on : 23/11/2025, 2:10:50 a. m.  
    Author     : camiv  
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Gestión de Docentes - SymphonySIAS</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/estilos.css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
</head>
<body class="bg-light">

    <!-- Fragmentos institucionales -->
    <jsp:include page="../fragmentos/header.jsp" />
    <jsp:include page="../fragmentos/sidebar.jsp" />

    <div class="container mt-5">
        <h2 class="text-center mb-4">
            <i class="fas fa-users"></i> Gestión Institucional de Docentes
        </h2>

        <!-- Mensajes -->
        <c:if test="${not empty sessionScope.mensaje}">
            <div class="alert alert-info text-center">
                ${sessionScope.mensaje}
            </div>
            <c:remove var="mensaje" scope="session"/>
        </c:if>

        <!-- Tabla de docentes -->
        <div class="table-responsive mb-4">
            <table class="table table-bordered table-striped align-middle">
                <thead class="table-success text-center">
                    <tr>
                        <th>ID</th>
                        <th>Nombre</th>
                        <th>Apellido</th>
                        <th>Correo</th>
                        <th>Teléfono</th>
                        <th>Dirección</th>
                        <th>Fecha Ingreso</th>
                        <th>Estado</th>
                        <th>Acciones</th>
                    </tr>
                </thead>
                <tbody>
                <c:forEach var="docente" items="${docentes}">
                    <tr>
                        <td>${docente.id}</td>
                        <td>${docente.nombre}</td>
                        <td>${docente.apellido}</td>
                        <td>${docente.correo}</td>
                        <td>${docente.telefono}</td>
                        <td>${docente.direccion}</td>
                        <td>${docente.fechaIngreso}</td>
                        <td>
                            <c:choose>
                                <c:when test="${docente.estado == 'activo'}">
                                    <span class="badge bg-success">Activo</span>
                                </c:when>
                                <c:otherwise>
                                    <span class="badge bg-secondary">Inactivo</span>
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td class="text-center">
                            <!-- Actualizar -->
                            <form action="GestionarDocentesServlet" method="post" class="d-inline">
                                <input type="hidden" name="accion" value="editar"/>
                                <input type="hidden" name="id_docente" value="${docente.id}"/>
                                <button type="submit" class="btn btn-sm btn-primary">
                                    <i class="fas fa-edit"></i> Actualizar
                                </button>
                            </form>
                            <!-- Eliminar -->
                            <form action="GestionarDocentesServlet" method="post" class="d-inline">
                                <input type="hidden" name="accion" value="eliminar"/>
                                <input type="hidden" name="id_docente" value="${docente.id}"/>
                                <button type="submit" class="btn btn-sm btn-danger"
                                        onclick="return confirm('¿Seguro que deseas eliminar este docente?')">
                                    <i class="fas fa-trash"></i> Eliminar
                                </button>
                            </form>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>

        <!-- Formulario registrar nuevo docente -->
        <div class="card shadow-sm">
            <div class="card-header bg-success text-white">
                <i class="fas fa-user-plus"></i> Registrar nuevo docente
            </div>
            <div class="card-body">
                <form action="GestionarDocentesServlet" method="post" class="row g-3">
                    <input type="hidden" name="accion" value="registrar"/>

                    <div class="col-md-6">
                        <label class="form-label">Nombre</label>
                        <input type="text" name="nombre" class="form-control" required/>
                    </div>
                    <div class="col-md-6">
                        <label class="form-label">Apellido</label>
                        <input type="text" name="apellido" class="form-control" required/>
                    </div>
                    <div class="col-md-6">
                        <label class="form-label">Correo</label>
                        <input type="email" name="correo" class="form-control" required/>
                    </div>
                    <div class="col-md-6">
                        <label class="form-label">Teléfono</label>
                        <input type="text" name="telefono" class="form-control"/>
                    </div>
                    <div class="col-md-6">
                        <label class="form-label">Dirección</label>
                        <input type="text" name="direccion" class="form-control"/>
                    </div>
                    <div class="col-md-6">
                        <label class="form-label">Fecha Ingreso</label>
                        <input type="date" name="fecha_ingreso" class="form-control" required/>
                    </div>
                    <div class="col-md-6">
                        <label class="form-label">Estado</label>
                        <select name="estado" class="form-select">
                            <option value="activo">Activo</option>
                            <option value="inactivo">Inactivo</option>
                        </select>
                    </div>

                    <div class="col-12 text-center mt-3">
                        <button type="submit" class="btn btn-success px-4">
                            <i class="fas fa-check"></i> Registrar
                        </button>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <!-- Pie institucional -->
    <jsp:include page="../fragmentos/footer.jsp" />

    <!-- Modal edición docente -->
    <c:if test="${not empty docenteEditar}">
        <div class="modal fade show" id="modalEditarDocente" tabindex="-1" aria-hidden="true" style="display:block;">
            <div class="modal-dialog modal-lg">
                <div class="modal-content">
                    <form action="GestionarDocentesServlet" method="post">
                        <input type="hidden" name="accion" value="actualizar"/>
                        <input type="hidden" name="id_docente" value="${docenteEditar.id}"/>

                        <div class="modal-header bg-primary text-white">
                            <h5 class="modal-title"><i class="fas fa-edit"></i> Editar docente</h5>
                            <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                        </div>

                        <div class="modal-body">
                            <div class="mb-3">
                                <label>Nombre</label>
                                <input type="text" name="nombre" class="form-control" value="${docenteEditar.nombre}" required/>
                            </div>
                            <div class="mb-3">
                                <label>Apellido</label>
                                <input type="text" name="apellido" class="form-control" value="${docenteEditar.apellido}" required/>
                            </div>
                            <div class="mb-3">
                                <label>Correo</label>
                                <input type="email" name="correo" class="form-control" value="${docenteEditar.correo}" required/>
                            </div>
                            <div class="mb-3">
                                <label>Teléfono</label>
                                <input type="text" name="telefono" class="form-control" value="${docenteEditar.telefono}"/>
                            </div>
                            <div class="mb-3">
                                <label>Dirección</label>
                                <input type="text" name="direccion" class="form-control" value="${docenteEditar.direccion}"/>
                            </div>
                            <div class="mb-3">
                                <label>Fecha Ingreso</label>
                                <input type="date" name="fecha_ingreso" class="form-control" value="${docenteEditar.fechaIngreso}"/>
                            </div>
                            <div class="mb-3">
                                <label>Estado</label>
                                <select name="estado" class="form-select">
                                    <option value="activo" ${docenteEditar.estado == 'activo' ? 'selected' : ''}>Activo</option>
                                    <option value="inactivo" ${docenteEditar.estado == 'inactivo' ? 'selected' : ''}>Inactivo</option>
                                </select>
                            </div>
                        </div>

                                                <div class="modal-footer">
                            <button type="submit" class="btn btn-success">
                                <i class="fas fa-save"></i> Guardar cambios
                            </button>
                            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">
                                <i class="fas fa-times"></i> Cancelar
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </c:if>

    <!-- Scripts Bootstrap -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>

</body>
</html>