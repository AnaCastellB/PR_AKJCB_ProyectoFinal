package org.example.app;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.example.model.Videojuego;
import org.example.service.InventarioService;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class VideojuegoHttpHandler implements HttpHandler {

    private final InventarioService inventario;

    public VideojuegoHttpHandler(InventarioService inventario) {
        this.inventario = inventario;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            String metodo = exchange.getRequestMethod();

            switch (metodo) {
                case "GET" -> manejarGet(exchange);
                case "POST" -> manejarPost(exchange);
                case "PUT" -> manejarPut(exchange);
                case "DELETE" -> manejarDelete(exchange);
                default -> responderJson(exchange, 405, "{\"error\":\"Metodo no permitido\"}");
            }
        } catch (Exception e) {
            responderJson(exchange, 500, "{\"error\":\"" + escapar(e.getMessage()) + "\"}");
        }
    }

    private void manejarGet(HttpExchange exchange) throws IOException {
        List<Videojuego> lista = inventario.listarInventario();
        String json = VideojuegoJSON.listaToJSON(lista);
        responderJson(exchange, 200, json);
    }

    private void manejarPost(HttpExchange exchange) throws IOException {
        String body = leerBody(exchange);
        Videojuego v = VideojuegoTraductor.jsonAVideojuego(body);

        inventario.registrar(v);
        responderJson(exchange, 201, "{\"mensaje\":\"Videojuego registrado\"}");
    }

    private void manejarPut(HttpExchange exchange) throws IOException {
        String body = leerBody(exchange);
        Videojuego v = VideojuegoTraductor.jsonAVideojuego(body);

        boolean ok = inventario.actualizarStock(v.getId(), v.getStock());
        if (ok) responderJson(exchange, 200, "{\"mensaje\":\"Stock actualizado\"}");
        else responderJson(exchange, 404, "{\"error\":\"Videojuego no encontrado\"}");
    }

    private void manejarDelete(HttpExchange exchange) throws IOException {
        Integer id = obtenerQueryInt(exchange.getRequestURI(), "id");
        if (id == null) {
            responderJson(exchange, 400, "{\"error\":\"Falta parametro ?id=...\"}");
            return;
        }

        boolean ok = inventario.eliminar(id);
        if (ok) responderJson(exchange, 200, "{\"mensaje\":\"Videojuego eliminado\"}");
        else responderJson(exchange, 404, "{\"error\":\"Videojuego no encontrado\"}");
    }

    private String leerBody(HttpExchange exchange) throws IOException {
        InputStream is = exchange.getRequestBody();
        return new String(is.readAllBytes(), StandardCharsets.UTF_8);
    }

    private void responderJson(HttpExchange exchange, int code, String body) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
        byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(code, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }

    private Integer obtenerQueryInt(URI uri, String key) {
        String q = uri.getQuery();
        if (q == null) return null;
        for (String part : q.split("&")) {
            String[] kv = part.split("=", 2);
            if (kv.length == 2 && kv[0].equals(key)) {
                try { return Integer.parseInt(kv[1]); } catch (Exception ignored) {}
            }
        }
        return null;
    }

    private String escapar(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}
