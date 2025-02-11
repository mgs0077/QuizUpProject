package com.example.quizupproject;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public abstract class PreguntaBaseActivity extends AppCompatActivity {

    protected String[] preguntas;
    protected String[][] respuestas;
    protected int[] respuestasCorrectas;
    protected int preguntaActual;
    protected int casillaActual;
    protected int retroceso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResource());

        casillaActual = getIntent().getIntExtra("CASILLA_ACTUAL", 1);
        cargarPreguntas();
        mostrarPreguntaAleatoria();
    }

    protected abstract int getLayoutResource();
    protected abstract void cargarPreguntas();

    private void mostrarPreguntaAleatoria() {
        Random random = new Random();
        preguntaActual = random.nextInt(preguntas.length);

        TextView textPregunta = findViewById(R.id.textPregunta);
        textPregunta.setText(preguntas[preguntaActual]);

        TextView respuestaA = findViewById(R.id.textView4);
        TextView respuestaB = findViewById(R.id.textView5);
        TextView respuestaC = findViewById(R.id.textView6);
        TextView respuestaD = findViewById(R.id.textView7);  // Corregir ID

        respuestaA.setText(respuestas[preguntaActual][0]);
        respuestaB.setText(respuestas[preguntaActual][1]);
        respuestaC.setText(respuestas[preguntaActual][2]);
        respuestaD.setText(respuestas[preguntaActual][3]);

        respuestaA.setOnClickListener(view -> manejarRespuesta(0));
        respuestaB.setOnClickListener(view -> manejarRespuesta(1));
        respuestaC.setOnClickListener(view -> manejarRespuesta(2));
        respuestaD.setOnClickListener(view -> manejarRespuesta(3));
    }

    private void manejarRespuesta(int seleccion) {
        // Si la respuesta es correcta, avanza una casilla, sino retrocede
        if (seleccion == respuestasCorrectas[preguntaActual]) {
            casillaActual++; // Avanza una casilla
        } else {
            casillaActual -= retroceso; // Retrocede la cantidad definida
            if (casillaActual < 1) {
                casillaActual = 1; // Asegura que la casilla no baje de 1
            }
        }

        // Envia la casilla actual a la siguiente actividad
        Intent intent = new Intent(this, CasillaActivity.class);
        intent.putExtra("CASILLA_ACTUAL", casillaActual);
        startActivity(intent);
        finish();
    }
}
