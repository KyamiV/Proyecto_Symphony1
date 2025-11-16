/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mysymphony.proyecto_symphony1.dao;

/**
 * DAO para operaciones sobre la tabla de notas.
 * Autor: camiv
 */

import com.mysymphony.proyecto_symphony1.util.Conexion;
import java.sql.*;
import java.util.*;

public class NotaDAO {

    // Registrar nota por instrumento y etapa
    public boolean registrarNota(String estudiante, String instrumento, String etapa, double nota, String observaciones, String docente) {
        String sql = "INSERT INTO notas (estudiante, instrumento, etapa, nota, observaciones, docente) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, estudiante);
            ps.setString(2, instrumento);
            ps.setString(3, etapa);
            ps.setDouble(4, nota);
            ps.setString(5, observaciones);
            ps.setString(6, docente);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error al registrar nota por etapa: " + e.getMessage());
            return false;
        }
    }

    // Registrar nota por curso
    public boolean registrarNota(String nombreEstudiante, String curso, double nota, String fechaRegistro) {
        String sql = "INSERT INTO notas (nombre_estudiante, curso, nota, fecha_registro) VALUES (?, ?, ?, ?)";
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, nombreEstudiante);
            ps.setString(2, curso);
            ps.setDouble(3, nota);
            ps.setString(4, fechaRegistro);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error al registrar nota por curso: " + e.getMessage());
            return false;
        }
    }

    // Actualizar nota por estudiante + instrumento + etapa
    public boolean actualizarNota(String estudiante, String instrumento, String etapa, double nota, String observaciones) {
        String sql = "UPDATE notas SET nota = ?, observaciones = ? WHERE estudiante = ? AND instrumento = ? AND etapa = ?";
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setDouble(1, nota);
            ps.setString(2, observaciones);
            ps.setString(3, estudiante);
            ps.setString(4, instrumento);
            ps.setString(5, etapa);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error al actualizar nota: " + e.getMessage());
            return false;
        }
    }

    // Obtener notas registradas por docente
    public List<Map<String, String>> obtenerNotasPorDocente(String docente) {
        List<Map<String, String>> lista = new ArrayList<>();
        String sql = "SELECT estudiante, instrumento, etapa, nota, observaciones FROM notas WHERE docente = ?";
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, docente);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Map<String, String> fila = new HashMap<>();
                    fila.put("estudiante", rs.getString("estudiante"));
                    fila.put("instrumento", rs.getString("instrumento"));
                    fila.put("etapa", rs.getString("etapa"));
                    fila.put("nota", rs.getString("nota"));
                    fila.put("observaciones", rs.getString("observaciones"));
                    lista.add(fila);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener notas por docente: " + e.getMessage());
        }
        return lista;
    }

    // Obtener notas por curso
    public List<Map<String, String>> obtenerNotasPorCurso(String curso) {
        List<Map<String, String>> lista = new ArrayList<>();
        String sql = "SELECT nombre_estudiante, nota, fecha_registro FROM notas WHERE curso = ?";
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, curso);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Map<String, String> fila = new HashMap<>();
                    fila.put("nombre_estudiante", rs.getString("nombre_estudiante"));
                    fila.put("nota", rs.getString("nota"));
                    fila.put("fecha_registro", rs.getString("fecha_registro"));
                    lista.add(fila);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener notas por curso: " + e.getMessage());
        }
        return lista;
    }

    // Obtener estudiantes registrados con rol "estudiante"
    public List<Map<String, String>> obtenerEstudiantesRegistrados() {
        List<Map<String, String>> lista = new ArrayList<>();
        String sql = "SELECT id_usuario, nombre FROM usuarios WHERE rol = 'estudiante'";
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Map<String, String> est = new HashMap<>();
                est.put("id", rs.getString("id_usuario"));
                est.put("nombre", rs.getString("nombre"));
                lista.add(est);
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener estudiantes registrados: " + e.getMessage());
        }
        return lista;
    }
}