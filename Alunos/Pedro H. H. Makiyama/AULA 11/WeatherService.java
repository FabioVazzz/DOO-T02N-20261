package com.pedro;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import com.fasterxml.jackson.databind.ObjectMapper;

public class WeatherService {

    private final String apiKey = System.getenv("VISUAL_CROSSING_API_KEY");
    private final HttpClient client = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();

    public WeatherDataDay getWeatherDataToday(){

        return getWeatherDataDay(LocalDate.now().toString());
    }

    public WeatherDataDay getWeatherDataDay(String date){

        try {

            if (apiKey == null || apiKey.isBlank()){

                throw new WeatherApiException("Variável VISUAL_CROSSING_API_KEY não configurada");
            }

            String url =
            "https://weather.visualcrossing.com/VisualCrossingWebServices/rest/services/timeline/" +
            "-24.54,-52.99/" +
            date +
            "?unitGroup=metric" +
            "&lang=pt" +
            "&include=days" +
            "&elements=temp,tempmax,tempmin,conditions,humidity,precip,windspeed,winddir" +
            "&key=" + apiKey;

            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).GET().build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println("Response Status: " + response.statusCode());

            switch (response.statusCode()) {

                case 200:

                    break;

                case 400:

                    throw new WeatherApiException("Data inválida ou requisição mal formada.");

                case 401:

                    throw new WeatherApiException("API Key inválida.");

                case 403:

                    throw new WeatherApiException("Acesso negado pela API.");

                case 404:

                    throw new WeatherApiException("Dados climáticos não encontrados.");

                case 429:

                    throw new WeatherApiException("Limite de requisições excedido.");

                case 500:

                    throw new WeatherApiException("Erro interno da API.");

                default:

                    throw new WeatherApiException("Erro HTTP: " + response.statusCode());
            }

            WeatherDataResponse weatherDataResponse = mapper.readValue(response.body(), WeatherDataResponse.class);

            if (weatherDataResponse.getDays() == null || weatherDataResponse.getDays().isEmpty()){

                throw new WeatherApiException("Nenhum dado encontrado para a data.");
            }

            System.out.println(response.body());

            return weatherDataResponse.getDays().get(0);
        
        } catch (WeatherApiException e) {

            throw e;

        } catch (Exception e) { 

            throw new RuntimeException("Erro no sistema.", e);
        }
    }
}
