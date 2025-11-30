<%-- 
    Document   : verDocentes
    Created on : 26/11/2025, 5:18:06 p. m.
    Author     : camiv
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Clases asignadas a docente</title>
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
        <!-- Título con ID de docente -->
        <div class="titulo-panel mb-4">
            <h2>
                👨‍🏫 Clases asignadas al docente #${idDocente}
            </h2>
        </div>

        <!-- ✅ Contador de clases -->
        <c:if test="${not empty totalClases}">
            <div class="alert alert-info">
                <i class="fas fa-chalkboard"></i> Total clases asignadas: <strong>${totalClases}</strong>
            </div>
        </c:if>

        <!-- ✅ Mensajes institucionales -->
        <c:if test="${not empty mensaje}">
            <div class="alert alert-${tipoMensaje}">
                ${mensaje}
            </div>
        </c:if>

        <!-- 🔹 Bloque de clases activas -->
        <h4 class="mb-3 text-success"><i class="fas fa-play-circle"></i> Clases activas</h4>
        <c:if test="${not empty clasesAsignadas}">
            <div class="table-responsive mb-4">
                <table class="table table-bordered table-hover align-middle">
                    <thead class="table-success text-center">
                        <tr>
                            <th>ID Clase</th>
                            <th>Nombre</th>
                            <th>Instrumento</th>
                            <th>Etapa</th>
                            <th>Grupo</th>
                            <th>Cupo</th>
                            <th>Inscritos</th>
                            <th>Día</th>
                            <th>Hora inicio</th>
                            <th>Hora fin</th>
                            <th>Estado</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="cl" items="${clasesAsignadas}">
                            <c:if test="${cl.estado eq 'activa'}">
                                <tr>
                                    <td class="text-center">${cl.idClase}</td>
                                    <td>${cl.nombreClase}</td>
                                    <td>${cl.instrumento}</td>
                                    <td>${cl.etapa}</td>
                                    <td>${cl.grupo}</td>
                                    <td class="text-center">${cl.cupo}</td>
                                    <td class="text-center">${cl.inscritos}</td>
                                    <td class="text-center">${cl.diaSemana}</td>
                                    <td class="text-center">${cl.horaInicio}</td>
                                    <td class="text-center">${cl.horaFin}</td>
                                    <td class="text-center">${cl.estado}</td>
                                </tr>
                            </c:if>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </c:if>

        <!-- 🔹 Bloque de clases no activas -->
        <h4 class="mb-3 text-secondary"><i class="fas fa-stop-circle"></i> Clases finalizadas / validadas / certificadas</h4>
        <c:if test="${not empty clasesAsignadas}">
            <div class="table-responsive">
                <table class="table table-bordered table-hover align-middle">
                    <thead class="table-secondary text-center">
                        <tr>
                            <th>ID Clase</th>
                            <th>Nombre</th>
                            <th>Instrumento</th>
                            <th>Etapa</th>
                            <th>Grupo</th>
                            <th>Cupo</th>
                            <th>Inscritos</th>
                            <th>Validados</th>
                            <th>Certificados</th>
                            <th>Estado</th>
                            <th>Acciones</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="cl" items="${clasesAsignadas}">
                            <c:if test="${cl.estado ne 'activa'}">
                                <tr>
                                    <td class="text-center">${cl.idClase}</td>
                                    <td>${cl.nombreClase}</td>
                                    <td>${cl.instrumento}</td>
                                    <td>${cl.etapa}</td>
                                    <td>${cl.grupo}</td>
                                    <td class="text-center">${cl.cupo}</td>
                                    <td class="text-center">${cl.inscritos}</td>
                                    <td class="text-center">${cl.validados}</td>
                                    <td class="text-center">${cl.certificados}</td>
                                    <td class="text-center">${cl.estado}</td>
                                    <td class="text-center">
                                        <!-- Botón institucional en Acciones -->
                                        <button type="button" class="btn btn-sm btn-outline-info"
                                                data-bs-toggle="modal" data-bs-target="#modalEstudiantes${cl.idClase}">
                                            <i class="fas fa-users"></i> Ver estudiantes
                                        </button>
                                    </td>
                                </tr>

                                <!-- Modal con tabla de estudiantes -->
                                <div class="modal fade" id="modalEstudiantes${cl.idClase}" tabindex="-1" aria-hidden="true">
                                    <div class="modal-dialog modal-lg modal-dialog-centered">
                                        <div class="modal-content">
                                            <div class="modal-header bg-success text-white">
                                                <h5 class="modal-title">Estudiantes de la clase #${cl.idClase}</h5>
                                                <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                                            </div>
                                            <div class="modal-body">
                                                <!-- Tabla de estudiantes validados -->
                                                <h6 class="text-success"><i class="fas fa-check-circle"></i> Validados</h6>
                                                <table class="table table-sm table-striped">
                                                    <thead><tr><th>Nombre</th></tr></thead>
                                                    <tbody>
                                                        <c:forEach var="est" items="${cl.estudiantesValidados}">
                                                            <tr><td>${est}</td></tr>
                                                        </c:forEach>
                                                        <c:if test="${empty cl.estudiantesValidados}">
                                                            <tr><td class="text-muted">No hay estudiantes validados</td></tr>
                                                        </c:if>
                                                    </tbody>
                                                </table>

                                                <!-- Tabla de estudiantes certificados -->
                                                <h6 class="text-primary"><i class="fas fa-certificate"></i> Certificados</h6>
                                                <table class="table table-sm table-striped">
                                                    <thead><tr><th>Nombre</th></tr></thead>
                                                    <tbody>
                                                        <c:forEach var="est" items="${cl.estudiantesCertificados}">
                                                            <tr><td>${est}</td></tr>
                                                        </c:forEach>
                                                        <c:if test="${empty cl.estudiantesCertificados}">
                                                            <tr><td class="text-muted">No hay estudiantes certificados</td></tr>
                                                        </c:if>
                                                    </tbody>
                                                </table>
                                            </div>
                                            <div class="modal-footer">
                                                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cerrar</button>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </c:if>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </c:if>

               <!-- ✅ Botón volver -->
        <div class="mt-3">
            <a href="GestionarClasesPrincipalServlet" class="btn btn-outline-secondary">
                <i class="fas fa-arrow-left"></i> Volver a gestión de clases
            </a>
        </div>
    </main>

    <!-- ✅ Footer institucional -->
    <jsp:include page="../fragmentos/footer.jsp" />

    <!-- Bootstrap JS (necesario para modales) -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>