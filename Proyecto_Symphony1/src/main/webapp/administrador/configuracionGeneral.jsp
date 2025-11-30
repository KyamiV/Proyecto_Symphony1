<%--  
    Document   : configuracionGeneral.jsp
    Created on : 14/11/2025, 8:35:57 p. m.
    Author     : Camila
    Descripción: Configuración general del sistema SymphonySIAS.
                 Menú maestro visual para definir parámetros globales como roles, claves maestras, etapas y colores institucionales.
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" session="true" %>
<%
    String nombre = (String) session.getAttribute("nombreActivo");
    String rol = (String) session.getAttribute("rolActivo");

    if (nombre == null || rol == null || !"administrador".equalsIgnoreCase(rol)) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Configuración general | SymphonySIAS</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/estilos.css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
    <style>
        .config-box {
            background-color: #fff;
            padding: 30px;
            border-radius: 12px;
            box-shadow: 0 4px 12px rgba(0,0,0,0.06);
        }
        .card-option {
            transition: transform 0.2s ease-in-out;
        }
        .card-option:hover {
            transform: scale(1.05);
        }
    </style>
</head>
<body class="bg-light">

<jsp:include page="../fragmentos/header.jsp" />

<div class="container mt-5 config-box">
    <h3 class="text-center mb-4"><i class="fas fa-cogs"></i> Configuración general del sistema</h3>

    <p class="text-center">
        Desde aquí podrás definir parámetros globales como roles, claves maestras, duración de etapas, colores institucionales y más.
    </p>

    <div class="alert alert-info text-center mt-4">
        <i class="fas fa-tools fa-lg"></i> Esta sección está en construcción. 
        Pronto podrás configurar el sistema desde aquí.
    </div>

    <!-- Menú maestro visual con cards -->
    <div class="row mt-4">
        <div class="col-md-4 mb-3">
            <div class="card card-option h-100 text-center">
                <div class="card-body">
                    <i class="fas fa-music fa-2x mb-3 text-primary"></i>
                    <h5 class="card-title">Instrumentos y etapas</h5>
                    <p class="card-text">Gestiona instrumentos musicales y etapas pedagógicas.</p>
                    <a href="<%= request.getContextPath() %>/ConfiguracionServlet" class="btn btn-outline-primary">
                        Abrir módulo
                    </a>
                </div>
            </div>
        </div>

        <div class="col-md-4 mb-3">
            <div class="card card-option h-100 text-center">
                <div class="card-body">
                    <i class="fas fa-users-cog fa-2x mb-3 text-success"></i>
                    <h5 class="card-title">Roles</h5>
                    <p class="card-text">Configura roles activos y permisos institucionales.</p>
                    <a href="<%= request.getContextPath() %>/ConfiguracionServlet" class="btn btn-outline-success">
                        Abrir módulo
                    </a>
                </div>
            </div>
        </div>

        <div class="col-md-4 mb-3">
            <div class="card card-option h-100 text-center">
                <div class="card-body">
                    <i class="fas fa-sliders-h fa-2x mb-3 text-warning"></i>
                    <h5 class="card-title">Parámetros técnicos</h5>
                    <p class="card-text">Define parámetros técnicos del sistema.</p>
                    <a href="<%= request.getContextPath() %>/ConfiguracionServlet" class="btn btn-outline-warning">
                        Abrir módulo
                    </a>
                </div>
            </div>
        </div>
    </div>

    <div class="text-end mt-4">
        <a href="<%= request.getContextPath() %>/PanelAdministradorServlet" class="btn btn-secondary">
            <i class="fas fa-arrow-left"></i> Volver al panel
        </a>
    </div>
</div>

<jsp:include page="../fragmentos/footer.jsp" />
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>

</body>
</html>