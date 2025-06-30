package com.example.am_acn4av_modic;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        this.mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = this.mAuth.getCurrentUser();
        if (user != null){
            String email = user.getEmail();
            Log.i("firebase-auth", "Usuario logueado con el email " + email);

            //Si ya estoy logueado paso a ESTA pantalla directamente, deberia de poner el main aca y si no esta logueado que lleve al login
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("userEmail", email);
            startActivity(intent);
            finish();
        }

        TextView textViewRegistro = findViewById(R.id.text_view_registro);
        textViewRegistro.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        ImageView imageView = findViewById(R.id.imageView);
        String imageUrl = "https://i.imgur.com/aJC3rc1.png";

        Glide.with(this)
                .load(imageUrl)
                .into(imageView);

        Button btnIngresar = findViewById(R.id.btnIngresar);
        btnIngresar.setOnClickListener(v -> {

            EditText editTextMail = findViewById(R.id.ingresoMail);
            EditText editTextPass = findViewById(R.id.ingresoPass);
            String email = editTextMail.getText().toString().trim();
            String password = editTextPass.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Por favor ingrese email y contraseña", Toast.LENGTH_SHORT).show();
                return;
            }

            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(LoginActivity.this, task -> {
                        if (task.isSuccessful()) {
                            Log.i("firebase-auth", "Usuario logueado con éxito");

                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.putExtra("userEmail", email);
                            intent.putExtra("userPass", password);
                            startActivity(intent);
                            finish();
                        } else {
                            Log.i("firebase-auth", "Login incorrecto", task.getException());
                            Toast.makeText(LoginActivity.this, "Error al iniciar sesión", Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }

}
