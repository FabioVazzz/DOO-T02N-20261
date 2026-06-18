package com.tvtracker.ui;

import com.tvtracker.model.Listas;
import com.tvtracker.service.SeriesService;

import javax.swing.*;
import java.awt.*;

/**
 * JANELA PRINCIPAL
 *
 * Contém:
 * - Barra de menus (Arquivo e Ajuda)
 * - Abas com: Busca, Favoritos, Assistidas, Para Assistir
 * - Barra de status inferior mostrando o nome do usuário
 */
public class MainFrame extends JFrame {

    private final SeriesService service;

    // Painéis das três listas
    private ListaPainel favPanel;
    private ListaPainel watchedPanel;
    private ListaPainel wantPanel;

    // Label na barra inferior que exibe o nome do usuário logado
    private JLabel statusLabel;

    /**
     * Construtor — configura a janela e constrói todos os componentes.
     */
    public MainFrame(SeriesService service) {
        this.service = service;

        setTitle(buildTitle());                            // "TV Series Tracker - NomeDoUsuario"
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);   // Fecha o programa ao fechar a janela
        setSize(950, 680);                                 // Tamanho inicial
        setMinimumSize(new Dimension(750, 520));           // Tamanho mínimo ao redimensionar
        setLocationRelativeTo(null);                       // Centraliza na tela

        buildMenuBar(); // Cria a barra de menus
        buildContent(); // Cria as abas e a barra de status
    }

    /**
     * Monta o título da janela incluindo o nome do usuário.
     */
    private String buildTitle() {
        String name = service.getUserProfile().getName();
        return "TV Series Tracker" + (name != null && !name.isEmpty() ? " - " + name : "");
    }

    /**
     * Cria a barra de menus com os menus "Arquivo" e "Ajuda".
     */
    private void buildMenuBar() {
        JMenuBar bar = new JMenuBar();

        // ===== MENU ARQUIVO =====
        JMenu fileMenu = new JMenu("Arquivo");

        // Item "Editar Perfil" — abre a janela de configuração do nome
        JMenuItem profileItem = new JMenuItem("Editar Perfil");
        profileItem.addActionListener(e -> {
            new Perfil(this, service).setVisible(true);

            // Atualiza o título e a barra de status com o novo nome
            setTitle(buildTitle());
            if (statusLabel != null) {
                statusLabel.setText("Usuario: " + service.getUserProfile().getName());
            }
        });

        // Item "Sair" — encerra o programa
        JMenuItem exitItem = new JMenuItem("Sair");
        exitItem.addActionListener(e -> System.exit(0));

        fileMenu.add(profileItem);
        fileMenu.addSeparator(); // Linha divisória entre os itens
        fileMenu.add(exitItem);

        // ===== MENU AJUDA =====
        JMenu helpMenu = new JMenu("Ajuda");

        // Item "Sobre" — exibe informações do sistema em um popup
        JMenuItem aboutItem = new JMenuItem("Sobre");
        aboutItem.addActionListener(e -> JOptionPane.showMessageDialog(this,
                "TV Series Tracker\n" +
                "API: TVMaze (tvmaze.com)\n" +
                "Desenvolvido com Java 21 + Swing\n\n" +
                "Funcionalidades:\n" +
                " - Busca de series pelo nome\n" +
                " - Listas: Favoritos, Assistidas, Para Assistir\n" +
                " - Ordenacao por nome, nota, estado e data de estreia\n" +
                " - Persistencia de dados em JSON",
                "Sobre o Sistema", JOptionPane.INFORMATION_MESSAGE));
        helpMenu.add(aboutItem);

        bar.add(fileMenu);
        bar.add(helpMenu);
        setJMenuBar(bar); // Aplica a barra de menus na janela
    }

    /**
     * Cria o conteúdo principal da janela:
     * - JTabbedPane com as quatro abas
     * - Barra de status na parte inferior
     */
    private void buildContent() {
        JTabbedPane tabs = new JTabbedPane();

        // Painel de busca — passamos um callback para ele atualizar as listas
        // quando o usuário adicionar uma série
        Buscar searchPanel = new Buscar(service);
        searchPanel.setOnListChanged(this::refreshLists); // Referência ao método refreshLists

        // Criação dos painéis de lista para cada tipo
        favPanel     = new ListaPainel(service, Listas.FAVORITES);
        watchedPanel = new ListaPainel(service, Listas.WATCHED);
        wantPanel    = new ListaPainel(service, Listas.WANT_TO_WATCH);

        // Adiciona as abas ao JTabbedPane
        tabs.addTab("Busca",         searchPanel);
        tabs.addTab("Favoritos",     favPanel);
        tabs.addTab("Assistidas",    watchedPanel);
        tabs.addTab("Para Assistir", wantPanel);

        // Quando o usuário troca de aba, atualiza a lista correspondente
        // para refletir qualquer adição feita na aba de Busca
        tabs.addChangeListener(e -> {
            int idx = tabs.getSelectedIndex();
            if (idx == 1) favPanel.refresh();
            else if (idx == 2) watchedPanel.refresh();
            else if (idx == 3) wantPanel.refresh();
        });

        // Adiciona as abas no centro da janela (layout BorderLayout padrão do JFrame)
        add(tabs, BorderLayout.CENTER);

        // ===== BARRA DE STATUS (parte inferior) =====
        JPanel statusBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 3));
        statusBar.setBorder(BorderFactory.createEtchedBorder()); // Borda discreta

        String name = service.getUserProfile().getName();
        statusLabel = new JLabel("Usuario: " + (name != null && !name.isEmpty() ? name : "—"));
        statusBar.add(statusLabel);

        add(statusBar, BorderLayout.SOUTH); // Posiciona na parte inferior da janela
    }

    /**
     * Atualiza todas as três listas de uma vez.
     * Chamado pelo SearchPanel via callback quando uma série é adicionada.
     */
    private void refreshLists() {
        favPanel.refresh();
        watchedPanel.refresh();
        wantPanel.refresh();
    }
}
