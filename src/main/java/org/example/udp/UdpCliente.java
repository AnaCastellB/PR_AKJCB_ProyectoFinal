package org.example.udp;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UdpCliente {

    public static void enviarMensaje(String mensaje) {
        try (DatagramSocket socket = new DatagramSocket()) {

            InetAddress ip = InetAddress.getByName("localhost");

            byte[] buffer = mensaje.getBytes();

            DatagramPacket packet = new DatagramPacket(
                    buffer,
                    buffer.length,
                    ip,
                    9999
            );

            socket.send(packet);
            System.out.println("Mensaje enviado por UDP: " + mensaje);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Para probar directo
    public static void main(String[] args) {
        enviarMensaje("Hola desde UDP");
    }
}
