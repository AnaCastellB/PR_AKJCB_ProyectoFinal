package org.example.rest;

import com.sun.net.httpserver.HttpServer;
import org.example.repository.VideojuegoRepository;

import java.io.IOException;
import java.net.InetSocketAddress;

public class ServidorRest {

    public static void iniciar(VideojuegoRepository repository) {
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

            server.createContext("/videojuegos", new VideojuegosHandler(repository));

            server.setExecutor(null); // multihilo básico
            server.start();

            System.out.println("Servidor REST iniciado en http://localhost:8080/videojuegos");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
