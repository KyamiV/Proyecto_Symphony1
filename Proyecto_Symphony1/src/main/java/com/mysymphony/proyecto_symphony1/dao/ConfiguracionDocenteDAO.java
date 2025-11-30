/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mysymphony.proyecto_symphony1.dao;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

/**
 * DAO para gestionar la configuración individual de cada docente
 * Tabla: configuracion_docente
 * Autor: Camila
 */
public class ConfiguracionDocenteDAO {

    private final Connection conn;

    public ConfiguracionDocenteDAO(Connection conn) {
        this.conn = conn;
    }

    // =========================
    // OBTENER CONFIGURACIÓN
    // =========================
    public Map<String, Object> obtenerConfiguracion(int idDocente) throws SQLException {
        Map<String, Object> config = new HashMap<>();
        String sql = "SELECT tema_oscuro, mostrar_indicadores, idioma FROM configuracion_docente WHERE id_docente = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idDocente);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    config.put("temaOscuro", rs.getBoolean("tema_oscuro"));
                    config.put("mostrarIndicadores", rs.getBoolean("mostrar_indicadores"));
                    config.put("idioma", rs.getString("idioma"));
                }
            }
        }
        return config;
    }

    // =========================
    // INSERTAR CONFIGURACIÓN
    // =========================
    public void insertarConfiguracion(int idDocente, boolean temaOscuro, boolean mostrarIndicadores, String idioma) throws SQLException {
        String sql = "INSERT INTO configuracion_docente (id_docente, tema_oscuro, mostrar_indicadores, idioma) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idDocente);
            ps.setBoolean(2, temaOscuro);
            ps.setBoolean(3, mostrarIndicadores);
            ps.setString(4, idioma);
            ps.executeUpdate();
        }
    }

    // =========================
    // ACTUALIZAR CONFIGURACIÓN
    // =========================
    public void actualizarConfiguracion(int idDocente, boolean temaOscuro, boolean mostrarIndicadores, String idioma) throws SQLException {
        String sql = "UPDATE configuracion_docente SET tema_oscuro = ?, mostrar_indicadores = ?, idioma = ?, fecha_actualizacion = CURRENT_TIMESTAMP WHERE id_docente = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBoolean(1, temaOscuro);
            ps.setBoolean(2, mostrarIndicadores);
            ps.setString(3, idioma);
            ps.setInt(4, idDocente);
            ps.executeUpdate();
        }
    }

    // =========================
    // VERIFICAR SI EXISTE CONFIGURACIÓN
    // =========================
    public boolean existeConfiguracion(int idDocente) throws SQLException {
        String sql = "SELECT 1 FROM configuracion_docente WHERE id_docente = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idDocente);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }
}