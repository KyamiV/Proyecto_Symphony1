<%-- 
    Document   : gestionarHorarios
    Created on : 16/11/2025, 10:46:39 a. m.
    Author     : camiv
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" session="true" %>
<%@ page import="java.util.List, java.util.Map" %>

<%
    // 🔐 Validación de sesión y rol
    String nombre = (String) session.getAttribute("nombreActivo");
    String rol = (String) session.getAttribute("rolActivo");

    if (nombre == null || rol == null || !"administrador".equalsIgnoreCase(rol)) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }

    // 📥 Datos recibidos desde el servlet
    List<Map<String, String>> clases = (List<Map<String, String>>) request.getAttribute("clases");
    List<Map<String, String>> horarios = (List<Map<String, String>>) request.getAttribute("horarios");
    String claseSeleccionada = (String) request.getAttribute("claseSeleccionada");
    String error = (String) request.getAttribute("error");
%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Horarios de clases | SymphonySIAS</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/estilos.css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
</head>
<body>

<jsp:include page="../fragmentos/header.jsp" />

<div class="container mt-5">
    <h3 class="text-center mb-4"><i class="fas fa-calendar-alt"></i> Gestión de horarios de clases</h3>

    <%-- 🧾 Mensajes institucionales desde sesión --%>
    <%
        String mensaje = (String) session.getAttribute("mensaje");
        String tipoMensaje = (String) session.getAttribute("tipoMensaje");
        if (mensaje != null) {
    %>
        <div class="alert alert-<%= tipoMensaje %> text-center">
            <%= mensaje %>
        </div>
    <%
            session.removeAttribute("mensaje");
            session.removeAttribute("tipoMensaje");
        }
    %>

    <%-- 🧾 Mensaje de error si existe en request --%>
    <% if (error != null) { %>
        <div class="alert alert-danger text-center"><%= error %></div>
    <% } %>

    <%-- 🔍 Selector de clase --%>
    <form method="get" action="<%= request.getContextPath() %>/GestionarHorariosServlet" class="mb-4">
        <label class="form-label">Seleccionar clase</label>
        <select name="id_clase" class="form-select" onchange="this.form.submit()">
            <option value="">-- Selecciona una clase --</option>
            <% for (Map<String, String> c : clases) {
                String idClase = c.get("id_clase");
                String nombreClase = c.get("nombre");
            %>
                <option value="<%= idClase %>" <%= idClase.equals(claseSeleccionada) ? "selected" : "" %>>
                    <%= nombreClase %>
                </option>
            <% } %>
        </select>
    </form>

    <%-- 🧩 Formulario de registro de horario por clase --%>
    <% if (claseSeleccionada != null && !claseSeleccionada.isEmpty()) { %>
        <form method="post" action="<%= request.getContextPath() %>/GestionarHorariosServlet">
            <input type="hidden" name="id_clase" value="<%= claseSeleccionada %>">
            <div class="row g-3">
                <div class="col-md-2">
                    <label class="form-label">Día</label>
                    <select name="dia" class="form-select" required>
                        <option>Lunes</option>
                        <option>Martes</option>
                        <option>Miércoles</option>
                        <option>Jueves</option>
                        <option>Viernes</option>
                        <option>Sábado</option>
                    </select>
                </div>
                <div class="col-md-3">
                    <label class="form-label">Fecha exacta</label>
                    <input type="date" name="fecha" class="form-control" required>
                </div>
                <div class="col-md-2">
                    <label class="form-label">Hora inicio</label>
                    <input type="time" name="hora_inicio" class="form-control" required>
                </div>
                <div class="col-md-2">
                    <label class="form-label">Hora fin</label>
                    <input type="time" name="hora_fin" class="form-control" required>
                </div>
                <div class="col-md-3">
                    <label class="form-label">Aula (opcional)</label>
                    <input type="text" name="aula" class="form-control">
                </div>
            </div>
            <div class="text-end mt-3">
                <button type="submit" class="btn btn-primary">
                    <i class="fas fa-plus-circle"></i> Agregar horario
                </button>
            </div>
        </form>

        <%-- 📅 Tabla de horarios registrados con edición en línea --%>
        <% if (horarios != null && !horarios.isEmpty()) { %>
            <div class="table-responsive mt-4">
                <table class="table table-bordered text-center align-middle">
                    <thead class="table-dark">
                        <tr>
                            <th>Día</th>
                            <th>Fecha</th>
                            <th>Hora inicio</th>
                            <th>Hora fin</th>
                            <th>Aula</th>
                            <th>Acción</th>
                        </tr>
                    </thead>
                    <tbody>
                        <% for (Map<String, String> h : horarios) { %>
                            <tr>
                                <form method="post" action="<%= request.getContextPath() %>/GestionarHorariosServlet">
                                    <input type="hidden" name="id_horario" value="<%= h.get("id_horario") %>">
                                    <input type="hidden" name="id_clase" value="<%= claseSeleccionada %>">

                                    <td>
                                        <select name="dia" class="form-select form-select-sm">
                                            <option <%= "Lunes".equals(h.get("dia")) ? "selected" : "" %>>Lunes</option>
                                            <option <%= "Martes".equals(h.get("dia")) ? "selected" : "" %>>Martes</option>
                                            <option <%= "Miércoles".equals(h.get("dia")) ? "selected" : "" %>>Miércoles</option>
                                            <option <%= "Jueves".equals(h.get("dia")) ? "selected" : "" %>>Jueves</option>
                                            <option <%= "Viernes".equals(h.get("dia")) ? "selected" : "" %>>Viernes</option>
                                            <option <%= "Sábado".equals(h.get("dia")) ? "selected" : "" %>>Sábado</option>
                                        </select>
                                    </td>
                                    <td><input type="date" name="fecha" class="form-control form-control-sm" value="<%= h.get("fecha") %>"></td>
                                    <td><input type="time" name="hora_inicio" class="form-control form-control-sm" value="<%= h.get("inicio") %>"></td>
                                    <td><input type="time" name="hora_fin" class="form-control form-control-sm" value="<%= h.get("fin") %>"></td>
                                    <td><input type="text" name="aula" class="form-control form-control-sm" value="<%= h.get("aula") %>"></td>
                                    <td>
                                        <button type="submit" name="accion" value="actualizar" class="btn btn-success btn-sm">
                                            <i class="fas fa-save"></i> Guardar
                                        </button>
                                        <button type="submit" name="accion" value="eliminar" class="btn btn-danger btn-sm">
                                            <i class="fas fa-trash-alt"></i> Eliminar
                                        </button>
                                    </td>
                                </form>
                            </tr>
                        <% } %>
                    </tbody>
                </table>
            </div>
        <% } else { %>
            <div class="alert alert-warning text-center mt-4">No hay horarios registrados para esta clase.</div>
        <% } %>
    <% } %>

    <%-- 🔙 Botón volver al panel --%>
    <div class="text-end mt-4">
        <a href="<%= request.getContextPath() %>/panelAdministrador.jsp" class="btn btn-secondary">
            <i class="fas fa-arrow-left"></i> Volver al panel
        </a>
    </div>
</div>

<jsp:include page="../fragmentos/footer.jsp" />

</body>
</html>