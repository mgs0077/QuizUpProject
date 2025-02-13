package com.example.quizupproject;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.quizupproject.client.Cliente;

public class UnirseSalaActivity extends AppCompatActivity {

    private EditText codigoSalaEditText;
    private ImageView botonUnirse;
    private Cliente cliente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.unirse_sala);

        codigoSalaEditText = findViewById(R.id.editText2);
        botonUnirse = findViewById(R.id.boton_unirse_sala);
        cliente = new Cliente();

        // Recuperar el código guardado en SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("SalaPreferences", MODE_PRIVATE);
        String codigoGuardado = sharedPreferences.getString("codigo_sala", "");
        System.out.println("Código guardado en SharedPreferences: " + codigoGuardado);

        // Si el código guardado no está vacío, lo colocamos en el campo de texto
        if (!codigoGuardado.isEmpty()) {
            codigoSalaEditText.setText(codigoGuardado);
        }

        botonUnirse.setOnClickListener(v -> {
            String codigoSala = codigoSalaEditText.getText().toString().trim();
            System.out.println("Código ingresado por el usuario: [" + codigoSala + "]");

            if (!codigoSala.isEmpty()) {
                // Guardar el código de la sala en SharedPreferences para uso futuro
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("codigo_sala", codigoSala);
                editor.apply();

                cliente.unirseASala(codigoSala);
                Toast.makeText(UnirseSalaActivity.this, "Intentando unirse a la sala: " + codigoSala, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(UnirseSalaActivity.this, "Por favor, ingresa el código de la sala", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
