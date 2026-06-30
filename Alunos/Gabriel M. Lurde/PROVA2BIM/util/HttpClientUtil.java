package util;

import exception.ApiException;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class HttpClientUtil {

    public static String buscarDados(String url)
            throws ApiException {

        try {

            HttpClient client =
                    HttpClient.newHttpClient();

            HttpRequest request =
                    HttpRequest.newBuilder()
                            .uri(URI.create(url))
                            .GET()
                            .build();

            HttpResponse<String> response =
                    client.send(
                            request,
                            HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {

                throw new ApiException(
                        "Erro ao acessar a API.");
            }

            return response.body();

        } catch (IOException | InterruptedException e) {

            throw new ApiException(
                    "Falha na conexão com a API.");
        }
    }

}