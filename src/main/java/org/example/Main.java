package org.example;

import org.example.model.Videojuego;
import org.example.repository.VideojuegoRepository;
import org.example.service.InventarioService;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        VideojuegoRepository repo = new VideojuegoRepository();
        InventarioService inventario = new InventarioService(repo);

        int opcion;

        do {
            System.out.println("\n=== TIENDA DE VIDEOJUEGOS ===");
            System.out.println("1) Agregar videojuego");
            System.out.println("2) Ver inventario");
            System.out.println("3) Buscar videojuego por título");
            System.out.println("4) Vender videojuego (por título)");
            System.out.println("5) Salir");
            System.out.print("Opción: ");

            while (!sc.hasNextInt()) {
                System.out.print("Ingresa un número válido: ");
                sc.next(); // limpia entrada
            }
            opcion = sc.nextInt();
            sc.nextLine(); // consume salto de línea

            switch (opcion) {
                case 1 -> {
                    System.out.print("ID: ");
                    int id = leerEntero(sc);

                    System.out.print("Título: ");
                    String titulo = sc.nextLine();

                    System.out.print("Consola: ");
                    String consola = sc.nextLine();

                    System.out.print("Precio: ");
                    double precio = leerDouble(sc);

                    System.out.print("Stock: ");
                    int stock = leerEntero(sc);

                    inventario.registrar(new Videojuego(id, titulo, consola, precio, stock));
                    System.out.println("✅ Videojuego agregado.");
                }
                case 2 -> inventario.mostrarInventario();

                case 3 -> {
                    System.out.print("Título a buscar: ");
                    String titulo = sc.nextLine();
                    inventario.buscarJuego(titulo);
                }

                case 4 -> {
                    System.out.print("Título a vender: ");
                    String titulo = sc.nextLine();
                    boolean vendido = inventario.venderVideojuego(titulo);

                    if (vendido) System.out.println("✅ Venta realizada.");
                    else System.out.println("❌ No se pudo vender (no existe o no hay stock).");
                }

                case 5 -> System.out.println("Saliendo...");

                default -> System.out.println("Opción inválida.");
            }

        } while (opcion != 5);

        sc.close();
    }

    private static int leerEntero(Scanner sc) {
        while (!sc.hasNextInt()) {
            System.out.print("Ingresa un entero válido: ");
            sc.next();
        }
        int v = sc.nextInt();
        sc.nextLine();
        return v;
    }

    private static double leerDouble(Scanner sc) {
        while (!sc.hasNextDouble()) {
            System.out.print("Ingresa un número válido: ");
            sc.next();
        }
        double v = sc.nextDouble();
        sc.nextLine();
        return v;
    }
}
