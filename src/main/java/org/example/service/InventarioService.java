package org.example.service;

import org.example.model.Videojuego;
import org.example.repository.VideojuegoRepository;

import java.util.List;

public class InventarioService {

    private VideojuegoRepository repository;

    public InventarioService(VideojuegoRepository repository) {
        this.repository = repository;
    }

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

        if (v == null) {
            return false;
        }

        if (v.getStock() > 0) {
            v.setStock(v.getStock() - 1);
            return true;
        }

        return false;
    }
}
