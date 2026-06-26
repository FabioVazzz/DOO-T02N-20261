package service;

import model.Show;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class TVMazeService {
    private static final String SEARCH_URL = "https://api.tvmaze.com/search/shows?q=";
    private static final String SHOW_URL   = "https://api.tvmaze.com/shows/";

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private final HttpClient http;

    public TVMazeService() {
        this.http = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .build();
    }

    public List<Show> searchShows(String query) throws TVTrackerException {
        String encoded = URLEncoder.encode(query, StandardCharsets.UTF_8);
        String body = doGet(SEARCH_URL + encoded);
        List<Show> shows = new ArrayList<>();
        JsonNode arr = readTree(body);
        if (arr.isArray()) {
            for (JsonNode item : arr) {
                JsonNode show = item.get("show");
                if (show != null && !show.isNull()) shows.add(parseShow(show));
            }
        }
        return shows;
    }

    public Show fetchShowById(int id) throws TVTrackerException {
        String body = doGet(SHOW_URL + id);
        return parseShow(readTree(body));
    }

    private JsonNode readTree(String json) throws TVTrackerException {
        try {
            return MAPPER.readTree(json);
        } catch (Exception e) {
            throw new TVTrackerException("JSON inválido da API: " + e.getMessage());
        }
    }

    private Show parseShow(JsonNode m) {
        int    id        = m.path("id").asInt(0);
        String name      = textOr(m, "name", "");
        String language  = textOr(m, "language", "N/A");
        String genres    = extractGenres(m);
        double rating    = m.path("rating").path("average").asDouble(0.0);
        String status    = textOr(m, "status", "N/A");
        String premiered = textOr(m, "premiered", "");
        String ended     = textOr(m, "ended", "");
        String network   = extractNetwork(m);
        String summary   = Show.stripHtml(textOr(m, "summary", ""));
        return new Show(id, name, language, genres, rating, status, premiered, ended, network, summary);
    }

    private String extractGenres(JsonNode m) {
        JsonNode genres = m.get("genres");
        if (genres == null || !genres.isArray()) return "";
        StringBuilder sb = new StringBuilder();
        for (JsonNode g : genres) {
            if (sb.length() > 0) sb.append(", ");
            sb.append(g.asText());
        }
        return sb.toString();
    }

    private String extractNetwork(JsonNode m) {
        JsonNode net = m.get("network");
        if (net != null && net.hasNonNull("name")) return net.get("name").asText();
        JsonNode web = m.get("webChannel");
        if (web != null && web.hasNonNull("name")) return web.get("name").asText();
        return "";
    }

    private static String textOr(JsonNode node, String field, String def) {
        JsonNode v = node.get(field);
        return (v == null || v.isNull()) ? def : v.asText();
    }

    private String doGet(String url) throws TVTrackerException {
        try {
            HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(Duration.ofSeconds(15))
                .GET()
                .build();
            HttpResponse<String> response =
                http.send(req, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
            if (response.statusCode() == 200) return response.body();
            throw new TVTrackerException("API retornou HTTP " + response.statusCode(), response.statusCode());
        } catch (java.net.http.HttpTimeoutException e) {
            throw new TVTrackerException("Tempo de conexão esgotado. Verifique sua internet.", 504);
        } catch (TVTrackerException e) {
            throw e;
        } catch (Exception e) {
            throw new TVTrackerException("Erro de rede: " + e.getMessage(), 503);
        }
    }
}
