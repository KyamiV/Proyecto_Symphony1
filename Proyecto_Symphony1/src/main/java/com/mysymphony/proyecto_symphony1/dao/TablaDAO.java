/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mysymphony.proyecto_symphony1.dao;

import java.sql.*;
import java.util.*;

/**
 * DAO institucional para gestionar tablas validadas.
 * Autor: Camila
 * Propósito:
 *   - Consultar tablas/clases validadas con filtros
 *   - Retornar datos con trazabilidad completa
 */
public class TablaDAO {
    private final Connection conn;

    public TablaDAO(Connection conn) {
        this.conn = conn;
    }

    // 🔹 Certificar tabla (versión simple)
    public boolean certificarTabla(int tablaId, int idEstudiante, int idClase, String usuarioAdmin) throws SQLException {
        String sql = "INSERT INTO certificados_estudiante (id_tabla, id_estudiante, id_clase, fecha_emision, estado, usuario_admin) " +
                     "VALUES (?, ?, ?, CURRENT_DATE, 'Emitido', ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, tablaId);
            ps.setInt(2, idEstudiante);
            ps.setInt(3, idClase);
            ps.setString(4, usuarioAdmin);
            return ps.executeUpdate() > 0;
        }
    }
    
    // 🔹 Listar todas las tablas pendientes (no validadas ni certificadas)
public List<Map<String, String>> listarTablasPendientes() throws SQLException {
    List<Map<String, String>> tablasPendientes = new ArrayList<>();

    String sql = "SELECT t.id AS id_tabla, " +
                 "c.id_clase, c.nombre_clase AS nombre, c.instrumento AS instrumento, c.etapa, " +
                 "t.descripcion, t.fecha_envio, t.usuario_editor, " +
                 "d.id_docente, u.nombre AS docente " +
                 "FROM tablas_guardadas t " +
                 "JOIN clases c ON t.id_clase = c.id_clase " +
                 "JOIN docentes d ON t.id_docente = d.id_docente " +
                 "JOIN usuarios u ON d.id_usuario = u.id_usuario " +
                 "WHERE t.validada != 'Sí' " +
                 "AND NOT EXISTS (SELECT 1 FROM certificados_estudiante ce WHERE ce.id_tabla = t.id) " +
                 "ORDER BY t.fecha_envio DESC";

    try (PreparedStatement ps = conn.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {

        while (rs.next()) {
            Map<String, String> tabla = new HashMap<>();
            int tablaId = rs.getInt("id_tabla");

            // 👇 Identificadores
            tabla.put("id_tabla", String.valueOf(tablaId));
            tabla.put("id_clase", String.valueOf(rs.getInt("id_clase")));
            tabla.put("id_docente", String.valueOf(rs.getInt("id_docente")));

            // 👇 Datos descriptivos
            tabla.put("nombre", rs.getString("nombre"));
            tabla.put("instrumento", rs.getString("instrumento"));
            tabla.put("etapa", rs.getString("etapa"));
            tabla.put("descripcion", rs.getString("descripcion"));
            tabla.put("fecha_envio", rs.getString("fecha_envio"));
            tabla.put("docente", rs.getString("docente"));
            tabla.put("usuario_editor", rs.getString("usuario_editor"));

            // 📊 Subconsulta para contar y promediar notas por tabla
            String sqlNotas = "SELECT COUNT(*) AS cantidad, AVG(nota) AS promedio FROM notas_clase WHERE id_tabla = ?";
            try (PreparedStatement psNotas = conn.prepareStatement(sqlNotas)) {
                psNotas.setInt(1, tablaId);
                try (ResultSet rsNotas = psNotas.executeQuery()) {
                    if (rsNotas.next()) {
                        tabla.put("cantidad_notas", String.valueOf(rsNotas.getInt("cantidad")));
                        tabla.put("promedio_notas", String.format("%.2f", rsNotas.getDouble("promedio")));
                    }
                }
            }

            tablasPendientes.add(tabla);
        }
    }

    return tablasPendientes;
}

    // 🔹 Verificar si ya está validada
    public boolean yaValidada(int idTabla) throws SQLException {
        String sql = "SELECT validada FROM tablas_guardadas WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idTabla);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return "Sí".equalsIgnoreCase(rs.getString("validada"));
                }
            }
        }
        return false;
    }

    // 🔹 Verificar si ya está certificada
    public boolean yaCertificada(int idTabla) throws SQLException {
        String sql = "SELECT COUNT(*) FROM certificados_estudiante WHERE id_tabla = ? AND estado = 'Emitido'";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idTabla);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }
    // 🔹 Marcar la tabla como certificada en tablas_guardadas
    public void marcarComoCertificada(int idTabla) throws SQLException {
    String sql = "UPDATE tablas_guardadas " +
                 "SET estado = 'Emitido', fecha_validacion = NOW() " +
                 "WHERE id = ?";
    try (PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, idTabla);
        ps.executeUpdate();
    }
}

    // 🔹 Listar todas las tablas/clases validadas
