<%-- 
    Document   : verUsuarios.jsp
    Autor      : camiv
    Descripción: Vista institucional para mostrar usuarios registrados en SymphonySIAS.
                 Requiere atributo 'usuarios' con lista de objetos Usuario.
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" session="true" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="com.mysymphony.proyecto_symphony1.modelo.Usuario" %>

<%
    List<Usuario> usuarios = (List<Usuario>) request.getAttribute("listaUsuarios");
    List<Map<String,String>> docentes = (List<Map<String,String>>) request.getAttribute("listaDocentes");
    List<Map<String,String>> estudiantes = (List<Map<String,String>>) request.getAttribute("listaEstudiantes");

    // Mensajes desde sesión o request
    String mensaje = (String) session.getAttribute("mensaje");
    if (mensaje == null) {
        mensaje = (String) request.getAttribute("mensaje");
    }
%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Usuarios registrados - SymphonySIAS</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
</head>
<body class="bg-light">

<jsp:include page="/fragmentos/header.jsp" />
<div class="d-flex">
    <jsp:include page="/fragmentos/sidebar.jsp" />

    <div class="flex-grow-1 p-4">
        <h3 class="text-success mb-4"><i class="fas fa-users"></i> Panel de Usuarios, Docentes y Estudiantes</h3>

        <% if (mensaje != null) { %>
            <div class="alert alert-info text-center"><%= mensaje %></div>
            <% session.removeAttribute("mensaje"); %>
        <% } %>

        <!-- 🔹 Usuarios -->
        <h4 class="mb-3"><i class="fas fa-user"></i> Usuarios</h4>
        <table class="table table-bordered table-hover">
            <thead class="table-success text-center">
                <tr>
                    <th>ID</th><th>Nombre</th><th>Correo</th><th>Rol</th><th>Estado</th><th>Acciones</th>
                </tr>
            </thead>
            <tbody>
                <% if (usuarios != null) {
                       for (Usuario u : usuarios) { %>
                    <tr>
                        <td class="text-center"><%= u.getIdUsuario() %></td>
                        <td><%= u.getNombre() %></td>
                        <td><%= u.getCorreo() %></td>
                        <td class="text-center"><%= u.getRol() %></td>
                        <td class="text-center"><%= u.getEstado() %></td>
                        <td class="text-center">
                            <!-- ✅ Editar con POST enviando todos los datos -->
                            <form action="<%= request.getContextPath() %>/EditarUsuarioServlet" method="post" style="display:inline;">
                                <input type="hidden" name="id" value="<%= u.getIdUsuario() %>">
                                <input type="hidden" name="nombre" value="<%= u.getNombre() %>">
                                <input type="hidden" name="correo" value="<%= u.getCorreo() %>">
                                <input type="hidden" name="rol" value="<%= u.getRol() %>">
                                <input type="hidden" name="estado" value="<%= u.getEstado() %>">
                                <button type="submit" class="btn btn-sm btn-outline-primary">
                                    <i class="fas fa-edit"></i> Editar
                                </button>
                            </form>
                        </td>
                    </tr>
                <%   }
                   } %>
            </tbody>
        </table>

        <!-- 🔹 Docentes -->
        <h4 class="mb-3"><i class="fas fa-chalkboard-teacher"></i> Docentes</h4>
        <table class="table table-bordered table-hover">
            <thead class="table-success text-center">
                <tr>
                    <th>ID</th><th>Nombre</th><th>Correo</th><th>Instrumentos</th><th>Clases asignadas</th><th>Estado</th><th>Acciones</th>
                </tr>
            </thead>
            <tbody>
                <% if (docentes != null) {
                       for (Map<String,String> d : docentes) { %>
                    <tr>
                        <td class="text-center"><%= d.get("id") %></td>
                        <td><%= d.get("nombre") %></td>
                        <td><%= d.get("correo") %></td>
                        <td><%= d.get("instrumento") %></td>
                        <td class="text-center"><%= d.get("clases_asignadas") %></td>
                        <td class="text-center"><%= d.get("estado") %></td>
                        <td class="text-center">
                            <a href="<%= request.getContextPath() %>/VerDocentesServlet?idDocente=<%= d.get("id") %>" 
                               class="btn btn-sm btn-outline-info">
                               <i class="fas fa-eye"></i> Ver clases
                            </a>
                        </td>
                    </tr>
                <%   }
                   } %>
            </tbody>
        </table>

        <!-- 🔹 Estudiantes -->
        <h4 class="mb-3"><i class="fas fa-user-graduate"></i> Estudiantes</h4>
        <table class="table table-bordered table-hover">
            <thead class="table-success text-center">
                <tr>
                    <th>ID</th><th>Nombre</th><th>Correo</th><th>Instrumento</th><th>Etapa</th>
                    <th>Clases activas</th><th>Clases certificadas</th><th>Acciones</th>
                </tr>
            </thead>
            <tbody>
                <% if (estudiantes != null) {
                       for (Map<String,String> e : estudiantes) { %>
                    <tr>
                        <td class="text-center"><%= e.get("id") %></td>
                        <td><%= e.get("nombre") %></td>
                        <td><%= e.get("correo") %></td>
                        <td class="text-center"><%= e.get("instrumento") %></td>
                        <td class="text-center"><%= e.get("etapa") %></td>
                        <td class="text-center"><%= e.get("clases_activas") %></td>
                        <td class="text-center"><%= e.get("clases_certificadas") %></td>
                        <td class="text-center">
                            <a href="<%= request.getContextPath() %>/VerEstudiantesServlet?idEstudiante=<%= e.get("id") %>" 
                               class="btn btn-sm btn-outline-info">
                               <i class="fas fa-eye"></i> Ver clases
                            </a>
                        </td>
                    </tr>
                <%   }
                   } %>
            </tbody>
        </table>

        <!-- 🔙 Botón de regreso -->
        <div class="mt-4 text-end">
            <a href="<%= request.getContextPath() %>/PanelAdministradorServlet" 
               class="btn btn-outline-success">
                <i class="fas fa-arrow-left"></i> Volver al panel
            </a>
        </div>
    </div>
</div>

<jsp:include page="/fragmentos/footer.jsp" />
</body>
</html>