/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mysymphony.proyecto_symphony1.dao;

/**
 * DAO institucional para gestión de notas.
 * Autor: Camila
 * Funciones: registrar, actualizar, consultar, contar y eliminar notas
 * Trazabilidad: mensajes de consola y uso de conexión institucional
 */
import com.mysymphony.proyecto_symphony1.modelo.Nota;
import com.mysymphony.proyecto_symphony1.util.Conexion;
import com.mysymphony.proyecto_symphony1.modelo.Estudiante;
import java.sql.*;
import java.util.*;

public class NotaDAO {

    private Connection conn;

    // Constructor que recibe la conexión
    public NotaDAO(Connection conn) {
        this.conn = conn;
}

    /**
     * Obtiene todas las notas registradas por un docente desde la tabla notas_clase,
     * incluyendo id_clase e id_tabla para trazabilidad institucional.
     */
    public List<Map<String, String>> obtenerNotasPorDocenteId(Integer idDocente) throws SQLException {
    List<Map<String, String>> notas = new ArrayList<>();

    String sql = "SELECT n.id_nota, n.id_estudiante, n.id_docente, n.id_tabla, n.id_clase, " +
                 "n.competencia, n.instrumento, n.etapa, n.nota, n.observacion, " +
                 "n.registrada_por, n.estado, n.fecha, n.fecha_registro, " +
                 "e.nombre AS estudiante " +
                 "FROM notas_clase n " +
                 "JOIN estudiantes e ON n.id_estudiante = e.id_estudiante " +
                 "WHERE n.id_docente = ? " +
                 "ORDER BY n.fecha_registro DESC";

    try (PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, idDocente);

        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Map<String, String> fila = new HashMap<>();
                fila.put("id_nota", String.valueOf(rs.getInt("id_nota")));
                fila.put("id_estudiante", String.valueOf(rs.getInt("id_estudiante")));
                fila.put("id_docente", String.valueOf(rs.getInt("id_docente")));
                fila.put("id_tabla", String.valueOf(rs.getInt("id_tabla")));
                fila.put("id_clase", String.valueOf(rs.getInt("id_clase")));
                fila.put("competencia", rs.getString("competencia"));
                fila.put("instrumento", rs.getString("instrumento"));
                fila.put("etapa", rs.getString("etapa"));
                fila.put("nota", rs.getString("nota"));
                fila.put("observacion", rs.getString("observacion"));
                fila.put("registrada_por", rs.getString("registrada_por"));
                fila.put("estado", rs.getString("estado"));
                fila.put("fecha", rs.getString("fecha"));
                fila.put("fecha_registro", rs.getString("fecha_registro"));
                fila.put("estudiante", rs.getString("estudiante"));
                notas.add(fila);
            }
        }
    }

    return notas;
}

    // Obtener notas por clase con todos los campos de la tabla notas_clase
    public List<Nota> obtenerNotasPorClase(int claseId) {
    List<Nota> lista = new ArrayList<>();
    String sql = "SELECT n.id_nota, n.id_estudiante, n.id_docente, n.id_tabla, n.id_clase, " +
                 "       CONCAT(u.nombre, ' ', u.apellido) AS nombre_estudiante, " +
                 "       n.competencia, n.instrumento, n.etapa, n.nota, n.observacion, " +
                 "       n.registrada_por, n.estado, n.fecha, n.fecha_registro " +
                 "FROM notas_clase n " +
                 "JOIN estudiantes e ON n.id_estudiante = e.id_estudiante " +
                 "JOIN usuarios u ON e.id_usuario = u.id_usuario " +
                 "WHERE n.id_clase = ? " +
                 "ORDER BY n.fecha DESC";

    try (PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, claseId);

        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Nota nota = new Nota();

                // IDs
                nota.setId(rs.getInt("id_nota"));
                nota.setIdEstudiante(rs.getInt("id_estudiante"));
                nota.setIdDocente(rs.getInt("id_docente"));
                nota.setIdClase(rs.getInt("id_clase"));

                // id_tabla con control de null
                int idTabla = rs.getInt("id_tabla");
                if (!rs.wasNull()) {
                    nota.setIdTabla(idTabla); // asegúrate de tener este setter en tu modelo Nota
                }

                // Datos descriptivos
                nota.setEstudiante(rs.getString("nombre_estudiante"));
                nota.setCompetencia(rs.getString("competencia"));
                nota.setInstrumento(rs.getString("instrumento"));
                nota.setEtapa(rs.getString("etapa"));
                nota.setNota(rs.getDouble("nota"));
                nota.setObservacion(rs.getString("observacion"));
                nota.setEstado(rs.getString("estado"));
                nota.setRegistradaPor(rs.getString("registrada_por"));

                // Fechas
                java.sql.Date sqlFecha = rs.getDate("fecha");
                if (sqlFecha != null) {
                    nota.setFecha(sqlFecha.toLocalDate());
                }

                java.sql.Timestamp tsRegistro = rs.getTimestamp("fecha_registro");
                if (tsRegistro != null) {
                    nota.setFechaRegistro(tsRegistro);
                }

                lista.add(nota);
            }
        }

        System.out.println("✔ Notas obtenidas correctamente para claseId=" + claseId + " → total=" + lista.size());
    } catch (SQLException e) {
        System.err.println("❌ Error al obtener notas por clase (claseId=" + claseId + "): " + e.getMessage());
    }
    return lista;
}
    // Contar notas registradas por docente
