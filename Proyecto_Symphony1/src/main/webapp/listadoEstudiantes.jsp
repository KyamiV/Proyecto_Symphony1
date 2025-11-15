<%-- 
    Document   : listadoEstudiantes
    Created on : 14/11/2025, 8:26:25 p. m.
    Author     : camiv
--%>

<%@ page import="java.sql.*, java.util.*" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String nombre = (String) session.getAttribute("nombreActivo");
    String rol = (String) session.getAttribute("rolActivo");

    if (nombre == null || rol == null || !"docente".equalsIgnoreCase(rol)) {
        response.sendRedirect("login.jsp");
        return;
    }

    String curso = "Matemáticas 9°"; // Puedes hacerlo dinámico
    List<String> estudiantes = new ArrayList<>();

    Connection conn = null;
    PreparedStatement ps = null;
    ResultSet rs = null;

    try {
        Class.forName("com.mysql.cj.jdbc.Driver");
        conn = DriverManager.getConnection("jdbc:mysql://localhost/sias_db", "root", "");

        ps = conn.prepareStatement("SELECT nombre FROM usuarios WHERE rol = 'estudiante' AND curso = ?");
        ps.setString(1, curso);
        rs = ps.executeQuery();

        while (rs.next()) {
            estudiantes.add(rs.getString("nombre"));
        }
    } catch (Exception e) {
        e.printStackTrace();
    } finally {
        try { if (rs != null) rs.close(); } catch (Exception e) {}
        try { if (ps != null) ps.close(); } catch (Exception e) {}
        try { if (conn != null) conn.close(); } catch (Exception e) {}
    }
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Listado de Estudiantes</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="assets/css/estilos.css">
</head>
<body class="container mt-5">
    <h2>Estudiantes inscritos en <%= curso %></h2>

    <table class="table table-striped mt-4">
        <thead>
            <tr><th>#</th><th>Nombre del estudiante</th></tr>
        </thead>
        <tbody>
            <% int i = 1;
               for (String estudiante : estudiantes) { %>
                <tr>
                    <td><%= i++ %></td>
                    <td><%= estudiante %></td>
                </tr>
            <% } %>
        </tbody>
    </table>
</body>
</html>
