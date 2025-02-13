package com.example.quizupproject;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    private EditText emailInput, passwordInput;
    private ImageView registerButton;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registro);

        // Inicializar Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Referencias a los elementos del layout
        emailInput = findViewById(R.id.introducir_correo);
        passwordInput = findViewById(R.id.editText2);
        registerButton = findViewById(R.id.crearcuenta);

        // Ocultar contraseña
        passwordInput.setTransformationMethod(PasswordTransformationMethod.getInstance());

        // Botón para registrar usuario
        registerButton.setOnClickListener(view -> registerUser());
    }

    private void registerUser() {
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        // Validaciones
        if (TextUtils.isEmpty(email)) {
            emailInput.setError("Introduce tu correo");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            passwordInput.setError("Introduce tu contraseña");
            return;
        }

        if (password.length() < 6) {
            passwordInput.setError("La contraseña debe tener al menos 6 caracteres");
            return;
        }

        // Crear usuario en Firebase
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(RegisterActivity.this, "Registro exitoso", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(RegisterActivity.this, InicioSesionActivity.class));
                        finish();
                    } else {
                        Toast.makeText(RegisterActivity.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }
}
