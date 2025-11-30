/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.mysymphony.proyecto_symphony1.dto;

import java.time.LocalDate;

/**
 * DTO para representar la relación docente ↔ clase institucional.
 * Autor: Camila
 * 
 * Trazabilidad: incluye datos de clase, docente, instrumento, etapa y métricas institucionales.
 */
public class DocenteConClaseDTO {
    private int docenteId;
    private String docenteNombre;
    private int claseId;
    private String claseNombre;
    private String instrumento;
    private String etapa;

    // Trazabilidad institucional
    private String grupo;
    private int cupo;
    private int inscritos;
    private String horario;      // día_semana + hora_inicio + hora_fin
    private String dia;          // agregado para evitar el error setDia
    private String aula;         // agregado para evitar el error setAula
    private LocalDate fechaLimite;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;

    // Estados diferenciados
    private String estadoClase;        // estado en tabla clases
    private String estadoAsignacion;   // estado en tabla clases_asignadas

    // Constructores
    public DocenteConClaseDTO() {}

    public DocenteConClaseDTO(int docenteId, String docenteNombre, int claseId,
                              String claseNombre, String instrumento, String etapa) {
        this.docenteId = docenteId;
        this.docenteNombre = docenteNombre;
        this.claseId = claseId;
        this.claseNombre = claseNombre;
        this.instrumento = instrumento;
        this.etapa = etapa;
    }

    public DocenteConClaseDTO(int docenteId, String docenteNombre, int claseId,
                              String claseNombre, String instrumento, String etapa,
                              String grupo, int cupo, int inscritos, String horario,
                              LocalDate fechaLimite, LocalDate fechaInicio, LocalDate fechaFin,
                              String estadoClase, String estadoAsignacion, String aula, String dia) {
        this(docenteId, docenteNombre, claseId, claseNombre, instrumento, etapa);
        this.grupo = grupo;
        this.cupo = cupo;
        this.inscritos = inscritos;
        this.horario = horario;
        this.fechaLimite = fechaLimite;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.estadoClase = estadoClase;
        this.estadoAsignacion = estadoAsignacion;
        this.aula = aula;
        this.dia = dia;
    }

    // Getters y setters
    public int getDocenteId() { return docenteId; }
    public void setDocenteId(int docenteId) { this.docenteId = docenteId; }

    public String getDocenteNombre() { return docenteNombre; }
    public void setDocenteNombre(String docenteNombre) { this.docenteNombre = docenteNombre; }

    public int getClaseId() { return claseId; }
    public void setClaseId(int claseId) { this.claseId = claseId; }

    public String getClaseNombre() { return claseNombre; }
    public void setClaseNombre(String claseNombre) { this.claseNombre = claseNombre; }

    // Alias institucionales para JSP
    public int getIdClase() { return claseId; }
    public void setIdClase(int idClase) { this.claseId = idClase; }

    public String getNombreClase() { return claseNombre; }
    public void setNombreClase(String nombreClase) { this.claseNombre = nombreClase; }

    public String getInstrumento() { return instrumento; }
    public void setInstrumento(String instrumento) { this.instrumento = instrumento; }

    public String getEtapa() { return etapa; }
    public void setEtapa(String etapa) { this.etapa = etapa; }

    public String getGrupo() { return grupo; }
    public void setGrupo(String grupo) { this.grupo = grupo; }

    public int getCupo() { return cupo; }
    public void setCupo(int cupo) { this.cupo = cupo; }

    public int getInscritos() { return inscritos; }
    public void setInscritos(int inscritos) { this.inscritos = inscritos; }

    public String getHorario() { return horario; }
    public void setHorario(String horario) { this.horario = horario; }

    public String getDia() { return dia; }
    public void setDia(String dia) { this.dia = dia; }

    public String getAula() { return aula; }
    public void setAula(String aula) { this.aula = aula; }

    public LocalDate getFechaLimite() { return fechaLimite; }
    public void setFechaLimite(LocalDate fechaLimite) { this.fechaLimite = fechaLimite; }

    public LocalDate getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDate fechaInicio) { this.fechaInicio = fechaInicio; }

    public LocalDate getFechaFin() { return fechaFin; }
    public void setFechaFin(LocalDate fechaFin) { this.fechaFin = fechaFin; }

    public String getEstadoClase() { return estadoClase; }
    public void setEstadoClase(String estadoClase) { this.estadoClase = estadoClase; }

    public String getEstadoAsignacion() { return estadoAsignacion; }
    public void setEstadoAsignacion(String estadoAsignacion) { this.estadoAsignacion = estadoAsignacion; }

    // Método utilitario para construir horario
    public void construirHorario(String dia, String inicio, String fin) {
        this.horario = (dia != null ? dia : "-") + " " +
                       (inicio != null ? inicio : "") + " - " +
                       (fin != null ? fin : "");
    }

    @Override
    public String toString() {
        return "DocenteConClaseDTO{" +
                "docenteId=" + docenteId +
                ", docenteNombre='" + docenteNombre + '\'' +
                ", claseId=" + claseId +
                ", claseNombre='" + claseNombre + '\'' +
                ", instrumento='" + instrumento + '\'' +
                ", etapa='" + etapa + '\'' +
                ", grupo='" + grupo + '\'' +
                ", cupo=" + cupo +
                ", inscritos=" + inscritos +
                ", horario='" + horario + '\'' +
                ", dia='" + dia + '\'' +
                ", aula='" + aula + '\'' +
                ", fechaLimite=" + fechaLimite +
                ", fechaInicio=" + fechaInicio +
                ", fechaFin=" + fechaFin +
                ", estadoClase='" + estadoClase + '\'' +
                ", estadoAsignacion='" + estadoAsignacion + '\'' +
                '}';
    }
}