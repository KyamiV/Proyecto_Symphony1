/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.mysymphony.proyecto_symphony1.modelo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.sql.Date;
import java.sql.Timestamp;

/**
 * Modelo institucional para representar una clase musical
 * Autor: camiv
 * Trazabilidad: refleja estructura de la tabla 'clases' y permite integración con DAOs, servlets y vistas JSP
 */
public class Clase {

    // Campos principales (tabla clases)
    private int id;                  // PK -> id_clase
    private String nombre;           // nombre_clase
    private String instrumento;      // instrumento
    private String etapa;            // etapa
    private String grupo;            // grupo
    private int cupo;                // cupo máximo
    private int inscritos;           // número de estudiantes inscritos

    // Fechas institucionales
    private LocalDate fechaLimite;   // fecha límite inscripción
    private LocalDate fechaCreacion; // fecha creación clase
    private LocalDateTime fechaInicio; // inicio clase
    private LocalDateTime fechaFin;    // fin clase

    // Estado y docente
    private String estado;           // estado (activa, cerrada, etc.)
    private String docenteNombre;    // nombre del docente asignado
    private int docenteId;           // FK -> id_docente

    // Datos adicionales de horario
    private String horario;
    private String aula;
    private String dia;
    private String inicio;
    private String fin;

    // Constructor vacío
    public Clase() {}

    // Constructor completo
    public Clase(int id, String nombre, String instrumento, String etapa, String grupo, int cupo,
                 int inscritos, LocalDate fechaLimite, LocalDate fechaCreacion,
                 LocalDateTime fechaInicio, LocalDateTime fechaFin,
                 String estado, String docenteNombre, int docenteId,
                 String horario, String aula, String dia, String inicio, String fin) {
        this.id = id;
        this.nombre = nombre;
        this.instrumento = instrumento;
        this.etapa = etapa;
        this.grupo = grupo;
        this.cupo = cupo;
        this.inscritos = inscritos;
        this.fechaLimite = fechaLimite;
        this.fechaCreacion = fechaCreacion;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.estado = estado;
        this.docenteNombre = docenteNombre;
        this.docenteId = docenteId;
        this.horario = horario;
        this.aula = aula;
        this.dia = dia;
        this.inicio = inicio;
        this.fin = fin;
    }

    // ✅ Métodos auxiliares para JSTL (formateo en JSP)
    public Date getFechaLimiteDate() {
        return (fechaLimite != null) ? Date.valueOf(fechaLimite) : null;
    }

    public Date getFechaCreacionDate() {
        return (fechaCreacion != null) ? Date.valueOf(fechaCreacion) : null;
    }

    public Timestamp getFechaInicioDate() {
        return (fechaInicio != null) ? Timestamp.valueOf(fechaInicio) : null;
    }

    public Timestamp getFechaFinDate() {
        return (fechaFin != null) ? Timestamp.valueOf(fechaFin) : null;
    }

    // ✅ Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    // Alias institucional para JSP y DAOs
    public int getIdClase() { return id; }
    public void setIdClase(int idClase) { this.id = idClase; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    // Alias institucional para JSP
    public String getNombreClase() { return nombre; }
    public void setNombreClase(String nombreClase) { this.nombre = nombreClase; }

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

    public LocalDate getFechaLimite() { return fechaLimite; }
    public void setFechaLimite(LocalDate fechaLimite) { this.fechaLimite = fechaLimite; }

    public LocalDate getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDate fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    public LocalDateTime getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDateTime fechaInicio) { this.fechaInicio = fechaInicio; }

    public LocalDateTime getFechaFin() { return fechaFin; }
    public void setFechaFin(LocalDateTime fechaFin) { this.fechaFin = fechaFin; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getDocenteNombre() { return docenteNombre; }
    public void setDocenteNombre(String docenteNombre) { this.docenteNombre = docenteNombre; }

    public int getDocenteId() { return docenteId; }
    public void setDocenteId(int docenteId) { this.docenteId = docenteId; }

    public String getHorario() { return horario; }
    public void setHorario(String horario) { this.horario = horario; }

    public String getAula() { return aula; }
    public void setAula(String aula) { this.aula = aula; }

    public String getDia() { return dia; }
    public void setDia(String dia) { this.dia = dia; }

    public String getInicio() { return inicio; }
    public void setInicio(String inicio) { this.inicio = inicio; }

    public String getFin() { return fin; }
    public void setFin(String fin) { this.fin = fin; }

    @Override
    public String toString() {
        return "Clase{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", instrumento='" + instrumento + '\'' +
                ", etapa='" + etapa + '\'' +
                ", grupo='" + grupo + '\'' +
                ", cupo=" + cupo +
                ", inscritos=" + inscritos +
                ", fechaLimite=" + fechaLimite +
                ", fechaCreacion=" + fechaCreacion +
                ", fechaInicio=" + fechaInicio +
                ", fechaFin=" + fechaFin +
                ", estado='" + estado + '\'' +
                ", docenteNombre='" + docenteNombre + '\'' +
                ", docenteId=" + docenteId +
                ", horario='" + horario + '\'' +
                ", aula='" + aula + '\'' +
                ", dia='" + dia + '\'' +
                ", inicio='" + inicio + '\'' +
                ", fin='" + fin + '\'' +
                '}';
    }
}