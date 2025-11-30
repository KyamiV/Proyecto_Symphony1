/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.mysymphony.proyecto_symphony1.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Clase utilitaria para gestionar la conexión JDBC con la base de datos MySQL.
 * Autor: Camila
 */
public class Conexion {

    // 🔹 Configuración de conexión
    private static final String URL =
        "jdbc:mysql://localhost:3307/symphony_db?useSSL=false&serverTimezone=UTC&useUnicode=true&characterEncoding=UTF-8";
    private static final String USUARIO = "root";
    private static final String CLAVE = "";

    /**
     * Obtiene una conexión activa a la base de datos.
     * @return Connection activa o null si falla
     */
    public static Connection getConnection() {
        try {
            // Cargar el driver JDBC
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Crear conexión
            Connection conn = DriverManager.getConnection(URL, USUARIO, CLAVE);
            System.out.println("✅ Conexión establecida con la base de datos.");
            return conn;

        } catch (ClassNotFoundException e) {
            System.err.println("❌ Driver JDBC no encontrado: " + e.getMessage());
            return null;
        } catch (SQLException e) {
            System.err.println("❌ Error al conectar con la base de datos: " + e.getMessage());
            return null;
        }
    }

    /**
     * Cierra una conexión activa de forma segura.
     * @param conn conexión a cerrar
     */
    public static void cerrarConexion(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
                System.out.println("🔒 Conexión cerrada correctamente.");
            } catch (SQLException e) {
                System.err.println("❌ Error al cerrar conexión: " + e.getMessage());
            }
        }
    }

    /**
     * Prueba la conexión con la base de datos.
     * @return true si la conexión es válida, false en caso contrario
     */
    public static boolean probarConexion() {
        try (Connection conn = getConnection()) {
            if (conn != null && !conn.isClosed()) {
                System.out.println("🔗 Conexión probada correctamente.");
                return true;
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al probar conexión: " + e.getMessage());
        }
        return false;
    }
}