<%--  
    Document   : perfilEstudiante.jsp
    Created on : 18/11/2025, 12:00:38 p. m.
    Autor      : camiv
    Rol        : estudiante
    Propósito  : Visualizar información personal y académica del estudiante
    Trazabilidad: recibe atributo 'perfil' desde VerPerfilEstudianteServlet, validado con auditoría institucional
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" session="true" %>
<%@ page import="java.util.Map" %>

<%
    String nombre = (String) session.getAttribute("nombreActivo");
    String rol = (String) session.getAttribute("rolActivo");

    if (nombre == null || rol == null || !"estudiante".equalsIgnoreCase(rol)) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }

    Map<String, String> perfil = (Map<String, String>) request.getAttribute("perfil");
    String mensaje = (String) request.getAttribute("mensaje");
    String tipoMensaje = (String) session.getAttribute("tipoMensaje"); // success, danger, warning
%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Perfil del Estudiante - SymphonySIAS</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;600&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/estilos.css">
</head>
<body class="bg-light">

    <jsp:include page="../fragmentos/sidebar.jsp" />
    <jsp:include page="../fragmentos/header.jsp" />

    <div class="container dashboard-box mt-4">
        <div class="d-flex justify-content-between align-items-center mb-4">
            <div>
                <div class="dashboard-title"><i class="fas fa-user-graduate"></i> Perfil del Estudiante</div>
                <div><strong><%= nombre %></strong> (estudiante)</div>
            </div>
            <img src="<%= request.getContextPath() %>/assets/img/logo.png" alt="Logo SymphonySIAS" style="max-height:70px;">
        </div>

        <div class="dashboard-body">

            <% if (perfil != null && !perfil.isEmpty()) { %>
                <div class="card shadow-sm">
                    <div class="card-header bg-primary text-white">
                        <i class="fas fa-id-card"></i> Editar Información Académica y de Usuario
                    </div>
                    <div class="card-body">
                        <form action="<%= request.getContextPath() %>/ActualizarPerfilEstudianteServlet" 
                              method="post" class="needs-validation" novalidate>

                            <div class="mb-3">
                                <label class="form-label">Nombre:</label>
                                <input type="text" name="nombre" class="form-control"
                                       value="<%= perfil.get("nombre") != null ? perfil.get("nombre") : "" %>" required>
                                <div class="invalid-feedback">El nombre es obligatorio.</div>
                            </div>

                            <div class="mb-3">
                                <label class="form-label">Apellido:</label>
                                <input type="text" name="apellido" class="form-control"
                                       value="<%= perfil.get("apellido") != null ? perfil.get("apellido") : "" %>" required>
                                <div class="invalid-feedback">El apellido es obligatorio.</div>
                            </div>

                            <div class="mb-3">
                                <label class="form-label">Correo estudiante:</label>
                                <input type="email" name="correo_estudiante" class="form-control"
                                       value="<%= perfil.get("correo_estudiante") != null ? perfil.get("correo_estudiante") : "" %>" required>
                                <div class="invalid-feedback">El correo institucional es obligatorio y debe ser válido.</div>
                            </div>

                            <div class="mb-3">
                                <label class="form-label">Correo usuario:</label>
                                <input type="email" name="correo" class="form-control"
                                       value="<%= perfil.get("correo_usuario") != null ? perfil.get("correo_usuario") : "" %>" required>
                                <div class="invalid-feedback">El correo de usuario es obligatorio y debe ser válido.</div>
                            </div>

                            <div class="mb-3">
                                <label class="form-label">Instrumento:</label>
                                <input type="text" name="instrumento" class="form-control"
                                       value="<%= perfil.get("instrumento") != null ? perfil.get("instrumento") : "" %>">
                            </div>

                            <div class="mb-3">
                                <label class="form-label">Dirección:</label>
                                <input type="text" name="direccion" class="form-control"
                                       value="<%= perfil.get("direccion") != null ? perfil.get("direccion") : "" %>">
                            </div>

                            <div class="mb-3">
                                <label class="form-label">Teléfono:</label>
                                <input type="text" name="telefono" pattern="[0-9]{7,10}" class="form-control"
                                       value="<%= perfil.get("telefono") != null ? perfil.get("telefono") : "" %>">
                                <div class="invalid-feedback">El teléfono debe tener entre 7 y 10 dígitos.</div>
                            </div>

                            <div class="mb-3">
                                <label class="form-label">Etapa pedagógica:</label>
                                <input type="text" name="etapa_pedagogica" class="form-control"
                                       value="<%= perfil.get("etapa_pedagogica") != null ? perfil.get("etapa_pedagogica") : "" %>">
                            </div>

                            <hr>
                            <button type="submit" class="btn btn-success">
                                <i class="fas fa-save"></i> Guardar cambios
                            </button>
                        </form>
                    </div>
                </div>
            <% } else { %>
                <div class="alert alert-warning text-center mt-4">
                    <i class="fas fa-exclamation-triangle"></i> No se encontró información de tu perfil.
                </div>
            <% } %>

            <div class="mt-4 text-start">
                <a href="<%= request.getContextPath() %>/PanelEstudianteServlet" class="btn btn-outline-primary">
                    <i class="fas fa-arrow-left"></i> Volver al panel estudiante
                </a>
            </div>
        </div>
    </div>

    <jsp:include page="../fragmentos/footer.jsp" />

    <!-- Modal para mensajes -->
    <div class="modal fade" id="mensajeModal" tabindex="-1" aria-hidden="true">
      <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content">
          <div class="modal-header 
               <%= "success".equals(tipoMensaje) ? "bg-success text-white" : 
                   "danger".equals(tipoMensaje) ? "bg-danger text-white" : 
                   "warning".equals(tipoMensaje) ? "bg-warning" : "bg-info" %>">
            <h5 class="modal-title">Mensaje del sistema</h5>
            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Cerrar"></button>
          </div>
          <div class="modal-body">
            <%= mensaje != null ? mensaje : "" %>
          </div>
          <div class="modal-footer">
            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cerrar</button>
          </div>
        </div>
      </div>
    </div>

    <script>
        // Bootstrap validación visual
        (function () {
            'use strict';
            var forms = document.querySelectorAll('.needs-validation');
            Array.prototype.slice.call(forms).forEach(function (form) {
                form.addEventListener('submit', function (event) {
                    if (!form.checkValidity()) {
                        event.preventDefault();
                        event.stopPropagation();
                    }
                    form.classList.add('was-validated');
                }, false);
            });
        })();

        // Mostrar modal si hay mensaje
        <% if (mensaje != null) { %>
            var modal = new bootstrap.Modal(document.getElementById('mensajeModal'));
            modal.show();
        <% } %>
    </script>

</body>
</html>