<%-- 
    Document   : clasesAsignadas
    Created on : 19/11/2025, 6:23:17 p. m.
    Author     : camiv
    Propósito  : Mostrar tabla institucional con clases que ya tienen docente asignado
    Trazabilidad: Solo se muestran clases con id_docente asignado, incluyendo cantidad de inscritos
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.*, java.util.Map" %>

<html>
<head>
    <title>Clases Asignadas</title>

    <!-- Estilos institucionales -->
    <link rel="stylesheet" href="<%=request.getContextPath()%>/css/estilos.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
</head>
<body>
    <div class="contenedor">
        <h2 class="titulo">📚 Clases Asignadas a Docentes</h2>

        <%-- Recuperar atributos enviados desde el servlet --%>
        <%
            List<Map<String, String>> clasesConInscritos = (List<Map<String, String>>) request.getAttribute("clasesConInscritos");
            String error = (String) request.getAttribute("error");
        %>

        <%-- Mostrar mensaje de error si existe --%>
        <% if (error != null) { %>
            <div class="alerta-error"><%= error %></div>

        <%-- Mostrar aviso si no hay clases asignadas --%>
        <% } else if (clasesConInscritos == null || clasesConInscritos.isEmpty()) { %>
            <div class="alerta-aviso">⚠️ No hay clases asignadas aún.</div>

        <%-- Mostrar tabla si hay clases asignadas --%>
        <% } else { %>
            <table class="tabla-institucional">
                <thead>
                    <tr>
                        <th>Clase</th>
                        <th>Instrumento</th>
                        <th>Etapa</th>
                        <th>Grupo</th>
                        <th>Inscritos</th>
                        <th>Docente Asignado</th>
                        <th>Estado</th>
                    </tr>
                </thead>
                <tbody>
                    <%-- Recorrer cada clase asignada y mostrar sus datos --%>
                    <% for (Map<String, String> clase : clasesConInscritos) { %>
                        <tr>
                            <td><%= clase.get("nombre_clase") %></td>
                            <td><%= clase.get("instrumento") %></td>
                            <td><%= clase.get("etapa") %></td>
                            <td><%= clase.get("grupo") %></td>
                            <td><%= clase.get("inscritos") %></td>
                            <td><%= clase.get("nombre_docente") %></td>
                            <td class="text-success text-center">
                                <i class="fas fa-check-circle"></i> Asignada
                            </td>
                        </tr>
                    <% } %>
                </tbody>
            </table>
        <% } %>
    </div>
</body>
</html>