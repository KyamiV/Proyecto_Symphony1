<%-- 
    Document   : verNotasDocente
    Created on : 14/11/2025, 10:30:00 p. m.
    Author     : camiv
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" session="true" %>
<%@ page import="java.util.*" %>

<%
    String rol = (String) session.getAttribute("rolActivo");
    if (rol == null || !"docente".equalsIgnoreCase(rol)) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }

    // Datos de ejemplo (pueden venir del servlet con request.setAttribute)
    List<Map<String,String>> notas = new ArrayList<>();
    Map<String,String> fila1 = new HashMap<>();
    fila1.put("id_clase","1");
    fila1.put("estudiante","Manuelita ApellidoReal");
    fila1.put("competencia","Principal");
    fila1.put("nota","4,5");
    fila1.put("observacion","Excelente");
    fila1.put("fecha","2025-11-29");
    notas.add(fila1);

    Map<String,String> fila2 = new HashMap<>();
    fila2.put("id_clase","1");
    fila2.put("estudiante","Manuelita ApellidoReal");
    fila2.put("competencia","Técnica");
    fila2.put("nota","3,0");
    fila2.put("observacion","Falta armonía");
    fila2.put("fecha","2025-11-28");
    notas.add(fila2);

    // Parámetros de tabla/clase para enviar al admin (ejemplo)
    Integer tablaId = 1;
    Integer claseId = 1;
%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Notas por clase - SymphonySIAS</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/estilos.css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;600&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
    <style>
        body { font-family: 'Poppins', sans-serif; background-color: #f4f6f9; padding: 30px; }
        .dashboard-box { background: #ffffff; padding: 25px; border-radius: 10px; box-shadow: 0 0 10px rgba(0,0,0,0.08); max-width: 100%; margin: 0 auto; }
        .tabla-notas th, .tabla-notas td { vertical-align: middle; text-align: center; }
    </style>
</head>
<body>

<jsp:include page="../fragmentos/header.jsp" />

<div class="dashboard-box">
    <h4 class="text-center mb-4"><i class="fas fa-table"></i> Notas por clase</h4>

    <div class="table-responsive">
        <table class="table table-bordered table-striped tabla-notas">
            <thead class="table-dark">
                <tr>
                    <th>ID Clase</th>
                    <th>Estudiante</th>
                    <th>Competencia</th>
                    <th>Nota</th>
                    <th>Observación</th>
                    <th>Fecha</th>
                    <th>Acciones</th>
                </tr>
            </thead>
            <tbody>
                <% for (Map<String,String> fila : notas) { %>
                <tr>
                    <td><%= fila.get("id_clase") %></td>
                    <td><%= fila.get("estudiante") %></td>
                    <td><%= fila.get("competencia") %></td>
                    <td><%= fila.get("nota") %></td>
                    <td><%= fila.get("observacion") %></td>
                    <td><%= fila.get("fecha") %></td>
                    <td>
                        <form action="<%= request.getContextPath() %>/ActualizarNotaServlet" method="post" style="display:inline;">
                            <input type="hidden" name="id_clase" value="<%= fila.get("id_clase") %>"/>
                            <input type="hidden" name="estudiante" value="<%= fila.get("estudiante") %>"/>
                            <input type="hidden" name="competencia" value="<%= fila.get("competencia") %>"/>
                            <button type="submit" class="btn btn-sm btn-success">
                                <i class="fas fa-save"></i> Guardar
                            </button>
                        </form>
                    </td>
                </tr>
                <% } %>
            </tbody>
        </table>
    </div>

    <!-- Botón para enviar todas las notas al administrador -->
    <div class="text-center mt-4">
        <form action="<%= request.getContextPath() %>/EnviarNotasAdminServlet" method="post">
            <input type="hidden" name="tablaId" value="<%= tablaId != null ? tablaId : "" %>"/>
            <input type="hidden" name="claseId" value="<%= claseId != null ? claseId : "" %>"/>
            <button type="submit" class="btn btn-sm btn-primary">
                <i class="fas fa-paper-plane"></i> Enviar a administrador
            </button>
        </form>
    </div>

    <div class="mt-4 text-start">
        <a href="<%= request.getContextPath() %>/PanelDocenteServlet" class="btn btn-outline-primary">
            <i class="fas fa-arrow-left"></i> Volver al panel docente
        </a>
    </div>
</div>

<jsp:include page="../fragmentos/footer.jsp" />

</body>
</html>