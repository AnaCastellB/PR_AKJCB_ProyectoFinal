package org.example.app;

import org.example.model.Videojuego;
import org.example.repository.VideojuegoRepository;
import org.example.service.InventarioService;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServidorMain {

    private static final int PUERTO = 5000;
    private static final int MAX_CLIENTES = 10;

    // Repositorio compartido entre todos los hilos
    private static VideojuegoRepository repo;
    private static InventarioService inventario;

    public static void main(String[] args) {
        System.out.println("= SERVIDOR TIENDA DE VIDEOJUEGOS ");
        System.out.println("Iniciando en puerto " + PUERTO + "...");

        // Inicializar repositorio compartido
        repo = new VideojuegoRepository();
        inventario = new InventarioService(repo);

        // Cargar datos persistentes
        inventario.cargar();

        // Datos de prueba
        if (inventario.listarInventario().isEmpty()) {
            inventario.agregarVideojuego(new Videojuego(1, "Zelda", "Switch", 1399, 3));
            inventario.agregarVideojuego(new Videojuego(2, "Mario Kart", "Switch", 1199, 5));
            inventario.agregarVideojuego(new Videojuego(3, "Halo Infinite", "Xbox", 1299, 0));
            System.out.println("Datos de prueba cargados.");
        }

        // Pool de hilos para manejar múltiples clientes
        ExecutorService pool = Executors.newFixedThreadPool(MAX_CLIENTES);

        try (ServerSocket server = new ServerSocket(PUERTO)) {
            System.out.println("Servidor listo. Esperando clientes...\n");

            int clienteId = 1;
            while (true) {
                // Aceptar nueva conexión
                Socket cliente = server.accept();

                System.out.println("[CONEXIÓN] Cliente #" + clienteId +
                        " conectado desde " + cliente.getInetAddress());

                // Crear y ejecutar un hilo para este cliente
                ClienteHandler handler = new ClienteHandler(cliente, clienteId, inventario);
                pool.execute(handler);

                clienteId++;
            }
        } catch (Exception e) {
            System.err.println("Error en el servidor: " + e.getMessage());
            e.printStackTrace();
        } finally {
            pool.shutdown();
        }
    }
}

/**
 * Clase que maneja la comunicación con un cliente individual
 * Cada instancia se ejecuta en un hilo separado
 */
class ClienteHandler implements Runnable {

    private final Socket socket;
    private final int clienteId;
    private final InventarioService inventario;

    public ClienteHandler(Socket socket, int clienteId, InventarioService inventario) {
        this.socket = socket;
        this.clienteId = clienteId;
        this.inventario = inventario;
    }

    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(
                new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true, StandardCharsets.UTF_8)) {

            log("Sesión iniciada");

            String linea;
            while ((linea = in.readLine()) != null) {
                log("Comando recibido: " + linea);

                String resp = procesar(linea, out);

                if (resp != null) {
                    out.println(resp);
                }

                if ("BYE".equals(resp)) {
                    break;
                }
            }

            log("Sesión finalizada");

        } catch (Exception e) {
            log("Error: " + e.getMessage());
        } finally {
            try {
                socket.close();
            } catch (Exception e) {
                // Ignorar
            }
        }
    }

    private String procesar(String linea, PrintWriter out) {
        try {
            String[] p = linea.split("\\|", -1);
            String cmd = p[0].trim().toUpperCase();

            switch (cmd) {
                case "LISTAR": {
                    out.println("OK");
                    List<Videojuego> lista = inventario.listarInventario(); // ✅ CORREGIDO
                    for (Videojuego v : lista) {
                        out.println(VideojuegoTraductor.serializar(v));
                    }
                    out.println("FIN");
                    return null; // Ya respondimos por stream
                }

                case "BUSCAR": {
                    if (p.length < 2) return "ERR|Falta titulo";
                    String titulo = p[1];

                    Videojuego v = inventario.buscarVideojuego(titulo);
                    if (v == null) return "NO|No encontrado";
                    if (v.getStock() <= 0) return "NO|No disponible";

                    return "OK|" + VideojuegoTraductor.serializar(v);
                }

                case "REGISTRAR": {
                    if (p.length != 6) {
                        return "ERR|Formato: REGISTRAR|id|titulo|consola|precio|stock";
                    }

                    int id = Integer.parseInt(p[1]);
                    String titulo = p[2];
                    String consola = p[3];
                    double precio = Double.parseDouble(p[4]);
                    int stock = Integer.parseInt(p[5]);

                    synchronized (inventario) {
                        inventario.agregarVideojuego(new Videojuego(id, titulo, consola, precio, stock));
                        inventario.guardar(); // Persistir cambios
                    }

                    return "OK|Registrado";
                }

                case "VENDER": {
                    if (p.length < 2) return "ERR|Falta titulo";
                    String titulo = p[1];

                    boolean ok;
                    synchronized (inventario) {
                        ok = inventario.venderVideojuego(titulo);
                        if (ok) {
                            inventario.guardar(); // Persistir cambios
                        }
                    }

                    return ok ? "OK|Venta realizada" : "NO|No disponible";
                }

                case "SALIR":
                    return "BYE";

                default:
                    return "ERR|Comando desconocido: " + cmd;
            }

        } catch (Exception e) {
            return "ERR|" + e.getMessage();
        }
    }

    private void log(String mensaje) {
        System.out.println("[Cliente #" + clienteId + "] " + mensaje);
    }
}