package fag;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class WeatherService {

    private final ObjectMapper mapper = new ObjectMapper();

    public WeatherData buscarClima(String cidade) {

        try {

            String endpoint =
                    "https://weather.visualcrossing.com/VisualCrossingWebServices/rest/services/timeline/"
                            + cidade
                            + "?unitGroup=metric&include=current&key="
                            + Config.API_KEY
                            + "&contentType=json";

            URL url = new URL(endpoint);

            HttpURLConnection conexao =
                    (HttpURLConnection) url.openConnection();

            conexao.setRequestMethod("GET");

            BufferedReader leitor =
                    new BufferedReader(
                            new InputStreamReader(conexao.getInputStream()));

            StringBuilder resposta = new StringBuilder();
            String linha;

            while ((linha = leitor.readLine()) != null) {
                resposta.append(linha);
            }

            leitor.close();

            JsonNode root =
                    mapper.readTree(resposta.toString());

            JsonNode atual =
                    root.get("currentConditions");

            JsonNode dia =
                    root.get("days").get(0);

            return new WeatherData(
                    atual.get("temp").asDouble(),
                    dia.get("tempmax").asDouble(),
                    dia.get("tempmin").asDouble(),
                    atual.get("humidity").asDouble(),
                    atual.get("conditions").asText(),
                    atual.get("precip").asDouble(),
                    atual.get("windspeed").asDouble(),
                    String.valueOf(atual.get("winddir").asDouble())
            );

        } catch (Exception e) {
            System.out.println("Erro ao consultar clima.");
            e.printStackTrace();
        }

        return null;
    }
}