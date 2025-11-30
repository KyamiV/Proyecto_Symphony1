<%--  
    Document   : gestionarClases.jsp
    Rol        : administrador
    Función    : Consultar, editar y eliminar clases institucionales
    Autor      : camiv
    Trazabilidad: incluye filtros por instrumento y etapa, tabla de clases, botones de acción y conexión a servlet
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" session="true" %>
<%@ page import="java.util.*, com.mysymphony.proyecto_symphony1.modelo.*" %>

<%
    HttpSession sesion = request.getSession(false);
    String nombre = (sesion != null) ? (String) sesion.getAttribute("nombreActivo") : null;
    String rol = (sesion != null) ? (String) sesion.getAttribute("rolActivo") : null;

    if (nombre == null || rol == null || !"administrador".equalsIgnoreCase(rol)) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }

    List<Clase> clases = (List<Clase>) request.getAttribute("clases");
    String instrumentoFiltro = request.getParameter("instrumento");
    String etapaFiltro = request.getParameter("etapa");
    String mensaje = (String) request.getAttribute("mensaje");
    String tipoMensaje = (String) request.getAttribute("tipoMensaje"); // opcional
%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Gestión de clases | SymphonySIAS</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/estilos.css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
</head>
<body class="bg-light">

    <!-- 📂 Menú lateral institucional -->
    <jsp:include page="../fragmentos/sidebar.jsp" />

    <!-- 🧭 Encabezado institucional -->
    <jsp:include page="../fragmentos/header.jsp" />

    <div class="container mt-5">
        <h3><i class="fas fa-school"></i> Gestión institucional de clases</h3>

        <% if (mensaje != null) { %>
            <div class="alert <%= (tipoMensaje != null && tipoMensaje.equals("error")) ? "alert-danger" : "alert-success" %> mt-3 text-center">
                <%= mensaje %>
            </div>
            <% session.removeAttribute("mensaje"); %>
        <% } %>

        <!-- 🔎 Filtros -->
        <form method="get" action="<%= request.getContextPath() %>/GestionarClasesServlet" class="row g-3 mt-4">
            <div class="col-md-4">
                <label>Instrumento:</label>
                <select name="instrumento" class="form-select">
                    <option value="">Todos</option>
                    <% String[] instrumentos = {"Piano", "Violín", "Guitarra", "Flauta", "Batería", "Canto"};
                       for (String inst : instrumentos) { %>
                        <option value="<%= inst %>" <%= inst.equals(instrumentoFiltro) ? "selected" : "" %>><%= inst %></option>
                    <% } %>
                </select>
            </div>
            <div class="col-md-4">
                <label>Etapa:</label>
                <select name="etapa" class="form-select">
                    <option value="">Todas</option>
                    <% String[] etapas = {"Exploración sonora", "Iniciación técnica", "Repertorio básico", "Repertorio intermedio", "Repertorio avanzado"};
                       for (String etapa : etapas) { %>
                        <option value="<%= etapa %>" <%= etapa.equals(etapaFiltro) ? "selected" : "" %>><%= etapa %></option>
                    <% } %>
                </select>
            </div>
            <div class="col-md-4 text-end">
                <button type="submit" class="btn btn-primary mt-4">
                    <i class="fas fa-filter"></i> Aplicar filtros
                </button>
                <a href="<%= request.getContextPath() %>/GestionarClasesServlet" class="btn btn-outline-secondary mt-4">
                    <i class="fas fa-undo"></i> Limpiar filtros
                </a>
            </div>
        </form>

        <!-- 📋 Tabla de clases -->
        <div class="table-responsive mt-5">
            <table class="table table-bordered table-hover align-middle">
                <thead class="table-dark text-center">
                    <tr>
                        <th>Nombre</th>
                        <th>Instrumento</th>
                        <th>Etapa</th>
                        <th>Docente</th>
                        <th>Horario</th>
                        <th>Inscritos</th>
                        <th>Estado</th>
                        <th>Acciones</th>
                    </tr>
                </thead>
                <tbody>
                    <% if (clases == null || clases.isEmpty()) { %>
                        <tr>
                            <td colspan="8" class="text-center text-muted">No hay clases registradas para los filtros seleccionados.</td>
                        </tr>
                    <% } else {
                        for (Clase clase : clases) { %>
                            <tr>
                                <td><%= clase.getNombre() %></td>
                                <td><%= clase.getInstrumento() %></td>
                                <td><%= clase.getEtapa() %></td>
                                <td><%= clase.getDocenteNombre() %></td>
                                <td><%= clase.getHorario() %></td>
                                <td class="text-center"><%= clase.getInscritos() %></td>
                                <td class="text-center"><%= clase.getEstado() %></td>
                                <td class="text-center">
                                    <a href="<%= request.getContextPath() %>/VerClaseServlet?id=<%= clase.getId() %>"
                                       class="btn btn-outline-primary btn-sm" title="Ver detalles de la clase">
                                       <i class="fas fa-search"></i> Ver detalles
                                    </a>
                                    <a href="<%= request.getContextPath() %>/VerInscritosClaseServlet?id=<%= clase.getId() %>"
                                       class="btn btn-info btn-sm" title="Ver inscritos">
                                       <i class="fas fa-users"></i> Ver inscritos
                                    </a>
                                    <% if ("lista".equalsIgnoreCase(clase.getEstado())) { %>
                                        <form method="post" action="<%= request.getContextPath() %>/AsignarClaseADocenteServlet" style="display:inline;">
                                            <input type="hidden" name="claseId" value="<%= clase.getId() %>">
                                            <button type="submit" class="btn btn-success btn-sm" title="Asignar clase a docente">
                                                <i class="fas fa-user-check"></i> Asignar a docente
                                            </button>
                                        </form>
                                    <% } %>
                                    <a href="<%= request.getContextPath() %>/EditarClaseServlet?id=<%= clase.getId() %>"
                                       class="btn btn-warning btn-sm" title="Editar clase">
                                       <i class="fas fa-edit"></i> Editar
                                    </a>
                                    <form method="post" action="<%= request.getContextPath() %>/EliminarClaseServlet" style="display:inline;">
                                        <input type="hidden" name="id" value="<%= clase.getId() %>">
                                        <button type="submit" class="btn btn-danger btn-sm"
                                                onclick="return confirm('¿Eliminar esta clase?');"
                                                title="Eliminar clase">
                                            <i class="fas fa-trash"></i> Eliminar
                                        </button>
                                    </form>
                                </td>
                            </tr>
                        <% }
                    } %>
                </tbody>
            </table>
        </div>

        <!-- 🔙 Volver al panel -->
        <div class="mt-4 text-end">
            <a href="<%= request.getContextPath() %>/PanelAdministradorServlet" class="btn btn-secondary">
                <i class="fas fa-arrow-left"></i> Volver al panel
            </a>
        </div>
    </div>

    <!-- 📌 Pie de página -->
    <jsp:include page="../fragmentos/footer.jsp" />

    <!-- Scripts -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>