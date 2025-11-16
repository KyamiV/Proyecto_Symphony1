<%-- 
    Document   : observaciones
    Created on : 14/11/2025, 9:54:04 p. m.
    Author     : camiv
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.sql.*, java.util.*" %>
<%
    String nombre = (String) session.getAttribute("nombreActivo");
    String rol = (String) session.getAttribute("rolActivo");

    if (nombre == null || rol == null || !"docente".equalsIgnoreCase(rol)) {
        response.sendRedirect("login.jsp");
        return;
    }

    List<String> estudiantes = new ArrayList<>();
    try {
        Class.forName("com.mysql.cj.jdbc.Driver");
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/sias_db", "root", "");
             PreparedStatement ps = conn.prepareStatement("SELECT nombre FROM usuarios WHERE rol = 'estudiante'");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                estudiantes.add(rs.getString("nombre"));
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Observaciones Académicas</title>
    <link rel="stylesheet" href="assets/css/estilos.css">
</head>
<body>
    <jsp:include page="fragmentos/header.jsp" />
    <h2>Registrar Observaciones Académicas</h2>

    <form action="GuardarObservacionesServlet" method="post">
        <table class="tabla">
            <tr>
                <th>Estudiante</th>
                <th>Observación</th>
            </tr>
            <% for (String estudiante : estudiantes) {
                String clave = estudiante.replaceAll(" ", "_");
            %>
            <tr>
                <td><%= estudiante %></td>
                <td>
                    <textarea name="obs_<%= clave %>" rows="2" cols="50" placeholder="Escribe una observación..."></textarea>
                    <input type="hidden" name="estudiante_<%= clave %>" value="<%= estudiante %>">
                </td>
            </tr>
            <% } %>
        </table>
        <button type="submit" class="boton">Guardar Observaciones</button>
    </form>

    <jsp:include page="fragmentos/footer.jsp" />
</body>
</html>