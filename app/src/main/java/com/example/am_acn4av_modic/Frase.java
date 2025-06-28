package com.example.am_acn4av_modic;

public class Frase {
    private int idFrase;
    private String frase;
    private String origen;
    private String significado;
    private String ejemploUso;
    private int nivelUso;
    private boolean esFavorita;

    public Frase() {
        // Constructor vacío requerido por Firestore
    }

    public Frase(int idFrase, String frase, String origen, String significado, String ejemploUso, int nivelUso) {
        this.idFrase = idFrase;
        this.frase = frase;
        this.origen = origen;
        this.significado = significado;
        this.ejemploUso = ejemploUso;
        this.nivelUso = nivelUso;
        this.esFavorita = false;
    }

    // Getters
    public int getIdFrase() { return idFrase; }
    public String getFrase() { return frase; }
    public String getOrigen() { return origen; }
    public String getSignificado() { return significado; }
    public String getEjemploUso() { return ejemploUso; }
    public int getNivelUso() { return nivelUso; }
    public boolean isEsFavorita() { return esFavorita; }

    // Setters
    public void setIdFrase(int idFrase) { this.idFrase = idFrase; }
    public void setFrase(String frase) { this.frase = frase; }
    public void setOrigen(String origen) { this.origen = origen; }
    public void setSignificado(String significado) { this.significado = significado; }
    public void setEjemploUso(String ejemploUso) { this.ejemploUso = ejemploUso; }
    public void setNivelUso(int nivelUso) { this.nivelUso = nivelUso; }
    public void setEsFavorita(boolean esFavorita) { this.esFavorita = esFavorita; }
}