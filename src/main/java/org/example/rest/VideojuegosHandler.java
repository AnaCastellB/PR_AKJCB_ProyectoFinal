package org.example.rest;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.example.model.Videojuego;
import org.example.repository.VideojuegoRepository;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class VideojuegosHandler implements HttpHandler {

    private VideojuegoRepository repository;

    public VideojuegosHandler(VideojuegoRepository repository) {
        this.repository = repository;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        String metodo = exchange.getRequestMethod();

        switch (metodo) {
            case "GET":
                manejarGet(exchange);
                break;
            case "POST":
                manejarPost(exchange);
                break;
            case "PUT":
                manejarPut(exchange);
                break;
            case "DELETE":
                manejarDelete(exchange);
                break;
            default:
                exchange.sendResponseHeaders(405, -1);
        }
    }

    // GET /videojuegos
    private void manejarGet(HttpExchange exchange) throws IOException {
        List<Videojuego> lista = repository.obtenerTodos();

        StringBuilder response = new StringBuilder();
        for (Videojuego v : lista) {
            response.append(v).append("\n");
        }

        enviarRespuesta(exchange, response.toString());
    }

    // POST /videojuegos
    private void manejarPost(HttpExchange exchange) throws IOException {
        repository.guardar(
                new Videojuego(999, "Nuevo Juego", "Consola", 1000, 5)
        );
        enviarRespuesta(exchange, "Videojuego registrado");
    }

    // PUT /videojuegos
    private void manejarPut(HttpExchange exchange) throws IOException {
        Videojuego v = repository.buscarPorTitulo("Nuevo Juego");
        if (v != null) {
            v.setStock(v.getStock() - 1);
            enviarRespuesta(exchange, "Stock actualizado");
        } else {
            enviarRespuesta(exchange, "No encontrado");
        }
    }

    // DELETE /videojuegos
    private void manejarDelete(HttpExchange exchange) throws IOException {
        enviarRespuesta(exchange, "DELETE recibido (simulado)");
    }

    private void enviarRespuesta(HttpExchange exchange, String mensaje) throws IOException {
        byte[] response = mensaje.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(200, response.length);
        OutputStream os = exchange.getResponseBody();
        os.write(response);
        os.close();
    }
}
