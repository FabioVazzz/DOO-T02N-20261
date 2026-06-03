import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class WeatherService {

    private static final String BASE_URL =
            "https://weather.visualcrossing.com/VisualCrossingWebServices/rest/services/timeline/";

    private static final String PARAMS =
            "?unitGroup=metric&include=current,days&contentType=json&lang=pt";

    private final String apiKey;

    public WeatherService(String apiKey) {
        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalArgumentException("A chave da API não pode ser nula ou vazia.");
        }
        this.apiKey = apiKey;
    }

    public WeatherData buscarClima(String cidade) throws WeatherApiException {
        if (cidade == null || cidade.isBlank()) {
            throw new IllegalArgumentException("O nome da cidade não pode ser nulo ou vazio.");
        }

        String urlString = construirUrl(cidade);
        String jsonResposta = fazerRequisicao(urlString);
        return parsearResposta(jsonResposta, cidade);
    }

    private String construirUrl(String cidade) throws WeatherApiException {
        try {
            String cidadeCodificada = URLEncoder.encode(cidade, StandardCharsets.UTF_8);
            return BASE_URL + cidadeCodificada + PARAMS + "&key=" + apiKey;
        } catch (Exception e) {
            throw new WeatherApiException("Erro ao codificar o nome da cidade: " + cidade, e);
        }
    }

    private String fazerRequisicao(String urlString) throws WeatherApiException {
        HttpURLConnection conexao = null;
        try {
            URL url = new URL(urlString);
            conexao = (HttpURLConnection) url.openConnection();
            conexao.setRequestMethod("GET");
            conexao.setConnectTimeout(10_000);
            conexao.setReadTimeout(10_000);

            int codigoHttp = conexao.getResponseCode();
            if (codigoHttp == HttpURLConnection.HTTP_UNAUTHORIZED) {
                throw new WeatherApiException("Chave de API inválida ou não autorizada (HTTP 401).");
            }
            if (codigoHttp == HttpURLConnection.HTTP_NOT_FOUND) {
                throw new WeatherApiException("Cidade não encontrada pela API (HTTP 404).");
            }
            if (codigoHttp != HttpURLConnection.HTTP_OK) {
                throw new WeatherApiException("Resposta inesperada da API: HTTP " + codigoHttp);
            }

            return lerResposta(conexao);

        } catch (IOException e) {
            throw new WeatherApiException("Erro de conexão com a API: " + e.getMessage(), e);
        } finally {
            if (conexao != null) {
                conexao.disconnect();
            }
        }
    }

    private String lerResposta(HttpURLConnection conexao) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader leitor = new BufferedReader(
                new InputStreamReader(conexao.getInputStream(), StandardCharsets.UTF_8))) {
            String linha;
            while ((linha = leitor.readLine()) != null) {
                sb.append(linha);
            }
        }
        return sb.toString();
    }

    private WeatherData parsearResposta(String json, String cidadeOriginal) throws WeatherApiException {
        try {
            String resolvedAddress = extrairValorString(json, "resolvedAddress");

            String blocoAtual = extrairBloco(json, "currentConditions");

            double tempAtual    = extrairValorDouble(blocoAtual, "temp");
            double umidade      = extrairValorDouble(blocoAtual, "humidity");
            String condicao     = extrairValorString(blocoAtual, "conditions");
            double precipitacao = extrairValorDoubleOuZero(blocoAtual, "precip");
            double velVento     = extrairValorDouble(blocoAtual, "windspeed");
            double dirVento     = extrairValorDouble(blocoAtual, "winddir");

            String primeiroDia = extrairPrimeiroDia(json);
            double tempMax = extrairValorDouble(primeiroDia, "tempmax");
            double tempMin = extrairValorDouble(primeiroDia, "tempmin");

            String nomeCidade = (resolvedAddress != null && !resolvedAddress.isBlank())
                    ? resolvedAddress
                    : cidadeOriginal;

            return new WeatherData(
                    nomeCidade, tempAtual, tempMax, tempMin,
                    umidade, condicao, precipitacao, velVento, dirVento);

        } catch (WeatherApiException e) {
            throw e;
        } catch (Exception e) {
            throw new WeatherApiException("Erro ao interpretar a resposta da API: " + e.getMessage(), e);
        }
    }

    private String extrairBloco(String json, String chave) throws WeatherApiException {
        String marcador = "\"" + chave + "\"";
        int pos = json.indexOf(marcador);
        if (pos == -1) {
            throw new WeatherApiException("Campo '" + chave + "' não encontrado na resposta.");
        }
        int inicio = json.indexOf('{', pos);
        if (inicio == -1) {
            throw new WeatherApiException("Bloco de '" + chave + "' malformado.");
        }
        int profundidade = 0;
        int fim = inicio;
        for (int i = inicio; i < json.length(); i++) {
            if (json.charAt(i) == '{') profundidade++;
            else if (json.charAt(i) == '}') profundidade--;
            if (profundidade == 0) {
                fim = i;
                break;
            }
        }
        return json.substring(inicio, fim + 1);
    }

    private String extrairPrimeiroDia(String json) throws WeatherApiException {
        String marcador = "\"days\"";
        int pos = json.indexOf(marcador);
        if (pos == -1) throw new WeatherApiException("Campo 'days' não encontrado na resposta.");
        int inicio = json.indexOf('{', pos);
        if (inicio == -1) throw new WeatherApiException("Array 'days' vazio ou malformado.");
        int profundidade = 0;
        int fim = inicio;
        for (int i = inicio; i < json.length(); i++) {
            if (json.charAt(i) == '{') profundidade++;
            else if (json.charAt(i) == '}') profundidade--;
            if (profundidade == 0) {
                fim = i;
                break;
            }
        }
        return json.substring(inicio, fim + 1);
    }

    private double extrairValorDouble(String json, String chave) throws WeatherApiException {
        String marcador = "\"" + chave + "\"";
        int pos = json.indexOf(marcador);
        if (pos == -1) {
            throw new WeatherApiException("Campo numérico '" + chave + "' não encontrado.");
        }
        int doisPontos = json.indexOf(':', pos);
        int inicio = doisPontos + 1;
        while (inicio < json.length() && json.charAt(inicio) == ' ') inicio++;
        int fim = inicio;
        while (fim < json.length() && (Character.isDigit(json.charAt(fim))
                || json.charAt(fim) == '.' || json.charAt(fim) == '-')) {
            fim++;
        }
        String valor = json.substring(inicio, fim).trim();
        if (valor.isEmpty()) {
            throw new WeatherApiException("Valor do campo '" + chave + "' está vazio.");
        }
        return Double.parseDouble(valor);
    }

    private double extrairValorDoubleOuZero(String json, String chave) {
        try {
            String marcador = "\"" + chave + "\"";
            int pos = json.indexOf(marcador);
            if (pos == -1) return 0.0;
            int doisPontos = json.indexOf(':', pos);
            int inicio = doisPontos + 1;
            while (inicio < json.length() && json.charAt(inicio) == ' ') inicio++;
            if (json.startsWith("null", inicio)) return 0.0;
            int fim = inicio;
            while (fim < json.length() && (Character.isDigit(json.charAt(fim))
                    || json.charAt(fim) == '.' || json.charAt(fim) == '-')) {
                fim++;
            }
            String valor = json.substring(inicio, fim).trim();
            return valor.isEmpty() ? 0.0 : Double.parseDouble(valor);
        } catch (Exception e) {
            return 0.0;
        }
    }

    private String extrairValorString(String json, String chave) {
        String marcador = "\"" + chave + "\":\"";
        int pos = json.indexOf(marcador);
        if (pos == -1) return "";
        int inicio = pos + marcador.length();
        int fim = json.indexOf('"', inicio);
        if (fim == -1) return "";
        return json.substring(inicio, fim);
    }
}