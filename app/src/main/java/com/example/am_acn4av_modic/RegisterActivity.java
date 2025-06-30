package com.example.am_acn4av_modic;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registro_activity);

        mAuth = FirebaseAuth.getInstance();

        EditText editTextMail = findViewById(R.id.ingresoMail);
        EditText editTextPass = findViewById(R.id.ingresoPass);
        Button btnRegistrar = findViewById(R.id.btnRegistrar);

        Button btnVolver = findViewById(R.id.btnVolver);
        btnVolver.setOnClickListener(v -> {
            finish(); // Vuelve al LoginActivity
        });

        ImageView imageView = findViewById(R.id.imageView);
        String imageUrl = "https://i.imgur.com/aJC3rc1.png";

        Glide.with(this)
                .load(imageUrl)
                .into(imageView);

        btnRegistrar.setOnClickListener(v -> {
            String email = editTextMail.getText().toString().trim();
            String password = editTextPass.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(RegisterActivity.this, "Por favor ingrese email y contraseña", Toast.LENGTH_SHORT).show();
                return;
            }

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Log.i("firebase-auth", "Usuario registrado con éxito");
                            FirebaseAuth.getInstance().signOut();

                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        } else {
                            Log.i("firebase-auth", "Error en el registro", task.getException());
                            Toast.makeText(RegisterActivity.this, "Error al registrarse: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }
}