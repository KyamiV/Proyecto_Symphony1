<%-- 
    Document   : login
    Created on : 13/11/2025, 6:59:19 p. m.
    Author     : camiv
--%>

<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Login Symphony</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="assets/css/estilos.css">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</head>
<body class="bg-light">
    <div class="container mt-5">
        <div class="row justify-content-center">
            <div class="col-md-6">
                <div class="card shadow-sm">
                    <div class="card-body">
                        <h3 class="text-center mb-4">Bienvenidos a SymphonySIAS</h3>
                        <form action="LoginServlet" method="post">
                            <div class="mb-3">
                                <label for="usuario" class="form-label">Correo</label>
                                <input type="text" class="form-control" name="usuario" id="usuario" placeholder="correo@symphony.edu" required>
                            </div>
                            <div class="mb-3">
                                <label for="clave" class="form-label">Contraseña</label>
                                <input type="password" class="form-control" name="clave" id="clave" placeholder="password" required>
                            </div>
                            <button type="submit" class="btn btn-primary w-100">Entrar</button>
                        </form>

                        <c:if test="${not empty error}">
                            <div class="alert alert-danger mt-3 text-center">${error}</div>
                        </c:if>

                        <div class="text-center mt-3">
                            <small>¿No estás registrado? <a href="registro.jsp">Crear cuenta</a></small>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</body>
</html>