/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mysymphony.proyecto_symphony1.dao;

import com.mysymphony.proyecto_symphony1.modelo.Estudiante;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClaseEstudianteDAO {
    private Connection conn;

    public ClaseEstudianteDAO(Connection conn) {
        this.conn = conn;
    }

    // Registrar inscripción de un estudiante en una clase
    public boolean registrarInscripcion(int idClase, int idEstudiante) {
        String sql = "INSERT INTO clases_estudiantes (id_clase, id_estudiante) VALUES (?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idClase);
            ps.setInt(2, idEstudiante);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("❌ Error al registrar inscripción: " + e.getMessage());
            return false;
        }
    }

    // Obtener todos los estudiantes inscritos en una clase
    public List<Estudiante> obtenerEstudiantesPorClase(int idClase) {
        List<Estudiante> lista = new ArrayList<>();
        String sql = "SELECT e.id_estudiante, e.nombre, e.correo " +
                     "FROM clases_estudiantes ce " +
                     "JOIN estudiantes e ON ce.id_estudiante = e.id_estudiante " +
                     "WHERE ce.id_clase = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idClase);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Estudiante est = new Estudiante();
                est.setIdEstudiante(rs.getInt("id_estudiante"));
                est.setNombre(rs.getString("nombre"));
                est.setCorreo(rs.getString("correo"));
                lista.add(est);
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al consultar estudiantes por clase: " + e.getMessage());
        }
        return lista;
    }

    // Eliminar inscripción de un estudiante en una clase
    public boolean eliminarInscripcion(int idClase, int idEstudiante) {
        String sql = "DELETE FROM clases_estudiantes WHERE id_clase = ? AND id_estudiante = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idClase);
            ps.setInt(2, idEstudiante);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("❌ Error al eliminar inscripción: " + e.getMessage());
            return false;
        }
    }
}