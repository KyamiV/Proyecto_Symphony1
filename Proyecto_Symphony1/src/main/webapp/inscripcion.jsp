<%-- 
    Document   : inscripcion
    Created on : 13/11/2025, 7:12:16 p. m.
    Author     : camiv
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
    <title>Registro de Inscripción</title></head>
<body>
    <h2>Registrar inscripción</h2>
    <form action="InscripcionServlet" method="post">
        <input type="text" name="estudiante" placeholder="Correo del estudiante"><br>
        <input type="text" name="programa" placeholder="Programa académico"><br>
        <input type="date" name="fecha"><br>
        <button type="submit">Registrar inscripción</button>
    </form>

    <c:if test="${not empty mensaje}">
        <p style="color:green">${mensaje}</p>
    </c:if>
</body>
</html>