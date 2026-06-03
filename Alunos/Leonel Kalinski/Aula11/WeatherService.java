package app;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class WeatherService {

    public String consultarCidade(String cidade) throws Exception {

        cidade = URLEncoder.encode(cidade, "UTF-8");

        String endpoint =
                "https://weather.visualcrossing.com/VisualCrossingWebServices/rest/services/timeline/"
                        + cidade
                        + "/today"
                        + "?unitGroup=metric"
                        + "&include=days"
                        + "&key=" + API_KEY
                        + "&contentType=csv";

        URL url = new URL(endpoint);

        HttpURLConnection conexao = (HttpURLConnection) url.openConnection();

        conexao.setRequestMethod("GET");

        BufferedReader reader =
                new BufferedReader(
                        new InputStreamReader(
                                conexao.getInputStream()));

        String cabecalho = reader.readLine();
        String dados = reader.readLine();

        reader.close();

        if (dados == null) {
            throw new Exception("Nenhum dado recebido da API.");
        }

        String[] colunas = cabecalho.split(",");
        String[] valores = dados.split(",");

        String temperaturaAtual = "";
        String temperaturaMaxima = "";
        String temperaturaMinima = "";
        String umidade = "";
        String condicao = "";
        String precipitacao = "";
        String velocidadeVento = "";
        String direcaoVento = "";

        for (int i = 0; i < colunas.length; i++) {

            String coluna = colunas[i].trim().toLowerCase();

            switch (coluna) {

                case "temp":
                    temperaturaAtual = valores[i];
                    break;

                case "tempmax":
                    temperaturaMaxima = valores[i];
                    break;

                case "tempmin":
                    temperaturaMinima = valores[i];
                    break;

                case "humidity":
                    umidade = valores[i];
                    break;

                case "conditions":
                    condicao = valores[i];
                    break;

                case "precip":
                    precipitacao = valores[i];
                    break;

                case "windspeed":
                    velocidadeVento = valores[i];
                    break;

                case "winddir":
                    direcaoVento = valores[i];
                    break;
            }
        }

        return "Cidade: " + cidade + "\n\n"
                + "Temperatura Atual: " + temperaturaAtual + " °C\n"
                + "Temperatura Máxima: " + temperaturaMaxima + " °C\n"
                + "Temperatura Mínima: " + temperaturaMinima + " °C\n"
                + "Umidade: " + umidade + " %\n"
                + "Condição: " + condicao + "\n"
                + "Precipitação: " + precipitacao + " mm\n"
                + "Velocidade do Vento: " + velocidadeVento + " km/h\n"
                + "Direção do Vento: " + direcaoVento + "°";
    }
}