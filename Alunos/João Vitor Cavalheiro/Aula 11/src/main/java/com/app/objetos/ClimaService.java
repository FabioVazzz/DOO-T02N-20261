package com.app.objetos;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.app.config.Config;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ClimaService {

    private static final String API_KEY = Config.getApiKey();

    public Clima buscarClima(String cidade) {

        try {

            String endereco = "https://weather.visualcrossing.com/VisualCrossingWebServices/rest/services/timeline/"
                    + cidade + "?unitGroup=metric&key=" + API_KEY;

            URL url = new URL(endereco);

            HttpURLConnection conexao = (HttpURLConnection) url.openConnection();

            conexao.setRequestMethod("GET");

            StringBuilder resposta;
            try (BufferedReader leitor = new BufferedReader(
                    new InputStreamReader(
                            conexao.getInputStream()))) {
                String linha;
                resposta = new StringBuilder();
                while ((linha = leitor.readLine()) != null) {
                    resposta.append(linha);
                }
            }

            ObjectMapper mapper = new ObjectMapper();

            JsonNode root = mapper.readTree(resposta.toString());

            JsonNode dia = root.get("days").get(0);

            JsonNode atual = root.get("currentConditions");

            Clima clima = new Clima();

            clima.setTemperaturaAtual(
                    atual.get("temp").asDouble());

            clima.setTemperaturaMaxima(
                    dia.get("tempmax").asDouble());

            clima.setTemperaturaMinima(
                    dia.get("tempmin").asDouble());

            clima.setUmidade(
                    atual.get("humidity").asDouble());

            String condicao = atual.get("conditions").asText();

            clima.setCondicao(
                    traduzirCondicao(condicao));

            clima.setPrecipitacao(
                    atual.get("precip").asDouble());

            clima.setVelocidadeVento(
                    atual.get("windspeed").asDouble());

            clima.setDirecaoVento(
                    atual.get("winddir").asDouble());

            return clima;

        } catch (IOException e) {
            return null;
        }
    }

    private String traduzirCondicao(String condicao) {

        String texto = condicao.toLowerCase();

        if (texto.contains("rain")) {
            return "Chuva";
        }

        if (texto.contains("thunder")) {
            return "Tempestade";
        }

        if (texto.contains("snow")) {
            return "Neve";
        }

        if (texto.contains("fog")) {
            return "Neblina";
        }

        if (texto.contains("overcast")) {
            return "Nublado";
        }

        if (texto.contains("cloud")) {
            return "Parcialmente Nublado";
        }

        if (texto.contains("clear")) {
            return "Céu Limpo";
        }

        return condicao;
    }

}