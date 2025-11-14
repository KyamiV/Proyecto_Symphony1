/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mysymphony.proyecto_symphony1.modelo;

public class Inscripcion {
    private String estudiante;
    private String programa;
    private String fecha;

    public Inscripcion() {}

    public Inscripcion(String estudiante, String programa, String fecha) {
        this.estudiante = estudiante;
        this.programa = programa;
        this.fecha = fecha;
    }

    public String getEstudiante() { return estudiante; }
    public void setEstudiante(String estudiante) { this.estudiante = estudiante; }

    public String getPrograma() { return programa; }
    public void setPrograma(String programa) { this.programa = programa; }

    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }
}