public int contarNotasRegistradas(int docenteId) {
    String sql = "SELECT COUNT(*) AS total " +
                 "FROM notas_clase " +
                 "WHERE id_docente = ? AND estado = 'registrada'";
    try (PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, docenteId);
        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                int total = rs.getInt("total");
                System.out.println("✔ Total notas registradas por docenteId=" + docenteId + " → " + total);
                return total;
            }
        }
    } catch (SQLException e) {
        System.err.println("❌ Error al contar notas registradas: " + e.getMessage());
    }
    return 0;
}

    // Actualizar nota por estudiante, instrumento y etapa
   public boolean actualizarNotaPorId(int idNota,
                                   String competencia,
                                   double notaValor,
                                   String observacion,
                                   java.time.LocalDate fecha) {
    String sql = "UPDATE notas_clase " +
                 "SET competencia = ?, nota = ?, observacion = ?, fecha = ? " +
                 "WHERE id_nota = ?";

    try (PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setString(1, competencia);
        ps.setDouble(2, notaValor);
        ps.setString(3, observacion);
        ps.setDate(4, java.sql.Date.valueOf(fecha));
        ps.setInt(5, idNota);

        int filas = ps.executeUpdate();
        if (filas > 0) {
            System.out.println("✔ Nota actualizada por id_nota=" + idNota);
            return true;
        } else {
            System.out.println("⚠️ No se encontró id_nota=" + idNota + " para actualizar");
            return false;
        }
    } catch (SQLException e) {
        System.err.println("❌ Error al actualizar nota por id: " + e.getMessage());
        return false;
    }
}
    
public boolean registrarNotaPorClase(int estudianteId, int claseId,
                                     String competencia, double nota,
                                     String observacion, String fecha,
                                     int docenteId, String instrumento,
                                     String etapa, int tablaId, String registradaPor) {
    if (fecha == null || fecha.trim().isEmpty()) {
        System.err.println("⚠️ Fecha inválida: no se puede registrar nota sin fecha.");
        return false;
    }

    // Validar formato de fecha (YYYY-MM-DD)
    java.sql.Date fechaSql;
    try {
        fechaSql = java.sql.Date.valueOf(fecha);
    } catch (IllegalArgumentException ex) {
        System.err.println("⚠️ Formato de fecha inválido: " + fecha);
        return false;
    }

    // Validar existencia del estudiante
    String validarSql = "SELECT 1 FROM estudiantes WHERE id_estudiante = ?";
    try (PreparedStatement psVal = conn.prepareStatement(validarSql)) {
        psVal.setInt(1, estudianteId);
        try (ResultSet rs = psVal.executeQuery()) {
            if (!rs.next()) {
                System.err.println("⚠️ El estudiante con id=" + estudianteId + " no existe.");
                return false;
            }
        }
    } catch (SQLException e) {
        System.err.println("❌ Error al validar estudiante: " + e.getMessage());
        return false;
    }

    // Insertar nota con metadatos completos
    String sql = "INSERT INTO notas_clase " +
                 "(id_estudiante, id_docente, id_tabla, id_clase, competencia, instrumento, etapa, nota, observacion, estado, fecha, fecha_registro, registrada_por) " +
                 "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, 'registrada', ?, NOW(), ?)";

    try (PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, estudianteId);
        ps.setInt(2, docenteId);
        ps.setInt(3, tablaId);          // vínculo con la tabla guardada
        ps.setInt(4, claseId);
        ps.setString(5, competencia);
        ps.setString(6, instrumento);
        ps.setString(7, etapa);
        ps.setDouble(8, nota);
        ps.setString(9, observacion);
        ps.setDate(10, fechaSql);       // fecha validada y convertida
        ps.setString(11, registradaPor); // docente en sesión
        int filas = ps.executeUpdate();
        if (filas > 0) {
            System.out.println("✔ Nota registrada correctamente en BD.");
            return true;
        } else {
            System.err.println("⚠️ El INSERT no afectó filas.");
            return false;
        }
    } catch (SQLException e) {
        System.err.println("❌ Error al registrar nota: " + e.getMessage());
        return false;
    }
}

