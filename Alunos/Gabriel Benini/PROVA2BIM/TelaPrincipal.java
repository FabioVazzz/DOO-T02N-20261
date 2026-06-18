import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class TelaPrincipal extends JFrame {

    private Usuario usuario;
    private GerenciadorSeries gerenciador;
    private ApiTvMaze api;

    private JTextField campoBusca;
    private JList<Serie> jListResultados;
    private DefaultListModel<Serie> modeloResultados;
    private JLabel lblStatus;
    private JLabel lblBemVindo;
    private JButton btnBuscar;

    public TelaPrincipal(Usuario usuario, GerenciadorSeries gerenciador) {
        this.usuario = usuario;
        this.gerenciador = gerenciador;
        this.api = new ApiTvMaze();

        configurarJanela();
        construirInterface();
        setVisible(true);
    }

    private void configurarJanela() {
        setTitle("SeriesTracker - Acompanhe suas séries favoritas");
        setSize(820, 620);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(700, 500));

        getContentPane().setBackground(Cores.FUNDO_ESCURO);
    }

    private void construirInterface() {
        setLayout(new BorderLayout(0, 0));

        JPanel barraLateral = construirBarraLateral();
        add(barraLateral, BorderLayout.WEST);

        JPanel areaPrincipal = construirAreaPrincipal();
        add(areaPrincipal, BorderLayout.CENTER);

        JPanel barraStatus = construirBarraStatus();
        add(barraStatus, BorderLayout.SOUTH);
    }

    private JPanel construirBarraLateral() {

        JPanel barra = new JPanel();
        barra.setLayout(new BoxLayout(barra, BoxLayout.Y_AXIS));
        barra.setBackground(Cores.FUNDO_PAINEL);
        barra.setPreferredSize(new Dimension(200, 0));
        barra.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Cores.BORDA));

        JPanel painelLogo = new JPanel(new FlowLayout(FlowLayout.CENTER));
        painelLogo.setBackground(Cores.FUNDO_PAINEL);
        painelLogo.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));

        JLabel lblNomeSistema = new JLabel("SeriesTracker");
        lblNomeSistema.setFont(Cores.FONTE_SUBTITULO);
        lblNomeSistema.setForeground(Cores.DESTAQUE);

        JPanel innerLogo = new JPanel();
        innerLogo.setLayout(new BoxLayout(innerLogo, BoxLayout.Y_AXIS));
        innerLogo.setBackground(Cores.FUNDO_PAINEL);
        lblNomeSistema.setAlignmentX(Component.CENTER_ALIGNMENT);
        innerLogo.add(Box.createVerticalStrut(5));
        innerLogo.add(lblNomeSistema);
        painelLogo.add(innerLogo);

        barra.add(painelLogo);

        lblBemVindo = new JLabel("Olá, " + usuario.getNome() + "!", SwingConstants.CENTER);
        lblBemVindo.setFont(Cores.FONTE_PEQUENA);
        lblBemVindo.setForeground(Cores.TEXTO_SECUNDARIO);
        lblBemVindo.setAlignmentX(Component.CENTER_ALIGNMENT);
        barra.add(lblBemVindo);
        barra.add(Box.createVerticalStrut(20));

        JButton btnFavoritos = criarBotaoMenu("Favoritos");
        JButton btnAssistidas = criarBotaoMenu("Já Assistidas");
        JButton btnQuerAssistir = criarBotaoMenu("Quero Assistir");

        btnFavoritos.addActionListener(e -> abrirLista("favoritos"));
        btnAssistidas.addActionListener(e -> abrirLista("assistidas"));
        btnQuerAssistir.addActionListener(e -> abrirLista("querAssistir"));

        barra.add(btnFavoritos);
        barra.add(Box.createVerticalStrut(5));
        barra.add(btnAssistidas);
        barra.add(Box.createVerticalStrut(5));
        barra.add(btnQuerAssistir);

        barra.add(Box.createVerticalGlue());

        JLabel lblVersao = new JLabel("v1.0 - TVMaze API", SwingConstants.CENTER);
        lblVersao.setFont(Cores.FONTE_PEQUENA);
        lblVersao.setForeground(Cores.TEXTO_SECUNDARIO);
        lblVersao.setAlignmentX(Component.CENTER_ALIGNMENT);
        barra.add(lblVersao);
        barra.add(Box.createVerticalStrut(15));

        return barra;
    }

    private JButton criarBotaoMenu(String texto) {
        JButton btn = new JButton(texto);
        btn.setFont(Cores.FONTE_NORMAL);
        btn.setForeground(Cores.TEXTO_PRINCIPAL);
        btn.setBackground(Cores.FUNDO_PAINEL);
        btn.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);

        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(Cores.FUNDO_CARD);
                btn.setForeground(Cores.DESTAQUE);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(Cores.FUNDO_PAINEL);
                btn.setForeground(Cores.TEXTO_PRINCIPAL);
            }
        });

        return btn;
    }

    private JPanel construirAreaPrincipal() {
        JPanel area = new JPanel(new BorderLayout(0, 10));
        area.setBackground(Cores.FUNDO_ESCURO);
        area.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

        JPanel painelBusca = new JPanel(new BorderLayout(10, 0));
        painelBusca.setBackground(Cores.FUNDO_ESCURO);

        JLabel lblBusca = ComponentesUI.criarTitulo("Buscar Séries");
        lblBusca.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        campoBusca = ComponentesUI.criarCampoTexto("Digite o nome de uma série...");
        btnBuscar = ComponentesUI.criarBotao("Buscar", true);
        btnBuscar.setPreferredSize(new Dimension(110, 36));

        JPanel linhaBusca = new JPanel(new BorderLayout(8, 0));
        linhaBusca.setBackground(Cores.FUNDO_ESCURO);
        linhaBusca.add(campoBusca, BorderLayout.CENTER);
        linhaBusca.add(btnBuscar, BorderLayout.EAST);

        painelBusca.add(lblBusca, BorderLayout.NORTH);
        painelBusca.add(linhaBusca, BorderLayout.CENTER);

        area.add(painelBusca, BorderLayout.NORTH);

        modeloResultados = new DefaultListModel<>();
        jListResultados = new JList<>(modeloResultados);
        ComponentesUI.estilizarLista(jListResultados);

        jListResultados.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    abrirDetalhesSerie();
                }
            }
        });

        JScrollPane scroll = new JScrollPane(jListResultados);
        ComponentesUI.estilizarScroll(scroll);

        JPanel painelResultados = new JPanel(new BorderLayout(0, 8));
        painelResultados.setBackground(Cores.FUNDO_ESCURO);

        JLabel lblResultados = ComponentesUI.criarLabel("Resultados da Busca (duplo clique para ver detalhes)");
        lblResultados.setForeground(Cores.TEXTO_SECUNDARIO);
        painelResultados.add(lblResultados, BorderLayout.NORTH);
        painelResultados.add(scroll, BorderLayout.CENTER);

        area.add(painelResultados, BorderLayout.CENTER);

        JPanel rodapeArea = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rodapeArea.setBackground(Cores.FUNDO_ESCURO);
        JButton btnDetalhes = ComponentesUI.criarBotao("Ver Detalhes da Selecionada", true);
        btnDetalhes.addActionListener(e -> abrirDetalhesSerie());
        rodapeArea.add(btnDetalhes);
        area.add(rodapeArea, BorderLayout.SOUTH);

        btnBuscar.addActionListener(e -> realizarBusca());

        campoBusca.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    realizarBusca();
                }
            }
        });

        return area;
    }

    private JPanel construirBarraStatus() {
        JPanel barra = new JPanel(new BorderLayout());
        barra.setBackground(Cores.FUNDO_PAINEL);
        barra.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Cores.BORDA));
        barra.setPreferredSize(new Dimension(0, 28));

        lblStatus = new JLabel("  Pronto. Busque uma série para começar.");
        lblStatus.setFont(Cores.FONTE_PEQUENA);
        lblStatus.setForeground(Cores.TEXTO_SECUNDARIO);
        barra.add(lblStatus, BorderLayout.WEST);

        return barra;
    }

    private void realizarBusca() {
        String termoBusca = campoBusca.getText();

        if (termoBusca == null || termoBusca.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Por favor, digite o nome de uma série para buscar.",
                    "Campo vazio", JOptionPane.WARNING_MESSAGE);
            campoBusca.requestFocus();
            return;
        }

        btnBuscar.setEnabled(false);
        btnBuscar.setText("Buscando...");
        atualizarStatus("Buscando por \"" + termoBusca.trim() + "\"...");
        modeloResultados.clear();

        SwingWorker<List<Serie>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<Serie> doInBackground() throws Exception {
                return api.buscarSeries(termoBusca.trim());
            }

            @Override
            protected void done() {
                try {
                    List<Serie> resultados = get();

                    if (resultados == null || resultados.isEmpty()) {
                        JOptionPane.showMessageDialog(TelaPrincipal.this,
                                "Nenhuma série encontrada para \"" + termoBusca.trim() + "\".\n" +
                                        "Tente um nome diferente ou verifique a ortografia.",
                                "Sem resultados", JOptionPane.INFORMATION_MESSAGE);
                        atualizarStatus("Nenhum resultado encontrado.");
                    } else {
                        for (Serie s : resultados) {
                            if (s != null) modeloResultados.addElement(s);
                        }
                        atualizarStatus(resultados.size() + " série(s) encontrada(s). Duplo clique para ver detalhes.");
                    }

                } catch (Exception e) {

                    String mensagem = e.getCause() != null ? e.getCause().getMessage() : e.getMessage();
                    JOptionPane.showMessageDialog(TelaPrincipal.this,
                            "Erro ao buscar séries:\n" + mensagem,
                            "Erro de comunicação", JOptionPane.ERROR_MESSAGE);
                    atualizarStatus("Erro na busca. Verifique sua conexão com a internet.");
                } finally {

                    btnBuscar.setEnabled(true);
                    btnBuscar.setText("Buscar");
                }
            }
        };

        worker.execute();
    }

    private void abrirDetalhesSerie() {
        Serie selecionada = jListResultados.getSelectedValue();
        if (selecionada == null) {
            JOptionPane.showMessageDialog(this,
                    "Selecione uma série na lista para ver os detalhes.",
                    "Nenhuma série selecionada", JOptionPane.WARNING_MESSAGE);
            return;
        }
        new TelaDetalhes(this, selecionada, gerenciador);
    }

    private void abrirLista(String tipoLista) {
        new TelaLista(this, gerenciador, tipoLista);
    }

    private void atualizarStatus(String mensagem) {
        lblStatus.setText("  " + mensagem);
    }
}