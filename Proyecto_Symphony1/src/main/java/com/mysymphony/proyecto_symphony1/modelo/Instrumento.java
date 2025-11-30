

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.mysymphony.proyecto_symphony1.modelo;

/**
 * Instrumento
 * Modelo institucional para representar un instrumento y su cupo disponible.
 */
public class Instrumento {
    private String nombre;
    private int cupo;

    // Constructor vacío
    public Instrumento() {}

    // Constructor con parámetros
    public Instrumento(String nombre, int cupo) {
        this.nombre = nombre;
        this.cupo = cupo;
    }

    // Getters y Setters
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getCupo() {
        return cupo;
    }

    public void setCupo(int cupo) {
        this.cupo = cupo;
    }

    // Método toString para trazabilidad
    @Override
    public String toString() {
        return "Instrumento{" +
                "nombre='" + nombre + '\'' +
                ", cupo=" + cupo +
                '}';
    }
}