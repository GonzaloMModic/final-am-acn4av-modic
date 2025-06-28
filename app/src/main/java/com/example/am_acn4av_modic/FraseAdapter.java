package com.example.am_acn4av_modic;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

public class FraseAdapter extends RecyclerView.Adapter<FraseAdapter.FraseViewHolder> {

    private List<Frase> frasesOriginales;
    private List<Frase> frasesFiltradas;
    private Context context;
    private Set<Integer> frasesFavoritas;
    private Consumer<Frase> onFavoritoAgregado;

    public FraseAdapter(List<Frase> frases, Context context, Set<Integer> frasesFavoritas, Consumer<Frase> onFavoritoAgregado) {
        this.frasesOriginales = new ArrayList<>(frases); // backup completo
        this.frasesFiltradas = new ArrayList<>(frases);  // lista que se muestra
        this.context = context;
        this.frasesFavoritas = frasesFavoritas;
        this.onFavoritoAgregado = onFavoritoAgregado;
    }

    @NonNull
    @Override
    public FraseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_frase, parent, false);
        return new FraseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FraseViewHolder holder, int position) {
        Frase frase = frasesFiltradas.get(position);
        holder.textoFrase.setText(frase.getFrase());

        // Cambiar el icono del corazón según si está en favoritos
        if (frasesFavoritas.contains(frase.getIdFrase())) {
            holder.btnFav.setImageResource(R.drawable.corazon_on);
        } else {
            holder.btnFav.setImageResource(R.drawable.corazon_off);
        }

        // Listener para el botón de favorito
        holder.btnFav.setOnClickListener(v -> {
            if (frasesFavoritas.contains(frase.getIdFrase())) {
                frasesFavoritas.remove(frase.getIdFrase());
                holder.btnFav.setImageResource(R.drawable.corazon_off);
            } else {
                frasesFavoritas.add(frase.getIdFrase());
                holder.btnFav.setImageResource(R.drawable.corazon_on);
            }
        });

        // Listener para ir a DetalleActivity
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetalleActivity.class);
            intent.putExtra("idFrase", frase.getIdFrase());
            intent.putExtra("frase", frase.getFrase());
            intent.putExtra("origen", frase.getOrigen());
            intent.putExtra("significado", frase.getSignificado());
            intent.putExtra("ejemploUso", frase.getEjemploUso());
            intent.putExtra("nivelUso", frase.getNivelUso());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return frasesFiltradas.size();
    }


    public void filtrar(String texto) {
        frasesFiltradas.clear();
        if (texto.isEmpty()) {
            frasesFiltradas.addAll(frasesOriginales);
        } else {
            String textoLower = texto.toLowerCase();
            for (Frase frase : frasesOriginales) {
                if (frase.getFrase().toLowerCase().contains(textoLower)) {
                    frasesFiltradas.add(frase);
                }
            }
        }
        notifyDataSetChanged();
    }



    static class FraseViewHolder extends RecyclerView.ViewHolder {
        TextView textoFrase;
        ImageButton btnFav;

        public FraseViewHolder(@NonNull View itemView) {
            super(itemView);
            textoFrase = itemView.findViewById(R.id.textoFrase);
            btnFav = itemView.findViewById(R.id.btnFav);
        }
    }
}