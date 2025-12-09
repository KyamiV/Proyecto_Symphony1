/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mysymphony.proyecto_symphony1.dao;

import com.mysymphony.proyecto_symphony1.modelo.Clase;
import com.mysymphony.proyecto_symphony1.modelo.Docente;
import com.mysymphony.proyecto_symphony1.modelo.TablaValidada;
import com.mysymphony.proyecto_symphony1.dto.DocenteConClaseDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.sql.Types;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * ClaseDAO institucional Estado institucional: - disponible: clase abierta para
 * inscripción - asignada: clase con docente asignado - pendiente:
 * etapa/ejecución aún no finalizada (opcional) - eliminada: baja lógica (soft
 * delete)
 *
 * Tablas institucionales: - clases - usuarios (incluye rol='docente') -
 * clases_asignadas (id_clase, id_docente, fecha_asignacion, estado) -
 * inscripciones_clase (id_estudiante, id_clase, fecha_asignacion[, estado])
 */
public class ClaseDAO {

    private final Connection conn;

    public ClaseDAO(Connection conn) {
        this.conn = conn;
    }

   // ---------------- CONSULTAS DE OFERTA ----------------

// Clases disponibles para inscripción
public List<Clase> obtenerClasesDisponibles() throws SQLException {
    List<Clase> lista = new ArrayList<>();
    String sql = "SELECT c.id_clase, c.nombre_clase, c.instrumento, c.etapa, c.grupo, c.cupo, " +
                 "       (SELECT COUNT(*) FROM inscripciones_clase i WHERE i.id_clase = c.id_clase) AS inscritos, " +
                 "       c.fecha_limite, c.fecha_inicio, c.fecha_fin, c.estado " +
                 "FROM clases c WHERE LOWER(c.estado) = 'disponible' " +
                 "ORDER BY c.fecha_limite ASC, c.nombre_clase ASC";

    try (PreparedStatement ps = conn.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
            Clase clase = new Clase();
            clase.setId(rs.getInt("id_clase"));
            clase.setNombre(rs.getString("nombre_clase"));
            clase.setInstrumento(rs.getString("instrumento"));
            clase.setEtapa(rs.getString("etapa"));
            clase.setGrupo(rs.getString("grupo"));
            clase.setCupo(rs.getInt("cupo"));
            clase.setInscritos(rs.getInt("inscritos"));

            java.sql.Date limite = rs.getDate("fecha_limite");
            clase.setFechaLimite(limite != null ? limite.toLocalDate() : null);

            Timestamp inicio = rs.getTimestamp("fecha_inicio");
            clase.setFechaInicio(inicio != null ? inicio.toLocalDateTime() : null);

            Timestamp fin = rs.getTimestamp("fecha_fin");
            clase.setFechaFin(fin != null ? fin.toLocalDateTime() : null);

            clase.setEstado(rs.getString("estado"));
            lista.add(clase);
        }
    }
    return lista;
}

// Tablas enviadas pendientes de certificación
public List<Map<String,Object>> obtenerTablasEnviadasPendientes() {
    List<Map<String,Object>> lista = new ArrayList<>();
    String sql = "SELECT c.id_clase, c.nombre_clase, c.instrumento, c.fecha_inicio, c.fecha_fin, c.estado " +
                 "FROM clases c " +
                 "WHERE c.estado = 'enviada' " +
                 "ORDER BY c.fecha_inicio DESC";

    try (PreparedStatement ps = conn.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
            Map<String,Object> fila = new HashMap<>();
            fila.put("id_clase", rs.getInt("id_clase"));
            fila.put("nombre_clase", rs.getString("nombre_clase"));
            fila.put("instrumento", rs.getString("instrumento"));
            fila.put("fecha_inicio", rs.getDate("fecha_inicio"));
            fila.put("fecha_fin", rs.getDate("fecha_fin"));
            fila.put("estado", rs.getString("estado"));
            lista.add(fila);
        }
    } catch (SQLException e) {
        System.err.println("❌ Error al obtener tablas enviadas pendientes: " + e.getMessage());
    }
    return lista;
}

// Contar inscritos por clase
public int contarInscritosPorClase(int idClase) throws SQLException {
    String sql = "SELECT COUNT(*) FROM inscripciones_clase WHERE id_clase = ?";
    try (PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, idClase);
        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
    }
    return 0;
}

// Emitir certificados de una clase
public boolean emitirCertificados(int idClase) {
    String sql = "UPDATE tablas_certificacion " +
                 "SET estado = 'emitida', fecha_emision = CURRENT_DATE " +
                 "WHERE id_clase = ?";
    try (PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, idClase);
        int filas = ps.executeUpdate();
        return filas > 0;
    } catch (SQLException e) {
        System.err.println("❌ Error al emitir certificados: " + e.getMessage());
        return false;
    }
}

// Validar tabla de certificación de una clase
public boolean validarTablaCertificacion(int idClase, String usuario, String rol) {
    boolean ok = false;
    String sqlUpdate = "UPDATE clases SET estado = 'validada' WHERE id_clase = ?";
    String sqlInsert = "INSERT INTO tablas_validadas (modulo, estado, usuario, fecha_validacion) VALUES (?, ?, ?, CURRENT_TIMESTAMP)";
    try (PreparedStatement psU = conn.prepareStatement(sqlUpdate)) {
        psU.setInt(1, idClase);
        int filas = psU.executeUpdate();
        if (filas > 0) {
            ok = true;
            try (PreparedStatement psI = conn.prepareStatement(sqlInsert)) {
                psI.setString(1, "Certificación");
                psI.setString(2, "validada");
                psI.setString(3, usuario + " (" + rol + ")");
                psI.executeUpdate();
            }
        }
    } catch (SQLException e) {
        System.err.println("❌ Error validar clase: " + e.getMessage());
    }
    return ok;
}

// Listar clases pendientes de certificación
public List<Map<String, Object>> listarClasesPendientes() {
    String sql = "SELECT id_clase, nombre_clase, docente, estado FROM tablas_certificacion WHERE estado = 'enviada'";
    List<Map<String, Object>> lista = new ArrayList<>();
    try (PreparedStatement ps = conn.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
            Map<String, Object> fila = new HashMap<>();
            fila.put("id_clase", rs.getInt("id_clase"));
            fila.put("nombre_clase", rs.getString("nombre_clase"));
            fila.put("docente", rs.getString("docente"));
            fila.put("estado", rs.getString("estado"));
            lista.add(fila);
        }
    } catch (SQLException e) {
        System.err.println("❌ Error al listar pendientes: " + e.getMessage());
    }
    return lista;
}

   // ---------------- CONSULTAS DE OFERTA ----------------

