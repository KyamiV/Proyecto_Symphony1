/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.mysymphony.proyecto_symphony1.modelo;

import java.time.LocalDate;
import java.sql.Date;

/**
 * Modelo institucional para representar una inscripción.
 * Autor: camiv
 * Trazabilidad: refleja estructura de la tabla 'inscripciones_clase'
 * y permite integración con DAOs, servlets y vistas JSP.
 */
public class Inscripcion {

    // Campos principales (tabla inscripciones_clase)
    private int id;                   // PK -> id_inscripcion
    private int claseId;              // FK -> id_clase
    private int idEstudiante;         // FK -> id_estudiante
    private LocalDate fechaInscripcion; // fecha_inscripcion

    // Campos adicionales para mostrar nombres y datos en JSP (JOIN con usuarios y clases)
    private String nombreEstudiante;   // nombre del estudiante
    private String correoEstudiante;   // correo del estudiante
    private String nombreClase;        // nombre de la clase
    private String observacion;        // opcional: comentarios de inscripción

    // Getters y Setters
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public int getClaseId() {
        return claseId;
    }
    public void setClaseId(int claseId) {
        this.claseId = claseId;
    }

    public int getIdEstudiante() {
        return idEstudiante;
    }
    public void setIdEstudiante(int idEstudiante) {
        this.idEstudiante = idEstudiante;
    }

    public LocalDate getFechaInscripcion() {
        return fechaInscripcion;
    }
    public void setFechaInscripcion(LocalDate fechaInscripcion) {
        this.fechaInscripcion = fechaInscripcion;
    }

    public String getNombreEstudiante() {
        return nombreEstudiante;
    }
    public void setNombreEstudiante(String nombreEstudiante) {
        this.nombreEstudiante = nombreEstudiante;
    }

    public String getCorreoEstudiante() {
        return correoEstudiante;
    }
    public void setCorreoEstudiante(String correoEstudiante) {
        this.correoEstudiante = correoEstudiante;
    }

    public String getNombreClase() {
        return nombreClase;
    }
    public void setNombreClase(String nombreClase) {
        this.nombreClase = nombreClase;
    }

    public String getObservacion() {
        return observacion;
    }
    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    // ✅ Helper para JSTL (formateo en JSP con <fmt:formatDate>)
    public Date getFechaDate() {
        return (fechaInscripcion != null) ? Date.valueOf(fechaInscripcion) : null;
    }

    @Override
    public String toString() {
        return "Inscripcion{" +
                "id=" + id +
                ", claseId=" + claseId +
                ", idEstudiante=" + idEstudiante +
                ", fechaInscripcion=" + fechaInscripcion +
                ", nombreEstudiante='" + nombreEstudiante + '\'' +
                ", correoEstudiante='" + correoEstudiante + '\'' +
                ", nombreClase='" + nombreClase + '\'' +
                ", observacion='" + observacion + '\'' +
                '}';
    }
}