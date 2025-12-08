/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.mysymphony.proyecto_symphony1.dao;

import com.mysymphony.proyecto_symphony1.modelo.Docente;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO institucional para gestión de docentes.
 * Autor: Camila
 * Trazabilidad:
 *   - Permite listar, obtener, insertar, actualizar y eliminar docentes.
 *   - Usado en flujos de administración y asignación de clases.
 */
public class DocenteDAO {
    private final Connection conn;

    public DocenteDAO(Connection conn) {
        this.conn = conn;
    }

    // 🔹 Obtener un docente por ID (perfil completo)
    public Docente obtenerPorId(int idDocente) {
        String sql = "SELECT id_docente, nombre, apellido, correo, telefono, direccion, fecha_ingreso, estado, nivel_tecnico " +
                     "FROM docentes WHERE id_docente = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idDocente);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Docente d = new Docente();
                    d.setId(rs.getInt("id_docente"));
                    d.setNombre(rs.getString("nombre"));
                    d.setApellido(rs.getString("apellido"));
                    d.setCorreo(rs.getString("correo"));
                    d.setTelefono(rs.getString("telefono"));
                    d.setDireccion(rs.getString("direccion"));
                    d.setFechaIngreso(rs.getDate("fecha_ingreso")); // 📌 institucional
                    d.setEstado(rs.getString("estado"));            // 📌 institucional
                    d.setNivelTecnico(rs.getString("nivel_tecnico"));// 📌 editable
                    return d;
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al obtener docente por ID: " + e.getMessage());
        }
        return null; // si no se encuentra
    }

    // 🔹 Actualizar perfil docente (solo datos personales y nivel técnico)
    public boolean actualizarPerfilDocente(Docente d) {
        String sql = "UPDATE docentes SET nombre=?, apellido=?, correo=?, telefono=?, direccion=?, nivel_tecnico=? " +
                     "WHERE id_docente=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, d.getNombre());
            ps.setString(2, d.getApellido());
            ps.setString(3, d.getCorreo());
            ps.setString(4, d.getTelefono());
            ps.setString(5, d.getDireccion());
            ps.setString(6, d.getNivelTecnico()); // ✅ editable por el docente
            ps.setInt(7, d.getId());

            int filas = ps.executeUpdate();
            return filas > 0; // true si se actualizó al menos un registro
        } catch (SQLException e) {
            System.err.println("❌ Error al actualizar perfil docente: " + e.getMessage());
            return false;
        }
    }

    // 🔹 Listar todos los docentes activos
    public List<Docente> listarDocentes() throws SQLException {
        List<Docente> lista = new ArrayList<>();
        String sql = "SELECT id_docente, nombre, apellido, correo, telefono, direccion, fecha_ingreso, estado " +
                     "FROM docentes WHERE estado = 'activo' ORDER BY nombre ASC";

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Docente d = new Docente();
                d.setId(rs.getInt("id_docente"));
                d.setNombre(rs.getString("nombre"));
                d.setApellido(rs.getString("apellido"));
                d.setCorreo(rs.getString("correo"));
                d.setTelefono(rs.getString("telefono"));
                d.setDireccion(rs.getString("direccion"));

                java.sql.Date sqlDate = rs.getDate("fecha_ingreso");
                d.setFechaIngreso(sqlDate != null ? sqlDate : null);

                d.setEstado(rs.getString("estado"));
                lista.add(d);
            }
        }
        return lista;
    }

    // ✅ Listar todos los docentes (sin filtro de estado)
    public List<Docente> listarTodos() throws SQLException {
        List<Docente> lista = new ArrayList<>();
        String sql = "SELECT id_docente, nombre, apellido, correo, telefono, direccion, fecha_ingreso, estado " +
                     "FROM docentes ORDER BY nombre ASC";

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Docente d = new Docente();
                d.setId(rs.getInt("id_docente"));
                d.setNombre(rs.getString("nombre"));
                d.setApellido(rs.getString("apellido"));
                d.setCorreo(rs.getString("correo"));
                d.setTelefono(rs.getString("telefono"));
                d.setDireccion(rs.getString("direccion"));

                java.sql.Date sqlDate = rs.getDate("fecha_ingreso");
                d.setFechaIngreso(sqlDate != null ? sqlDate : null);

                d.setEstado(rs.getString("estado"));
                lista.add(d);
            }
        }
        return lista;
    }

    // 🔹 Listar docentes básicos (id, nombre, correo)
    public List<Docente> obtenerTodosLosDocentes() {
        List<Docente> lista = new ArrayList<>();
        String sql = "SELECT id_docente, nombre, correo FROM docentes";

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Docente d = new Docente();
                d.setId(rs.getInt("id_docente"));
                d.setNombre(rs.getString("nombre"));
                d.setCorreo(rs.getString("correo"));
                lista.add(d);
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al obtener docentes: " + e.getMessage());
        }
        return lista;
    }

    // 🔹 Insertar nuevo docente
    public boolean insertarDocente(Docente d) {
    String sql = "INSERT INTO docentes (id_usuario, nombre, apellido, correo, telefono, direccion, fecha_ingreso, estado, nivel_tecnico) " +
                 "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
    try (PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, d.getIdUsuario()); // FK hacia usuarios
        ps.setString(2, d.getNombre());
        ps.setString(3, d.getApellido());
        ps.setString(4, d.getCorreo());
        ps.setString(5, d.getTelefono());
        ps.setString(6, d.getDireccion());
        ps.setDate(7, d.getFechaIngreso() != null ? d.getFechaIngreso() : new java.sql.Date(System.currentTimeMillis()));
        ps.setString(8, d.getEstado());
        ps.setString(9, d.getNivelTecnico());
        return ps.executeUpdate() > 0;
    } catch (SQLException e) {
        System.err.println("❌ Error al insertar docente: " + e.getMessage());
        return false;
    }
}

    // 🔹 Actualizar docente existente
    public boolean actualizarDocente(Docente d) {
        String sql = "UPDATE docentes SET nombre=?, apellido=?, correo=?, telefono=?, direccion=?, fecha_ingreso=?, estado=? " +
                     "WHERE id_docente=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, d.getNombre());
            ps.setString(2, d.getApellido());
            ps.setString(3, d.getCorreo());
            ps.setString(4, d.getTelefono());
            ps.setString(5, d.getDireccion());

            if (d.getFechaIngreso() != null) {
                ps.setDate(6, d.getFechaIngreso());
            } else {
                ps.setNull(6, Types.DATE);
            }

            ps.setString(7, d.getEstado());
            ps.setInt(8, d.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("❌ Error al actualizar docente: " + e.getMessage());
            return false;
        }
    }

    // 🔹 Eliminar docente por ID
    public boolean eliminarDocente(int idDocente) {
        String sql = "DELETE FROM docentes WHERE id_docente=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idDocente);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("❌ Error al eliminar docente: " + e.getMessage());
            return false;
        }
    }
  }
