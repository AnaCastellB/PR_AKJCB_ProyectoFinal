package org.example.app;

import com.sun.net.httpserver.HttpServer;
import org.example.repository.VideojuegoRepository;
import org.example.repository.UsuarioRepository;
import org.example.service.InventarioService;
import org.example.app.VideojuegoHttpHandler;
import org.example.rest.LoginHttpHandler;

import java.net.InetSocketAddress;

public class ServidorRestMain {

    public static void main(String[] args) throws Exception {

        int port = 8080;

        // REPOSITORIOS
        VideojuegoRepository videojuegoRepository = new VideojuegoRepository();
        UsuarioRepository usuarioRepository = new UsuarioRepository();

        // SERVICIOS
        InventarioService inventarioService =
                new InventarioService(videojuegoRepository);

        inventarioService.cargar();

        // SERVIDOR HTTP
        HttpServer server =
                HttpServer.create(new InetSocketAddress(port), 0);

        // CONTEXTOS
        server.createContext(
                "/videojuegos",
                new VideojuegoHttpHandler(inventarioService)
        );

        server.createContext(
                "/login",
                new LoginHttpHandler(usuarioRepository)
        );

        server.setExecutor(null);
        server.start();

        System.out.println("Servidor REST activo en http://localhost:" + port);
    }
}
