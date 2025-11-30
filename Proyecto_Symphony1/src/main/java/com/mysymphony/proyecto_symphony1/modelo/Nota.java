/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.mysymphony.proyecto_symphony1.modelo;

import java.time.LocalDate;
import java.sql.Timestamp;

/**
 * Modelo que representa una nota registrada por clase.
 * Autor: Camila
 * Trazabilidad: refleja estructura de la tabla 'notas_clase'
 */
public class Nota {

    // 🔹 Campos principales (tabla notas_clase)
    private int id;                 // id_nota en la BD
    private int idEstudiante;       // id_estudiante en la BD
    private int idDocente;          // id_docente en la BD
    private int idClase;            // id_clase en la BD
    private int idTabla;            // id_tabla en la BD (tabla institucional)

    private String estudiante;      // nombre completo del estudiante (JOIN usuarios)
    private String competencia;     // competencia evaluada (ej: Técnica, Ritmo)
    private String instrumento;     // instrumento asociado
    private String etapa;           // etapa pedagógica

    private double nota;            // valor numérico de la nota
    private String observacion;     // comentario del docente
    private String estado;          // estado de la nota (ej: registrada, enviada)
    private String registradaPor;   // docente que registró la nota

    private LocalDate fecha;        // fecha de la nota (YYYY-MM-DD)
    private Timestamp fechaRegistro;// fecha y hora de registro en BD

    // 🔸 Constructor vacío
    public Nota() {}

    // 🔸 Constructor completo
    public Nota(int id, int idEstudiante, int idDocente, int idClase, int idTabla,
                String estudiante, String competencia, String instrumento, String etapa,
                double nota, String observacion, String estado, String registradaPor,
                LocalDate fecha, Timestamp fechaRegistro) {
        this.id = id;
        this.idEstudiante = idEstudiante;
        this.idDocente = idDocente;
        this.idClase = idClase;
        this.idTabla = idTabla;
        this.estudiante = estudiante;
        this.competencia = competencia;
        this.instrumento = instrumento;
        this.etapa = etapa;
        this.nota = nota;
        this.observacion = observacion;
        this.estado = estado;
        this.registradaPor = registradaPor;
        this.fecha = fecha;
        this.fechaRegistro = fechaRegistro;
    }

    // 🔸 Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getIdEstudiante() { return idEstudiante; }
    public void setIdEstudiante(int idEstudiante) { this.idEstudiante = idEstudiante; }

    public int getIdDocente() { return idDocente; }
    public void setIdDocente(int idDocente) { this.idDocente = idDocente; }

    public int getIdClase() { return idClase; }
    public void setIdClase(int idClase) { this.idClase = idClase; }

    public int getIdTabla() { return idTabla; }
    public void setIdTabla(int idTabla) { this.idTabla = idTabla; }

    public String getEstudiante() { return estudiante; }
    public void setEstudiante(String estudiante) { this.estudiante = estudiante; }

    public String getCompetencia() { return competencia; }
    public void setCompetencia(String competencia) { this.competencia = competencia; }

    public String getInstrumento() { return instrumento; }
    public void setInstrumento(String instrumento) { this.instrumento = instrumento; }

    public String getEtapa() { return etapa; }
    public void setEtapa(String etapa) { this.etapa = etapa; }

    public double getNota() { return nota; }
    public void setNota(double nota) { this.nota = nota; }

    public String getObservacion() { return observacion; }
    public void setObservacion(String observacion) { this.observacion = observacion; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getRegistradaPor() { return registradaPor; }
    public void setRegistradaPor(String registradaPor) { this.registradaPor = registradaPor; }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    public Timestamp getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(Timestamp fechaRegistro) { this.fechaRegistro = fechaRegistro; }

    // 🔍 toString para depuración
    @Override
    public String toString() {
        return "Nota{" +
                "id=" + id +
                ", idEstudiante=" + idEstudiante +
                ", idDocente=" + idDocente +
                ", idClase=" + idClase +
                ", idTabla=" + idTabla +
                ", estudiante='" + estudiante + '\'' +
                ", competencia='" + competencia + '\'' +
                ", instrumento='" + instrumento + '\'' +
                ", etapa='" + etapa + '\'' +
                ", nota=" + nota +
                ", observacion='" + observacion + '\'' +
                ", estado='" + estado + '\'' +
                ", registradaPor='" + registradaPor + '\'' +
                ", fecha=" + fecha +
                ", fechaRegistro=" + fechaRegistro +
                '}';
    }
}