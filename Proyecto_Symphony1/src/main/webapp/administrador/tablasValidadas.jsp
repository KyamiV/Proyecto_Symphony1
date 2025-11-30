<%-- 
    Document   : tablasValidadas
    Created on : 17/11/2025, 2:00:10 p. m.
    Author     : camiv
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" session="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%
    // 🔐 Validación de sesión y rol
    String rol = (String) session.getAttribute("rolActivo");
    if (rol == null || !"administrador".equalsIgnoreCase(rol)) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }

    String mensaje = (String) request.getAttribute("mensaje");
%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Tablas Validadas - SymphonySIAS</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/estilos.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
    <style>
        body { font-family: 'Poppins', sans-serif; background-color: #f4f6f9; }
        .titulo-panel { background-color: #198754; color: white; padding: 20px; border-radius: 0 0 20px 20px;
                        box-shadow: 0 4px 12px rgba(0,0,0,0.1); text-align: center; }
        .scroll-table { max-height: 450px; overflow-y: auto; }
        .scroll-table thead th { position: sticky; top: 0; background: #e9ecef; }
    </style>
</head>
<body class="bg-light">

    <!-- 🔹 Encabezado institucional -->
    <jsp:include page="../fragmentos/header.jsp" />

    <!-- 🔹 Menú lateral institucional -->
    <jsp:include page="../fragmentos/sidebar.jsp" />

    <main class="container mt-4">
        <div class="titulo-panel mb-4">
            <h3><i class="fas fa-check-circle"></i> Tablas institucionales validadas</h3>
        </div>

        <%-- 🧾 Mensaje si existe --%>
        <c:if test="${not empty mensaje}">
            <div class="alert alert-info text-center">${mensaje}</div>
        </c:if>

        <%-- 🔍 Filtros de búsqueda --%>
        <form action="VerTablasValidadasServlet" method="get" class="row g-3 mb-4">
            <div class="col-md-4">
                <label class="form-label">Docente</label>
                <input type="text" name="docente" class="form-control" placeholder="Nombre del docente">
            </div>
            <div class="col-md-3">
                <label class="form-label">Desde</label>
                <input type="date" name="desde" class="form-control">
            </div>
            <div class="col-md-3">
                <label class="form-label">Hasta</label>
                <input type="date" name="hasta" class="form-control">
            </div>
            <div class="col-md-2 d-flex align-items-end">
                <button type="submit" class="btn btn-primary w-100">
                    <i class="fas fa-search"></i> Filtrar
                </button>
            </div>
        </form>

        <%-- 📊 Tabla de resultados con scroll --%>
        <div class="scroll-table mb-4">
            <table class="table table-bordered table-hover align-middle">
                <thead class="table-success text-center">
                <tr>
                    <th>ID</th>
                    <th>Nombre tabla</th>
                    <th>Descripción</th>
                    <th>Docente</th>
                    <th>Fecha envío</th>
                    <th>Fecha validación</th>
                    <th>Cantidad notas</th>
                    <th>Promedio</th>
                    <th>Estado certificado</th>
                    <th>Acciones</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="tabla" items="${tablasValidadas}">
                    <tr>
                        <td class="text-center">${tabla.id_tabla}</td>
                        <td>${tabla.nombre}</td>
                        <td>${tabla.descripcion}</td>
                        <td>${tabla.docente}</td>
                        <td>${tabla.fecha_envio}</td>
                        <td>${tabla.fecha_validacion}</td>
                        <td class="text-center">${tabla.cantidad_notas}</td>
                        <td class="text-center">${tabla.promedio_notas}</td>
                        <td class="text-center">
                            <c:choose>
                                <c:when test="${tabla.estado eq 'Emitido'}">
                                    <span class="text-success"><i class="fas fa-certificate"></i> Emitido</span>
                                </c:when>
                                <c:when test="${tabla.estado eq 'Anulado'}">
                                    <span class="text-danger"><i class="fas fa-times-circle"></i> Anulado</span>
                                </c:when>
                                <c:otherwise>
                                    <span class="text-muted">${tabla.estado}</span>
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td class="text-center">
                            <!-- Botón eliminar -->
                            <a href="<%= request.getContextPath() %>/EliminarTablaServlet?tablaId=${tabla.id_tabla}"
                               class="btn btn-sm btn-outline-danger"
                               onclick="return confirm('¿Seguro que deseas eliminar esta validación?');">
                               <i class="fas fa-trash"></i> Eliminar
                            </a>
                            <!-- Botón exportar -->
                            <a href="<%= request.getContextPath() %>/ExportarTablaValidadaServlet?tablaId=${tabla.id_tabla}"
                               class="btn btn-sm btn-outline-success">
                               <i class="fas fa-file-export"></i> Exportar
                            </a>
                            <!-- Botón certificar -->
                            <c:choose>
                                <c:when test="${tabla.estado eq 'Emitido'}">
                                    <button class="btn btn-sm btn-secondary" disabled>
                                        <i class="fas fa-certificate"></i> Ya certificada
                                    </button>
                                </c:when>
                                <c:otherwise>
                                    <a href="${pageContext.request.contextPath}/CertificarServlet?tablaId=${tabla.id_tabla}"
                                       class="btn btn-sm btn-outline-primary"
                                       onclick="return confirm('¿Deseas certificar esta tabla?');">
                                       <i class="fas fa-certificate"></i> Certificar
                                    </a>
                                </c:otherwise>
                            </c:choose>
                        </td>
                    </tr>
                </c:forEach>
                <c:if test="${empty tablasValidadas}">
                    <tr>
                        <td colspan="10" class="text-center text-muted">
                            No se encontraron tablas validadas con los filtros aplicados.
                        </td>
                    </tr>
                </c:if>
                </tbody>
            </table>
        </div>

        <!-- 🔙 Botón de regreso -->
        <div class="text-center mt-3">
            <a href="<%= request.getContextPath() %>/panelAdministrador.jsp" class="btn btn-secondary">
                <i class="fas fa-arrow-left"></i> Volver al panel
            </a>
        </div>
    </main>

    <!-- 🔹 Pie institucional -->
    <jsp:include page="../fragmentos/footer.jsp" />

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>