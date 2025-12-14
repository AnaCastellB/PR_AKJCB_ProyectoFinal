package org.example.app;

import org.example.model.Videojuego;
import java.util.List;

public class VideojuegoJSON {

    public static String toJSON(Videojuego v) {
        return "{"
                + "\"id\":" + v.getId() + ","
                + "\"titulo\":\"" + v.getTitulo() + "\","
                + "\"consola\":\"" + v.getConsola() + "\","
                + "\"precio\":" + v.getPrecio() + ","
                + "\"stock\":" + v.getStock()
                + "}";
    }

    public static String listaToJSON(List<Videojuego> lista) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < lista.size(); i++) {
            sb.append(toJSON(lista.get(i)));
            if (i < lista.size() - 1) sb.append(",");
        }
        sb.append("]");
        return sb.toString();
    }
}
