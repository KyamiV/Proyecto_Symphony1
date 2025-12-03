/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mysymphony.proyecto_symphony1.dao;

import java.sql.*;
import java.util.*;

public class ObservacionesDAO {
    private final Connection conn;

    public ObservacionesDAO(Connection conn) {
        this.conn = conn;
    }

    // =========================
    // INSERTAR OBSERVACIÓN
    // =========================
    public void insertarObservacion(String nombreDocente, int idEstudiante,
                                    String instrumento, String etapaPedagogica,
                                    String comentario) throws SQLException {
        String sql = "INSERT INTO observaciones (id_estudiante, instrumento, etapa_pedagogica, comentario, docente, fecha_registro, enviada) " +
                     "VALUES (?, ?, ?, ?, ?, NOW(), 0)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idEstudiante);
            ps.setString(2, instrumento);
            ps.setString(3, etapaPedagogica);
            ps.setString(4, comentario);
            ps.setString(5, nombreDocente); // Se guarda el nombre del docente como texto
            ps.executeUpdate();
        }
    }

    // =========================
    // LISTAR OBSERVACIONES POR DOCENTE
    // =========================
    public List<Map<String, String>> listarObservacionesPorDocente(String nombreDocente) throws SQLException {
        List<Map<String, String>> observaciones = new ArrayList<>();

        String sql = "SELECT o.id_observacion, o.id_estudiante, e.nombre AS estudiante, e.correo, " +
                     "o.instrumento, o.etapa_pedagogica, o.comentario, o.docente, o.fecha_registro, o.enviada " +
                     "FROM observaciones o " +
                     "JOIN estudiantes e ON o.id_estudiante = e.id_estudiante " +
                     "WHERE o.docente = ? " +
                     "ORDER BY o.fecha_registro DESC";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nombreDocente);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Map<String, String> obs = new HashMap<>();
                    obs.put("id_observacion", String.valueOf(rs.getInt("id_observacion")));
                    obs.put("id_estudiante", String.valueOf(rs.getInt("id_estudiante")));
                    obs.put("estudiante", rs.getString("estudiante"));   // ✅ nombre real del estudiante
                    obs.put("email", rs.getString("correo"));            // opcional: correo del estudiante
                    obs.put("instrumento", rs.getString("instrumento"));
                    obs.put("etapa_pedagogica", rs.getString("etapa_pedagogica"));
                    obs.put("comentario", rs.getString("comentario"));
                    obs.put("docente", rs.getString("docente"));
                    obs.put("fecha_registro", rs.getTimestamp("fecha_registro").toString());
                    obs.put("enviada", rs.getBoolean("enviada") ? "Sí" : "No");
                    observaciones.add(obs);
                }
            }
        }

        return observaciones;
    }

    // =========================
    // LISTAR ESTUDIANTES POR DOCENTE
    // =========================
    public List<Map<String, String>> listarEstudiantesPorDocente(int idDocente) throws SQLException {
        List<Map<String, String>> lista = new ArrayList<>();
        String sql = "SELECT DISTINCT i.id_inscripcion, e.id_estudiante, e.nombre, e.correo, " +
                     "c.id_clase, c.nombre_clase " +
                     "FROM estudiantes e " +
                     "JOIN inscripciones_clase i ON e.id_estudiante = i.id_estudiante " +
                     "JOIN clases c ON i.id_clase = c.id_clase " +
                     "JOIN clases_asignadas ca ON c.id_clase = ca.id_clase " +
                     "WHERE ca.id_docente = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idDocente);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Map<String, String> fila = new HashMap<>();
                    fila.put("id_inscripcion", rs.getString("id_inscripcion"));
                    fila.put("id_estudiante", rs.getString("id_estudiante"));
                    fila.put("nombre", rs.getString("nombre"));
                    fila.put("email", rs.getString("correo")); // campo real de tu tabla estudiantes
                    fila.put("id_clase", rs.getString("id_clase"));
                    fila.put("clase", rs.getString("nombre_clase"));
                    lista.add(fila);
                }
            }
        }
        return lista;
    }
    
     // 🔹 Método para eliminar observación por ID
    public boolean eliminarObservacion(int idObservacion) throws SQLException {
        String sql = "DELETE FROM observaciones WHERE id_observacion = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idObservacion);
            return ps.executeUpdate() > 0;
        }
    }

}