// Listar tablas validadas
public List<TablaValidada> listarTablasValidadas() {
    List<TablaValidada> lista = new ArrayList<>();
    String sql = "SELECT id_validacion, modulo, estado, usuario, fecha_validacion " +
                 "FROM tablas_validadas ORDER BY fecha_validacion DESC";

    try (PreparedStatement ps = conn.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {

        while (rs.next()) {
            TablaValidada tv = new TablaValidada();
            tv.setIdValidacion(rs.getInt("id_validacion"));
            tv.setModulo(rs.getString("modulo"));
            tv.setEstado(rs.getString("estado"));
            tv.setUsuario(rs.getString("usuario"));
            tv.setFechaValidacion(rs.getTimestamp("fecha_validacion").toLocalDateTime());
            lista.add(tv);
        }

        if (lista.isEmpty()) {
            System.out.println("ℹ️ No aparecen tablas validadas porque la consulta devolvió vacío. " +
                               "Verifica que los nombres de columnas en la BD coincidan con el modelo: " +
                               "id_validacion, modulo, estado, usuario, fecha_validacion.");
        }

    } catch (SQLException e) {
        System.err.println("❌ Error al listar tablas validadas: " + e.getMessage());
    }
    return lista;
}

// Clases filtradas por instrumento y etapa, con docente y conteo de inscritos
public List<Clase> obtenerClasesFiltradas(String instrumento, String etapa) {
    List<Clase> clases = new ArrayList<>();

    StringBuilder sql = new StringBuilder();
    sql.append("SELECT c.id_clase, c.nombre_clase, c.instrumento, c.etapa, c.grupo, c.cupo, ")
       .append("       c.fecha_inicio, c.fecha_fin, c.fecha_limite, c.estado, ")
       .append("       COALESCE(u.nombre, '') AS docente, ")
       .append("       CONCAT(COALESCE(c.dia_semana,''), ' ', COALESCE(c.hora_inicio,''), ' - ', COALESCE(c.hora_fin,'')) AS horario, ")
       .append("       COUNT(i.id_estudiante) AS inscritos ")
       .append("FROM clases c ")
       .append("LEFT JOIN usuarios u ON c.id_docente = u.id_usuario ")
       .append("LEFT JOIN inscripciones_clase i ON c.id_clase = i.id_clase ") // ✅ corregido
       .append("WHERE 1=1 ");

    List<Object> params = new ArrayList<>();
    if (instrumento != null && !instrumento.trim().isEmpty()) {
        sql.append(" AND c.instrumento = ? ");
        params.add(instrumento.trim());
    }
    if (etapa != null && !etapa.trim().isEmpty()) {
        sql.append(" AND c.etapa = ? ");
        params.add(etapa.trim());
    }

    sql.append(" GROUP BY c.id_clase, c.nombre_clase, c.instrumento, c.etapa, c.grupo, c.cupo, ")
       .append("          c.fecha_inicio, c.fecha_fin, c.fecha_limite, c.estado, u.nombre, c.dia_semana, c.hora_inicio, c.hora_fin ")
       .append(" ORDER BY c.nombre_clase ASC ");

    try (PreparedStatement ps = conn.prepareStatement(sql.toString())) {
        int idx = 1;
        for (Object p : params) {
            ps.setString(idx++, (String) p);
        }
        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Clase clase = new Clase();
                clase.setId(rs.getInt("id_clase"));
                clase.setNombre(rs.getString("nombre_clase"));
                clase.setInstrumento(rs.getString("instrumento"));
                clase.setEtapa(rs.getString("etapa"));
                clase.setGrupo(rs.getString("grupo"));
                clase.setCupo(rs.getInt("cupo"));

                Timestamp tsInicio = rs.getTimestamp("fecha_inicio");
                clase.setFechaInicio(tsInicio != null ? tsInicio.toLocalDateTime() : null);

                Timestamp tsFin = rs.getTimestamp("fecha_fin");
                clase.setFechaFin(tsFin != null ? tsFin.toLocalDateTime() : null);

                java.sql.Date fechaLimite = rs.getDate("fecha_limite");
                clase.setFechaLimite(fechaLimite != null ? fechaLimite.toLocalDate() : null);

                clase.setEstado(rs.getString("estado"));
                clase.setDocenteNombre(rs.getString("docente"));
                clase.setHorario(rs.getString("horario"));
                clase.setInscritos(rs.getInt("inscritos"));

                clases.add(clase);
            }
        }
    } catch (SQLException e) {
        System.err.println("❌ Error al consultar clases filtradas: " + e.getMessage());
    }

    return clases;
}

    // Datos completos de una clase
