package org.example.model;

import java.io.Serializable;
import java.util.Objects;

public class Videojuego implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private String titulo;
    private String consola;
    private double precio;
    private int stock;

    public Videojuego(int id, String titulo, String consola, double precio, int stock) {
        this.id = id;
        this.titulo = titulo;
        this.consola = consola;
        this.precio = precio;
        this.stock = stock;
    }

    // Getters
    public int getId() { return id; }
    public String getTitulo() { return titulo; }
    public String getConsola() { return consola; }
    public double getPrecio() { return precio; }
    public int getStock() { return stock; }

    // Setters (por si luego editas datos)
    public void setId(int id) { this.id = id; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public void setConsola(String consola) { this.consola = consola; }
    public void setPrecio(double precio) { this.precio = precio; }
    public void setStock(int stock) { this.stock = stock; }

    // Comparación por ID (muy útil en repositorios/colecciones)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Videojuego videojuego = (Videojuego) o;
        return id == videojuego.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Videojuego{" +
                "id=" + id +
                ", titulo='" + titulo + '\'' +
                ", consola='" + consola + '\'' +
                ", precio=" + precio +
                ", stock=" + stock +
                '}';
    }
}
