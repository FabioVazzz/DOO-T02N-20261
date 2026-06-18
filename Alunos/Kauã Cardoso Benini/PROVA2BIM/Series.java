package com.tvtracker.model;

import java.util.List;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Representa uma série de TV.
 *
 * Esta classe que guarda todas as informações
 * de uma série buscada na API do TVMaze.
 *
 * Cada campo corresponde a um dado da série:
 * id, nome, idioma, gêneros, nota, status, datas, emissora e descrição.
 */
public class Series {

    // Identificador único da série (vem da API TVMaze)
    private int id;

    // Nome da série (ex: "Breaking Bad")
    private String name;

    // Idioma original da série (ex: "English")
    private String language;

    // Lista de gêneros (ex: ["Drama", "Crime"])
    private List<String> genres;

    // Nota média da série (ex: 9.2). Zero significa sem avaliação.
    private double rating;

    // Estado atual da série: "Running", "Ended", etc.
    private String status;

    // Data de estreia no formato "YYYY-MM-DD" (ex: "2008-01-20")
    private String premiered;

    // Data de encerramento. Null se ainda estiver em exibição.
    private String ended;

    // Nome da emissora ou plataforma (ex: "Netflix", "HBO")
    private String networkName;

    // Sinopse/descrição da série (texto limpo, sem HTML)
    private String summary;

    // Construtor vazio necessário para o Gson conseguir criar objetos ao ler o JSON
    public Series() {}

    // Construtor completo usado ao mapear os dados da API
    public Series(int id, String name, String language, List<String> genres,
                  double rating, String status, String premiered, String ended,
                  String networkName, String summary) {
        this.id = id;
        this.name = name;
        this.language = language;
        this.genres = genres != null ? genres : new ArrayList<>(); // Evita lista nula
        this.rating = rating;
        this.status = status;
        this.premiered = premiered;
        this.ended = ended;
        this.networkName = networkName;
        this.summary = summary;
    }

    // ===================== GETTERS E SETTERS =====================
    // Métodos de acesso aos campos privados (encapsulamento)

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    // Retorna o nome ou string vazia se for nulo (evita NullPointerException)
    public String getName() { return name != null ? name : ""; }
    public void setName(String name) { this.name = name; }

    // Retorna "N/A" se o idioma não estiver disponível
    public String getLanguage() { return language != null ? language : "N/A"; }
    public void setLanguage(String language) { this.language = language; }

    public List<String> getGenres() { return genres != null ? genres : new ArrayList<>(); }
    public void setGenres(List<String> genres) { this.genres = genres; }

    /**
     * Converte a lista de gêneros em uma única string separada por vírgulas.
     * Usado para exibir na tabela. Ex: "Drama, Crime, Thriller"
     */
    public String getGenresAsString() {
        if (genres == null || genres.isEmpty()) return "N/A";
        return String.join(", ", genres);
    }

    public double getRating() { return rating; }
    public void setRating(double rating) { this.rating = rating; }

    /**
     * Formata a nota para exibição.
     * Se a nota for 0 (sem avaliação), retorna "N/A".
     * Senão, retorna com 1 casa decimal. Ex: "8.5"
     */
    public String getRatingAsString() {
        return rating > 0 ? String.format("%.1f", rating) : "N/A";
    }

    public String getStatus() { return status != null ? status : "N/A"; }
    public void setStatus(String status) { this.status = status; }

    public String getPremiered() { return premiered; }
    public void setPremiered(String premiered) { this.premiered = premiered; }

    public String getEnded() { return ended; }
    public void setEnded(String ended) { this.ended = ended; }

    public String getNetworkName() { return networkName; }
    public void setNetworkName(String networkName) { this.networkName = networkName; }

    public String getSummary() { return summary; }
    public void setSummary(String summary) { this.summary = summary; }

    // ===================== EQUALS E HASHCODE =====================
    /**
     * Duas séries são iguais se tiverem o mesmo ID.
     * Isso é fundamental para o método contains() funcionar corretamente
     * nas listas de Favoritos, Assistidas e Para Assistir.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Series)) return false;
        Series series = (Series) o;
        return id == series.id; // Compara apenas pelo ID
    }

    @Override
    public int hashCode() {
        return Objects.hash(id); // Hash baseado no ID
    }

    // Usado quando a série precisa ser exibida como texto simples
    @Override
    public String toString() {
        return name != null ? name : "";
    }
}
