package org.example.service;

import org.example.database.DatabaseConnection;
import org.example.model.Videojuego;
import org.example.repository.VideojuegoRepository;

import java.util.ArrayList;
import java.util.List;

public class InventarioService {

    private final VideojuegoRepository repository;

    public InventarioService(VideojuegoRepository repository) {
        this.repository = repository;
    }

    /**
     * Cargar datos (ahora desde BD, no desde archivo)
     */
    public void cargar() {
        // Inicializar datos de prueba si la BD está vacía
        DatabaseConnection.inicializarDatosPrueba();
        System.out.println("Inventario cargado desde BD");
    }

    /**
     * Guardar datos NOTAAAA: ya no es necesario porque se guarda automáticamente en BD
     */
    public void guardar() {
        // Ya no hace nada porque cada operación guarda directamente en BD
        System.out.println("Cambios persistidos en BD automáticamente");
    }


    public void registrar(Videojuego videojuego) {
        repository.guardar(videojuego);
    }

    public List<Videojuego> listarInventario() {
        return repository.obtenerTodos();
    }

    public Videojuego buscarPorTitulo(String titulo) {
        return repository.buscarPorTitulo(titulo);
    }

    public Videojuego buscarPorId(int id) {
        return repository.buscarPorId(id);
    }

    public boolean venderPorTitulo(String titulo) {
        Videojuego v = repository.buscarPorTitulo(titulo);
        if (v == null || v.getStock() <= 0) return false;

        v.setStock(v.getStock() - 1);
        repository.guardar(v);
        return true;
    }

    public boolean actualizarStock(int id, int nuevoStock) {
        return repository.actualizarStock(id, nuevoStock);
    }

    public boolean eliminar(int id) {
        return repository.eliminarPorId(id);
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

    public void agregarVideojuego(Videojuego v) {
        registrar(v);
    }

    public Videojuego buscarVideojuego(String titulo) {
        return buscarPorTitulo(titulo);
    }

    public boolean venderVideojuego(String titulo) {
        return venderPorTitulo(titulo);
    }

    public List<Videojuego> listarVideojuegos() {
        return listarInventario();
    }
}