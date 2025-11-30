package com.mysymphony.proyecto_symphony1.modelo;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


import java.sql.Timestamp;

/**
 * Modelo institucional para la tabla auditoria.
 * Autor: camiv
 */
public class Auditoria {

    private int id;
    private String usuario;
    private String rol;
    private String modulo;
    private String accion;
    private String detalle;
    private Timestamp fechaRegistro;

    // 🔹 Getters y Setters
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getUsuario() {
        return usuario;
    }
    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getRol() {
        return rol;
    }
    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getModulo() {
        return modulo;
    }
    public void setModulo(String modulo) {
        this.modulo = modulo;
    }

    public String getAccion() {
        return accion;
    }
    public void setAccion(String accion) {
        this.accion = accion;
    }

    public String getDetalle() {
        return detalle;
    }
    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public Timestamp getFechaRegistro() {
        return fechaRegistro;
    }
    public void setFechaRegistro(Timestamp fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    // 🔹 Para depuración y trazabilidad
    @Override
    public String toString() {
        return "Auditoria{" +
                "id=" + id +
                ", usuario='" + usuario + '\'' +
                ", rol='" + rol + '\'' +
                ", modulo='" + modulo + '\'' +
                ", accion='" + accion + '\'' +
                ", detalle='" + detalle + '\'' +
                ", fechaRegistro=" + fechaRegistro +
                '}';
    }
}