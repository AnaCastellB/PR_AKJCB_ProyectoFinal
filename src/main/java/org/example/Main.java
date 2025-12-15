package org.example;

import org.example.model.Videojuego;
import org.example.repository.VideojuegoRepository;
import org.example.service.InventarioService;
import org.example.app.ServidorRestMain;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        VideojuegoRepository repo = new VideojuegoRepository();
        InventarioService inventario = new InventarioService(repo);

        inventario.cargar();

        Scanner sc = new Scanner(System.in);
        int opcion;

        do {
            System.out.println("\n TIENDA DE VIDEOJUEGOS ");
            System.out.println("1) Registrar videojuego");
            System.out.println("2) Mostrar inventario");
            System.out.println("3) Buscar por titulo");
            System.out.println("4) Vender por titulo");
            System.out.println("5) Mostrar disponibles");
            System.out.println("6) Mostrar agotados");
            System.out.println("7) Buscar por ID");
            System.out.println("8) Guardar ahora");
            System.out.println("9) Cargar ahora");
            System.out.println("0) Salir");
            System.out.print("Opcion: ");

            opcion = leerEntero(sc);

            switch (opcion) {
                case 1 -> {
                    System.out.print("ID: ");
                    int id = leerEntero(sc);

                    System.out.print("Titulo: ");
                    String titulo = sc.nextLine().trim();

                    System.out.print("Consola: ");
                    String consola = sc.nextLine().trim();

                    System.out.print("Precio: ");
                    double precio = leerDouble(sc);

                    System.out.print("Stock: ");
                    int stock = leerEntero(sc);

                    inventario.registrar(new Videojuego(id, titulo, consola, precio, stock));
                    System.out.println("Registrado.");
                }
                case 2 -> imprimirLista("Inventario:", inventario.listarInventario());
                case 3 -> {
                    System.out.print("Titulo a buscar: ");
                    String titulo = sc.nextLine().trim();

                    Videojuego v = inventario.buscarPorTitulo(titulo);
                    if (v == null) System.out.println("No encontrado.");
                    else if (v.getStock() > 0) System.out.println("Disponible: " + v);
                    else System.out.println("No disponible: " + v);
                }
                case 4 -> {
                    System.out.print("Titulo a vender: ");
                    String titulo = sc.nextLine().trim();

                    boolean ok = inventario.venderPorTitulo(titulo);
                    System.out.println(ok ? "Venta realizada." : "No se pudo vender (no existe o sin stock).");
                }
                case 5 -> imprimirLista("Disponibles:", inventario.listarDisponibles());
                case 6 -> imprimirLista("Agotados:", inventario.listarAgotados());
                case 7 -> {
                    System.out.print("ID a buscar: ");
                    int id = leerEntero(sc);

                    Videojuego v = inventario.buscarPorId(id);
                    if (v == null) System.out.println("No encontrado.");
                    else if (v.getStock() > 0) System.out.println("Disponible: " + v);
                    else System.out.println("No disponible: " + v);
                }
                case 8 -> {
                    inventario.guardar();
                    System.out.println("Guardado en archivo.");
                }
                case 9 -> {
                    inventario.cargar();
                    System.out.println("Cargado desde archivo.");
                }
                case 0 -> {
                    inventario.guardar();
                    System.out.println("Guardado y saliendo...");
                }
                default -> System.out.println("Opcion invalida.");
            }

        } while (opcion != 0);
    }

    private static void imprimirLista(String titulo, List<Videojuego> lista) {
        System.out.println(titulo);
        if (lista.isEmpty()) {
            System.out.println("(vacio)");
            return;
        }
        for (Videojuego v : lista) {
            System.out.println(v);
        }
    }

    private static int leerEntero(Scanner sc) {
        while (true) {
            try {
                String s = sc.nextLine().trim();
                return Integer.parseInt(s);
            } catch (Exception e) {
                System.out.print("Numero invalido, intenta otra vez: ");
            }
        }
    }

    private static double leerDouble(Scanner sc) {
        while (true) {
            try {
                String s = sc.nextLine().trim();
                return Double.parseDouble(s);
            } catch (Exception e) {
                System.out.print("Numero invalido, intenta otra vez: ");
            }
        }
    }
}
