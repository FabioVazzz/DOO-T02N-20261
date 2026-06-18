package com.tvtracker.persistence;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.tvtracker.model.Dados;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Responsável por salvar e carregar os dados do usuário em JSON.
 *
 */
public class JsonPersistence {

    // Caminho completo para o arquivo data.json
    private final Path dataFile;

    // Gson é a biblioteca para converter objetos Java <-> JSON
    // setPrettyPrinting() formata o JSON com indentação (mais legível)
    private final Gson gson;

    /**
     * Construtor — define onde o arquivo será salvo e cria a pasta se necessário.
     */
    public JsonPersistence() {
        // Pega o diretório home do usuário atual (ex: C:\Users\João ou /home/joao)
        String home = System.getProperty("user.home");

        // Define a pasta oculta .tvtracker dentro do home
        Path dir = Paths.get(home, ".tvtracker");

        // Define o arquivo data.json dentro dessa pasta
        this.dataFile = dir.resolve("data.json");

        // Cria o Gson com formatação bonita (JSON identado)
        this.gson = new GsonBuilder().setPrettyPrinting().create();

        // Cria a pasta .tvtracker caso não exista ainda
        try {
            Files.createDirectories(dir);
        } catch (IOException e) {
            System.err.println("Nao foi possivel criar o diretorio de dados: " + e.getMessage());
        }
    }

    /**
     * Salva todos os dados do aplicativo no arquivo JSON.
     *
     * Converte o objeto AppData (que contém o perfil e as listas)
     * para texto JSON e grava no arquivo data.json.
     */
    public void save(Dados data) {
        // OutputStreamWriter com UTF-8 garante que acentos e caracteres especiais
        // sejam salvos corretamente no arquivo
        try (Writer writer = new OutputStreamWriter(
                new FileOutputStream(dataFile.toFile()), StandardCharsets.UTF_8)) {
            gson.toJson(data, writer); // Converte AppData para JSON e escreve no arquivo
        } catch (IOException e) {
            System.err.println("Erro ao salvar dados: " + e.getMessage());
        }
    }

    /**
     * Carrega os dados do arquivo JSON e os converte para um objeto AppData.
     *
     * Se o arquivo não existir (primeira execução), retorna um AppData vazio.
     * Se o arquivo estiver corrompido, também retorna um AppData vazio e loga o erro.
     */
    public Dados load() {
        // Se o arquivo ainda não existe, é a primeira execução — retorna dados vazios
        if (!Files.exists(dataFile)) {
            return new Dados();
        }

        // Lê o arquivo e converte o JSON de volta para um objeto AppData
        try (Reader reader = new InputStreamReader(
                new FileInputStream(dataFile.toFile()), StandardCharsets.UTF_8)) {
            Dados data = gson.fromJson(reader, Dados.class);

            // Se o JSON retornar nulo (arquivo vazio), retorna AppData vazio
            return data != null ? data : new Dados();

        } catch (IOException | JsonSyntaxException e) {
            // IOException: erro ao ler o arquivo
            // JsonSyntaxException: arquivo JSON malformado/corrompido
            System.err.println("Erro ao carregar dados: " + e.getMessage());
            return new Dados(); // Retorna vazio para não quebrar o programa
        }
    }
}
