/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.mysymphony.proyecto_symphony1.dao;

import com.mysymphony.proyecto_symphony1.util.Conexion;
import com.mysymphony.proyecto_symphony1.modelo.Usuario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO institucional para gestión de usuarios en SymphonySIAS.
 * Incluye registro, validación, búsqueda, actualización, eliminación y listados.
 * Autor: Camila
 */
public class UsuarioDAO {

    private Connection conn;

    // 🔹 Constructor con conexión externa
    public UsuarioDAO(Connection conn) {
        this.conn = conn;
    }

    // 🔹 Constructor por defecto
    public UsuarioDAO() {
        this.conn = Conexion.getConnection();
        if (conn == null) {
            System.err.println("❌ No se pudo establecer conexión desde UsuarioDAO.");
        } else {
            System.out.println("✅ Conexión establecida desde UsuarioDAO.");
        }
    }

    // ============================================================
    // MÉTODOS CRUD Y DE VALIDACIÓN
    // ============================================================

    // 🔹 Insertar usuario y devolver su ID
    public int insertarUsuario(String nombre, String correo, String clave, String rol) throws SQLException {
        String sql = "INSERT INTO usuarios (nombre, correo, clave, rol, estado, usuario_creador) VALUES (?, ?, ?, ?, 'activo', 'sistema')";
        PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        ps.setString(1, nombre);
        ps.setString(2, correo);
        ps.setString(3, clave);
        ps.setString(4, rol);
        ps.executeUpdate();

        ResultSet rs = ps.getGeneratedKeys();
        if (rs.next()) {
            return rs.getInt(1); // devuelve id_usuario
        }
        return -1;
    }

