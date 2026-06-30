package view;

import exception.ApiException;
import java.awt.*;
import javax.swing.*;
import model.Serie;
import model.Usuario;
import service.TvMazeService;
import util.JsonUtil;

public class MainFrame extends JFrame {

    private Usuario usuario;

    private JLabel lblUsuario;
    private JLabel lblStatus;

    private BuscaPanel buscaPanel;

    private ListaPanel favoritosPanel;
    private ListaPanel assistidasPanel;
    private ListaPanel desejoPanel;

    private TvMazeService service;

    public MainFrame(Usuario usuario) {

        this.usuario = usuario;

        service = new TvMazeService();

        setTitle("📺 Minhas Séries de TV");
        setSize(900, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        inicializarComponentes();

        configurarEventos();

        setVisible(true);
    }

    private void inicializarComponentes() {

        setLayout(new BorderLayout());

        criarMenu();

        JPanel painelSuperior = new JPanel(new BorderLayout());

        lblUsuario = new JLabel("👤 Usuário: " + usuario.getNome());
        lblUsuario.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        painelSuperior.add(lblUsuario, BorderLayout.WEST);

        add(painelSuperior, BorderLayout.NORTH);

        JTabbedPane abas = new JTabbedPane();

        buscaPanel = new BuscaPanel();

        favoritosPanel = new ListaPanel();
        assistidasPanel = new ListaPanel();
        desejoPanel = new ListaPanel();

        abas.addTab("🔎 Buscar", buscaPanel);
        abas.addTab("❤ Favoritos", favoritosPanel);
        abas.addTab("✔ Assistidas", assistidasPanel);
        abas.addTab("➕ Desejo assistir", desejoPanel);

        carregarListasSalvas();

        add(abas, BorderLayout.CENTER);

        lblStatus = new JLabel("Sistema pronto.");
        lblStatus.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));

        add(lblStatus, BorderLayout.SOUTH);

    }

    private void carregarListasSalvas() {

        for (Serie serie : usuario.getFavoritos()) {
            favoritosPanel.adicionarSerie(serie);
        }

        for (Serie serie : usuario.getAssistidas()) {
            assistidasPanel.adicionarSerie(serie);
        }

        for (Serie serie : usuario.getDesejaAssistir()) {
            desejoPanel.adicionarSerie(serie);
        }

    }

    private void configurarEventos() {

        buscaPanel.getBtnPesquisar().addActionListener(e -> pesquisarSerie());

        buscaPanel.getBtnFavorito().addActionListener(e -> adicionarFavorito());

        buscaPanel.getBtnAssistida().addActionListener(e -> adicionarAssistida());

        buscaPanel.getBtnDesejo().addActionListener(e -> adicionarDesejo());

        favoritosPanel.getBtnRemover().addActionListener(e -> {

            favoritosPanel.removerSerieSelecionada();

            atualizarStatus("Série removida dos favoritos.");

        });

        assistidasPanel.getBtnRemover().addActionListener(e -> {

            assistidasPanel.removerSerieSelecionada();

            atualizarStatus("Série removida da lista de assistidas.");

        });

        desejoPanel.getBtnRemover().addActionListener(e -> {

            desejoPanel.removerSerieSelecionada();

            atualizarStatus("Série removida da lista de desejo.");

        });

        favoritosPanel.getBtnDetalhes().addActionListener(e ->
                favoritosPanel.mostrarDetalhes(this));

        assistidasPanel.getBtnDetalhes().addActionListener(e ->
                assistidasPanel.mostrarDetalhes(this));

        desejoPanel.getBtnDetalhes().addActionListener(e ->
                desejoPanel.mostrarDetalhes(this));

    }

    private void pesquisarSerie() {

        try {

            String nome = buscaPanel.getTextoPesquisa();

            Serie serie = service.pesquisarSerie(nome);

            buscaPanel.mostrarSerie(serie);

            atualizarStatus("Série encontrada com sucesso.");

        } catch (ApiException e) {

            buscaPanel.limparInformacoes();

            atualizarStatus("Erro na pesquisa.");

            JOptionPane.showMessageDialog(
                    this,
                    e.getMessage(),
                    "Aviso",
                    JOptionPane.WARNING_MESSAGE);

        } catch (Exception e) {

            buscaPanel.limparInformacoes();

            atualizarStatus("Erro inesperado.");

            JOptionPane.showMessageDialog(
                    this,
                    "Ocorreu um erro inesperado.",
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);

        }

    }

    private void adicionarFavorito() {

        Serie serie = buscaPanel.getSerieAtual();

        if (serie == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Pesquise uma série primeiro.");

            return;

        }

        favoritosPanel.adicionarSerie(serie);

        atualizarStatus("Série adicionada aos favoritos.");

    }

    private void adicionarAssistida() {

        Serie serie = buscaPanel.getSerieAtual();

        if (serie == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Pesquise uma série primeiro.");

            return;

        }

        assistidasPanel.adicionarSerie(serie);

        atualizarStatus("Série adicionada à lista de assistidas.");

    }

    private void adicionarDesejo() {

        Serie serie = buscaPanel.getSerieAtual();

        if (serie == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Pesquise uma série primeiro.");

            return;

        }

        desejoPanel.adicionarSerie(serie);

        atualizarStatus("Série adicionada à lista de desejo.");

    }

    private void criarMenu() {

        JMenuBar menuBar = new JMenuBar();

        JMenu menuArquivo = new JMenu("Arquivo");

        JMenuItem itemSalvar = new JMenuItem("💾 Salvar");

        JMenuItem itemSair = new JMenuItem("❌ Sair");

        itemSalvar.addActionListener(e -> {

            usuario.getFavoritos().clear();
            usuario.getFavoritos().addAll(favoritosPanel.getSeries());

            usuario.getAssistidas().clear();
            usuario.getAssistidas().addAll(assistidasPanel.getSeries());

            usuario.getDesejaAssistir().clear();
            usuario.getDesejaAssistir().addAll(desejoPanel.getSeries());

            JsonUtil.salvar(
                    usuario,
                    usuario.getFavoritos(),
                    usuario.getAssistidas(),
                    usuario.getDesejaAssistir());

            JOptionPane.showMessageDialog(
                    this,
                    "Dados salvos com sucesso!");

            atualizarStatus("Dados salvos.");

        });

        itemSair.addActionListener(e -> dispose());

        menuArquivo.add(itemSalvar);
        menuArquivo.addSeparator();
        menuArquivo.add(itemSair);

        JMenu menuAjuda = new JMenu("Ajuda");

        JMenuItem itemSobre = new JMenuItem("ℹ Sobre");

        itemSobre.addActionListener(e ->
                JOptionPane.showMessageDialog(
                        this,
                        """
                        Minhas Séries de TV

                        Desenvolvimento Orientado a Objetos

                        API utilizada:
                        TVMaze

                        Autor:
                        Gabriel Mesomo
                        """,
                        "Sobre",
                        JOptionPane.INFORMATION_MESSAGE));

        menuAjuda.add(itemSobre);

        menuBar.add(menuArquivo);
        menuBar.add(menuAjuda);

        setJMenuBar(menuBar);

    }

    public void atualizarStatus(String texto) {

        lblStatus.setText(texto);

    }

}