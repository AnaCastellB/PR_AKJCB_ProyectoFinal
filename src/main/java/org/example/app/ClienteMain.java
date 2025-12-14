package org.example.app;

import org.example.model.Videojuego;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class ClienteMain {

    private static final String HOST = "localhost";
    private static final int PUERTO = 5000;

    public static void main(String[] args) {
        try (Socket socket = new Socket(HOST, PUERTO);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true, StandardCharsets.UTF_8);
             Scanner sc = new Scanner(System.in)) {

            while (true) {
                System.out.println("\n VENDEDOR TIENDA DE VIDEOJUEGOS ");
                System.out.println("1) Registrar videojuego");
                System.out.println("2) Listar inventario");
                System.out.println("3) Buscar videojuego por titulo");
                System.out.println("4) Vender videojuego por titulo");
                System.out.println("0) Salir");
                System.out.print("Opcion: ");

                String op = sc.nextLine().trim();

                switch (op) {
                    case "1": {
                        System.out.print("ID: ");
                        int id = Integer.parseInt(sc.nextLine().trim());
                        System.out.print("Titulo: ");
                        String titulo = sc.nextLine().trim();
                        System.out.print("Consola: ");
                        String consola = sc.nextLine().trim();
                        System.out.print("Precio: ");
                        double precio = Double.parseDouble(sc.nextLine().trim());
                        System.out.print("Stock: ");
                        int stock = Integer.parseInt(sc.nextLine().trim());

                        out.println("REGISTRAR|" + id + "|" + titulo + "|" + consola + "|" + precio + "|" + stock);
                        System.out.println(leerUnaRespuesta(in));
                        break;
                    }

                    case "2": {
                        out.println("LISTAR");
                        String head = in.readLine();
                        if (head != null && head.startsWith("OK")) {
                            System.out.println("Inventario:");
                            String linea;
                            while ((linea = in.readLine()) != null) {
                                if (linea.equals("FIN")) break;
                                Videojuego v = VideojuegoTraductor.desdeLinea(linea);
                                System.out.println(v);
                            }
                        } else {
                            System.out.println("Error al listar: " + head);
                        }
                        break;
                    }

                    case "3": {
                        System.out.print("Titulo: ");
                        String titulo = sc.nextLine().trim();

                        out.println("BUSCAR|" + titulo);
                        String resp = leerUnaRespuesta(in);

                        if (resp.startsWith("OK|")) {
                            String data = resp.substring(3);
                            Videojuego v = VideojuegoTraductor.desdeLinea(data);
                            System.out.println("Disponible: " + v);
                        } else {
                            System.out.println(resp);
                        }
                        break;
                    }

                    case "4": {
                        System.out.print("Titulo: ");
                        String titulo = sc.nextLine().trim();

                        out.println("VENDER|" + titulo);
                        System.out.println(leerUnaRespuesta(in));
                        break;
                    }

                    case "0": {
                        out.println("SALIR");
                        System.out.println(leerUnaRespuesta(in));
                        return;
                    }

                    default:
                        System.out.println("Opción inválida.");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String leerUnaRespuesta(BufferedReader in) throws Exception {
        String resp = in.readLine();
        return (resp == null) ? "ERR|Sin respuesta del servidor" : resp;
    }
}
