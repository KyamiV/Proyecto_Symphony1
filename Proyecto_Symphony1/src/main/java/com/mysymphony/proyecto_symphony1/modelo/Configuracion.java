/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.Configuracion to edit this template
 */
package com.mysymphony.proyecto_symphony1.modelo;

/**
 * Configuracion
 * Modelo institucional para representar parámetros técnicos del sistema.
 * Cada objeto corresponde a una fila en la tabla configuracion_sistema.
 */
public class Configuracion {
    private String clave;
    private String valor;

    // Constructor vacío
    public Configuracion() {}

    // Constructor con parámetros
    public Configuracion(String clave, String valor) {
        this.clave = clave;
        this.valor = valor;
    }

    // Getters y Setters
    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    // Método toString para trazabilidad
    @Override
    public String toString() {
        return "Configuracion{" +
                "clave='" + clave + '\'' +
                ", valor='" + valor + '\'' +
                '}';
    }
}