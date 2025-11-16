<%-- 
    Document   : inscripcion
    Created on : 13/11/2025, 7:12:16 p. m.
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
    <title>Inscripción de asignaturas - SymphonySIAS</title>
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
        .btn-enviar {
            background-color: #198754;
            color: white;
            padding: 10px 20px;
            border: none;
            border-radius: 6px;
            font-weight: 500;
            margin-top: 20px;
        }
        .btn-enviar:hover {
            background-color: #157347;
        }
        .form-check {
            margin-bottom: 10px;
        }
    </style>
</head>
<body>
    <div class="dashboard-box">
        <div class="dashboard-header <%= claseHeader %>">
            <div>
                <div class="dashboard-title"><i class="<%= iconoRol %>"></i> Inscripción de asignaturas</div>
                <div><strong><%= nombre %></strong> (estudiante)</div>
            </div>
            <img src="../assets/img/logo.png" alt="Logo SymphonySIAS">
        </div>

        <div class="dashboard-body">
            <h4 class="text-center mt-3">Selecciona tus asignaturas musicales</h4>

            <form action="../InscripcionServlet" method="post" class="mt-4" style="max-width: 700px; margin: 0 auto;">
                <div class="form-check">
                    <input class="form-check-input" type="checkbox" name="asignaturas" value="Teoría Musical" id="teoria">
                    <label class="form-check-label" for="teoria">Teoría Musical</label>
                </div>
                <div class="form-check">
                    <input class="form-check-input" type="checkbox" name="asignaturas" value="Instrumento Principal" id="instrumento">
                    <label class="form-check-label" for="instrumento">Instrumento Principal</label>
                </div>
                <div class="form-check">
                    <input class="form-check-input" type="checkbox" name="asignaturas" value="Lectura Musical" id="lectura">
                    <label class="form-check-label" for="lectura">Lectura Musical</label>
                </div>
                <div class="form-check">
                    <input class="form-check-input" type="checkbox" name="asignaturas" value="Ensamble" id="ensamble">
                    <label class="form-check-label" for="ensamble">Ensamble</label>
                </div>
                <div class="form-check">
                    <input class="form-check-input" type="checkbox" name="asignaturas" value="Historia de la Música" id="historia">
                    <label class="form-check-label" for="historia">Historia de la Música</label>
                </div>
                <div class="form-check">
                    <input class="form-check-input" type="checkbox" name="asignaturas" value="Armonía" id="armonia">
                    <label class="form-check-label" for="armonia">Armonía</label>
                </div>

                <div class="text-center">
                    <input type="submit" value="Inscribirme" class="btn-enviar">
                </div>
            </form>
        </div>
    </div>

    <footer class="text-center mt-5 text-muted">
        &copy; 2025 SymphonySIAS - Sistema de Información Académico
    </footer>
</body>
</html>