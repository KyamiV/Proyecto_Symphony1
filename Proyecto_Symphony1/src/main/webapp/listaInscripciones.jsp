<%-- 
    Document   : listaInscripciones
    Created on : 13/11/2025, 7:23:34 p. m.
    Author     : camiv
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="com.mysymphony.proyecto_symphony1.modelo.Inscripcion" %>
<%
    List<Inscripcion> inscripciones = (List<Inscripcion>) request.getAttribute("inscripciones");
%>
<html>
<head>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
    <title>Lista de Inscripciones</title>
    <style>
        body { font-family: Arial; margin: 40px; }
        form { margin-bottom: 20px; }
        table { border-collapse: collapse; width: 80%; margin: auto; }
        th, td { border: 1px solid #ccc; padding: 8px; text-align: left; }
        th { background-color: #f2f2f2; }
    </style>
</head>
<body>
    <h2 style="text-align:center;">Inscripciones registradas</h2>

    <form action="InscripcionListServlet" method="get" style="text-align:center;">
        <input type="text" name="filtro" placeholder="Buscar por estudiante o programa">
        <button type="submit">Buscar</button>
    </form>

    <table>
        <tr>
            <th>Estudiante</th>
            <th>Programa</th>
            <th>Fecha</th>
        </tr>
        <%
            if (inscripciones != null && !inscripciones.isEmpty()) {
                for (Inscripcion i : inscripciones) {
        %>
        <tr>
            <td><%= i.getEstudiante() %></td>
            <td><%= i.getPrograma() %></td>
            <td><%= i.getFecha() %></td>
        </tr>
        <%
                }
            } else {
        %>
        <tr><td colspan="3">No hay inscripciones registradas.</td></tr>
        <%
            }
        %>
    </table>
</body>
</html>