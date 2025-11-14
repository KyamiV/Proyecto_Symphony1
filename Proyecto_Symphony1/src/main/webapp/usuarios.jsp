<%-- 
    Document   : usuarios
    Created on : 13/11/2025, 8:45:37 p. m.
    Author     : camiv
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Usuarios Registrados</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">
    <div class="container mt-5">
        <h2 class="mb-4">Usuarios Registrados</h2>
        <table class="table table-bordered table-striped">
            <thead class="table-dark">
                <tr>
                    <th>ID</th>
                    <th>Correo</th>
                    <th>Nombre</th>
                    <th>Rol</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="u" items="${usuarios}">
                    <tr>
                        <td>${u.id}</td>
                        <td>${u.usuario}</td>
                        <td>${u.nombre}</td>
                        <td>${u.rol}</td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
        <a href="dashboard.jsp" class="btn btn-secondary">Volver al panel</a>
    </div>
</body>
</html>