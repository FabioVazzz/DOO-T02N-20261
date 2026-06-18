import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class ApiTvMaze {

    private static final String URL_BASE = "https://api.tvmaze.com";
    private static final int TIMEOUT_MS = 8000;

    public List<Serie> buscarSeries(String nomeDaBusca) throws Exception {
        List<Serie> resultados = new ArrayList<>();

        if (nomeDaBusca == null || nomeDaBusca.trim().isEmpty()) {
            throw new IllegalArgumentException("O termo de busca não pode estar vazio.");
        }

        String termoCodificado = URLEncoder.encode(nomeDaBusca.trim(), "UTF-8");
        String urlCompleta = URL_BASE + "/search/shows?q=" + termoCodificado;

        String jsonResposta = fazerRequisicao(urlCompleta);

        JsonArray arrayResultados = JsonParser.parseString(jsonResposta).getAsJsonArray();

        for (JsonElement elemento : arrayResultados) {
            try {
                JsonObject itemBusca = elemento.getAsJsonObject();
                JsonObject dadosSerie = itemBusca.getAsJsonObject("show");

                Serie serie = converterJsonParaSerie(dadosSerie);
                resultados.add(serie);
            } catch (Exception e) {
                System.out.println("Aviso: não foi possível converter um resultado da busca.");
            }
        }

        return resultados;
    }

    private Serie converterJsonParaSerie(JsonObject dados) {
        Serie serie = new Serie();

        if (dados.has("id") && !dados.get("id").isJsonNull()) {
            serie.setId(dados.get("id").getAsInt());
        }

        if (dados.has("name") && !dados.get("name").isJsonNull()) {
            serie.setNome(dados.get("name").getAsString());
        }

        if (dados.has("language") && !dados.get("language").isJsonNull()) {
            serie.setIdioma(dados.get("language").getAsString());
        }

        if (dados.has("genres") && !dados.get("genres").isJsonNull()) {
            JsonArray genArray = dados.getAsJsonArray("genres");
            StringBuilder generosStr = new StringBuilder();
            for (int i = 0; i < genArray.size(); i++) {
                if (i > 0) generosStr.append(", ");
                generosStr.append(genArray.get(i).getAsString());
            }
            serie.setGeneros(generosStr.toString());
        }

        if (dados.has("rating") && !dados.get("rating").isJsonNull()) {
            JsonObject rating = dados.getAsJsonObject("rating");
            if (rating.has("average") && !rating.get("average").isJsonNull()) {
                serie.setNota(rating.get("average").getAsDouble());
            }
        }

        if (dados.has("status") && !dados.get("status").isJsonNull()) {
            serie.setEstado(dados.get("status").getAsString());
        }

        if (dados.has("premiered") && !dados.get("premiered").isJsonNull()) {
            serie.setDataEstreia(dados.get("premiered").getAsString());
        }

        if (dados.has("ended") && !dados.get("ended").isJsonNull()) {
            serie.setDataTermino(dados.get("ended").getAsString());
        }

        if (dados.has("network") && !dados.get("network").isJsonNull()) {
            JsonObject network = dados.getAsJsonObject("network");
            if (network.has("name") && !network.get("name").isJsonNull()) {
                serie.setEmissora(network.get("name").getAsString());
            }
        }

        if ((serie.getEmissora() == null || serie.getEmissora().equals("Informação não disponível"))
                && dados.has("webChannel") && !dados.get("webChannel").isJsonNull()) {
            JsonObject webChannel = dados.getAsJsonObject("webChannel");
            if (webChannel.has("name") && !webChannel.get("name").isJsonNull()) {
                serie.setEmissora(webChannel.get("name").getAsString());
            }
        }

        if (dados.has("summary") && !dados.get("summary").isJsonNull()) {
            String resumoHtml = dados.get("summary").getAsString();

            String resumoLimpo = resumoHtml.replaceAll("<[^>]+>", "").trim();
            serie.setResumo(resumoLimpo);
        }

        if (dados.has("image") && !dados.get("image").isJsonNull()) {
            JsonObject image = dados.getAsJsonObject("image");
            if (image.has("medium") && !image.get("medium").isJsonNull()) {
                serie.setUrlImagem(image.get("medium").getAsString());
            }
        }

        return serie;
    }

    private String fazerRequisicao(String urlString) throws Exception {
        HttpURLConnection conexao = null;

        try {
            URL url = new URL(urlString);
            conexao = (HttpURLConnection) url.openConnection();
            conexao.setRequestMethod("GET");
            conexao.setConnectTimeout(TIMEOUT_MS);
            conexao.setReadTimeout(TIMEOUT_MS);
            conexao.setRequestProperty("Accept", "application/json");
            conexao.setRequestProperty("User-Agent", "SeriesTracker/1.0");

            int codigoResposta = conexao.getResponseCode();

            if (codigoResposta != HttpURLConnection.HTTP_OK) {
                throw new Exception("A API retornou erro: código " + codigoResposta);
            }

            BufferedReader leitor = new BufferedReader(
                    new InputStreamReader(conexao.getInputStream(), "UTF-8")
            );

            StringBuilder resposta = new StringBuilder();
            String linha;
            while ((linha = leitor.readLine()) != null) {
                resposta.append(linha);
            }
            leitor.close();

            return resposta.toString();

        } catch (java.net.UnknownHostException e) {
            throw new Exception("Sem conexão com a internet. Verifique sua rede e tente novamente.");
        } catch (java.net.SocketTimeoutException e) {
            throw new Exception("Tempo de resposta esgotado. A API demorou muito para responder.");
        } catch (java.io.IOException e) {
            throw new Exception("Erro ao se comunicar com a API: " + e.getMessage());
        } finally {

            if (conexao != null) {
                conexao.disconnect();
            }
        }
    }
}