public Map<String, String> obtenerDatosClase(int claseId) {
    Map<String, String> datos = new HashMap<>();
    String sql = "SELECT id_clase, nombre_clase, instrumento, etapa, grupo, cupo, " +
                 "       fecha_inicio, fecha_fin, id_docente, fecha_limite, estado, " +
                 "       fecha_creacion, fecha_actualizacion," +
                 "       aula, usuario_editor, fecha_asignacion " +
                 "FROM clases WHERE id_clase = ?";

    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setInt(1, claseId);
        try (ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                datos.put("id_clase", String.valueOf(rs.getInt("id_clase")));
                datos.put("nombre", rs.getString("nombre_clase"));
                datos.put("instrumento", rs.getString("instrumento"));
                datos.put("etapa", rs.getString("etapa"));
                datos.put("grupo", rs.getString("grupo"));
                datos.put("cupo", String.valueOf(rs.getInt("cupo")));
                datos.put("fecha_inicio", rs.getString("fecha_inicio"));
                datos.put("fecha_fin", rs.getString("fecha_fin"));
                datos.put("id_docente", rs.getString("id_docente"));
                datos.put("fecha_limite", rs.getString("fecha_limite"));
                datos.put("estado", rs.getString("estado"));
                datos.put("fecha_creacion", rs.getString("fecha_creacion"));
                datos.put("fecha_actualizacion", rs.getString("fecha_actualizacion"));
                datos.put("aula", rs.getString("aula"));
                datos.put("usuario_editor", rs.getString("usuario_editor"));
                datos.put("fecha_asignacion", rs.getString("fecha_asignacion"));
            }
        }
    } catch (Exception e) {
        System.out.println("❌ Error al obtener datos de clase: " + e.getMessage());
    }
    return datos;
}
    public List<Map<String, String>> obtenerHorariosPorClase(int claseId) {
    List<Map<String, String>> horarios = new ArrayList<>();
    String sql = "SELECT dia_semana, hora_inicio, hora_fin, aula FROM horarios_clase WHERE id_clase = ? ORDER BY dia_semana, hora_inicio";

    try (PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, claseId);
        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Map<String, String> fila = new HashMap<>();
                fila.put("dia", rs.getString("dia_semana"));
                fila.put("inicio", rs.getString("hora_inicio"));
                fila.put("fin", rs.getString("hora_fin"));
                fila.put("aula", rs.getString("aula"));
                horarios.add(fila);
            }
        }
    } catch (Exception e) {
        System.out.println("❌ Error al obtener horarios: " + e.getMessage());
    }
    return horarios;
}

    // ✅ Obtener detalle extendido de una clase
    public Map<String, Object> obtenerDetalleExtendido(int idClase) {
        Map<String, Object> detalle = new HashMap<>();
        detalle.put("id_clase", idClase); // ✅ poblar el identificador desde el inicio

        try {
            // 🧑‍🏫 Datos del docente asignado
            String sqlDocente = "SELECT c.id_clase, d.id_docente, CONCAT(d.nombre, ' ', d.apellido) AS docente, "
                    + "c.instrumento, c.dia_semana, c.hora_inicio, c.hora_fin "
                    + "FROM clases c "
                    + "LEFT JOIN docentes d ON c.id_docente = d.id_docente "
                    + "WHERE c.id_clase = ?";
            int idDocente = 0;
            try (PreparedStatement ps = conn.prepareStatement(sqlDocente)) {
                ps.setInt(1, idClase);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        detalle.put("id_clase", rs.getInt("id_clase"));
                        idDocente = rs.getInt("id_docente");
                        detalle.put("nombre_docente", rs.getString("docente"));
                        detalle.put("instrumento_docente", rs.getString("instrumento"));
                        detalle.put("dia_semana", rs.getString("dia_semana"));
                        detalle.put("hora_inicio", rs.getString("hora_inicio"));
                        detalle.put("hora_fin", rs.getString("hora_fin"));
                    }
                }
            }

            // 📊 Porcentaje ocupado
            String sqlIndicadores = "SELECT c.cupo, COUNT(i.id_inscripcion) AS inscritos "
                    + "FROM clases c "
                    + "LEFT JOIN inscripciones_clase i ON c.id_clase = i.id_clase "
                    + "WHERE c.id_clase = ? GROUP BY c.cupo";
            try (PreparedStatement ps = conn.prepareStatement(sqlIndicadores)) {
                ps.setInt(1, idClase);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        int cupo = rs.getInt("cupo");
                        int inscritos = rs.getInt("inscritos");
                        double porcentaje = (cupo > 0) ? (inscritos * 100.0 / cupo) : 0;
                        detalle.put("cupo", cupo);
                        detalle.put("inscritos", inscritos);
                        detalle.put("porcentaje_ocupado", porcentaje);
                    }
                }
            }

            // 📊 Total de clases activas del docente
            String sqlTotalClases = "SELECT COUNT(*) AS total FROM clases WHERE id_docente = ? AND estado='activa'";
            try (PreparedStatement ps = conn.prepareStatement(sqlTotalClases)) {
                ps.setInt(1, idDocente);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        detalle.put("total_clases_docente", rs.getInt("total"));
                    }
                }
            }

            // 📊 Tablas enviadas
            String sqlTablas = "SELECT COUNT(*) AS enviadas FROM tablas_certificacion WHERE id_clase = ?";
            try (PreparedStatement ps = conn.prepareStatement(sqlTablas)) {
                ps.setInt(1, idClase);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        int enviadas = rs.getInt("enviadas");
                        detalle.put("tablas_enviadas", enviadas);
                        detalle.put("tabla_enviada_por_docente", enviadas > 0);
                    }
                }
            }

            // 📊 Estudiantes aprobados
            String sqlAprobados = "SELECT COUNT(*) AS aprobados FROM notas WHERE id_clase = ? AND estado='aprobado'";
            try (PreparedStatement ps = conn.prepareStatement(sqlAprobados)) {
                ps.setInt(1, idClase);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        detalle.put("estudiantes_aprobados", rs.getInt("aprobados"));
                    }
                }
            }

            // 📊 Estudiantes que finalizaron etapa
            String sqlFinalizados = "SELECT COUNT(*) AS finalizados FROM notas WHERE id_clase = ? AND estado='finalizado'";
            try (PreparedStatement ps = conn.prepareStatement(sqlFinalizados)) {
                ps.setInt(1, idClase);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        detalle.put("estudiantes_finalizaron_etapa", rs.getInt("finalizados"));
                    }
                }
            }

            // 🧾 Historial
            String sqlHistorial = "SELECT fecha_creacion FROM clases WHERE id_clase = ?";
            try (PreparedStatement ps = conn.prepareStatement(sqlHistorial)) {
                ps.setInt(1, idClase);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        detalle.put("fecha_creacion_clase", rs.getString("fecha_creacion"));
                    }
                }
            }

            // Última acción en auditoría
            String sqlAuditoria = "SELECT accion FROM auditoria WHERE modulo='Clases' AND tabla_id=? ORDER BY fecha DESC LIMIT 1";
            try (PreparedStatement ps = conn.prepareStatement(sqlAuditoria)) {
                ps.setInt(1, idClase);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        String accion = rs.getString("accion");
                        detalle.put("ultima_accion_auditoria", accion);
                        detalle.put("validada_por_administrador", accion != null && accion.contains("Validación"));
                    }
                }
            }

            // 👥 Estudiantes inscritos con detalle
            String sqlEstudiantes = "SELECT e.nombre, e.correo, e.etapa_pedagogica AS etapa_actual, "
                    + "CASE WHEN n.id_nota IS NOT NULL THEN TRUE ELSE FALSE END AS tiene_notas, "
                    + "n.estado AS condicion "
                    + "FROM inscripciones_clase i "
                    + "JOIN estudiantes e ON i.id_estudiante = e.id_estudiante "
                    + "LEFT JOIN notas n ON i.id_estudiante = n.id_estudiante AND i.id_clase = n.id_clase "
                    + "WHERE i.id_clase = ?";
            List<Map<String, Object>> estudiantes = new ArrayList<>();
            try (PreparedStatement ps = conn.prepareStatement(sqlEstudiantes)) {
                ps.setInt(1, idClase);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        Map<String, Object> est = new HashMap<>();
                        est.put("nombre", rs.getString("nombre"));
                        est.put("correo", rs.getString("correo"));
                        est.put("etapa_actual", rs.getString("etapa_actual"));
                        est.put("tiene_notas", rs.getBoolean("tiene_notas"));
                        est.put("condicion", rs.getString("condicion"));
                        estudiantes.add(est);
                    }
                }
            }
            detalle.put("estudiantes_inscritos", estudiantes);

        } catch (SQLException e) {
            System.err.println("❌ Error al obtener detalle extendido de clase: " + e.getMessage());
        }

        return detalle;
    }

    // Obtener clase por ID con fechas y estado
    public Clase obtenerClasePorId(int idClase) {
        String sql = "SELECT id_clase, nombre_clase, instrumento, etapa, grupo, cupo, fecha_limite, fecha_inicio, fecha_fin, estado "
                + "FROM clases WHERE id_clase = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idClase);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Clase clase = new Clase();
                    clase.setId(rs.getInt("id_clase"));
                    clase.setNombre(rs.getString("nombre_clase"));
                    clase.setInstrumento(rs.getString("instrumento"));
                    clase.setEtapa(rs.getString("etapa"));
                    clase.setGrupo(rs.getString("grupo"));
                    clase.setCupo(rs.getInt("cupo"));

                    java.sql.Date limite = rs.getDate("fecha_limite");
                    clase.setFechaLimite(limite != null ? limite.toLocalDate() : null);

                    Timestamp inicio = rs.getTimestamp("fecha_inicio");
                    clase.setFechaInicio(inicio != null ? inicio.toLocalDateTime() : null);

                    Timestamp fin = rs.getTimestamp("fecha_fin");
                    clase.setFechaFin(fin != null ? fin.toLocalDateTime() : null);

                    clase.setEstado(rs.getString("estado"));
                    return clase;
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al obtener clase por ID: " + e.getMessage());
        }
        return null;
    }

    // ---------------- CRUD ----------------
    // Registrar nueva clase (estado: disponible)
    public boolean registrarClase(String nombreClase, String instrumento, String etapa, String grupo,
            int cupo, LocalDateTime fechaInicio, LocalDateTime fechaFin, LocalDate fechaLimite) {
        String sql = "INSERT INTO clases (nombre_clase, instrumento, etapa, grupo, cupo, "
                + "fecha_inicio, fecha_fin, fecha_limite, estado, fecha_creacion) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, 'disponible', CURRENT_DATE)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nombreClase != null ? nombreClase.trim() : "");
            stmt.setString(2, instrumento != null ? instrumento.trim() : "");
            stmt.setString(3, etapa != null ? etapa.trim() : "");
            stmt.setString(4, grupo != null ? grupo.trim() : "");
            stmt.setInt(5, cupo);

            if (fechaInicio != null) {
                stmt.setTimestamp(6, Timestamp.valueOf(fechaInicio));
            } else {
                stmt.setNull(6, Types.TIMESTAMP);
            }

            if (fechaFin != null) {
                stmt.setTimestamp(7, Timestamp.valueOf(fechaFin));
            } else {
                stmt.setNull(7, Types.TIMESTAMP);
            }

            if (fechaLimite != null) {
                stmt.setDate(8, java.sql.Date.valueOf(fechaLimite));
            } else {
                stmt.setNull(8, Types.DATE);
            }

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("❌ Error al registrar clase: " + e.getMessage());
            return false;
        }
    }

    // Actualizar una clase (datos y fechas)
    public boolean actualizarClase(int idClase, String nombre, String instrumento, String etapa, String grupo,
            int cupo, LocalDateTime fechaInicio, LocalDateTime fechaFin, LocalDate fechaLimite) {
        String sql = "UPDATE clases SET nombre_clase = ?, instrumento = ?, etapa = ?, grupo = ?, cupo = ?, "
                + "fecha_inicio = ?, fecha_fin = ?, fecha_limite = ?, fecha_actualizacion = CURRENT_DATE "
                + "WHERE id_clase = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nombre != null ? nombre.trim() : "");
            stmt.setString(2, instrumento != null ? instrumento.trim() : "");
            stmt.setString(3, etapa != null ? etapa.trim() : "");
            stmt.setString(4, grupo != null ? grupo.trim() : "");
            stmt.setInt(5, cupo);

            // Manejo seguro de fechas
            if (fechaInicio != null) {
                stmt.setTimestamp(6, Timestamp.valueOf(fechaInicio));
            } else {
                stmt.setNull(6, java.sql.Types.TIMESTAMP);
            }

            if (fechaFin != null) {
                stmt.setTimestamp(7, Timestamp.valueOf(fechaFin));
            } else {
                stmt.setNull(7, java.sql.Types.TIMESTAMP);
            }

            if (fechaLimite != null) {
                stmt.setDate(8, java.sql.Date.valueOf(fechaLimite));
            } else {
                stmt.setNull(8, java.sql.Types.DATE);
            }

            stmt.setInt(9, idClase);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("❌ Error al actualizar clase ID " + idClase + ": " + e.getMessage());
            return false;
        }
    }

    // Eliminar clase (soft delete → estado = eliminada)
    public boolean eliminarClase(int idClase) {
        String sql = "UPDATE clases SET estado = 'eliminada', fecha_actualizacion = CURRENT_DATE WHERE id_clase = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idClase);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("❌ Error al eliminar clase: " + e.getMessage());
            return false;
        }
    }

    // ---------------- HORARIO ----------------
    // Actualizar horario (día, inicio, fin) con trazabilidad
    public boolean actualizarHorarioClase(int idClase, String diaSemana, String horaInicio, String horaFin) {
        String sql = "UPDATE clases SET dia_semana = ?, hora_inicio = ?, hora_fin = ?, fecha_actualizacion = CURRENT_DATE WHERE id_clase = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, diaSemana);
            ps.setString(2, horaInicio);
            ps.setString(3, horaFin);
            ps.setInt(4, idClase);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("❌ Error al actualizar horario: " + e.getMessage());
            return false;
        }
    }

    // ---------------- DOCENTES ----------------
    // Listar docentes institucionales (usuarios rol=docente)
    public List<Docente> listarDocentes() throws SQLException {
        List<Docente> lista = new ArrayList<>();
        String sql = "SELECT id_usuario, nombre FROM usuarios WHERE rol = 'docente' ORDER BY nombre ASC";
        try (PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Docente d = new Docente();
                d.setId(rs.getInt("id_usuario"));
                d.setNombre(rs.getString("nombre"));
                lista.add(d);
            }
        }
        return lista;
    }

    public boolean asignarDocenteConTrazabilidad(int idDocente, int idClase, String usuario, String rol) {
    // Actualizar clase
    String updateClase = "UPDATE clases SET id_docente = ?, estado = 'asignada', fecha_actualizacion = CURRENT_DATE WHERE id_clase = ?";

    // Insertar trazabilidad con 5 parámetros (registrada_por + tipo_registro + observacion)
    String insertAsignacion = "INSERT INTO clases_asignadas (id_clase, id_docente, fecha_asignacion, estado, registrada_por, tipo_registro, observacion) " +
                              "VALUES (?, ?, CURRENT_DATE, 'asignada', ?, ?, ?)";

    try {
        conn.setAutoCommit(false);

        // Actualizar clase
        try (PreparedStatement ps = conn.prepareStatement(updateClase)) {
            ps.setInt(1, idDocente);
            ps.setInt(2, idClase);
            ps.executeUpdate();
        }

        // Insertar trazabilidad
        try (PreparedStatement stmt = conn.prepareStatement(insertAsignacion)) {
            stmt.setInt(1, idClase);   // id_clase
            stmt.setInt(2, idDocente); // id_docente
            stmt.setString(3, usuario); // registrada_por
            stmt.setString(4, "Asignación manual"); // tipo_registro
            stmt.setString(5, "Clase asignada al docente desde panel administrador"); // observacion
            stmt.executeUpdate();
        }

        // Bitácora
        new BitacoraDAO(conn).registrarAccion(
                "Clase ID " + idClase + " asignada al docente ID " + idDocente,
                usuario, rol, "Asignación de Docentes");

        // Auditoría
        Map<String, String> registro = new HashMap<>();
        registro.put("usuario", usuario);
        registro.put("rol", rol);
        registro.put("modulo", "Asignación de Docentes");
        registro.put("accion", "Asignó docente ID " + idDocente + " a clase ID " + idClase);
        new AuditoriaDAO(conn).registrarAccion(registro);

        conn.commit();
        return true;
    } catch (Exception e) {
        try { conn.rollback(); } catch (Exception ex) { }
        System.err.println("❌ Error en asignarDocenteConTrazabilidad: " + e.getMessage());
        return false;
    } finally {
        try { conn.setAutoCommit(true); } catch (Exception e) { }
    }
}

    // Asignar un docente con trazabilidad institucional
    public boolean asignarDocenteAClase(int idDocente, int idClase) throws SQLException {
        // Validar docente
        try (PreparedStatement psVal = conn.prepareStatement(
                "SELECT COUNT(*) FROM usuarios WHERE id_usuario = ? AND rol = 'docente'")) {
            psVal.setInt(1, idDocente);
            try (ResultSet rs = psVal.executeQuery()) {
                if (rs.next() && rs.getInt(1) == 0) {
                    throw new SQLException("❌ El docente con ID " + idDocente + " no existe.");
                }
            }
        }

        // Validar clase y que no tenga docente ya
        try (PreparedStatement psValClase = conn.prepareStatement("SELECT id_docente FROM clases WHERE id_clase = ?")) {
            psValClase.setInt(1, idClase);
            try (ResultSet rsClase = psValClase.executeQuery()) {
                if (rsClase.next()) {
                    int actual = rsClase.getInt("id_docente");
                    if (!rsClase.wasNull() && actual > 0) {
                        throw new SQLException("⚠️ La clase ya tiene un docente asignado.");
                    }
                } else {
                    throw new SQLException("❌ La clase con ID " + idClase + " no existe.");
                }
            }
        }

        // Actualizar clase
        String sqlUpdate = "UPDATE clases SET id_docente = ?, estado = 'asignada', fecha_actualizacion = CURRENT_DATE WHERE id_clase = ?";
        try (PreparedStatement stmtUpdate = conn.prepareStatement(sqlUpdate)) {
            stmtUpdate.setInt(1, idDocente);
            stmtUpdate.setInt(2, idClase);
            int filas = stmtUpdate.executeUpdate();
            if (filas > 0) {
                // Registrar trazabilidad
                // Evitar duplicado
                try (PreparedStatement psDup = conn.prepareStatement(
                        "SELECT COUNT(*) FROM clases_asignadas WHERE id_clase = ?")) {
                    psDup.setInt(1, idClase);
                    try (ResultSet rs = psDup.executeQuery()) {
                        if (rs.next() && rs.getInt(1) == 0) {
                            String sqlInsert = "INSERT INTO clases_asignadas (id_clase, id_docente, fecha_asignacion, estado) "
                                    + "VALUES (?, ?, CURRENT_DATE, 'asignada')";
                            try (PreparedStatement stmtInsert = conn.prepareStatement(sqlInsert)) {
                                stmtInsert.setInt(1, idClase);
                                stmtInsert.setInt(2, idDocente);
                                stmtInsert.executeUpdate();
                            }
                        }
                    }
                }
                return true;
            }
        }
        return false;
    }


    // Desasignar docente (libera clase y la deja disponible)
    public boolean desasignarDocenteDeClase(int idClase) {
        try {
            conn.setAutoCommit(false);

            try (PreparedStatement ps = conn.prepareStatement(
                    "UPDATE clases SET id_docente = NULL, estado = 'disponible', fecha_actualizacion = CURRENT_DATE WHERE id_clase = ?")) {
                ps.setInt(1, idClase);
                int filas = ps.executeUpdate();
                if (filas == 0) {
                    conn.rollback();
                    return false;
                }
            }

            try (PreparedStatement ps2 = conn.prepareStatement(
                    "DELETE FROM clases_asignadas WHERE id_clase = ?")) {
                ps2.setInt(1, idClase);
                ps2.executeUpdate();
            }

            conn.commit();
            return true;
        } catch (SQLException e) {
            try { conn.rollback(); } catch (SQLException ignored) {}
            System.err.println("❌ Error al desasignar docente: " + e.getMessage());
            return false;
        } finally {
            try { conn.setAutoCommit(true); } catch (SQLException ignored) {}
        }
    }

    // Obtener clases asignadas por docente
    public List<Clase> obtenerClasesAsignadasPorDocente(int docenteId) {
        List<Clase> clases = new ArrayList<>();
        String sql = "SELECT c.id_clase, c.nombre_clase, c.instrumento, c.etapa, c.aula, c.dia_semana, c.hora_inicio, c.hora_fin, c.cupo "
                + "FROM clases c INNER JOIN clases_asignadas ca ON c.id_clase = ca.id_clase "
                + "WHERE ca.id_docente = ? "
                + "ORDER BY c.nombre_clase ASC";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, docenteId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Clase clase = new Clase();
                    clase.setId(rs.getInt("id_clase"));
                    clase.setNombre(rs.getString("nombre_clase"));
                    clase.setInstrumento(rs.getString("instrumento"));
                    clase.setEtapa(rs.getString("etapa"));
                    clase.setAula(rs.getString("aula"));
                    clase.setDia(rs.getString("dia_semana"));
                    clase.setInicio(rs.getString("hora_inicio"));
                    clase.setFin(rs.getString("hora_fin"));
                    clase.setCupo(rs.getInt("cupo"));
                    clases.add(clase);
                }
            }
            System.out.println("✅ Clases asignadas encontradas: " + clases.size());
        } catch (SQLException e) {
            System.err.println("❌ Error al obtener clases por docente: " + e.getMessage());
        }
        return clases;
    }

    // Verificar si clase ya está asignada
    /*---------------------------------------------------------
     * 1️⃣ Validar si ya existe una clase con nombre y grupo
     *---------------------------------------------------------*/
    public boolean existeClase(String nombreClase, String grupo) throws SQLException {
        String sql = "SELECT COUNT(*) FROM clases WHERE nombre_clase = ? AND grupo = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nombreClase);
            ps.setString(2, grupo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    /*---------------------------------------------------------
     * 2️⃣ Validar si una clase ya tiene docente asignado
     *---------------------------------------------------------*/
    public boolean estaClaseYaAsignada(int idClase) throws SQLException {
        String sql = "SELECT id_docente FROM clases WHERE id_clase = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idClase);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int idDocente = rs.getInt("id_docente");
                    if (rs.wasNull()) {
                        return false;
                    }
                    return idDocente > 0;
                }
            }
        }
        return false;
    }

    // ---------------- FECHAS ----------------
    // Listar clases con fechas (solo disponibles)
    public List<Clase> listarClasesConFechas() throws SQLException {
        List<Clase> clases = new ArrayList<>();
        String sql = "SELECT id_clase, nombre_clase, instrumento, etapa, id_docente, fecha_inicio, fecha_fin, grupo "
                + "FROM clases WHERE estado = 'disponible' ORDER BY nombre_clase ASC";
        try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Clase clase = new Clase();
                clase.setId(rs.getInt("id_clase"));
                clase.setNombre(rs.getString("nombre_clase"));
                clase.setInstrumento(rs.getString("instrumento"));
                clase.setEtapa(rs.getString("etapa"));
                clase.setDocenteId(rs.getInt("id_docente"));

                Timestamp inicioTs = rs.getTimestamp("fecha_inicio");
                clase.setFechaInicio(inicioTs != null ? inicioTs.toLocalDateTime() : null);

                Timestamp finTs = rs.getTimestamp("fecha_fin");
                clase.setFechaFin(finTs != null ? finTs.toLocalDateTime() : null);

                clase.setGrupo(rs.getString("grupo"));
                clases.add(clase);
            }
        }
        return clases;
    }

    // Actualizar fechas de clase con trazabilidad
    public boolean actualizarFechasClase(int idClase, LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        if (fechaInicio != null && fechaFin != null && fechaFin.isBefore(fechaInicio)) {
            throw new IllegalArgumentException("La fecha de fin no puede ser anterior a la fecha de inicio.");
        }

        String sql = "UPDATE clases SET fecha_inicio = ?, fecha_fin = ?, fecha_actualizacion = CURRENT_DATE WHERE id_clase = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            if (fechaInicio != null) {
                ps.setTimestamp(1, Timestamp.valueOf(fechaInicio));
            } else {
                ps.setNull(1, Types.TIMESTAMP);
            }

            if (fechaFin != null) {
                ps.setTimestamp(2, Timestamp.valueOf(fechaFin));
            } else {
                ps.setNull(2, Types.TIMESTAMP);
            }

            ps.setInt(3, idClase);
            int filas = ps.executeUpdate();
            return filas > 0;
        } catch (SQLException e) {
            System.err.println("❌ Error al actualizar fechas de clase ID " + idClase + ": " + e.getMessage());
            return false;
        }
    }

    // Solo rol docente puede actualizar horario
    public boolean actualizarHorarioClase(int idClase, String diaSemana, String horaInicio, String horaFin, int idDocente) {
        String sql = "UPDATE clases SET dia_semana = ?, hora_inicio = ?, hora_fin = ?, fecha_actualizacion = CURRENT_DATE "
                + "WHERE id_clase = ? AND id_docente = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, diaSemana);
            ps.setString(2, horaInicio);
            ps.setString(3, horaFin);
            ps.setInt(4, idClase);
            ps.setInt(5, idDocente); // validación: solo el docente asignado puede modificar
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("❌ Error al actualizar horario: " + e.getMessage());
            return false;
        }
    }

    // ---------------- LISTADOS ADMIN ----------------
    // Listar clases creadas (todas)
    public List<Clase> listarClasesCreadas() throws SQLException {
        List<Clase> lista = new ArrayList<>();
        String sql = "SELECT id_clase, nombre_clase, instrumento, etapa, grupo, cupo, "
                + "fecha_limite, fecha_inicio, fecha_fin, fecha_creacion, estado "
                + "FROM clases ORDER BY nombre_clase ASC";

        try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Clase c = new Clase();
                c.setId(rs.getInt("id_clase"));
                c.setNombre(rs.getString("nombre_clase"));
                c.setInstrumento(rs.getString("instrumento"));
                c.setEtapa(rs.getString("etapa"));
                c.setGrupo(rs.getString("grupo"));
                c.setCupo(rs.getInt("cupo"));

                // Fechas
                java.sql.Date limite = rs.getDate("fecha_limite");
                c.setFechaLimite(limite != null ? limite.toLocalDate() : null);

                java.sql.Timestamp inicio = rs.getTimestamp("fecha_inicio");
                c.setFechaInicio(inicio != null ? inicio.toLocalDateTime() : null);

                java.sql.Timestamp fin = rs.getTimestamp("fecha_fin");
                c.setFechaFin(fin != null ? fin.toLocalDateTime() : null);

                java.sql.Date creacion = rs.getDate("fecha_creacion");
                c.setFechaCreacion(creacion != null ? creacion.toLocalDate() : null);

                c.setEstado(rs.getString("estado"));

                // 🔹 Actualizar inscritos reales
                int inscritos = contarInscritosPorClase(c.getIdClase());
                c.setInscritos(inscritos);

                lista.add(c);
            }
        }
        return lista;
    }

    // ✅ Listar clases con detalle para el JSP
    public List<Map<String, String>> listarClasesConDetalle() {
        List<Map<String, String>> lista = new ArrayList<>();

        String sql = "SELECT c.id_clase, c.nombre_clase, c.instrumento, c.fecha_inicio, c.fecha_fin, c.estado "
                + "FROM clases c WHERE c.estado <> 'eliminada'";

        try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Map<String, String> fila = new HashMap<>();

                // 🔑 Poblar cada campo con snake_case
                fila.put("id_clase", String.valueOf(rs.getInt("id_clase")));
                fila.put("nombre_clase", rs.getString("nombre_clase"));
                fila.put("instrumento", rs.getString("instrumento"));
                fila.put("fecha_inicio", rs.getString("fecha_inicio"));
                fila.put("fecha_fin", rs.getString("fecha_fin"));
                fila.put("estado", rs.getString("estado"));

                lista.add(fila);
            }

        } catch (SQLException e) {
            System.err.println("❌ Error al listar clases con detalle: " + e.getMessage());
        }

        return lista;
    }

    public List<Clase> listarClases() {
        List<Clase> lista = new ArrayList<>();
        String sql = "SELECT c.id_clase, c.nombre_clase, c.instrumento, c.etapa, c.grupo, c.cupo, c.fecha_limite, c.estado "
                + "FROM clases c ORDER BY c.nombre_clase";

        try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Clase clase = new Clase();

                // ✅ Mapeo institucional de columnas
                clase.setIdClase(rs.getInt("id_clase"));
                clase.setNombreClase(rs.getString("nombre_clase"));
                clase.setInstrumento(rs.getString("instrumento"));
                clase.setEtapa(rs.getString("etapa"));
                clase.setGrupo(rs.getString("grupo"));
                clase.setCupo(rs.getInt("cupo"));

                // ✅ Conversión segura de fecha
                java.sql.Date sqlFechaLimite = rs.getDate("fecha_limite");
                if (sqlFechaLimite != null) {
                    clase.setFechaLimite(sqlFechaLimite.toLocalDate());
                }

                clase.setEstado(rs.getString("estado"));

                lista.add(clase);
            }

        } catch (SQLException e) {
            System.err.println("❌ Error al listar clases: " + e.getMessage());
        }

        return lista;
    }

    // Listar clases disponibles para inscripción (panel oferta)
    public List<Clase> listarClasesDisponiblesParaInscripcion() {
    List<Clase> lista = new ArrayList<>();

    String sql = "SELECT c.id_clase, c.nombre_clase, c.instrumento, c.etapa, c.grupo, c.cupo, " +
                 "       c.fecha_limite, c.estado, c.aula, c.dia_semana, c.hora_inicio, c.hora_fin, " +
                 "       c.id_docente, c.fecha_inicio, c.fecha_fin, " +
                 "       COALESCE(u.nombre, '') AS docente_nombre, " +
                 "       (SELECT COUNT(*) FROM inscripciones_clase i WHERE i.id_clase = c.id_clase) AS inscritos " +
                 "FROM clases c " +
                 "LEFT JOIN usuarios u ON c.id_docente = u.id_usuario " +
                 "WHERE c.estado = 'disponible' " +
                 "  AND (c.id_docente IS NULL OR c.id_docente = 0) " +   // 🔎 Solo clases sin docente asignado
                 "ORDER BY c.fecha_limite ASC";

    try (PreparedStatement stmt = conn.prepareStatement(sql);
         ResultSet rs = stmt.executeQuery()) {

        while (rs.next()) {
            Clase clase = new Clase();

            // Datos básicos
            clase.setId(rs.getInt("id_clase"));
            clase.setNombre(rs.getString("nombre_clase"));
            clase.setInstrumento(rs.getString("instrumento"));
            clase.setEtapa(rs.getString("etapa"));
            clase.setGrupo(rs.getString("grupo"));
            clase.setCupo(rs.getInt("cupo"));

            // Fechas
            java.sql.Date fechaSql = rs.getDate("fecha_limite");
            clase.setFechaLimite(fechaSql != null ? fechaSql.toLocalDate() : null);

            clase.setEstado(rs.getString("estado"));
            clase.setAula(rs.getString("aula"));
            clase.setDia(rs.getString("dia_semana"));
            clase.setInicio(rs.getString("hora_inicio"));
            clase.setFin(rs.getString("hora_fin"));

            // Docente
            clase.setDocenteId(rs.getInt("id_docente"));
            clase.setDocenteNombre(rs.getString("docente_nombre"));

            // Inscritos
            clase.setInscritos(rs.getInt("inscritos"));

            // Fechas inicio y fin
            Timestamp inicioTs = rs.getTimestamp("fecha_inicio");
            clase.setFechaInicio(inicioTs != null ? inicioTs.toLocalDateTime() : null);

            Timestamp finTs = rs.getTimestamp("fecha_fin");
            clase.setFechaFin(finTs != null ? finTs.toLocalDateTime() : null);

            lista.add(clase);
        }

    } catch (SQLException e) {
        System.err.println("❌ Error al listar clases disponibles: " + e.getMessage());
    }

    return lista;
}


    // Listar todas las clases (administrador)
    public List<Clase> listarTodasLasClases() {
        List<Clase> lista = new ArrayList<>();
        String sql = "SELECT c.id_clase, c.nombre_clase, c.instrumento, c.etapa, c.grupo, c.cupo, "
                + "       c.fecha_limite, c.estado, c.aula, c.dia_semana, c.hora_inicio, c.hora_fin, "
                + "       c.id_docente, c.fecha_inicio, c.fecha_fin, "
                + "       COALESCE(u.nombre, '') AS docente_nombre, "
                + "       (SELECT COUNT(*) FROM inscripciones_clase i WHERE i.id_clase = c.id_clase) AS inscritos "
                + "FROM clases c "
                + "LEFT JOIN usuarios u ON c.id_docente = u.id_usuario "
                + "ORDER BY c.fecha_limite ASC";
        try (PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Clase clase = new Clase();
                clase.setId(rs.getInt("id_clase"));
                clase.setNombre(rs.getString("nombre_clase"));
                clase.setInstrumento(rs.getString("instrumento"));
                clase.setEtapa(rs.getString("etapa"));
                clase.setGrupo(rs.getString("grupo"));
                clase.setCupo(rs.getInt("cupo"));

                java.sql.Date fechaSql = rs.getDate("fecha_limite");
                clase.setFechaLimite(fechaSql != null ? fechaSql.toLocalDate() : null);

                clase.setEstado(rs.getString("estado"));
                clase.setAula(rs.getString("aula"));
                clase.setDia(rs.getString("dia_semana"));
                clase.setInicio(rs.getString("hora_inicio"));
                clase.setFin(rs.getString("hora_fin"));
                clase.setDocenteId(rs.getInt("id_docente"));
                clase.setDocenteNombre(rs.getString("docente_nombre"));
                clase.setInscritos(rs.getInt("inscritos"));

                Timestamp inicioTs = rs.getTimestamp("fecha_inicio");
                clase.setFechaInicio(inicioTs != null ? inicioTs.toLocalDateTime() : null);

                Timestamp finTs = rs.getTimestamp("fecha_fin");
                clase.setFechaFin(finTs != null ? finTs.toLocalDateTime() : null);

                lista.add(clase);
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al listar todas las clases: " + e.getMessage());
        }
        return lista;
    }

    // Listar clases con inscritos (conteo > 0)
    public List<Clase> listarClasesConInscritos() throws SQLException {
        List<Clase> lista = new ArrayList<>();
        String sql = "SELECT c.id_clase, c.nombre_clase, c.instrumento, c.etapa, c.grupo, c.estado, "
                + "       COUNT(i.id_estudiante) AS inscritos "
                + "FROM clases c "
                + "JOIN inscripciones_clase i ON i.id_clase = c.id_clase "
                + "GROUP BY c.id_clase, c.nombre_clase, c.instrumento, c.etapa, c.grupo, c.estado "
                + "HAVING COUNT(i.id_estudiante) > 0";
        try (PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Clase c = new Clase();
                c.setId(rs.getInt("id_clase"));
                c.setNombre(rs.getString("nombre_clase"));
                c.setInstrumento(rs.getString("instrumento"));
                c.setEtapa(rs.getString("etapa"));
                c.setGrupo(rs.getString("grupo"));
                c.setEstado(rs.getString("estado"));
                c.setInscritos(rs.getInt("inscritos"));
                lista.add(c);
            }
        }
        return lista;
    }

    // Clases sin docente asignado
    public List<Clase> obtenerClasesSinDocente() {
        List<Clase> lista = new ArrayList<>();
        String sql = "SELECT id_clase, nombre_clase, instrumento, etapa FROM clases WHERE id_docente IS NULL ORDER BY nombre_clase ASC";
        try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Clase clase = new Clase();
                clase.setId(rs.getInt("id_clase"));
                clase.setNombre(rs.getString("nombre_clase"));
                clase.setInstrumento(rs.getString("instrumento"));
                clase.setEtapa(rs.getString("etapa"));
                lista.add(clase);
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al obtener clases sin docente: " + e.getMessage());
        }
        return lista;
    }
    
    

    // ---------------- LISTADOS CON DOCENTE ----------------
    // Listar clases con docente asignado e inscritos
    public List<DocenteConClaseDTO> listarClasesConDocenteYInscritos() throws SQLException {
        List<DocenteConClaseDTO> lista = new ArrayList<>();
        String sql = "SELECT c.id_clase, c.nombre_clase, c.instrumento, c.etapa, c.grupo, "
                + "COUNT(i.id_estudiante) AS inscritos, d.nombre AS docenteNombre, "
                + "c.fecha_inicio, c.fecha_fin "
                + "FROM clases c "
                + "LEFT JOIN inscripciones_clase i ON c.id_clase = i.id_clase "
                + "LEFT JOIN docentes d ON c.id_docente = d.id_docente "
                + "WHERE c.id_docente IS NOT NULL "
                + "GROUP BY c.id_clase, c.nombre_clase, c.instrumento, c.etapa, c.grupo, d.nombre, c.fecha_inicio, c.fecha_fin";

        try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                DocenteConClaseDTO dto = new DocenteConClaseDTO();
                dto.setClaseId(rs.getInt("id_clase"));
                dto.setClaseNombre(rs.getString("nombre_clase"));
                dto.setInstrumento(rs.getString("instrumento"));
                dto.setEtapa(rs.getString("etapa"));
                dto.setGrupo(rs.getString("grupo"));
                dto.setInscritos(rs.getInt("inscritos"));
                dto.setDocenteNombre(rs.getString("docenteNombre"));

                // ✅ Conversión segura
                dto.setFechaInicio(rs.getObject("fecha_inicio", LocalDate.class));
                dto.setFechaFin(rs.getObject("fecha_fin", LocalDate.class));

                lista.add(dto);
            }
        }
        return lista;
    }
    
    public List<Clase> listarClasesAsignadas(int idDocente) throws SQLException {
    List<Clase> lista = new ArrayList<>();
    String sql = "SELECT c.* FROM clases c " +
                 "INNER JOIN clases_asignadas ca ON c.id_clase = ca.id_clase " +
                 "WHERE ca.id_docente = ?";
    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setInt(1, idDocente);
        try (ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Clase clase = new Clase();
                clase.setIdClase(rs.getInt("id_clase"));
                clase.setNombre(rs.getString("nombre"));
                // otros campos...
                lista.add(clase);
            }
        }
    }
    return lista;
}
    public List<DocenteConClaseDTO> listarClasesPorDocente(int idDocente) throws SQLException {
    List<DocenteConClaseDTO> lista = new ArrayList<>();

    String sql = "SELECT d.id_docente, d.nombre AS docenteNombre, " +
                 "       c.id_clase, c.nombre_clase, c.instrumento, c.etapa, c.grupo, " +
                 "       c.cupo, " +
                 "       (SELECT COUNT(*) FROM inscripciones i WHERE i.id_clase = c.id_clase) AS inscritos, " +
                 "       c.dia_semana, c.hora_inicio, c.hora_fin, c.aula, " +
                 "       c.fecha_limite, c.fecha_inicio, c.fecha_fin, " +
                 "       c.estado AS estadoClase, ca.estado AS estadoAsignacion " +
                 "FROM clases c " +
                 "INNER JOIN clases_asignadas ca ON c.id_clase = ca.id_clase " +
                 "INNER JOIN docentes d ON ca.id_docente = d.id_docente " +
                 "WHERE ca.id_docente = ? AND ca.estado IN ('asignada','activa') " +
                 "ORDER BY c.nombre_clase ASC";

    try (PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, idDocente);

        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                DocenteConClaseDTO dto = new DocenteConClaseDTO();
                dto.setDocenteId(rs.getInt("id_docente"));
                dto.setDocenteNombre(rs.getString("docenteNombre"));
                dto.setClaseId(rs.getInt("id_clase"));
                dto.setClaseNombre(rs.getString("nombre_clase"));
                dto.setInstrumento(rs.getString("instrumento"));
                dto.setEtapa(rs.getString("etapa"));
                dto.setGrupo(rs.getString("grupo"));
                dto.setCupo(rs.getInt("cupo"));
                dto.setInscritos(rs.getInt("inscritos"));

                // Construir horario
                String dia = rs.getString("dia_semana");
                String inicio = rs.getString("hora_inicio");
                String fin = rs.getString("hora_fin");
                dto.construirHorario(dia, inicio, fin);

                // Aula
                dto.setAula(rs.getString("aula"));

                // Manejo de fechas con java.sql.Date → LocalDate
                java.sql.Date fechaLimite = rs.getDate("fecha_limite");
                java.sql.Date fechaInicio = rs.getDate("fecha_inicio");
                java.sql.Date fechaFin = rs.getDate("fecha_fin");
                if (fechaLimite != null) dto.setFechaLimite(fechaLimite.toLocalDate());
                if (fechaInicio != null) dto.setFechaInicio(fechaInicio.toLocalDate());
                if (fechaFin != null) dto.setFechaFin(fechaFin.toLocalDate());

                // Estados diferenciados
                dto.setEstadoClase(rs.getString("estadoClase"));
                dto.setEstadoAsignacion(rs.getString("estadoAsignacion"));

                lista.add(dto);
            }
        }
    }

    return lista;
}
    // ---------------- INSTRUMENTOS / ETAPAS ----------------

    // Listar instrumentos disponibles (combos/filtros)
    public List<String> listarInstrumentosDisponibles() throws SQLException {
        List<String> lista = new ArrayList<>();
        String sql = "SELECT DISTINCT instrumento FROM clases WHERE estado = 'disponible' ORDER BY instrumento ASC";
        try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String inst = rs.getString("instrumento");
                if (inst != null && !inst.trim().isEmpty()) {
                    lista.add(inst.trim());
                }
            }
        }
        return lista;
    }

    public Map<String, Integer> listarInstrumentos() throws SQLException {
        Map<String, Integer> instrumentos = new HashMap<>();
        String sql = "SELECT nombre, cupo_maximo FROM instrumentos";
        try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                instrumentos.put(rs.getString("nombre"), rs.getInt("cupo_maximo"));
            }
        }
        return instrumentos;
    }

    public List<String> listarEtapas() throws SQLException {
        List<String> etapas = new ArrayList<>();
        String sql = "SELECT nombre FROM etapas_musicales";
        try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                etapas.add(rs.getString("nombre"));
            }
        }
        return etapas;
    }

    // Obtener instrumentos disponibles (panorama general)
    public List<String> obtenerInstrumentosDisponibles() {
        List<String> instrumentos = new ArrayList<>();
        String sql = "SELECT DISTINCT instrumento FROM clases WHERE estado = 'disponible' AND instrumento IS NOT NULL ORDER BY instrumento ASC";
        try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String instrumento = rs.getString("instrumento");
                if (instrumento != null && !instrumento.trim().isEmpty()) {
                    instrumentos.add(instrumento.trim());
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al obtener instrumentos disponibles: " + e.getMessage());
        }
        return instrumentos;
    }

    // ---------------- INSCRIPCIONES ----------------
    // Inscribir estudiante en clase
    public boolean inscribirEstudianteEnClase(int idEstudiante, int idClase) throws SQLException {
        String sqlInsert = "INSERT INTO inscripciones_clase (id_estudiante, id_clase, fecha_asignacion) VALUES (?, ?, CURRENT_DATE)";
        try (PreparedStatement ps = conn.prepareStatement(sqlInsert)) {
            ps.setInt(1, idEstudiante);
            ps.setInt(2, idClase);
            return ps.executeUpdate() > 0;
        }
    }

    // Verificar si un estudiante está inscrito en una clase
    public boolean estaInscrito(int idEstudiante, int idClase) throws SQLException {
        String sql = "SELECT COUNT(*) FROM inscripciones_clase WHERE id_estudiante = ? AND id_clase = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idEstudiante);
            ps.setInt(2, idClase);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    // Eliminar inscripción de estudiante
    public boolean eliminarInscripcion(int idEstudiante, int idClase) throws SQLException {
        String sqlDelete = "DELETE FROM inscripciones_clase WHERE id_estudiante = ? AND id_clase = ?";
        try (PreparedStatement ps = conn.prepareStatement(sqlDelete)) {
            ps.setInt(1, idEstudiante);
            ps.setInt(2, idClase);
            return ps.executeUpdate() > 0;
        }
    }

    // ---------------- INDICADORES ----------------
    // Contar estudiantes inscritos (global)
    public int contarEstudiantesInscritos() {
        String sql = "SELECT COUNT(*) AS total FROM inscripciones_clase";
        try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al contar estudiantes inscritos: " + e.getMessage());
        }
        return 0;
    }

    public int contarAsignadas(int estudianteId) {
        int total = 0;
        String sql = "SELECT COUNT(*) FROM inscripciones_clase WHERE id_estudiante = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, estudianteId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    total = rs.getInt(1);
                }
            }
        } catch (Exception e) {
            System.err.println("❌ Error al contar clases asignadas: " + e.getMessage());
        }
        return total;
    }

    // Contar etapas pendientes (si usas estado 'pendiente')
    public int contarEtapasPendientes() {
        String sql = "SELECT COUNT(*) AS total FROM clases WHERE estado = 'pendiente'";
        try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al contar etapas pendientes: " + e.getMessage());
        }
        return 0;
    }

    // Contar clases inscritas por estudiante (tabla inscripciones_clase)
    public int contarClasesInscritasPorEstudiante(int idEstudiante) {
        String sql = "SELECT COUNT(*) FROM inscripciones_clase WHERE id_estudiante = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idEstudiante);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al contar clases inscritas por estudiante: " + e.getMessage());
        }
        return 0;
    }

    public List<Map<String, Object>> obtenerTablasEnviadas() {
        List<Map<String, Object>> lista = new ArrayList<>();
        String sql = "SELECT c.id_clase, c.nombre_clase, c.instrumento, "
                + "       c.fecha_inicio, c.fecha_fin, c.estado "
                + "FROM clases c "
                + "WHERE c.enviada = 1"; // ejemplo de filtro

        try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Map<String, Object> fila = new HashMap<>();
                // ✅ Poblar el ID de la clase
                fila.put("id_clase", rs.getInt("id_clase"));
                fila.put("nombre_clase", rs.getString("nombre_clase"));
                fila.put("instrumento", rs.getString("instrumento"));
                fila.put("fecha_inicio", rs.getDate("fecha_inicio"));
                fila.put("fecha_fin", rs.getDate("fecha_fin"));
                fila.put("estado", rs.getString("estado"));
                lista.add(fila);
            }

        } catch (SQLException e) {
            System.err.println("❌ Error al obtener tablas enviadas: " + e.getMessage());
        }
        return lista;
    }

    // Contar asignaciones activas por docente (tabla clases_asignadas)
    public int contarAsignacionesActivasPorDocente(int idDocente) {
        String sql = "SELECT COUNT(*) FROM clases_asignadas WHERE id_docente = ? AND estado = 'activa'";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idDocente);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al contar asignaciones activas por docente: " + e.getMessage());
        }
        return 0;
    }
    
    public List<DocenteConClaseDTO> listarClasesDisponibles() {
    List<DocenteConClaseDTO> lista = new ArrayList<>();
    String sql = "SELECT c.id_clase, c.nombre_clase, c.instrumento, c.etapa, c.grupo, c.cupo, " +
                 "       c.fecha_inicio, c.fecha_fin, c.fecha_limite, c.estado, " +
                 "       c.dia_semana, c.hora_inicio, c.hora_fin, c.aula " +
                 "FROM clases c " +
                 "WHERE c.id_clase NOT IN (SELECT id_clase FROM clases_asignadas) " +
                 "AND c.estado = 'disponible'";

    try (PreparedStatement ps = conn.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
            DocenteConClaseDTO dto = new DocenteConClaseDTO();
            dto.setClaseId(rs.getInt("id_clase"));
            dto.setClaseNombre(rs.getString("nombre_clase"));
            dto.setInstrumento(rs.getString("instrumento"));
            dto.setEtapa(rs.getString("etapa"));
            dto.setGrupo(rs.getString("grupo"));
            dto.setCupo(rs.getInt("cupo"));
            dto.setFechaInicio(rs.getDate("fecha_inicio").toLocalDate());
            dto.setFechaFin(rs.getDate("fecha_fin").toLocalDate());
            dto.setFechaLimite(rs.getDate("fecha_limite").toLocalDate());
            dto.setEstadoClase(rs.getString("estado"));
            dto.setHorario(rs.getString("dia_semana") + " " +
                           rs.getString("hora_inicio") + " - " +
                           rs.getString("hora_fin"));
            dto.setAula(rs.getString("aula"));
            lista.add(dto);
        }
    } catch (SQLException e) {
        System.err.println("❌ Error al listar clases disponibles: " + e.getMessage());
    }
    return lista;
}

    // Consultar clases detalladas por docente (sin docente, estado, fecha límite, fecha creación)
