/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mysymphony.proyecto_symphony1.modelo;

import java.time.LocalDateTime;

/**
 * Modelo institucional para representar las tablas validadas
 * en el módulo de certificación.
 * 
 * @author camiv
 */
public class TablaValidada {
    private int idValidacion;
    private String modulo;
    private String estado;
    private String usuario;
    private LocalDateTime fechaValidacion;

    // ✅ Getters y Setters
    public int getIdValidacion() {
        return idValidacion;
    }

    public void setIdValidacion(int idValidacion) {
        this.idValidacion = idValidacion;
    }

    public String getModulo() {
        return modulo;
    }

    public void setModulo(String modulo) {
        this.modulo = modulo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public LocalDateTime getFechaValidacion() {
        return fechaValidacion;
    }

    public void setFechaValidacion(LocalDateTime fechaValidacion) {
        this.fechaValidacion = fechaValidacion;
    }

    // ✅ Método toString para depuración
    @Override
    public String toString() {
        return "TablaValidada{" +
                "idValidacion=" + idValidacion +
                ", modulo='" + modulo + '\'' +
                ", estado='" + estado + '\'' +
                ", usuario='" + usuario + '\'' +
                ", fechaValidacion=" + fechaValidacion +
                '}';
    }
}