public void completarCamposNotasClase() {
    String sql = "{CALL completar_campos_notas_clase()}";
    try (Connection conn = Conexion.getConnection();
         CallableStatement cs = conn.prepareCall(sql)) {

        cs.execute();
        System.out.println("✔ Procedimiento completar_campos_notas_clase ejecutado correctamente.");

        // 📖 Registro en bitácora institucional
        BitacoraDAO bitacoraDAO = new BitacoraDAO(conn);
        bitacoraDAO.registrarAccion("Procedimiento completar_campos_notas_clase ejecutado",
                                    "sistema", "interno", "Notas");

    } catch (SQLException e) {
        System.err.println("❌ Error al ejecutar procedimiento completar_campos_notas_clase: " + e.getMessage());
    }
}

    public boolean actualizarNotaPorClase(int notaId,
                                      double nota,
                                      String observacion,
                                      String fecha) {
    // 🔍 Validar que la fecha no sea nula
    if (fecha == null || fecha.trim().isEmpty()) {
        System.err.println("⚠️ Fecha inválida: no se puede actualizar con valor nulo.");
        return false;
    }

    String sql = "UPDATE notas_clase " +
                 "SET nota = ?, observacion = ?, fecha = ? " +
                 "WHERE id_nota = ?";

    try (PreparedStatement ps = conn.prepareStatement(sql)) {
        // Parámetros de actualización
        ps.setDouble(1, nota);
        ps.setString(2, (observacion != null && !observacion.trim().isEmpty()) ? observacion : null);
        ps.setString(3, fecha); // formato 'YYYY-MM-DD'
        ps.setInt(4, notaId);

        int filas = ps.executeUpdate();

        if (filas > 0) {
            System.out.println("✔ Nota actualizada correctamente: " +
                               "ID=" + notaId +
                               ", nota=" + nota +
                               ", fecha=" + fecha);
            return true;
        } else {
            System.out.println("⚠️ No se encontró nota con ID=" + notaId);
            return false;
        }

    } catch (SQLException e) {
        System.err.println("❌ Error al actualizar nota por clase (ID=" + notaId + "): " + e.getMessage());
        return false;
    }
}
       

    // Contar todas las notas
    public int contarTodas() {
        String sql = "SELECT COUNT(*) FROM notas_clase";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            System.err.println("❌ Error al contar notas: " + e.getMessage());
        }
        return 0;
    }

    // Contar notas por estudiante
    public int contarPorEstudiante(int estudianteId) {
        String sql = "SELECT COUNT(*) FROM notas_clase WHERE id_estudiante = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, estudianteId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            System.err.println("❌ Error al contar notas por estudiante: " + e.getMessage());
        }
        return 0;
    }

    // Obtener nota por id
    public Map<String, String> obtenerNotaPorId(int notaId) {
        Map<String, String> nota = new HashMap<>();
        String sql = "SELECT n.id_nota, n.id_estudiante, e.nombre AS nombre_estudiante, " +
                     "n.id_clase, c.nombre_clase, n.etapa, n.instrumento, n.nota, n.observacion, n.fecha, " +
                     "u.nombre AS nombre_docente " +
                     "FROM notas_clase n " +
                     "JOIN estudiantes e ON n.id_estudiante = e.id_estudiante " +
                     "JOIN clases c ON n.id_clase = c.id_clase " +
                     "JOIN usuarios u ON n.id_docente = u.id_usuario " +
                     "WHERE n.id_nota = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, notaId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    nota.put("id", String.valueOf(rs.getInt("id_nota")));
                    nota.put("estudiante_id", String.valueOf(rs.getInt("id_estudiante")));
                    nota.put("nombre_estudiante", rs.getString("nombre_estudiante"));
                    nota.put("id_clase", String.valueOf(rs.getInt("id_clase")));
                    nota.put("nombre_clase", rs.getString("nombre_clase"));
                    nota.put("etapa", rs.getString("etapa"));
                    nota.put("instrumento", rs.getString("instrumento"));
                    nota.put("nota", rs.getString("nota"));
                    nota.put("observacion", rs.getString("observacion"));
                    nota.put("fecha", rs.getString("fecha"));
                    nota.put("nombre_docente", rs.getString("nombre_docente"));
                }
            }
        } catch (Exception e) {
            System.err.println("❌ Error al obtener nota por ID: " + e.getMessage());
        }
        return nota;
    }
    
    // Listar notas registradas por clase y tabla
