package org.example.security;

public class Seguridad {

    private static final String API_KEY = "PR-2025-SEGURA";

    public static boolean validarApiKey(String key) {
        return API_KEY.equals(key);
    }
}
