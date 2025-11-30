<%-- 
    Document   : confirmacionInscripcion
    Created on : 14/11/2025, 9:07:17 p. m.
    Author     : camiv
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.Arrays" %>
<%
    // Datos recibidos desde InscripcionServlet
    String nombre = (String) request.getAttribute("nombreEstudiante");
    String[] asignaturas = (String[]) request.getAttribute("asignaturasSeleccionadas");
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Confirmación de inscripción musical</title>
    <link rel="stylesheet" href="../assets/css/estilos.css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;600&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
    <style>
        body {
            font-family: 'Poppins', sans-serif;
            background-color: #f8f9fa;
            padding: 40px;
        }
        .confirmacion-box {
            background: #fff;
            padding: 30px;
            border-radius: 12px;
            box-shadow: 0 0 10px rgba(0,0,0,0.08);
            max-width: 800px;
            margin: 0 auto;
        }
        .confirmacion-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 25px;
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
    <div class="confirmacion-box">
        <div class="confirmacion-header">
            <h4><i class="fas fa-check-circle text-success"></i> Inscripción completada</h4>
            <a href="estudiante.jsp" class="btn-volver"><i class="fas fa-arrow-left"></i> Volver al panel</a>
        </div>

        <p><strong>Estudiante:</strong> <%= nombre != null ? nombre : "No disponible" %></p>

        <h5 class="mt-4">Asignaturas musicales inscritas:</h5>
        <ul class="list-group mt-2">
            <% if (asignaturas != null && asignaturas.length > 0) {
                for (String a : asignaturas) { %>
                    <li class="list-group-item"><i class="fas fa-music text-primary"></i> <%= a %></li>
            <% } } else { %>
                <li class="list-group-item text-muted">No se seleccionaron asignaturas.</li>
            <% } %>
        </ul>
    </div>

    <jsp:include page="../fragmentos/footer.jsp" />
</body>
</html>