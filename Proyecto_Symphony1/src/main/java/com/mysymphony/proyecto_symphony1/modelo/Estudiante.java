/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.mysymphony.proyecto_symphony1.modelo;

import java.time.LocalDate;
import java.sql.Date;

/**
 * Modelo que representa a un estudiante con sus datos institucionales y pedagógicos.
 * Autor: camiv
 */
public class Estudiante {

    // 🔹 Campos institucionales (BD)
    private int idEstudiante;   // corresponde a id_estudiante en la BD
    private int idUsuario;      // corresponde a id_usuario en la BD (FK hacia usuarios)
    private String nombre;      // nombre en la BD
    private String apellido;    // apellido en la BD (usuarios)
    private String correo;      // correo en la BD
    private String instrumento; // instrumento en la BD
    private String direccion;   // dirección en la BD
    private String telefono;    // teléfono en la BD
    private String estado;      // estado en la BD (activo/inactivo)
    private String colorHex;    // color institucional (columna color_hex en la BD)

    // 🔹 Campos pedagógicos (seguimiento académico)
    private String nivelTecnico;
    private String etapaPedagogica;
    private String progreso;
    private String observaciones;
    private LocalDate fechaActualizacion;

    // 🔹 Campos de trazabilidad
    private LocalDate fechaInscripcion; // fecha en que se inscribió en una clase
    private LocalDate fechaIngreso;     // fecha_ingreso en la BD

    // 🔸 Constructor vacío
    public Estudiante() {}

    // 🔸 Constructor completo
    public Estudiante(int idEstudiante, int idUsuario, String nombre, String apellido, String correo, String instrumento,
                      String direccion, String telefono, String estado,
                      String nivelTecnico, String etapaPedagogica,
                      String progreso, String observaciones,
                      LocalDate fechaActualizacion, String colorHex,
                      LocalDate fechaInscripcion, LocalDate fechaIngreso) {
        this.idEstudiante = idEstudiante;
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.apellido = apellido;
        this.correo = correo;
        this.instrumento = instrumento;
        this.direccion = direccion;
        this.telefono = telefono;
        this.estado = estado;
        this.nivelTecnico = nivelTecnico;
        this.etapaPedagogica = etapaPedagogica;
        this.progreso = progreso;
        this.observaciones = observaciones;
        this.fechaActualizacion = fechaActualizacion;
        this.colorHex = colorHex;
        this.fechaInscripcion = fechaInscripcion;
        this.fechaIngreso = fechaIngreso;
    }

    // 🔸 Getters y Setters
    public int getIdEstudiante() { return idEstudiante; }
    public void setIdEstudiante(int idEstudiante) { this.idEstudiante = idEstudiante; }

    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }

    // 👉 Alias para compatibilidad con DAOs que usan setId/getId
    public int getId() { return idEstudiante; }
    public void setId(int idEstudiante) { this.idEstudiante = idEstudiante; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public String getInstrumento() { return instrumento; }
    public void setInstrumento(String instrumento) { this.instrumento = instrumento; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getNivelTecnico() { return nivelTecnico; }
    public void setNivelTecnico(String nivelTecnico) { this.nivelTecnico = nivelTecnico; }

    public String getEtapaPedagogica() { return etapaPedagogica; }
    public void setEtapaPedagogica(String etapaPedagogica) { this.etapaPedagogica = etapaPedagogica; }

    public String getProgreso() { return progreso; }
    public void setProgreso(String progreso) { this.progreso = progreso; }

    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }

    public LocalDate getFechaActualizacion() { return fechaActualizacion; }
    public void setFechaActualizacion(LocalDate fechaActualizacion) { this.fechaActualizacion = fechaActualizacion; }

    public String getColorHex() { return colorHex; }
    public void setColorHex(String colorHex) { this.colorHex = colorHex; }

    public LocalDate getFechaInscripcion() { return fechaInscripcion; }
    public void setFechaInscripcion(LocalDate fechaInscripcion) { this.fechaInscripcion = fechaInscripcion; }

    public LocalDate getFechaIngreso() { return fechaIngreso; }
    public void setFechaIngreso(LocalDate fechaIngreso) { this.fechaIngreso = fechaIngreso; }

    // ✅ Helpers para JSP (formateo con <fmt:formatDate>)
    public Date getFechaInscripcionDate() {
        return (fechaInscripcion != null) ? Date.valueOf(fechaInscripcion) : null;
    }

    public Date getFechaIngresoDate() {
        return (fechaIngreso != null) ? Date.valueOf(fechaIngreso) : null;
    }

    // 🔍 toString para depuración
    @Override
    public String toString() {
        return "Estudiante{" +
                "idEstudiante=" + idEstudiante +
                ", idUsuario=" + idUsuario +
                ", nombre='" + nombre + '\'' +
                ", apellido='" + apellido + '\'' +
                ", correo='" + correo + '\'' +
                ", instrumento='" + instrumento + '\'' +
                ", direccion='" + direccion + '\'' +
                ", telefono='" + telefono + '\'' +
                ", estado='" + estado + '\'' +
                ", nivelTecnico='" + nivelTecnico + '\'' +
                ", etapaPedagogica='" + etapaPedagogica + '\'' +
                ", progreso='" + progreso + '\'' +
                ", observaciones='" + observaciones + '\'' +
                ", fechaActualizacion=" + fechaActualizacion +
                ", colorHex='" + colorHex + '\'' +
                ", fechaInscripcion=" + fechaInscripcion +
                ", fechaIngreso=" + fechaIngreso +
                '}';
    }
}