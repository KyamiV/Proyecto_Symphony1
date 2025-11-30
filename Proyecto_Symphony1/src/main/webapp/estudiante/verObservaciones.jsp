<%--  
    Document   : verObservaciones.jsp
    Created on : 18/11/2025, 11:53:31 a. m.
    Autor      : camiv
    Rol        : estudiante
    Propósito  : Visualizar observaciones pedagógicas registradas por los docentes
    Trazabilidad: recibe atributo 'observaciones' desde VerObservacionesEstudianteServlet, validado con auditoría institucional
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

    // 📦 Datos recibidos desde el servlet
    List<Map<String, String>> observaciones = (List<Map<String, String>>) request.getAttribute("observaciones");
    String mensaje = (String) request.getAttribute("mensaje");
%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Observaciones pedagógicas - SymphonySIAS</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;600&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/estilos.css">

    <style>
        body {
            font-family: 'Poppins', sans-serif;
            background-color: #f4f6f9;
        }
        .dashboard-box {
            background: #ffffff;
            padding: 25px;
            border-radius: 12px;
            box-shadow: 0 4px 12px rgba(0,0,0,0.08);
            max-width: 1100px;
            margin: 30px auto;
        }
        .dashboard-title {
            font-size: 1.6rem;
            font-weight: 600;
            color: #198754;
        }
        .btn-volver {
            background-color: #198754;
            color: white;
            padding: 10px 20px;
            border-radius: 8px;
            text-decoration: none;
            font-weight: 500;
            transition: background 0.3s ease;
        }
        .btn-volver:hover {
            background-color: #157347;
            color: #fff;
        }
        .table thead {
            background-color: #0dcaf0;
            color: #000;
        }
        .table-hover tbody tr:hover {
            background-color: #f1f3f5;
        }
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
                <div class="dashboard-title"><i class="fas fa-comments"></i> Observaciones pedagógicas</div>
                <div><strong><%= nombre %></strong> (estudiante)</div>
            </div>
            <img src="<%= request.getContextPath() %>/assets/img/logo.png" alt="Logo SymphonySIAS" style="max-height:70px;">
        </div>

        <!-- 📄 Cuerpo principal -->
        <div class="dashboard-body">
            <h4 class="text-center mt-3">Seguimiento académico y observaciones docentes</h4>

            <% if (mensaje != null) { %>
                <div class="alert alert-info text-center mt-3"><%= mensaje %></div>
            <% } else if (observaciones == null || observaciones.isEmpty()) { %>
                <div class="alert alert-warning text-center mt-4">
                    <i class="fas fa-comment-slash"></i> No tienes observaciones registradas aún.
                </div>
            <% } else { %>
                <!-- 📊 Tabla de observaciones -->
                <div class="table-responsive mt-4">
                    <table class="table table-bordered table-hover align-middle text-center">
                        <thead>
                            <tr>
                                <th>Instrumento</th>
                                <th>Etapa</th>
                                <th>Observación</th>
                                <th>Docente</th>
                                <th>Fecha</th>
                                <th>Enviada</th>
                            </tr>
                        </thead>
                        <tbody>
                            <% for (Map<String, String> obs : observaciones) { %>
                                <tr>
                                    <td><%= obs.get("instrumento") %></td>
                                    <td><%= obs.get("etapa_pedagogica") %></td>
                                    <td><%= obs.get("comentario") %></td>
                                    <td><%= obs.get("docente") %></td>
                                    <td><%= obs.get("fecha") %></td>
                                    <td>
                                        <% if ("Sí".equalsIgnoreCase(obs.get("enviada"))) { %>
                                            <span class="badge bg-success">Sí</span>
                                        <% } else { %>
                                            <span class="badge bg-secondary">No</span>
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
                <a href="<%= request.getContextPath() %>/estudiante/estudiante.jsp" class="btn-volver">
                    <i class="fas fa-arrow-left"></i> Volver al panel
                </a>
            </div>
        </div>
    </div>

    <!-- 📌 Pie de página institucional -->
    <jsp:include page="../fragmentos/footer.jsp" />

</body>
</html>