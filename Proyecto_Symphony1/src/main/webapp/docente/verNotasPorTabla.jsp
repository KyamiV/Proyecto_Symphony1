<%--  
    Document   : verNotasPorTabla
    Created on : 17/11/2025, 1:08:39 p. m.
    Author     : camiv
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" session="true" %>
<%@ page import="com.mysymphony.proyecto_symphony1.modelo.Nota" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>

<%
    String nombre = (String) session.getAttribute("nombreActivo");
    String rol = (String) session.getAttribute("rolActivo");

    if (nombre == null || rol == null || !"docente".equalsIgnoreCase(rol)) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }

    List<Nota> notas = (List<Nota>) request.getAttribute("notas");
    Map<String, String> datosClase = (Map<String, String>) request.getAttribute("datosClase");
    Map<String, String> estadoTabla = (Map<String, String>) request.getAttribute("estadoTabla");

    Integer claseId = (Integer) request.getAttribute("claseId");
    Integer tablaId = (Integer) request.getAttribute("tablaId");

    String nombreClase = datosClase != null ? datosClase.getOrDefault("nombre", "Sin nombre") : "Clase desconocida";
    String aula = datosClase != null ? datosClase.getOrDefault("aula", "Sin aula") : "Aula desconocida";
    String horario = datosClase != null ? datosClase.getOrDefault("dia", "Horario no disponible") : "Horario no disponible";

    String estadoEnvio = estadoTabla != null ? estadoTabla.getOrDefault("enviada", "No") : "Desconocido";
    String fechaEnvio = estadoTabla != null ? estadoTabla.getOrDefault("fecha_envio", "") : "";
%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Notas por tabla - SymphonySIAS</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/estilos.css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;600&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
    <style>
        body {
            font-family: 'Poppins', sans-serif;
            background-color: #f4f6f9;
            padding: 30px;
        }
        .dashboard-box {
            background: #ffffff;
            padding: 25px;
            border-radius: 10px;
            box-shadow: 0 0 10px rgba(0,0,0,0.08);
            max-width: 100%;
            margin: 0 auto;
        }
        .tabla-notas th, .tabla-notas td {
            vertical-align: middle;
            text-align: center;
        }
        .form-control-plaintext {
            border: none;
            background: transparent;
        }
    </style>
</head>
<body>

<jsp:include page="../fragmentos/header.jsp" />

<div class="dashboard-box">
    <h4 class="text-center mb-4"><i class="fas fa-table"></i> Notas registradas de la clase</h4>
    <p class="text-center mb-4">
        <strong>Clase:</strong> <%= nombreClase %> | 
        <strong>Aula:</strong> <%= aula %> | 
        <strong>Horario:</strong> <%= horario %> | 
        <strong>Estado de envío:</strong> <%= estadoEnvio %> 
        <% if (!fechaEnvio.isEmpty()) { %> | <strong>Fecha de envío:</strong> <%= fechaEnvio %> <% } %>
    </p>

    <% if (notas == null || notas.isEmpty()) { %>
        <div class="alert alert-info text-center" style="max-width: 700px; margin: 0 auto;">
            <p class="mb-1">No hay notas registradas aún.</p>
            <p class="mb-0">Una vez que se registre el avance de los estudiantes, aparecerá aquí.</p>
        </div>
    <% } else { %>
        <div class="table-responsive">
            <table class="table table-bordered table-striped tabla-notas">
                <thead class="table-dark">
                    <tr>
                        <th>Estudiante</th>
                        <th>Competencia</th>
                        <th>Nota</th>
                        <th>Observación</th>
                        <th>Acciones</th>
                    </tr>
                </thead>
                <tbody>
                    <% for (Nota nota : notas) { %>
                    <tr>
                        <form action="<%= request.getContextPath() %>/EditarNotaClaseServlet" method="post">
                            <input type="hidden" name="idNota" value="<%= nota.getId() %>"/>
                            <input type="hidden" name="idClase" value="<%= nota.getIdClase() %>"/>
                            <input type="hidden" name="idTabla" value="<%= nota.getIdTabla() %>"/>

                            <td><input type="text" value="<%= nota.getEstudiante() %>" readonly class="form-control-plaintext text-center" /></td>
                            <td><input type="text" name="competencia" value="<%= nota.getCompetencia() %>" class="form-control text-center" /></td>
                            <td><input type="number" name="nota" value="<%= nota.getNota() %>" step="0.1" min="1" max="5" class="form-control text-center" required /></td>
                            <td><input type="text" name="observacion" value="<%= nota.getObservacion() %>" class="form-control text-center" /></td>
                            <td>
                                <button type="submit" class="btn btn-sm btn-success">
                                    <i class="fas fa-save"></i> Guardar
                                </button>
                            </td>
                        </form>
                    </tr>
                    <% } %>
                </tbody>
            </table>
        </div>

        <!-- Botón para enviar toda la tabla al administrador -->
        <div class="text-center mt-4">
            <form action="<%= request.getContextPath() %>/EnviarNotasAdminServlet" method="post">
                <input type="hidden" name="idClase" value="<%= claseId %>"/>
                <input type="hidden" name="idTabla" value="<%= tablaId %>"/>
                <button type="submit" class="btn btn-primary">
                    <i class="fas fa-paper-plane"></i> Enviar notas al administrador
                </button>
            </form>
        </div>
    <% } %>

    <div class="mt-4 text-start">
        <a href="<%= request.getContextPath() %>/PanelDocenteServlet" class="btn btn-outline-primary">
            <i class="fas fa-arrow-left"></i> Volver al panel docente
        </a>
    </div>
</div>

<jsp:include page="../fragmentos/footer.jsp" />

</body>
</html>