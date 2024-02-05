package org.j130.lab4.tcp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class TCPClient {
    public void start(String host, int port, String disconnectCommand) {
        try (Socket socket = new Socket(host, port);
             PrintWriter pw = new PrintWriter(socket.getOutputStream());
             BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             BufferedReader console = new BufferedReader(new InputStreamReader(System.in))) {
            String input;
            while ((input = console.readLine()) != null) {
                pw.println(input);
                pw.flush();
                if (input.equals(disconnectCommand)) {
                    break;
                }
                System.out.println(br.readLine());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        new TCPClient().start("127.0.0.1", 8080, "disconnect");
    }
}
