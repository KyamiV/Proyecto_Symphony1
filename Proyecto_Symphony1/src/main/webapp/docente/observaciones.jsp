<%-- 
    Document   : observaciones
    Created on : 14/11/2025, 9:54:04 p. m.
    Author     : camiv
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List, java.util.Map" %>

<%
    String nombre = (String) session.getAttribute("nombreActivo");
    String rol = (String) session.getAttribute("rolActivo");

    if (nombre == null || rol == null || !"docente".equalsIgnoreCase(rol)) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }

    List<Map<String,String>> estudiantes = (List<Map<String,String>>) request.getAttribute("estudiantes");
    List<Map<String,String>> observaciones = (List<Map<String,String>>) request.getAttribute("observaciones");
    String mensaje = (String) session.getAttribute("mensaje");
    session.removeAttribute("mensaje");
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Observaciones pedagógicas - SymphonySIAS</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/estilos.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
    <style>
        body { font-family: 'Poppins', sans-serif; background-color: #f4f6f9; padding: 30px; }
        .dashboard-box {
            background: #ffffff;
            padding: 25px;
            border-radius: 10px;
            box-shadow: 0 0 10px rgba(0,0,0,0.08);
            max-width: 100%;
            margin: 0 auto;
        }
        .tabla-observaciones th, .tabla-observaciones td {
            vertical-align: middle;
            text-align: center;
        }
    </style>
</head>
<body>

<jsp:include page="../fragmentos/header.jsp" />

<div class="dashboard-box">
    <h4 class="text-center mb-4"><i class="fas fa-comments"></i> Observaciones pedagógicas</h4>

    <% if (mensaje != null) { %>
        <div class="alert alert-info text-center" style="max-width: 700px; margin: 0 auto;">
            <%= mensaje %>
        </div>
    <% } %>

    <!-- Validación visual de carga de estudiantes -->
    <% if (estudiantes == null || estudiantes.isEmpty()) { %>
        <div class="alert alert-warning text-center">⚠️ No hay estudiantes asignados a tus clases.</div>
    <% } else { %>
        <div class="alert alert-success text-center">✅ Se cargaron <%= estudiantes.size() %> estudiantes asignados.</div>
    <% } %>

    <!-- Formulario para enviar observación -->
    <div class="mb-4">
        <h5><i class="fas fa-plus-circle"></i> Registrar observación</h5>
        <form action="<%= request.getContextPath() %>/GuardarObservacionesServlet" method="post" class="row g-3">
            <div class="col-md-6">
                <label class="form-label">Estudiante</label>
                <select name="idEstudiante" class="form-select" required>
                    <option value="">Seleccione estudiante...</option>
                    <% if (estudiantes != null) {
                           for (Map<String,String> est : estudiantes) { %>
                        <option value="<%= est.get("id_estudiante") %>">
                            <%= est.get("nombre") %> - <%= est.get("email") %>
                        </option>
                    <% } } %>
                </select>
            </div>
            <div class="col-md-6">
                <label class="form-label">Instrumento</label>
                <input type="text" name="instrumento" class="form-control" required>
            </div>
            <!-- Etapa y Nota juntos -->
            <div class="col-md-6">
                <label class="form-label">Etapa pedagógica</label>
                <input type="text" name="etapa_pedagogica" class="form-control" required>
            </div>
            <div class="col-md-6">
                <label class="form-label">Nota</label>
                <input type="number" name="nota" class="form-control" min="0" max="100" step="0.1" required>
            </div>
            <div class="col-md-12">
                <label class="form-label">Comentario</label>
                <textarea name="comentario" class="form-control" rows="3" required></textarea>
            </div>
            <!-- Checkbox para enviar al estudiante -->
            <div class="col-md-12">
                <div class="form-check">
                    <input class="form-check-input" type="checkbox" name="enviar" id="enviar">
                    <label class="form-check-label" for="enviar">
                        Enviar observación al estudiante
                    </label>
                </div>
            </div>
            <div class="col-md-12 text-end">
                <button type="submit" class="btn btn-success">
                    <i class="fas fa-save"></i> Guardar y enviar observación
                </button>
            </div>
        </form>
    </div>

    <!-- Tabla de observaciones ya registradas -->
    <div class="table-responsive">
        <table class="table table-bordered table-striped tabla-observaciones">
            <thead class="table-dark">
                <tr>
                    <th>Estudiante</th>
                    <th>Instrumento</th>
                    <th>Etapa / Nota</th>
                    <th>Comentario</th>
                    <th>Docente</th>
                    <th>Fecha</th>
                    <th>Estado</th>
                </tr>
            </thead>
            <tbody>
                <% if (observaciones != null && !observaciones.isEmpty()) {
                       for (Map<String,String> obs : observaciones) { %>
                <tr>
                    <td><%= obs.get("estudiante") %></td>
                    <td><%= obs.get("instrumento") %></td>
                    <td><%= obs.get("etapa_pedagogica") %> / <%= obs.get("nota") %></td>
                    <td><%= obs.get("comentario") %></td>
                    <td><%= obs.get("docente") %></td>
                    <td><%= obs.get("fecha_registro") %></td>
                    <td><%= "Sí".equalsIgnoreCase(obs.get("enviada")) ? "✅ Enviada" : "⌛ Pendiente" %></td>
                </tr>
                <% } } else { %>
                <tr>
                    <td colspan="7" class="text-center">No hay observaciones registradas.</td>
                </tr>
                <% } %>
            </tbody>
        </table>
    </div>
    
    <div class="mt-4 text-start">
        <a href="<%= request.getContextPath() %>/PanelDocenteServlet" class="btn btn-outline-primary">
            <i class="fas fa-arrow-left"></i> Volver al panel docente
        </a>
    </div>
</div>

<jsp:include page="../fragmentos/footer.jsp" />

</body>
</html>