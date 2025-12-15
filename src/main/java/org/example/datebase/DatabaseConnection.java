package org.example.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {

    private static final String URL = "jdbc:mysql://127.0.0.1:3306/tienda_videojuegos?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "1234";
    private static DatabaseConnection instance;
    private Connection connection;

    private DatabaseConnection() {
        try {
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

    public static DatabaseConnection getInstance() {
        if (instance == null || !isConnectionValid()) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

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

    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("✓ Conexión cerrada correctamente");
            }
        } catch (SQLException e) {
            System.err.println("Error al cerrar conexión: " + e.getMessage());
        }
    }

    // Método para inicializar datos de prueba
    public static void inicializarDatosPrueba() {
        try (Connection conn = getInstance().getConnection();
             Statement stmt = conn.createStatement()) {

            // Verificar si ya hay videojuegos
            var rs = stmt.executeQuery("SELECT COUNT(*) FROM videojuegos");
            if (rs.next() && rs.getInt(1) == 0) {
                System.out.println("Insertando datos de prueba...");

                stmt.execute("INSERT INTO videojuegos (nombre, plataforma, stock_tienda, stock_online, descripcion) VALUES " +
                        "('The Legend of Zelda', 'Nintendo Switch', 5, 10, 'Aventura épica'), " +
                        "('Mario Kart 8', 'Nintendo Switch', 8, 15, 'Carreras multijugador'), " +
                        "('God of War', 'PlayStation', 3, 7, 'Acción mitológica'), " +
                        "('Halo Infinite', 'Xbox', 0, 5, 'FPS espacial'), " +
                        "('Cyberpunk 2077', 'PC', 2, 20, 'RPG futurista')");

                System.out.println("✓ Datos de prueba insertados");
            }

            // Verificar si ya hay usuarios
            rs = stmt.executeQuery("SELECT COUNT(*) FROM usuarios");
            if (rs.next() && rs.getInt(1) == 0) {
                // Contraseñas en texto plano por ahora (luego las cifraremos)
                stmt.execute("INSERT INTO usuarios (nombre, correo, password, rol) VALUES " +
                        "('Admin Sistema', 'admin@tienda.com', 'admin123', 'ADMIN'), " +
                        "('Juan Pérez', 'juan@cliente.com', 'juan123', 'CLIENTE'), " +
                        "('María García', 'maria@cliente.com', 'maria123', 'CLIENTE')");

                System.out.println("✓ Usuarios de prueba creados");
            }

        } catch (SQLException e) {
            System.err.println("Error al inicializar datos: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        DatabaseConnection db = DatabaseConnection.getInstance();
        Connection conn = db.getConnection();

        if (conn != null) {
            System.out.println("Prueba de conexión exitosa!");
            inicializarDatosPrueba();
        } else {
            System.out.println("Fallo en la prueba de conexión");
        }

        db.closeConnection();
    }
}