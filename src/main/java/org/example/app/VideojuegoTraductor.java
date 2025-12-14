package org.example.app;

import org.example.model.Videojuego;
import java.util.List;

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

    public static Videojuego jsonAVideojuego(String json) {
        // Quitar llaves
        json = json.trim();
        json = json.substring(1, json.length() - 1);

        String[] partes = json.split(",");

        int id = 0;
        String titulo = "";
        String consola = "";
        double precio = 0;
        int stock = 0;

        for (String p : partes) {
            String[] kv = p.split(":");
            String key = kv[0].replace("\"", "").trim();
            String value = kv[1].replace("\"", "").trim();

            switch (key) {
                case "id" -> id = Integer.parseInt(value);
                case "titulo" -> titulo = value;
                case "consola" -> consola = value;
                case "precio" -> precio = Double.parseDouble(value);
                case "stock" -> stock = Integer.parseInt(value);
            }
        }

        return new Videojuego(id, titulo, consola, precio, stock);
    }

    public static String listaAJson(List<Videojuego> lista) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");

        for (int i = 0; i < lista.size(); i++) {
            Videojuego v = lista.get(i);
            sb.append("{");
            sb.append("\"id\":").append(v.getId()).append(",");
            sb.append("\"titulo\":\"").append(v.getTitulo()).append("\",");
            sb.append("\"consola\":\"").append(v.getConsola()).append("\",");
            sb.append("\"precio\":").append(v.getPrecio()).append(",");
            sb.append("\"stock\":").append(v.getStock());
            sb.append("}");

            if (i < lista.size() - 1) sb.append(",");
        }

        sb.append("]");
        return sb.toString();
    }


}
