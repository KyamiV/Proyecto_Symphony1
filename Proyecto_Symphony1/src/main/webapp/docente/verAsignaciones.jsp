<%-- 
    Document   : verAsignaciones
    Created on : 15/11/2025, 7:08:46 p. m.
    Author     : camiv
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.*" %>

<%
    HttpSession sesion = request.getSession();
    String rol = (String) sesion.getAttribute("rolActivo");

    if (rol == null || !"docente".equalsIgnoreCase(rol)) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }

    List<Map<String, String>> asignaciones = (List<Map<String, String>>) request.getAttribute("asignaciones");
    String nombreDocente = (String) request.getAttribute("nombreDocente");
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Estudiantes asignados</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/estilos/estilos.css">
</head>
<body>
    <h2>Estudiantes asignados a <%= nombreDocente %></h2>

    <%
        if (asignaciones == null || asignaciones.isEmpty()) {
    %>
        <p class="mensaje">No tienes estudiantes asignados aún. El administrador debe realizar la asignación.</p>
    <%
        } else {
    %>
        <table border="1">
            <tr>
                <th>Nombre del estudiante</th>
                <th>Instrumento asignado</th>
                <th>Fecha de asignación</th>
                <th>Acciones</th>
            </tr>
            <%
                for (Map<String, String> fila : asignaciones) {
            %>
                <tr>
                    <td><%= fila.get("estudiante") %></td>
                    <td><%= fila.get("instrumento") %></td>
                    <td><%= fila.get("fecha") %></td>
                    <td>
                        <form action="${pageContext.request.contextPath}/docente/editarAsignacion.jsp" method="get" style="display:inline;">
                            <input type="hidden" name="id" value="<%= fila.get("id") %>">
                            <input type="hidden" name="nombre" value="<%= fila.get("estudiante") %>">
                            <input type="hidden" name="instrumento" value="<%= fila.get("instrumento") %>">
                            <button type="submit">Editar</button>
                        </form>
                    </td>
                </tr>
            <%
                }
            %>
        </table>
    <%
        }
    %>

    <p><a href="${pageContext.request.contextPath}/LoginServlet">← Volver al panel</a></p>

    <%
        String mensaje = (String) request.getAttribute("mensaje");
        if (mensaje != null) {
    %>
        <div class="mensaje"><%= mensaje %></div>
    <%
        }
    %>
</body>
</html>