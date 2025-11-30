<%--  
    Document   : sidebar.jsp  
    Created on : 14/11/2025, 10:12:19 p. m.  
    Autor      : camiv  
    Propósito  : Menú lateral institucional dinámico según rol activo  
    Trazabilidad: se incluye con <jsp:include page="fragmentos/sidebar.jsp" />  
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" session="true" %>
<%@ page import="javax.servlet.http.HttpSession" %>

<%
    HttpSession sesion = request.getSession(false);
    String rol = (sesion != null) ? (String) sesion.getAttribute("rolActivo") : null;
%>

<style>
    .sidebar {
        position: fixed;
        top: 0;
        left: 0;
        width: 250px;
        height: 100%;
        background-color: #f8f9fa;
        box-shadow: 2px 0 5px rgba(0,0,0,0.1);
        z-index: 1050;
        padding-top: 60px;
        overflow-y: auto;
    }
    .sidebar a {
        display: block;
        padding: 12px 20px;
        color: #333;
        text-decoration: none;
        font-weight: 500;
        font-family: 'Poppins', sans-serif;
    }
    .sidebar a:hover { background-color: #e9ecef; }
    .sidebar .section-title {
        font-size: 13px;
        font-weight: 600;
        color: #6c757d;
        padding: 8px 20px;
        text-transform: uppercase;
    }
    .sidebar hr {
        margin: 10px 0;
        border-top: 1px solid #dee2e6;
    }
</style>

<% if ("administrador".equalsIgnoreCase(rol)) { %>
    <!-- Sidebar ADMINISTRADOR -->
    <div id="sidebar" class="sidebar">
        <a href="<%= request.getContextPath() %>/PanelAdministradorServlet"><i class="fas fa-home"></i> Inicio</a>
        <hr/>

        <div class="section-title">Gestión académica</div>
        <a href="<%= request.getContextPath() %>/GestionarClasesServlet"><i class="fas fa-chalkboard-teacher"></i> Gestionar clases</a>
        <a href="<%= request.getContextPath() %>/GestionarHorariosServlet"><i class="fas fa-calendar-alt"></i> Horarios Clases</a>
        <a href="<%= request.getContextPath() %>/AsignarDocenteServlet"><i class="fas fa-user-check"></i> Asignar docentes</a>

        <div class="section-title">Gestión de Usuarios</div>
        <!-- ✅ Corregido: apunta al servlet correcto -->
        <a href="<%= request.getContextPath() %>/RegistrarUsuarioServlet" class="btn btn-sm btn-outline-success">
            <i class="fas fa-user-plus"></i> Registrar usuario
        </a>
        <a href="<%= request.getContextPath() %>/ListaInscripcionesServlet"><i class="fas fa-list"></i> Inscripciones</a>

        <div class="section-title">Gestión de usuarios</div>
        <a href="<%= request.getContextPath() %>/VerUsuariosServlet"><i class="fas fa-users"></i> Usuarios</a>

        <div class="section-title">Gestión de notas</div>
        <a href="<%= request.getContextPath() %>/NotasAdministradorServlet"><i class="fas fa-chart-bar"></i> Notas globales</a>
        <a href="<%= request.getContextPath() %>/VerNotasRecibidasServlet"><i class="fas fa-inbox"></i> Notas recibidas</a>

        <div class="section-title">Trazabilidad</div>
        <a href="<%= request.getContextPath() %>/VerAuditoriaServlet"><i class="fas fa-shield-alt"></i> Auditoría</a>
        <a href="<%= request.getContextPath() %>/VerBitacoraServlet"><i class="fas fa-clipboard-list"></i> Bitácora</a>
        <a href="<%= request.getContextPath() %>/VerTablasValidadasServlet"><i class="fas fa-table"></i> Tablas Validadas</a>

        <div class="section-title">Configuración</div>
        <a href="<%= request.getContextPath() %>/administrador/configuracionGeneral.jsp"><i class="fas fa-cogs"></i> Configuración general</a>

        <hr/>
        <a href="<%= request.getContextPath() %>/CerrarSesionServlet"><i class="fas fa-sign-out-alt"></i> Cerrar sesión</a>
    </div>

<% } else if ("docente".equalsIgnoreCase(rol)) { %>


    <!-- Sidebar DOCENTE -->
   <div id="sidebar" class="sidebar">
    <!-- Inicio -->
    <a href="<%= request.getContextPath() %>/PanelDocenteServlet">
        <i class="fas fa-home"></i> Inicio
    </a>
    <hr/>

    <!-- Perfil -->
    <div class="section-title">Mi perfil</div>
    <a href="<%= request.getContextPath() %>/PerfilDocenteServlet">
        <i class="fas fa-user-circle"></i> Perfil
    </a>
    <a href="<%= request.getContextPath() %>/ConfiguracionDocenteServlet">
        <i class="fas fa-cog"></i> Configuración
    </a>

    <!-- Gestión académica -->
    <div class="section-title">Académico</div>
    <a href="<%= request.getContextPath() %>/GestionarAsignacionesServlet">
        <i class="fas fa-chalkboard"></i> Mis clases
    </a>
    <a href="<%= request.getContextPath() %>/ListadoEstudiantesServlet?claseId=1">
        <i class="fas fa-users"></i> Estudiantes
    </a>
    <a href="<%= request.getContextPath() %>/CargarNotasServlet?claseId=1">
        <i class="fas fa-pen"></i> Registrar notas
    </a>
    <a href="<%= request.getContextPath() %>/VerNotasDocenteServlet">
        <i class="fas fa-chart-line"></i> Mis notas
    </a>
    <a href="<%= request.getContextPath() %>/GuardarObservacionesServlet">
        <i class="fas fa-comments"></i> Observaciones
    </a>
    <a href="<%= request.getContextPath() %>/VerTablasDocenteServlet">
        <i class="fas fa-table"></i> Tablas guardadas
    </a>

    <!-- Indicadores -->
    <div class="section-title">Indicadores</div>
    <a href="<%= request.getContextPath() %>/IndicadoresDocenteServlet">
        <i class="fas fa-chart-bar"></i> Estadísticas
    </a>

    <hr/>

    <!-- Cerrar sesión -->
    <a href="<%= request.getContextPath() %>/CerrarSesionServlet">
        <i class="fas fa-sign-out-alt"></i> Cerrar sesión
    </a>
</div>

<% } else if ("estudiante".equalsIgnoreCase(rol)) { %>
    <!-- Sidebar ESTUDIANTE -->
    <div id="sidebar" class="sidebar">
        <a href="<%= request.getContextPath() %>/PanelEstudianteServlet"><i class="fas fa-home"></i> Inicio</a>
        <hr/>
        <div class="section-title">Mi perfil</div>
        <a href="<%= request.getContextPath() %>/VerPerfilEstudianteServlet"><i class="fas fa-id-card"></i> Perfil</a>

        <div class="section-title">Académico</div>
        <a href="<%= request.getContextPath() %>/VerClasesEstudianteServlet"><i class="fas fa-book"></i> Mis clases</a>
        <a href="<%= request.getContextPath() %>/VerNotasEstudianteServlet"><i class="fas fa-chart-line"></i> Mis notas</a>
        <a href="<%= request.getContextPath() %>/VerObservacionesEstudianteServlet"><i class="fas fa-comments"></i> Observaciones</a>
        <a href="<%= request.getContextPath() %>/CertificadosEstudianteServlet"><i class="fas fa-certificate"></i> Certificados</a>
        <a href="<%= request.getContextPath() %>/ActividadesEstudianteServlet?claseId=1"><i class="fas fa-tasks"></i> Actividades</a>
        <!-- Nota: el parámetro claseId debe ser dinámico según la clase seleccionada -->

        <hr/>

        <a href="<%= request.getContextPath() %>/CerrarSesionServlet"><i class="fas fa-sign-out-alt"></i> Cerrar sesión</a>
    </div>
<% } %>