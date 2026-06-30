package view;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import javax.swing.*;
import model.Serie;

public class ListaPanel extends JPanel {

    private DefaultListModel<String> model;

    private JList<String> lista;

    private JButton btnRemover;

    private JButton btnDetalhes;

    private JComboBox<String> comboOrdenacao;

    private List<Serie> series;

    public ListaPanel() {

        series = new ArrayList<>();

        setLayout(new BorderLayout(10, 10));

        JPanel painelSuperior = new JPanel(new FlowLayout(FlowLayout.LEFT));

        painelSuperior.add(new JLabel("Ordenar por:"));

        comboOrdenacao = new JComboBox<>();

        comboOrdenacao.addItem("Nome");
        comboOrdenacao.addItem("Nota");
        comboOrdenacao.addItem("Estado");
        comboOrdenacao.addItem("Data de estreia");

        painelSuperior.add(comboOrdenacao);

        add(painelSuperior, BorderLayout.NORTH);

        model = new DefaultListModel<>();

        lista = new JList<>(model);

        add(new JScrollPane(lista), BorderLayout.CENTER);

        JPanel painelInferior = new JPanel();

        btnDetalhes = new JButton("Ver detalhes");

        btnRemover = new JButton("Remover");

        painelInferior.add(btnDetalhes);
        painelInferior.add(btnRemover);

        add(painelInferior, BorderLayout.SOUTH);

        comboOrdenacao.addActionListener(e -> ordenar());

    }

    public void adicionarSerie(Serie serie) {

        if (series.contains(serie)) {
            return;
        }

        series.add(serie);

        ordenar();

    }

    public void removerSerieSelecionada() {

        int indice = lista.getSelectedIndex();

        if (indice == -1) {
            return;
        }

        series.remove(indice);

        atualizarLista();

    }

    private void ordenar() {

        String opcao = (String) comboOrdenacao.getSelectedItem();

        if (opcao == null) {
            return;
        }

        switch (opcao) {

            case "Nome":

                series.sort(Comparator.comparing(Serie::getNome));

                break;

            case "Nota":

                series.sort(Comparator.comparing(Serie::getNota).reversed());

                break;

            case "Estado":

                series.sort(Comparator.comparing(Serie::getEstado));

                break;

            case "Data de estreia":

                series.sort(Comparator.comparing(Serie::getDataEstreia));

                break;

        }

        atualizarLista();

    }

    private void atualizarLista() {

        model.clear();

        for (Serie serie : series) {

            model.addElement(serie.getNome());

        }

    }

    public void mostrarDetalhes(Component parent) {

        Serie serie = getSerieSelecionada();

        if (serie == null) {

            JOptionPane.showMessageDialog(
                    parent,
                    "Selecione uma série.");

            return;

        }

        String mensagem =
                "Nome: " + serie.getNome() +
                "\nIdioma: " + serie.getIdioma() +
                "\nGêneros: " + serie.getGeneros() +
                "\nNota: " + serie.getNota() +
                "\nEstado: " + serie.getEstado() +
                "\nEstreia: " + serie.getDataEstreia() +
                "\nTérmino: " + serie.getDataTermino() +
                "\nEmissora: " + serie.getEmissora();

        JOptionPane.showMessageDialog(
                parent,
                mensagem,
                "Detalhes da Série",
                JOptionPane.INFORMATION_MESSAGE);

    }

    public Serie getSerieSelecionada() {

        int indice = lista.getSelectedIndex();

        if (indice == -1) {
            return null;
        }

        return series.get(indice);

    }

    public JButton getBtnRemover() {
        return btnRemover;
    }

    public JButton getBtnDetalhes() {
        return btnDetalhes;
    }

    public JComboBox<String> getComboOrdenacao() {
        return comboOrdenacao;
    }

    public List<Serie> getSeries() {
        return series;
    }

}