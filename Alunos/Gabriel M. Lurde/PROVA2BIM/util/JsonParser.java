package util;

import exception.ApiException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import model.Serie;

public class JsonParser {

    public static Serie parse(String json)
            throws ApiException {

        try {

            if (json == null || json.equals("[]")) {
                throw new ApiException("Série não encontrada.");
            }

            String blocoShow = extrairBlocoShow(json);

            String nome = texto(blocoShow, "name");
            String idioma = texto(blocoShow, "language");
            String estado = texto(blocoShow, "status");
            String estreia = texto(blocoShow, "premiered");
            String termino = texto(blocoShow, "ended");
            String generos = lista(blocoShow, "genres");
            double nota = numero(blocoShow, "average");
            String emissora = texto(blocoShow, "network", "name");

            return new Serie(
                    nome,
                    idioma,
                    generos,
                    nota,
                    estado,
                    estreia,
                    termino,
                    emissora
            );

        } catch (ApiException e) {

            throw e;

        } catch (Exception e) {

            throw new ApiException(
                    "Erro ao interpretar os dados da API.");

        }

    }

    private static String extrairBlocoShow(String json) {

        int inicio = json.indexOf("\"show\"");

        if (inicio == -1) {
            return json;
        }

        return json.substring(inicio);

    }

    private static String texto(String json, String chave) {

        Pattern p = Pattern.compile(
                "\"" + chave + "\"\\s*:\\s*\"([^\"]*)\"");

        Matcher m = p.matcher(json);

        if (m.find()) {
            return m.group(1);
        }

        return "";

    }

    private static String texto(
            String json,
            String objeto,
            String chave) {

        Pattern p = Pattern.compile(
                "\"" + objeto + "\".*?\"" +
                        chave +
                        "\"\\s*:\\s*\"([^\"]*)\"",
                Pattern.DOTALL);

        Matcher m = p.matcher(json);

        if (m.find()) {
            return m.group(1);
        }

        return "";

    }

    private static double numero(String json, String chave) {

        Pattern p = Pattern.compile(
                "\"" + chave + "\"\\s*:\\s*([0-9.]+)");

        Matcher m = p.matcher(json);

        if (m.find()) {

            return Double.parseDouble(
                    m.group(1));

        }

        return 0;

    }

    private static String lista(String json, String chave) {

        Pattern p = Pattern.compile(
                "\"" + chave + "\"\\s*:\\s*\\[(.*?)\\]");

        Matcher m = p.matcher(json);

        if (m.find()) {

            return m.group(1)
                    .replace("\"", "");

        }

        return "";

    }

}