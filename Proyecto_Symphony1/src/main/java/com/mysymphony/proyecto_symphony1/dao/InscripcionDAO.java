/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mysymphony.proyecto_symphony1.dao;

import com.mysymphony.proyecto_symphony1.modelo.Inscripcion;   // 👈 tu modelo institucional
import com.mysymphony.proyecto_symphony1.modelo.Estudiante;   // 👈 si usas también Estudiante

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * DAO institucional para gestión de inscripciones.
 * Autor: camiv
 * Función: Registrar, listar, buscar y contar inscripciones con trazabilidad completa.
 */
public class InscripcionDAO {

    private final Connection conn;

    public InscripcionDAO(Connection conn) {
        this.conn = conn;
    }

    // ✅ Registrar una nueva inscripción
    public boolean registrar(Map<String,Object> insc) {
        String sql = "INSERT INTO inscripciones_clase (id_clase, id_estudiante, fecha_inscripcion) VALUES (?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, (Integer) insc.get("id_clase"));
            ps.setInt(2, (Integer) insc.get("id_estudiante"));

            if (insc.get("fecha_inscripcion") != null) {
                ps.setDate(3, Date.valueOf(insc.get("fecha_inscripcion").toString()));
            } else {
                ps.setDate(3, null);
            }

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("❌ Error al registrar inscripción: " + e.getMessage());
            return false;
        }
    }

    // ✅ Listar todas las inscripciones con nombres de estudiante y clase
public List<Inscripcion> listar() {
    List<Inscripcion> lista = new ArrayList<>();
    String sql = "SELECT i.id_inscripcion, i.id_clase, i.id_estudiante, i.fecha_inscripcion, " +
                 "e.nombre AS nombre_estudiante, e.correo AS correo_estudiante, " +
                 "c.nombre_clase AS nombre_clase " +
                 "FROM inscripciones_clase i " +
                 "LEFT JOIN estudiantes e ON i.id_estudiante = e.id_estudiante " +
                 "LEFT JOIN clases c ON i.id_clase = c.id_clase " +
                 "ORDER BY i.fecha_inscripcion DESC";

    try (PreparedStatement ps = conn.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {

        while (rs.next()) {
            Inscripcion ins = new Inscripcion();
            ins.setId(rs.getInt("id_inscripcion"));
            ins.setClaseId(rs.getInt("id_clase"));
            ins.setIdEstudiante(rs.getInt("id_estudiante"));

            // Conversión correcta de java.sql.Date a LocalDate
            java.sql.Date sqlDate = rs.getDate("fecha_inscripcion");
            ins.setFechaInscripcion(sqlDate != null ? sqlDate.toLocalDate() : null);

            // Poblar datos adicionales desde los JOIN
            ins.setNombreEstudiante(rs.getString("nombre_estudiante"));
            ins.setCorreoEstudiante(rs.getString("correo_estudiante"));
            ins.setNombreClase(rs.getString("nombre_clase"));

            lista.add(ins);
        }

    } catch (SQLException e) {
        System.err.println("❌ Error al listar inscripciones: " + e.getMessage());
    }
    return lista;
}

    // ✅ Listar inscripciones por clase (usando modelo Inscripcion)
    public List<Inscripcion> listarInscripcionesPorClase(int idClase) {
    List<Inscripcion> lista = new ArrayList<>();
    String sql = "SELECT i.id_inscripcion, i.id_clase, i.id_estudiante, i.fecha_inscripcion, " +
                 "       e.nombre AS nombreEstudiante, e.correo AS correoEstudiante, " +
                 "       c.nombre_clase AS nombreClase " +
                 "FROM inscripciones_clase i " +
                 "JOIN estudiantes e ON i.id_estudiante = e.id_estudiante " +
                 "JOIN clases c ON i.id_clase = c.id_clase " +
                 "WHERE i.id_clase = ?";

    try (PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, idClase);
        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Inscripcion ins = new Inscripcion();
                ins.setId(rs.getInt("id_inscripcion"));
                ins.setClaseId(rs.getInt("id_clase"));
                ins.setIdEstudiante(rs.getInt("id_estudiante"));
                ins.setFechaInscripcion(rs.getDate("fecha_inscripcion").toLocalDate());
                ins.setNombreEstudiante(rs.getString("nombreEstudiante"));
                ins.setCorreoEstudiante(rs.getString("correoEstudiante"));
                ins.setNombreClase(rs.getString("nombreClase"));
                lista.add(ins);
            }
        }
    } catch (SQLException e) {
        System.err.println("❌ Error al listar inscripciones por clase: " + e.getMessage());
    }
    return lista;
}

    // ✅ Buscar inscripciones por filtro textual (nombre estudiante o clase)
