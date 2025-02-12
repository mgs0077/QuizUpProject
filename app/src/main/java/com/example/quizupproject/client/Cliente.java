package com.example.quizupproject.client;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Cliente {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 56210;

    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private boolean esMiTurno = false;

    public Cliente() {}

    // Método para crear una sala con contraseña
    public void conectarServidor(String passwordSala) {
        new Thread(() -> {
            try {
                if (socket != null && !socket.isClosed()) {
                    socket.close();
                }

                socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
                System.out.println("Conectado al servidor");

                in = new DataInputStream(socket.getInputStream());
                out = new DataOutputStream(socket.getOutputStream());

                out.writeUTF("CREAR_SALA");
                out.writeUTF(passwordSala);
                out.flush();

                String response = in.readUTF();
                if ("OK".equals(response)) {
                    System.out.println("Sala creada con éxito.");
                    out.writeUTF("ESTADO_JUEGO");
                    out.flush();
                    escucharServidor();
                } else {
                    System.out.println("No se pudo crear la sala.");
                }

            } catch (IOException e) {
                System.out.println("No se pudo conectar al servidor: " + e.getMessage());
                e.printStackTrace();
            }
        }).start();
    }

    // Método sobrecargado para unirse a una sala sin contraseña
    public void conectarServidor() {
        new Thread(() -> {
            try {
                if (socket != null && !socket.isClosed()) {
                    socket.close();
                }

                socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
                System.out.println("Conectado al servidor");

                in = new DataInputStream(socket.getInputStream());
                out = new DataOutputStream(socket.getOutputStream());

                out.writeUTF("UNIR_SALA");
                out.flush();

                String response = in.readUTF();
                if ("OK".equals(response)) {
                    System.out.println("Unido correctamente a la sala.");
                    out.writeUTF("ESTADO_JUEGO");
                    out.flush();
                    escucharServidor();
                } else {
                    System.out.println("No se pudo unir a la sala.");
                }

            } catch (IOException e) {
                System.out.println("No se pudo conectar al servidor: " + e.getMessage());
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

                    if ("TURNO_ACTIVO".equals(serverMessage)) {
                        esMiTurno = true;
                        System.out.println("Es tu turno.");
                    } else if ("ESPERA_TURNO".equals(serverMessage)) {
                        esMiTurno = false;
                        System.out.println("Esperando turno...");
                    } else if ("DESCONEXION".equals(serverMessage)) {
                        System.out.println("El servidor ha desconectado al cliente.");
                        desconectarServidor();
                        break;
                    }
                }
            } catch (IOException e) {
                System.out.println("Error al recibir mensaje del servidor.");
                desconectarServidor();
            }
        }).start();
    }

    public void enviarFinTurno() {
        if (esMiTurno) {
            try {
                out.writeUTF("TURNO_FINALIZADO");
                out.flush();
                esMiTurno = false;
                System.out.println("Turno finalizado.");
            } catch (IOException e) {
                System.out.println("Error al enviar mensaje de fin de turno.");
            }
        } else {
            System.out.println("No es tu turno.");
        }
    }

    public void desconectarServidor() {
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
                System.out.println("Desconectado del servidor.");
            }
        } catch (IOException e) {
            System.out.println("Error al desconectar del servidor.");
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Cliente cliente = new Cliente();

        System.out.print("Introduce la contraseña para crear la sala: ");
        String passwordSala = scanner.nextLine();

        cliente.conectarServidor(passwordSala);
    }
}
