<%-- 
    Document   : verObservaciones
    Created on : 14/11/2025, 10:05:14 p. m.
    Author     : camiv
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.sql.*" %>
<%@ page session="true" %>
<%
    String nombre = (String) session.getAttribute("nombreActivo");
    String rol = (String) session.getAttribute("rolActivo");

    if (nombre == null || rol == null || !"docente".equalsIgnoreCase(rol)) {
        response.sendRedirect("login.jsp");
        return;
    }

    Connection conn = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Observaciones Académicas</title>
    <link rel="stylesheet" href="assets/css/estilos.css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
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
    <h2>Observaciones Académicas Registradas</h2>

    <table class="tabla">
        <tr>
            <th>Estudiante</th>
            <th>Observación</th>
            <th>Fecha</th>
        </tr>
        <%
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                conn = DriverManager.getConnection("jdbc:mysql://localhost/sias_db", "root", "");
                ps = conn.prepareStatement("SELECT nombre_estudiante, observacion, fecha FROM observaciones ORDER BY fecha DESC");
                rs = ps.executeQuery();

                boolean hayDatos = false;
                while (rs.next()) {
                    hayDatos = true;
        %>
        <tr>
            <td><%= rs.getString("nombre_estudiante") %></td>
            <td><%= rs.getString("observacion") %></td>
            <td><%= rs.getTimestamp("fecha") %></td>
        </tr>
        <%
                }

                if (!hayDatos) {
        %>
        <tr>
            <td colspan="3">⚠️ No hay observaciones registradas.</td>
        </tr>
        <%
                }

            } catch (Exception e) {
                out.println("<tr><td colspan='3'>❌ Error al cargar observaciones.</td></tr>");
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