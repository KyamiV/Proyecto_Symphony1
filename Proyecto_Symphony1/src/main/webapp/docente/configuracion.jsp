<%-- 
    Document   : configuracion
    Created on : 17/11/2025, 2:49:59 p. m.
    Author     : camiv
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String nombre = (String) session.getAttribute("nombreActivo");
    String rol = (String) session.getAttribute("rolActivo");

    if (nombre == null || rol == null || !"docente".equalsIgnoreCase(rol)) {
        response.sendRedirect("login.jsp");
        return;
    }

    // Recuperar preferencias guardadas en sesión
    Boolean temaOscuro = (Boolean) session.getAttribute("temaOscuro");
    Boolean mostrarIndicadores = (Boolean) session.getAttribute("mostrarIndicadores");
    String idioma = (String) session.getAttribute("idioma");

    if (temaOscuro == null) temaOscuro = false;
    if (mostrarIndicadores == null) mostrarIndicadores = true;
    if (idioma == null) idioma = "es";
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Configuración docente - SymphonySIAS</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/estilos.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
    <style>
        body { font-family: 'Poppins', sans-serif; }
        .config-card {
            max-width: 600px;
            margin: auto;
            border-radius: 15px;
        }
        .config-icon {
            font-size: 2rem;
            color: #0d6efd;
        }
        .sidebar {
            position: fixed;
            top: 0;
            left: -250px;
            width: 250px;
            height: 100%;
            background-color: #f8f9fa;
            box-shadow: 2px 0 5px rgba(0,0,0,0.1);
            transition: left 0.3s ease;
            z-index: 1050;
            padding-top: 60px;
        }
        .sidebar.show { left: 0; }
        .sidebar a {
            display: block;
            padding: 12px 20px;
            color: #333;
            text-decoration: none;
            font-weight: 500;
        }
        .sidebar a:hover { background-color: #e9ecef; }
        .hamburguesa-btn {
            position: fixed;
            top: 15px;
            left: 15px;
            z-index: 1100;
            background-color: #fff;
            border: none;
            font-size: 24px;
            padding: 8px 12px;
            border-radius: 5px;
            box-shadow: 0 2px 6px rgba(0,0,0,0.2);
        }
    </style>
</head>
<body class="bg-light">

<!-- Botón hamburguesa -->
<button class="hamburguesa-btn" onclick="toggleSidebar()">
    <i class="fas fa-bars"></i>
</button>

<!-- Menú lateral -->
<div id="sidebar" class="sidebar">
    <a href="<%= request.getContextPath() %>/dashboard.jsp"><i class="fas fa-home"></i> Inicio</a>
    <a href="<%= request.getContextPath() %>/docente/registrarNotas.jsp"><i class="fas fa-pen"></i> Registrar calificaciones</a>
    <a href="<%= request.getContextPath() %>/docente/verNotasDocente.jsp"><i class="fas fa-chart-bar"></i> Consultar notas</a>
    <a href="<%= request.getContextPath() %>/docente/listadoEstudiantes.jsp"><i class="fas fa-users"></i> Ver estudiantes</a>
    <a href="<%= request.getContextPath() %>/docente/verTablasEnviadas.jsp"><i class="fas fa-table"></i> Tablas enviadas</a>
    <a href="<%= request.getContextPath() %>/docente/configuracion.jsp"><i class="fas fa-cog"></i> Configuración</a>
    <a href="<%= request.getContextPath() %>/CerrarSesionServlet"><i class="fas fa-sign-out-alt"></i> Cerrar sesión</a>
</div>

<!-- Contenido principal -->
<div class="container mt-5">
    <div class="card shadow p-4 config-card bg-white">
        <div class="text-center mb-4">
            <i class="fas fa-user-cog config-icon"></i>
            <h4 class="mt-2">Configuración del docente</h4>
            <p class="text-muted">Personaliza tu experiencia en SymphonySIAS</p>
        </div>

        <!-- Formulario de configuración -->
        <form action="<%= request.getContextPath() %>/ConfiguracionDocenteServlet" method="post">
            <ul class="list-group list-group-flush">
                <li class="list-group-item">
                    <strong>Nombre:</strong> <%= nombre %>
                </li>
                <li class="list-group-item">
                    <strong>Rol:</strong> <%= rol %>
                </li>
                <li class="list-group-item">
                    <strong>Preferencias:</strong>
                    <div class="form-check mt-2">
                        <input class="form-check-input" type="checkbox" id="temaOscuro" name="temaOscuro" <%= temaOscuro ? "checked" : "" %>>
                        <label class="form-check-label" for="temaOscuro">Activar tema oscuro</label>
                    </div>
                    <div class="form-check">
                        <input class="form-check-input" type="checkbox" id="mostrarIndicadores" name="mostrarIndicadores" <%= mostrarIndicadores ? "checked" : "" %>>
                        <label class="form-check-label" for="mostrarIndicadores">Mostrar indicadores en el dashboard</label>
                    </div>
                </li>
                <li class="list-group-item">
                    <strong>Idioma preferido:</strong>
                    <select class="form-select mt-2" name="idioma">
                        <option value="es" <%= "es".equals(idioma) ? "selected" : "" %>>Español</option>
                        <option value="en" <%= "en".equals(idioma) ? "selected" : "" %>>Inglés</option>
                        <option value="pt" <%= "pt".equals(idioma) ? "selected" : "" %>>Portugués</option>
                    </select>
                </li>
            </ul>

            <div class="text-center mt-4">
                <button type="submit" class="btn btn-primary"><i class="fas fa-save"></i> Guardar configuración</button>
            </div>
        </form>
    </div>
</div>

<script>
    function toggleSidebar() {
        document.getElementById("sidebar").classList.toggle("show");
    }
</script>

</body>
</html>