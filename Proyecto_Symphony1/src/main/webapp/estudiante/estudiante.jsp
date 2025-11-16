<%-- 
    Document   : estudiante
    Created on : 15/11/2025, 1:28:43 p. m.
    Author     : camiv
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String nombre = (String) session.getAttribute("nombreActivo");
    String rol = (String) session.getAttribute("rolActivo");

    if (nombre == null || rol == null || !"estudiante".equalsIgnoreCase(rol)) {
        response.sendRedirect("login.jsp");
        return;
    }

    String claseHeader = "dashboard-estudiante";
    String claseBoton = "btn-estudiante";
    String iconoRol = "fas fa-user-graduate";
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Panel Estudiante - SymphonySIAS</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
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
        .dashboard-estudiante { background-color: #198754; color: white; }
        .btn-estudiante { background-color: #198754; color: white; }
    </style>
</head>
<body>
    <div class="container dashboard-box">
        <div class="dashboard-header <%= claseHeader %>">
            <div>
                <div class="dashboard-title"><i class="<%= iconoRol %>"></i> Panel Estudiante</div>
                <div>Bienvenida, <strong><%= nombre %></strong> (<%= rol %>)</div>
            </div>
            <img src="../assets/img/logo.png" alt="Logo SymphonySIAS">
        </div>

        <div class="dashboard-body">
            <div class="row">
                <div class="col-md-4">
                    <a href="verNotas.jsp" class="btn-dashboard <%= claseBoton %>">
                        <i class="fas fa-book"></i> Ver mis notas
                    </a>
                </div>
                <div class="col-md-4">
                    <a href="inscripcion.jsp" class="btn-dashboard <%= claseBoton %>">
                        <i class="fas fa-edit"></i> Inscribirme en cursos
                    </a>
                </div>
                <div class="col-md-4">
                    <a href="certificados.jsp" class="btn-dashboard <%= claseBoton %>">
                        <i class="fas fa-file-alt"></i> Mis certificados
                    </a>
                </div>
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