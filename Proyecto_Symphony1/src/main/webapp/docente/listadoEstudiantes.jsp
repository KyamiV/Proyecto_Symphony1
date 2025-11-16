<%-- 
    Document   : listadoEstudiantes
    Created on : 14/11/2025, 8:26:25 p. m.
    Author     : camiv
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" session="true" %>
<%@ page import="java.util.*, java.text.SimpleDateFormat" %>
<%
    String nombre = (String) session.getAttribute("nombreActivo");
    String rol = (String) session.getAttribute("rolActivo");

    if (nombre == null || rol == null || !"docente".equalsIgnoreCase(rol)) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }

    List<Map<String, String>> estudiantes = new ArrayList<>();

    // Simulación de estudiantes asignados
    Map<String, String> e1 = new HashMap<>();
    e1.put("nombre", "Laura Gómez");
    e1.put("instrumento", "Violín");
    e1.put("nivel", "Básico");
    e1.put("etapa", "Iniciación técnica");
    estudiantes.add(e1);

    Map<String, String> e2 = new HashMap<>();
    e2.put("nombre", "Mateo Ruiz");
    e2.put("instrumento", "Piano");
    e2.put("nivel", "Intermedio");
    e2.put("etapa", "Repertorio básico");
    estudiantes.add(e2);
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Estudiantes asignados - SymphonySIAS</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/estilos.css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;600&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
</head>
<body>

<jsp:include page="../fragmentos/header.jsp" />

<div class="dashboard-box">
    <h4 class="text-center mb-4"><i class="fas fa-users"></i> Estudiantes asignados</h4>

    <% if (estudiantes == null || estudiantes.isEmpty()) { %>
        <div class="alert alert-warning text-center" style="max-width: 700px; margin: 0 auto;">
            <p class="mb-1">No tienes estudiantes asignados actualmente.</p>
            <p class="mb-0">Una vez que se asignen estudiantes a tu rol, aparecerán aquí.</p>
        </div>
    <% } else { %>
        <table class="tabla-notas">
            <thead>
                <tr>
                    <th>Nombre</th>
                    <th>Instrumento</th>
                    <th>Nivel técnico</th>
                    <th>Etapa pedagógica</th>
                </tr>
            </thead>
            <tbody>
                <% for (Map<String, String> est : estudiantes) { %>
                    <tr>
                        <td><%= est.get("nombre") %></td>
                        <td><%= est.get("instrumento") %></td>
                        <td><%= est.get("nivel") %></td>
                        <td><%= est.get("etapa") %></td>
                    </tr>
                <% } %>
            </tbody>
        </table>
    <% } %>

    <div class="mt-4 text-start">
        <a href="<%= request.getContextPath() %>/dashboard.jsp" class="btn-volver">
            <i class="fas fa-arrow-left"></i> Volver al panel
        </a>
    </div>
</div>

<jsp:include page="../fragmentos/footer.jsp" />

</body>
</html>