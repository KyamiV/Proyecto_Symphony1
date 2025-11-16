<%-- 
    Document   : gestionarUsuarios
    Created on : 14/11/2025, 10:33:49 p. m.
    Author     : camiv
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" session="true" %>
<%
    String nombre = (String) session.getAttribute("nombreActivo");
    String rol = (String) session.getAttribute("rolActivo");

    if (nombre == null || rol == null || !"administrador".equalsIgnoreCase(rol)) {
        response.sendRedirect("login.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html>
<head>
    <title>Gestión de usuarios</title>
    <link rel="stylesheet" href="assets/css/estilos.css">
</head>
<body>

    <jsp:include page="../fragmentos/header.jsp" />

    <h2>Gestión de cuentas en SymphonySIAS</h2>

    <!-- Formulario para registrar nuevo usuario -->
    <form action="RegistrarUsuarioServlet" method="post">
        <label for="nombre">Nombre completo:</label>
        <input type="text" name="nombre" required>

        <label for="usuario">Usuario:</label>
        <input type="text" name="usuario" required>

        <label for="clave">Contraseña:</label>
        <input type="password" name="clave" required>

        <label for="rol">Rol:</label>
        <select name="rol" required>
            <option value="">Seleccione un rol</option>
            <option value="estudiante">Estudiante</option>
            <option value="docente">Docente</option>
            <option value="administrador">Administrador</option>
        </select>

        <button type="submit" class="btn-enviar">Registrar usuario</button>
    </form>

    <!-- Tabla de usuarios registrados -->
    <table class="tabla-usuarios">
        <tr>
            <th>Nombre</th>
            <th>Usuario</th>
            <th>Rol</th>
            <th>Acciones</th>
        </tr>
        <tr>
            <td>Laura Gómez</td>
            <td>laura123</td>
            <td>Estudiante</td>
            <td>
                <a href="EditarUsuarioServlet?id=101" class="btn-enviar">Editar</a>
                <a href="EliminarUsuarioServlet?id=101" class="btn-volver">Eliminar</a>
            </td>
        </tr>
        <!-- Más registros -->
    </table>

    <div style="text-align:center; margin-top:30px;">
        <a href="dashboardAdmin.jsp" class="btn-volver">Volver al panel</a>
    </div>

    <jsp:include page="../fragmentos/footer.jsp" />

</body>
</html>