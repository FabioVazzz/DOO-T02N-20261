package view;

import java.awt.*;
import javax.swing.*;
import model.Serie;

public class BuscaPanel extends JPanel {

    private JTextField txtPesquisa;
    private JButton btnPesquisar;

    private JLabel lblNome;
    private JLabel lblIdioma;
    private JLabel lblGeneros;
    private JLabel lblNota;
    private JLabel lblEstado;
    private JLabel lblEstreia;
    private JLabel lblTermino;
    private JLabel lblEmissora;

    private JButton btnFavorito;
    private JButton btnAssistida;
    private JButton btnDesejo;

    private Serie serieAtual;

    public BuscaPanel() {

        setLayout(new BorderLayout(10, 10));

        JPanel painelPesquisa = new JPanel(new FlowLayout(FlowLayout.LEFT));

        painelPesquisa.add(new JLabel("Pesquisar série:"));

        txtPesquisa = new JTextField(25);

        painelPesquisa.add(txtPesquisa);

        btnPesquisar = new JButton("🔎 Pesquisar");

        painelPesquisa.add(btnPesquisar);

        add(painelPesquisa, BorderLayout.NORTH);

        JPanel painelInformacoes = new JPanel(new GridLayout(8, 1, 5, 5));

        lblNome = new JLabel("Nome:");
        lblIdioma = new JLabel("Idioma:");
        lblGeneros = new JLabel("Gêneros:");
        lblNota = new JLabel("Nota:");
        lblEstado = new JLabel("Estado:");
        lblEstreia = new JLabel("Data de estreia:");
        lblTermino = new JLabel("Data de término:");
        lblEmissora = new JLabel("Emissora:");

        painelInformacoes.add(lblNome);
        painelInformacoes.add(lblIdioma);
        painelInformacoes.add(lblGeneros);
        painelInformacoes.add(lblNota);
        painelInformacoes.add(lblEstado);
        painelInformacoes.add(lblEstreia);
        painelInformacoes.add(lblTermino);
        painelInformacoes.add(lblEmissora);

        add(painelInformacoes, BorderLayout.CENTER);

        JPanel painelBotoes = new JPanel(new FlowLayout());

        btnFavorito = new JButton("❤ Favoritar");
        btnAssistida = new JButton("✔ Já assisti");
        btnDesejo = new JButton("➕ Quero assistir");

        painelBotoes.add(btnFavorito);
        painelBotoes.add(btnAssistida);
        painelBotoes.add(btnDesejo);

        add(painelBotoes, BorderLayout.SOUTH);

        limparInformacoes();
    }

    public void mostrarSerie(Serie serie) {

        serieAtual = serie;

        lblNome.setText("Nome: " + serie.getNome());
        lblIdioma.setText("Idioma: " + serie.getIdioma());
        lblGeneros.setText("Gêneros: " + serie.getGeneros());
        lblNota.setText("Nota: " + serie.getNota());
        lblEstado.setText("Estado: " + serie.getEstado());
        lblEstreia.setText("Data de estreia: " + serie.getDataEstreia());
        lblTermino.setText("Data de término: " + serie.getDataTermino());
        lblEmissora.setText("Emissora: " + serie.getEmissora());
    }

    public void limparInformacoes() {

        serieAtual = null;

        lblNome.setText("Nome:");
        lblIdioma.setText("Idioma:");
        lblGeneros.setText("Gêneros:");
        lblNota.setText("Nota:");
        lblEstado.setText("Estado:");
        lblEstreia.setText("Data de estreia:");
        lblTermino.setText("Data de término:");
        lblEmissora.setText("Emissora:");
    }

    public Serie getSerieAtual() {
        return serieAtual;
    }

    public String getTextoPesquisa() {
        return txtPesquisa.getText().trim();
    }

    public JButton getBtnPesquisar() {
        return btnPesquisar;
    }

    public JButton getBtnFavorito() {
        return btnFavorito;
    }

    public JButton getBtnAssistida() {
        return btnAssistida;
    }

    public JButton getBtnDesejo() {
        return btnDesejo;
    }
}