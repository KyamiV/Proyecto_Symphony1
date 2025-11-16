<%-- 
    Document   : footer
    Created on : 14/11/2025, 10:12:31 p. m.
    Author     : camiv
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    java.util.Calendar cal = java.util.Calendar.getInstance();
    int anio = cal.get(java.util.Calendar.YEAR);
%>
<footer class="text-center mt-5 text-muted">
    &copy; <%= anio %> SymphonySIAS - Sistema de Información Académico Musical
</footer>