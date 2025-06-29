package com.example.am_acn4av_modic;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AgregarFraseActivity extends AppCompatActivity {

    private EditText textoFrase, textoOrigen, textoSignificado, textoEjemploUso, nivelUsoNumber;
    private Button btnAgregar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_agregar_frase);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button btnVolver = findViewById(R.id.btnVolver);
        btnVolver.setOnClickListener(v -> {
            finish();
        });

        // Vincular con layout
        textoFrase = findViewById(R.id.textoFrase);
        textoOrigen = findViewById(R.id.textoOrigen);
        textoSignificado = findViewById(R.id.textoSignificado);
        textoEjemploUso = findViewById(R.id.textoEjemploUso);
        nivelUsoNumber = findViewById(R.id.nivelUsoNumber);
        btnAgregar = findViewById(R.id.btnAgregarFrase);
        btnVolver = findViewById(R.id.btnVolver);

        btnAgregar.setOnClickListener(v -> agregarFrase());
        btnVolver.setOnClickListener(v -> finish()); // Vuelve a la pantalla anterior
    }

    private void agregarFrase() {
        String frase = textoFrase.getText().toString().trim();
        String origen = textoOrigen.getText().toString().trim();
        String significado = textoSignificado.getText().toString().trim();
        String ejemplo = textoEjemploUso.getText().toString().trim();
        String nivelStr = nivelUsoNumber.getText().toString().trim();

        if (frase.isEmpty() || origen.isEmpty() || significado.isEmpty() || ejemplo.isEmpty() || nivelStr.isEmpty()) {
            Toast.makeText(this, "Completá todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        int nivelUso;
        try {
            nivelUso = Integer.parseInt(nivelStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Nivel de uso debe ser un número", Toast.LENGTH_SHORT).show();
            return;
        }

        if (nivelUso < 0 || nivelUso > 5) {
            Toast.makeText(this, "El nivel de uso debe estar entre 0 y 5", Toast.LENGTH_SHORT).show();
            return;
        }

        // Generar nuevo ID automáticamente
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Obtener el ID más alto actual para sumar uno
        db.collection("frases")
                .orderBy("idFrase", com.google.firebase.firestore.Query.Direction.DESCENDING)
                .limit(1)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    int nuevoId = 1;
                    if (!queryDocumentSnapshots.isEmpty()) {
                        int ultimoId = queryDocumentSnapshots.getDocuments().get(0).getLong("idFrase").intValue();
                        nuevoId = ultimoId + 1;
                    }

                    Map<String, Object> nuevaFrase = new HashMap<>();
                    nuevaFrase.put("idFrase", nuevoId);
                    nuevaFrase.put("frase", frase);
                    nuevaFrase.put("origen", origen);
                    nuevaFrase.put("significado", significado);
                    nuevaFrase.put("ejemploUso", ejemplo);
                    nuevaFrase.put("nivelUso", nivelUso);

                    db.collection("frases")
                            .add(nuevaFrase)
                            .addOnSuccessListener(documentReference -> {
                                Toast.makeText(this, "Frase agregada correctamente", Toast.LENGTH_SHORT).show();
                                finish(); // Vuelve al MainActivity
                            })
                            .addOnFailureListener(e -> {
                                Log.e("Firebase", "Error al agregar", e);
                                Toast.makeText(this, "Error al agregar frase", Toast.LENGTH_SHORT).show();
                            });

                });
    }
}