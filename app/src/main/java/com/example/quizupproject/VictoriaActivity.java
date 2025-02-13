package com.example.quizupproject;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class VictoriaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_victoria);  // Aquí debes tener un layout llamado activity_victoria

        // Mostramos el mensaje de victoria
        TextView victoriaMessage = findViewById(R.id.victoria_message);
        victoriaMessage.setText("¡Felicidades, has ganado!");
    }
}