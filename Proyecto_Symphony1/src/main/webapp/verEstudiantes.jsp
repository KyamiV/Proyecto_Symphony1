<%-- 
    Document   : verEstudiantes
    Created on : 14/11/2025, 9:47:37 p. m.
    Author     : camiv
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.sql.*" %>
<%@ page session="true" %>
<%
    if (session.getAttribute("rol") == null || !session.getAttribute("rol").equals("docente")) {
        response.sendRedirect("login.jsp");
        return;
    }

    String grupoFiltro = request.getParameter("grupo");
    Connection conn = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
%>
<!DOCTYPE html>
<html>
<head>
    <title>Listado de Estudiantes - SymphonySIAS</title>
    <link rel="stylesheet" href="assets/css/estilos.css">
    <style>
        body {
            background-color: #f4f6f9;
            padding: 30px;
        }
        h2 {
            color: #333;
        }
        .tabla {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
        }
        .tabla th, .tabla td {
            padding: 10px;
            border: 1px solid #ccc;
        }
        .tabla th {
            background-color: #e9ecef;
        }
    </style>
</head>
<body>
    <jsp:include page="fragmentos/header.jsp" />
    <h2>Estudiantes Inscritos</h2>

    <form method="get" action="verEstudiantes.jsp">
        <label for="grupo">Filtrar por grupo:</label>
        <select name="grupo" id="grupo">
            <option value="">Todos</option>
            <option value="A" <%= "A".equals(grupoFiltro) ? "selected" : "" %>>Grupo A</option>
            <option value="B" <%= "B".equals(grupoFiltro) ? "selected" : "" %>>Grupo B</option>
            <option value="C" <%= "C".equals(grupoFiltro) ? "selected" : "" %>>Grupo C</option>
        </select>
        <button type="submit">Filtrar</button>
    </form>

    <table class="tabla">
        <tr>
            <th>ID</th>
            <th>Nombre</th>
            <th>Grupo</th>
            <th>Asignatura</th>
            <th>Curso</th>
        </tr>
        <%
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                conn = DriverManager.getConnection("jdbc:mysql://localhost/sias_db", "root", "");

                String sql = "SELECT id, nombre, grupo, asignatura, curso FROM usuarios WHERE rol = 'estudiante'";
                if (grupoFiltro != null && !grupoFiltro.isEmpty()) {
                    sql += " AND grupo = ?";
                    ps = conn.prepareStatement(sql);
                    ps.setString(1, grupoFiltro);
                } else {
                    ps = conn.prepareStatement(sql);
                }

                rs = ps.executeQuery();
                boolean hayResultados = false;

                while (rs.next()) {
                    hayResultados = true;
        %>
        <tr>
            <td><%= rs.getInt("id") %></td>
            <td><%= rs.getString("nombre") %></td>
            <td><%= rs.getString("grupo") %></td>
            <td><%= rs.getString("asignatura") %></td>
            <td><%= rs.getString("curso") %></td>
        </tr>
        <%
                }

                if (!hayResultados) {
        %>
        <tr>
            <td colspan="5">⚠️ No hay estudiantes registrados en este grupo.</td>
        </tr>
        <%
                }

            } catch (Exception e) {
                out.println("<tr><td colspan='5'>❌ Error al cargar estudiantes.</td></tr>");
                e.printStackTrace();
            } finally {
                try { if (rs != null) rs.close(); } catch (Exception e) {}
                try { if (ps != null) ps.close(); } catch (Exception e) {}
                try { if (conn != null) conn.close(); } catch (Exception e) {}
            }
        %>
    </table>

    <jsp:include page="fragmentos/footer.jsp" />
</body>
</html>