package model;

import java.util.ArrayList;

public class Usuario {

    private String nome;

    private ArrayList<Serie> favoritos;
    private ArrayList<Serie> assistidas;
    private ArrayList<Serie> desejaAssistir;

    public Usuario() {

        favoritos = new ArrayList<>();
        assistidas = new ArrayList<>();
        desejaAssistir = new ArrayList<>();
    }

    public Usuario(String nome) {

        this();

        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public ArrayList<Serie> getFavoritos() {
        return favoritos;
    }

    public ArrayList<Serie> getAssistidas() {
        return assistidas;
    }

    public ArrayList<Serie> getDesejaAssistir() {
        return desejaAssistir;
    }
}