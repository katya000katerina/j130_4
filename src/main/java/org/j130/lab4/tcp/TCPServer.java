package org.j130.lab4.tcp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import static org.j130.lab4.Utilities.getCurrentDateAndTime;

public class TCPServer {
    private String socketDisconnectCommand;

    public TCPServer(String socketDisconnectCommand) {
        this.socketDisconnectCommand = socketDisconnectCommand;
    }

    public void start(int port) {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (true) {
                Socket socket = serverSocket.accept();
                new Thread(() -> handleSocket(socket)).start();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void handleSocket(Socket socket) {
        try (socket;
             PrintWriter pw = new PrintWriter(socket.getOutputStream());
             BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            String message;
            while ((message = br.readLine()) != null) {
                String clientAddress = socket.getInetAddress().getHostAddress();
                int clientPort = socket.getPort();
                if (message.equals(socketDisconnectCommand)) {
                    System.out.printf("%s:%d disconnected from the server%n", clientAddress, clientPort);
                    break;
                }
                String receiptDateAndTime = getCurrentDateAndTime();
                System.out.printf("A message from %s:%d : %s%nDate and time of receipt: %s%n", clientAddress, clientPort, message, receiptDateAndTime);
                pw.println("Message received " + receiptDateAndTime);
                pw.flush();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        new TCPServer("disconnect").start(8080);
    }
}
