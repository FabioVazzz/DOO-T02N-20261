package com.tvtracker.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.tvtracker.model.Series;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * CLIENTE DA API — faz requisições HTTP para a API pública do TVMaze.


 Responsável por:
 * 1. Montar a URL da requisição
 * 2. Fazer a chamada HTTP GET
 * 3. Receber o JSON de resposta
 * 4. Converter o JSON em objetos Series
 */
public class api {

    // URL base da API — todas as requisições partem daqui
    private static final String BASE_URL = "https://api.tvmaze.com";

    // Gson é a biblioteca do Google para converter JSON <-> objetos Java
    private final Gson gson = new GsonBuilder().create();

    // ===================== CLASSES INTERNAS (estrutura do JSON da API) =====================

    /**
     * Representa um item da lista de resultados da busca.
     * A API retorna: [{ "score": 0.9, "show": {...} }, ...]
     */
    private static class SearchResult {
        double score; // Relevância do resultado (não usamos, mas está no JSON)
        ShowData show; // Os dados da série em si
    }

    /**
     * Representa os dados brutos de uma série vindos da API.
     * Os nomes dos campos DEVEM ser iguais às chaves do JSON da API.
     */
    private static class ShowData {
        int id;
        String name;
        String language;
        List<String> genres;
        RatingData rating;     // A nota vem dentro de um objeto aninhado
        String status;
        String premiered;
        String ended;
        NetworkData network;    // Emissora tradicional (ex: HBO, NBC)
        NetworkData webChannel; // Plataforma de streaming (ex: Netflix, Prime)
        String summary;         // Sinopse em HTML (vamos remover as tags depois)
    }

    /**
     * Objeto aninhado que contém a nota média da série.
     * No JSON: "rating": { "average": 8.5 }
     */
    private static class RatingData {
        Double average; // Double (com D maiúsculo) para aceitar null quando não há nota
    }


    private static class NetworkData {
        String name;
    }

    // ===================== MÉTODOS PÚBLICOS =====================

    /**
     * Busca séries pelo nome na API do TVMaze.
     */
    public List<Series> searchShows(String query) throws IOException {
        // URLEncoder.encode garante que caracteres especiais (espaços, acentos)
        // sejam convertidos para o formato correto de URL (ex: "breaking bad" → "breaking+bad")
        String encoded = URLEncoder.encode(query, StandardCharsets.UTF_8.name());

        // Faz a requisição e recebe o JSON como string
        String json = fetchJson(BASE_URL + "/search/shows?q=" + encoded);

        // TypeToken é necessário para o Gson entender que precisa criar uma List<SearchResult>
        // (Java apaga informações de generics em tempo de execução — isso contorna esse problema)
        Type listType = new TypeToken<List<SearchResult>>() {}.getType();

        // Converte o JSON para uma lista de objetos SearchResult
        List<SearchResult> results = gson.fromJson(json, listType);

        // Transforma cada SearchResult em um objeto Series do nosso modelo
        List<Series> series = new ArrayList<>();
        if (results != null) {
            for (SearchResult r : results) {
                if (r.show != null) {
                    series.add(mapToSeries(r.show)); // Mapeia ShowData → Series
                }
            }
        }
        return series;
    }

    /**
     * Busca uma série específica pelo ID na API.
     * Endpoint: GET /shows/{id}
     */
    public Series getShowById(int id) throws IOException {
        String json = fetchJson(BASE_URL + "/shows/" + id);
        ShowData show = gson.fromJson(json, ShowData.class);
        return show != null ? mapToSeries(show) : null;
    }

    // ===================== MÉTODOS PRIVADOS =====================

    /**
     * Converte um objeto ShowData (estrutura da API) em Series (nosso modelo).
     */
    private Series mapToSeries(ShowData s) {
        // Se não há nota ou ela é nula, usa 0.0 como padrão
        double rating = (s.rating != null && s.rating.average != null) ? s.rating.average : 0.0;

        // Tenta pegar o nome da emissora tradicional; se não houver, tenta streaming
        String network = "N/A";
        if (s.network != null && s.network.name != null) {
            network = s.network.name;
        } else if (s.webChannel != null && s.webChannel.name != null) {
            network = s.webChannel.name;
        }

        // Remove todas as tags HTML da sinopse usando regex
        // Ex: "<p><b>Breaking Bad</b> is a series...</p>" → "Breaking Bad is a series..."
        String summary = s.summary;
        if (summary != null) {
            summary = summary.replaceAll("<[^>]*>", "").trim();
        }

        // Cria e retorna o objeto Series com todos os campos mapeados
        return new Series(
                s.id,
                s.name,
                s.language,
                s.genres != null ? s.genres : new ArrayList<>(),
                rating,
                s.status,
                s.premiered,
                s.ended,
                network,
                summary
        );
    }

    /**
     * Faz uma requisição HTTP GET para a URL informada e retorna o corpo como String.
     */
    private String fetchJson(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(10000); // 10 segundos para conectar
        conn.setReadTimeout(15000);    // 15 segundos para receber a resposta
        conn.setRequestProperty("User-Agent", "TVSeriesTracker/1.0"); // Identifica o app
        conn.setRequestProperty("Accept", "application/json");         // Pede resposta em JSON

        int code = conn.getResponseCode();

        // Se o servidor não retornar 200 (OK), lança exceção com o código recebido
        if (code != HttpURLConnection.HTTP_OK) {
            conn.disconnect();
            throw new IOException("HTTP " + code + " ao acessar: " + urlString);
        }

        // Lê a resposta linha por linha e monta uma string completa
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();
        } finally {
            // Sempre desconecta ao final, com sucesso ou erro
            conn.disconnect();
        }
    }
}
