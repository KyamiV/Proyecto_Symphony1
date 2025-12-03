/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.mysymphony.proyecto_symphony1.dao;

import com.mysymphony.proyecto_symphony1.util.Conexion;
import com.mysymphony.proyecto_symphony1.modelo.Auditoria;


import java.sql.*;
import java.util.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * DAO para consultar y registrar auditoría institucional con trazabilidad avanzada.
 * Autor: camiv
 */
public class AuditoriaDAO {

    private Connection conn;

    public AuditoriaDAO(Connection conn) {
        this.conn = conn;
    }

    public AuditoriaDAO() {
        this.conn = Conexion.getConnection();
        if (conn == null) {
            System.err.println("❌ No se pudo establecer conexión desde AuditoriaDAO.");
        } else {
            System.out.println("✅ Conexión establecida desde AuditoriaDAO.");
        }
    }

    public List<Auditoria> filtrarPorFechas(String fechaInicio, String fechaFin) throws Exception {
        List<Auditoria> lista = new ArrayList<>();
        String sql = "SELECT * FROM auditoria WHERE fecha_registro BETWEEN ? AND ? ORDER BY fecha_registro DESC";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, fechaInicio);
            ps.setString(2, fechaFin);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Auditoria a = new Auditoria();
                    a.setId(rs.getInt("id_auditoria"));
                    a.setUsuario(rs.getString("usuario"));
                    a.setRol(rs.getString("rol"));
                    a.setModulo(rs.getString("modulo"));
                    a.setAccion(rs.getString("accion"));
                    a.setDetalle(rs.getString("detalle"));
                    a.setFechaRegistro(rs.getTimestamp("fecha_registro"));
                    lista.add(a);
                }
            }
        }
        return lista;
    }
    
    public int limpiarAuditoria() throws SQLException {
    String sql = "DELETE FROM auditoria";
    try (PreparedStatement ps = conn.prepareStatement(sql)) {
        return ps.executeUpdate(); // devuelve cantidad de registros eliminados
    }
}
    
    // Ejemplo simple de paginación
