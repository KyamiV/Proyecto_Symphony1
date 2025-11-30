/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.mysymphony.proyecto_symphony1.modelo;

import java.sql.Date;

/**
 * Modelo institucional para docentes.
 * Autor: camiv
 *
 * Hereda de Usuario para integrarse con el flujo institucional.
 */
public class Docente extends Usuario {
    private String apellido;
    private String correo;
    private String telefono;
    private String direccion;
    private Date fechaIngreso;  // 📌 campo institucional (no editable por el docente)
    private String estado;      // 📌 campo institucional (no editable por el docente)
    private String nivelTecnico; // ✅ campo editable por el docente

    // Constructor vacío
    public Docente() {
        super();
    }

    
    // Constructor con parámetros
    public Docente(int id, String nombre, String apellido, String correo,
                   String telefono, String direccion, Date fechaIngreso,
                   String estado, String nivelTecnico) {
        super(id, nombre); // ✅ hereda id y nombre de Usuario
        this.apellido = apellido;
        this.correo = correo;
        this.telefono = telefono;
        this.direccion = direccion;
        this.fechaIngreso = fechaIngreso;
        this.estado = estado;
        this.nivelTecnico = nivelTecnico;
    }

    // Getters y setters
    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public Date getFechaIngreso() { return fechaIngreso; }
    public void setFechaIngreso(Date fechaIngreso) { this.fechaIngreso = fechaIngreso; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getNivelTecnico() { return nivelTecnico; }
    public void setNivelTecnico(String nivelTecnico) { this.nivelTecnico = nivelTecnico; }

    // toString para depuración
    @Override
    public String toString() {
        return "Docente{" +
                "id=" + getId() + // ✅ usa el id heredado de Usuario
                ", nombre='" + getNombre() + '\'' + // ✅ usa el nombre heredado de Usuario
                ", apellido='" + apellido + '\'' +
                ", correo='" + correo + '\'' +
                ", telefono='" + telefono + '\'' +
                ", direccion='" + direccion + '\'' +
                ", fechaIngreso=" + fechaIngreso +
                ", estado='" + estado + '\'' +
                ", nivelTecnico='" + nivelTecnico + '\'' +
                '}';
    }
}