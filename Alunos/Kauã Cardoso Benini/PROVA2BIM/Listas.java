package com.tvtracker.model;

/**
 * ENUM — define os tipos de lista disponíveis no sistema.
 */
public enum Listas {

    FAVORITES("Favoritos"),       // Lista de séries favoritas
    WATCHED("Assistidas"),        // Lista de séries já assistidas
    WANT_TO_WATCH("Para Assistir"); // Lista de séries para assistir futuramente

    // Nome de exibição em português (usado nos botões e mensagens da interface)
    private final String displayName;

    // Construtor do enum — associa o nome de exibição a cada constante
    Listas(String displayName) {
        this.displayName = displayName;
    }

    // Retorna o nome legível para o usuário (ex: "Favoritos")
    public String getDisplayName() {
        return displayName;
    }
}
