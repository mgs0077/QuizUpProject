package com.example.quizupproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quizupproject.client.Cliente;

public class CrearSalaActivity extends AppCompatActivity {

    private EditText nombreSalaEditText;
    private ImageView botonCrearSala;
    private Cliente cliente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crear_sala);

        nombreSalaEditText = findViewById(R.id.codigodesala);
        botonCrearSala = findViewById(R.id.boton_crear_sesion);

        cliente = new Cliente();  // Cliente será el que se encargue de la comunicación

        botonCrearSala.setOnClickListener(v -> {
            String codigoSala = nombreSalaEditText.getText().toString().trim();

            if (!codigoSala.isEmpty()) {
                // Guardar el código de la sala en SharedPreferences
                SharedPreferences sharedPreferences = getSharedPreferences("SalaPreferences", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("codigo_sala", codigoSala);  // Guardamos el código de la sala
                editor.apply();

                // Iniciar el servidor en un hilo en segundo plano
                new Thread(() -> iniciarServidor(codigoSala)).start();

                // Conectar el cliente al servidor con el código de sala
                cliente.conectarServidor(codigoSala);

                // Mostrar mensaje de confirmación
                Toast.makeText(CrearSalaActivity.this, "Sala creada. Esperando jugadores...", Toast.LENGTH_SHORT).show();

                // Pasar a la siguiente actividad (pantalla de jugadores en la sala)
                Intent intent = new Intent(CrearSalaActivity.this, MainActivity.class); // Cambia "MainActivity" si el nombre de tu clase es diferente
                startActivity(intent);
                finish(); // Opcional: finalizar la actividad actual para evitar que el usuario vuelva a esta pantalla al presionar el botón atrás
            } else {
                Toast.makeText(CrearSalaActivity.this, "Por favor, ingresa el código de la sala", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Método para levantar el servidor
    private void iniciarServidor(String codigoSala) {
        try {
            // Aquí se simula la inicialización del servidor
            // Llamar al código del servidor para iniciar la sala
            // Suponiendo que tienes un método para levantar el servidor, como el siguiente:
            com.example.quizupproject.server.Server servidor = new com.example.quizupproject.server.Server();
            servidor.iniciarServidor(codigoSala);  // Pasamos el código de la sala para que se inicie con ese código
            System.out.println("Servidor levantado con el código de sala: " + codigoSala);
        } catch (Exception e) {
            e.printStackTrace();
            runOnUiThread(() ->
                    Toast.makeText(CrearSalaActivity.this, "Error al iniciar el servidor", Toast.LENGTH_SHORT).show());
        }
    }
}
