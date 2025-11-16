<%-- 
    Document   : editar
    Created on : 14/11/2025, 10:31:13 p. m.
    Author     : camiv
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" session="true" %>
<%
    String nombre = (String) session.getAttribute("nombreActivo");
    String rol = (String) session.getAttribute("rolActivo");

    if (nombre == null || rol == null || !"docente".equalsIgnoreCase(rol)) {
        response.sendRedirect("../login.jsp");
        return;
    }

    String estudiante = request.getParameter("estudiante");
%>
<!DOCTYPE html>
<html>
<head>
    <title>Editar nota</title>
    <link rel="stylesheet" href="../assets/css/estilos.css">
</head>
<body>

<jsp:include page="../fragmentos/header.jsp" />

<h2>Editar nota de <%= estudiante %></h2>

<form action="<%= request.getContextPath() %>/ActualizarNotaServlet" method="post">
    <input type="hidden" name="estudiante" value="<%= estudiante %>">

    <label for="instrumento">Instrumento:</label>
    <select name="instrumento" required>
        <option value="">Seleccione un instrumento</option>
        <option value="Violín">Violín</option>
        <option value="Piano">Piano</option>
    </select>

    <label for="etapa">Etapa pedagógica:</label>
    <select name="etapa" required>
        <option value="">Seleccione una etapa</option>
        <option value="Exploración sonora">Exploración sonora</option>
        <option value="Iniciación técnica">Iniciación técnica</option>
        <option value="Repertorio básico">Repertorio básico</option>
        <option value="Repertorio intermedio">Repertorio intermedio</option>
        <option value="Repertorio avanzado">Repertorio avanzado</option>
    </select>

    <label for="nota">Nueva nota:</label>
    <input type="number" step="0.1" min="0" max="5" name="nota" required>

    <label for="observaciones">Observaciones:</label>
    <textarea name="observaciones" rows="3"></textarea>

    <div style="text-align:center; margin-top:20px;">
        <button type="submit" class="btn-enviar">Actualizar</button>
        <a href="verNotasDocente.jsp" class="btn-volver">Cancelar</a>
    </div>
</form>

<jsp:include page="../fragmentos/footer.jsp" />

</body>
</html>