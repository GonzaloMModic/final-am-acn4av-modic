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

import com.bumptech.glide.Glide;

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
            finish();
        });

        TextView textFrase = findViewById(R.id.textoFrase);
        String frase = getIntent().getStringExtra("frase");

        TextView textOrigen = findViewById(R.id.textoOrigen);
        String origen = getIntent().getStringExtra("origen");

        TextView textSignificado = findViewById(R.id.textoSignificado);
        String significado = getIntent().getStringExtra("significado");

        TextView textEjemploUso = findViewById(R.id.textoEjemploUso);
        String ejemploUso = getIntent().getStringExtra("ejemploUso");


        String[] urlsMates ={
                "https://i.imgur.com/vUOlJOS.png",  //mates0
                "https://i.imgur.com/bAV91Ij.png",  //mates1
                "https://i.imgur.com/PETj5pW.png",  //mates2
                "https://i.imgur.com/hZSMb6y.png",  //mates3
                "https://i.imgur.com/AXSl6fJ.png",  //mates4
                "https://i.imgur.com/NS0z8en.png"   //mates5
        };

        ImageView imagenNivel = findViewById(R.id.imagenMatecitos);
        int nivelUso = getIntent().getIntExtra("nivelUso", 0);

        if (nivelUso >= 0 && nivelUso < urlsMates.length) {
            Glide.with(this).load(urlsMates[nivelUso]).into(imagenNivel);
        } else {
            imagenNivel.setImageResource(R.drawable.corazon_on);
        }

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
    }
}