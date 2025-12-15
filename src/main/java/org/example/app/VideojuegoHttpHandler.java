package org.example.app;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.example.app.VideojuegoTraductor;
import org.example.model.Videojuego;
import org.example.security.Seguridad;
import org.example.service.InventarioService;
import org.example.udp.UdpCliente;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class VideojuegoHttpHandler implements HttpHandler {

    private final InventarioService inventario;

    public VideojuegoHttpHandler(InventarioService inventario) {
        this.inventario = inventario;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        // SEGURIDAD
        String apiKey = exchange.getRequestHeaders().getFirst("X-API-KEY");
        if (apiKey == null || !Seguridad.validarApiKey(apiKey)) {
            responder(exchange, 401, "{\"error\":\"No autorizado\"}");
            return;
        }

        String metodo = exchange.getRequestMethod();

        switch (metodo) {
            case "GET" -> manejarGet(exchange);
            case "POST" -> manejarPost(exchange);
            case "PUT" -> manejarPut(exchange);
            case "DELETE" -> manejarDelete(exchange);
            default -> responder(exchange, 405, "{\"error\":\"Metodo no permitido\"}");
        }
    }

    // GET
    private void manejarGet(HttpExchange exchange) throws IOException {
        List<Videojuego> lista = inventario.listarInventario();

        StringBuilder json = new StringBuilder("[");
        for (int i = 0; i < lista.size(); i++) {
            json.append(VideojuegoTraductor.serializar(lista.get(i)));
            if (i < lista.size() - 1) json.append(",");
        }
        json.append("]");

        responder(exchange, 200, json.toString());
    }

    // POST
    private void manejarPost(HttpExchange exchange) throws IOException {
        String body = leerBody(exchange);
        Videojuego v = VideojuegoTraductor.jsonAVideojuego(body);

        inventario.registrar(v);
        inventario.guardar();

        UdpCliente.enviarMensaje("SECURE|Videojuego agregado: " + v.getTitulo());

        responder(exchange, 201, "{\"mensaje\":\"Videojuego registrado\"}");
    }

    // PUT
    private void manejarPut(HttpExchange exchange) throws IOException {
        String body = leerBody(exchange);
        Videojuego v = VideojuegoTraductor.jsonAVideojuego(body);

        boolean ok = inventario.actualizarStock(v.getId(), v.getStock());

        if (ok) {
            UdpCliente.enviarMensaje(
                    "SECURE|Stock actualizado ID " + v.getId() +
                            " nuevo stock " + v.getStock()
            );
            responder(exchange, 200, "{\"mensaje\":\"Stock actualizado\"}");
        } else {
            responder(exchange, 404, "{\"error\":\"Videojuego no encontrado\"}");
        }
    }

    // DELETE
    private void manejarDelete(HttpExchange exchange) throws IOException {
        String body = leerBody(exchange);
        int id = Integer.parseInt(body.trim());

        boolean ok = inventario.eliminar(id);

        if (ok) {
            UdpCliente.enviarMensaje("SECURE|Videojuego eliminado ID " + id);
            responder(exchange, 200, "{\"mensaje\":\"Videojuego eliminado\"}");
        } else {
            responder(exchange, 404, "{\"error\":\"No encontrado\"}");
        }
    }

    private String leerBody(HttpExchange exchange) throws IOException {
        InputStream is = exchange.getRequestBody();
        return new String(is.readAllBytes(), StandardCharsets.UTF_8);
    }

    private void responder(HttpExchange exchange, int code, String body) throws IOException {
        exchange.getResponseHeaders().add(
                "Content-Type", "application/json; charset=UTF-8"
        );
        byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(code, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }
}
