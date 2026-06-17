import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.net.URLEncoder;   
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets; 
import java.util.ArrayList;
import java.util.List;

public class TvMazeService {

    private final HttpClient client = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();

    public List<Series> search(String query) throws Exception {

        //encoder para caracteres especiais
        String encoded = URLEncoder.encode(query, StandardCharsets.UTF_8);
        String url = "https://api.tvmaze.com/search/shows?q=" + encoded;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());

        JsonNode root = mapper.readTree(response.body());
        List<Series> results = new ArrayList<>();

        for (JsonNode node : root) {
            Series s = mapper.treeToValue(node.get("show"), Series.class);
            results.add(s);
        }

        return results;
    }
}