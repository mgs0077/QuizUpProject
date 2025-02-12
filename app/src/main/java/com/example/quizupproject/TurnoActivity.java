package com.example.quizupproject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quizupproject.client.Cliente;

import java.util.List;

public class TurnoActivity extends AppCompatActivity {
    private TextView jugadoresTextView;
    private Button iniciarButton;
    private List<String> jugadores;
    private Cliente cliente;
    private Handler uiHandler = new Handler(Looper.getMainLooper());

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sala_creador_turno);

        jugadoresTextView = findViewById(R.id.jugadoresTextView);
        iniciarButton = findViewById(R.id.boton_iniciar);

        jugadores = List.of("Jugador1", "Jugador2", "Jugador3", "Jugador4");

        StringBuilder jugadoresTexto = new StringBuilder("Jugadores en la sala:\n");
        for (String jugador : jugadores) {
            jugadoresTexto.append(jugador).append("\n");
        }
        jugadoresTextView.setText(jugadoresTexto.toString());

        // Crear la instancia del Cliente y conectarse al servidor
        cliente = new Cliente();
        new Thread(() -> cliente.conectarServidor()).start(); // Se usa la versión sin argumento

        iniciarButton.setOnClickListener(v -> {
            Intent intent = new Intent(TurnoActivity.this, CasillaActivity.class);
            startActivity(intent);
        });
    }

    public void actualizarUI(String mensaje) {
        uiHandler.post(() -> jugadoresTextView.setText(mensaje));
    }
}
