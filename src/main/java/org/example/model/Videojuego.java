package org.example.model;

public class Videojuego {

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

    public String getTitulo() {
        return titulo;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
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
