<%-- 
    Document   : perfilDocente
    Created on : 27/11/2025, 6:00:04 p. m.
    Author     : camiv
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" session="true" %>
<%
    String nombre = (String) session.getAttribute("nombreActivo");
    String rol = (String) session.getAttribute("rolActivo");

    if (nombre == null || rol == null || !"docente".equalsIgnoreCase(rol)) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }
    com.mysymphony.proyecto_symphony1.modelo.Docente docente =
        (com.mysymphony.proyecto_symphony1.modelo.Docente) request.getAttribute("docentePerfil");
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Perfil Docente | SymphonySIAS</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
    <style>
        body { background-color: #f8f9fa; font-family: 'Poppins', sans-serif; font-size: 1rem; }
        .config-box { background: #fff; padding: 25px; border-radius: 12px; box-shadow: 0 4px 12px rgba(0,0,0,0.08); }
        .table-modern { max-width: 750px; margin: 0 auto; } /* 🔹 tabla más estrecha y centrada */
        .table-modern thead { background: #198754; color: #fff; font-size: 1rem; }
        .table-modern td, .table-modern th { padding: 10px 14px; font-size: 1rem; }
        .btn-custom { border-radius: 6px; padding: 8px 18px; font-size: 1rem; }
        .icon-col { width: 40px; text-align: center; color: #198754; }
    </style>
</head>
<body class="p-3">

<jsp:include page="../fragmentos/header.jsp" />

<div class="container config-box">
    <div class="text-center mb-3">
        <h4 class="text-success"><i class="fas fa-user-cog"></i> Perfil del Docente</h4>
        <p class="text-muted">Consulta y edita tu información personal e institucional</p>
    </div>

    <% if (docente != null) { %>
        <form action="<%= request.getContextPath() %>/PerfilDocenteServlet" method="post">
            <input type="hidden" name="id_docente" value="<%= docente.getId() %>">

            <table class="table table-modern table-bordered align-middle">
                <thead>
                    <tr>
                        <th class="icon-col"><i class="fas fa-tag"></i></th>
                        <th>Campo</th>
                        <th>Valor</th>
                        <th>Acción</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td class="icon-col"><i class="fas fa-user"></i></td>
                        <td>Nombre</td>
                        <td><input type="text" name="nombre" value="<%= docente.getNombre() %>" class="form-control"></td>
                        <td class="text-center"><i class="fas fa-edit text-muted"></i></td>
                    </tr>
                    <tr>
                        <td class="icon-col"><i class="fas fa-user"></i></td>
                        <td>Apellido</td>
                        <td><input type="text" name="apellido" value="<%= docente.getApellido() %>" class="form-control"></td>
                        <td class="text-center"><i class="fas fa-edit text-muted"></i></td>
                    </tr>
                    <tr>
                        <td class="icon-col"><i class="fas fa-envelope"></i></td>
                        <td>Correo</td>
                        <td><input type="email" name="correo" value="<%= docente.getCorreo() %>" class="form-control"></td>
                        <td class="text-center"><i class="fas fa-edit text-muted"></i></td>
                    </tr>
                    <tr>
                        <td class="icon-col"><i class="fas fa-phone"></i></td>
                        <td>Teléfono</td>
                        <td><input type="text" name="telefono" value="<%= docente.getTelefono() %>" class="form-control"></td>
                        <td class="text-center"><i class="fas fa-edit text-muted"></i></td>
                    </tr>
                    <tr>
                        <td class="icon-col"><i class="fas fa-map-marker-alt"></i></td>
                        <td>Dirección</td>
                        <td><input type="text" name="direccion" value="<%= docente.getDireccion() %>" class="form-control"></td>
                        <td class="text-center"><i class="fas fa-edit text-muted"></i></td>
                    </tr>
                    <tr>
                        <td class="icon-col"><i class="fas fa-calendar-alt"></i></td>
                        <td>Fecha de ingreso</td>
                        <td><input type="text" value="<%= docente.getFechaIngreso() %>" class="form-control" readonly></td>
                        <td class="text-center"><i class="fas fa-lock text-danger"></i></td>
                    </tr>
                    <tr>
                        <td class="icon-col"><i class="fas fa-toggle-on"></i></td>
                        <td>Estado</td>
                        <td><input type="text" value="<%= docente.getEstado() %>" class="form-control" readonly></td>
                        <td class="text-center"><i class="fas fa-lock text-danger"></i></td>
                    </tr>
                    <tr>
                        <td class="icon-col"><i class="fas fa-graduation-cap"></i></td>
                        <td>Nivel Técnico</td>
                        <td>
                            <select name="nivel_tecnico" class="form-select">
                                <option value="Básico" <%= "Básico".equals(docente.getNivelTecnico()) ? "selected" : "" %>>Básico</option>
                                <option value="Intermedio" <%= "Intermedio".equals(docente.getNivelTecnico()) ? "selected" : "" %>>Intermedio</option>
                                <option value="Avanzado" <%= "Avanzado".equals(docente.getNivelTecnico()) ? "selected" : "" %>>Avanzado</option>
                            </select>
                        </td>
                        <td class="text-center"><i class="fas fa-edit text-muted"></i></td>
                    </tr>
                </tbody>
            </table>

            <div class="mt-3 d-flex justify-content-between">
                <button type="submit" class="btn btn-success btn-custom">
                    <i class="fas fa-save"></i> Guardar cambios
                </button>
                <a href="<%= request.getContextPath() %>/PanelDocenteServlet" class="btn btn-outline-secondary btn-custom">
                    <i class="fas fa-arrow-left"></i> Volver
                </a>
            </div>
        </form>
    <% } else { %>
        <div class="alert alert-warning text-center">
            <i class="fas fa-exclamation-triangle"></i> No se encontró información del docente.
        </div>
    <% } %>
</div>

<jsp:include page="../fragmentos/footer.jsp" />
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>