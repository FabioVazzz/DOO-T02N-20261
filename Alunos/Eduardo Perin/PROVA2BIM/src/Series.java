import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

// Jackson ignora oque nao declaramos
@JsonIgnoreProperties(ignoreUnknown = true)
public class Series {

    public int id;
    public String name;
    public String language;
    public List<String> genres;
    public String status;
    public String premiered;
    public String ended;
    public String summary;

    public Rating rating;
    public Network network;
    public Network webChannel; // alguns canais só têm webChannel, não network

    // Retorna a nota ou 0 se não tiver
    public double getRatingAverage() {
        if (rating != null && rating.average != null) return rating.average;
        return 0.0;
    }

    // Retorna o nome da emissora (tenta network, depois webChannel)
    public String getNetworkName() {
        if (network != null && network.name != null) return network.name;
        if (webChannel != null && webChannel.name != null) return webChannel.name;
        return "Desconhecida";
    }

    // Retorna os gêneros como string separada por vírgula
    public String getGenresAsString() {
        if (genres == null || genres.isEmpty()) return "Não informado";
        return String.join(", ", genres);
    }

    @Override
    public String toString() {
        return name != null ? name : "Sem nome";
    }

    // Igualdade pelo ID — evita duplicatas nas listas
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Series)) return false;
        return this.id == ((Series) obj).id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }

    // Classes internas para os objetos aninhados do JSON
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Rating {
        public Double average;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Network {
        public String name;
    }
}