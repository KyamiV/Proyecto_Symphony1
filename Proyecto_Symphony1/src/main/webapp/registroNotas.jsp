<%-- 
    Document   : registroNotas
    Created on : 14/11/2025, 6:06:08 p. m.
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

    String curso = "Matemáticas 9°"; // Puedes hacerlo dinámico si el docente tiene varios cursos
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

    String exito = request.getParameter("exito");
    String error = request.getParameter("error");
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Registro de Calificaciones</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="assets/css/estilos.css">
    <style>
        body {
            background-color: #f4f6f9;
            padding: 30px;
        }
        h2 {
            color: #333;
        }
        table {
            width: 100%;
            border-collapse: collapse;
            margin-bottom: 20px;
        }
        th, td {
            padding: 10px;
            border: 1px solid #ccc;
        }
        .mensaje {
            font-weight: bold;
            margin-bottom: 20px;
            padding: 10px;
            border-radius: 5px;
        }
        .exito {
            color: green;
            background-color: #e6ffe6;
            border: 1px solid green;
        }
        .error {
            color: red;
            background-color: #ffe6e6;
            border: 1px solid red;
        }
    </style>
</head>
<body>
    <h2>Registro de Calificaciones - <%= curso %></h2>

    <% if ("true".equals(exito)) { %>
        <div class="mensaje exito">✅ Notas guardadas correctamente.</div>
    <% } else if ("true".equals(error)) { %>
        <div class="mensaje error">❌ Error al guardar las notas. Intenta de nuevo.</div>
    <% } %>

    <% if (estudiantes.isEmpty()) { %>
        <div class="mensaje error">⚠️ No hay estudiantes registrados para este curso.</div>
    <% } else { %>
        <form action="GuardarNotasServlet" method="post">
            <input type="hidden" name="curso" value="<%= curso %>">
            <table class="table table-bordered">
                <thead class="table-light">
                    <tr><th>Estudiante</th><th>Nota</th></tr>
                </thead>
                <tbody>
                <% for (String estudiante : estudiantes) {
                    String clave = estudiante.replaceAll(" ", "_");
                %>
                    <tr>
                        <td><%= estudiante %></td>
                        <td>
                            <input type="number" name="nota_<%= clave %>" min="0" max="5" step="0.1" required class="form-control">
                            <input type="hidden" name="estudiante_<%= clave %>" value="<%= estudiante %>">
                        </td>
                    </tr>
                <% } %>
                </tbody>
            </table>
            <button type="submit" class="btn btn-primary">Guardar notas</button>
        </form>
    <% } %>
</body>
</html>