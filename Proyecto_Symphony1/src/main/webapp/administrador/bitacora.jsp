<%-- 
    Document   : bitacora
    Created on : 20/11/2025, 12:29:13 a. m.
    Author     : camiv
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" session="true" %>
<%@ page import="java.util.*, com.mysymphony.proyecto_symphony1.dao.BitacoraDAO" %>

<%
    HttpSession sesion = request.getSession(false);
    String rol = (sesion != null) ? (String) sesion.getAttribute("rolActivo") : null;
    String usuario = (sesion != null) ? (String) sesion.getAttribute("nombreActivo") : null;

    if (rol == null || !(rol.equalsIgnoreCase("administrador") || rol.equalsIgnoreCase("coordinador"))) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }

    BitacoraDAO dao = new BitacoraDAO();
    List<Map<String, String>> registros = dao.obtenerTodos();

    String filtro = request.getParameter("filtro");
    if (filtro != null && !filtro.trim().isEmpty()) {
        final String filtroFinal = filtro.trim().toLowerCase();
        registros.removeIf(fila ->
            !(fila.get("usuario").toLowerCase().contains(filtroFinal) ||
              fila.get("rol").toLowerCase().contains(filtroFinal) ||
              fila.get("modulo").toLowerCase().contains(filtroFinal) ||
              fila.get("accion").toLowerCase().contains(filtroFinal))
        );
    }
%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Bitácora institucional - SymphonySIAS</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/estilos.css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
    <style>
        /* 🎨 Scroll para tabla de bitácora */
        .scroll-bitacora {
            max-height: 400px; /* altura fija */
            overflow-y: auto;  /* activa scroll vertical */
        }
    </style>
</head>
<body>

<jsp:include page="../fragmentos/header.jsp" />
<jsp:include page="../fragmentos/sidebar.jsp" />

<form method="get" class="mb-4 text-center">
    <div class="input-group" style="max-width: 500px; margin: 0 auto;">
        <input type="text" name="filtro" class="form-control" placeholder="Buscar en la bitácora..."
               value="<%= request.getParameter("filtro") != null ? request.getParameter("filtro") : "" %>">
        <button type="submit" class="btn btn-primary"><i class="fas fa-search"></i> Filtrar</button>
    </div>
</form>

<div class="tabla-box">
    <h4 class="mb-4 text-center"><i class="fas fa-clipboard-list"></i> Bitácora institucional</h4>

    <!-- 📑 Tabla con scroll -->
    <div class="table-responsive scroll-bitacora">
        <table class="table table-bordered table-hover">
            <thead class="table-light text-center">
                <tr>
                    <th>Usuario</th>
                    <th>Rol</th>
                    <th>Módulo</th>
                    <th>Acción registrada</th>
                    <th>Fecha</th>
                </tr>
            </thead>
            <tbody>
            <% if (registros != null && !registros.isEmpty()) {
                   for (Map<String, String> fila : registros) { %>
                <tr>
                    <td><%= fila.get("usuario") %></td>
                    <td><%= fila.get("rol") %></td>
                    <td><%= fila.get("modulo") %></td>
                    <td><%= fila.get("accion") %></td>
                    <td><%= fila.get("fecha") %></td>
                </tr>
            <%     }
               } else { %>
                <tr>
                    <td colspan="5" class="text-center">No hay registros en la bitácora.</td>
                </tr>
            <% } %>
            </tbody>
        </table>
    </div>
</div>

<div class="text-end mt-4">
    <!-- Botón registrar acción -->
    <form action="registrarAccion.jsp" method="get" style="display:inline;">
        <button type="submit" class="btn btn-outline-primary">
            <i class="fas fa-pen-nib"></i> Registrar acción institucional
        </button>
    </form>

    <!-- Botón limpiar bitácora con confirmación -->
    <form action="<%= request.getContextPath() %>/LimpiarBitacoraServlet" method="post" style="display:inline;">
        <button type="submit" class="btn btn-outline-danger ms-2"
                onclick="return confirm('¿Estás seguro de limpiar la bitácora?');">
            <i class="fas fa-trash"></i> Limpiar bitácora
        </button>
    </form>
</div>

<jsp:include page="../fragmentos/footer.jsp" />
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>

</body>
</html>