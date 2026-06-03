import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;

public class ApiService {

    public String fetch(String city) throws Exception {

        String url = Config.BASE_URL +
                URLEncoder.encode(city, StandardCharsets.UTF_8) +
                "?unitGroup=metric&key=" + Config.API_KEY +
                "&contentType=json";

        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
        conn.setRequestMethod("GET");

        BufferedReader br = new BufferedReader(
                new InputStreamReader(conn.getInputStream()));

        StringBuilder sb = new StringBuilder();
        String line;

        while ((line = br.readLine()) != null) {
            sb.append(line);
        }

        return sb.toString();
    }
}