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
        response.sendRedirect("login.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html>
<head>
    <title>Inscripción de asignaturas</title>
    <link rel="stylesheet" href="assets/css/estilos.css">
</head>
<body>
    <div class="header">
        <span>Bienvenido, <%= nombre %> (estudiante)</span>
        <a href="CerrarSesionServlet" class="btn-cerrar">Cerrar sesión</a>
    </div>

    <h2>Selecciona tus asignaturas musicales</h2>

    <form action="InscripcionServlet" method="post">
        <label><input type="checkbox" name="asignaturas" value="Teoría Musical"> Teoría Musical</label><br>
        <label><input type="checkbox" name="asignaturas" value="Instrumento Principal"> Instrumento Principal</label><br>
        <label><input type="checkbox" name="asignaturas" value="Lectura Musical"> Lectura Musical</label><br>
        <label><input type="checkbox" name="asignaturas" value="Ensamble"> Ensamble</label><br>
        <label><input type="checkbox" name="asignaturas" value="Historia de la Música"> Historia de la Música</label><br>
        <label><input type="checkbox" name="asignaturas" value="Armonía"> Armonía</label><br>
        <input type="submit" value="Inscribirme" class="btn-enviar">
    </form>

    <footer>
        © 2025 SymphonySIAS - Sistema de Información Académico
    </footer>
</body>
</html>