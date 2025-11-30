/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.mysymphony.proyecto_symphony1.modelo;

import java.time.LocalDate;

public class Asignacion {
    private int id;                 // PK de la asignación
    private int idDocente;          // FK al docente
    private int idEstudiante;       // FK al estudiante (puede ser null si es institucional)
    private int idClase;            // FK a la clase
    private String instrumento;     // instrumento asignado
    private String etapa;           // etapa pedagógica
    private LocalDate fechaAsignacion; // fecha de la asignación

    // ✅ Getters y setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getIdDocente() { return idDocente; }
    public void setIdDocente(int idDocente) { this.idDocente = idDocente; }

    public int getIdEstudiante() { return idEstudiante; }
    public void setIdEstudiante(int idEstudiante) { this.idEstudiante = idEstudiante; }

    public int getIdClase() { return idClase; }
    public void setIdClase(int idClase) { this.idClase = idClase; }

    public String getInstrumento() { return instrumento; }
    public void setInstrumento(String instrumento) { this.instrumento = instrumento; }

    public String getEtapa() { return etapa; }
    public void setEtapa(String etapa) { this.etapa = etapa; }

    public LocalDate getFechaAsignacion() { return fechaAsignacion; }
    public void setFechaAsignacion(LocalDate fechaAsignacion) { this.fechaAsignacion = fechaAsignacion; }
}