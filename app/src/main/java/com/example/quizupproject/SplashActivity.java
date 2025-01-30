package com.example.quizupproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pantalla_carga);

        // Cargar animación de deslizamiento
        Animation slideOutLeft = AnimationUtils.loadAnimation(this, R.anim.slide_out_left);

        // Referencias a la imagen y el texto
        ImageView logo = findViewById(R.id.logo);
        TextView title = findViewById(R.id.quizup);

        // Aplicar animación a los elementos
        logo.startAnimation(slideOutLeft);
        title.startAnimation(slideOutLeft);

        // Esperar 2 segundos y abrir la pantalla de inicio de sesión
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(SplashActivity.this, InicioSesionActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            finish(); // Cierra esta actividad para que no se pueda regresar con el botón "Atrás"
        }, 2000);
    }
}
