package org.j130.lab4.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import static org.j130.lab4.Utilities.getCurrentDateAndTime;

public class UDPServer {

    public void start(int port) {
        try (DatagramSocket datagramSocket = new DatagramSocket(port)) {
            while (true) {
                byte[] buffer = new byte[256];
                DatagramPacket messageDatagramPacket = new DatagramPacket(buffer, buffer.length);
                datagramSocket.receive(messageDatagramPacket);
                new Thread(() -> {
                    DatagramPacket answerDatagramPacket = handleDatagramPacket(messageDatagramPacket);
                    try {
                        datagramSocket.send(answerDatagramPacket);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }).start();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private DatagramPacket handleDatagramPacket(DatagramPacket datagramPacket) {
        InetAddress clientAddress = datagramPacket.getAddress();
        int clientPort = datagramPacket.getPort();
        String message = new String(datagramPacket.getData(), 0, datagramPacket.getLength());
        String receiptDateAndTime = getCurrentDateAndTime();
        System.out.printf("A message from %s:%d : %s%nDate and time of receipt: %s%n", clientAddress.getHostAddress(), clientPort, message, receiptDateAndTime);
        String answerToClient = "Message received " + receiptDateAndTime;
        byte[] answerBuffer = answerToClient.getBytes();
        return new DatagramPacket(answerBuffer, answerBuffer.length, clientAddress, clientPort);
    }

    public static void main(String[] args) {
        new UDPServer().start(9009);
    }
}
