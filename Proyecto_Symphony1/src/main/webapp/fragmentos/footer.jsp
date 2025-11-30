<%-- 
    Document   : footer.jsp
    Created on : 14/11/2025, 10:12:31 p. m.
    Autor      : camiv
    Propósito  : Pie de página institucional reutilizable para todas las vistas del sistema SymphonySIAS
    Trazabilidad: muestra año dinámico y nombre del sistema, se incluye con <jsp:include page="fragmentos/footer.jsp" />
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.text.SimpleDateFormat, java.util.Date, java.util.Calendar" %>

<%
    // Año actual
    Calendar cal = Calendar.getInstance();
    int anio = cal.get(Calendar.YEAR);

    // Fecha y hora de acceso formateada
    String fechaAcceso = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date());

    // Usuario y rol desde sesión
    String usuario = (String) session.getAttribute("nombreActivo");
    String rol = (String) session.getAttribute("rolActivo");
%>

<footer class="text-center mt-5 text-muted small">
    <hr>
    &copy; <%= anio %> <strong>SymphonySIAS</strong> · Sistema de Información Académico Musical<br>
    Último acceso: <%= fechaAcceso %>
    <% if (usuario != null && rol != null) { %>
        — Usuario: <%= usuario %> (Rol: <%= rol %>)
    <% } else { %>
        — Sesión no activa
    <% } %>
    <br>
    <a href="<%= request.getContextPath() %>/administrador/ayuda.jsp" class="text-decoration-none text-success">Ayuda</a> ·
    <a href="<%= request.getContextPath() %>/administrador/politicaPrivacidad.jsp" class="text-decoration-none text-success">Política de privacidad</a>
</footer>