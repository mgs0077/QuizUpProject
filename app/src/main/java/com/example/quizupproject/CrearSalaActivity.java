package com.example.quizupproject;

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
                cliente.conectarServidor(codigoSala);
            } else {
                Toast.makeText(CrearSalaActivity.this, "Por favor, ingresa el código de la sala", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
