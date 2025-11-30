<%-- 
    Document   : verClasesEstudiante.jsp
    Autor      : camiv
    Rol        : estudiante
    Propósito  : Visualizar las clases musicales creadas por el administrador y permitir inscripción
    Trazabilidad: recibe atributo 'clases' desde VerClasesEstudianteServlet, validado con auditoría institucional
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" session="true" %>
<%@ page import="java.util.List, java.util.Map" %>

<%
    // 🔐 Validación de sesión y rol
    String nombre = (String) session.getAttribute("nombreActivo");
    String rol = (String) session.getAttribute("rolActivo");

    if (nombre == null || rol == null || !"estudiante".equalsIgnoreCase(rol)) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }

    // 🎨 Variables visuales
    String iconoRol = "fas fa-music";

    // 📦 Datos recibidos desde el servlet
    List<Map<String, String>> clasesDisponibles = (List<Map<String, String>>) request.getAttribute("clasesDisponibles");
    List<Map<String, String>> clasesInscritas = (List<Map<String, String>>) request.getAttribute("clasesInscritas");
    String mensaje = (String) request.getAttribute("mensaje");
    String tipoMensaje = (String) request.getAttribute("tipoMensaje"); // success, warning, danger
%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Clases - SymphonySIAS</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;600&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/estilos.css">

    <style>
        .alert { font-weight: 600; border-radius: 10px; }
        .dashboard-title { font-size: 1.6rem; font-weight: 600; color: #198754; }
    </style>
</head>
<body class="bg-light">

    <!-- 📂 Menú lateral institucional -->
    <jsp:include page="../fragmentos/sidebar.jsp" />

    <!-- 🧭 Encabezado institucional -->
    <jsp:include page="../fragmentos/header.jsp" />

    <!-- Contenedor principal -->
    <div class="container dashboard-box">
        <div class="d-flex justify-content-between align-items-center mb-4">
            <div>
                <div class="dashboard-title"><i class="<%= iconoRol %>"></i> Clases</div>
                <div><strong><%= nombre %></strong> (estudiante)</div>
            </div>
        </div>

        <!-- 📄 Cuerpo principal -->
        <div class="dashboard-body">

            <% if (mensaje != null) { %>
                <!-- Bloque institucional de mensajes -->
                <div class="alert alert-<%= (tipoMensaje != null ? tipoMensaje : "info") %> text-center mt-3">
                    <i class="fas fa-info-circle"></i> <%= mensaje %>
                </div>
            <% } %>

            <!-- 📊 Tabla de clases disponibles -->
            <h4 class="text-center mt-3">Clases disponibles para inscripción</h4>
            <% if (clasesDisponibles == null || clasesDisponibles.isEmpty()) { %>
                <div class="alert alert-warning text-center mt-4">
                    <i class="fas fa-calendar-times"></i> No hay clases disponibles para inscripción.
                </div>
            <% } else { %>
                <div class="table-responsive mt-4">
                    <table class="table table-bordered table-hover align-middle text-center">
                        <thead class="table-success">
                            <tr>
                                <th>Clase</th>
                                <th>Instrumento</th>
                                <th>Docente</th>
                                <th>Etapa</th>
                                <th>Día inicio</th>
                                <th>Día fin</th>
                                <th>Fecha límite</th>
                                <th>Acción</th>
                            </tr>
                        </thead>
                        <tbody>
                            <% for (Map<String, String> clase : clasesDisponibles) { %>
                                <tr>
                                    <td><%= clase.get("nombre_clase") %></td>
                                    <td><%= clase.get("instrumento") %></td>
                                    <td><%= (clase.get("docente") != null && !clase.get("docente").isEmpty()) ? clase.get("docente") : "<em class='text-muted'>Sin asignar</em>" %></td>
                                    <td><%= clase.get("etapa") %></td>
                                    <td><%= clase.get("fecha_inicio") %></td>
                                    <td><%= clase.get("fecha_fin") %></td>
                                    <td><%= clase.get("fecha_limite") %></td>
                                    <td>
                                        <form action="<%= request.getContextPath() %>/InscripcionClaseServlet" method="post"
                                              onsubmit="return confirm('¿Deseas inscribirte en esta clase?');" class="m-0">
                                            <input type="hidden" name="claseId" value="<%= clase.get("id_clase") %>">
                                            <button type="submit" class="btn btn-sm btn-outline-primary">
                                                <i class="fas fa-plus-circle"></i> Inscribirme
                                            </button>
                                        </form>
                                    </td>
                                </tr>
                            <% } %>
                        </tbody>
                    </table>
                </div>
            <% } %>

            <!-- 📊 Tabla de clases inscritas -->
            <h4 class="text-center mt-5">Clases en las que ya estás inscrito</h4>
            <% if (clasesInscritas == null || clasesInscritas.isEmpty()) { %>
                <div class="alert alert-secondary text-center mt-4">
                    <i class="fas fa-check-circle"></i> Aún no te has inscrito en ninguna clase.
                </div>
            <% } else { %>
                <div class="table-responsive mt-4">
                    <table class="table table-bordered table-hover align-middle text-center">
                        <thead class="table-info">
                            <tr>
                                <th>Clase</th>
                                <th>Instrumento</th>
                                <th>Docente</th>
                                <th>Etapa</th>
                                <th>Día inicio</th>
                                <th>Día fin</th>
                                <th>Fecha límite</th>
                                <th>Acción</th>
                            </tr>
                        </thead>
                        <tbody>
                            <% for (Map<String, String> clase : clasesInscritas) { 
                                   int cupo = Integer.parseInt(clase.get("cupo"));
                                   int inscritos = Integer.parseInt(clase.get("inscritos"));
                            %>
                                <tr>
                                    <td><%= clase.get("nombre_clase") %></td>
                                    <td><%= clase.get("instrumento") %></td>
                                    <td><%= (clase.get("docente") != null && !clase.get("docente").isEmpty()) ? clase.get("docente") : "<em class='text-muted'>Sin asignar</em>" %></td>
                                    <td><%= clase.get("etapa") %></td>
                                    <td><%= clase.get("fecha_inicio") %></td>
                                    <td><%= clase.get("fecha_fin") %></td>
                                    <td><%= clase.get("fecha_limite") %></td>
                                    <td>
                                        <% if (inscritos >= cupo) { %>
                                            <!-- Clase llena: no se puede cancelar -->
                                            <a href="<%= request.getContextPath() %>/ActividadesEstudianteServlet?claseId=<%= clase.get("id_clase") %>"
                                               class="btn btn-sm btn-warning">
                                                <i class="fas fa-tasks"></i> Clase asignada - Ver actividades
                                            </a>
                                        <% } else { %>
                                            <!-- Clase no llena: se puede cancelar inscripción -->
                                            <form action="<%= request.getContextPath() %>/CancelarInscripcionServlet" method="post"
                                                  onsubmit="return confirm('¿Deseas cancelar tu inscripción en esta clase?');" class="m-0">
                                                <input type="hidden" name="claseId" value="<%= clase.get("id_clase") %>">
                                                <button type="submit" class="btn btn-sm btn-outline-danger">
                                                    <i class="fas fa-trash-alt"></i> Cancelar inscripción
                                                </button>
                                            </form>
                                        <% } %>
                                    </td>
                                </tr>
                            <% } %>
                        </tbody>
                    </table>
                </div>
            <% } %>

                       <!-- 🔙 Botón de regreso -->
            <div class="mt-4 text-start">
                <a href="<%= request.getContextPath() %>/estudiante/estudiante.jsp" class="btn btn-success">
                    <i class="fas fa-arrow-left"></i> Volver al panel
                </a>
            </div>
        </div>
    </div>

    <!-- 📌 Pie de página institucional -->
    <jsp:include page="../fragmentos/footer.jsp" />

</body>
</html>