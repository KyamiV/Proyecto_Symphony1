<%--
    Document   : configuracion.jsp
    Author     : Camila
    Descripción: Vista institucional para mostrar y gestionar instrumentos, etapas, roles y parámetros técnicos.
                 Recibe atributos desde ConfiguracionServlet: instrumentos, etapas, rolesActivos, parametrosTecnicos.
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" session="true" %>
<%@ page import="java.util.*, java.util.Map" %>

<%
    String nombre = (String) session.getAttribute("nombreActivo");
    String rol = (String) session.getAttribute("rolActivo");

    if (nombre == null || rol == null || !"administrador".equalsIgnoreCase(rol)) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }

    List<com.mysymphony.proyecto_symphony1.modelo.Instrumento> instrumentos =
        (List<com.mysymphony.proyecto_symphony1.modelo.Instrumento>) request.getAttribute("instrumentos");
    List<String> etapas = (List<String>) request.getAttribute("etapas");
    Map<String, String> rolesActivos = (Map<String, String>) request.getAttribute("rolesActivos"); // 👈 corregido
    Map<String, String> parametrosTecnicos = (Map<String, String>) request.getAttribute("parametrosTecnicos");
    String error = (String) request.getAttribute("error");
    String mensaje = (String) request.getAttribute("mensaje");
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Configuración institucional | SymphonySIAS</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/estilos.css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
    <style>
        .config-box { background-color: #fff; padding: 30px; border-radius: 12px; box-shadow: 0 4px 12px rgba(0,0,0,0.06); }
        .config-section { margin-bottom: 40px; }
        .config-section h4 { margin-bottom: 25px; border-bottom: 2px solid #dee2e6; padding-bottom: 10px; }
        .list-group-item input { max-width: 200px; }
    </style>
</head>
<body class="bg-light">

<jsp:include page="../fragmentos/header.jsp" />
<jsp:include page="../fragmentos/sidebar.jsp" />

<div class="container mt-5 config-box">
    <h3 class="text-center mb-4"><i class="fas fa-cogs"></i> Configuración institucional</h3>

    <% if (mensaje != null && !mensaje.isEmpty()) { %>
        <div class="alert alert-success text-center"><%= mensaje %></div>
    <% } %>
    <% if (error != null) { %>
        <div class="alert alert-danger text-center"><%= error %></div>
    <% } %>

    <!-- Sección: Registro de elementos -->
    <div class="config-section">
        <h4><i class="fas fa-plus-circle"></i> Agregar elementos institucionales</h4>

        <!-- Agregar instrumento -->
        <form action="<%= request.getContextPath() %>/ConfiguracionServlet" method="post" class="mb-3">
            <div class="row g-3 align-items-end">
                <div class="col-md-5">
                    <label class="form-label">Nuevo instrumento</label>
                    <input type="text" name="nombreInstrumento" class="form-control" placeholder="Ej: Violín" required>
                </div>
                <div class="col-md-3">
                    <label class="form-label">Cupo máximo</label>
                    <input type="number" name="cupoInstrumento" class="form-control" min="1" value="5" required>
                </div>
                <div class="col-md-4">
                    <button type="submit" name="accion" value="agregarInstrumento" class="btn btn-primary w-100">
                        <i class="fas fa-plus"></i> Agregar instrumento
                    </button>
                </div>
            </div>
        </form>

        <!-- Agregar etapa -->
        <form action="<%= request.getContextPath() %>/ConfiguracionServlet" method="post" class="mb-3">
            <div class="row g-3 align-items-end">
                <div class="col-md-8">
                    <label class="form-label">Nueva etapa pedagógica</label>
                    <input type="text" name="nuevaEtapa" class="form-control" placeholder="Ej: Básica, Intermedia, Avanzada" required>
                </div>
                <div class="col-md-4">
                    <button type="submit" name="accion" value="agregarEtapa" class="btn btn-primary w-100">
                        <i class="fas fa-plus"></i> Agregar etapa
                    </button>
                </div>
            </div>
        </form>

        <!-- Asignar color a rol -->
        <form action="<%= request.getContextPath() %>/ConfiguracionServlet" method="post" class="mb-3">
            <div class="row g-3 align-items-end">
                <div class="col-md-6">
                    <label class="form-label">Rol</label>
                    <select name="rolColor" class="form-select" required>
                        <option value="administrador">Administrador</option>
                        <option value="docente">Docente</option>
                        <option value="estudiante">Estudiante</option>
                    </select>
                </div>
                <div class="col-md-4">
                    <label class="form-label">Color institucional</label>
                    <input type="color" name="colorRol" class="form-control form-control-color" value="#198754" required>
                </div>
                <div class="col-md-2">
                    <button type="submit" name="accion" value="asignarColor" class="btn btn-primary w-100">
                        <i class="fas fa-palette"></i> Asignar
                    </button>
                </div>
            </div>
        </form>
    </div>

    <!-- Sección: Edición de configuración -->
    <div class="config-section">
        <h4><i class="fas fa-tools"></i> Gestión de configuración institucional</h4>
        <div class="row">
           
            <!-- Instrumentos -->
            <div class="col-md-6">
                <h5><i class="fas fa-music"></i> Instrumentos registrados</h5>
                <% if (instrumentos != null && !instrumentos.isEmpty()) { %>
                    <table class="table table-bordered table-hover">
                        <thead class="table-primary text-center">
                            <tr>
                                <th>Instrumento</th>
                                <th>Cupo máximo</th>
                                <th>Acciones</th>
                            </tr>
                        </thead>
                        <tbody>
                            <% for (com.mysymphony.proyecto_symphony1.modelo.Instrumento inst : instrumentos) { %>
                                <tr>
                                    <td><%= inst.getNombre() %></td>
                                    <td>
                                       <!-- Formulario para editar cupo -->
                                        <form action="<%= request.getContextPath() %>/ConfiguracionServlet" method="post" class="d-flex">
                                            <input type="number" name="nuevoCupo" value="<%= inst.getCupo() %>" min="1" 
                                                   class="form-control form-control-sm me-2" required>
                                            <input type="hidden" name="nombreInstrumento" value="<%= inst.getNombre() %>">
                                            <button type="submit" name="accion" value="editarInstrumento" 
                                                    class="btn btn-success btn-sm p-1 px-2">
                                                <i class="fas fa-save"></i>
                                            </button>
                                        </form>
                                        </td>
                                        <td class="text-center">
                                            <!-- Formulario para eliminar instrumento -->
                                            <form action="<%= request.getContextPath() %>/ConfiguracionServlet" method="post" style="display:inline;">
                                                <input type="hidden" name="nombreInstrumento" value="<%= inst.getNombre() %>">
                                                <button type="submit" name="accion" value="eliminarInstrumento" 
                                                        class="btn btn-danger btn-sm p-1 px-2"
                                                        onclick="return confirm('¿Eliminar instrumento <%= inst.getNombre() %>?');">
                                                    <i class="fas fa-trash-alt"></i>
                                                </button>
                                            </form>
                                        </td>
                                    </td>
                                </tr>
                            <% } %>
                        </tbody>
                    </table>
                <% } else { %>
                    <div class="alert alert-warning">No hay instrumentos registrados.</div>
                <% } %>
            </div>

            <!-- Etapas pedagógicas -->
            <div class="col-md-6">
                <h5><i class="fas fa-layer-group"></i> Etapas pedagógicas</h5>
                <% if (etapas != null && !etapas.isEmpty()) { %>
                    <table class="table table-bordered table-hover">
                        <thead class="table-success text-center">
                            <tr>
                                <th>Nombre de etapa</th>
                                <th>Acciones</th>
                            </tr>
                        </thead>
                        <tbody>
                            <% for (String etapa : etapas) { %>
                                <tr>
                                    <!-- Formulario de edición -->
                                    <td>
                                        <form action="<%= request.getContextPath() %>/ConfiguracionServlet" method="post" class="d-flex">
                                            <input type="text" name="nuevaEtapaNombre" value="<%= etapa %>" 
                                                   class="form-control form-control-sm me-2" required>
                                            <input type="hidden" name="etapaOriginal" value="<%= etapa %>">
                                            <button type="submit" name="accion" value="editarEtapa" 
                                                    class="btn btn-success btn-sm p-1 px-2">
                                                <i class="fas fa-save"></i>
                                            </button>
                                        </form>
                                    </td>
                                    <!-- Formulario de eliminación -->
                                    <td class="text-center">
                                        <form action="<%= request.getContextPath() %>/ConfiguracionServlet" method="post" style="display:inline;">
                                            <input type="hidden" name="etapaEliminar" value="<%= etapa %>">
                                            <button type="submit" name="accion" value="eliminarEtapa" 
                                                    class="btn btn-danger btn-sm p-1 px-2"
                                                    onclick="return confirm('¿Eliminar etapa <%= etapa %>?');">
                                                <i class="fas fa-trash-alt"></i>
                                            </button>
                                        </form>
                                    </td>
                                </tr>
                            <% } %>
                        </tbody>
                    </table>
                <% } else { %>
                    <div class="alert alert-warning">No hay etapas pedagógicas registradas.</div>
                <% } %>
            </div>

            <!-- Roles activos -->
            <div class="col-md-12 mt-4">
                <h5><i class="fas fa-users"></i> Roles activos</h5>
                <% if (rolesActivos != null && !rolesActivos.isEmpty()) { %>
                    <table class="table table-bordered">
                        <thead>
                            <tr>
                                <th>Rol</th>
                                <th>Color</th>
                            </tr>
                        </thead>
                        <tbody>
                            <% for (Map.Entry<String, String> entry : rolesActivos.entrySet()) { %>
                                <tr>
                                                                        <td><%= entry.getKey() %></td>
                                    <td>
                                        <span style="color:<%= entry.getValue() %>">
                                            <%= entry.getValue() %>
                                        </span>
                                    </td>
                                </tr>
                            <% } %>
                        </tbody>
                    </table>
                <% } else { %>
                    <div class="alert alert-warning">No hay roles registrados.</div>
                <% } %>
            </div>

            <!-- Parámetros técnicos -->
            <div class="col-md-12 mt-4">
                <h5><i class="fas fa-sliders-h"></i> Parámetros técnicos del sistema</h5>
                <% if (parametrosTecnicos != null && !parametrosTecnicos.isEmpty()) { %>
                    <form action="<%= request.getContextPath() %>/ConfiguracionServlet" method="post">
                        <ul class="list-group mb-4">
                            <% for (Map.Entry<String, String> entry : parametrosTecnicos.entrySet()) { %>
                                <li class="list-group-item d-flex justify-content-between align-items-center">
                                    <span><i class="fas fa-wrench"></i> <strong><%= entry.getKey() %></strong></span>
                                    <input type="text" name="parametro_<%= entry.getKey() %>"
                                           value="<%= entry.getValue() %>" class="form-control" required>
                                </li>
                            <% } %>
                        </ul>
                        <div class="text-end mt-3">
                            <button type="submit" name="accion" value="actualizarParametros" class="btn btn-success">
                                <i class="fas fa-save"></i> Guardar cambios
                            </button>
                            <a href="<%= request.getContextPath() %>/PanelAdministradorServlet" class="btn btn-secondary ms-2">
                                <i class="fas fa-arrow-left"></i> Volver al panel
                            </a>
                        </div>
                    </form>
                <% } else { %>
                    <div class="alert alert-warning">No hay parámetros técnicos registrados.</div>
                <% } %>
            </div>
        </div>
    </div>
</div>

<jsp:include page="../fragmentos/footer.jsp" />
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>

</body>
</html>



    