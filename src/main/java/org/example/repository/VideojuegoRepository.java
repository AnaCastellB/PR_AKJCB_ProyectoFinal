package org.example.repository;

import org.example.model.Videojuego;

import java.util.ArrayList;
import java.util.List;

public class VideojuegoRepository {

    private List<Videojuego> videojuegos;

    public VideojuegoRepository() {
        this.videojuegos = new ArrayList<>();
    }

    public void guardar(Videojuego videojuego) {
        videojuegos.add(videojuego);
    }

    public List<Videojuego> obtenerTodos() {
        return videojuegos;
    }

    public Videojuego buscarPorTitulo(String titulo) {
        for (Videojuego v : videojuegos) {
            if (v.getTitulo().equalsIgnoreCase(titulo)) {
                return v;
            }
        }
        return null;
    }
}
