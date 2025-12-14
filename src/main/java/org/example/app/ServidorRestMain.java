package org.example.app;

import org.example.model.Videojuego;
import org.example.repository.VideojuegoRepository;
import org.example.service.InventarioService;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class ServidorRestMain {

    private static final int PUERTO = 8080;

    public static void main(String[] args) {
        System.out.println("Servidor REST iniciado en puerto " + PUERTO);

        VideojuegoRepository repo = new VideojuegoRepository();
        InventarioService inventario = new InventarioService(repo);

        // Datos de prueba
        inventario.registrar(new Videojuego(1, "Zelda", "Switch", 1399, 2));
        inventario.registrar(new Videojuego(2, "Mario Kart", "Switch", 1199, 5));
        inventario.registrar(new Videojuego(3, "Halo Infinite", "Xbox", 1299, 0));

        try (ServerSocket server = new ServerSocket(PUERTO)) {
            while (true) {
                Socket cliente = server.accept();
                new Thread(() -> atenderCliente(cliente, inventario)).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void atenderCliente(Socket cliente, InventarioService inventario) {
        try (
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(cliente.getInputStream(), StandardCharsets.UTF_8)
                );
                PrintWriter out = new PrintWriter(
                        new OutputStreamWriter(cliente.getOutputStream(), StandardCharsets.UTF_8),
                        true
                )
        ) {
            String requestLine = in.readLine();
            if (requestLine == null) return;

            String[] request = requestLine.split(" ");
            String method = request[0];
            String path = request[1];

            // Consumir headers
            while (in.readLine() != null && !in.readLine().isEmpty()) {}

            /* GET /videojuegos */
            if (method.equals("GET") && path.equals("/videojuegos")) {

                List<Videojuego> lista = inventario.listarInventario();
                String json = VideojuegoJSON.listaToJSON(lista);

                out.println("HTTP/1.1 200 OK");
                out.println("Content-Type: application/json");
                out.println();
                out.println(json);

            } else {
                out.println("HTTP/1.1 404 Not Found");
                out.println();
                out.println("{\"error\":\"Ruta no encontrada\"}");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
