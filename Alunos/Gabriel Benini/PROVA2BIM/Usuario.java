import java.util.ArrayList;
import java.util.List;

public class Usuario {

    private String nome;

    private List<Serie> favoritos;
    private List<Serie> assistidas;
    private List<Serie> querAssistir;
    private String ordemFavoritos;
    private String ordemAssistidas;
    private String ordemQuerAssistir;

    public Usuario() {
        this.favoritos = new ArrayList<>();
        this.assistidas = new ArrayList<>();
        this.querAssistir = new ArrayList<>();
    }

    public Usuario(String nome) {
        this.nome = nome;
        this.favoritos = new ArrayList<>();
        this.assistidas = new ArrayList<>();
        this.querAssistir = new ArrayList<>();
    }

    public void inicializarListas() {
        if (favoritos == null) favoritos = new ArrayList<>();
        if (assistidas == null) assistidas = new ArrayList<>();
        if (querAssistir == null) querAssistir = new ArrayList<>();
    }

    public String getNome() { return nome != null ? nome : "Usuário"; }
    public void setNome(String nome) { this.nome = nome; }

    public List<Serie> getFavoritos() {
        if (favoritos == null) favoritos = new ArrayList<>();
        return favoritos;
    }
    public void setFavoritos(List<Serie> favoritos) { this.favoritos = favoritos; }

    public List<Serie> getAssistidas() {
        if (assistidas == null) assistidas = new ArrayList<>();
        return assistidas;
    }
    public void setAssistidas(List<Serie> assistidas) { this.assistidas = assistidas; }

    public List<Serie> getQuerAssistir() {
        if (querAssistir == null) querAssistir = new ArrayList<>();
        return querAssistir;
    }
    public void setQuerAssistir(List<Serie> querAssistir) { this.querAssistir = querAssistir; }

    public String getOrdemFavoritos() {
        return ordemFavoritos != null ? ordemFavoritos : "Nome (A-Z)";
    }
    public void setOrdemFavoritos(String ordemFavoritos) { this.ordemFavoritos = ordemFavoritos; }

    public String getOrdemAssistidas() {
        return ordemAssistidas != null ? ordemAssistidas : "Nome (A-Z)";
    }
    public void setOrdemAssistidas(String ordemAssistidas) { this.ordemAssistidas = ordemAssistidas; }

    public String getOrdemQuerAssistir() {
        return ordemQuerAssistir != null ? ordemQuerAssistir : "Nome (A-Z)";
    }
    public void setOrdemQuerAssistir(String ordemQuerAssistir) { this.ordemQuerAssistir = ordemQuerAssistir; }

}