// HashUtil.java
package org.example.security;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashUtil {

    /**
     * Hashea una contraseña con SHA-256
     */
    public static String hashear(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));

            // Convertir a hexadecimal
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error al hashear contraseña", e);
        }
    }

    /**
     * Verifica si una contraseña coincide con un hash
     */
    public static boolean verificar(String password, String hash) {
        String hashPassword = hashear(password);
        return hashPassword.equals(hash);
    }

    // Método para probar
    public static void main(String[] args) {
        String password = "admin123";
        String hash = hashear(password);

        System.out.println("Contraseña: " + password);
        System.out.println("Hash: " + hash);
        System.out.println("Verificación: " + verificar("admin123", hash));
        System.out.println("Verificación falsa: " + verificar("otra", hash));
    }
}