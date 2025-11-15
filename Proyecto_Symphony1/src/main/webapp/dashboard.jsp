<%-- 
    Document   : dashboard
    Created on : 13/11/2025, 6:59:40 p. m.
    Author     : camiv
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String nombre = (String) session.getAttribute("nombreActivo");
    String rol = (String) session.getAttribute("rolActivo");

    if (nombre == null || rol == null) {
        response.sendRedirect("login.jsp");
        return;
    }

    String claseHeader = "";
    String claseBoton = "";
    String iconoRol = "fas fa-user";

    if ("estudiante".equalsIgnoreCase(rol)) {
        claseHeader = "dashboard-estudiante";
        claseBoton = "btn-estudiante";
        iconoRol = "fas fa-user-graduate";
    } else if ("docente".equalsIgnoreCase(rol)) {
        claseHeader = "dashboard-docente";
        claseBoton = "btn-docente";
        iconoRol = "fas fa-chalkboard-teacher";
    } else if ("coordinador académico".equalsIgnoreCase(rol)) {
        claseHeader = "dashboard-coordinador";
        claseBoton = "btn-coordinador";
        iconoRol = "fas fa-user-cog";
    } else if ("administrador".equalsIgnoreCase(rol)) {
        claseHeader = "dashboard-admin";
        claseBoton = "btn-admin";
        iconoRol = "fas fa-tools";
    }
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Dashboard - SymphonySIAS</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;600&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
    <link rel="stylesheet" href="assets/css/estilos.css">
    <style>
        body {
            font-family: 'Poppins', sans-serif;
            background-color: #f4f6f9;
            padding: 30px;
        }
        .dashboard-box {
            background: #ffffff;
            padding: 25px;
            border-radius: 10px;
            box-shadow: 0 0 10px rgba(0,0,0,0.08);
        }
        .dashboard-header {
            padding: 15px 20px;
            border-radius: 10px 10px 0 0;
            display: flex;
            align-items: center;
            justify-content: space-between;
        }
        .dashboard-header img {
            max-height: 80px;
            border-radius: 8px;
        }
        .dashboard-title {
            font-size: 1.5rem;
            font-weight: 600;
        }
        .dashboard-body {
            padding-top: 20px;
        }
        .btn-dashboard {
            font-weight: 500;
            border-radius: 6px;
            margin-bottom: 10px;
            display: block;
            text-align: center;
            padding: 12px;
            text-decoration: none;
        }
        .btn-dashboard i {
            margin-right: 6px;
        }

        /* Colores por rol */
        .dashboard-estudiante { background-color: #198754; color: white; }
        .dashboard-docente { background-color: #0d6efd; color: white; }
        .dashboard-coordinador { background-color: #fd7e14; color: white; }
        .dashboard-admin { background-color: #343a40; color: white; }

        .btn-estudiante { background-color: #198754; color: white; }
        .btn-docente { background-color: #0d6efd; color: white; }
        .btn-coordinador { background-color: #fd7e14; color: white; }
        .btn-admin { background-color: #343a40; color: white; }
    </style>
</head>
<body>
    <div class="container dashboard-box">
        <div class="dashboard-header <%= claseHeader %>">
            <div>
                <div class="dashboard-title"><i class="<%= iconoRol %>"></i> Panel Principal</div>
                <div>Bienvenida, <strong><%= nombre %></strong> (<%= rol %>)</div>
            </div>
            <img src="assets/img/logo.png" alt="Logo SymphonySIAS">
        </div>

        <div class="dashboard-body">
            <div class="row">
                <% if ("estudiante".equalsIgnoreCase(rol)) { %>
                    <div class="col-md-4">
                        <a href="verNotas.jsp" class="btn-dashboard btn-estudiante">
                            <i class="fas fa-book"></i> Ver mis notas
                        </a>
                    </div>
                    <div class="col-md-4">
                        <a href="inscripcion.jsp" class="btn-dashboard btn-estudiante">
                            <i class="fas fa-edit"></i> Inscribirme en cursos
                        </a>
                    </div>
                    <div class="col-md-4">
                        <a href="certificados.jsp" class="btn-dashboard btn-estudiante">
                            <i class="fas fa-file-alt"></i> Mis certificados
                        </a>
                    </div>
                <% } else if ("docente".equalsIgnoreCase(rol)) { %>
                    <div class="col-md-4">
                        <a href="registroNotas.jsp" class="btn-dashboard btn-docente">
                            <i class="fas fa-pen"></i> Registrar calificaciones
                        </a>
                    </div>
                    <div class="col-md-4">
                        <a href="VerNotasServlet" class="btn-dashboard btn-docente">
                            <i class="fas fa-chart-bar"></i> Consultar notas registradas
                        </a>
                    </div>
                    <div class="col-md-4">
                        <a href="listadoEstudiantes.jsp" class="btn-dashboard btn-docente">
                            <i class="fas fa-users"></i> Ver estudiantes
                        </a>
                    </div>
                <% } else if ("coordinador académico".equalsIgnoreCase(rol)) { %>
                    <div class="col-md-4">
                        <a href="usuarios.jsp" class="btn-dashboard btn-coordinador">
                            <i class="fas fa-users-cog"></i> Administrar usuarios
                        </a>
                    </div>
                    <div class="col-md-4">
                        <a href="reportes.jsp" class="btn-dashboard btn-coordinador">
                            <i class="fas fa-chart-line"></i> Reportes académicos
                        </a>
                    </div>
                <% } else if ("administrador".equalsIgnoreCase(rol)) { %>
                    <div class="col-md-4">
                        <a href="UsuarioListServlet" class="btn-dashboard btn-coordinador">
                            <i class="fas fa-tools"></i> Configuración del sistema
                        </a>
                    </div>
                    <div class="col-md-4">
                        <a href="auditoria.jsp" class="btn-dashboard btn-admin">
                            <i class="fas fa-shield-alt"></i> Auditoría
                        </a>
                    </div>
                <% } %>
            </div>

            <div class="text-end mt-4">
                <a href="CerrarSesionServlet" class="btn btn-outline-secondary">Cerrar sesión</a>
            </div>
        </div>
    </div>

    <footer class="text-center mt-5 text-muted">
        &copy; 2025 SymphonySIAS - Sistema de Información Académico
    </footer>
</body>
</html>