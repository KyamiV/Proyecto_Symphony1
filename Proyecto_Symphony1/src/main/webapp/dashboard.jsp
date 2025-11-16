<%-- 
    Document   : dashboard
    Created on : 13/11/2025, 6:59:40 p. m.
    Author     : camiv
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" session="true" %>
<%@ page import="com.mysymphony.proyecto_symphony1.dao.NotaDAO" %>
<%@ page import="java.util.*" %>
<%
    String nombre = (String) session.getAttribute("nombreActivo");
    String rol = (String) session.getAttribute("rolActivo");

    if (nombre == null || rol == null || !"docente".equalsIgnoreCase(rol)) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }

    String claseHeader = "dashboard-docente";
    String claseBoton = "btn-docente";
    String iconoRol = "fas fa-chalkboard-teacher";

    NotaDAO dao = new NotaDAO();
    List<Map<String, String>> notas = dao.obtenerNotasPorDocente(nombre);
    int totalNotas = notas.size();

    Set<String> estudiantesUnicos = new HashSet<>();
    Set<String> instrumentosUnicos = new HashSet<>();
    for (Map<String, String> fila : notas) {
        if (fila.get("estudiante") != null) {
            estudiantesUnicos.add(fila.get("estudiante"));
        }
        if (fila.get("instrumento") != null) {
            instrumentosUnicos.add(fila.get("instrumento"));
        }
    }
    int totalEstudiantes = estudiantesUnicos.size();
    int totalInstrumentos = instrumentosUnicos.size();
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Panel docente - SymphonySIAS</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/estilos.css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;600&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
</head>
<body>

<jsp:include page="fragmentos/header.jsp" />

<div class="dashboard-box">
    <h3 class="text-center mb-4"><i class="<%= iconoRol %>"></i> Panel docente</h3>

    <div class="row mb-4 text-center">
        <div class="col-md-4">
            <a href="<%= request.getContextPath() %>/docente/registrarNotas.jsp" class="btn-dashboard <%= claseBoton %>">
                <i class="fas fa-pen"></i> Registrar calificaciones
            </a>
        </div>
        <div class="col-md-4">
            <a href="<%= request.getContextPath() %>/docente/verNotasDocente.jsp" class="btn-dashboard <%= claseBoton %>">
                <i class="fas fa-chart-bar"></i> Consultar notas registradas
            </a>
        </div>
        <div class="col-md-4">
            <a href="<%= request.getContextPath() %>/docente/listadoEstudiantes.jsp" class="btn-dashboard <%= claseBoton %>">
                <i class="fas fa-users"></i> Ver estudiantes
            </a>
        </div>
    </div>

    <!-- Indicadores rápidos -->
    <div class="row text-center mt-4">
        <div class="col-md-4">
            <div class="p-3 border rounded bg-light">
                <h5><i class="fas fa-users"></i> Estudiantes asignados</h5>
                <p class="fs-4"><%= totalEstudiantes %></p>
            </div>
        </div>
        <div class="col-md-4">
            <div class="p-3 border rounded bg-light">
                <h5><i class="fas fa-clipboard-check"></i> Notas registradas</h5>
                <p class="fs-4"><%= totalNotas %></p>
            </div>
        </div>
        <div class="col-md-4">
            <div class="p-3 border rounded bg-light">
                <h5><i class="fas fa-music"></i> Instrumentos activos</h5>
                <p class="fs-4"><%= totalInstrumentos %></p>
            </div>
        </div>
    </div>

    <!-- Mensaje institucional -->
    <div class="mt-5 text-center">
        <blockquote class="blockquote text-secondary">
            <p>“Educar no es dar carrera para vivir, sino templar el alma para las dificultades de la vida.”</p>
            <footer class="blockquote-footer">Pitágoras</footer>
        </blockquote>
    </div>

    <!-- Botón de cierre de sesión -->
    <div class="text-end mt-4">
        <a href="<%= request.getContextPath() %>/CerrarSesionServlet" class="btn btn-outline-secondary">Cerrar sesión</a>
    </div>
</div>

<footer class="text-center mt-5 text-muted">
    &copy; 2025 SymphonySIAS - Sistema de Información Académico Musical
</footer>

</body>
</html>