package com.example.quizupproject;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quizupproject.client.Cliente;

public class CrearSalaActivity extends AppCompatActivity {

    private EditText nombreSalaEditText, contrasenaEditText;
    private Button botonCrearSala;
    private Cliente cliente;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crear_sala); // El layout de la pantalla que compartiste

        nombreSalaEditText = findViewById(R.id.introducir_correo);
        contrasenaEditText = findViewById(R.id.editText2);
        botonCrearSala = findViewById(R.id.boton_crear_sesion);  // Asegúrate de que el ID coincide con el de tu layout

        cliente = new Cliente();  // Cliente será el que se encargue de la comunicación

        botonCrearSala.setOnClickListener(v -> {
            String nombreSala = nombreSalaEditText.getText().toString().trim();
            String contrasenaSala = contrasenaEditText.getText().toString().trim();

            if (!nombreSala.isEmpty() && !contrasenaSala.isEmpty()) {
                cliente.conectarServidor(contrasenaSala);
            } else {
                Toast.makeText(CrearSalaActivity.this, "Por favor, ingresa los datos correctamente", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
