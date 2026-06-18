package com.tvtracker.model;

import java.util.ArrayList;
import java.util.List;

/**
 * MODELO — representa o perfil do usuário.
 *
 * Armazena o nome do usuário e suas três listas de séries:
 * - Favoritos
 * - Assistidas
 * - Para Assistir
 */
public class UserProfile {

    // Nome ou apelido do usuário (definido na primeira execução)
    private String name;

    // Lista de séries marcadas como favoritas
    private List<Series> favorites;

    // Lista de séries já assistidas
    private List<Series> watched;

    // Lista de séries que o usuário quer assistir futuramente
    private List<Series> wantToWatch;

    /**
     * Construtor padrão — inicializa todas as listas vazias.
     */
    public UserProfile() {
        this.name = "";
        this.favorites = new ArrayList<>();
        this.watched = new ArrayList<>();
        this.wantToWatch = new ArrayList<>();
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    /**
     * Os getters abaixo verificam se a lista é nula antes de retornar.
     * Isso é necessário porque o Gson pode deixar campos nulos
     * ao carregar um JSON que não possui aquele campo.
     */
    public List<Series> getFavorites() {
        if (favorites == null) favorites = new ArrayList<>();
        return favorites;
    }

    public List<Series> getWatched() {
        if (watched == null) watched = new ArrayList<>();
        return watched;
    }

    public List<Series> getWantToWatch() {
        if (wantToWatch == null) wantToWatch = new ArrayList<>();
        return wantToWatch;
    }

    // ===================== MÉTODOS DE MANIPULAÇÃO DAS LISTAS =====================

    /**
     * Adiciona uma série aos favoritos, apenas se ainda não estiver lá.
     * O método contains() usa o equals() da classe Series (que compara por ID).
     */
    public void addToFavorites(Series s) {
        if (!getFavorites().contains(s)) getFavorites().add(s);
    }

    public void removeFromFavorites(Series s) {
        getFavorites().remove(s); // remove() também usa o equals() por ID
    }

    public void addToWatched(Series s) {
        if (!getWatched().contains(s)) getWatched().add(s);
    }

    public void removeFromWatched(Series s) {
        getWatched().remove(s);
    }

    public void addToWantToWatch(Series s) {
        if (!getWantToWatch().contains(s)) getWantToWatch().add(s);
    }

    public void removeFromWantToWatch(Series s) {
        getWantToWatch().remove(s);
    }

    // ===================== VERIFICAÇÕES =====================

    // Verifica se a série já está em cada lista (usado para evitar duplicatas)
    public boolean isInFavorites(Series s) { return getFavorites().contains(s); }
    public boolean isInWatched(Series s) { return getWatched().contains(s); }
    public boolean isInWantToWatch(Series s) { return getWantToWatch().contains(s); }
}
