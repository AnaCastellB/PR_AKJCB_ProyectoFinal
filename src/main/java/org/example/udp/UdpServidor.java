package org.example.udp;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class UdpServidor {

    public static final int PUERTO = 9999;

    public static void main(String[] args) {
        try (DatagramSocket socket = new DatagramSocket(PUERTO)) {

            System.out.println("Servidor UDP escuchando en puerto " + PUERTO);

            byte[] buffer = new byte[1024];

            while (true) {
                DatagramPacket packet =
                        new DatagramPacket(buffer, buffer.length);

                socket.receive(packet);

                String mensaje = new String(
                        packet.getData(),
                        0,
                        packet.getLength()
                );

                System.out.println("Mensaje recibido: " + mensaje);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
