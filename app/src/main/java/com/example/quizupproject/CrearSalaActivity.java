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

        cliente = new Cliente();

        botonCrearSala.setOnClickListener(v -> {
            String codigoSala = nombreSalaEditText.getText().toString().trim();

            if (!codigoSala.isEmpty()) {
                SharedPreferences sharedPreferences = getSharedPreferences("SalaPreferences", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("codigo_sala", codigoSala);
                editor.apply();

                new Thread(() -> iniciarServidor(codigoSala)).start();
                cliente.conectarServidor(codigoSala);

                Toast.makeText(CrearSalaActivity.this, "Sala creada con código: " + codigoSala, Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(CrearSalaActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(CrearSalaActivity.this, "Por favor, ingresa el código de la sala", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void iniciarServidor(String codigoSala) {
        try {
            com.example.quizupproject.server.Server servidor = new com.example.quizupproject.server.Server();
            servidor.iniciarServidor(codigoSala);
            System.out.println("Servidor levantado con código: " + codigoSala);
        } catch (Exception e) {
            e.printStackTrace();
            runOnUiThread(() -> Toast.makeText(CrearSalaActivity.this, "Error al iniciar el servidor", Toast.LENGTH_SHORT).show());
        }
    }
}
