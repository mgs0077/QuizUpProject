package com.example.quizupproject;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quizupproject.client.Cliente;

public class UnirseSalaActivity extends AppCompatActivity {

    private EditText codigoSalaEditText;
    private ImageView botonUnirse;
    private Cliente cliente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.unirse_sala);

        codigoSalaEditText = findViewById(R.id.editText2);
        botonUnirse = findViewById(R.id.boton_unirse_sala); // Verifica que el ID esté bien configurado

        cliente = new Cliente();

        botonUnirse.setOnClickListener(v -> {
            String codigoSala = codigoSalaEditText.getText().toString().trim();

            if (!codigoSala.isEmpty()) {
                // Conectar al servidor para verificar el código de sala
                cliente.unirseASala(codigoSala);

                // Mostrar mensaje dependiendo de la respuesta del servidor
                // Asumiendo que el servidor notificará si el código es correcto o no
            } else {
                Toast.makeText(UnirseSalaActivity.this, "Por favor, ingresa el código de la sala", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
