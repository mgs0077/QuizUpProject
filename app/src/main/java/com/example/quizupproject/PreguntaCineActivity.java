package com.example.quizupproject;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class PreguntaCineActivity extends PreguntaBaseActivity {

    private TextView textPregunta, respuestaA, respuestaB, respuestaC, respuestaD;

    @Override
    protected int getLayoutResource() {
        return R.layout.pregunta_cine;
    }

    @Override
    protected void cargarPreguntas() {
        // Definir las preguntas, respuestas y la correcta
        preguntas = new String[] {
                "¿Quién dirigió 'Titanic'?",
                "¿Qué película ganó el Oscar a Mejor Película en 2020?"
        };

        respuestas = new String[][] {
                {"James Cameron", "Steven Spielberg", "Martin Scorsese", "Christopher Nolan"},
                {"Parasite", "1917", "Once Upon a Time in Hollywood", "The Irishman"}
        };

        respuestasCorrectas = new int[] {0, 0};  // Respuestas correctas: Titanic -> James Cameron, 2020 -> Parasite
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inicializar los elementos de la UI
        textPregunta = findViewById(R.id.textPregunta);  // ID para la pregunta
        respuestaA = findViewById(R.id.textView3);  // ID para respuesta A
        respuestaB = findViewById(R.id.textView4);  // ID para respuesta B
        respuestaC = findViewById(R.id.textView5);  // ID para respuesta C
        respuestaD = findViewById(R.id.textView6);  // ID para respuesta D

        // Configurar la primera pregunta y las respuestas
        mostrarPreguntaYRespuestas(0);

        // Definir los eventos de clic para las respuestas
        respuestaA.setOnClickListener(v -> verificarRespuesta(0));
        respuestaB.setOnClickListener(v -> verificarRespuesta(1));
        respuestaC.setOnClickListener(v -> verificarRespuesta(2));
        respuestaD.setOnClickListener(v -> verificarRespuesta(3));
    }

    // Método para mostrar la pregunta y las respuestas
    private void mostrarPreguntaYRespuestas(int indexPregunta) {
        textPregunta.setText(preguntas[indexPregunta]);
        respuestaA.setText(respuestas[indexPregunta][0]);
        respuestaB.setText(respuestas[indexPregunta][1]);
        respuestaC.setText(respuestas[indexPregunta][2]);
        respuestaD.setText(respuestas[indexPregunta][3]);
    }

    // Verificar si la respuesta seleccionada es correcta
    private void verificarRespuesta(int respuestaSeleccionada) {
        int respuestaCorrecta = respuestasCorrectas[0];  // Aquí deberíamos adaptarlo según la pregunta actual
        if (respuestaSeleccionada == respuestaCorrecta) {
            Toast.makeText(this, "¡Correcto!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Respuesta incorrecta", Toast.LENGTH_SHORT).show();
        }

        // Pasar a la siguiente actividad con la casilla actualizada
        Intent intent = new Intent(this, CasillaActivity.class);
        intent.putExtra("CASILLA_ACTUAL", casillaActual);
        startActivity(intent);
        finish();
    }
}