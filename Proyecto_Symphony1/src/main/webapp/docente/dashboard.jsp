<%-- 
    Document   : dashboard
    Created on : 13/11/2025, 6:59:40 p. m.
    Author     : camiv
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" session="true" %>
<%@ page import="java.util.*" %>

<%
    String nombre = (String) session.getAttribute("nombreActivo");
    String rol = (String) session.getAttribute("rolActivo");

    if (nombre == null || rol == null || !"docente".equalsIgnoreCase(rol)) {
        session.setAttribute("mensaje", "⚠️ Acceso restringido: requiere rol docente.");
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }

    int totalClases = (request.getAttribute("totalClases") != null) ? (Integer) request.getAttribute("totalClases") : 0;
    int totalNotas = (request.getAttribute("totalNotas") != null) ? (Integer) request.getAttribute("totalNotas") : 0;
    int notasEnviadas = (request.getAttribute("notasEnviadas") != null) ? (Integer) request.getAttribute("notasEnviadas") : 0;
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
    <style>
        .indicador-box {
            background: #ffffff;
            border-radius: 10px;
            padding: 20px;
            box-shadow: 0 2px 8px rgba(0,0,0,0.08);
            margin-bottom: 20px;
        }
        .indicador-box h6 {
            font-weight: 600;
            color: #198754;
        }
        .modulo-card {
            background: #ffffff;
            border-radius: 10px;
            padding: 25px;
            text-align: center;
            box-shadow: 0 2px 8px rgba(0,0,0,0.08);
            transition: transform 0.2s;
        }
        .modulo-card:hover {
            transform: translateY(-5px);
        }
        .modulo-card i {
            font-size: 40px;
            color: #198754;
            margin-bottom: 15px;
        }
        .modulo-card h5 {
            font-weight: 600;
            margin-bottom: 10px;
        }
    </style>
</head>
<body class="bg-light">

    <!-- 📂 Menú lateral institucional -->
    <jsp:include page="../fragmentos/sidebar.jsp" />

    <!-- 🧭 Encabezado institucional -->
    <jsp:include page="../fragmentos/header.jsp" />

    <!-- 📊 Indicadores docentes -->
    <div class="container mt-5">
        <h5 class="text-center mb-3">
            <i class="fas fa-chart-pie"></i> Indicadores docentes
        </h5>

        <div class="row text-center mb-5">
            <div class="col-md-4">
                <div class="indicador-box">
                    <h6><i class="fas fa-chalkboard"></i> Clases asignadas</h6>
                    <p class="fs-4"><%= totalClases %></p>
                </div>
            </div>
            <div class="col-md-4">
                <div class="indicador-box">
                    <h6><i class="fas fa-clipboard-check"></i> Notas registradas</h6>
                    <p class="fs-4"><%= totalNotas %></p>
                </div>
            </div>
            <div class="col-md-4">
                <div class="indicador-box">
                    <h6><i class="fas fa-paper-plane"></i> Notas enviadas</h6>
                    <p class="fs-4"><%= notasEnviadas %></p>
                </div>
            </div>
        </div>

        <!-- 📦 Módulos de gestión docente -->
        <h5 class="text-center mb-4"><i class="fas fa-cogs"></i> Módulos de gestión docente</h5>
        <div class="row text-center">
            <div class="col-md-3 mb-4">
                <div class="modulo-card">
                    <i class="fas fa-pen"></i>
                    <h5>Registrar Notas</h5>
                    <p>Registrar calificaciones de tus estudiantes.</p>
                    <a href="RegistrarNotaClaseServlet" class="btn btn-success">Acceder</a>
                </div>
            </div>
            <div class="col-md-3 mb-4">
                <div class="modulo-card">
                    <i class="fas fa-table"></i>
                    <h5>Ver Tablas</h5>
                    <p>Consultar tablas guardadas y enviadas.</p>
                    <a href="VerTablasDocenteServlet" class="btn btn-success">Acceder</a>
                </div>
            </div>
            <div class="col-md-3 mb-4">
                <div class="modulo-card">
                    <i class="fas fa-calendar-alt"></i>
                    <h5>Calendario</h5>
                    <p>Visualizar tus clases y envíos.</p>
                    <a href="#calendario" class="btn btn-success">Acceder</a>
                </div>
            </div>
            <div class="col-md-3 mb-4">
                <div class="modulo-card">
                    <i class="fas fa-file-export"></i>
                    <h5>Exportar Notas</h5>
                    <p>Descargar tus tablas completas en formato institucional.</p>
                    <a href="ExportarTablaNotasServlet" class="btn btn-success">Acceder</a>
                </div>
            </div>
        </div>

        <!-- Calendario visual -->
        <div id="calendario" class="card shadow p-4 mb-4 bg-white rounded-4">
            <h5 class="mb-3 text-center"><i class="fas fa-calendar-alt"></i> Calendario de clases y envíos</h5>
            <iframe src="https://calendar.google.com/calendar/embed?src=tu_correo_institucional%40gmail.com&ctz=America%2FBogota"
                    style="border:0" width="100%" height="400" frameborder="0" scrolling="no" class="rounded-3">
            </iframe>
        </div>
    </div>

    <!-- 📌 Pie de página -->
    <jsp:include page="../fragmentos/footer.jsp" />

</body>
</html>