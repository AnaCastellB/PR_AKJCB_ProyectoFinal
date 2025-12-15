package org.example.repository;

import org.example.database.DatabaseConnection;
import org.example.model.Videojuego;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VideojuegoRepository {

    private Connection getConnection() {
        return DatabaseConnection.getInstance().getConnection();
    }

    /**
     * Guarda o actualiza un videojuego en la BD
     */
    public void guardar(Videojuego videojuego) {
        String sqlCheck = "SELECT ID_juego FROM videojuegos WHERE ID_juego = ?";
        String sqlUpdate = "UPDATE videojuegos SET nombre = ?, plataforma = ?, stock_tienda = ?, descripcion = ? WHERE ID_juego = ?";
        String sqlInsert = "INSERT INTO videojuegos (ID_juego, nombre, plataforma, stock_tienda, stock_online, descripcion) VALUES (?, ?, ?, ?, 0, ?)";

        try (Connection conn = getConnection()) {
            // Verificar si existe
            try (PreparedStatement psCheck = conn.prepareStatement(sqlCheck)) {
                psCheck.setInt(1, videojuego.getId());
                ResultSet rs = psCheck.executeQuery();

                if (rs.next()) {
                    // ACTUALIZAR
                    try (PreparedStatement psUpdate = conn.prepareStatement(sqlUpdate)) {
                        psUpdate.setString(1, videojuego.getTitulo());
                        psUpdate.setString(2, videojuego.getConsola());
                        psUpdate.setInt(3, videojuego.getStock());
                        psUpdate.setString(4, "Actualizado");
                        psUpdate.setInt(5, videojuego.getId());
                        psUpdate.executeUpdate();
                        System.out.println("✓ Videojuego actualizado: ID " + videojuego.getId());
                    }
                } else {
                    // INSERTAR
                    try (PreparedStatement psInsert = conn.prepareStatement(sqlInsert)) {
                        psInsert.setInt(1, videojuego.getId());
                        psInsert.setString(2, videojuego.getTitulo());
                        psInsert.setString(3, videojuego.getConsola());
                        psInsert.setInt(4, videojuego.getStock());
                        psInsert.setString(5, "Nuevo videojuego");
                        psInsert.executeUpdate();
                        System.out.println("✓ Videojuego insertado: ID " + videojuego.getId());
                    }
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al guardar videojuego: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Obtiene todos los videojuegos de la BD
     */
    public List<Videojuego> obtenerTodos() {
        List<Videojuego> lista = new ArrayList<>();
        String sql = "SELECT ID_juego, nombre, plataforma, stock_tienda FROM videojuegos";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Videojuego v = new Videojuego(
                        rs.getInt("ID_juego"),
                        rs.getString("nombre"),
                        rs.getString("plataforma"),
                        0.0, // Precio no está en tu BD, ponemos 0
                        rs.getInt("stock_tienda")
                );
                lista.add(v);
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener todos: " + e.getMessage());
        }

        return lista;
    }

    /**
     * Busca un videojuego por título
     */
    public Videojuego buscarPorTitulo(String titulo) {
        if (titulo == null) return null;

        String sql = "SELECT ID_juego, nombre, plataforma, stock_tienda FROM videojuegos WHERE nombre LIKE ?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, "%" + titulo.trim() + "%");
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Videojuego(
                        rs.getInt("ID_juego"),
                        rs.getString("nombre"),
                        rs.getString("plataforma"),
                        0.0,
                        rs.getInt("stock_tienda")
                );
            }

        } catch (SQLException e) {
            System.err.println("Error al buscar por título: " + e.getMessage());
        }

        return null;
    }

    /**
     * Busca un videojuego por ID
     */
    public Videojuego buscarPorId(int id) {
        String sql = "SELECT ID_juego, nombre, plataforma, stock_tienda FROM videojuegos WHERE ID_juego = ?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Videojuego(
                        rs.getInt("ID_juego"),
                        rs.getString("nombre"),
                        rs.getString("plataforma"),
                        0.0,
                        rs.getInt("stock_tienda")
                );
            }

        } catch (SQLException e) {
            System.err.println("Error al buscar por ID: " + e.getMessage());
        }

        return null;
    }

    /**
     * Elimina un videojuego por ID
     */
    public boolean eliminarPorId(int id) {
        String sql = "DELETE FROM videojuegos WHERE ID_juego = ?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            int filas = ps.executeUpdate();

            if (filas > 0) {
                System.out.println("Videojuego eliminado: ID " + id);
                return true;
            }

        } catch (SQLException e) {
            System.err.println("Error al eliminar: " + e.getMessage());
        }

        return false;
    }


    /**
     * Actualiza solo el stock de un videojuego
     */
    public boolean actualizarStock(int id, int nuevoStock) {
        String sql = "UPDATE videojuegos SET stock_tienda = ? WHERE ID_juego = ?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, nuevoStock);
            ps.setInt(2, id);
            int filas = ps.executeUpdate();

            if (filas > 0) {
                System.out.println("Stock actualizado: ID " + id + " → " + nuevoStock);
                return true;
            }

        } catch (SQLException e) {
            System.err.println("Error al actualizar stock: " + e.getMessage());
        }

        return false;
    }
}