import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class GerenciadorSeries {

    private Usuario usuario;
    private PersistenciaJson persistencia;

    public GerenciadorSeries(Usuario usuario, PersistenciaJson persistencia) {
        this.usuario = usuario;
        this.persistencia = persistencia;
    }

    public boolean adicionarFavorito(Serie serie) {
        if (serie == null) return false;
        if (usuario.getFavoritos().contains(serie)) {
            return false;
        }
        usuario.getFavoritos().add(serie);
        persistencia.salvarUsuario(usuario);
        return true;
    }

    public boolean removerFavorito(Serie serie) {
        if (serie == null) return false;
        boolean removeu = usuario.getFavoritos().remove(serie);
        if (removeu) persistencia.salvarUsuario(usuario);
        return removeu;
    }

    public List<Serie> getFavoritos() {
        return new ArrayList<>(usuario.getFavoritos());
    }

    public boolean adicionarAssistida(Serie serie) {
        if (serie == null) return false;
        if (usuario.getAssistidas().contains(serie)) {
            return false;
        }
        usuario.getAssistidas().add(serie);
        persistencia.salvarUsuario(usuario);
        return true;
    }

    public boolean removerAssistida(Serie serie) {
        if (serie == null) return false;
        boolean removeu = usuario.getAssistidas().remove(serie);
        if (removeu) persistencia.salvarUsuario(usuario);
        return removeu;
    }

    public List<Serie> getAssistidas() {
        return new ArrayList<>(usuario.getAssistidas());
    }

    public boolean adicionarQuerAssistir(Serie serie) {
        if (serie == null) return false;
        if (usuario.getQuerAssistir().contains(serie)) {
            return false;
        }
        usuario.getQuerAssistir().add(serie);
        persistencia.salvarUsuario(usuario);
        return true;
    }

    public boolean removerQuerAssistir(Serie serie) {
        if (serie == null) return false;
        boolean removeu = usuario.getQuerAssistir().remove(serie);
        if (removeu) persistencia.salvarUsuario(usuario);
        return removeu;
    }

    public List<Serie> getQuerAssistir() {
        return new ArrayList<>(usuario.getQuerAssistir());
    }

    public List<Serie> ordenarLista(List<Serie> lista, String criterio) {
        if (lista == null || lista.isEmpty()) return lista;

        List<Serie> copia = new ArrayList<>(lista);

        switch (criterio) {
            case "Nome (A-Z)":
                Collections.sort(copia, Comparator.comparing(
                        s -> s.getNome().toLowerCase()
                ));
                break;

            case "Nota (Maior-Menor)":
                copia.sort((a, b) -> Double.compare(b.getNota(), a.getNota()));
                break;

            case "Estado":
                Collections.sort(copia, Comparator.comparing(Serie::getEstado));
                break;

            case "Data de Estreia":
                Collections.sort(copia, Comparator.comparing(
                        s -> s.getDataEstreia()
                ));
                break;

            default:
                break;
        }

        return copia;
    }

    public static String[] getCriteriosOrdenacao() {
        return new String[]{"Nome (A-Z)", "Nota (Maior-Menor)", "Estado", "Data de Estreia"};
    }

    public Usuario getUsuario() { return usuario; }

    public void salvarDados() {
        persistencia.salvarUsuario(usuario);
    }

}