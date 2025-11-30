/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mysymphony.proyecto_symphony1.dao;

import com.mysymphony.proyecto_symphony1.dto.CertificadoDTO;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CertificadoDAO {
    private final Connection conn;

    public CertificadoDAO(Connection conn) {
        this.conn = conn;
    }

    // 🔹 Emitir certificado
    public boolean emitirCertificado(CertificadoDTO cert) throws SQLException {
        String sql = "INSERT INTO certificados_estudiante " +
             "(id_tabla, id_clase, id_docente, id_estudiante, instrumento, etapa, fecha_emision, estado, usuario_admin, url_certificado) " +
             "VALUES (?, ?, ?, ?, ?, ?, NOW(), ?, ?, ?)";
    try (PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, cert.getIdTabla());
        ps.setInt(2, cert.getIdClase());
        ps.setInt(3, cert.getIdDocente());
        ps.setInt(4, cert.getIdEstudiante());
        ps.setString(5, cert.getInstrumento());
        ps.setString(6, cert.getEtapa());
        ps.setString(7, cert.getEstado() != null ? cert.getEstado() : "Emitido");
        ps.setString(8, cert.getUsuarioAdmin());
        ps.setString(9, cert.getUrlCertificado()); // 👈 nuevo campo
        return ps.executeUpdate() > 0;
    }
    }

    // 🔹 Listar todos los certificados
    public List<CertificadoDTO> listarCertificados() throws SQLException {
        List<CertificadoDTO> certificados = new ArrayList<>();
        String sql = "SELECT id_certificado, id_tabla, id_clase, id_docente, id_estudiante, instrumento, etapa, fecha_emision, estado, usuario_admin " +
                     "FROM certificados_estudiante ORDER BY fecha_emision DESC";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                CertificadoDTO cert = new CertificadoDTO();
                cert.setIdCertificado(rs.getInt("id_certificado"));
                cert.setIdTabla(rs.getInt("id_tabla"));
                cert.setIdClase(rs.getInt("id_clase"));
                cert.setIdDocente(rs.getInt("id_docente"));
                cert.setIdEstudiante(rs.getInt("id_estudiante"));
                cert.setInstrumento(rs.getString("instrumento"));
                cert.setEtapa(rs.getString("etapa"));
                cert.setFechaEmision(rs.getTimestamp("fecha_emision").toLocalDateTime());
                cert.setEstado(rs.getString("estado"));
                cert.setUsuarioAdmin(rs.getString("usuario_admin"));
                certificados.add(cert);
            }
        }
        return certificados;
    }

    // 🔹 Anular certificado
    public boolean anularCertificado(int idCertificado) throws SQLException {
        String sql = "UPDATE certificados_estudiante SET estado = 'Anulado' WHERE id_certificado = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idCertificado);
            return ps.executeUpdate() > 0;
        }
    }

    // 🔹 Listar certificados emitidos (solo activos)
    public List<Map<String, String>> listarCertificadosEmitidos() throws SQLException {
        List<Map<String, String>> certificados = new ArrayList<>();
        String sql = "SELECT id_certificado, id_estudiante, id_clase, instrumento, etapa, fecha_emision, estado, usuario_admin " +
                     "FROM certificados_estudiante WHERE estado = 'Emitido' ORDER BY fecha_emision DESC";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Map<String, String> fila = new HashMap<>();
                fila.put("id_certificado", rs.getString("id_certificado"));
                fila.put("id_estudiante", rs.getString("id_estudiante"));
                fila.put("id_clase", rs.getString("id_clase"));
                fila.put("instrumento", rs.getString("instrumento"));
                fila.put("etapa", rs.getString("etapa"));
                fila.put("fecha_emision", rs.getString("fecha_emision"));
                fila.put("estado", rs.getString("estado"));
                fila.put("usuario_admin", rs.getString("usuario_admin"));
                certificados.add(fila);
            }
        }
        return certificados;
    }
}