public List<Map<String, String>> listarTablasValidadas(String docenteFiltro, java.sql.Date desde, java.sql.Date hasta) throws SQLException {
    List<Map<String, String>> tablasValidadas = new ArrayList<>();

    StringBuilder sql = new StringBuilder(
        "SELECT t.id AS id_tabla, " +
        "c.id_clase, c.nombre_clase AS nombre, c.instrumento AS instrumento, c.etapa, " +
        "t.descripcion, t.fecha_envio, t.fecha_validacion, t.usuario_editor, " +
        "d.id_docente, u.nombre AS docente, " +
        "(SELECT estado FROM certificados_estudiante ce WHERE ce.id_tabla = t.id LIMIT 1) AS estado_certificado " +
        "FROM tablas_guardadas t " +
        "JOIN clases c ON t.id_clase = c.id_clase " +
        "JOIN docentes d ON t.id_docente = d.id_docente " +
        "JOIN usuarios u ON d.id_usuario = u.id_usuario " +
        "WHERE t.validada = 'Sí'"
    );

    List<Object> parametros = new ArrayList<>();

    if (docenteFiltro != null && !docenteFiltro.trim().isEmpty()) {
        sql.append(" AND LOWER(u.nombre) LIKE ?");
        parametros.add("%" + docenteFiltro.toLowerCase() + "%");
    }
    if (desde != null) {
        sql.append(" AND t.fecha_validacion >= ?");
        parametros.add(desde);
    }
    if (hasta != null) {
        sql.append(" AND t.fecha_validacion <= ?");
        parametros.add(hasta);
    }

    sql.append(" ORDER BY t.fecha_validacion DESC");

    try (PreparedStatement ps = conn.prepareStatement(sql.toString())) {
        for (int i = 0; i < parametros.size(); i++) {
            ps.setObject(i + 1, parametros.get(i));
        }

        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Map<String, String> tabla = new HashMap<>();
                int tablaId = rs.getInt("id_tabla");

                // 👇 Identificadores claros
                tabla.put("id_tabla", String.valueOf(tablaId));
                tabla.put("id_clase", String.valueOf(rs.getInt("id_clase")));
                tabla.put("id_docente", String.valueOf(rs.getInt("id_docente")));

                // 👇 Datos descriptivos
                tabla.put("nombre", rs.getString("nombre"));
                tabla.put("instrumento", rs.getString("instrumento"));
                tabla.put("etapa", rs.getString("etapa"));
                tabla.put("descripcion", rs.getString("descripcion"));
                tabla.put("fecha_envio", rs.getString("fecha_envio"));
                tabla.put("fecha_validacion", rs.getString("fecha_validacion"));
                tabla.put("docente", rs.getString("docente"));
                tabla.put("usuario_editor", rs.getString("usuario_editor"));

                // ✅ Estado certificado desde subconsulta
                tabla.put("estado", rs.getString("estado_certificado"));

                // 📊 Subconsulta para contar y promediar notas por tabla
                String sqlNotas = "SELECT COUNT(*) AS cantidad, AVG(nota) AS promedio FROM notas_clase WHERE id_tabla = ?";
                try (PreparedStatement psNotas = conn.prepareStatement(sqlNotas)) {
                    psNotas.setInt(1, tablaId);
                    try (ResultSet rsNotas = psNotas.executeQuery()) {
                        if (rsNotas.next()) {
                            tabla.put("cantidad_notas", String.valueOf(rsNotas.getInt("cantidad")));
                            tabla.put("promedio_notas", String.format("%.2f", rsNotas.getDouble("promedio")));
                        }
                    }
                }

                tablasValidadas.add(tabla);
            }
        }
    }

    return tablasValidadas;
}

    // 🔹 Obtener datos completos de una tabla guardada (clase, docente, instrumento, etapa)
    public Map<String, String> obtenerDatosTabla(int tablaId) throws SQLException {
        Map<String, String> datos = new HashMap<>();
        String sql = "SELECT t.id_clase, t.id_docente, c.instrumento, c.etapa " +
                     "FROM tablas_guardadas t " +
                     "JOIN clases c ON t.id_clase = c.id_clase " +
                     "WHERE t.id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, tablaId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    datos.put("id_clase", String.valueOf(rs.getInt("id_clase")));
                    datos.put("id_docente", String.valueOf(rs.getInt("id_docente")));
                    datos.put("instrumento", rs.getString("instrumento"));
                    datos.put("etapa", rs.getString("etapa"));
                }
            }
        }
        return datos;
    }

    // 🔹 Obtener estudiantes asociados a una tabla guardada
    public List<Integer> obtenerEstudiantesPorTabla(int tablaId) throws SQLException {
        List<Integer> estudiantes = new ArrayList<>();

        // Primero obtener la clase asociada a la tabla
        int idClase = obtenerClasePorTabla(tablaId);
        if (idClase == -1) {
            return estudiantes; // No hay clase asociada
        }

        // Luego obtener los estudiantes inscritos en esa clase
        String sql = "SELECT id_estudiante FROM inscripciones_clase WHERE id_clase = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idClase);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    estudiantes.add(rs.getInt("id_estudiante"));
                }
            }
        }
        return estudiantes;
    }

    // 🔹 Obtener estado de una tabla guardada
    public Map<String, String> obtenerEstadoTabla(int tablaId) throws SQLException {
        Map<String, String> estado = new HashMap<>();
        String sql = "SELECT enviada, fecha_envio, fecha_validacion FROM tablas_guardadas WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, tablaId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    estado.put("enviada", rs.getBoolean("enviada") ? "Sí" : "No");
                    estado.put("fecha_envio", rs.getString("fecha_envio"));
                    estado.put("fecha_validacion", rs.getString("fecha_validacion"));
                }
            }
        }
        return estado;
    }

        // 🔹 Obtener el id_clase asociado a una tabla guardada
    public int obtenerClasePorTabla(int tablaId) throws SQLException {
        String sql = "SELECT id_clase FROM tablas_guardadas WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, tablaId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id_clase");
                }
            }
        }
        return -1; // No encontrada
    }
}