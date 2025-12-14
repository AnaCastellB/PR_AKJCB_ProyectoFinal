package org.example.app;

import org.example.model.Videojuego;

public class VideojuegoTraductor {

    public static String serializar(Videojuego v) {
        if (v == null) return "";
        return v.getId() + "|" +
                limpiar(v.getTitulo()) + "|" +
                limpiar(v.getConsola()) + "|" +
                v.getPrecio() + "|" +
                v.getStock();
    }

    public static Videojuego deserializar(String data) {
        if (data == null) throw new IllegalArgumentException("data null");
        String[] p = data.split("\\|", -1);
        if (p.length != 5) throw new IllegalArgumentException("Formato inválido: " + data);

        int id = Integer.parseInt(p[0].trim());
        String titulo = p[1].trim();
        String consola = p[2].trim();
        double precio = Double.parseDouble(p[3].trim());
        int stock = Integer.parseInt(p[4].trim());

        return new Videojuego(id, titulo, consola, precio, stock);
    }

    public static Videojuego desdeLinea(String linea) {
        return deserializar(linea);
    }

    public static String aLinea(Videojuego v) {
        return serializar(v);
    }

    private static String limpiar(String s) {
        if (s == null) return "";
        return s.replace("|", "/").trim();
    }
}
