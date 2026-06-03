package fag;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class ServicoClima {

    private static final String API_KEY = "chavedaapivaiaquipai";

    private static final String BASE_URL =
            "https://weather.visualcrossing.com/VisualCrossingWebServices/rest/services/timeline/";

    public DadosClima buscarClima(String cidade) throws Exception {
        String cidadeCodificada = URLEncoder.encode(cidade, StandardCharsets.UTF_8);
        String urlStr = BASE_URL + cidadeCodificada
                + "/today?unitGroup=metric&include=current&key=" + API_KEY
                + "&contentType=json&lang=pt";

        URI uri = new URI(urlStr);
        HttpURLConnection conexao = (HttpURLConnection) uri.toURL().openConnection();
        conexao.setRequestMethod("GET");
        conexao.setConnectTimeout(10_000);
        conexao.setReadTimeout(10_000);

        int status = conexao.getResponseCode();
        if (status != 200) {
            throw new Exception("Erro ao consultar a API. Código HTTP: " + status
                    + ". Verifique o nome da cidade e sua chave de API.");
        }

        BufferedReader leitor = new BufferedReader(
                new InputStreamReader(conexao.getInputStream(), StandardCharsets.UTF_8));
        StringBuilder resposta = new StringBuilder();
        String linha;
        while ((linha = leitor.readLine()) != null) {
            resposta.append(linha);
        }
        leitor.close();
        conexao.disconnect();

        return parsearJson(cidade, resposta.toString());
    }

    private DadosClima parsearJson(String cidade, String json) throws Exception {
        String blocoAtual = extrairBloco(json, "currentConditions");
        String blocoDia   = extrairBloco(json, "days");

        double tempAtual      = extrairDouble(blocoAtual, "temp");
        double umidade        = extrairDouble(blocoAtual, "humidity");
        double precipitacao   = extrairDouble(blocoAtual, "precip");
        double velVento       = extrairDouble(blocoAtual, "windspeed");
        double dirVento       = extrairDouble(blocoAtual, "winddir");
        String condicao       = extrairString(blocoAtual, "conditions");

        double tempMax = extrairDouble(blocoDia, "tempmax");
        double tempMin = extrairDouble(blocoDia, "tempmin");

        String direcaoTexto = converterDirecaoVento(dirVento);

        String nomeResolvido = extrairString(json, "resolvedAddress");
        if (nomeResolvido == null || nomeResolvido.isEmpty()) {
            nomeResolvido = cidade;
        }

        return new DadosClima(nomeResolvido, tempAtual, tempMax, tempMin,
                umidade, condicao, precipitacao, velVento, direcaoTexto);
    }

    private String extrairBloco(String json, String chave) {
        String marcador = "\"" + chave + "\"";
        int inicio = json.indexOf(marcador);
        if (inicio == -1) return "";

        int abre = json.indexOf('{', inicio);
        int abreArr = json.indexOf('[', inicio);
        boolean usaArray = (abreArr != -1 && (abre == -1 || abreArr < abre));
        char abertura = usaArray ? '[' : '{';
        char fechamento = usaArray ? ']' : '}';
        int pos = usaArray ? abreArr : abre;
        if (pos == -1) return "";

        int profundidade = 0;
        StringBuilder bloco = new StringBuilder();
        for (int i = pos; i < json.length(); i++) {
            char c = json.charAt(i);
            bloco.append(c);
            if (c == abertura)  profundidade++;
            if (c == fechamento) {
                profundidade--;
                if (profundidade == 0) break;
            }
        }
        return bloco.toString();
    }

    private double extrairDouble(String json, String chave) {
        String marcador = "\"" + chave + "\":";
        int idx = json.indexOf(marcador);
        if (idx == -1) return 0.0;
        int inicio = idx + marcador.length();
        int fim = inicio;
        while (fim < json.length() && (Character.isDigit(json.charAt(fim))
                || json.charAt(fim) == '.' || json.charAt(fim) == '-')) {
            fim++;
        }
        try {
            return Double.parseDouble(json.substring(inicio, fim));
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    private String extrairString(String json, String chave) {
        String marcador = "\"" + chave + "\":\"";
        int idx = json.indexOf(marcador);
        if (idx == -1) return "";
        int inicio = idx + marcador.length();
        int fim = json.indexOf('"', inicio);
        if (fim == -1) return "";
        return json.substring(inicio, fim);
    }

    private String converterDirecaoVento(double graus) {
        String[] direcoes = {"N", "NNE", "NE", "ENE", "L", "ESE", "SE", "SSE",
                             "S", "SSO", "SO", "OSO", "O", "ONO", "NO", "NNO"};
        int indice = (int) Math.round(graus / 22.5) % 16;
        return direcoes[indice];
    }
}
