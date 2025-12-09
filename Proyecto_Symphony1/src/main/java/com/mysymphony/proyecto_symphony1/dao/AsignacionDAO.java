/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.mysymphony.proyecto_symphony1.dao;

/**
 * DAO institucional para gestión de asignaciones.
 * Autor: Camila
 * Funciones: registrar, actualizar, consultar, contar y eliminar asignaciones
 * Trazabilidad: mensajes de consola y uso de conexión institucional
 */

import com.mysymphony.proyecto_symphony1.dto.DocenteConClaseDTO;

import java.sql.*;
import java.util.*;
import java.sql.Date;

public class AsignacionDAO {

    private Connection conn;

    // Constructor que recibe la conexión
    public AsignacionDAO(Connection conn) {
        this.conn = conn;
    }
    
    
    // ✅ Listar clases asignadas a un docente
public List<DocenteConClaseDTO> listarClasesPorDocente(int idDocente) {
    List<DocenteConClaseDTO> lista = new ArrayList<>();
    String sql = "SELECT ca.id_asignacion, ca.id_docente, d.nombre AS docente_nombre, " +
                 "ca.id_clase, c.nombre_clase, c.instrumento, c.etapa, c.grupo, c.cupo, " +
                 "ca.horario, ca.aula, ca.dia, ca.fecha_asignacion, " +
                 "ca.estado AS estado_asignacion, c.estado AS estado_clase " +
                 "FROM clases_asignadas ca " +
                 "INNER JOIN clases c ON ca.id_clase = c.id_clase " +
                 "INNER JOIN docentes d ON ca.id_docente = d.id_docente " +
                 "WHERE ca.id_docente = ?";

    try (PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, idDocente);
        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                DocenteConClaseDTO dto = new DocenteConClaseDTO();
                // Datos del docente
                dto.setDocenteId(rs.getInt("id_docente"));
                dto.setDocenteNombre(rs.getString("docente_nombre"));

                // Datos de la clase
                dto.setClaseId(rs.getInt("id_clase"));
                dto.setClaseNombre(rs.getString("nombre_clase"));
                dto.setInstrumento(rs.getString("instrumento"));
                dto.setEtapa(rs.getString("etapa"));
                dto.setGrupo(rs.getString("grupo"));
                dto.setCupo(rs.getInt("cupo"));

                // Datos de la asignación
                dto.setHorario(rs.getString("horario"));
                dto.setAula(rs.getString("aula"));
                dto.setDia(rs.getString("dia"));
                dto.setEstadoAsignacion(rs.getString("estado_asignacion"));

                // Estado de la clase
                dto.setEstadoClase(rs.getString("estado_clase"));

                lista.add(dto);
            }
        }
    } catch (SQLException e) {
        System.err.println("❌ Error en listarClasesPorDocente: " + e.getMessage());
    }
    return lista;
}
    /**
 * 📅 Listar horarios de las clases asignadas a un docente
 * @param idDocente identificador del docente
 * @return lista de mapas con datos de clase y horario
 */
    public List<Map<String, Object>> listarHorariosPorDocente(int idDocente) {
    List<Map<String, Object>> lista = new ArrayList<>();

    String sql = "SELECT c.id_clase, c.nombre_clase, c.instrumento, c.etapa, c.grupo, c.cupo, " +
                 "       (SELECT COUNT(*) FROM inscripciones_clase ic WHERE ic.id_clase = c.id_clase) AS inscritos, " +
                 "       h.dia_semana, h.fecha, h.hora_inicio, h.hora_fin, h.aula " +
                 "FROM clases c " +
                 "JOIN clases_asignadas ca ON c.id_clase = ca.id_clase " +
                 "JOIN horarios_clase h ON c.id_clase = h.id_clase " +   // ✅ columna correcta
                 "WHERE ca.id_docente = ? " +
                 "ORDER BY h.fecha, h.hora_inicio";

    try (PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, idDocente);

        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Map<String, Object> horario = new HashMap<>();
                horario.put("id_clase", rs.getInt("id_clase"));
                horario.put("nombre_clase", rs.getString("nombre_clase"));
                horario.put("instrumento", rs.getString("instrumento"));
                horario.put("etapa", rs.getString("etapa"));
                horario.put("grupo", rs.getString("grupo"));
                horario.put("cupo", rs.getInt("cupo"));
                horario.put("inscritos", rs.getInt("inscritos"));
                horario.put("dia", rs.getString("dia_semana"));

                // ✅ Guardar como Date para que JSTL lo formatee
                Date fechaSql = rs.getDate("fecha");
                horario.put("fecha", fechaSql);

                horario.put("inicio", rs.getString("hora_inicio"));
                horario.put("fin", rs.getString("hora_fin"));
                horario.put("aula", rs.getString("aula"));

                lista.add(horario);
            }
        }
    } catch (SQLException e) {
        System.err.println("❌ Error al listar horarios por docente: " + e.getMessage());
    }

    return lista;
}

    

    // ✅ Listar asignaciones institucionales (para administrador)
    public List<Map<String, String>> listarAsignacionesInstitucionales() {
        List<Map<String, String>> lista = new ArrayList<>();
        String sql = "SELECT ca.id_asignacion, d.nombre AS docente, c.nombre_clase AS clase, " +
                     "ca.horario, ca.aula, ca.dia, ca.estado, ca.registrada_por, ca.tipo_registro " +
                     "FROM clases_asignadas ca " +
                     "INNER JOIN clases c ON ca.id_clase = c.id_clase " +
                     "INNER JOIN docentes d ON ca.id_docente = d.id_docente";

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Map<String, String> fila = new HashMap<>();
                fila.put("id_asignacion", String.valueOf(rs.getInt("id_asignacion")));
                fila.put("docente", rs.getString("docente"));
                fila.put("clase", rs.getString("clase"));
                fila.put("horario", rs.getString("horario"));
                fila.put("aula", rs.getString("aula"));
                fila.put("dia", rs.getString("dia"));
                fila.put("estado", rs.getString("estado"));
                fila.put("registrada_por", rs.getString("registrada_por"));
                fila.put("tipo_registro", rs.getString("tipo_registro"));
                lista.add(fila);
            }
        } catch (SQLException e) {
            System.err.println("❌ Error en listarAsignacionesInstitucionales: " + e.getMessage());
        }
        return lista;
    }

    // ✅ Listar estudiantes asignados (para administrador)
    public List<Map<String, String>> listarEstudiantesAsignados() {
        List<Map<String, String>> lista = new ArrayList<>();
        String sql = "SELECT ea.id_estudiante, e.nombre AS estudiante, c.nombre_clase AS clase " +
                     "FROM estudiantes_asignados ea " +
                     "INNER JOIN estudiantes e ON ea.id_estudiante = e.id_estudiante " +
                     "INNER JOIN clases c ON ea.id_clase = c.id_clase";

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Map<String, String> fila = new HashMap<>();
                fila.put("id_estudiante", String.valueOf(rs.getInt("id_estudiante")));
                fila.put("estudiante", rs.getString("estudiante"));
                fila.put("clase", rs.getString("clase"));
                lista.add(fila);
            }
        } catch (SQLException e) {
            System.err.println("❌ Error en listarEstudiantesAsignados: " + e.getMessage());
        }
        return lista;
    }

    // ✅ Listar asignaciones por estudiante
    public List<Map<String, String>> listarAsignacionesPorEstudiante(int idEstudiante) {
        List<Map<String, String>> lista = new ArrayList<>();
        String sql = "SELECT c.nombre_clase, c.instrumento, c.etapa, ca.horario, ca.aula, ca.dia, ca.estado " +
                     "FROM clases_asignadas ca " +
                     "INNER JOIN clases c ON ca.id_clase = c.id_clase " +
                     "INNER JOIN estudiantes_asignados ea ON ca.id_clase = ea.id_clase " +
                     "WHERE ea.id_estudiante = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idEstudiante);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Map<String, String> fila = new HashMap<>();
                    fila.put("clase", rs.getString("nombre_clase"));
                    fila.put("instrumento", rs.getString("instrumento"));
                    fila.put("etapa", rs.getString("etapa"));
                    fila.put("horario", rs.getString("horario"));
                    fila.put("aula", rs.getString("aula"));
                    fila.put("dia", rs.getString("dia"));
                    fila.put("estado", rs.getString("estado"));
                    lista.add(fila);
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Error en listarAsignacionesPorEstudiante: " + e.getMessage());
        }
        return lista;
    }

    // ✅ Asignar estudiante a clase
    public void asignarEstudianteAClase(int idEstudiante, int idClase, String usuario, String rol) {
        String sql = "INSERT INTO estudiantes_asignados (id_estudiante, id_clase, registrada_por, tipo_registro) " +
                     "VALUES (?, ?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idEstudiante);
            ps.setInt(2, idClase);
            ps.setString(3, usuario);
            ps.setString(4, rol);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("❌ Error en asignarEstudianteAClase: " + e.getMessage());
        }
    }

    
    public boolean asignarDocenteAClase(int claseId, int docenteId, String usuario, String rol) {
    boolean asignado = false;

    String sql = "UPDATE clases SET id_docente = ?, estado = 'asignada', fecha_asignacion = CURRENT_DATE " +
                 "WHERE id_clase = ? AND (id_docente IS NULL OR id_docente = 0)";

    try (PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, docenteId);
        ps.setInt(2, claseId);

        int filas = ps.executeUpdate();
        if (filas > 0) {
            asignado = true;

            // 📌 Registrar en bitácora institucional
            BitacoraDAO bitacoraDAO = new BitacoraDAO(conn);
            bitacoraDAO.registrarAccion(
                "Administrador asignó docente ID " + docenteId + " a la clase ID " + claseId,
                usuario, rol, "Asignaciones"
            );

            // 📌 Registrar en auditoría técnica
            Map<String, String> registro = new HashMap<>();
            registro.put("usuario", usuario);
            registro.put("rol", rol);
            registro.put("modulo", "Asignaciones");
            registro.put("accion", "Asignación de docente ID " + docenteId + " a clase ID " + claseId);
            new AuditoriaDAO(conn).registrarAccion(registro);
        }

    } catch (SQLException e) {
        System.err.println("❌ Error al asignar docente a clase: " + e.getMessage());
    }

    return asignado;
}

    // ✅ Asignar estudiante a docente con validación de duplicados y fecha
    public boolean asignarEstudiante(int idDocente, int idEstudiante, String instrumento) {
        String verificar = "SELECT COUNT(*) FROM asignaciones_docente WHERE id_docente = ? AND id_estudiante = ?";
        String insertar = "INSERT INTO clases_asignadas (id_docente, id_estudiante, instrumento, fecha_asignacion) VALUES (?, ?, ?, CURRENT_DATE)";
        try (PreparedStatement psVerificar = conn.prepareStatement(verificar)) {
            psVerificar.setInt(1, idDocente);
            psVerificar.setInt(2, idEstudiante);
            ResultSet rs = psVerificar.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) return false;
            try (PreparedStatement psInsertar = conn.prepareStatement(insertar)) {
                psInsertar.setInt(1, idDocente);
                psInsertar.setInt(2, idEstudiante);
                psInsertar.setString(3, instrumento);
                psInsertar.executeUpdate();
                return true;
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al asignar estudiante: " + e.getMessage());
            return false;
        }
    }
    
    

    // ✅ Actualizar instrumento asignado
    public boolean actualizarInstrumentoAsignado(int idAsignacion, String nuevoInstrumento) {
        String sql = "UPDATE clases_asignadas SET instrumento = ? WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nuevoInstrumento);
            ps.setInt(2, idAsignacion);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("❌ Error al actualizar instrumento: " + e.getMessage());
            return false;
        }
    }
    
    public List<Map<String, String>> obtenerClasesPorDocente(int idDocente) {
    List<Map<String, String>> lista = new ArrayList<>();
    String sql = "SELECT c.id_clase, c.nombre_clase, c.instrumento, c.etapa, c.grupo, c.cupo, c.fecha_limite " +
                 "FROM clases c " +
                 "JOIN clases_asignadas ca ON c.id_clase = ca.id_clase " +
                 "WHERE ca.id_docente = ?";
    try (PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, idDocente);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Map<String, String> fila = new HashMap<>();
            fila.put("id_clase", rs.getString("id_clase"));
            fila.put("nombre_clase", rs.getString("nombre_clase"));
            fila.put("instrumento", rs.getString("instrumento"));
            fila.put("etapa", rs.getString("etapa"));
            fila.put("grupo", rs.getString("grupo"));
            fila.put("cupo", rs.getString("cupo"));
            fila.put("fecha_limite", rs.getString("fecha_limite"));
            lista.add(fila);
        }
    } catch (SQLException e) {
        System.err.println("❌ Error al obtener clases por docente: " + e.getMessage());
    }
    return lista;
}

    // ✅ Obtener estudiantes asignados por docente, instrumento y clase
    public List<Map<String, String>> obtenerEstudiantesAsignados(int docenteId, String instrumento, int claseId) {
        List<Map<String, String>> lista = new ArrayList<>();
        String sql = "SELECT e.id AS id_estudiante, e.nombre AS estudiante " +
                     "FROM clases_asignadas a " +
                     "JOIN usuarios e ON a.id_estudiante = e.id " +
                     "WHERE a.id_docente = ? AND a.instrumento = ? AND a.id_clase = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, docenteId);
            ps.setString(2, instrumento);
            ps.setInt(3, claseId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Map<String, String> fila = new HashMap<>();
                fila.put("id_estudiante", rs.getString("id_estudiante"));
                fila.put("estudiante", rs.getString("estudiante"));
                lista.add(fila);
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al obtener estudiantes asignados: " + e.getMessage());
        }
        return lista;
    }

    // ✅ Obtener asignaciones completas por docente
    public List<Map<String, String>> obtenerAsignacionesPorDocente(int idDocente) {
        List<Map<String, String>> lista = new ArrayList<>();
        String sql = "SELECT a.id, e.nombre AS estudiante, a.instrumento, a.fecha_asignacion " +
                     "FROM clases_asignados a " +
                     "JOIN usuarios e ON a.id_estudiante = e.id " +
                     "WHERE a.id_docente = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idDocente);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Map<String, String> fila = new HashMap<>();
                fila.put("id", String.valueOf(rs.getInt("id")));
                fila.put("estudiante", rs.getString("estudiante"));
                fila.put("instrumento", rs.getString("instrumento"));
                Date fecha = rs.getDate("fecha_asignacion");
                fila.put("fecha", fecha != null ? fecha.toString() : "");
                lista.add(fila);
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al obtener asignaciones completas: " + e.getMessage());
        }
        return lista;
    }

    // ✅ Obtener asignaciones institucionales sin estudiante
    public List<DocenteConClaseDTO> obtenerAsignacionesInstitucionales() {
        List<DocenteConClaseDTO> lista = new ArrayList<>();
        String sql = "SELECT c.id AS id_clase, c.nombre_clase AS clase_nombre, " +
                     "u.id AS id_docente, u.nombre AS nombre, " +
                     "c.instrumento, c.etapa, c.grupo, c.cupo, " +
                     "c.dia_semana, c.hora_inicio, c.hora_fin, c.fecha_limite, " +
                     "COUNT(ic.id_estudiante) AS inscritos " +
                     "FROM clases c " +
                     "JOIN usuarios u ON c.id_docente = u.id " +
                     "LEFT JOIN inscripciones_clase ic ON c.id = ic.id_clase " +
                     "GROUP BY c.id, c.nombre_clase, u.id, u.nombre, " +
                     "c.instrumento, c.etapa, c.grupo, c.cupo, " +
                     "c.dia_semana, c.hora_inicio, c.hora_fin, c.fecha_limite";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                DocenteConClaseDTO dto = new DocenteConClaseDTO();
                dto.setClaseId(rs.getInt("id_clase"));
                dto.setClaseNombre(rs.getString("clase_nombre"));
                dto.setDocenteId(rs.getInt("id_docente"));
                dto.setDocenteNombre(rs.getString("nombre"));
                dto.setInstrumento(rs.getString("instrumento"));
                dto.setEtapa(rs.getString("etapa"));
                dto.setGrupo(rs.getString("grupo"));
                dto.setCupo(rs.getInt("cupo"));
                dto.setHorario(rs.getString("dia_semana") + " " +
                               rs.getString("hora_inicio") + " - " +
                               rs.getString("hora_fin"));
                Date sqlDate = rs.getDate("fecha_limite");
                dto.setFechaLimite((sqlDate != null) ? sqlDate.toLocalDate() : null);
                dto.setInscritos(rs.getInt("inscritos"));
                lista.add(dto);
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al obtener asignaciones institucionales: " + e.getMessage());
        }
        return lista;
    }

    // ✅ Subir asignación institucional sin estudiante
    public boolean subirAsignacion(int idDocente, String instrumento, String fecha) {
        String sqlVerificar = "SELECT COUNT(*) FROM asignaciones_docente WHERE id_docente = ? AND instrumento = ? AND id_estudiante IS NULL";
        String sqlInsertar = "INSERT INTO clases_asignadas (id_docente, id_estudiante, instrumento, fecha_asignacion) VALUES (?, NULL, ?, ?)";
        try (PreparedStatement psVer = conn.prepareStatement(sqlVerificar)) {
            psVer.setInt(1, idDocente);
            psVer.setString(2, instrumento);
            ResultSet rs = psVer.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) return false;
            try (PreparedStatement psIns = conn.prepareStatement(sqlInsertar)) {
                psIns.setInt(1, idDocente);
                psIns.setString(2, instrumento);
                if (fecha != null && !fecha.isEmpty()) {
                    psIns.setDate(3, Date.valueOf(fecha));
                } else {
                    psIns.setNull(3, Types.DATE);
                }
                return psIns.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al subir asignación institucional: " + e.getMessage());
            return false;
        }
    }

        // ✅ Editar asignación completa
    public boolean editarAsignacion(int idAsignacion, String nuevoInstrumento, int nuevaClaseId, String nuevaFecha) {
        String sql = "UPDATE clases_asignadas SET instrumento = ?, id_clase = ?, fecha_asignacion = ? WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nuevoInstrumento);
            ps.setInt(2, nuevaClaseId);
            if (nuevaFecha != null && !nuevaFecha.isEmpty()) {
                ps.setDate(3, Date.valueOf(nuevaFecha));
            } else {
                ps.setNull(3, Types.DATE);
            }
            ps.setInt(4, idAsignacion);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("❌ Error al editar asignación: " + e.getMessage());
            return false;
        }
    }

    // ✅ Eliminar asignación por ID
    public boolean eliminarAsignacion(int idAsignacion) {
        String sql = "DELETE FROM clases_asignadas WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idAsignacion);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("❌ Error al eliminar asignación: " + e.getMessage());
            return false;
        }
    }

    // ✅ Verificar si ya existe una asignación para evitar duplicados
    public boolean existeAsignacion(int idDocente, int idEstudiante, String instrumento) {
        String sql = "SELECT COUNT(*) FROM clases_asignadas WHERE id_docente = ? AND id_estudiante = ? AND instrumento = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idDocente);
            ps.setInt(2, idEstudiante);
            ps.setString(3, instrumento);
            ResultSet rs = ps.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        } catch (SQLException e) {
            System.err.println("❌ Error al verificar existencia de asignación: " + e.getMessage());
            return false;
        }
    }

    // ✅ Registrar asignación completa con etapa y fecha
    public boolean registrarAsignacion(int idDocente, int idEstudiante, String instrumento, String etapa, String fecha) {
        String sql = "INSERT INTO clases_asignadas (id_docente, id_estudiante, instrumento, etapa, fecha_asignacion) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idDocente);
            ps.setInt(2, idEstudiante);
            ps.setString(3, instrumento);
            ps.setString(4, etapa);
            if (fecha != null && !fecha.isEmpty()) {
                ps.setDate(5, Date.valueOf(fecha));
            } else {
                ps.setNull(5, Types.DATE);
            }
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("❌ Error al registrar asignación: " + e.getMessage());
            return false;
        }
    }

    // 🔎 Verificar si ya existe una asignación
    public boolean existeAsignacion(int idDocente, int idEstudiante, int claseId) {
        String sql = "SELECT COUNT(*) FROM clases_asignadas WHERE id_docente=? AND id_estudiante=? AND id_clase=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idDocente);
            ps.setInt(2, idEstudiante);
            ps.setInt(3, claseId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al verificar asignación: " + e.getMessage());
        }
        return false;
    }

    // ✅ Método para contar todas las asignaciones activas en el sistema
    public int contarTodas() {
        String sql = "SELECT COUNT(*) FROM clases_asignadas";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            System.err.println("❌ Error al contar asignaciones: " + e.getMessage());
        }
        return 0;
    }

    // ✅ Obtener tablas enviadas para certificación institucional
    public List<Map<String,Object>> obtenerTablasEnviadasPendientes() {
    List<Map<String,Object>> lista = new ArrayList<>();
    String sql = "SELECT c.id_clase, c.nombre_clase, c.instrumento, c.fecha_inicio, c.fecha_fin, c.estado " +
                 "FROM clases c " +
                 "WHERE c.estado = 'enviada' " +   // ✅ Solo pendientes
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
    // ✅ Emitir todos los certificados pendientes
    public int emitirCertificadosPendientes(String usuario) throws Exception {
    String sql = "UPDATE certificados " +
                 "SET estado = 'emitido', fecha_certificado = CURRENT_DATE, usuario_emisor = ? " +
                 "WHERE estado = 'pendiente'";
    try (PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setString(1, usuario); // registrar quién emitió
        return ps.executeUpdate();
    }
}

    // ✅ Contar certificados pendientes de emisión
    public int contarCertificadosPendientes() throws Exception {
        String sql = "SELECT COUNT(*) AS total FROM certificados WHERE estado = 'pendiente'";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("total");
            }
        }
        return 0;
    }

    // ✅ Contar todas las asignaciones que sí tienen estudiante asociado (clases asignadas)
    public int contarTodasAsignadas() {
        String sql = "SELECT COUNT(*) FROM clases_asignadas WHERE id_estudiante IS NOT NULL";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al contar asignaciones activas: " + e.getMessage());
        }
        return 0;
    }

    public boolean asignarDocenteConTrazabilidad(int idDocente, int idClase, String horario, String aula, String dia, String estado) {
    String sql = "INSERT INTO clases_asignadas (id_clase, id_docente, horario, aula, dia, fecha_asignacion, estado) " +
                 "VALUES (?, ?, ?, ?, ?, NOW(), ?)";
    try (PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, idClase);
        ps.setInt(2, idDocente);
        ps.setString(3, horario);
        ps.setString(4, aula);
        ps.setString(5, dia);
        ps.setString(6, estado);
        return ps.executeUpdate() > 0;
    } catch (SQLException e) {
        System.err.println("❌ Error en asignarDocenteConTrazabilidad: " + e.getMessage());
        return false;
    }
}
    
}


    