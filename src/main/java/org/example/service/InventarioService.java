package org.example.service;

import org.example.model.Videojuego;
import java.util.ArrayList;
import java.util.List;

public class InventarioService {

    private List<Videojuego> inventario = new ArrayList<>();

    public void agregarVideojuego(Videojuego v) {
        inventario.add(v);
    }

    public Videojuego buscarPorTitulo(String titulo) {
        for (Videojuego v : inventario) {
            if (v.getTitulo().equalsIgnoreCase(titulo)) {
                return v;
            }
        }
        return null;
    }

    public void mostrarInventario() {
        for (Videojuego v : inventario) {
            System.out.println(v);
        }
    }
}
