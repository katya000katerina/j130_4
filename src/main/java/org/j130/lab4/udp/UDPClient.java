package org.j130.lab4.udp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPClient {
    public void start(String host, int port, String stopClientApplicationCommand) {
        try (DatagramSocket datagramSocket = new DatagramSocket();
             BufferedReader console = new BufferedReader(new InputStreamReader(System.in))) {
            String message;
            while ((message = console.readLine()) != null) {
                if (message.equals(stopClientApplicationCommand)) {
                    break;
                }
                byte[] messageBuffer = message.getBytes();
                DatagramPacket messageDatagramPacket = new DatagramPacket(messageBuffer, messageBuffer.length, InetAddress.getByName(host), port);
                datagramSocket.send(messageDatagramPacket);

                byte[] answerBuffer = new byte[256];
                DatagramPacket answerDatagramPacket = new DatagramPacket(answerBuffer, answerBuffer.length);
                datagramSocket.receive(answerDatagramPacket);
                String answer = new String(answerDatagramPacket.getData(), 0, answerDatagramPacket.getLength());
                System.out.println(answer);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        new UDPClient().start("127.0.0.1", 9009, "shutdown");
    }
}