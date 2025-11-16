<%-- 
    Document   : certificados
    Created on : 14/11/2025, 8:24:34 p. m.
    Author     : camiv
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String nombre = (String) session.getAttribute("nombreActivo");
    String rol = (String) session.getAttribute("rolActivo");

    if (nombre == null || rol == null || !"estudiante".equalsIgnoreCase(rol)) {
        response.sendRedirect("../login.jsp");
        return;
    }

    String claseHeader = "dashboard-estudiante";
    String iconoRol = "fas fa-file-alt";
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Mis certificados - SymphonySIAS</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;600&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
    <link rel="stylesheet" href="../assets/css/estilos.css">
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
            max-width: 1100px;
            margin: 0 auto;
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
        .btn-volver {
            background-color: #198754;
            color: white;
            padding: 10px 20px;
            border-radius: 6px;
            text-decoration: none;
            font-weight: 500;
        }
        .btn-volver:hover {
            background-color: #157347;
        }
    </style>
</head>
<body>
    <div class="dashboard-box">
        <div class="dashboard-header <%= claseHeader %>">
            <div>
                <div class="dashboard-title"><i class="<%= iconoRol %>"></i> Certificados</div>
                <div><strong><%= nombre %></strong> (estudiante)</div>
            </div>
            <div style="display: flex; align-items: center; gap: 15px;">
                <img src="../assets/img/logo.png" alt="Logo SymphonySIAS">
                <a href="../CerrarSesionServlet" class="btn btn-outline-light">Cerrar sesión</a>
            </div>
        </div>

        <div class="dashboard-body">
            <h4 class="text-center mt-3">Visualización de certificados musicales</h4>
            <p class="text-center">Desde aquí podrás visualizar y descargar tus certificados académicos.</p>

            <div class="alert alert-success mt-4 text-center">
                <i class="fas fa-file-alt"></i> Esta sección está en construcción. Pronto podrás ver tus certificados aquí.
            </div>

            <div class="mt-4 text-start">
                <a href="../estudiante/estudiante.jsp" class="btn-volver"><i class="fas fa-arrow-left"></i> Volver al panel</a>
            </div>
        </div>
    </div>

    <footer class="text-center mt-5 text-muted">
        © 2025 SymphonySIAS - Sistema de Información Académico Musical
    </footer>
</body>
</html>