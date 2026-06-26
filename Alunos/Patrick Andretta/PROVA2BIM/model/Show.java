package model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Locale;

public class Show {
    private final int id;
    private final String name;
    private final String language;
    private final String genres;
    private final double rating;
    private final String status;
    private final String premiereDate;
    private final String endDate;
    private final String network;
    private final String summary;

    @JsonCreator
    public Show(
            @JsonProperty("id") int id,
            @JsonProperty("name") String name,
            @JsonProperty("language") String language,
            @JsonProperty("genres") String genres,
            @JsonProperty("rating") double rating,
            @JsonProperty("status") String status,
            @JsonProperty("premiereDate") String premiereDate,
            @JsonProperty("endDate") String endDate,
            @JsonProperty("network") String network,
            @JsonProperty("summary") String summary) {
        this.id = id;
        this.name = name != null ? name : "";
        this.language = language != null ? language : "";
        this.genres = genres != null ? genres : "";
        this.rating = rating;
        this.status = status != null ? status : "";
        this.premiereDate = premiereDate != null ? premiereDate : "";
        this.endDate = endDate != null ? endDate : "";
        this.network = network != null ? network : "";
        this.summary = summary != null ? summary : "";
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getLanguage() { return language; }
    public String getGenres() { return genres; }
    public double getRating() { return rating; }
    public String getStatus() { return status; }
    public String getPremiereDate() { return premiereDate; }
    public String getEndDate() { return endDate; }
    public String getNetwork() { return network; }
    public String getSummary() { return summary; }

    public String describe() {
        return String.format(
            "Nome:       %s%n" +
            "Idioma:     %s%n" +
            "Gêneros:    %s%n" +
            "Nota:       %s%n" +
            "Status:     %s%n" +
            "Estreia:    %s%n" +
            "Encerrou:   %s%n" +
            "Emissora:   %s%n%n" +
            "Sinopse:%n%s",
            name,
            language.isEmpty() ? "N/A" : language,
            genres.isEmpty() ? "N/A" : genres,
            rating > 0 ? String.format(Locale.US, "%.1f", rating) : "N/A",
            status.isEmpty() ? "N/A" : status,
            premiereDate.isEmpty() ? "N/A" : premiereDate,
            endDate.isEmpty() ? "—" : endDate,
            network.isEmpty() ? "N/A" : network,
            summary.isEmpty() ? "Sem descrição." : summary
        );
    }

    public static String stripHtml(String raw) {
        if (raw == null) return "";
        StringBuilder sb = new StringBuilder();
        boolean inTag = false;
        for (char c : raw.toCharArray()) {
            if (c == '<') {
                inTag = true;
            } else if (c == '>') {
                inTag = false;
            } else if (!inTag) {
                sb.append(c);
            }
        }
        return sb.toString()
            .replace("&amp;", "&")
            .replace("&lt;", "<")
            .replace("&gt;", ">")
            .replace("&quot;", "\"")
            .replace("&#39;", "'")
            .replace("&nbsp;", " ")
            .trim();
    }
}
