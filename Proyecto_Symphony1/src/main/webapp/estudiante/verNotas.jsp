<%-- 
    Document   : verNotas
    Created on : 14/11/2025, 8:22:19 p. m.
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
    String iconoRol = "fas fa-user-graduate";
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Mis calificaciones - SymphonySIAS</title>
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
                <div class="dashboard-title"><i class="<%= iconoRol %>"></i> Mis calificaciones</div>
                <div><strong><%= nombre %></strong> (estudiante)</div>
            </div>
            <img src="../assets/img/logo.png" alt="Logo SymphonySIAS">
        </div>

        <div class="dashboard-body">
            <div class="text-center mt-4">
                <h4 class="mb-3">Calificaciones por asignatura musical</h4>

                <div class="alert alert-info" style="max-width: 700px; margin: 0 auto;">
                    <p class="mb-1">No tienes calificaciones registradas aún.</p>
                    <p class="mb-0">Una vez que tus docentes ingresen tus notas, aparecerán aquí de forma automática.</p>
                </div>

                <div class="mt-4">
                    <div class="col text-start">
                    <a href="../estudiante/estudiante.jsp" class="btn-volver"><i class="fas fa-arrow-left"></i> Volver al panel</a>
                </div>
            </div>
        </div>
    </div>

    <footer class="text-center mt-5 text-muted">
        &copy; 2025 SymphonySIAS - Sistema de Información Académico
    </footer>
</body>
</html>