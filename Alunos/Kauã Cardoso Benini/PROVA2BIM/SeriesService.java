package com.tvtracker.service;

import com.tvtracker.api.api;
import com.tvtracker.model.Dados;
import com.tvtracker.model.Listas;
import com.tvtracker.model.Series;
import com.tvtracker.model.UserProfile;
import com.tvtracker.persistence.JsonPersistence;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * SERVIÇO
 *
 * Responsabilidades:
 * - Buscar séries na API
 * - Gerenciar as listas do usuário (adicionar, remover, verificar)
 * - Ordenar listas
 * - Salvar e carregar dados pelo JsonPersistence
 */
public class SeriesService {

    // Cliente da API TVMaze — faz as chamadas HTTP
    private final api apiClient;

    // Responsável por salvar/carregar o arquivo JSON
    private final JsonPersistence persistence;

    // Todos os dados em memória (perfil + listas do usuário)
    private final Dados appData;

    /**
     * Construtor — inicializa o serviço carregando os dados salvos.
     */
    public SeriesService(JsonPersistence persistence) {
        this.persistence = persistence;
        this.apiClient = new api();

        // Carrega os dados salvos do arquivo JSON (ou cria dados vazios se for a primeira vez)
        this.appData = persistence.load();

        // Garante que o perfil nunca seja nulo
        if (appData.getUserProfile() == null) {
            appData.setUserProfile(new UserProfile());
        }
    }

    // ===================== BUSCA NA API =====================

    /**
     * Busca séries pelo nome usando a API do TVMaze.
     * Lança IOException se não houver conexão com a internet.
     */
    public List<Series> searchShows(String query) throws IOException {
        return apiClient.searchShows(query);
    }

    // ===================== PERFIL DO USUÁRIO =====================

    /** Retorna o perfil completo do usuário (nome + listas) */
    public UserProfile getUserProfile() {
        return appData.getUserProfile();
    }

    /**
     * Define ou altera o nome do usuário e salva imediatamente.
     */
    public void setUserName(String name) {
        appData.getUserProfile().setName(name);
        save(); // Persiste a alteração no arquivo JSON
    }

    // ===================== MANIPULAÇÃO DAS LISTAS =====================

    /**
     * Adiciona uma série a uma das listas do usuário.
     * Usa switch para direcionar ao método correto conforme o tipo de lista.
     */
    public void addToList(Series s, Listas type) {
        switch (type) {
            case FAVORITES:     appData.getUserProfile().addToFavorites(s);    break;
            case WATCHED:       appData.getUserProfile().addToWatched(s);      break;
            case WANT_TO_WATCH: appData.getUserProfile().addToWantToWatch(s);  break;
        }
        save(); // Salva após cada alteração
    }

    /**
     * Remove uma série de uma das listas do usuário.
     */
    public void removeFromList(Series s, Listas type) {
        switch (type) {
            case FAVORITES:     appData.getUserProfile().removeFromFavorites(s);    break;
            case WATCHED:       appData.getUserProfile().removeFromWatched(s);      break;
            case WANT_TO_WATCH: appData.getUserProfile().removeFromWantToWatch(s);  break;
        }
        save(); // Salva após cada alteração
    }

    /**
     * Retorna uma cópia da lista solicitada.
     * Retorna cópia (new ArrayList) para que a UI não modifique a lista original acidentalmente.
     */
    public List<Series> getList(Listas type) {
        switch (type) {
            case FAVORITES:     return new ArrayList<>(appData.getUserProfile().getFavorites());
            case WATCHED:       return new ArrayList<>(appData.getUserProfile().getWatched());
            case WANT_TO_WATCH: return new ArrayList<>(appData.getUserProfile().getWantToWatch());
            default:            return new ArrayList<>();
        }
    }

    /**
     * Verifica se uma série já está em determinada lista.
     * Usado para evitar duplicatas antes de adicionar.
     *
     */
    public boolean isInList(Series s, Listas type) {
        switch (type) {
            case FAVORITES:     return appData.getUserProfile().isInFavorites(s);
            case WATCHED:       return appData.getUserProfile().isInWatched(s);
            case WANT_TO_WATCH: return appData.getUserProfile().isInWantToWatch(s);
            default:            return false;
        }
    }

    // ===================== ORDENAÇÃO =====================

    /**
     * Aplica a ordenação escolhida pelo usuário na lista.
     * O parâmetro "option" é o texto selecionado no JComboBox da interface.
     */
    public List<Series> applySorting(List<Series> list, String option) {
        switch (option) {
            case "Nome":            return sortByName(list);
            case "Nota":            return sortByRating(list);
            case "Estado":          return sortByStatus(list);
            case "Data de Estreia": return sortByPremiereDate(list);
            default:                return list; // "Sem ordenacao" — retorna como está
        }
    }

    /**
     * Ordena por nome em ordem alfabética, ignorando maiúsculas/minúsculas.
     * Ex: "avatar" e "Avatar" são tratados iguais na ordenação.
     */
    private List<Series> sortByName(List<Series> list) {
        return list.stream()
                .sorted(Comparator.comparing(Series::getName, String.CASE_INSENSITIVE_ORDER))
                .collect(Collectors.toList());
    }

    /**
     * Ordena por nota em ordem decrescente (maior nota primeiro).
     * .reversed() inverte a ordenação padrão crescente.
     */
    private List<Series> sortByRating(List<Series> list) {
        return list.stream()
                .sorted(Comparator.comparingDouble(Series::getRating).reversed())
                .collect(Collectors.toList());
    }

    /**
     * Ordena por estado com uma prioridade customizada:
     * 1º Running (em exibição)
     * 2º In Development (em desenvolvimento)
     * 3º To Be Determined (indefinido)
     * 4º Ended (encerrada)
     * 5º outros estados desconhecidos
     */
    private List<Series> sortByStatus(List<Series> list) {
        Map<String, Integer> order = new LinkedHashMap<>();
        order.put("Running", 0);
        order.put("In Development", 1);
        order.put("To Be Determined", 2);
        order.put("Ended", 3);

        return list.stream()
                .sorted(Comparator.comparingInt(
                        s -> order.getOrDefault(s.getStatus(), 4))) // 4 = estado desconhecido
                .collect(Collectors.toList());
    }

    /**
     * Ordena por data de estreia em ordem crescente (mais antigas primeiro).
     */
    private List<Series> sortByPremiereDate(List<Series> list) {
        return list.stream()
                .sorted(Comparator.comparing(
                        s -> s.getPremiered() != null ? s.getPremiered() : "9999",
                        String.CASE_INSENSITIVE_ORDER))
                .collect(Collectors.toList());
    }

    // ===================== PERSISTÊNCIA =====================

    /**
     * Salva o estado atual dos dados no arquivo JSON.
     * Chamado internamente sempre que há uma alteração.
     */
    private void save() {
        persistence.save(appData);
    }
}
