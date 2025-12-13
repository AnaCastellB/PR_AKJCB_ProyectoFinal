package org.example.repository;

import org.example.model.Videojuego;

import java.util.ArrayList;
import java.util.List;

public class VideojuegoRepository {

    private final List<Videojuego> videojuegos = new ArrayList<>();

    public void guardar(Videojuego videojuego) {
        // Si ya existe el ID, lo reemplazamos (evita duplicados por id)
        for (int i = 0; i < videojuegos.size(); i++) {
            if (videojuegos.get(i).getId() == videojuego.getId()) {
                videojuegos.set(i, videojuego);
                return;
            }
        }
        videojuegos.add(videojuego);
    }

    public List<Videojuego> obtenerTodos() {
        return new ArrayList<>(videojuegos); // copia para no exponer la lista interna
    }

    public Videojuego buscarPorTitulo(String titulo) {
        if (titulo == null) return null;
        for (Videojuego v : videojuegos) {
            if (v.getTitulo() != null && v.getTitulo().equalsIgnoreCase(titulo.trim())) {
                return v;
            }
        }
        return null;
    }

    public Videojuego buscarPorId(int id) {
        for (Videojuego v : videojuegos) {
            if (v.getId() == id) return v;
        }
        return null;
    }

    public void reemplazarTodo(List<Videojuego> nuevos) {
        videojuegos.clear();
        if (nuevos != null) videojuegos.addAll(nuevos);
    }
}
