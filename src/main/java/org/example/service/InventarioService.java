package org.example.service;

import org.example.model.Videojuego;
import org.example.repository.VideojuegoRepository;

import java.util.List;

public class InventarioService {

    private final VideojuegoRepository repository;

    public InventarioService(VideojuegoRepository repository) {
        this.repository = repository;
    }

    // ---- API principal ----
    public void agregarVideojuego(Videojuego videojuego) {
        repository.guardar(videojuego);
    }

    public List<Videojuego> listarVideojuegos() {
        return repository.obtenerTodos();
    }

    public Videojuego buscarVideojuego(String titulo) {
        return repository.buscarPorTitulo(titulo);
    }

    public boolean venderVideojuego(String titulo) {
        Videojuego v = repository.buscarPorTitulo(titulo);

        if (v == null) return false;
        if (v.getStock() <= 0) return false;

        v.setStock(v.getStock() - 1);
        return true;
    }

    // ---- Métodos que tu Main está usando ----
    public void registrar(Videojuego videojuego) {
        agregarVideojuego(videojuego);
    }

    public void mostrarInventario() {
        System.out.println("Inventario:");
        for (Videojuego v : listarVideojuegos()) {
            System.out.println(v);
        }
    }

    public void buscarJuego(String titulo) {
        Videojuego v = buscarVideojuego(titulo);
        if (v == null) {
            System.out.println("No existe");
        } else if (v.getStock() <= 0) {
            System.out.println("No disponible");
        } else {
            System.out.println("Disponible: " + v);
        }
    }
}
