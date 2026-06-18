import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.util.List;

public class TelaLista extends JDialog {

    private GerenciadorSeries gerenciador;
    private String tipoLista;
    private JList<Serie> jListSeries;
    private DefaultListModel<Serie> modeloLista;
    private JComboBox<String> comboOrdem;

    public TelaLista(Frame pai, GerenciadorSeries gerenciador, String tipoLista) {
        super(pai, getTituloLista(tipoLista), true);
        this.gerenciador = gerenciador;
        this.tipoLista = tipoLista;

        configurarJanela();
        construirInterface();
        String ordemSalva = getOrdemSalva();
        comboOrdem.setSelectedItem(ordemSalva);
        carregarLista(ordemSalva);
        setVisible(true);
    }

    private static String getTituloLista(String tipo) {
        switch (tipo) {
            case "favoritos": return "Meus Favoritos";
            case "assistidas": return "Séries Assistidas";
            case "querAssistir": return "Quero Assistir";
            default: return "Lista de Séries";
        }
    }

    private void configurarJanela() {
        setSize(520, 500);
        setLocationRelativeTo(getOwner());
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        getContentPane().setBackground(Cores.FUNDO_ESCURO);
    }

    private void construirInterface() {
        setLayout(new BorderLayout(0, 0));

        JPanel cabecalho = new JPanel(new BorderLayout(10, 0));
        cabecalho.setBackground(Cores.FUNDO_PAINEL);
        cabecalho.setBorder(BorderFactory.createEmptyBorder(12, 15, 12, 15));

        JLabel lblTitulo = ComponentesUI.criarTitulo(getTituloLista(tipoLista));
        cabecalho.add(lblTitulo, BorderLayout.WEST);

        add(cabecalho, BorderLayout.NORTH);

        JPanel barraOrdem = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        barraOrdem.setBackground(Cores.FUNDO_ESCURO);

        JLabel lblOrdem = ComponentesUI.criarLabel("Ordenar por:");
        comboOrdem = new JComboBox<>(GerenciadorSeries.getCriteriosOrdenacao());
        ComponentesUI.estilizarComboBox(comboOrdem);
        comboOrdem.setPreferredSize(new Dimension(180, 30));

        JButton btnOrdenar = ComponentesUI.criarBotao("Aplicar", true);
        btnOrdenar.setPreferredSize(new Dimension(90, 30));
        btnOrdenar.addActionListener(e -> {
            String criterio = (String) comboOrdem.getSelectedItem();
            salvarOrdemUsuario(criterio);
            carregarLista(criterio);
        });

        barraOrdem.add(lblOrdem);
        barraOrdem.add(comboOrdem);
        barraOrdem.add(btnOrdenar);

        add(barraOrdem, BorderLayout.BEFORE_LINE_BEGINS);

        modeloLista = new DefaultListModel<>();
        jListSeries = new JList<>(modeloLista);
        ComponentesUI.estilizarLista(jListSeries);

        jListSeries.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) {
                    abrirDetalhes();
                }
            }
        });

        JScrollPane scroll = new JScrollPane(jListSeries);
        ComponentesUI.estilizarScroll(scroll);
        scroll.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));

        JPanel centro = new JPanel(new BorderLayout());
        centro.setBackground(Cores.FUNDO_ESCURO);
        centro.add(barraOrdem, BorderLayout.NORTH);
        centro.add(scroll, BorderLayout.CENTER);
        add(centro, BorderLayout.CENTER);

        JPanel rodape = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        rodape.setBackground(Cores.FUNDO_PAINEL);
        rodape.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Cores.BORDA));

        JButton btnDetalhes = ComponentesUI.criarBotao("Ver Detalhes", true);
        JButton btnRemover = ComponentesUI.criarBotao("Remover", false);
        JButton btnFechar = ComponentesUI.criarBotao("Fechar", false);

        btnDetalhes.addActionListener(e -> abrirDetalhes());

        btnRemover.addActionListener(e -> removerSerieSelecionada());

        btnFechar.addActionListener(e -> dispose());

        rodape.add(btnDetalhes);
        rodape.add(btnRemover);
        rodape.add(btnFechar);
        add(rodape, BorderLayout.SOUTH);
    }

    private String getOrdemSalva() {
        Usuario usuario = gerenciador.getUsuario();
        switch (tipoLista) {
            case "favoritos":    return usuario.getOrdemFavoritos();
            case "assistidas":   return usuario.getOrdemAssistidas();
            case "querAssistir": return usuario.getOrdemQuerAssistir();
            default:             return "Nome (A-Z)";
        }
    }

    private void salvarOrdemUsuario(String criterio) {
        Usuario usuario = gerenciador.getUsuario();
        switch (tipoLista) {
            case "favoritos":    usuario.setOrdemFavoritos(criterio); break;
            case "assistidas":   usuario.setOrdemAssistidas(criterio); break;
            case "querAssistir": usuario.setOrdemQuerAssistir(criterio); break;
        }
        gerenciador.salvarDados();
    }

    private void carregarLista(String criterioOrdem) {
        modeloLista.clear();

        List<Serie> series;
        switch (tipoLista) {
            case "favoritos": series = gerenciador.getFavoritos(); break;
            case "assistidas": series = gerenciador.getAssistidas(); break;
            case "querAssistir": series = gerenciador.getQuerAssistir(); break;
            default: return;
        }

        if (series.isEmpty()) {
            modeloLista.clear();
            return;
        }

        if (criterioOrdem != null && !criterioOrdem.isEmpty()) {
            series = gerenciador.ordenarLista(series, criterioOrdem);
        }

        for (Serie s : series) {
            if (s != null) {
                modeloLista.addElement(s);
            }
        }
    }

    private void abrirDetalhes() {
        Serie selecionada = jListSeries.getSelectedValue();
        if (selecionada == null) {
            JOptionPane.showMessageDialog(this,
                    "Selecione uma série para ver os detalhes.",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        new TelaDetalhes((Frame) getOwner(), selecionada, gerenciador);
        carregarLista((String) comboOrdem.getSelectedItem());
    }

    private void removerSerieSelecionada() {
        Serie selecionada = jListSeries.getSelectedValue();
        if (selecionada == null) {
            JOptionPane.showMessageDialog(this,
                    "Selecione uma série para remover.",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirmacao = JOptionPane.showConfirmDialog(this,
                "Remover \"" + selecionada.getNome() + "\" da lista?",
                "Confirmar remoção", JOptionPane.YES_NO_OPTION);

        if (confirmacao != JOptionPane.YES_OPTION) return;

        boolean removeu = false;
        switch (tipoLista) {
            case "favoritos": removeu = gerenciador.removerFavorito(selecionada); break;
            case "assistidas": removeu = gerenciador.removerAssistida(selecionada); break;
            case "querAssistir": removeu = gerenciador.removerQuerAssistir(selecionada); break;
        }

        if (removeu) {
            JOptionPane.showMessageDialog(this,
                    "\"" + selecionada.getNome() + "\" removida da lista.",
                    "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            carregarLista((String) comboOrdem.getSelectedItem());
        } else {
            JOptionPane.showMessageDialog(this,
                    "Não foi possível remover a série.",
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}