    // 🔹 Insertar estudiante vinculado a un usuario existente
    public void insertarEstudiante(int idUsuario, String instrumento, String etapa) throws SQLException {
        String sql = "INSERT INTO estudiantes (id_usuario, instrumento, etapa_pedagogica, estado) VALUES (?, ?, ?, 'activo')";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, idUsuario);
        ps.setString(2, instrumento);
        ps.setString(3, etapa);
        ps.executeUpdate();
    }

    // 🔹 Insertar docente vinculado a un usuario existente
    public void insertarDocente(int idUsuario, String especialidad) throws SQLException {
        String sql = "INSERT INTO docentes (id_usuario, especialidad, estado) VALUES (?, ?, 'activo')";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, idUsuario);
        ps.setString(2, especialidad);
        ps.executeUpdate();
    }


    // ✅ Validar usuario por correo y clave
    public Usuario validarUsuario(String correo, String claveHash) {
        Usuario usuario = null;

        String sql = "SELECT u.id_usuario, u.correo, u.nombre, u.rol, u.clave, u.estado, u.usuario_creador, " +
                     "d.id_docente, d.instrumento, d.etapa, " +
                     "e.id_estudiante " +
                     "FROM usuarios u " +
                     "LEFT JOIN docentes d ON u.id_usuario = d.id_usuario " +
                     "LEFT JOIN estudiantes e ON u.id_usuario = e.id_usuario " +
                     "WHERE u.correo = ? AND u.clave = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, correo);
            ps.setString(2, claveHash);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    usuario = new Usuario();
                    usuario.setIdUsuario(rs.getInt("id_usuario"));
                    usuario.setCorreo(rs.getString("correo"));
                    usuario.setNombre(rs.getString("nombre"));
                    usuario.setRol(rs.getString("rol"));
                    usuario.setClave(rs.getString("clave"));
                    usuario.setEstado(rs.getString("estado"));
                    usuario.setUsuarioCreador(rs.getString("usuario_creador"));

                    // 🔹 Poblar datos de docente si aplica
                    int idDocente = rs.getInt("id_docente");
                    if (!rs.wasNull()) {
                        usuario.setIdDocente(idDocente);
                        usuario.setInstrumento(rs.getString("instrumento"));
                        usuario.setEtapa(rs.getString("etapa"));
                    }

                    // 🔹 Poblar datos de estudiante si aplica
                    int idEstudiante = rs.getInt("id_estudiante");
                    if (!rs.wasNull()) {
                        usuario.setIdEstudiante(idEstudiante);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Error en validarUsuario: " + e.getMessage());
        }

        return usuario;
    }


    // 🔹 Buscar usuario por correo
    public Usuario buscarPorCorreo(String correo) {
        String sql = "SELECT id_usuario, correo, nombre, rol, estado, usuario_creador FROM usuarios WHERE correo = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, correo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Usuario usuario = new Usuario();
                    usuario.setIdUsuario(rs.getInt("id_usuario"));
                    usuario.setCorreo(rs.getString("correo"));
                    usuario.setNombre(rs.getString("nombre"));
                    usuario.setRol(rs.getString("rol"));
                    usuario.setEstado(rs.getString("estado"));
                    usuario.setUsuarioCreador(rs.getString("usuario_creador"));
                    return usuario;
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Error en buscarPorCorreo: " + e.getMessage());
        }
        return null;
    }

    // 🔹 Registrar nuevo usuario institucional
    public boolean registrar(Usuario usuario, String claveHash) {
        String sql = "INSERT INTO usuarios (correo, nombre, clave, rol, estado, usuario_creador) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, usuario.getCorreo());
            ps.setString(2, usuario.getNombre());
            ps.setString(3, claveHash);
            ps.setString(4, usuario.getRol());
            ps.setString(5, "activo");
            ps.setString(6, "sistema");

            int filasAfectadas = ps.executeUpdate();

            int nuevoId = -1;
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    nuevoId = rs.getInt(1);
                    usuario.setIdUsuario(nuevoId);
                }
            }

            // 📌 Inserción automática en estudiantes
            if ("estudiante".equalsIgnoreCase(usuario.getRol()) && nuevoId != -1) {
                try {
                    String sqlEst = "INSERT INTO estudiantes (id_usuario, instrumento, direccion, telefono, etapa_pedagogica, fecha_ingreso, estado) " +
                                    "VALUES (?, 'Pendiente', 'Pendiente', 'Pendiente', ?, NOW(), 'activo')";
                    try (PreparedStatement psEst = conn.prepareStatement(sqlEst)) {
                        psEst.setInt(1, nuevoId);
                        psEst.setString(2, usuario.getEtapa()); // etapa inicial
                        psEst.executeUpdate();
                    }
                } catch (SQLException e) {
                    System.err.println("⚠️ Error al insertar en estudiantes: " + e.getMessage());
                }
            }

            // 📌 Inserción automática en docentes
            if ("docente".equalsIgnoreCase(usuario.getRol()) && nuevoId != -1) {
                try {
                    String sqlDoc = "INSERT INTO docentes (id_usuario, especialidad, fecha_ingreso, estado) " +
                                    "VALUES (?, ?, NOW(), 'activo')";
                    try (PreparedStatement psDoc = conn.prepareStatement(sqlDoc)) {
                        psDoc.setInt(1, nuevoId);
                        psDoc.setString(2, usuario.getInstrumento()); // aquí usas instrumento como especialidad
                        psDoc.executeUpdate();
                    }
                } catch (SQLException e) {
                    System.err.println("⚠️ Error al insertar en docentes: " + e.getMessage());
                }
            }

            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.err.println("❌ Error al registrar usuario en usuarios: " + e.getMessage());
            return false;
        }
    }

    // ============================================================
    // MÉTODOS DE LISTADO Y UTILIDAD
    // ============================================================

    public List<Usuario> listarTodos() {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT id_usuario, correo, nombre, rol, estado, usuario_creador FROM usuarios";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Usuario usuario = new Usuario();
                usuario.setIdUsuario(rs.getInt("id_usuario"));
                usuario.setCorreo(rs.getString("correo"));
                usuario.setNombre(rs.getString("nombre"));
                usuario.setRol(rs.getString("rol"));
                usuario.setEstado(rs.getString("estado"));
                usuario.setUsuarioCreador(rs.getString("usuario_creador"));
                lista.add(usuario);
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al listar usuarios: " + e.getMessage());
        }
        return lista;
    }

        public int contarTodos() {
        String sql = "SELECT COUNT(*) FROM usuarios";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al contar usuarios: " + e.getMessage());
        }
        return 0;
    }

    public boolean eliminar(int id) {
        String sql = "DELETE FROM usuarios WHERE id_usuario = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("❌ Error al eliminar usuario con ID " + id + ": " + e.getMessage());
        }
        return false;
    }

    public int contarDocentesActivos() {
        String sql = "SELECT COUNT(*) FROM usuarios WHERE rol = 'docente' AND estado = 'activo'";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al contar docentes activos: " + e.getMessage());
        }
        return 0;
    }

    public Usuario obtenerPorId(int id) {
        String sql = "SELECT id_usuario, correo, nombre, rol, estado, usuario_creador FROM usuarios WHERE id_usuario = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Usuario u = new Usuario();
                    u.setIdUsuario(rs.getInt("id_usuario"));
                    u.setCorreo(rs.getString("correo"));
                    u.setNombre(rs.getString("nombre"));
                    u.setRol(rs.getString("rol"));
                    u.setEstado(rs.getString("estado"));
                    u.setUsuarioCreador(rs.getString("usuario_creador"));
                    return u;
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al obtener usuario por ID: " + e.getMessage());
        }
        return null;
    }

    public boolean actualizar(Usuario u) {
        String sql = "UPDATE usuarios SET nombre = ?, correo = ?, rol = ?, estado = ? WHERE id_usuario = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, u.getNombre());
            ps.setString(2, u.getCorreo());
            ps.setString(3, u.getRol());
            ps.setString(4, u.getEstado());
            ps.setInt(5, u.getIdUsuario());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("❌ Error al actualizar usuario ID " + u.getIdUsuario() + ": " + e.getMessage());
        }
        return false;
    }

    public boolean insertar(Usuario u) {
        String sql = "INSERT INTO usuarios (nombre, correo, rol, estado, usuario_creador) VALUES (?, ?, ?, 'activo', 'sistema')";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, u.getNombre());
            ps.setString(2, u.getCorreo());
            ps.setString(3, u.getRol());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("❌ Error al insertar usuario: " + e.getMessage());
        }
        return false;
    }

    public List<Usuario> listarDocentes() {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT id_usuario, nombre, correo FROM usuarios WHERE rol = 'docente' AND estado = 'activo'";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Usuario u = new Usuario();
                u.setIdUsuario(rs.getInt("id_usuario"));
                u.setNombre(rs.getString("nombre"));
                u.setCorreo(rs.getString("correo"));
                lista.add(u);
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al listar docentes: " + e.getMessage());
        }
        return lista;
    }

    public boolean registrarEstudiante(String nombre, String instrumento, String etapa) {
        String sqlUsuario = "INSERT INTO usuarios (nombre, rol, estado, usuario_creador) VALUES (?, 'estudiante', 'activo', 'sistema')";
        String sqlEstudiante = "INSERT INTO estudiantes (nombre, instrumento, etapa_pedagogica, id_usuario) VALUES (?, ?, ?, ?)";

        try (PreparedStatement psUsuario = conn.prepareStatement(sqlUsuario, Statement.RETURN_GENERATED_KEYS)) {
            psUsuario.setString(1, nombre);

            int filas = psUsuario.executeUpdate();
            if (filas > 0) {
                try (ResultSet rs = psUsuario.getGeneratedKeys()) {
                    if (rs.next()) {
                        int idGenerado = rs.getInt(1);
                        try (PreparedStatement psEst = conn.prepareStatement(sqlEstudiante)) {
                            psEst.setString(1, nombre);
                            psEst.setString(2, instrumento);
                            psEst.setString(3, etapa);
                            psEst.setInt(4, idGenerado);
                            return psEst.executeUpdate() > 0;
                        }
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al registrar estudiante desde admin: " + e.getMessage());
        }
        return false;
    }

    public int obtenerUltimoId() {
        String sql = "SELECT LAST_INSERT_ID() AS id";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al obtener último id_usuario: " + e.getMessage());
        }
        return -1;
    }

    public int obtenerIdPorNombre(String nombre) {
        int id = -1;
        String sql = "SELECT id_usuario FROM usuarios WHERE nombre = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nombre);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    id = rs.getInt("id_usuario");
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al obtener ID por nombre: " + e.getMessage());
        }
        return id;
    }

    // 📝 Registrar usuario básico (solo nombre, correo, clave, rol, estado)
    public boolean registrarBasico(Usuario u) throws SQLException {
        String sql = "INSERT INTO usuarios (nombre, correo, clave, rol, estado, fecha_registro) " +
                     "VALUES (?, ?, ?, ?, 'activo', NOW())";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, u.getNombre());
            ps.setString(2, u.getCorreo());
            ps.setString(3, u.getClave());
            ps.setString(4, u.getRol());
            return ps.executeUpdate() > 0;
        }
    }

}
