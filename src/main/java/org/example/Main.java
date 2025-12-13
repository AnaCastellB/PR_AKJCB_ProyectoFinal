package org.example;

import org.example.model.Videojuego;
import org.example.repository.VideojuegoRepository;
import org.example.service.InventarioService;

public class Main {
    public static void main(String[] args) {

        VideojuegoRepository repo = new VideojuegoRepository();
        InventarioService inventario = new InventarioService(repo);

        inventario.agregarVideojuego(
                new Videojuego(1, "Zelda Tears of the Kingdom", "Nintendo Switch", 1399, 3)
        );

        inventario.agregarVideojuego(
                new Videojuego(2, "Mario Kart 8 Deluxe", "Nintendo Switch", 1199, 0)
        );

        for (Videojuego v : inventario.listarVideojuegos()) {
            System.out.println(v);
        }

        System.out.println("\nIntentando vender Mario Kart...");
        boolean vendido = inventario.venderVideojuego("Mario Kart 8 Deluxe");

        if (vendido) {
            System.out.println("Venta realizada");
        } else {
            System.out.println("No disponible, contactar a tienda");
        }
    }
}