public List<Map<String, Object>> listarNotasPorClase(int claseId) {
    List<Map<String, Object>> notas = new ArrayList<>();
    String sql = "SELECT n.id_nota, u.nombre, u.apellido, " +
                 "       n.competencia, n.etapa, n.instrumento, n.nota, " +
                 "       n.observacion, n.estado, n.fecha, n.registrada_por, n.id_tabla " +
                 "FROM notas_clase n " +
                 "JOIN estudiantes e ON n.id_estudiante = e.id_estudiante " +
                 "JOIN usuarios u ON e.id_usuario = u.id_usuario " +
                 "WHERE n.id_clase = ? " +
                 "ORDER BY u.apellido, u.nombre";
    try (PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, claseId);
        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Map<String, Object> nota = new HashMap<>();
                nota.put("id", rs.getInt("id_nota"));
                nota.put("estudiante", rs.getString("nombre") + " " + rs.getString("apellido"));
                nota.put("competencia", rs.getString("competencia"));
                nota.put("etapa", rs.getString("etapa"));
                nota.put("instrumento", rs.getString("instrumento"));
                nota.put("nota", rs.getDouble("nota"));
                nota.put("observacion", rs.getString("observacion"));
                nota.put("estado", rs.getString("estado"));
                nota.put("fecha", rs.getDate("fecha"));
                nota.put("registrada_por", rs.getString("registrada_por"));
                nota.put("id_tabla", rs.getInt("id_tabla"));
                notas.add(nota);
            }
        }
    } catch (SQLException e) {
        System.err.println("❌ Error al listar notas por clase: " + e.getMessage());
    }
    return notas;
}

        // Contar certificados emitidos
    public int contarCertificadosEmitidos(int estudianteId) {
        String sql = "SELECT COUNT(*) FROM certificados WHERE id_estudiante = ? AND estado = 'Disponible'";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, estudianteId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            System.err.println("❌ Error al contar certificados emitidos: " + e.getMessage());
        }
        return 0;
    }

    public boolean existeNotaPorClase(int claseId, int estudianteId, String competencia) {
    String sql = "SELECT COUNT(*) FROM notas_clase " +
                 "WHERE id_clase = ? AND id_estudiante = ? AND competencia = ?";
    try (PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, claseId);
        ps.setInt(2, estudianteId);
        ps.setString(3, competencia);

        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                int count = rs.getInt(1);
                boolean existe = count > 0;
                System.out.println("🔎 Validación duplicado → claseId=" + claseId +
                                   ", estudianteId=" + estudianteId +
                                   ", competencia=" + competencia +
                                   " → existe=" + existe);
                return existe;
            }
            return false;
        }
    } catch (SQLException e) {
        System.err.println("❌ Error al verificar existencia de nota (claseId=" + claseId +
                           ", estudianteId=" + estudianteId +
                           ", competencia=" + competencia + "): " + e.getMessage());
        return false;
    }
}

    /**
 * 📋 Listar estudiantes inscritos en una clase
 * @param claseId identificador de la clase
 * @return lista de objetos Estudiante
 */
