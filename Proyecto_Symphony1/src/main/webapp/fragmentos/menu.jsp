<%-- 
    Document   : menu.jsp
    Created on : 16/11/2025, 11:14:36 a. m.
    Autor      : camiv
    Propósito  : Menú de navegación institucional para el rol docente en SymphonySIAS
    Trazabilidad: se incluye con <jsp:include page="../fragmentos/menu.jsp" />, permite acceso a vistas clave del docente
--%>

<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page session="true" %>
<%
    String rol = (String) session.getAttribute("rolActivo");
%>

<!-- Botón hamburguesa -->
<button class="hamburguesa-btn" onclick="document.querySelector('.sidebar').classList.toggle('show')">
    <i class="fas fa-bars"></i>
</button>

<!-- Sidebar institucional -->
<div class="sidebar">
    <a href="<%= request.getContextPath() %>/index.jsp">
        <i class="fas fa-home"></i> Inicio
    </a>

    <% if ("admin".equals(rol)) { %>
        <div id="sidebar" class="sidebar">
    <a href="<%= request.getContextPath() %>/PanelAdministradorServlet"><i class="fas fa-home"></i> Inicio</a>
    <a href="<%= request.getContextPath() %>/VerUsuariosServlet"><i class="fas fa-users"></i> Gestionar usuarios</a>
    <a href="<%= request.getContextPath() %>/NotasAdministradorServlet"><i class="fas fa-chart-bar"></i> Consultar notas</a>
    <a href="<%= request.getContextPath() %>/AsignarEstudiantesServlet"><i class="fas fa-user-plus"></i> Asignar estudiantes</a>
    <a href="<%= request.getContextPath() %>/administrador/bitacora.jsp"><i class="fas fa-clipboard-list"></i> Bitácora institucional</a>
    <a href="<%= request.getContextPath() %>/VerAuditoriaServlet"><i class="fas fa-shield-alt"></i> Auditoría institucional</a>
    <a href="<%= request.getContextPath() %>/ConfiguracionServlet"><i class="fas fa-cogs"></i> Configuración</a>
    <a href="<%= request.getContextPath() %>/CerrarSesionServlet"><i class="fas fa-sign-out-alt"></i> Cerrar sesión</a>
</div>
    <% } else if ("docente".equals(rol)) { %>
        <a href="<%= request.getContextPath() %>/docente/listadoClases.jsp">
            <i class="fas fa-chalkboard-teacher"></i> Clases asignadas
        </a>
        <a href="<%= request.getContextPath() %>/docente/registrarNotas.jsp">
            <i class="fas fa-pen"></i> Registrar notas
        </a>
        <a href="<%= request.getContextPath() %>/docente/verObservaciones.jsp">
            <i class="fas fa-comments"></i> Observaciones
        </a>
    <% } else if ("estudiante".equals(rol)) { %>
        <a href="<%= request.getContextPath() %>/estudiante/verClasesEstudiante.jsp">
            <i class="fas fa-music"></i> Mis clases
        </a>
        <a href="<%= request.getContextPath() %>/estudiante/verNotas.jsp">
            <i class="fas fa-clipboard-check"></i> Mis notas
        </a>
        <a href="<%= request.getContextPath() %>/estudiante/verMensajes.jsp">
            <i class="fas fa-envelope"></i> Mensajes
        </a>
    <% } %>

    <a href="<%= request.getContextPath() %>/LoginServlet">
        <i class="fas fa-sign-out-alt"></i> Cerrar sesión
    </a>
</div>