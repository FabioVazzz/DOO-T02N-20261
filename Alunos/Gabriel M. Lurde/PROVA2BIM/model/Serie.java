package model;

import java.util.Objects;

public class Serie {

    private String nome;
    private String idioma;
    private String generos;
    private double nota;
    private String estado;
    private String dataEstreia;
    private String dataTermino;
    private String emissora;

    public Serie(String nome,
                 String idioma,
                 String generos,
                 double nota,
                 String estado,
                 String dataEstreia,
                 String dataTermino,
                 String emissora) {

        this.nome = nome;
        this.idioma = idioma;
        this.generos = generos;
        this.nota = nota;
        this.estado = estado;
        this.dataEstreia = dataEstreia;
        this.dataTermino = dataTermino;
        this.emissora = emissora;
    }

    public String getNome() {
        return nome;
    }

    public String getIdioma() {
        return idioma;
    }

    public String getGeneros() {
        return generos;
    }

    public double getNota() {
        return nota;
    }

    public String getEstado() {
        return estado;
    }

    public String getDataEstreia() {
        return dataEstreia;
    }

    public String getDataTermino() {
        return dataTermino;
    }

    public String getEmissora() {
        return emissora;
    }

    @Override
    public String toString() {
        return nome;
    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj) {
            return true;
        }

        if (!(obj instanceof Serie)) {
            return false;
        }

        Serie outra = (Serie) obj;

        return Objects.equals(nome, outra.nome);

    }

    @Override
    public int hashCode() {

        return Objects.hash(nome);

    }

}