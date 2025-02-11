package com.example.quizupproject;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class PreguntaAngelActivity extends PreguntaBaseActivity {

    private TextView textPregunta, respuestaA, respuestaB, respuestaC, respuestaD;

    @Override
    protected int getLayoutResource() {
        return R.layout.pregunta_angel;  // El layout correspondiente para las preguntas (puedes mantener este o crear uno específico)
    }

    @Override
    protected void cargarPreguntas() {
        // Preguntas fáciles para la categoría Ángel
        preguntas = new String[] {
                "¿Cuál es el color de un ángel típico en las películas?",
                "¿Cómo se llama el lugar donde viven los ángeles?",
                "¿Quién suele ser el líder de los ángeles?",
                "¿Qué instrumento musical se asocia comúnmente con los ángeles?"
        };

        respuestas = new String[][] {
                {"Blanco", "Negro", "Rojo", "Azul"},
                {"Cielo", "Infierno", "Tierra", "Mar"},
                {"Arcángel Miguel", "Lucifer", "San Pedro", "Santa Teresa"},
                {"Guitarra", "Piano", "Arpa", "Violín"}
        };

        respuestasCorrectas = new int[] {0, 0, 0, 2};  // Respuestas correctas: Blanco, Cielo, Arcángel Miguel, Arpa
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
            casillaActual -= 2;  // Retrocede 2 casillas (ajuste realizado aquí)
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
