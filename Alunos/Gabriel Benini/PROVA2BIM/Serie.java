import java.io.Serializable;

public class Serie implements Serializable {

    private int id;
    private String nome;
    private String idioma;
    private String generos;
    private double nota;
    private String estado;
    private String dataEstreia;
    private String dataTermino;
    private String emissora;
    private String resumo;
    private String urlImagem;

    public Serie() {
    }

    public Serie(int id, String nome, String idioma, String generos,
                 double nota, String estado, String dataEstreia,
                 String dataTermino, String emissora, String resumo, String urlImagem) {
        this.id = id;
        this.nome = nome;
        this.idioma = idioma;
        this.generos = generos;
        this.nota = nota;
        this.estado = estado;
        this.dataEstreia = dataEstreia;
        this.dataTermino = dataTermino;
        this.emissora = emissora;
        this.resumo = resumo;
        this.urlImagem = urlImagem;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNome() { return nome != null ? nome : "Informação não disponível"; }
    public void setNome(String nome) { this.nome = nome; }

    public String getIdioma() { return idioma != null ? idioma : "Informação não disponível"; }
    public void setIdioma(String idioma) { this.idioma = idioma; }

    public String getGeneros() { return generos != null && !generos.isEmpty() ? generos : "Informação não disponível"; }
    public void setGeneros(String generos) { this.generos = generos; }

    public double getNota() { return nota; }
    public void setNota(double nota) { this.nota = nota; }

    public String getNotaFormatada() {
        if (nota <= 0) return "Sem avaliação";
        return String.format("%.1f / 10", nota);
    }

    public String getEstado() { return estado != null ? estado : "Informação não disponível"; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getDataEstreia() { return dataEstreia != null ? dataEstreia : "Informação não disponível"; }
    public void setDataEstreia(String dataEstreia) { this.dataEstreia = dataEstreia; }

    public String getDataTermino() { return dataTermino != null ? dataTermino : "Informação não disponível"; }
    public void setDataTermino(String dataTermino) { this.dataTermino = dataTermino; }

    public String getEmissora() { return emissora != null ? emissora : "Informação não disponível"; }
    public void setEmissora(String emissora) { this.emissora = emissora; }

    public String getResumo() { return resumo != null && !resumo.isEmpty() ? resumo : "Resumo não disponível."; }
    public void setResumo(String resumo) { this.resumo = resumo; }

    public String getUrlImagem() { return urlImagem; }
    public void setUrlImagem(String urlImagem) { this.urlImagem = urlImagem; }

    @Override
    public String toString() {
        return nome != null ? nome : "Série sem nome";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Serie outra = (Serie) obj;
        return this.id == outra.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}