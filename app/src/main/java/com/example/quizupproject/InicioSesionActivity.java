package com.example.quizupproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class InicioSesionActivity extends Activity {

    private EditText emailInput, passwordInput;
    private ImageView loginButton;
    private TextView registerButton;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inicio_sesion); // Asegúrate de que este archivo XML existe

        // Inicializar Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Referencias a los elementos del layout
        emailInput = findViewById(R.id.introducir_correo);
        passwordInput = findViewById(R.id.introducircontrasena);
        loginButton = findViewById(R.id.botoniniciarsesion); // Botón de inicio de sesión
        registerButton = findViewById(R.id.botonregistrarse); // Texto de "Registrarse"

        // Verificar si ya hay una sesión iniciada
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            goToCrearUnirseActivity();
        }

        // Botón para iniciar sesión
        loginButton.setOnClickListener(view -> loginUser());

        // Botón para ir a la pantalla de registro
        registerButton.setOnClickListener(view -> {
            Intent intent = new Intent(InicioSesionActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }

    // Método para iniciar sesión con Firebase Auth
    private void loginUser() {
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        // Validar que los campos no estén vacíos
        if (TextUtils.isEmpty(email)) {
            emailInput.setError("Introduce tu correo");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            passwordInput.setError("Introduce tu contraseña");
            return;
        }

        // Iniciar sesión con Firebase
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(InicioSesionActivity.this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show();
                        goToCrearUnirseActivity(); // Redirigir al usuario
                    } else {
                        Toast.makeText(InicioSesionActivity.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    // Método para redirigir al usuario después de iniciar sesión
    private void goToCrearUnirseActivity() {
        Intent intent = new Intent(InicioSesionActivity.this, CrearUnirseActivity.class);
        startActivity(intent);
        finish(); // Cierra esta actividad para evitar volver atrás con el botón de retroceso
    }
}
