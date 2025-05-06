package com.example.am_acn4av_modic;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private LinearLayout linearLayoutFrases;
    private LinearLayout layoutFrasesFavoritas;
    private TextView tituloFrases;
    private LinearLayout layoutFraseDelDia;

    private final List<String> frases = new ArrayList<>();
    protected List<String> frasesFavoritas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tituloFrases = findViewById(R.id.tituloFrases);
        layoutFrasesFavoritas = findViewById(R.id.frasesFavoritas);
        linearLayoutFrases = findViewById(R.id.frasesContainer);
        LinearLayout layoutFraseDelDia = findViewById(R.id.fraseDelDia);

        ImageButton btnInicio = findViewById(R.id.btnInicio);
        ImageButton btnFavoritos = findViewById(R.id.btnFavoritos);
        ImageButton btnFraseDia = findViewById(R.id.btnFraseDia);

        ScrollView scrollFavoritos = findViewById(R.id.scrollFavoritos);

        btnInicio.setOnClickListener(v -> {
            tituloFrases.setText("Frases");
            linearLayoutFrases.setVisibility(View.VISIBLE);
            scrollFavoritos.setVisibility(View.GONE);
            layoutFraseDelDia.setVisibility(View.GONE);

            btnInicio.setImageResource(R.drawable.mate_on);
            btnFavoritos.setImageResource(R.drawable.corazon_off);
            btnFraseDia.setImageResource(R.drawable.pregunta);
        });

        btnFavoritos.setOnClickListener(v -> {
            tituloFrases.setText("Favoritos");
            linearLayoutFrases.setVisibility(View.GONE);
            scrollFavoritos.setVisibility(View.VISIBLE);
            layoutFraseDelDia.setVisibility(View.GONE);

            btnInicio.setImageResource(R.drawable.mate_off);
            btnFavoritos.setImageResource(R.drawable.corazon_on);
            btnFraseDia.setImageResource(R.drawable.pregunta);
        });

        btnFraseDia.setOnClickListener(v -> {
            tituloFrases.setText("Frase del día");
            linearLayoutFrases.setVisibility(View.GONE);
            scrollFavoritos.setVisibility(View.GONE);
            layoutFraseDelDia.setVisibility(View.VISIBLE);

            btnInicio.setImageResource(R.drawable.mate_off);
            btnFavoritos.setImageResource(R.drawable.corazon_off);
            btnFraseDia.setImageResource(R.drawable.pregunta);
            mostrarFraseDelDia();
        });

        cargarFrases();
        mostrarFrases();
    }
    private void cargarFrases() {
        frases.add("A esta altura del partido");
        frases.add("Los de afuera son de palo");
        frases.add("Me quieren vender gato por liebre");
        frases.add("Del año del ñaupa");
        frases.add("En Pampa y la vía");
        frases.add("A caballo regalado no se le miran los dientes");
        frases.add("Cocodrilo que duerme se lo lleva la corriente");
        frases.add("Más vale pájaro en mano que cien volando");
        frases.add("Ser de fierro");
        frases.add("Ser Gardel");
        frases.add("Pegar un tubazo");
        frases.add("Ser de madera");
        frases.add("De queruza");
        frases.add("Se le saltó la térmica");
        frases.add("Hacer la gamba");
        frases.add("Estar al horno");
        frases.add("Saltar la ficha");
        frases.add("Ser un chanta");
        frases.add("Mandar fruta");
        frases.add("Me tiro a la pileta");
        frases.add("Acá el que no corre vuela");
        frases.add("Buscarle la quinta pata al gato");
        frases.add("No hay tu tía");

        Log.d("Frases", "Total frases cargadas: " + frases.size());


    }

    private void mostrarFrases() {
        for (String frase : frases) {
            Log.d("DEBUG", "Agregando frase: " + frase);
            LinearLayout contenedor = new LinearLayout(this);
            contenedor.setOrientation(LinearLayout.HORIZONTAL);
            contenedor.setPadding(0, 10, 0, 10);

            TextView textView = new TextView(this);
            textView.setText(frase);
            textView.setTextSize(18);
            textView.setTextColor(Color.BLACK);
            textView.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));

            ImageButton btnFav = new ImageButton(this);
            btnFav.setImageResource(R.drawable.corazon_off);
            btnFav.setBackgroundColor(Color.TRANSPARENT);
            btnFav.setAdjustViewBounds(true);
            btnFav.setMaxWidth(100);
            btnFav.setMaxHeight(100);

            btnFav.setOnClickListener(v -> {
                if (!frasesFavoritas.contains(frase)) {
                    frasesFavoritas.add(frase);
                    btnFav.setImageResource(R.drawable.corazon_on);
                    agregarAFavoritos(frase);
                }
            });

            contenedor.addView(textView);
            contenedor.addView(btnFav);

            linearLayoutFrases.addView(contenedor);
        }

    }

    private void mostrarFraseDelDia() {
        if (!frases.isEmpty()) {
            Random random = new Random();
            String fraseAleatoria = frases.get(random.nextInt(frases.size()));

            TextView fraseDelDiaText = findViewById(R.id.fraseDelDiaText);
            fraseDelDiaText.setText(fraseAleatoria);
        }
    }

    private void agregarAFavoritos(String frase) {
        TextView textoFavorito = new TextView(this);
        textoFavorito.setText(frase);
        textoFavorito.setTextSize(18);
        textoFavorito.setTextColor(Color.BLACK);
        textoFavorito.setPadding(80, 10, 80, 10);
        textoFavorito.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        layoutFrasesFavoritas.addView(textoFavorito);
    }
}