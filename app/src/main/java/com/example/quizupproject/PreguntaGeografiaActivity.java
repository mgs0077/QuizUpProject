package com.example.quizupproject;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class PreguntaGeografiaActivity extends PreguntaBaseActivity {

    private TextView textPregunta, respuestaA, respuestaB, respuestaC, respuestaD;

    @Override
    protected int getLayoutResource() {
        return R.layout.pregunta_geografia;  // El layout correspondiente para las preguntas de geografía
    }

    @Override
    protected void cargarPreguntas() {
        // Definir las preguntas, respuestas y la respuesta correcta
        preguntas = new String[] {
                "¿Cuál es la capital de Francia?",
                "¿En qué continente se encuentra el desierto del Sahara?",
                "¿Qué océano está al oeste de África?",
                "¿Cuál es el país más grande del mundo?"
        };

        respuestas = new String[][] {
                {"París", "Londres", "Madrid", "Roma"},
                {"África", "Asia", "Europa", "Oceanía"},
                {"Océano Pacífico", "Océano Atlántico", "Océano Índico", "Océano Ártico"},
                {"China", "Rusia", "Estados Unidos", "Canadá"}
        };

        respuestasCorrectas = new int[] {0, 0, 1, 1};  // Respuestas correctas: París, África, Océano Atlántico, Rusia
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inicializar los elementos de la UI
        textPregunta = findViewById(R.id.textPregunta);  // ID para la pregunta
        respuestaA = findViewById(R.id.textView4);  // ID para respuesta A
        respuestaB = findViewById(R.id.textView5);  // ID para respuesta B
        respuestaC = findViewById(R.id.textView6);  // ID para respuesta C
        respuestaD = findViewById(R.id.textView7);  // ID para respuesta D

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
        int respuestaCorrecta = respuestasCorrectas[preguntaActual];  // Usar la pregunta actual para obtener la respuesta correcta
        if (respuestaSeleccionada == respuestaCorrecta) {
            // Respuesta correcta
            Toast.makeText(this, "¡Correcto!", Toast.LENGTH_SHORT).show();
            casillaActual++;  // Avanza en la casilla
        } else {
            // Respuesta incorrecta
            Toast.makeText(this, "Respuesta incorrecta", Toast.LENGTH_SHORT).show();
            casillaActual -= retroceso;  // Retrocede según la lógica definida
            if (casillaActual < 1) {
                casillaActual = 1;  // Asegura que la casilla no baje de 1
            }
        }

        // Pasar a la siguiente actividad con la casilla actualizada
        Intent intent = new Intent(this, CasillaActivity.class);
        intent.putExtra("CASILLA_ACTUAL", casillaActual);
        startActivity(intent);
        finish();
    }
}
