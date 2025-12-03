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
    
<!-- 📂 Menú lateral institucional -->
    <jsp:include page="../fragmentos/sidebar.jsp" />

    <!-- 🧭 Encabezado institucional -->
    <jsp:include page="../fragmentos/header.jsp" />

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
    <!-- 📌 Pie de página -->
    <jsp:include page="../fragmentos/footer.jsp" />

</body>
</html>