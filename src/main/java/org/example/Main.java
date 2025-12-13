package org.example;

import org.example.model.Videojuego;
import org.example.repository.VideojuegoRepository;
import org.example.service.InventarioService;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        VideojuegoRepository repo = new VideojuegoRepository();
        InventarioService inventario = new InventarioService(repo);

        // Datos de arranque (puedes quitarlos si quieres)
        inventario.registrar(new Videojuego(1, "Zelda", "Switch", 1399, 3));
        inventario.registrar(new Videojuego(2, "Mario Kart", "Switch", 1199, 0));

        Scanner sc = new Scanner(System.in);
        int opcion;

        do {
            System.out.println("\n=== TIENDA DE VIDEOJUEGOS ===");
            System.out.println("1) Registrar videojuego");
            System.out.println("2) Mostrar inventario");
            System.out.println("3) Buscar videojuego por titulo");
            System.out.println("4) Vender videojuego por titulo");
            System.out.println("0) Salir");
            System.out.print("Opcion: ");

            while (!sc.hasNextInt()) {
                sc.nextLine();
                System.out.print("Opcion: ");
            }
            opcion = sc.nextInt();
            sc.nextLine(); // limpiar salto de linea

            switch (opcion) {
                case 1 -> {
                    System.out.print("ID: ");
                    int id = sc.nextInt();
                    sc.nextLine();

                    System.out.print("Titulo: ");
                    String titulo = sc.nextLine();

                    System.out.print("Consola: ");
                    String consola = sc.nextLine();

                    System.out.print("Precio: ");
                    double precio = sc.nextDouble();
                    sc.nextLine();

                    System.out.print("Stock: ");
                    int stock = sc.nextInt();
                    sc.nextLine();

                    inventario.registrar(new Videojuego(id, titulo, consola, precio, stock));
                    System.out.println("Registrado.");
                }
                case 2 -> inventario.mostrarInventario();
                case 3 -> {
                    System.out.print("Titulo a buscar: ");
                    String titulo = sc.nextLine();
                    inventario.buscarJuego(titulo);
                }
                case 4 -> {
                    System.out.print("Titulo a vender: ");
                    String titulo = sc.nextLine();
                    boolean ok = inventario.venderVideojuego(titulo);
                    System.out.println(ok ? "Venta realizada." : "No disponible o no existe.");
                }
                case 0 -> System.out.println("Saliendo...");
                default -> System.out.println("Opcion invalida.");
            }

        } while (opcion != 0);

        sc.close();
    }
}
