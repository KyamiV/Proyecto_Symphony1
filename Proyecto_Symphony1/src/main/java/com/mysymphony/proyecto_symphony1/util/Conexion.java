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
 * Incluye reintentos automáticos para evitar fallo si la BD tarda en arrancar.
 */
public class Conexion {

    // 🔹 Configuración de conexión mediante variables de entorno
    private static final String HOST = System.getenv("DB_HOST");     // Ej: mysql-symphony
    private static final String PORT = System.getenv("DB_PORT");     // Ej: 3307
    private static final String DB   = System.getenv("DB_NAME");     // Ej: symphony_db
    private static final String USER = System.getenv("DB_USER");     // Ej: root
    private static final String PASS = System.getenv("DB_PASS");     // Ej: Admin1234* o vacío

    // 🔹 Construcción dinámica de la URL JDBC
    private static final String URL =
    "jdbc:mysql://" + HOST + ":" + PORT + "/" + DB +
    "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC&useUnicode=true&characterEncoding=UTF-8";

    /**
     * Obtiene una conexión activa a la base de datos con reintentos.
     * @return Connection activa o null si falla
     */
    public static Connection getConnection() {
        int intentos = 10;          // número máximo de intentos
        int esperaMs = 10000;       // tiempo de espera entre intentos (5 segundos)

        for (int i = 1; i <= intentos; i++) {
            try {
                // Cargar el driver JDBC
                Class.forName("com.mysql.cj.jdbc.Driver");

                // Crear conexión (si PASS es null, se pasa como cadena vacía)
                Connection conn = DriverManager.getConnection(URL, USER, PASS != null ? PASS : "");
                System.out.println("✅ Conexión establecida con la base de datos en intento " + i);
                return conn;

            } catch (ClassNotFoundException e) {
                System.err.println("❌ Driver JDBC no encontrado: " + e.getMessage());
                return null; // sin driver no tiene sentido reintentar
            } catch (SQLException e) {
                System.err.println("❌ Error al conectar (intento " + i + "): " + e.getMessage());
                if (i < intentos) {
                    try {
                        System.out.println("⏳ Reintentando conexión en " + (esperaMs / 1000) + " segundos...");
                        Thread.sleep(esperaMs);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        return null;
                    }
                }
            }
        }
        System.err.println("❌ No se pudo establecer conexión tras " + intentos + " intentos.");
        return null;
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