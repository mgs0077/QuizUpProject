package com.example.quizupproject;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class CrearUnirseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crear_unirse);

        // Referencia al botón "Crear Sala"
        ImageView botonCrear = findViewById(R.id.botoncrear); // Asegúrate de que el ID es correcto

        // Establecer el listener para el clic del botón
        botonCrear.setOnClickListener(v -> {
            // Redirigir a la actividad CrearSalaActivity
            Intent intent = new Intent(CrearUnirseActivity.this, CrearSalaActivity.class);
            startActivity(intent);
        });
    }
}
