/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.mysymphony.proyecto_symphony1.dao;

/**
 * DAO para gestionar estudiantes y sus asignaciones.
 * Autor: camiv
 */

import com.mysymphony.proyecto_symphony1.modelo.Estudiante;
import com.mysymphony.proyecto_symphony1.util.Conexion;

import java.sql.*;
import java.util.*;

public class EstudianteDAO {
    
    private Connection conn;

    public EstudianteDAO(Connection conn) {
        this.conn = conn;
    }


    // ✅ Actualizar notas y seguimiento musical
public boolean actualizarNotas(Estudiante est) {
    String sql = "UPDATE estudiantes SET instrumento = ?, nivel_tecnico = ?, etapa_pedagogica = ?, progreso = ?, observaciones = ?, fecha_actualizacion = ? WHERE id_estudiante = ?";

    try (Connection con = Conexion.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setString(1, est.getInstrumento());
        ps.setString(2, est.getNivelTecnico());
        ps.setString(3, est.getEtapaPedagogica());
        ps.setString(4, est.getProgreso());
        ps.setString(5, est.getObservaciones());

        if (est.getFechaActualizacion() != null) {
            ps.setDate(6, java.sql.Date.valueOf(est.getFechaActualizacion()));
        } else {
            ps.setNull(6, java.sql.Types.DATE);
        }

        // 🔧 Aquí el cambio: usar getIdEstudiante()
        ps.setInt(7, est.getIdEstudiante());

        return ps.executeUpdate() > 0;

    } catch (SQLException e) {
        System.err.println("❌ Error al actualizar notas del estudiante: " + e.getMessage());
        return false;
    }
}
    public List<Map<String, String>> obtenerEstudiantesAsignados(int idDocente) {
    List<Map<String, String>> lista = new ArrayList<>();

    String sql = "SELECT e.nombre, e.instrumento, e.nivel_tecnico, e.etapa_pedagogica " +
                 "FROM estudiantes e " +
                 "JOIN asignaciones a ON e.id_estudiante = a.id_estudiante " +
                 "WHERE a.id_docente = ?";

    try (Connection con = Conexion.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setInt(1, idDocente);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            Map<String, String> est = new HashMap<>();
            est.put("nombre", rs.getString("nombre"));
            est.put("instrumento", rs.getString("instrumento"));
            est.put("nivel", rs.getString("nivel_tecnico"));
            est.put("etapa", rs.getString("etapa_pedagogica"));
            lista.add(est);
        }

    } catch (SQLException e) {
        System.err.println("❌ Error al obtener estudiantes asignados: " + e.getMessage());
    }

    return lista;
}
    
        // ✅ Listar estudiantes disponibles (no asignados a ninguna clase)
    public List<Estudiante> listarEstudiantesDisponibles() throws SQLException {
    List<Estudiante> lista = new ArrayList<>();
    String sql = "SELECT e.id_estudiante, e.nombre, e.etapa_pedagogica " +
                 "FROM estudiantes e " +
                 "WHERE e.id_estudiante NOT IN (SELECT id_estudiante FROM inscripciones_clase) " +
                 "ORDER BY e.nombre ASC";

    try (PreparedStatement ps = conn.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
            Estudiante est = new Estudiante();
            est.setId(rs.getInt("id_estudiante"));   // asegúrate que tu modelo tenga setId()
            est.setNombre(rs.getString("nombre"));
            est.setEtapaPedagogica(rs.getString("etapa_pedagogica"));
            lista.add(est);
        }
    }
    return lista;
}
    public List<Estudiante> listarPorClase(int idClase) throws SQLException {
    List<Estudiante> lista = new ArrayList<>();
    String sql = "SELECT e.id_estudiante, e.nombre, e.correo " +
                "FROM estudiantes e " +
                "INNER JOIN inscripciones i ON e.id_estudiante = i.id_estudiante " +
                "WHERE i.id_clase = ?";
    try (PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, idClase);
        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Estudiante est = new Estudiante();
                est.setIdEstudiante(rs.getInt("id_estudiante"));
                est.setNombre(rs.getString("nombre"));
                est.setCorreo(rs.getString("correo"));
                lista.add(est);
            }
        }
    }
    return lista;
}

}