public List<Estudiante> obtenerEstudiantesPorClase(int claseId) {
    List<Estudiante> lista = new ArrayList<>();

    String sql = "SELECT i.id_inscripcion, e.id_estudiante, u.id_usuario, u.nombre, u.apellido, u.correo, " +
                 "       e.instrumento, e.direccion, e.telefono, " +
                 "       e.etapa_pedagogica, e.fecha_ingreso, e.estado, " +
                 "       e.nivel_tecnico, e.progreso, e.observaciones " +
                 "FROM inscripciones_clase i " +
                 "JOIN estudiantes e ON i.id_estudiante = e.id_estudiante " +
                 "JOIN usuarios u ON e.id_usuario = u.id_usuario " +
                 "WHERE i.id_clase = ?";

    try (PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, claseId);

        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Estudiante est = new Estudiante();
                est.setIdEstudiante(rs.getInt("id_estudiante"));   // ✅ ahora correcto
                est.setIdUsuario(rs.getInt("id_usuario"));         // opcional si lo tienes en el modelo
                est.setNombre(rs.getString("nombre"));
                est.setApellido(rs.getString("apellido"));
                est.setCorreo(rs.getString("correo"));
                est.setInstrumento(rs.getString("instrumento"));
                est.setDireccion(rs.getString("direccion"));
                est.setTelefono(rs.getString("telefono"));
                est.setEtapaPedagogica(rs.getString("etapa_pedagogica"));

                java.sql.Date sqlFechaIngreso = rs.getDate("fecha_ingreso");
                if (sqlFechaIngreso != null) {
                    est.setFechaIngreso(sqlFechaIngreso.toLocalDate());
                }

                est.setEstado(rs.getString("estado"));
                est.setNivelTecnico(rs.getString("nivel_tecnico"));
                est.setProgreso(rs.getString("progreso"));
                est.setObservaciones(rs.getString("observaciones"));

                lista.add(est);
            }
        }

        System.out.println("📌 Estudiantes encontrados en clase " + claseId + ": " + lista.size());

    } catch (SQLException e) {
        System.err.println("❌ Error al obtener estudiantes por clase: " + e.getMessage());
    }

    return lista;
}

    // Contar todas las notas que pertenecen a clases asignadas
    public int contarNotasDeClasesAsignadas() {
        String sql = "SELECT COUNT(*) " +
                     "FROM notas_clase n " +
                     "JOIN clases_asignadas ca ON n.id_clase = ca.id_clase " +
                     "WHERE ca.id_docente IS NOT NULL";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al contar notas de clases asignadas: " + e.getMessage());
        }
        return 0;
    }

    // Obtener notas por docenteId
public List<Map<String, String>> obtenerNotasPorDocenteId(int idDocente) {
    List<Map<String, String>> notas = new ArrayList<>();

    String sql = "SELECT n.id_nota, u.nombre AS estudiante, c.nombre_clase, " +
                 "       n.etapa, n.instrumento, n.nota, n.observacion, n.fecha " +
                 "FROM notas_clase n " +
                 "INNER JOIN estudiantes e ON n.id_estudiante = e.id_estudiante " +
                 "INNER JOIN usuarios u ON e.id_usuario = u.id_usuario " +
                 "INNER JOIN clases c ON n.id_clase = c.id_clase " +
                 "INNER JOIN clases_asignadas ca ON c.id_clase = ca.id_clase " +
                 "WHERE ca.id_docente = ? " +
                 "ORDER BY n.fecha DESC";

    try (PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, idDocente);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Map<String, String> fila = new HashMap<>();
            fila.put("id", rs.getString("id_nota"));
            fila.put("estudiante", rs.getString("estudiante"));
            fila.put("clase", rs.getString("nombre_clase"));
            fila.put("etapa", rs.getString("etapa"));
            fila.put("instrumento", rs.getString("instrumento"));
            fila.put("nota", rs.getString("nota"));
            fila.put("observacion", rs.getString("observacion"));
            fila.put("fecha", rs.getString("fecha"));
            notas.add(fila);
        }
    } catch (SQLException e) {
        System.err.println("❌ Error al obtener notas por docenteId: " + e.getMessage());
    }

    return notas;
}

   // Eliminar nota por clase
public boolean eliminarNotaPorClase(int notaId) {
    // 🔍 Validar que la nota exista antes de eliminar
    String validarSql = "SELECT 1 FROM notas_clase WHERE id_nota = ?";
    try (PreparedStatement psVal = conn.prepareStatement(validarSql)) {
        psVal.setInt(1, notaId);
        try (ResultSet rs = psVal.executeQuery()) {
            if (!rs.next()) {
                System.err.println("⚠️ No se encontró nota con ID=" + notaId + " para eliminar.");
                return false;
            }
        }
    } catch (SQLException e) {
        System.err.println("❌ Error al validar existencia de nota: " + e.getMessage());
        return false;
    }

    // 🗑️ Eliminar nota
    String sql = "DELETE FROM notas_clase WHERE id_nota = ?";
    try (PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, notaId);
        int filas = ps.executeUpdate();
        if (filas > 0) {
            System.out.println("✔ Nota eliminada correctamente: ID=" + notaId);
            return true;
        } else {
            System.out.println("⚠️ No se pudo eliminar la nota con ID=" + notaId);
            return false;
        }
    } catch (SQLException e) {
        System.err.println("❌ Error al eliminar nota por clase: " + e.getMessage());
        return false;
    }
}

