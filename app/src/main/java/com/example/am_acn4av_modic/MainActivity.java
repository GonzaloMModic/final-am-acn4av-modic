package com.example.am_acn4av_modic;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private FraseAdapter adapter;
    private ArrayList<Frase> frases = new ArrayList<>();
    private Set<Integer> frasesFavoritas = new HashSet<>();

    private RecyclerView recyclerFavoritos;
    private FraseAdapter adapterFavoritos;
    //----
    private TextView tituloFrases;
    private LinearLayout layoutFraseDelDia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        String email = getIntent().getStringExtra("userEmail");
        String password = getIntent().getStringExtra("userPass");

        Log.i("LoginData", "Email recibido: " + email);
        Log.i("LoginData", "Contraseña recibida: " + password);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);

            Window window = getWindow();
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.my_primary));

            return insets;
        });

        SearchView searchView = findViewById(R.id.barraBusqueda);
        recyclerView = findViewById(R.id.recyclerViewFrases);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerFavoritos = findViewById(R.id.recyclerFavoritos);
        recyclerFavoritos.setLayoutManager(new LinearLayoutManager(this));


        tituloFrases = findViewById(R.id.tituloFrases);

        layoutFraseDelDia = findViewById(R.id.fraseDelDia);

        ImageButton btnInicio = findViewById(R.id.btnInicio);
        ImageButton btnFavoritos = findViewById(R.id.btnFavoritos);
        ImageButton btnFraseDia = findViewById(R.id.btnFraseDia);

        btnInicio.setOnClickListener(v -> {
            tituloFrases.setText("Frases");
            recyclerView.setVisibility(View.VISIBLE);
            recyclerFavoritos.setVisibility(View.GONE);
            layoutFraseDelDia.setVisibility(View.GONE);

            btnInicio.setImageResource(R.drawable.mate_on);
            btnFavoritos.setImageResource(R.drawable.corazon_off);
            btnFraseDia.setImageResource(R.drawable.pregunta);
        });

        btnFavoritos.setOnClickListener(v -> {
            tituloFrases.setText("Favoritos");
            recyclerView.setVisibility(View.GONE);
            recyclerFavoritos.setVisibility(View.VISIBLE);
            layoutFraseDelDia.setVisibility(View.GONE);

            btnInicio.setImageResource(R.drawable.mate_off);
            btnFavoritos.setImageResource(R.drawable.corazon_on);
            btnFraseDia.setImageResource(R.drawable.pregunta);


            List<Frase> favoritas = new ArrayList<>();
            for (Frase f : frases) {
                if (frasesFavoritas.contains(f.getIdFrase())) {
                    favoritas.add(f);
                }
            }
            adapterFavoritos = new FraseAdapter(favoritas, this, frasesFavoritas, this::agregarAFavoritos);
            recyclerFavoritos.setAdapter(adapterFavoritos);

        });

        btnFraseDia.setOnClickListener(v -> {
            tituloFrases.setText("Frase aleatoria");
            recyclerView.setVisibility(View.GONE);
            recyclerFavoritos.setVisibility(View.GONE);
            layoutFraseDelDia.setVisibility(View.VISIBLE);

            btnInicio.setImageResource(R.drawable.mate_off);
            btnFavoritos.setImageResource(R.drawable.corazon_off);
            btnFraseDia.setImageResource(R.drawable.pregunta);
            mostrarFraseAleatoria();
        });

        cargarFrasesDesdeFirestore();


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Cuando el usuario presiona enter/lupa
                aplicarFiltro(query);
                adapter.filtrar(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Mientras el usuario escribe
                aplicarFiltro(newText);
                adapter.filtrar(newText);
                return false;
            }
        });

        ImageButton btnLogOut = findViewById(R.id.btnLogOut);

        btnLogOut.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Cerrar sesión")
                    .setMessage("¿Estás seguro que querés cerrar sesión?")
                    .setPositiveButton("Sí", (dialog, which) -> {
                        // Aquí se hace el logout
                        FirebaseAuth.getInstance().signOut();
                        // Volver al login
                        Intent intent = new Intent(this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    })
                    .setNegativeButton("No", (dialog, which) -> {
                        dialog.dismiss(); // simplemente cierra el diálogo
                    })
                    .show();
        });

    }

    private void aplicarFiltro(String texto) {
        if (recyclerView.getVisibility() == View.VISIBLE) {
            adapter.filtrar(texto);
        } else if (recyclerFavoritos.getVisibility() == View.VISIBLE) {
            adapterFavoritos.filtrar(texto);
        }
    }
    private void cargarFrasesDesdeFirestore() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("frases")
                .orderBy("idFrase")  // Ordena por ID numérico
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    frases.clear();
                    for (DocumentSnapshot doc : queryDocumentSnapshots) {
                        try {
                            Frase frase = doc.toObject(Frase.class);
                            if (frase != null) {
                                Log.d("FirestoreFrase", "Frase cargada: " + frase.getIdFrase() + " - " + frase.getFrase());
                                frases.add(frase);
                            }
                        } catch (Exception e) {
                            Log.e("Firestore", "Error al convertir documento: " + doc.getId(), e);
                        }
                    }
                    adapter = new FraseAdapter(frases, this, frasesFavoritas, this::agregarAFavoritos);
                    recyclerView.setAdapter(adapter);
                    adapter.filtrar("");
                    Log.d("Firestore", "Frases cargadas: " + frases.size());
                })
                .addOnFailureListener(e -> {
                    Log.e("Firestore", "Error al cargar frases", e);
                    Toast.makeText(this, "Error al cargar frases", Toast.LENGTH_SHORT).show();
                });
    }


    private void mostrarFraseAleatoria() {
        if (!frases.isEmpty()) {
            Random random = new Random();
            Frase fraseAleatoria = frases.get(random.nextInt(frases.size()));
            TextView fraseDelDiaText = findViewById(R.id.fraseDelDiaText);
            fraseDelDiaText.setText(fraseAleatoria.getFrase());
        }
    }
    private void agregarAFavoritos(Frase frase) {
        TextView textoFavorito = new TextView(this);
        textoFavorito.setText(frase.getFrase());
        textoFavorito.setTextSize(18);
        textoFavorito.setTextColor(Color.BLACK);
        textoFavorito.setPadding(80, 10, 80, 10);
        textoFavorito.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
    }
}