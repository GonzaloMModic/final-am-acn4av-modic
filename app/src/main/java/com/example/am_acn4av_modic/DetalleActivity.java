package com.example.am_acn4av_modic;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class DetalleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detalle);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button btnVolver = findViewById(R.id.btnVolver);
        btnVolver.setOnClickListener(v -> {
            finish(); // Vuelve al LoginActivity
        });

        TextView textFrase = findViewById(R.id.textoFrase);
        String frase = getIntent().getStringExtra("frase");

        TextView textOrigen = findViewById(R.id.textoOrigen);
        String origen = getIntent().getStringExtra("origen");

        TextView textSignificado = findViewById(R.id.textoSignificado);
        String significado = getIntent().getStringExtra("significado");

        TextView textEjemploUso = findViewById(R.id.textoEjemploUso);
        String ejemploUso = getIntent().getStringExtra("frase");

        ImageView imagenNivel = findViewById(R.id.imagenMatecitos);
        int nivelUso = getIntent().getIntExtra("nivelUso", 0);
        String nombreImagen = "mates" + nivelUso;
        int idImagen = getResources().getIdentifier(nombreImagen, "drawable", getPackageName());

        if (frase != null) {
            textFrase.setText(frase);
        } else {
            textFrase.setText("No se recibió frase");
        }

        if (origen != null) {
            textOrigen.setText(origen);
        } else {
            textOrigen.setText("No se recibió el origen");
        }

        if (significado != null) {
            textSignificado.setText(significado);
        } else {
            textSignificado.setText("No se recibió el significado");
        }

        if (ejemploUso != null) {
            textEjemploUso.setText(ejemploUso);
        } else {
            textEjemploUso.setText("No se recibió el ejemplo de uso");
        }

        if (idImagen != 0) {
            imagenNivel.setImageResource(idImagen);
        } else {
            imagenNivel.setImageResource(R.drawable.mates0); // imagen por defecto si no encuentra la otra
        }
    }
}