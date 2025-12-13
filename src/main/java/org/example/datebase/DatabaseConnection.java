package org.example.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Clase Singleton para manejar la conexión a MySQL
 */
public class DatabaseConnection {

    private static final String URL = "jdbc:mysql://localhost:3306/tienda_videojuegos?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "1234";
    private static DatabaseConnection instance;
    private Connection connection;

    private DatabaseConnection() {
        try {
            // Cargar el driver de MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Conexión a MySQL establecida correctamente");
        } catch (ClassNotFoundException e) {
            System.err.println("Error: Driver MySQL no encontrado");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Error al conectar con MySQL: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Obtiene la instancia única de la conexión (Singleton)
     */
    public static DatabaseConnection getInstance() {
        if (instance == null || !isConnectionValid()) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    /**
     * Obtiene la conexión activa
     */
    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener conexión: " + e.getMessage());
        }
        return connection;
    }

    /**
     * Verifica si la conexión está activa
     */
    private static boolean isConnectionValid() {
        try {
            return instance != null &&
                    instance.connection != null &&
                    !instance.connection.isClosed() &&
                    instance.connection.isValid(2);
        } catch (SQLException e) {
            return false;
        }
    }

    /**
     * Cierra la conexión
     */
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("✅ Conexión cerrada correctamente");
            }
        } catch (SQLException e) {
            System.err.println("Error al cerrar conexión: " + e.getMessage());
        }
    }

    /**
     * Prueba la conexión
     */
    public static void main(String[] args) {
        DatabaseConnection db = DatabaseConnection.getInstance();
        Connection conn = db.getConnection();

        if (conn != null) {
            System.out.println("Prueba de conexión exitosa!");
        } else {
            System.out.println("Fallo en la prueba de conexión");
        }

        db.closeConnection();
    }
}