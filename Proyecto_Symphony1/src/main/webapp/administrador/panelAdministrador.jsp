<%--  
    Document   : panelAdministrador.jsp  
    Autor      : camiv  
    Descripción: Vista principal del rol administrador en SymphonySIAS.  
                 Muestra indicadores institucionales, menú lateral y mensaje motivador.  
                 Requiere atributos: totalUsuarios, totalNotas, totalAsignaciones.  
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" session="true" %>
<%@ page import="java.util.*" %>

<%
    String nombre = (String) session.getAttribute("nombreActivo");
    String rol = (String) session.getAttribute("rolActivo");

    if (nombre == null || rol == null || !"administrador".equalsIgnoreCase(rol)) {
        session.setAttribute("mensaje", "⚠️ Acceso restringido: requiere rol administrador.");
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }

    int totalUsuarios = (request.getAttribute("totalUsuarios") != null) ? (Integer) request.getAttribute("totalUsuarios") : 0;
    int totalNotas = (request.getAttribute("totalNotas") != null) ? (Integer) request.getAttribute("totalNotas") : 0;
    int totalAsignaciones = (request.getAttribute("totalAsignaciones") != null) ? (Integer) request.getAttribute("totalAsignaciones") : 0;
    int totalDocentesActivos = (request.getAttribute("totalDocentesActivos") != null) ? (Integer) request.getAttribute("totalDocentesActivos") : 0;
%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Panel administrador - SymphonySIAS</title>
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

    <!-- 📊 Indicadores institucionales -->
    <div class="container mt-5">
        <h5 class="text-center mb-3">
            <i class="fas fa-chart-pie"></i> Indicadores institucionales
        </h5>

        <div class="row text-center mb-5">
            <div class="col-md-3">
                <div class="indicador-box">
                    <h6><i class="fas fa-users-cog"></i> Usuarios registrados</h6>
                    <p class="fs-4"><%= totalUsuarios %></p>
                </div>
            </div>
            <div class="col-md-3">
                <div class="indicador-box">
                    <h6><i class="fas fa-clipboard-list"></i> Notas registradas</h6>
                    <p class="fs-4"><%= totalNotas %></p>
                </div>
            </div>
            <div class="col-md-3">
                <div class="indicador-box">
                    <h6><i class="fas fa-chalkboard-teacher"></i> Asignaciones activas</h6>
                    <p class="fs-4"><%= totalAsignaciones %></p>
                </div>
            </div>
            <div class="col-md-3">
                <div class="indicador-box">
                    <h6><i class="fas fa-user-tie"></i> Docentes activos</h6>
                    <p class="fs-4"><%= totalDocentesActivos %></p>
                </div>
            </div>
        </div>

        <!-- 📦 Módulos de gestión -->
        <h5 class="text-center mb-4"><i class="fas fa-cogs"></i> Módulos de gestión</h5>
        <div class="row text-center">
            <div class="col-md-3 mb-4">
                <div class="modulo-card">
                    <i class="fas fa-users"></i>
                    <h5>Gestión de Usuarios</h5>
                    <p>Administrar cuentas y roles institucionales.</p>
                    <a href="VerUsuariosServlet" class="btn btn-success">Acceder</a>
                </div>
            </div>
            <div class="col-md-3 mb-4">
                <div class="modulo-card">
                    <i class="fas fa-chalkboard"></i>
                    <h5>Gestión de Clases</h5>
                    <p>Crear, asignar y validar clases institucionales.</p>
                    <a href="GestionarClasesPrincipalServlet" class="btn btn-success">Acceder</a>
                </div>
            </div>
            <div class="col-md-3 mb-4">
                <div class="modulo-card">
                    <i class="fas fa-user-tie"></i>
                    <h5>Gestión de Docentes</h5>
                    <p>Registrar y administrar docentes.</p>
                    <a href="GestionarDocentesServlet" class="btn btn-success">Acceder</a>
                </div>
            </div>
            <div class="col-md-3 mb-4">
                <div class="modulo-card">
                    <i class="fas fa-chalkboard-teacher"></i>
                    <h5>Gestión de Asignaciones</h5>
                    <p>Administrar clases, docentes y estudiantes asignados.</p>
                    <a href="GestionarAsignacionesServlet" class="btn btn-success">Acceder</a>
                </div>
            </div>
        </div>
    </div>

    <!-- 📌 Pie de página -->
    <jsp:include page="../fragmentos/footer.jsp" />

</body>
</html>