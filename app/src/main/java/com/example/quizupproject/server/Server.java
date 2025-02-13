package com.example.quizupproject.server;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.Semaphore;
import java.util.Scanner;

public class Server {
    private static final int PORT = 56210;
    private static final int MAX_PLAYERS = 4;
    private static Semaphore semaphore = new Semaphore(MAX_PLAYERS);
    private static List<ClientHandler> players = Collections.synchronizedList(new ArrayList<>());
    private static String codigoSala;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Ingrese el código de la sala: ");
        String codigoIngresado = scanner.nextLine().trim();

        if (codigoIngresado.isEmpty()) {
            System.out.println("El código de sala no puede estar vacío. Usando '12345' por defecto.");
            codigoIngresado = "12345";
        }

        Server server = new Server();
        server.iniciarServidor(codigoIngresado);
    }

    public void iniciarServidor(String codigoSala) {
        Server.codigoSala = codigoSala;
        System.out.println("Código de sala asignado en el servidor: " + Server.codigoSala);

        try (ServerSocket serverSocket = new ServerSocket(PORT, 50, InetAddress.getByName("0.0.0.0"))) {
            System.out.println("Servidor iniciado en el puerto " + PORT + " con código de sala: " + codigoSala);

            while (true) {
                Socket clientSocket = serverSocket.accept();

                if (semaphore.tryAcquire()) {
                    System.out.println("Cliente conectado: " + clientSocket.getInetAddress());

                    ClientHandler player = new ClientHandler(clientSocket);
                    synchronized (players) {
                        players.add(player);
                    }
                    new Thread(player).start();
                } else {
                    DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());
                    out.writeUTF("Servidor lleno. Inténtelo más tarde.");
                    clientSocket.close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class ClientHandler implements Runnable {
        private Socket clientSocket;
        private DataInputStream in;
        private DataOutputStream out;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        @Override
        public void run() {
            try {
                in = new DataInputStream(clientSocket.getInputStream());
                out = new DataOutputStream(clientSocket.getOutputStream());

                String tipoSolicitud = in.readUTF();
                System.out.println("Tipo de solicitud recibida: " + tipoSolicitud);

                if ("CREAR_SALA".equals(tipoSolicitud)) {
                    out.writeUTF("OK");
                    out.flush();
                    System.out.println("Sala creada exitosamente.");
                } else if ("UNIRSE_SALA".equals(tipoSolicitud)) {
                    String codigoIntentado = in.readUTF().trim();
                    System.out.println("Código recibido del cliente: [" + codigoIntentado + "]");
                    System.out.println("Código de sala en el servidor: [" + codigoSala + "]");

                    if (codigoIntentado.equals(codigoSala)) {
                        out.writeUTF("OK");
                        out.flush();
                        System.out.println("Cliente unido a la sala.");
                    } else {
                        out.writeUTF("Código de sala incorrecto");
                        out.flush();
                        System.out.println("Código de sala incorrecto. Cerrando conexión.");
                        clientSocket.close();
                    }
                }

                // Enviar el código de sala al cliente
                out.writeUTF("Código de sala en servidor: " + codigoSala);
                out.flush();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
