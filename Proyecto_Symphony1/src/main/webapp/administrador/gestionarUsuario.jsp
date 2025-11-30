<%-- 
    Document   : gestionarUsuarios
    Created on : 14/11/2025, 10:33:49 p. m.
    Author     : camiv
    Descripción: Vista institucional del rol administrador para registrar, consultar y administrar cuentas de usuario.
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" session="true" %>
<%@ page import="java.util.*, com.mysymphony.proyecto_symphony1.modelo.Usuario" %>

<%
    // Validación de sesión y rol
    String nombre = (String) session.getAttribute("nombreActivo");
    String rol = (String) session.getAttribute("rolActivo");

    if (nombre == null || rol == null || !"administrador".equalsIgnoreCase(rol)) {
        response.sendRedirect("../login.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Gestión de usuarios - SymphonySIAS</title>
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
            margin-bottom: 30px;
        }
        .tabla-usuarios th {
            background-color: #198754;
            color: white;
        }
        .btn-enviar {
            background-color: #198754;
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
        footer {
            font-size: 0.9rem;
            color: #6c757d;
            padding: 20px 0;
            text-align: center;
        }
    </style>
</head>
<body>

    <!-- Encabezado institucional -->
    <jsp:include page="../fragmentos/header.jsp" />

    <div class="container mt-5">
        <h3 class="text-center mb-4"><i class="fas fa-user-cog"></i> Gestión de cuentas en SymphonySIAS</h3>

        <!-- Mensaje de sesión -->
        <%
            String mensaje = (String) session.getAttribute("mensaje");
            if (mensaje != null) {
        %>
            <div class="alert alert-info text-center">
                <%= mensaje %>
            </div>
        <%
                session.removeAttribute("mensaje");
            }
        %>

        <!-- Formulario para registrar nuevo usuario -->
        <div class="form-box">
            <h3 class="fw-bold text-success">
                <i class="fas fa-users"></i> Registrar Nuevo Usuario
            </h3>

            <form action="<%= request.getContextPath() %>/RegistrarUsuarioServlet" method="post">
                <div class="mb-3">
                    <label for="nombre" class="form-label">Nombre completo:</label>
                    <input type="text" name="nombre" class="form-control" required>
                </div>
                <div class="mb-3">
                    <label for="usuario" class="form-label">Usuario:</label>
                    <input type="text" name="usuario" class="form-control" required>
                </div>
                <div class="mb-3">
                    <label for="clave" class="form-label">Contraseña:</label>
                    <input type="password" name="clave" class="form-control" required>
                </div>
                <div class="mb-3">
                    <label for="rol" class="form-label">Rol:</label>
                    <select name="rol" class="form-select" required>
                        <option value="">Seleccione un rol</option>
                        <option value="estudiante">Estudiante</option>
                        <option value="docente">Docente</option>
                        <option value="administrador">Administrador</option>
                    </select>
                </div>
                <button type="submit" class="btn-enviar"><i class="fas fa-user-plus"></i> Registrar usuario</button>
            </form>
        </div>

        <!-- Tabla de usuarios registrados -->
        <div class="table-responsive">
            <table class="table table-bordered table-hover tabla-usuarios">
                <thead class="text-center">
                    <tr>
                        <th>Nombre</th>
                        <th>Usuario</th>
                        <th>Rol</th>
                        <th>Acciones</th>
                    </tr>
                </thead>
                <tbody>
                    <%
                        List<Usuario> listaUsuarios = (List<Usuario>) request.getAttribute("listaUsuarios");
                        if (listaUsuarios != null && !listaUsuarios.isEmpty()) {
                            for (Usuario u : listaUsuarios) {
                    %>
                    <tr>
                        <td><%= u.getNombre() %></td>
                        <td><%= u.getCorreo() != null ? u.getCorreo() : "—" %></td>
                        <td><%= u.getRol() %></td>
                        <td class="text-center">
                            <a href="<%= request.getContextPath() %>/CargarUsuarioEditarServlet?id=<%= u.getId() %>" class="btn btn-sm btn-warning">
                                <i class="fas fa-edit"></i> Editar
                            </a>
                            <a href="<%= request.getContextPath() %>/EliminarUsuarioServlet?id=<%= u.getId() %>" class="btn btn-sm btn-danger" onclick="return confirm('¿Eliminar este usuario?');">
                                <i class="fas fa-trash"></i> Eliminar
                            </a>
                        </td>
                    </tr>
                    <%
                            }
                        } else {
                    %>
                    <tr>
                        <td colspan="4" class="text-center text-muted">No hay usuarios registrados en el sistema.</td>
                    </tr>
                    <%
                        }
                    %>
                </tbody>
            </table>
        </div>

        <!-- Botón de regreso -->
        <div class="mt-4 text-end">
            <a href="<%= request.getContextPath() %>/PanelAdministradorServlet" class="btn-volver">
                <i class="fas fa-arrow-left"></i> Volver al panel
            </a>
        </div>
    </div>

    <!-- Pie de página -->
    <jsp:include page="../fragmentos/footer.jsp" />

</body>
</html>