public List<Inscripcion> buscar(String filtro) {
    List<Inscripcion> lista = new ArrayList<>();
    String sql = "SELECT i.id_inscripcion, i.id_clase, i.id_estudiante, i.fecha_inscripcion, " +
                 "e.nombre AS nombre_estudiante, e.correo AS correo_estudiante, " +
                 "c.nombre_clase AS nombre_clase " +
                 "FROM inscripciones_clase i " +
                 "LEFT JOIN estudiantes e ON i.id_estudiante = e.id_estudiante " +
                 "LEFT JOIN clases c ON i.id_clase = c.id_clase " +
                 "WHERE e.nombre LIKE ? OR c.nombre_clase LIKE ? " +
                 "ORDER BY i.fecha_inscripcion DESC";

    try (PreparedStatement ps = conn.prepareStatement(sql)) {
        String filtroLike = "%" + filtro + "%";
        ps.setString(1, filtroLike);
        ps.setString(2, filtroLike);

        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Inscripcion ins = new Inscripcion();
                ins.setId(rs.getInt("id_inscripcion"));
                ins.setClaseId(rs.getInt("id_clase"));
                ins.setIdEstudiante(rs.getInt("id_estudiante"));

                java.sql.Date sqlDate = rs.getDate("fecha_inscripcion");
                ins.setFechaInscripcion(sqlDate != null ? sqlDate.toLocalDate() : null);

                ins.setNombreEstudiante(rs.getString("nombre_estudiante"));
                ins.setCorreoEstudiante(rs.getString("correo_estudiante"));
                ins.setNombreClase(rs.getString("nombre_clase"));

                lista.add(ins);
            }
        }
    } catch (SQLException e) {
        System.err.println("❌ Error al buscar inscripciones con filtro '" + filtro + "': " + e.getMessage());
    }
    return lista;
}

    // ✅ Listar estudiantes inscritos en una clase específica (modo Estudiante)
    public List<Estudiante> listarEstudiantesPorClase(int idClase) {
    List<Estudiante> lista = new ArrayList<>();
    String sql = "SELECT e.id_estudiante, e.nombre, e.correo, e.instrumento, " +
                 "       e.etapa_pedagogica, e.color_hex, i.fecha_inscripcion " +
                 "FROM inscripciones_clase i " +
                 "JOIN estudiantes e ON i.id_estudiante = e.id_estudiante " +
                 "WHERE i.id_clase = ?";

    try (PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, idClase);
        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Estudiante est = new Estudiante();
                est.setIdEstudiante(rs.getInt("id_estudiante"));
                est.setNombre(rs.getString("nombre"));
                est.setCorreo(rs.getString("correo"));
                est.setInstrumento(rs.getString("instrumento"));
                est.setEtapaPedagogica(rs.getString("etapa_pedagogica"));
                est.setColorHex(rs.getString("color_hex"));

                // ✅ conversión segura de java.sql.Date a LocalDate
                Date sqlDate = rs.getDate("fecha_inscripcion");
                if (sqlDate != null) {
                    est.setFechaInscripcion(sqlDate.toLocalDate());
                }

                lista.add(est);
            }
        }
    } catch (SQLException e) {
        System.err.println("❌ Error al listar estudiantes por clase: " + e.getMessage());
    }
    return lista;
}

    // ✅ Validar inscripción (evitar duplicados)
    public boolean validarInscripcion(int idEstudiante, int idClase) {
        String sql = "SELECT 1 FROM inscripciones_clase WHERE id_estudiante = ? AND id_clase = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idEstudiante);
            ps.setInt(2, idClase);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al validar inscripción: " + e.getMessage());
        }
        return false;
    }

    // ✅ Contar inscritos por clase (para métricas administrativas)
    public int contarInscritosPorClase(int idClase) {
        String sql = "SELECT COUNT(*) FROM inscripciones_clase WHERE id_clase = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idClase);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al contar inscritos: " + e.getMessage());
        }
        return 0;
    }
    public List<Map<String, String>> obtenerEstudiantesPorClase(int claseId) {
    List<Map<String, String>> lista = new ArrayList<>();
    String sql = "SELECT e.id_estudiante, u.nombre, e.etapa_pedagogica " +
                 "FROM inscripciones i " +
                 "JOIN estudiantes e ON i.id_estudiante = e.id_estudiante " +
                 "JOIN usuarios u ON e.id_usuario = u.id_usuario " +
                 "WHERE i.id_clase = ?";
    try (PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, claseId);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Map<String, String> est = new HashMap<>();
            est.put("id", rs.getString("id_estudiante"));
            est.put("nombre", rs.getString("nombre"));
            est.put("etapa", rs.getString("etapa_pedagogica"));
            lista.add(est);
        }
    } catch (SQLException e) {
        System.err.println("❌ Error al obtener estudiantes por clase: " + e.getMessage());
    }
    return lista;
}
}