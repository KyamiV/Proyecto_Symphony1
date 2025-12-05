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

    // 🔹 Configuración de conexión mediante variables de entorno
    private static final String HOST = System.getenv("DB_HOST");     // Ej: mysql.render.com o host.docker.internal
    private static final String PORT = System.getenv("DB_PORT");     // Ej: 3306 o 3307
    private static final String DB   = System.getenv("DB_NAME");     // Ej: symphony_db
    private static final String USER = System.getenv("DB_USER");     // Ej: root
    private static final String PASS = System.getenv("DB_PASS");     // Ej: clave_segura_123

    // 🔹 Construcción dinámica de la URL JDBC
    private static final String URL =
        "jdbc:mysql://" + HOST + ":" + PORT + "/" + DB +
        "?useSSL=false&serverTimezone=UTC&useUnicode=true&characterEncoding=UTF-8";

    /**
     * Obtiene una conexión activa a la base de datos.
     * @return Connection activa o null si falla
     */
    public static Connection getConnection() {
        try {
            // Cargar el driver JDBC
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Crear conexión
            Connection conn = DriverManager.getConnection(URL, USER, PASS);
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