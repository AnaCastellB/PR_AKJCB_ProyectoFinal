package org.example.udp;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UdpCliente {

    private static final int PUERTO = 9999;
    private static final String HOST = "localhost";

    public static void enviarMensaje(String mensaje) {
        try (DatagramSocket socket = new DatagramSocket()) {

            InetAddress ip = InetAddress.getByName(HOST);
            byte[] buffer = mensaje.getBytes();

            DatagramPacket packet = new DatagramPacket(
                    buffer,
                    buffer.length,
                    ip,
                    PUERTO
            );

            socket.send(packet);

            System.out.println("Mensaje UDP enviado");

        } catch (Exception e) {
            System.out.println("Error enviando UDP: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        enviarMensaje("SECURE|Prueba desde UdpCliente");
    }
}
