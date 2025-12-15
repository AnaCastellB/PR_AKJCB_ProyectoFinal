package org.example.rest;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.example.model.Usuario;
import org.example.repository.UsuarioRepository;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class LoginHttpHandler implements HttpHandler {

    private final UsuarioRepository usuarioRepository;

    public LoginHttpHandler(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        if (!exchange.getRequestMethod().equalsIgnoreCase("POST")) {
            responder(exchange, 405, "{\"error\":\"Método no permitido\"}");
            return;
        }

        String body = leerBody(exchange);

        String correo = extraer(body, "correo");
        String password = extraer(body, "password");

        if (correo == null || password == null) {
            responder(exchange, 400, "{\"error\":\"Datos incompletos\"}");
            return;
        }

        Usuario usuario = usuarioRepository.login(correo, password);

        if (usuario != null) {
            responder(exchange, 200,
                    "{\"mensaje\":\"Login correcto\",\"rol\":\"" + usuario.getRol() + "\"}"
            );
        } else {
            responder(exchange, 401, "{\"error\":\"Credenciales incorrectas\"}");
        }
    }

    private String leerBody(HttpExchange exchange) throws IOException {
        InputStream is = exchange.getRequestBody();
        return new String(is.readAllBytes(), StandardCharsets.UTF_8);
    }

    private void responder(HttpExchange exchange, int code, String body)
            throws IOException {

        exchange.getResponseHeaders().add(
                "Content-Type",
                "application/json; charset=UTF-8"
        );

        byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(code, bytes.length);

        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }

    private String extraer(String json, String campo) {
        String buscado = "\"" + campo + "\"";
        int i = json.indexOf(buscado);
        if (i == -1) return null;
        int inicio = json.indexOf(":", i) + 1;
        int fin = json.indexOf(",", inicio);
        if (fin == -1) fin = json.indexOf("}", inicio);
        return json.substring(inicio, fin)
                .replace("\"", "")
                .trim();
    }
}
