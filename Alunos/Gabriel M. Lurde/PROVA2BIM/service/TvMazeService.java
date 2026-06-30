package service;

import exception.ApiException;
import model.Serie;
import util.HttpClientUtil;
import util.JsonParser;

public class TvMazeService {

    private static final String URL_BASE =
            "https://api.tvmaze.com/search/shows?q=";

    public Serie pesquisarSerie(String nome)
            throws ApiException {

        if (nome == null || nome.trim().isEmpty()) {

            throw new ApiException(
                    "Digite o nome da série.");

        }

        String url =
                URL_BASE + nome.replace(" ", "%20");

        String json =
                HttpClientUtil.buscarDados(url);

        return JsonParser.parse(json);

    }

}