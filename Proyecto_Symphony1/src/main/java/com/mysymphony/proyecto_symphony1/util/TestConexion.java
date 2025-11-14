/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mysymphony.proyecto_symphony1.util;

import java.sql.Connection;

public class TestConexion {
    public static void main(String[] args) {
        try {
            Connection conn = Conexion.getConexion();
            if (conn != null) {
                System.out.println("✅ Conexión establecida correctamente");
            } else {
                System.out.println("❌ No se pudo conectar a la base de datos");
            }
        } catch (Exception e) {
            System.out.println("❌ Error al intentar conectar: " + e.getMessage());
            e.printStackTrace();
        }
    }
}