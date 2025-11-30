<%-- 
    Document   : verMensajes.jsp
    Created on : 18/11/2025, 11:56:38 a. m.
    Autor      : camiv
    Rol        : estudiante
    Propósito  : Visualizar mensajes institucionales y comunicados académicos dirigidos al estudiante
    Trazabilidad: recibe atributo 'mensajes' desde PanelEstudianteServlet, validado con auditoría institucional
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List, java.util.Map" %>
<%
    // Validación de sesión y rol
    String nombre = (String) session.getAttribute("nombreActivo");
    String rol = (String) session.getAttribute("rolActivo");

    if (nombre == null || rol == null || !"estudiante".equalsIgnoreCase(rol)) {
        response.sendRedirect("../login.jsp");
        return;
    }

    // Variables visuales
    String claseHeader = "dashboard-estudiante";
    String iconoRol = "fas fa-envelope";

    // Datos recibidos desde el servlet
    List<Map<String, String>> mensajes = (List<Map<String, String>>) request.getAttribute("mensajes");
    String mensaje = (String) request.getAttribute("mensaje");
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Mensajes institucionales - SymphonySIAS</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
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
    <!-- Contenedor principal -->
    <div class="dashboard-box">
        <!-- Encabezado institucional -->
        <div class="dashboard-header <%= claseHeader %>">
            <div>
                <div class="dashboard-title"><i class="<%= iconoRol %>"></i> Mensajes institucionales</div>
                <div><strong><%= nombre %></strong> (estudiante)</div>
            </div>
            <img src="../assets/img/logo.png" alt="Logo SymphonySIAS">
        </div>

        <!-- Cuerpo principal -->
        <div class="dashboard-body">
            <h4 class="text-center mt-3">Comunicados académicos y mensajes institucionales</h4>

            <%-- Mensaje si hay error o éxito --%>
            <% if (mensaje != null) { %>
                <div class="alert alert-info text-center mt-3"><%= mensaje %></div>
            <% } else if (mensajes == null || mensajes.isEmpty()) { %>
                <div class="alert alert-warning text-center mt-4">
                    <i class="fas fa-inbox"></i> No tienes mensajes registrados aún.
                </div>
            <% } else { %>
                <%-- Tabla de mensajes --%>
                <div class="table-responsive mt-4">
                    <table class="table table-bordered table-hover align-middle">
                        <thead class="table-primary text-center">
                            <tr>
                                <th>Remitente</th>
                                <th>Tipo</th>
                                <th>Mensaje</th>
                                <th>Fecha</th>
                            </tr>
                        </thead>
                        <tbody>
                            <% for (Map<String, String> msg : mensajes) { %>
                                <tr>
                                    <td><%= msg.get("remitente") %></td>
                                    <td><%= msg.get("tipo") %></td>
                                    <td><%= msg.get("contenido") %></td>
                                    <td><%= msg.get("fecha") %></td>
                                </tr>
                            <% } %>
                        </tbody>
                    </table>
                </div>
            <% } %>

            <!-- Botón de regreso -->
            <div class="mt-4 text-start">
                <a href="estudiante.jsp" class="btn-volver"><i class="fas fa-arrow-left"></i> Volver al panel</a>
            </div>
        </div>
    </div>

    <!-- Pie de página institucional -->
    <jsp:include page="../fragmentos/footer.jsp" />
</body>
</html>