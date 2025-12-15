package org.example.repository;

import org.example.database.DatabaseConnection;
import org.example.model.Usuario;
import org.example.security.HashUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioRepository {

    private Connection getConnection() {
        return DatabaseConnection.getInstance().getConnection();
    }

    /**
     * Validar login con hash de contraseña
     */
    public Usuario login(String correo, String password) {
        String sql = "SELECT id_usuario, nombre, correo, password, rol FROM usuarios WHERE correo = ?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, correo);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String hashGuardado = rs.getString("password");

                // Validar contraseña con hash
                if (HashUtil.verificar(password, hashGuardado)) {
                    return new Usuario(
                            rs.getInt("id_usuario"),
                            rs.getString("nombre"),
                            rs.getString("correo"),
                            hashGuardado,
                            rs.getString("rol")
                    );
                }
            }

        } catch (SQLException e) {
            System.err.println("Error en login: " + e.getMessage());
        }

        return null;
    }

    /**
     * Registrar nuevo usuario con hash de contraseña
     */
    public boolean registrar(Usuario usuario) {
        String sql = "INSERT INTO usuarios (nombre, correo, password, rol) VALUES (?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            // Hashear la contraseña antes de guardar
            String hashPassword = HashUtil.hashear(usuario.getPassword());

            ps.setString(1, usuario.getNombre());
            ps.setString(2, usuario.getCorreo());
            ps.setString(3, hashPassword);
            ps.setString(4, usuario.getRol());

            ps.executeUpdate();
            System.out.println("✓ Usuario registrado: " + usuario.getCorreo());
            return true;

        } catch (SQLException e) {
            System.err.println("Error al registrar usuario: " + e.getMessage());
            return false;
        }
    }

    /**
     * Listar todos los usuarios
     */
    public List<Usuario> obtenerTodos() {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT id_usuario, nombre, correo, password, rol FROM usuarios";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(new Usuario(
                        rs.getInt("id_usuario"),
                        rs.getString("nombre"),
                        rs.getString("correo"),
                        rs.getString("password"),
                        rs.getString("rol")
                ));
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener usuarios: " + e.getMessage());
        }

        return lista;
    }

    /**
     * Buscar usuario por correo
     */
    public Usuario buscarPorCorreo(String correo) {
        String sql = "SELECT id_usuario, nombre, correo, password, rol FROM usuarios WHERE correo = ?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, correo);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Usuario(
                        rs.getInt("id_usuario"),
                        rs.getString("nombre"),
                        rs.getString("correo"),
                        rs.getString("password"),
                        rs.getString("rol")
                );
            }

        } catch (SQLException e) {
            System.err.println("Error al buscar usuario: " + e.getMessage());
        }

        return null;
    }
}