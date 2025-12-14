package org.example.service;

import org.example.model.Videojuego;
import org.example.repository.VideojuegoRepository;
import org.example.app.VideojuegoTraductor;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class InventarioService {

    private final VideojuegoRepository repository;
    private final Path archivo = Paths.get("videojuegos.txt"); // se guarda en la raíz del proyecto

    public InventarioService(VideojuegoRepository repository) {
        this.repository = repository;
    }

    // ====== Persistencia simple ======
    public void cargar() {
        try {
            if (!Files.exists(archivo)) return;

            List<String> lineas = Files.readAllLines(archivo, StandardCharsets.UTF_8);
            List<Videojuego> cargados = new ArrayList<>();

            for (String linea : lineas) {
                if (linea == null) continue;
                linea = linea.trim();
                if (linea.isEmpty()) continue;
                cargados.add(VideojuegoTraductor.deserializar(linea));
            }

            repository.reemplazarTodo(cargados);
        } catch (Exception e) {
            System.out.println("No se pudo cargar archivo: " + e.getMessage());
        }
    }

    public void guardar() {
        try {
            List<Videojuego> lista = repository.obtenerTodos();
            List<String> lineas = new ArrayList<>();
            for (Videojuego v : lista) {
                lineas.add(VideojuegoTraductor.serializar(v));
            }
            Files.write(archivo, lineas, StandardCharsets.UTF_8);
        } catch (Exception e) {
            System.out.println("No se pudo guardar archivo: " + e.getMessage());
        }
    }

    //  Métodos del menú
    public void registrar(Videojuego videojuego) {
        repository.guardar(videojuego);
    }

    public List<Videojuego> listarInventario() {
        return repository.obtenerTodos();
    }

    public void mostrarInventario() {
        System.out.println("Inventario:");
        for (Videojuego v : repository.obtenerTodos()) {
            System.out.println(v);
        }
    }

    public Videojuego buscarPorTitulo(String titulo) {
        return repository.buscarPorTitulo(titulo);
    }

    public Videojuego buscarPorId(int id) {
        return repository.buscarPorId(id);
    }

    public boolean venderPorTitulo(String titulo) {
        Videojuego v = repository.buscarPorTitulo(titulo);
        if (v == null) return false;
        if (v.getStock() <= 0) return false;

        v.setStock(v.getStock() - 1);
        repository.guardar(v); // guarda actualización
        return true;
    }

    public List<Videojuego> listarDisponibles() {
        List<Videojuego> res = new ArrayList<>();
        for (Videojuego v : repository.obtenerTodos()) {
            if (v.getStock() > 0) res.add(v);
        }
        return res;
    }

    public List<Videojuego> listarAgotados() {
        List<Videojuego> res = new ArrayList<>();
        for (Videojuego v : repository.obtenerTodos()) {
            if (v.getStock() <= 0) res.add(v);
        }
        return res;
    }

    public void agregarVideojuego(Videojuego v) { registrar(v); }
    public Videojuego buscarVideojuego(String titulo) { return buscarPorTitulo(titulo); }
    public boolean venderVideojuego(String titulo) { return venderPorTitulo(titulo); }
}
