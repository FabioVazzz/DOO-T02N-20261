import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class PersistenciaJson {

    private static final String ARQUIVO_DADOS = "dados_usuarios.json";
    private Gson gson;

    public PersistenciaJson() {
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }

    public List<Usuario> carregarTodosUsuarios() {
        File arquivo = new File(ARQUIVO_DADOS);

        if (!arquivo.exists() || arquivo.length() == 0) {
            return new ArrayList<>();
        }

        try {
            BufferedReader leitor = new BufferedReader(
                    new InputStreamReader(new FileInputStream(arquivo), StandardCharsets.UTF_8)
            );

            StringBuilder conteudo = new StringBuilder();
            String linha;
            while ((linha = leitor.readLine()) != null) {
                conteudo.append(linha);
            }
            leitor.close();

            String json = conteudo.toString().trim();

            if (json.isEmpty() || !json.startsWith("[")) {
                System.out.println("Arquivo de dados corrompido. Iniciando do zero.");
                return new ArrayList<>();
            }

            Type tipoLista = new TypeToken<List<Usuario>>(){}.getType();
            List<Usuario> usuarios = gson.fromJson(json, tipoLista);

            if (usuarios == null) return new ArrayList<>();

            for (Usuario u : usuarios) {
                if (u != null) u.inicializarListas();
            }

            return usuarios;

        } catch (JsonSyntaxException e) {
            System.out.println("Arquivo corrompido: " + e.getMessage());
            return new ArrayList<>();
        } catch (IOException e) {
            System.out.println("Erro ao ler arquivo: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public Usuario buscarUsuarioPorNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) return null;

        List<Usuario> todos = carregarTodosUsuarios();
        for (Usuario u : todos) {
            if (u != null && u.getNome() != null &&
                    u.getNome().trim().equalsIgnoreCase(nome.trim())) {
                return u;
            }
        }
        return null;
    }

    public void salvarUsuario(Usuario usuario) {
        if (usuario == null) return;

        List<Usuario> todos = carregarTodosUsuarios();

        boolean encontrou = false;
        for (int i = 0; i < todos.size(); i++) {
            Usuario u = todos.get(i);
            if (u != null && u.getNome() != null &&
                    u.getNome().trim().equalsIgnoreCase(usuario.getNome().trim())) {
                todos.set(i, usuario);
                encontrou = true;
                break;
            }
        }

        if (!encontrou) {
            todos.add(usuario);
        }

        try {
            String json = gson.toJson(todos);
            FileWriter escritor = new FileWriter(ARQUIVO_DADOS, StandardCharsets.UTF_8);
            escritor.write(json);
            escritor.close();
        } catch (IOException e) {
            System.out.println("Erro ao salvar dados: " + e.getMessage());
        }
    }

    public boolean existeDadosSalvos() {
        File arquivo = new File(ARQUIVO_DADOS);
        return arquivo.exists() && arquivo.length() > 0;
    }
}