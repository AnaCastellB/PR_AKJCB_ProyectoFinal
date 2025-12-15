package org.example.dao;

import org.example.database.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SolicitudDAO {

    private Connection getConnection() {
        return DatabaseConnection.getInstance().getConnection();
    }

    public boolean crearSolicitud(int idCliente, int idJuego, String mensaje) {
        String sql = """
                INSERT INTO solicitudes (FR_IDcliente, FR_IDjuego, mensaje)
                VALUES (?, ?, ?)
                """;

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idCliente);
            ps.setInt(2, idJuego);
            ps.setString(3, mensaje);

            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("Error al crear solicitud: " + e.getMessage());
            return false;
        }
    }

    public ResultSet obtenerSolicitudesPendientes() {
        String sql = """
                SELECT * FROM solicitudes
                WHERE estado = 'PENDIENTE'
                ORDER BY fecha_solicitud
                """;

        try {
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            return ps.executeQuery();

        } catch (SQLException e) {
            System.err.println("Error al obtener solicitudes: " + e.getMessage());
            return null;
        }
    }
}
