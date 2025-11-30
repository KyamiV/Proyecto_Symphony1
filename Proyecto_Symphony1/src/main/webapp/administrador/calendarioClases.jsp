<%-- 
    Document   : calndarioClases
    Created on : 21/11/2025, 1:30:38 a. m.
    Author     : camiv
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <title>Calendario institucional | SymphonySIAS</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
  <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/estilos.css">
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
  <link href="https://cdn.jsdelivr.net/npm/fullcalendar@6.1.8/index.global.min.css" rel="stylesheet">
  <style>
    body { background-color: #f3f4f6; font-family: 'Poppins', sans-serif; }
    #calendar {
      max-width: 100%;
      margin: 20px auto;
      background: #fff;
      padding: 20px;
      border-radius: 12px;
      box-shadow: 0 4px 12px rgba(0,0,0,0.12);
    }
  </style>
</head>
<body class="bg-light">

  <!-- 📂 Menú lateral institucional -->
  <jsp:include page="../fragmentos/sidebar.jsp" />

  <!-- 🧭 Encabezado institucional -->
  <jsp:include page="../fragmentos/header.jsp" />

  <div class="container mt-4">
    <h5 class="mb-3 text-center">
      <i class="fas fa-calendar-alt me-2"></i> Calendario institucional editable
    </h5>
    <div id="calendar"></div>

    <!-- 🔙 Volver al panel administrador -->
    <div class="mt-4 text-end">
      <a href="PanelAdministradorServlet" class="btn btn-secondary">
        <i class="fas fa-arrow-left"></i> Volver al panel
      </a>
    </div>
  </div>

  <!-- 📌 Pie de página -->
  <jsp:include page="../fragmentos/footer.jsp" />

  <!-- ⚙️ Scripts -->
  <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
  <script src="https://cdn.jsdelivr.net/npm/fullcalendar@6.1.8/index.global.min.js"></script>
  <script>
  document.addEventListener('DOMContentLoaded', function() {
    var calendarEl = document.getElementById('calendar');
    var calendar = new FullCalendar.Calendar(calendarEl, {
      initialView: 'dayGridMonth',
      locale: 'es',
      editable: true,
      selectable: true,
      headerToolbar: {
        left: 'prev,next today',
        center: 'title',
        right: 'dayGridMonth,timeGridWeek,timeGridDay'
      },
      events: '<%= request.getContextPath() %>/CalendarioServlet', // 🔹 conecta con tu servlet

      // 📌 Selección de fecha
      dateClick: function(info) {
        alert('Seleccionaste: ' + info.dateStr);
      },

      // 📌 Click en evento
      eventClick: function(info) {
        alert(
          'Clase: ' + info.event.title + '\n' +
          'Docente: ' + info.event.extendedProps.docente + '\n' +
          'Etapa: ' + info.event.extendedProps.etapa + '\n' +
          'Grupo: ' + info.event.extendedProps.grupo
        );
      },

      // 📌 Arrastrar evento (cambiar fecha)
      eventDrop: function(info) {
        fetch('<%= request.getContextPath() %>/ActualizarCalendarioServlet', {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify({
            id_clase: info.event.extendedProps.id_clase,   // ✅ usar id_clase
            start: info.event.startStr,
            end: info.event.endStr
          })
        })
        .then(response => response.json())
        .then(data => {
          alert(data.message);
          if (!data.success) {
            info.revert(); // 🔹 revierte el cambio si falla
          }
        });
      },

      // 📌 Redimensionar evento (cambiar duración)
      eventResize: function(info) {
        fetch('<%= request.getContextPath() %>/ActualizarCalendarioServlet', {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify({
            id_clase: info.event.extendedProps.id_clase,   // ✅ mismo ajuste aquí
            start: info.event.startStr,
            end: info.event.endStr
          })
        })
        .then(response => response.json())
        .then(data => {
          alert(data.message);
          if (!data.success) {
            info.revert(); // 🔹 revierte el cambio si falla
          }
        });
      }
    });
    calendar.render();
  });
  </script>
</body>
</html>
