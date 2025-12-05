/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.mysymphony.proyecto_symphony1.dao;

import com.mysymphony.proyecto_symphony1.util.Conexion;

import java.sql.*;
import java.util.*;

public class BitacoraDAO {

    private Connection conn;

    public BitacoraDAO() {
        this.conn = Conexion.getConnection();
        if (conn == null) {
            System.err.println("❌ No se pudo establecer conexión desde BitacoraDAO.");
        } else {
            System.out.println("✅ Conexión establecida desde BitacoraDAO.");
        }
    }

    public BitacoraDAO(Connection conn) {
        this.conn = conn;
    }

    /**
     * CREATE → Registra una acción en la bitácora institucional.
     *
     * @param descripcion Descripción de la acción realizada
     * @param usuario     Usuario que ejecutó la acción
     * @param rol         Rol del usuario
     * @param modulo      Módulo o sección del sistema donde ocurrió
     * @return true si se registró correctamente, false si hubo error
     */
    public boolean registrarAccion(String descripcion, String usuario, String rol, String modulo) {
    String sql = "INSERT INTO bitacora_validaciones (usuario, rol, modulo, accion, fecha_registro) " +
                 "VALUES (?, ?, ?, ?, CURRENT_TIMESTAMP)";

    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setString(1, usuario);
        stmt.setString(2, rol);
        stmt.setString(3, modulo);
        stmt.setString(4, descripcion);

        int filas = stmt.executeUpdate();
        System.out.println("✅ Acción registrada en bitácora institucional (" + filas + " fila(s))");
        return filas > 0;
    } catch (SQLException e) {
        System.err.println("❌ Error al registrar acción en bitácora: " + e.getMessage());
        return false;
    }
}

    
    // ✅ Eliminar todos los registros de bitácora_validaciones
    public boolean limpiarBitacora() {
        String sql = "DELETE FROM bitacora_validaciones";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            System.err.println("❌ Error al limpiar bitácora_validaciones: " + e.getMessage());
            return false;
        }
    }

        /**
     * READ → Consulta todos los registros de la bitácora institucional.
     *
     * @return Lista de registros con usuario, rol, módulo, acción y fecha_registro
     */
    public List<Map<String, String>> obtenerTodos() {
    List<Map<String, String>> lista = new ArrayList<>();
    String sql = "SELECT id_accion, usuario, rol, modulo, accion, fecha_registro " +
                 "FROM bitacora_validaciones ORDER BY fecha_registro DESC";

    try (Connection con = Conexion.getConnection();
         PreparedStatement ps = con.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {

        while (rs.next()) {
            Map<String, String> fila = new HashMap<>();
            fila.put("id_accion", String.valueOf(rs.getInt("id_accion")));
            fila.put("usuario", rs.getString("usuario"));
            fila.put("rol", rs.getString("rol"));
            fila.put("modulo", rs.getString("modulo"));
            fila.put("accion", rs.getString("accion"));

            // ✅ Corrección aquí
            Timestamp ts = rs.getTimestamp("fecha_registro");
            fila.put("fecha_registro", (ts != null) ? ts.toString() : "");

            lista.add(fila);
        }

    } catch (SQLException e) {
        System.err.println("❌ Error al obtener bitácora: " + e.getMessage());
    }

    return lista;
}

    /**
     * UPDATE → Actualiza la descripción de una acción registrada.
     * (No recomendado en bitácora institucional, pero disponible si se requiere)
     *
     * @param idAccion        ID del registro en bitácora
     * @param nuevaDescripcion Nueva descripción de la acción
     * @return true si se actualizó correctamente, false si hubo error
     */
    public boolean actualizarAccion(int idAccion, String nuevaDescripcion) {
        String sql = "UPDATE bitacora_validaciones SET accion = ? WHERE id_accion = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nuevaDescripcion);
            stmt.setInt(2, idAccion);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("❌ Error al actualizar acción en bitácora: " + e.getMessage());
            return false;
        }
    }

    /**
     * DELETE → Elimina un registro de la bitácora.
     * (No recomendado en bitácora institucional, pero disponible si se requiere)
     *
     * @param idAccion ID del registro en bitácora
     * @return true si se eliminó correctamente, false si hubo error
     */
    public boolean eliminarAccion(int idAccion) {
        String sql = "DELETE FROM bitacora_validaciones WHERE id_accion = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idAccion);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("❌ Error al eliminar acción en bitácora: " + e.getMessage());
            return false;
        }
    }

    /**
     * Método específico para registrar actualización de nota en la bitácora.
     */
    public boolean registrarActualizacionNota(String docente, String estudiante, int idClase, String nuevaNota) {
        String descripcion = "Docente " + docente + " actualizó nota de " + estudiante +
                             " en clase ID " + idClase + " a valor " + nuevaNota;

        return registrarAccion(descripcion, docente, "docente", "Gestión de notas por clase");
    }
    
    
}