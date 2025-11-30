/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.mysymphony.proyecto_symphony1.dao;

import com.mysymphony.proyecto_symphony1.modelo.Instrumento;
import java.sql.*;
import java.util.*;

public class InstrumentoDAO {
    private final Connection conn;

    public InstrumentoDAO(Connection conn) {
        this.conn = conn;
    }

    /** Obtener todos los instrumentos con su cupo */
    public List<Instrumento> listarInstrumentos() throws SQLException {
        List<Instrumento> instrumentos = new ArrayList<>();
        String sql = "SELECT nombre, cupo_maximo FROM instrumentos ORDER BY nombre";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                instrumentos.add(new Instrumento(
                        rs.getString("nombre"),
                        rs.getInt("cupo_maximo")
                ));
            }
        }
        return instrumentos;
    }

    /** Insertar nuevo instrumento */
    public void insertarInstrumento(String nombre, int cupo) throws SQLException {
        String sql = "INSERT INTO instrumentos (nombre, cupo_maximo) VALUES (?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nombre);
            ps.setInt(2, cupo);
            ps.executeUpdate();
        }
    }

    /** Actualizar cupo de un instrumento */
    public void actualizarCupoInstrumento(String nombre, int nuevoCupo) throws SQLException {
        String sql = "UPDATE instrumentos SET cupo_maximo=? WHERE nombre=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, nuevoCupo);
            ps.setString(2, nombre);
            ps.executeUpdate();
        }
    }

    /** Eliminar instrumento */
    public void eliminarInstrumento(String nombre) throws SQLException {
        String sql = "DELETE FROM instrumentos WHERE nombre=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nombre);
            ps.executeUpdate();
        }
    }
}