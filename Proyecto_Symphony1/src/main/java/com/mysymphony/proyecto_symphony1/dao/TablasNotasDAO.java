/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.mysymphony.proyecto_symphony1.dao;

import java.sql.*;
import java.util.*;

public class TablasNotasDAO {
    
    private Connection conn;

    public TablasNotasDAO(Connection conn) {
        this.conn = conn;
    }

    // 🔹 Verificar si existe una tabla por ID
    public boolean existeTabla(int tablaId) {
        String sql = "SELECT COUNT(*) FROM tablas_guardadas WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, tablaId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al verificar existencia de tabla: " + e.getMessage());
        }
        return false;
    }

    // 🔹 Registrar nueva tabla institucional de notas
    public int crearTablaNotas(int claseId, int docenteId, String nombre, String descripcion) {
        int tablaId = -1;
        String sql = "INSERT INTO tablas_guardadas (id_clase, id_docente, nombre, descripcion, fecha) " +
                     "VALUES (?, ?, ?, ?, CURRENT_DATE)";

        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, claseId);
            ps.setInt(2, docenteId);
            ps.setString(3, nombre);
            ps.setString(4, descripcion);

            if (ps.executeUpdate() > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        tablaId = rs.getInt(1);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al crear tabla de notas: " + e.getMessage());
        }

        return tablaId;
    }

    // 🔹 Consultar todas las tablas creadas por un docente (versión simple)
    public List<Map<String, String>> obtenerTablasPorDocente(int docenteId) {
        List<Map<String, String>> tablas = new ArrayList<>();

        String sql = "SELECT t.id, t.nombre, t.descripcion, t.fecha, t.enviada, c.nombre AS clase " +
                     "FROM tablas_guardadas t " +
                     "JOIN clases c ON t.id_clase = c.id " +
                     "WHERE t.id_docente = ? " +
                     "ORDER BY t.fecha DESC";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, docenteId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Map<String, String> tabla = new HashMap<>();
                    tabla.put("id", String.valueOf(rs.getInt("id")));
                    tabla.put("nombre", rs.getString("nombre"));
                    tabla.put("descripcion", rs.getString("descripcion"));
                    tabla.put("fecha", rs.getString("fecha"));
                    tabla.put("enviada", rs.getBoolean("enviada") ? "Sí" : "No");
                    tabla.put("clase", rs.getString("clase"));
                    tablas.add(tabla);
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al obtener tablas del docente: " + e.getMessage());
        }

        return tablas;
    }

    // 🔹 Consultar nombre y descripción de una tabla por ID
    public Map<String, String> obtenerTablaPorId(int tablaId) {
        Map<String, String> tabla = new HashMap<>();

        String sql = "SELECT t.nombre, t.descripcion, t.fecha, c.nombre AS clase " +
                     "FROM tablas_guardadas t " +
                     "JOIN clases c ON t.id_clase = c.id " +
                     "WHERE t.id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, tablaId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    tabla.put("nombre", rs.getString("nombre"));
                    tabla.put("descripcion", rs.getString("descripcion"));
                    tabla.put("fecha", rs.getString("fecha"));
                    tabla.put("clase", rs.getString("clase"));
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al obtener tabla por ID: " + e.getMessage());
        }

        return tabla;
    }

    // 🔹 Marcar tabla como enviada con trazabilidad
    public boolean marcarTablaComoEnviada(int tablaId, int idDocente) {
    String sql = "UPDATE tablas_guardadas " +
                 "SET enviada = 'Sí', enviada_por = ?, fecha_envio = CURRENT_DATE " +
                 "WHERE id = ?";

    try (PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, idDocente);
        ps.setInt(2, tablaId);
        return ps.executeUpdate() > 0;
    } catch (SQLException e) {
        System.err.println("❌ Error al marcar tabla como enviada: " + e.getMessage());
        return false;
    }
}

    // 🔹 Consultar estado completo de una tabla
    public Map<String, String> obtenerEstadoTabla(int tablaId) {
        Map<String, String> estado = new HashMap<>();
        String sql = "SELECT id, enviada, validada, enviada_por, fecha_envio, fecha " +
                     "FROM tablas_guardadas WHERE id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, tablaId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    estado.put("id", String.valueOf(rs.getInt("id")));
                    estado.put("enviada", rs.getBoolean("enviada") ? "Sí" : "No");
                    estado.put("validada", rs.getString("validada"));
                    estado.put("enviada_por", rs.getString("enviada_por"));
                    estado.put("fecha_envio", rs.getString("fecha_envio"));
                    estado.put("fecha", rs.getString("fecha"));
                }
            }
        } catch (Exception e) {
            System.out.println("❌ Error al obtener estado de tabla: " + e.getMessage());
        }
        return estado;
    }

    // 🔹 Listar todas las tablas guardadas por un docente (versión extendida con validada)
    public List<Map<String, String>> listarTablasPorDocente(int docenteId) {
    List<Map<String, String>> tablas = new ArrayList<>();

    String sql = "SELECT t.id, t.nombre, t.descripcion, t.fecha, t.enviada, t.validada, " +
                 "c.id_clase, c.nombre AS clase " +   // ✅ ahora usa id_clase
                 "FROM tablas_guardadas t " +
                 "JOIN clases c ON t.id_clase = c.id_clase " +  // ✅ corregido
                 "WHERE t.id_docente = ? " +
                 "ORDER BY t.fecha DESC";

    try (PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, docenteId);

        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Map<String, String> tabla = new HashMap<>();
                tabla.put("id", String.valueOf(rs.getInt("id")));
                tabla.put("nombre", rs.getString("nombre"));
                tabla.put("descripcion", rs.getString("descripcion"));
                tabla.put("fecha", rs.getString("fecha"));
                tabla.put("enviada", rs.getBoolean("enviada") ? "Sí" : "No");
                tabla.put("validada", rs.getString("validada"));
                tabla.put("id_clase", String.valueOf(rs.getInt("id_clase"))); // ✅ agregado
                tabla.put("clase", rs.getString("clase"));
                tablas.add(tabla);
            }
        }
    } catch (SQLException e) {
        System.err.println("❌ Error al listar tablas del docente: " + e.getMessage());
    }

    return tablas;
}
}