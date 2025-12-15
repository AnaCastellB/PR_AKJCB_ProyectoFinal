package org.example.dao;

import org.example.database.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MensajeDAO {

    private Connection getConnection() {
        return DatabaseConnection.getInstance().getConnection();
    }

    public boolean insertarMensaje(int idCliente, int idAdmin, String contenido) {
        String sql = """
                INSERT INTO mensajes (FR_IDcliente, FR_IDadmin, contenido)
                VALUES (?, ?, ?)
                """;

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idCliente);
            ps.setInt(2, idAdmin);
            ps.setString(3, contenido);

            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("Error al insertar mensaje: " + e.getMessage());
            return false;
        }
    }

    public ResultSet obtenerMensajesPorCliente(int idCliente) {
        String sql = """
                SELECT * FROM mensajes
                WHERE FR_IDcliente = ?
                ORDER BY fecha_envio
                """;

        try {
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, idCliente);
            return ps.executeQuery();

        } catch (SQLException e) {
            System.err.println("Error al obtener mensajes: " + e.getMessage());
            return null;
        }
    }
}
