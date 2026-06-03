package com.weather;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

// essa classe e responsavel por chamar a API e retornar os dados
public class ApiClima {

    private String chaveApi;

    public ApiClima(String chaveApi) {
        this.chaveApi = chaveApi;
    }

    public DadosClima buscar(String cidade) throws Exception {

        // monta a URL com a cidade e a chave
        String cidadeCodificada = URLEncoder.encode(cidade, StandardCharsets.UTF_8);
        String urlStr = "https://weather.visualcrossing.com/VisualCrossingWebServices/rest/services/timeline/"
                + cidadeCodificada + "/today"
                + "?unitGroup=metric"
                + "&include=current,days"
                + "&key=" + chaveApi
                + "&contentType=json";

        // abre conexao
        URL url = new URL(urlStr);
        HttpURLConnection conexao = (HttpURLConnection) url.openConnection();
        conexao.setRequestMethod("GET");
        conexao.setConnectTimeout(8000);
        conexao.setReadTimeout(8000);

        int codigo = conexao.getResponseCode();

        // verifica se deu erro
        if (codigo != 200) {
            if (codigo == 401 || codigo == 403) {
                throw new Exception("Chave de API inválida.");
            } else if (codigo == 404) {
                throw new Exception("Cidade não encontrada.");
            } else {
                throw new Exception("Erro na requisição. Código: " + codigo);
            }
        }

        // le a resposta
        BufferedReader leitor = new BufferedReader(
                new InputStreamReader(conexao.getInputStream(), StandardCharsets.UTF_8)
        );

        StringBuilder resposta = new StringBuilder();
        String linha;
        while ((linha = leitor.readLine()) != null) {
            resposta.append(linha);
        }
        leitor.close();

        // converte o JSON e monta o objeto
        return converterJson(resposta.toString());
    }

    private DadosClima converterJson(String json) {
        JSONObject root = new JSONObject(json);
        JSONObject atual = root.getJSONObject("currentConditions");
        JSONArray dias = root.getJSONArray("days");
        JSONObject hoje = dias.getJSONObject(0);

        DadosClima dados = new DadosClima();

        dados.cidade    = root.optString("resolvedAddress", "—");
        dados.condicao  = atual.optString("conditions", "—");
        dados.tempAtual = atual.optDouble("temp", 0);
        dados.sensacao  = atual.optDouble("feelslike", 0);
        dados.umidade   = atual.optDouble("humidity", 0);
        dados.chuva     = hoje.optDouble("precip", 0);
        dados.vento     = atual.optDouble("windspeed", 0);
        dados.tempMax   = hoje.optDouble("tempmax", 0);
        dados.tempMin   = hoje.optDouble("tempmin", 0);

        return dados;
    }
}
