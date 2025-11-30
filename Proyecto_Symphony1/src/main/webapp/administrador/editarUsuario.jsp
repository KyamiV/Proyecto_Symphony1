<%-- 
    Document   : editarUsuario
    Created on : 18/11/2025, 10:26:09 p. m.
    Author     : camiv
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.mysymphony.proyecto_symphony1.modelo.Usuario" %>

<%
    Usuario usuario = (Usuario) request.getAttribute("usuarioEditar");
    if (usuario == null) {
        response.sendRedirect("VerUsuariosServlet");
        return;
    }
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Editar usuario - SymphonySIAS</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/estilos.css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
    <style>
        body {
            font-family: 'Poppins', sans-serif;
            background-color: #f4f6f9;
        }
        .form-box {
            background-color: #ffffff;
            padding: 25px;
            border-radius: 10px;
            box-shadow: 0 0 10px rgba(0,0,0,0.08);
            margin-top: 40px;
        }
        .btn-guardar {
            background-color: #0d6efd;
            color: white;
            border: none;
            padding: 8px 16px;
            border-radius: 6px;
        }
        .btn-volver {
            background-color: #6c757d;
            color: white;
            padding: 8px 16px;
            border-radius: 6px;
            text-decoration: none;
        }
        .btn-volver:hover {
            background-color: #5a6268;
        }
    </style>
</head>
<body>

<jsp:include page="../fragmentos/header.jsp" />

<div class="container">
    <div class="form-box">
        <h4 class="mb-4"><i class="fas fa-user-edit"></i> Editar usuario institucional</h4>
        <form action="<%= request.getContextPath() %>/EditarUsuarioServlet" method="post">
            <input type="hidden" name="id" value="<%= usuario.getId() %>">

            <div class="mb-3">
                <label class="form-label">Nombre completo:</label>
                <input type="text" name="nombre" class="form-control" value="<%= usuario.getNombre() %>" required>
            </div>

            <div class="mb-3">
                <label class="form-label">Correo / Usuario:</label>
                <input type="text" name="correo" class="form-control" value="<%= usuario.getCorreo() %>" required>
            </div>

            <div class="mb-3">
                <label class="form-label">Rol:</label>
                <select name="rol" class="form-select" required>
                    <option value="estudiante" <%= "estudiante".equals(usuario.getRol()) ? "selected" : "" %>>Estudiante</option>
                    <option value="docente" <%= "docente".equals(usuario.getRol()) ? "selected" : "" %>>Docente</option>
                    <option value="administrador" <%= "administrador".equals(usuario.getRol()) ? "selected" : "" %>>Administrador</option>
                </select>
            </div>

            <button type="submit" class="btn-guardar"><i class="fas fa-save"></i> Guardar cambios</button>
            <a href="VerUsuariosServlet" class="btn-volver ms-2"><i class="fas fa-arrow-left"></i> Cancelar</a>
        </form>
    </div>
</div>

<jsp:include page="../fragmentos/footer.jsp" />

</body>
</html>