public List<Map<String, String>> obtenerAuditoriaPaginada(int pagina, int limite) {
    List<Map<String, String>> lista = new ArrayList<>();
    String sql = "SELECT usuario, rol, accion, modulo, tabla, id_referencia, fecha_registro " +
                 "FROM auditoria ORDER BY fecha_registro DESC LIMIT ? OFFSET ?";
    int offset = (pagina - 1) * limite;

    try (Connection con = Conexion.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {
        
        ps.setInt(1, limite);
        ps.setInt(2, offset);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            Map<String, String> fila = new HashMap<>();
            fila.put("usuario", rs.getString("usuario"));
            fila.put("rol", rs.getString("rol"));
            fila.put("accion", rs.getString("accion"));
            fila.put("modulo", rs.getString("modulo"));
            fila.put("id_tabla", rs.getString("id_tabla"));
            fila.put("id_referencia", rs.getString("id_referencia"));
            fila.put("fecha_registro", rs.getString("fecha_registro"));
            lista.add(fila);
        }
    } catch (SQLException e) {
        System.err.println("❌ Error al obtener auditoría paginada: " + e.getMessage());
    }
    return lista;
}

    // 🔹 Registrar auditoría básica
    public void registrarAuditoria(String descripcion, String modulo, String usuario) {
        Map<String, String> registro = new HashMap<>();
        registro.put("usuario", usuario);
        registro.put("rol", "administrador");
        registro.put("accion", descripcion);
        registro.put("modulo", modulo);
        registro.put("id_tabla", null);
        registro.put("id_referencia", null);

        registrarAccion(registro);
    }

    // 🔹 Registrar acción institucional
    public void registrarAccion(Map<String, String> registro) {
    String sql = "INSERT INTO auditoria (usuario, rol, modulo, accion, detalle) " +
                 "VALUES (?, ?, ?, ?, ?)"; // fecha_registro se llena sola en BD

    try (Connection con = Conexion.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {

        String usuario = registro.getOrDefault("usuario", "—");
        String rol     = registro.getOrDefault("rol", "—");
        String modulo  = registro.getOrDefault("modulo", "—");
        String accion  = registro.getOrDefault("accion", "—");
        String detalle = registro.getOrDefault("detalle", "—"); // mejor no dejar null

        ps.setString(1, usuario);
        ps.setString(2, rol);
        ps.setString(3, modulo);
        ps.setString(4, accion);
        ps.setString(5, detalle);

        int filas = ps.executeUpdate();

        if (filas > 0) {
            System.out.println("✅ Acción registrada en auditoría:");
            System.out.println("Usuario: " + usuario + " | Rol: " + rol + " | Módulo: " + modulo);
            System.out.println("Acción: " + accion);
        } else {
            System.out.println("⚠️ No se registró ninguna fila en auditoría.");
        }

    } catch (SQLException e) {
        System.err.println("❌ Error al registrar acción en auditoría: " + e.getMessage());
        e.printStackTrace();
    }
}

    // 🔹 Consultar todos los registros
    public List<Map<String, String>> obtenerTodos() {
        List<Map<String, String>> lista = new ArrayList<>();
        String sql = "SELECT usuario, rol, accion, modulo, id_tabla, id_referencia, fecha_registro " +
                     "FROM auditoria ORDER BY fecha_registro DESC";

        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Map<String, String> fila = new HashMap<>();
                fila.put("usuario", rs.getString("usuario"));
                fila.put("rol", rs.getString("rol"));
                fila.put("accion", rs.getString("accion"));
                fila.put("modulo", rs.getString("modulo"));
                fila.put("id_tabla", rs.getString("id_tabla"));
                fila.put("id_referencia", rs.getString("id_referencia"));
                fila.put("fecha_registro", rs.getString("fecha_registro"));
                lista.add(fila);
            }

        } catch (SQLException e) {
            System.err.println("❌ Error al obtener auditoría: " + e.getMessage());
        }

        return lista;
    }

    // 🔹 Filtrar por palabra clave
    public List<Map<String, String>> obtenerPorFiltro(String filtro) {
        List<Map<String, String>> lista = new ArrayList<>();
        String sql = "SELECT usuario, rol, accion, modulo, id_tabla, id_referencia, fecha_registro " +
                     "FROM auditoria WHERE usuario LIKE ? OR accion LIKE ? OR modulo LIKE ? ORDER BY fecha_registro DESC";

        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            String like = "%" + filtro + "%";
            ps.setString(1, like);
            ps.setString(2, like);
            ps.setString(3, like);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Map<String, String> fila = new HashMap<>();
                    fila.put("usuario", rs.getString("usuario"));
                    fila.put("rol", rs.getString("rol"));
                    fila.put("accion", rs.getString("accion"));
                    fila.put("modulo", rs.getString("modulo"));
                    fila.put("id_tabla", rs.getString("id_tabla"));
                    fila.put("id_referencia", rs.getString("id_referencia"));
                    fila.put("fecha_registro", rs.getString("fecha_registro"));
                    lista.add(fila);
                }
            }

        } catch (SQLException e) {
            System.err.println("❌ Error al filtrar auditoría: " + e.getMessage());
        }

        return lista;
    }

    // 🔹 Filtrar por rango de fechas
    public List<Map<String, String>> obtenerPorRangoFechas(String inicio, String fin) {
        List<Map<String, String>> lista = new ArrayList<>();
        String sql = "SELECT usuario, rol, accion, modulo, id_tabla, id_referencia, fecha_registro " +
                     "FROM auditoria WHERE fecha_registro BETWEEN ? AND ? ORDER BY fecha_registro DESC";

        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, inicio);
            ps.setString(2, fin);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Map<String, String> fila = new HashMap<>();
                fila.put("usuario", rs.getString("usuario"));
                fila.put("rol", rs.getString("rol"));
                fila.put("accion", rs.getString("accion"));
                fila.put("modulo", rs.getString("modulo"));
                fila.put("id_tabla", rs.getString("id_tabla"));
                fila.put("id_referencia", rs.getString("id_referencia"));
                fila.put("fecha_registro", rs.getString("fecha_registro"));
                lista.add(fila);
            }

        } catch (SQLException e) {
            System.err.println("❌ Error al filtrar por fechas: " + e.getMessage());
        }

        return lista;
    }

    // 🔹 Filtrar por usuario
    public List<Map<String, String>> obtenerPorUsuario(String usuario) {
        List<Map<String, String>> lista = new ArrayList<>();
        String sql = "SELECT usuario, rol, accion, modulo, id_tabla, id_referencia, fecha_registro " +
                     "FROM auditoria WHERE usuario = ? ORDER BY fecha_registro DESC";

        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, usuario);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Map<String, String> fila = new HashMap<>();
                fila.put("usuario", rs.getString("usuario"));
                fila.put("rol", rs.getString("rol"));
                fila.put("accion", rs.getString("accion"));
                fila.put("modulo", rs.getString("modulo"));
                fila.put("id_tabla", rs.getString("id_tabla"));
                fila.put("id_referencia", rs.getString("id_referencia"));
                fila.put("fecha_registro", rs.getString("fecha_registro"));
                lista.add(fila);
            }

        } catch (SQLException e) {
            System.err.println("❌ Error al filtrar por usuario: " + e.getMessage());
            e.printStackTrace();
        }

        return lista;
    }

       // 🔹 Listar auditoría
    public List<Map<String, String>> listarAuditoria() {
        List<Map<String, String>> lista = new ArrayList<>();

        String sql = "SELECT id_auditoria, usuario, rol, modulo, accion, fecha_registro " +
                     "FROM auditoria ORDER BY fecha_registro DESC";

        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Map<String, String> fila = new HashMap<>();
                fila.put("id_auditoria", rs.getString("id_auditoria"));
                fila.put("usuario", rs.getString("usuario"));
                fila.put("rol", rs.getString("rol"));
                fila.put("modulo", rs.getString("modulo"));
                fila.put("accion", rs.getString("accion"));
                fila.put("fecha_registro", rs.getString("fecha_registro"));
                lista.add(fila);
            }

            System.out.println("📌 Auditoría cargada: " + lista.size() + " registros");

        } catch (SQLException e) {
            System.err.println("❌ Error al listar auditoría: " + e.getMessage());
        }

        return lista;
    }

    /**
     * Registra una nueva acción en la auditoría institucional.
     * Recibe un mapa con usuario, rol, módulo y acción.
     */
    public void registrarActualizacionNota(String docente, String estudiante, int idClase, String nuevaNota) {
        String descripcion = "Docente " + docente + " actualizó nota de " + estudiante +
                             " en clase ID " + idClase + " a valor " + nuevaNota;

        Map<String, String> registro = new HashMap<>();
        registro.put("usuario", docente);
        registro.put("rol", "docente");
        registro.put("accion", "Actualizar nota");
        registro.put("modulo", "Gestión de notas por clase");
        registro.put("detalle", descripcion);
        registro.put("ip_origen", "sistema");

        registrarAccion(registro);
    }
    
}