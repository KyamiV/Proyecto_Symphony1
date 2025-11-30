/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.mysymphony.proyecto_symphony1.dao;

import java.sql.*;
import java.util.*;

public class ConfiguracionDAO {

    private final Connection conn;

    public ConfiguracionDAO(Connection conn) {
        this.conn = conn;
    }

    // =========================
    // CONFIGURACION_SISTEMA
    // =========================
    public Map<String, String> listarParametros() throws SQLException {
        Map<String, String> parametros = new LinkedHashMap<>();
        String sql = "SELECT clave, valor FROM configuracion_sistema ORDER BY clave";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                parametros.put(rs.getString("clave"), rs.getString("valor"));
            }
        }
        return parametros;
    }

    public void insertarParametro(String clave, String valor) throws SQLException {
        String sql = "INSERT INTO configuracion_sistema (clave, valor) VALUES (?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, clave);
            ps.setString(2, valor);
            ps.executeUpdate();
        }
    }

    public void actualizarParametro(String clave, String nuevoValor) throws SQLException {
        String sql = "UPDATE configuracion_sistema SET valor=? WHERE clave=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nuevoValor);
            ps.setString(2, clave);
            ps.executeUpdate();
        }
    }

    public void eliminarParametro(String clave) throws SQLException {
        String sql = "DELETE FROM configuracion_sistema WHERE clave=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, clave);
            ps.executeUpdate();
        }
    }

    // =========================
    // ETAPAS_MUSICALES
    // =========================
    public List<String> listarEtapas() throws SQLException {
        List<String> etapas = new ArrayList<>();
        String sql = "SELECT nombre_etapa FROM etapas_musicales ORDER BY id_etapa";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                etapas.add(rs.getString("nombre_etapa"));
            }
        }
        return etapas;
    }

    public void insertarEtapa(String nombreEtapa, String descripcion) throws SQLException {
        String sql = "INSERT INTO etapas_musicales (nombre_etapa, descripcion) VALUES (?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nombreEtapa);
            ps.setString(2, descripcion);
            ps.executeUpdate();
        }
    }

    public void actualizarEtapa(String etapaOriginal, String nuevaEtapa, String nuevaDescripcion) throws SQLException {
        String sql = "UPDATE etapas_musicales SET nombre_etapa=?, descripcion=? WHERE nombre_etapa=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nuevaEtapa);
            ps.setString(2, nuevaDescripcion);
            ps.setString(3, etapaOriginal);
            ps.executeUpdate();
        }
    }

    public void eliminarEtapa(String nombreEtapa) throws SQLException {
        String sql = "DELETE FROM etapas_musicales WHERE nombre_etapa=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nombreEtapa);
            ps.executeUpdate();
        }
    }

    // =========================
    // COLORES_ROLES
    // =========================
    public Map<String, String> listarRoles() throws SQLException {
        Map<String, String> roles = new LinkedHashMap<>();
        String sql = "SELECT rol, color_hex FROM colores_roles ORDER BY rol";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                roles.put(rs.getString("rol"), rs.getString("color_hex"));
            }
        }
        return roles;
    }

    public void insertarRol(String rol, String colorHex) throws SQLException {
        String sql = "INSERT INTO colores_roles (rol, color_hex) VALUES (?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, rol);
            ps.setString(2, colorHex);
            ps.executeUpdate();
        }
    }

    public void asignarColorRol(String rol, String nuevoColorHex) throws SQLException {
        String sql = "UPDATE colores_roles SET color_hex=? WHERE rol=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nuevoColorHex);
            ps.setString(2, rol);
            ps.executeUpdate();
        }
    }

    public void eliminarRol(String rol) throws SQLException {
        String sql = "DELETE FROM colores_roles WHERE rol=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, rol);
            ps.executeUpdate();
        }
    }
}