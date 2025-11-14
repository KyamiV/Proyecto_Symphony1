<%-- 
    Document   : dashboard
    Created on : 13/11/2025, 6:59:40 p. m.
    Author     : camiv
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String nombre = (String) session.getAttribute("nombreActivo");
    String rol = (String) session.getAttribute("rolActivo");

    if (nombre == null || rol == null) {
        response.sendRedirect("login.jsp");
        return;
    }
%>
<html>
<head>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
    <title>Panel de Bienvenida</title>
    <style>
        body { font-family: Arial; margin: 40px; }
        h2 { color: #2c3e50; }
        .rol { font-weight: bold; color: #2980b9; }
        .acciones a { display: block; margin: 10px 0; }
    </style>
</head>
<body>
    <h2>Bienvenida, <%= nombre %>!</h2>
    <p>Tu rol es: <span class="rol"><%= rol %></span></p>

    <form action="LogoutServlet" method="post">
        <button type="submit">Cerrar sesión</button>
    </form>

    <hr>
    <div class="acciones">
        <h3>Acciones disponibles:</h3>
        <%
            switch (rol.toLowerCase()) {
                case "estudiante":
        %>
                    <a href="inscripcion.jsp">Ver o registrar inscripciones</a>
        <%
                    break;
                case "docente":
        %>
                    <a href="#">Registrar calificaciones</a>
        <%
                    break;
                case "coordinador académico":
        %>
                    <a href="registro.jsp">Registrar nuevos usuarios</a>
                    <a href="inscripcion.jsp">Gestionar inscripciones</a>
        <%
                    break;
                default:
        %>
                    <p>No hay acciones definidas para tu rol.</p>
        <%
                    break;
            }
        %>
    </div>
</body>
</html>