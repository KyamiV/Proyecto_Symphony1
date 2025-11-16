/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.mysymphony.proyecto_symphony1.dao;

import com.mysymphony.proyecto_symphony1.modelo.Inscripcion;
import com.mysymphony.proyecto_symphony1.util.Conexion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InscripcionDAO {

    // Registrar una nueva inscripción
    public boolean registrar(Inscripcion insc) {
        String sql = "INSERT INTO inscripciones (estudiante, programa, fecha) VALUES (?, ?, ?)";

        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, insc.getEstudiante());
            ps.setString(2, insc.getPrograma());
            ps.setString(3, insc.getFecha());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error al registrar inscripción: " + e.getMessage());
            return false;
        }
    }

    // Listar todas las inscripciones
    public List<Inscripcion> listar() {
        List<Inscripcion> lista = new ArrayList<>();
        String sql = "SELECT * FROM inscripciones";

        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Inscripcion i = new Inscripcion();
                i.setEstudiante(rs.getString("estudiante"));
                i.setPrograma(rs.getString("programa"));
                i.setFecha(rs.getString("fecha"));
                lista.add(i);
            }

        } catch (SQLException e) {
            System.err.println("Error al listar inscripciones: " + e.getMessage());
        }

        return lista;
    }

    // Buscar inscripciones por estudiante o programa
    public List<Inscripcion> buscar(String filtro) {
        List<Inscripcion> lista = new ArrayList<>();
        String sql = "SELECT * FROM inscripciones WHERE estudiante LIKE ? OR programa LIKE ?";

        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            String criterio = "%" + filtro + "%";
            ps.setString(1, criterio);
            ps.setString(2, criterio);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Inscripcion i = new Inscripcion();
                    i.setEstudiante(rs.getString("estudiante"));
                    i.setPrograma(rs.getString("programa"));
                    i.setFecha(rs.getString("fecha"));
                    lista.add(i);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al buscar inscripciones: " + e.getMessage());
        }

        return lista;
    }
}