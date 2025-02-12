package com.example.quizupproject.server;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.Semaphore;
import java.util.Scanner;  // Importar Scanner para leer desde la consola

public class Server {
    private static final int PORT = 56210;
    private static final int MAX_PLAYERS = 4;
    private static Semaphore semaphore = new Semaphore(MAX_PLAYERS);  // Controla el número máximo de jugadores
    private static List<ClientHandler> players = Collections.synchronizedList(new ArrayList<>());
    private static int turnoActual = -1;
    private static String codigoSala;  // Usar String para manejar contraseñas

    // Método principal para iniciar el servidor
    public static void main(String[] args) {
        // Crear un Scanner para leer desde la consola
        Scanner scanner = new Scanner(System.in);

        // Solicitar al usuario que ingrese un código para la sala
        System.out.print("Ingrese el código de la sala: ");
        String codigoIngresado = scanner.nextLine();  // Leer el código de sala

        // Validar que el código ingresado no esté vacío
        if (codigoIngresado.isEmpty()) {
            System.out.println("El código de sala no puede estar vacío. Usando el valor predeterminado '12345'.");
            codigoIngresado = "12345";  // Asignar un valor predeterminado si el usuario no proporciona uno
        }

        // Crear instancia del servidor e iniciar con el código de sala proporcionado
        Server server = new Server();
        server.iniciarServidor(codigoIngresado);  // Pasar el código de la sala ingresado por el usuario
    }

    // Método para iniciar el servidor con un código de sala
    public void iniciarServidor(String codigoSala) {
        Server.codigoSala = codigoSala;  // Asignar el código de sala

        try (ServerSocket serverSocket = new ServerSocket(PORT, 50, InetAddress.getByName("0.0.0.0"))) {
            System.out.println("Servidor iniciado. Esperando conexiones en el puerto " + PORT + "...");

            while (true) {
                // Aceptar conexiones de los clientes
                Socket clientSocket = serverSocket.accept();

                // Si el servidor no está lleno, se acepta la conexión
                if (semaphore.tryAcquire()) {
                    System.out.println("Cliente conectado: " + clientSocket.getInetAddress());

                    // Crear el manejador de clientes
                    ClientHandler player = new ClientHandler(clientSocket);
                    synchronized (players) {
                        players.add(player);  // Añadir el nuevo jugador a la lista
                    }
                    new Thread(player).start();  // Iniciar el hilo que manejará la comunicación con el cliente

                } else {
                    // Si el servidor está lleno, se rechaza la conexión
                    DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());
                    out.writeUTF("Servidor lleno. Inténtelo más tarde.");
                    clientSocket.close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Clase interna que maneja la comunicación con los clientes
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
                // Establecer flujo de entrada y salida
                in = new DataInputStream(clientSocket.getInputStream());
                out = new DataOutputStream(clientSocket.getOutputStream());

                // Leer la solicitud del cliente (puede ser "CREAR_SALA" o "UNIRSE_SALA")
                String tipoSolicitud = in.readUTF();

                // Si el cliente quiere crear una sala
                if ("CREAR_SALA".equals(tipoSolicitud)) {
                    out.writeUTF("OK");  // Confirmar que la sala fue creada
                    out.flush();
                }
                // Si el cliente quiere unirse a una sala existente
                else if ("UNIRSE_SALA".equals(tipoSolicitud)) {
                    String codigoIntentado = in.readUTF();  // Leer el código de sala que el cliente proporciona
                    if (codigoIntentado.equals(codigoSala)) {
                        out.writeUTF("OK");  // Código correcto
                        out.flush();
                        // Aquí puedes agregar más lógica para gestionar al jugador dentro de la sala
                    } else {
                        out.writeUTF("Código de sala incorrecto");  // Si el código es incorrecto
                        out.flush();
                        clientSocket.close();  // Cerrar la conexión con el cliente
                    }
                }

                // Enviar el código de la sala al cliente
                out.writeUTF("Código de sala: " + codigoSala);  // Enviar el código de la sala
                out.flush();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
