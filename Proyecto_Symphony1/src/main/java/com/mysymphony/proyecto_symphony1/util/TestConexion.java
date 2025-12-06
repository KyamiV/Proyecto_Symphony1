/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.mysymphony.proyecto_symphony1.util;

import java.sql.Connection;

public class TestConexion {
    public static void main(String[] args) {
        System.out.println("🔍 Iniciando prueba de conexión con la base de datos...");

        try {
            // Intentar obtener la conexión desde tu clase Conexion
            Connection conn = Conexion.getConnection();

            if (conn != null) {
                System.out.println("✅ Conexión establecida correctamente con la base de datos.");
                // Cerrar la conexión como buena práctica
                conn.close();
                System.out.println("🔒 Conexión cerrada correctamente.");
            } else {
                System.out.println("❌ No se pudo conectar a la base de datos (conn es null).");
            }

        } catch (Exception e) {
            System.out.println("❌ Error al intentar conectar: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("🏁 Prueba de conexión finalizada.");
    }
}