package util;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import model.Serie;
import model.Usuario;

public class JsonUtil {

    private static final String ARQUIVO = "dados.json";

    public static void salvar(
            Usuario usuario,
            List<Serie> favoritos,
            List<Serie> assistidas,
            List<Serie> desejo) {

        try (PrintWriter writer = new PrintWriter(new FileWriter(ARQUIVO))) {

            writer.println("{");

            writer.println("\"usuario\":\"" + usuario.getNome() + "\",");

            escreverLista(writer, "favoritos", favoritos);
            writer.println(",");

            escreverLista(writer, "assistidas", assistidas);
            writer.println(",");

            escreverLista(writer, "desejo", desejo);

            writer.println("}");

        } catch (IOException e) {

            e.printStackTrace();

        }

    }

    private static void escreverLista(
            PrintWriter writer,
            String nomeLista,
            List<Serie> lista) {

        writer.println("\"" + nomeLista + "\":[");

        for (int i = 0; i < lista.size(); i++) {

            Serie s = lista.get(i);

            writer.print("{");
            writer.print("\"nome\":\"" + s.getNome() + "\",");
            writer.print("\"idioma\":\"" + s.getIdioma() + "\",");
            writer.print("\"generos\":\"" + s.getGeneros() + "\",");
            writer.print("\"nota\":" + s.getNota() + ",");
            writer.print("\"estado\":\"" + s.getEstado() + "\",");
            writer.print("\"estreia\":\"" + s.getDataEstreia() + "\",");
            writer.print("\"termino\":\"" + s.getDataTermino() + "\",");
            writer.print("\"emissora\":\"" + s.getEmissora() + "\"");
            writer.print("}");

            if (i < lista.size() - 1) {
                writer.println(",");
            }

        }

        writer.println("]");

    }

    public static Usuario carregarUsuario() {

        String json = lerArquivo();

        if (json == null) {
            return null;
        }

        String nome = valor(json, "usuario");

        if (nome.isEmpty()) {
            return null;
        }

        Usuario usuario = new Usuario(nome);

        usuario.getFavoritos().addAll(extrairLista(json, "favoritos"));
        usuario.getAssistidas().addAll(extrairLista(json, "assistidas"));
        usuario.getDesejaAssistir().addAll(extrairLista(json, "desejo"));

        return usuario;

    }

    private static List<Serie> extrairLista(String json, String chave) {

        List<Serie> lista = new ArrayList<>();

        String marcador = "\"" + chave + "\":[";

        int inicio = json.indexOf(marcador);

        if (inicio == -1) {
            return lista;
        }

        inicio += marcador.length();

        int fim = json.indexOf("]", inicio);

        if (fim == -1) {
            return lista;
        }

        String trecho = json.substring(inicio, fim).trim();

        if (trecho.isEmpty()) {
            return lista;
        }

        String[] partes = trecho.split("\\},\\s*\\{");

        for (String parte : partes) {

            parte = parte.replace("{", "").replace("}", "").trim();

            if (parte.isEmpty()) {
                continue;
            }

            lista.add(criarSerie(parte));

        }

        return lista;

    }

    private static Serie criarSerie(String json) {

        String nome = valor(json, "nome");
        String idioma = valor(json, "idioma");
        String generos = valor(json, "generos");
        double nota = valorNumero(json, "nota");
        String estado = valor(json, "estado");
        String estreia = valor(json, "estreia");
        String termino = valor(json, "termino");
        String emissora = valor(json, "emissora");

        return new Serie(
                nome, idioma, generos, nota,
                estado, estreia, termino, emissora);

    }

    private static String valor(String json, String chave) {

        String marcador = "\"" + chave + "\":\"";

        int inicio = json.indexOf(marcador);

        if (inicio == -1) {
            return "";
        }

        inicio += marcador.length();

        int fim = json.indexOf("\"", inicio);

        if (fim == -1) {
            return "";
        }

        return json.substring(inicio, fim);

    }

    private static double valorNumero(String json, String chave) {

        String marcador = "\"" + chave + "\":";

        int inicio = json.indexOf(marcador);

        if (inicio == -1) {
            return 0;
        }

        inicio += marcador.length();

        int fim = inicio;

        while (fim < json.length() &&
                (Character.isDigit(json.charAt(fim)) || json.charAt(fim) == '.')) {
            fim++;
        }

        try {
            return Double.parseDouble(json.substring(inicio, fim));
        } catch (Exception e) {
            return 0;
        }

    }

    public static String lerArquivo() {

        File arquivo = new File(ARQUIVO);

        if (!arquivo.exists()) {
            return null;
        }

        StringBuilder json = new StringBuilder();

        try (BufferedReader reader =
                     new BufferedReader(new FileReader(arquivo))) {

            String linha;

            while ((linha = reader.readLine()) != null) {

                json.append(linha);

            }

            return json.toString();

        } catch (IOException e) {

            return null;

        }

    }

}