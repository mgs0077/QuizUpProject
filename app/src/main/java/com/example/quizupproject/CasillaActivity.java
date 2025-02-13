package com.example.quizupproject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class CasillaActivity extends AppCompatActivity {

    private int casillaActual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.juego_principal);

        // Obtener casilla actual desde el intent
        casillaActual = getIntent().getIntExtra("CASILLA_ACTUAL", 1);

        // Referencias a los elementos de la UI
        ImageView ivCasillaActual = findViewById(R.id.iv_casilla_actual);
        ImageView ivCasillaAnterior = findViewById(R.id.iv_casilla_anterior);
        ImageView ivCasillaPosterior = findViewById(R.id.iv_casilla_posterior);

        // Actualizar imágenes según la casilla actual
        ivCasillaActual.setImageResource(obtenerImagenCasilla(casillaActual));

        // Configurar solo las imágenes de casillas anterior y posterior
        configurarCasillas(ivCasillaAnterior, ivCasillaPosterior);

        // Si estamos en la casilla 24, no esperar 5 segundos
        if (casillaActual == 24) {
            irAPregunta();  // Ir directamente a la actividad de victoria
        } else {
            // Esperar 5 segundos y cambiar de pantalla
            new Handler().postDelayed(this::irAPregunta, 5000);
        }
    }

    private void configurarCasillas(ImageView ivAnterior, ImageView ivPosterior) {
        // Configura la casilla anterior
        if (casillaActual > 1) {
            ivAnterior.setImageResource(obtenerImagenCasilla(casillaActual - 1));
        } else {
            ivAnterior.setImageDrawable(null); // No hay casilla anterior
        }

        // Configura la casilla posterior
        if (casillaActual < 24) {
            ivPosterior.setImageResource(obtenerImagenCasilla(casillaActual + 1));
        } else {
            ivPosterior.setImageResource(R.drawable.casilla_meta); // Casilla meta
        }
    }

    private int obtenerImagenCasilla(int casilla) {
        switch (casilla) {
            case 1:
                return R.drawable.casilla_inicio;
            case 2: case 8: case 15: case 20:
                return R.drawable.casilla_cine;
            case 3: case 9: case 16: case 21:
                return R.drawable.casilla_geografia;
            case 4: case 11: case 17: case 22:
                return R.drawable.casilla_musica;
            case 5: case 12: case 18:
                return R.drawable.casilla_historia;
            case 6: case 14:
                return R.drawable.casilla_arte;
            case 7: case 13: case 19:
                return R.drawable.casilla_angel;
            case 10: case 23:
                return R.drawable.casilla_demonio;
            case 24:
                return R.drawable.casilla_meta;
            default:
                return R.drawable.casilla_default;
        }
    }

    private void irAPregunta() {
        Class<?> siguienteActivity;

        if (casillaActual == 24) {
            siguienteActivity = VictoriaActivity.class;  // Directamente a victoria si estamos en la casilla 24
        } else {
            switch (casillaActual) {
                case 2: case 8: case 15: case 20:
                    siguienteActivity = PreguntaCineActivity.class;
                    break;
                case 3: case 9: case 16: case 21:
                    siguienteActivity = PreguntaGeografiaActivity.class;
                    break;
                case 4: case 11: case 17: case 22:
                    siguienteActivity = PreguntaMusicaActivity.class;
                    break;
                case 5: case 12: case 18:
                    siguienteActivity = PreguntaHistoriaActivity.class;
                    break;
                case 6: case 14:
                    siguienteActivity = PreguntaArteActivity.class;
                    break;
                case 7: case 13: case 19:
                    siguienteActivity = PreguntaAngelActivity.class;
                    break;
                case 10: case 23:
                    siguienteActivity = PreguntaDemonioActivity.class;
                    break;
                default:
                    siguienteActivity = CasillaActivity.class; // Regresar a CasillaActivity si no corresponde
            }
        }

        Intent intent = new Intent(this, siguienteActivity);
        intent.putExtra("CASILLA_ACTUAL", casillaActual);  // Pasar la casilla actual
        startActivity(intent);
        finish();
    }
}
