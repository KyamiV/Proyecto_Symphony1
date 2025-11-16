/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mysymphony.proyecto_symphony1.dao;

/**
 * DAO para gestionar asignaciones entre docentes y estudiantes.
 * Autor: camiv
 */

import com.mysymphony.proyecto_symphony1.util.Conexion;

import java.sql.*;
import java.util.*;

public class AsignacionDAO {

    // Asignar estudiante a docente
    public void asignarEstudiante(int idDocente, int idEstudiante, String instrumento) {
        String sql = "INSERT INTO asignaciones_docente (id_docente, id_estudiante, instrumento) VALUES (?, ?, ?)";
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idDocente);
            ps.setInt(2, idEstudiante);
            ps.setString(3, instrumento);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error al asignar estudiante: " + e.getMessage());
        }
    }

    // Obtener IDs de estudiantes asignados a un docente
    public List<Integer> obtenerEstudiantesAsignados(int idDocente) {
        List<Integer> lista = new ArrayList<>();
        String sql = "SELECT id_estudiante FROM asignaciones_docente WHERE id_docente = ?";
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idDocente);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(rs.getInt("id_estudiante"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener estudiantes asignados: " + e.getMessage());
        }
        return lista;
    }

    // Obtener asignaciones completas con nombres e instrumento
    public List<Map<String, String>> obtenerAsignacionesPorDocente(int idDocente) {
        List<Map<String, String>> lista = new ArrayList<>();
        String sql = "SELECT a.id, e.nombre AS estudiante, a.instrumento, a.fecha_asignacion " +
                     "FROM asignaciones_docente a " +
                     "JOIN estudiantes e ON a.id_estudiante = e.id " +
                     "WHERE a.id_docente = ?";
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idDocente);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Map<String, String> fila = new HashMap<>();
                    fila.put("id", String.valueOf(rs.getInt("id")));
                    fila.put("estudiante", rs.getString("estudiante"));
                    fila.put("instrumento", rs.getString("instrumento"));
                    fila.put("fecha", rs.getString("fecha_asignacion"));
                    lista.add(fila);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener asignaciones completas: " + e.getMessage());
        }
        return lista;
    }

    // Actualizar instrumento asignado a un estudiante
    public boolean actualizarInstrumentoAsignado(int idAsignacion, String nuevoInstrumento) {
        boolean actualizado = false;
        String sql = "UPDATE asignaciones_docente SET instrumento = ? WHERE id = ?";

        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, nuevoInstrumento);
            ps.setInt(2, idAsignacion);

            int filas = ps.executeUpdate();
            actualizado = (filas > 0);

        } catch (SQLException e) {
            System.err.println("Error al actualizar instrumento: " + e.getMessage());
        }

        return actualizado;
    }
}