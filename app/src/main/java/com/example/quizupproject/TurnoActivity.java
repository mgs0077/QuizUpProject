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

import java.util.ArrayList;
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

        // Inicializamos la lista de jugadores
        jugadores = new ArrayList<>();

        // Crear la instancia del Cliente y conectarse al servidor (aquí se pasaría el código de la sala)
        cliente = new Cliente();

        // Suponiendo que el código de la sala se pasa como extra en el Intent
        String codigoSala = getIntent().getStringExtra("CODIGO_SALA");
        if (codigoSala != null && !codigoSala.isEmpty()) {
            new Thread(() -> cliente.conectarServidor(codigoSala)).start();  // Pasar el código de la sala al cliente
        }

        // Escuchando los cambios en los jugadores
        cliente.setOnJugadoresUpdateListener(this::actualizarListaJugadores);

        iniciarButton.setOnClickListener(v -> {
            Intent intent = new Intent(TurnoActivity.this, CasillaActivity.class);
            startActivity(intent);
        });
    }

    // Este método será llamado para actualizar la lista de jugadores
    public void actualizarListaJugadores(List<String> nuevosJugadores) {
        jugadores.clear();
        jugadores.addAll(nuevosJugadores);

        // Actualizar la UI con la nueva lista de jugadores
        StringBuilder jugadoresTexto = new StringBuilder("Jugadores en la sala:\n");
        for (String jugador : jugadores) {
            jugadoresTexto.append(jugador).append("\n");
        }

        // Usamos el Handler para actualizar la UI desde el hilo principal
        uiHandler.post(() -> jugadoresTextView.setText(jugadoresTexto.toString()));
    }
}