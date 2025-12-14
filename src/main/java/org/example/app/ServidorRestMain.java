package org.example.app;

import com.sun.net.httpserver.HttpServer;
import org.example.repository.VideojuegoRepository;
import org.example.service.InventarioService;

import java.net.InetSocketAddress;

public class ServidorRestMain {

    public static void main(String[] args) throws Exception {
        int port = 8080;

        VideojuegoRepository repo = new VideojuegoRepository();
        InventarioService inventario = new InventarioService(repo);
        inventario.cargar();

        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/videojuegos", new VideojuegoHttpHandler(inventario));
        server.setExecutor(null);

        System.out.println("Servidor REST (HttpServer) iniciado en http://localhost:" + port + "/videojuegos");
        server.start();
    }
}