public List<Map<String, String>> obtenerClasesDetalladasPorDocente(int idDocente) {
    List<Map<String, String>> lista = new ArrayList<>();

    String sql = "SELECT c.id_clase, c.nombre_clase, c.instrumento, c.etapa, c.grupo, c.cupo, " +
                 "       c.fecha_inicio, c.fecha_fin, c.aula, c.dia_semana, c.hora_inicio, c.hora_fin, " +
                 "       c.fecha_actualizacion, c.usuario_editor, ca.fecha_asignacion, " +
                 "       COUNT(i.id_estudiante) AS inscritos " +
                 "FROM clases c " +
                 "JOIN clases_asignadas ca ON c.id_clase = ca.id_clase " +
                 "LEFT JOIN inscripciones_clase i ON c.id_clase = i.id_clase " +
                 "WHERE ca.id_docente = ? " +
                 "GROUP BY c.id_clase, c.nombre_clase, c.instrumento, c.etapa, c.grupo, c.cupo, " +
                 "         c.fecha_inicio, c.fecha_fin, c.aula, c.dia_semana, c.hora_inicio, c.hora_fin, " +
                 "         c.fecha_actualizacion, c.usuario_editor, ca.fecha_asignacion";

    try (PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, idDocente);

        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Map<String, String> clase = new HashMap<>();

                // 🔑 Claves alineadas con dashboard.jsp
                clase.put("id_clase", String.valueOf(rs.getInt("id_clase")));
                clase.put("nombre", rs.getString("nombre_clase"));
                clase.put("aula", rs.getString("aula"));
                clase.put("dia", rs.getString("dia_semana"));
                clase.put("inicio", rs.getString("hora_inicio"));
                clase.put("fin", rs.getString("hora_fin"));

                // 📦 Información adicional
                clase.put("instrumento", rs.getString("instrumento"));
                clase.put("etapa", rs.getString("etapa"));
                clase.put("grupo", rs.getString("grupo"));
                clase.put("cupo", String.valueOf(rs.getInt("cupo")));
                clase.put("inscritos", String.valueOf(rs.getInt("inscritos")));

                // 🗓️ Fechas
                Timestamp inicioTs = rs.getTimestamp("fecha_inicio");
                clase.put("fecha_inicio", inicioTs != null ? inicioTs.toLocalDateTime().toString() : "");

                Timestamp finTs = rs.getTimestamp("fecha_fin");
                clase.put("fecha_fin", finTs != null ? finTs.toLocalDateTime().toString() : "");

                java.sql.Timestamp actualizacionTs = rs.getTimestamp("fecha_actualizacion");
                clase.put("fecha_actualizacion", actualizacionTs != null ? actualizacionTs.toLocalDateTime().toString() : "");

                clase.put("usuario_editor", rs.getString("usuario_editor"));
                clase.put("fecha_asignacion", rs.getString("fecha_asignacion"));

                lista.add(clase);
            }
        }
    } catch (SQLException e) {
        System.err.println("❌ Error al obtener clases detalladas por docente: " + e.getMessage());
    }

    return lista;
}
    
}
