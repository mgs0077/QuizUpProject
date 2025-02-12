package com.example.quizupproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class CrearUnirseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crear_unirse);

        // Referencia al botón "Crear Sala"
        ImageView botonCrear = findViewById(R.id.botoncrear); // Verifica que este ID exista en XML

        if (botonCrear != null) {
            botonCrear.setOnClickListener(v -> {
                try {
                    // Redirigir a la actividad CrearSalaActivity
                    Intent intent = new Intent(CrearUnirseActivity.this, CrearSalaActivity.class);
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("CrearUnirseActivity", "Error al iniciar CrearSalaActivity: " + e.getMessage());
                }
            });
        } else {
            Log.e("CrearUnirseActivity", "Error: botonCrear es null. Verifica el ID en el XML.");
        }
    }
}
