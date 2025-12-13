package org.example;

import org.example.model.Videojuego;
import org.example.service.InventarioService;

public class Main {
    public static void main(String[] args) {

        InventarioService inventario = new InventarioService();

        inventario.agregarVideojuego(
                new Videojuego(1, "Zelda Tears of the Kingdom", "Nintendo Switch", 1399, 3)
        );

        inventario.agregarVideojuego(
                new Videojuego(2, "Mario Kart 8 Deluxe", "Nintendo Switch", 1199, 0)
        );

        inventario.mostrarInventario();

        System.out.println("\nBuscando juego...");
        var juego = inventario.buscarPorTitulo("Mario Kart 8 Deluxe");

        if (juego != null && juego.getStock() > 0) {
            System.out.println("Disponible en tienda");
        } else {
            System.out.println("No disponible, contactar a tienda");
        }
    }
}
