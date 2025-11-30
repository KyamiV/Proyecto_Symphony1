<%-- 
    Document   : gestionarAsignaciones.jsp
    Rol        : administrador
    Función    : Gestionar asignaciones institucionales por docente, instrumento y clase
    Autor      : camiv
    Trazabilidad: Incluye filtros, tablas, asignación, desasignación, exportación y subida institucional
--%>
   
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List, java.util.Map" %>
<%@ page import="com.mysymphony.proyecto_symphony1.modelo.Usuario" %>
<%@ page import="com.mysymphony.proyecto_symphony1.modelo.Clase" %>
<%@ page import="com.mysymphony.proyecto_symphony1.modelo.Estudiante" %>

<%
    HttpSession sesion = request.getSession(false);
    String rol = (sesion != null) ? (String) sesion.getAttribute("rolActivo") : null;
    String nombre = (sesion != null) ? (String) sesion.getAttribute("nombreActivo") : null;

    if (rol == null || !"administrador".equalsIgnoreCase(rol)) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }

    String mensaje = (sesion != null) ? (String) sesion.getAttribute("mensaje") : null;
    String tipoMensaje = (sesion != null) ? (String) sesion.getAttribute("tipoMensaje") : null;
    if (sesion != null) {
        sesion.removeAttribute("mensaje");
        sesion.removeAttribute("tipoMensaje");
    }

    List<Usuario> docentes = (List<Usuario>) request.getAttribute("docentes");
    List<String> instrumentos = (List<String>) request.getAttribute("instrumentos");
    List<Clase> clases = (List<Clase>) request.getAttribute("clases");
    List<Map<String, String>> asignacionesInstitucionales = (List<Map<String, String>>) request.getAttribute("asignacionesInstitucionales");
    List<Map<String, String>> estudiantesAsignados = (List<Map<String, String>>) request.getAttribute("estudiantesAsignados");
    List<Estudiante> estudiantesDisponibles = (List<Estudiante>) request.getAttribute("estudiantesDisponibles");
