package com.example.quizupproject.server;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.Semaphore;

public class Server {
    private static final int PORT = 56210;
    private static final int MAX_PLAYERS = 4;
    private static Semaphore semaphore = new Semaphore(MAX_PLAYERS);
    private static List<ClientHandler> players = Collections.synchronizedList(new ArrayList<>());
    private static int turnoActual = -1;
    private static String codigoSala;  // Usar String para manejar contraseñas

    public static void main(String[] args) {
        Server servidor = new Server();
        servidor.ejecutarServidor();
    }

    public void ejecutarServidor() {
        try (ServerSocket serverSocket = new ServerSocket(PORT, 50, InetAddress.getByName("0.0.0.0"))) {
            System.out.println("Servidor iniciado. Esperando conexiones en el puerto " + PORT + "...");

            while (true) {
                Socket clientSocket = serverSocket.accept();

                if (semaphore.tryAcquire()) {
                    System.out.println("Cliente conectado: " + clientSocket.getInetAddress());

                    // Crear el manejador de clientes
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
                if ("CREAR_SALA".equals(tipoSolicitud)) {
                    codigoSala = in.readUTF(); // Obtener la contraseña de la sala
                    System.out.println("Código de sala recibido: " + codigoSala);
                    out.writeUTF("OK");
                    out.flush();
                }

                out.writeUTF("Código de sala: " + codigoSala); // Enviar el código de la sala al cliente

                while (true) {
                    String message = in.readUTF();

                    if ("TURNO_FINALIZADO".equals(message)) {
                        avanzarTurno();
                    }
                }
            } catch (IOException e) {
                System.out.println("Jugador desconectado.");
                synchronized (players) {
                    players.remove(this);
                }
                semaphore.release();
                if (!players.isEmpty()) {
                    ajustarTurnosTrasDesconexion();
                }
            }
        }

        public void enviarMensaje(String mensaje) {
            try {
                out.writeUTF(mensaje);
            } catch (IOException e) {
                System.out.println("Error al enviar mensaje al cliente.");
            }
        }
    }

    private static void avanzarTurno() {
        if (!players.isEmpty()) {
            turnoActual = (turnoActual + 1) % players.size();
            actualizarTurnos();
        }
    }

    private static void actualizarTurnos() {
        synchronized (players) {
            for (int i = 0; i < players.size(); i++) {
                ClientHandler player = players.get(i);
                if (i == turnoActual) {
                    player.enviarMensaje("TURNO_ACTIVO");
                } else {
                    player.enviarMensaje("ESPERA_TURNO");
                }
            }
        }
    }

    private static void ajustarTurnosTrasDesconexion() {
        if (players.isEmpty()) {
            turnoActual = -1;
            return;
        }
        if (turnoActual >= players.size()) {
            turnoActual = 0;
        }
        actualizarTurnos();
    }
}
