import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class ClimaServico {

    private static final String API_KEY =
            System.getenv("VISUAL_CROSSING_KEY");

    public Clima buscarClima(String cidade) {

        try {

            cidade = URLEncoder.encode(cidade, StandardCharsets.UTF_8);

            String endereco =
                    "https://weather.visualcrossing.com/VisualCrossingWebServices/rest/services/timeline/"
                            + cidade
                            + "?unitGroup=metric&key="
                            + API_KEY
                            + "&contentType=json";

            URL url = new URL(endereco);

            HttpURLConnection conexao =
                    (HttpURLConnection) url.openConnection();

            conexao.setRequestMethod("GET");

            BufferedReader leitor =
                    new BufferedReader(
                            new InputStreamReader(conexao.getInputStream()));

            StringBuilder resposta = new StringBuilder();
            String linha;

            while ((linha = leitor.readLine()) != null) {
                resposta.append(linha);
            }

            leitor.close();

            String json = resposta.toString();

            String blocoDia = extrairBlocoDays(json);

            Clima clima = new Clima();

            clima.setCidade(extrair(json, "\"resolvedAddress\":\""));

            clima.setTemperatura(
                    Double.parseDouble(extrair(blocoDia, "\"temp\":"))
            );

            clima.setTempMax(
                    Double.parseDouble(extrair(blocoDia, "\"tempmax\":"))
            );

            clima.setTempMin(
                    Double.parseDouble(extrair(blocoDia, "\"tempmin\":"))
            );

            clima.setUmidade(
                    Double.parseDouble(extrair(blocoDia, "\"humidity\":"))
            );

            clima.setCondicao(extrair(blocoDia, "\"conditions\":\""));

            clima.setPrecipitacao(
                    Double.parseDouble(extrair(blocoDia, "\"precip\":"))
            );

            clima.setVelocidadeVento(
                    Double.parseDouble(extrair(blocoDia, "\"windspeed\":"))
            );

            clima.setDirecaoVento(
                    Double.parseDouble(extrair(blocoDia, "\"winddir\":"))
            );

            return clima;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    private String extrair(String json, String chave) {

        int inicio = json.indexOf(chave);
        if (inicio == -1) return "0";

        inicio += chave.length();

        int fimVirgula = json.indexOf(",", inicio);
        int fimChave = json.indexOf("}", inicio);

        int fim;

        if (fimVirgula == -1) fim = fimChave;
        else if (fimChave == -1) fim = fimVirgula;
        else fim = Math.min(fimVirgula, fimChave);

        return json.substring(inicio, fim)
                .replace("\"", "")
                .trim();
    }


    private String extrairBlocoDays(String json) {

        int inicioArray = json.indexOf("\"days\":[");
        if (inicioArray == -1) return json;

        int inicioObj = json.indexOf("{", inicioArray);
        int fimObj = json.indexOf("}", inicioObj);

        return json.substring(inicioObj, fimObj + 1);
    }
}