%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Gestión de asignaciones | SymphonySIAS</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/estilos.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">

    <!-- 📂 Menú lateral institucional -->
    <jsp:include page="../fragmentos/sidebar.jsp" />

    <!-- 🧭 Encabezado institucional -->
    <jsp:include page="../fragmentos/header.jsp" />

    <div class="container mt-4">
        <h3 class="mb-4 text-center text-success">
            <i class="fas fa-chalkboard-teacher"></i> Gestión de asignaciones por docente
        </h3>

        <% if (mensaje != null) { %>
            <div class="alert <%= (tipoMensaje != null && tipoMensaje.equals("error")) ? "alert-danger" : "alert-success" %> text-center">
                <i class="fas fa-info-circle"></i> <%= mensaje %>
            </div>
        <% } %>

        <!-- 🔎 Filtros -->
        <form method="get" action="GestionarAsignacionesServlet" class="row g-3 mb-4">
            <!-- Docente -->
            <div class="col-md-4">
                <label class="form-label"><i class="fas fa-user"></i> Docente:</label>
                <select name="docenteId" class="form-select" required>
                    <% if (docentes != null && !docentes.isEmpty()) {
                           for (Usuario d : docentes) { %>
                        <option value="<%= d.getId() %>"><%= d.getNombre() %></option>
                    <% } } else { %>
                        <option value="">No hay docentes disponibles</option>
                    <% } %>
                </select>
            </div>
            <!-- Instrumento -->
            <div class="col-md-4">
                <label class="form-label"><i class="fas fa-music"></i> Instrumento:</label>
                <select name="instrumento" class="form-select" required>
                    <% if (instrumentos != null && !instrumentos.isEmpty()) {
                           for (String inst : instrumentos) { %>
                        <option value="<%= inst %>"><%= inst %></option>
                    <% } } else { %>
                        <option value="">No hay instrumentos disponibles</option>
                    <% } %>
                </select>
            </div>
            <!-- Clase -->
            <div class="col-md-4">
                <label class="form-label"><i class="fas fa-book"></i> Clase:</label>
                <select name="claseId" class="form-select" required>
                    <% if (clases != null && !clases.isEmpty()) {
                           for (Clase c : clases) { %>
                        <option value="<%= c.getId() %>"><%= c.getNombre() %></option>
                    <% } } else { %>
                        <option value="">No hay clases disponibles</option>
                    <% } %>
                </select>
            </div>
            <div class="col-12 text-end">
                <button type="submit" class="btn btn-primary"><i class="fas fa-filter"></i> Aplicar filtros</button>
                <a href="GestionarAsignacionesServlet" class="btn btn-outline-secondary"><i class="fas fa-undo"></i> Limpiar filtros</a>
            </div>
        </form>

        <!-- 🏫 Tabla de asignaciones institucionales -->
        <h5><i class="fas fa-building"></i> Asignaciones institucionales</h5>
        <div class="table-responsive">
            <table class="table table-bordered table-striped">
                <thead class="table-success text-center">
                    <tr>
                        <th><i class="fas fa-music"></i> Instrumento</th>
                        <th><i class="fas fa-book"></i> Clase</th>
                        <th><i class="fas fa-calendar-alt"></i> Fecha</th>
                        <th><i class="fas fa-check-circle"></i> Estado</th>
                    </tr>
                </thead>
                <tbody>
                    <% if (asignacionesInstitucionales != null && !asignacionesInstitucionales.isEmpty()) {
                           for (Map<String, String> a : asignacionesInstitucionales) { %>
                        <tr>
                            <td><%= a.get("instrumento") %></td>
                            <td><%= a.get("clase") %></td>
                            <td><%= a.get("fecha") %></td>
                            <td><%= a.get("estado") %></td>
                        </tr>
                    <% } } else { %>
                        <tr><td colspan="4" class="text-center text-muted">No hay asignaciones institucionales.</td></tr>
                    <% } %>
                </tbody>
            </table>
        </div>

        <!-- 👩‍🎓 Tabla de estudiantes asignados -->
        <h5 class="mt-4"><i class="fas fa-users"></i> Estudiantes asignados</h5>
        <div class="table-responsive">
            <table class="table table-bordered table-hover">
                <thead class="table-info text-center">
                    <tr>
                        <th>Nombre</th>
                        <th>Etapa</th>
                        <th>Clase</th>
                        <th>Fecha</th>
                        <th>Acción</th>
                    </tr>
                </thead>
                <tbody>
                    <% if (estudiantesAsignados != null && !estudiantesAsignados.isEmpty()) {
                           for (Map<String, String> e : estudiantesAsignados) { %>
                        <tr>
                            <td><%= e.get("nombre") %></td>
                            <td><%= e.get("etapa") %></td>
                            <td><%= e.get("clase") %></td>
                                                        <td><%= e.get("fecha") %></td>
                            <td>
                                <form method="post" action="DesasignarEstudianteServlet" style="display:inline;">
                                    <input type="hidden" name="estudianteId" value="<%= e.get("id") %>">
                                    <button type="submit" class="btn btn-danger btn-sm"
                                            title="Desasignar estudiante"
                                            onclick="return confirm('¿Desasignar este estudiante?');">
                                        <i class="fas fa-times"></i> Desasignar
                                    </button>
                                </form>
                            </td>
                        </tr>
                    <% } } else { %>
                        <tr><td colspan="5" class="text-center text-muted">No hay estudiantes asignados.</td></tr>
                    <% } %>
                </tbody>
            </table>
        </div>

        <!-- ➕ Asignación de estudiantes disponibles -->
        <h5 class="mt-4"><i class="fas fa-user-plus"></i> Asignar estudiantes</h5>
        <form method="post" action="AsignarEstudiantesServlet">
            <input type="hidden" name="docenteId" value="<%= request.getParameter("docenteId") %>">
            <input type="hidden" name="instrumento" value="<%= request.getParameter("instrumento") %>">
            <input type="hidden" name="claseId" value="<%= request.getParameter("claseId") %>">

            <input type="text" name="busqueda" class="form-control mb-3" placeholder="Buscar estudiante por nombre">

            <% if (estudiantesDisponibles != null && !estudiantesDisponibles.isEmpty()) {
                   for (Estudiante est : estudiantesDisponibles) { %>
                <div class="form-check">
                    <input class="form-check-input" type="checkbox" name="estudiantesSeleccionados" value="<%= est.getId() %>">
                    <label class="form-check-label">
                        <%= est.getNombre() %> - <%= est.getEtapaPedagogica() %>
                    </label>
                </div>
            <% } } else { %>
                <p class="text-muted">No hay estudiantes disponibles para asignar.</p>
            <% } %>

            <button type="submit" class="btn btn-primary mt-3">
                <i class="fas fa-plus"></i> Asignar estudiantes seleccionados
            </button>
        </form>

        <!-- 🔙 Volver al panel administrador -->
        <div class="mt-4">
            <a href="PanelAdministradorServlet" class="btn btn-dark">
                <i class="fas fa-arrow-left"></i> Volver al panel
            </a>
        </div>
    </div>

    <!-- 📌 Pie de página -->
    <jsp:include page="../fragmentos/footer.jsp" />

    <!-- ⚙️ Scripts Bootstrap -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>

</body>
</html>