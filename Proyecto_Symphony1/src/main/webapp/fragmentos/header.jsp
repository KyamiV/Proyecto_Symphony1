<%-- 
    Document   : header.jsp
    Created on : 14/11/2025, 10:12:19 p. m.
    Autor      : camiv
    Propósito  : Encabezado institucional reutilizable con color, ícono y título dinámico según rol activo
    Trazabilidad: se incluye con <jsp:include page="fragmentos/header.jsp" />, valida sesión y muestra bienvenida personalizada
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.sql.*, java.text.SimpleDateFormat, com.mysymphony.proyecto_symphony1.util.Conexion" %>
<%@ page import="java.util.Date" %>

<%
    // 🔐 Validación de sesión
    String nombre = (String) session.getAttribute("nombreActivo");
    String rol = (String) session.getAttribute("rolActivo");

    if (nombre == null || rol == null) {
        session.setAttribute("mensaje", "⚠️ Debes iniciar sesión para acceder al panel.");
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }

    // 🕒 Hora de acceso
    String horaEntrada = new SimpleDateFormat("HH:mm:ss").format(new Date());

    // 🎯 Configuración visual por rol
    String iconoRol = "fas fa-user";
    String tituloRol = "Panel";
    String colorHex = "#6c757d";

    switch (rol.toLowerCase()) {
        case "estudiante":
            iconoRol = "fas fa-user-graduate";
            tituloRol = "Panel Estudiante";
            break;
        case "docente":
            iconoRol = "fas fa-chalkboard-teacher";
            tituloRol = "Panel Docente";
            break;
        case "coordinador académico":
            iconoRol = "fas fa-user-cog";
            tituloRol = "Panel Coordinador Académico";
            break;
        case "administrador":
            iconoRol = "fas fa-tools";
            tituloRol = "Panel Administrador";
            break;
    }

    // 🎨 Color institucional desde BD
    try (Connection conn = Conexion.getConnection()) {
        try (PreparedStatement psRol = conn.prepareStatement("SELECT color_hex FROM colores_roles WHERE rol = ?")) {
            psRol.setString(1, rol.trim().toLowerCase());
            try (ResultSet rsRol = psRol.executeQuery()) {
                if (rsRol.next()) {
                    colorHex = rsRol.getString("color_hex");
                } else {
                    try (PreparedStatement psGeneral = conn.prepareStatement("SELECT color_hex FROM colores_roles WHERE rol = 'general'");
                         ResultSet rsGeneral = psGeneral.executeQuery()) {
                        if (rsGeneral.next()) {
                            colorHex = rsGeneral.getString("color_hex");
                        }
                    }
                }
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
%>

<!-- 📂 Menú lateral -->
<jsp:include page="../fragmentos/sidebar.jsp" />

<!-- 🧭 Encabezado institucional -->
<div class="container-fluid px-4" style="margin-top: -10px;">
    <div class="dashboard-header d-flex justify-content-between align-items-center rounded-4 px-4 py-2 mb-4"
         style="background-color: <%= colorHex %>; color: white; box-shadow: 0 4px 12px rgba(0,0,0,0.08); font-family: 'Poppins', sans-serif;">

        <div class="d-flex align-items-center gap-4">
            <!-- 🍔 Botón hamburguesa -->
            <button id="menu-toggle" class="hamburguesa-btn" onclick="toggleSidebar()" title="Menú">
                <i class="fas fa-bars"></i>
            </button>

            <!-- 🎓 Título y saludo -->
            <div class="ms-4">
                <h3 class="mb-1"><i class="<%= iconoRol %>"></i> <%= tituloRol %> - SymphonySIAS</h3>
                <p class="mb-0">Bienvenid@, <strong><%= nombre %></strong> (<%= rol %>)</p>
                <p class="mb-0 text-white-50">Último acceso: <%= horaEntrada %></p>
            </div>
        </div>

        <!-- 🎼 Logo institucional -->
        <img src="<%= request.getContextPath() %>/assets/img/logo.png" alt="Logo SymphonySIAS" style="height: 55px;">
    </div>
</div>

<!-- 🎨 Estilos institucionales -->
<style>
    body { font-family: 'Poppins', sans-serif; background-color: #f4f6f9; padding: 30px; }
    .hamburguesa-btn { position: absolute; top: 40px; left: 20px; background-color: #f8f9fa; border: 2px solid white;
                       border-radius: 8px; padding: 6px 12px; font-size: 18px; color: #198754;
                       box-shadow: 0 2px 6px rgba(0,0,0,0.1); cursor: pointer; z-index: 1100; }
    .hamburguesa-btn:hover { background-color: #e2e6ea; }
    #sidebar { position: fixed; top: 0; left: -250px; width: 250px; height: 100%; background-color: #f8f9fa;
               transition: left 0.3s ease; z-index: 1000; padding: 1rem; }
    #sidebar.show { left: 0; }
    .btn-volver { background-color: #6c757d; color: white; padding: 8px 16px; border-radius: 6px; text-decoration: none; }
    .btn-volver:hover { background-color: #5a6268; }
    .tabla-auditoria th { background-color: #198754; color: white; }
</style>

<!-- ⚙️ Script para activar el menú -->
<script>
    function toggleSidebar() {
        const sidebar = document.getElementById("sidebar");
        sidebar.classList.toggle("show");
    }
</script>