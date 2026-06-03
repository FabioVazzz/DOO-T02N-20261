import java.net.ConnectException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class RequisicaoApi {

    private static final String BASE_URL = "https://weather.visualcrossing.com/VisualCrossingWebServices/rest/services/timeline/";
    private final ObjectMapper objectMapper;
    private final HttpClient httpClient;

    public RequisicaoApi() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        this.httpClient = HttpClient.newHttpClient();
    }

    public RespostaApi buscarClima(String cidade) throws ClimaException {
        try {
            String apiKey = Config.getApiKey();

            if (apiKey == null || apiKey.isEmpty()) {
                throw new ClimaException(
                    "Chave API não configurada", 
                    ClimaException.TipoErro.CHAVE_NAO_CONFIGURADA
                );
            }

            String cidadeDecodificada = URLEncoder.encode(cidade, StandardCharsets.UTF_8);

            String urlString = BASE_URL + cidadeDecodificada + "/today?unitGroup=metric&key=" + apiKey + "&include=days,current";

            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(urlString))
                .GET()
                .header("accept", "application/json")
                .timeout(java.time.Duration.ofSeconds(10))
                .build();

            HttpResponse<String> response;
            try{
                response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            } catch (ConnectException e) {
                throw new ClimaException(
                    "Sem conexão com a internet",
                    ClimaException.TipoErro.SEM_CONEXAO
                );
            } catch (java.net.http.HttpConnectTimeoutException e) {
                throw new ClimaException(
                    "Tempo limite excedido",
                    ClimaException.TipoErro.TIME_OUT
                );
            }

            if (response.statusCode() == 401) {
                throw new ClimaException(
                    "Chave de API inválida", 
                    ClimaException.TipoErro.CHAVE_INVALIDA, 401);
            }

            if (response.statusCode() == 404) {
                throw new ClimaException(
                    "Cidade não encontrada" + cidade, 
                    ClimaException.TipoErro.CIDADE_NAO_ENCONTRADA, 404);
            }

            if (response.statusCode() != 200) {
                throw new ClimaException(
                    "Ocorreu um erro durante a busca.\n Verifique os dados digitados e tente novamente.\n", 
                    ClimaException.TipoErro.ERRO_API,
                    response.statusCode()
                );
            }

            return objectMapper.readValue(response.body(), RespostaApi.class);

        } catch (ClimaException e) {
            throw e;
        } catch (Exception e) {
            throw new ClimaException(
                "Erro inesperado: " + e.getMessage(),
                ClimaException.TipoErro.ERRO_API
            );
        }
    }
}