// Obtener notas filtradas por docente, instrumento y etapa
public List<Map<String, String>> obtenerNotasFiltradas(int docenteId, String instrumento, String etapa) {
    List<Map<String, String>> lista = new ArrayList<>();
    StringBuilder sql = new StringBuilder(
        "SELECT u.nombre AS estudiante, n.instrumento, n.etapa, n.nota, n.observacion, n.fecha " +
        "FROM notas_clase n " +
        "JOIN estudiantes e ON n.id_estudiante = e.id_estudiante " +
        "JOIN usuarios u ON e.id_usuario = u.id_usuario " +
        "WHERE n.id_docente = ?"
    );

    if (instrumento != null && !instrumento.isEmpty()) {
        sql.append(" AND n.instrumento = ?");
    }
    if (etapa != null && !etapa.isEmpty()) {
        sql.append(" AND n.etapa = ?");
    }
    sql.append(" ORDER BY n.fecha DESC");

    try (PreparedStatement ps = conn.prepareStatement(sql.toString())) {
        ps.setInt(1, docenteId);
        int index = 2;
        if (instrumento != null && !instrumento.isEmpty()) {
            ps.setString(index++, instrumento);
        }
        if (etapa != null && !etapa.isEmpty()) {
            ps.setString(index, etapa);
        }

        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Map<String, String> fila = new HashMap<>();
            fila.put("estudiante", rs.getString("estudiante"));
            fila.put("instrumento", rs.getString("instrumento"));
            fila.put("etapa", rs.getString("etapa"));
            fila.put("nota", rs.getString("nota"));
            fila.put("observacion", rs.getString("observacion"));
            fila.put("fecha", rs.getString("fecha"));
            lista.add(fila);
        }
    } catch (SQLException e) {
        System.err.println("❌ Error al filtrar notas: " + e.getMessage());
    }
    return lista;
}
    // Obtener notas por estudiante
public List<Map<String, String>> obtenerNotasPorEstudiante(int estudianteId) {
    List<Map<String, String>> lista = new ArrayList<>();
    String sql = "SELECT instrumento, etapa, nota, observacion, fecha " +
                 "FROM notas_clase " +
                 "WHERE id_estudiante = ? ORDER BY fecha DESC";
    try (PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, estudianteId);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Map<String, String> fila = new HashMap<>();
            fila.put("instrumento", rs.getString("instrumento"));
            fila.put("etapa", rs.getString("etapa"));
            fila.put("nota", rs.getString("nota"));
            fila.put("observacion", rs.getString("observacion"));
            fila.put("fecha", rs.getString("fecha"));
            lista.add(fila);
        }
    } catch (SQLException e) {
        System.err.println("❌ Error al obtener notas del estudiante: " + e.getMessage());
    }
    return lista;
}

// Validar existencia de estudiante
public boolean existeEstudiante(int estudianteId) {
    String sql = "SELECT 1 FROM estudiantes WHERE id_estudiante = ?";
    try (PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, estudianteId);
        try (ResultSet rs = ps.executeQuery()) {
            boolean existe = rs.next();
            System.out.println("🔎 Validación estudiante → id=" + estudianteId + " → existe=" + existe);
            return existe;
        }
    } catch (SQLException e) {
        System.err.println("❌ Error al verificar existencia de estudiante (id=" + estudianteId + "): " + e.getMessage());
        return false;
    }
}
public boolean existenNotasPorTabla(int tablaId) {
    String sql = "SELECT COUNT(*) FROM notas_clase WHERE id_tabla = ?";
    try (PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, tablaId);
        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
    } catch (SQLException e) {
        System.err.println("❌ Error al verificar notas por tabla: " + e.getMessage());
    }
    return false;
}

public int contarNotasPorTabla(int tablaId) {
    String sql = "SELECT COUNT(*) FROM notas_clase WHERE id_tabla = ?";
    try (PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, tablaId);
        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
    } catch (SQLException e) {
        System.err.println("❌ Error al contar notas por tabla: " + e.getMessage());
    }
    return 0;
}

}
