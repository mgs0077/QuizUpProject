package com.example.quizupproject.client;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class Cliente {
    private static final String SERVER_ADDRESS = "10.192.114.60"; // Asegúrate de que esta IP es la correcta
    private static final int SERVER_PORT = 56210;

    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private List<String> jugadores = new ArrayList<>();
    private OnJugadoresUpdateListener jugadoresUpdateListener;

    public Cliente() {}

    // Método para establecer el listener
    public void setOnJugadoresUpdateListener(OnJugadoresUpdateListener listener) {
        this.jugadoresUpdateListener = listener;
    }

    // Método para conectarse al servidor con un código de sala
    public void conectarServidor(String passwordSala) {
        new Thread(() -> {
            try {
                // Asegurarse de que el socket no esté cerrado antes de intentar conectarse
                if (socket == null || socket.isClosed()) {
                    socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
                    System.out.println("Conectado al servidor");
                }

                in = new DataInputStream(socket.getInputStream());
                out = new DataOutputStream(socket.getOutputStream());

                // Enviar solicitud para crear sala con código
                out.writeUTF("CREAR_SALA");
                out.writeUTF(passwordSala);
                out.flush();

                // Esperar respuesta del servidor
                String response = in.readUTF();
                if ("OK".equals(response)) {
                    System.out.println("Sala creada con éxito.");
                    String codigoSala = in.readUTF(); // Recibir el código de la sala
                    System.out.println("Código de sala recibido: " + codigoSala);

                    // Enviar mensaje para solicitar el estado del juego
                    out.writeUTF("ESTADO_JUEGO");
                    out.flush();

                    escucharServidor();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    // Método para unirse a una sala con un código
    public void unirseASala(String codigoSala) {
        new Thread(() -> {
            try {
                if (socket == null || socket.isClosed()) {
                    socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
                    System.out.println("Conectado al servidor");
                }

                in = new DataInputStream(socket.getInputStream());
                out = new DataOutputStream(socket.getOutputStream());

                // Enviar solicitud para unirse a la sala con el código
                out.writeUTF("UNIRSE_SALA");
                out.writeUTF(codigoSala);
                out.flush();

                // Esperar respuesta del servidor
                String response = in.readUTF();
                if ("OK".equals(response)) {
                    System.out.println("Unido a la sala con éxito.");
                    escucharServidor();
                } else {
                    System.out.println("Código de sala incorrecto.");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void escucharServidor() {
        new Thread(() -> {
            try {
                while (true) {
                    String serverMessage = in.readUTF();
                    System.out.println("Servidor: " + serverMessage);

                    // Simulación de mensajes del servidor sobre jugadores
                    if (serverMessage.startsWith("JUGADORES")) {
                        // Se espera que el servidor envíe una lista de jugadores
                        List<String> nuevosJugadores = new ArrayList<>();
                        String[] jugadoresArray = serverMessage.split(",");
                        for (String jugador : jugadoresArray) {
                            nuevosJugadores.add(jugador);
                        }

                        // Notificar a la actividad sobre los cambios en la lista de jugadores
                        if (jugadoresUpdateListener != null) {
                            jugadoresUpdateListener.onJugadoresUpdate(nuevosJugadores);
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public interface OnJugadoresUpdateListener {
        void onJugadoresUpdate(List<String> nuevosJugadores);
    }
}
