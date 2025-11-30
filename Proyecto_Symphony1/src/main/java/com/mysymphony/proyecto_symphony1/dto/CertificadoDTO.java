/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mysymphony.proyecto_symphony1.dto;

import java.time.LocalDateTime;

/**
 * DTO institucional para representar certificados emitidos
 * Incluye trazabilidad de tabla, clase, docente, estudiante y administrador
 * Autor: Camila
 */
public class CertificadoDTO {
    private int idCertificado;
    private int idTabla;
    private int idClase;
    private int idDocente;
    private int idEstudiante;   // 👈 campo agregado
    private String instrumento;
    private String etapa;
    private LocalDateTime fechaEmision;
    private String estado;
    private String usuarioAdmin;   // 👈 administrador que emite
    private String urlCertificado; // 👈 ruta del PDF generado

    // 🔹 Getters y Setters
    public int getIdCertificado() { return idCertificado; }
    public void setIdCertificado(int idCertificado) { this.idCertificado = idCertificado; }

    public int getIdTabla() { return idTabla; }
    public void setIdTabla(int idTabla) { this.idTabla = idTabla; }

    public int getIdClase() { return idClase; }
    public void setIdClase(int idClase) { this.idClase = idClase; }

    public int getIdDocente() { return idDocente; }
    public void setIdDocente(int idDocente) { this.idDocente = idDocente; }

    public int getIdEstudiante() { return idEstudiante; }
    public void setIdEstudiante(int idEstudiante) { this.idEstudiante = idEstudiante; }

    public String getInstrumento() { return instrumento; }
    public void setInstrumento(String instrumento) { this.instrumento = instrumento; }

    public String getEtapa() { return etapa; }
    public void setEtapa(String etapa) { this.etapa = etapa; }

    public LocalDateTime getFechaEmision() { return fechaEmision; }
    public void setFechaEmision(LocalDateTime fechaEmision) { this.fechaEmision = fechaEmision; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getUsuarioAdmin() { return usuarioAdmin; }
    public void setUsuarioAdmin(String usuarioAdmin) { this.usuarioAdmin = usuarioAdmin; }

    public String getUrlCertificado() { return urlCertificado; }
    public void setUrlCertificado(String urlCertificado) { this.urlCertificado = urlCertificado; }
}