package com.example.am_acn4av_modic;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class FraseAdapter extends RecyclerView.Adapter<FraseAdapter.FraseViewHolder> {
    private List<Frase> frasesOriginales;
    private List<Frase> frasesFiltradas;
    private Context context;

    public FraseAdapter(List<Frase> listaFrases, Context context) {
        this.frasesOriginales = listaFrases;
        this.frasesFiltradas = new ArrayList<>(listaFrases);
        this.context = context;
    }

    @NonNull
    @Override
    public FraseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_frase, parent, false);
        return new FraseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FraseViewHolder holder, int position) {
        Frase frase = frasesFiltradas.get(position);
        holder.textoFrase.setText(frase.getFrase());
        holder.textoOrigen.setText("Origen: " + frase.getOrigen());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetalleActivity.class);
            intent.putExtra("fraseSeleccionada", frase.getFrase()); // O podés pasar el idFrase
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
            texto = texto.toLowerCase();
            for (Frase f : frasesOriginales) {
                if (f.getFrase().toLowerCase().contains(texto)) {
                    frasesFiltradas.add(f);
                }
            }
        }
        notifyDataSetChanged();
    }

    static class FraseViewHolder extends RecyclerView.ViewHolder {
        TextView textoFrase, textoOrigen;

        public FraseViewHolder(@NonNull View itemView) {
            super(itemView);
            textoFrase = itemView.findViewById(R.id.textoFrase);
            textoOrigen = itemView.findViewById(R.id.textoOrigen);
        }
    }
}