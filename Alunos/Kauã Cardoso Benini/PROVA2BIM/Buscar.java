package com.tvtracker.ui;

import com.tvtracker.model.Listas;
import com.tvtracker.model.Series;
import com.tvtracker.service.SeriesService;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * PAINEL DE BUSCA
 *
 * Permite ao usuário digitar o nome de uma série e buscá-la na API TVMaze.
 * Os resultados são exibidos em uma tabela e o usuário pode:
 * - Ver detalhes da série (duplo clique ou botão)
 * - Adicionar a Favoritos, Assistidas ou Para Assistir
 */
public class Buscar extends JPanel {

    private final SeriesService service;

    // Campo de texto onde o usuário digita o nome da série
    private JTextField searchField;

    // Modelo de dados da tabela (controla o que é exibido)
    private final Tabela tableModel;

    // Componente visual da tabela
    private final JTable table;

    // Texto de status exibido abaixo da tabela (ex: "5 série(s) encontrada(s)")
    private final JLabel statusLabel;

    /**
     * Callback (função) chamado quando o usuário adiciona uma série a uma lista.
     * Serve para notificar o MainFrame que deve atualizar os painéis de lista.
     * É definido externamente via setOnListChanged().
     */
    private Runnable onListChanged;

    public Buscar(SeriesService service) {
        this.service = service;
        this.tableModel = new Tabela();
        this.table = new JTable(tableModel); // JTable usa o modelo para exibir os dados
        this.statusLabel = new JLabel("Digite o nome de uma serie e clique em Buscar.");

        setLayout(new BorderLayout(5, 5));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Margem interna

        add(buildSearchBar(), BorderLayout.NORTH);   // Campo de busca no topo
        add(buildTable(),     BorderLayout.CENTER);  // Tabela no centro
        add(buildActionPanel(), BorderLayout.SOUTH); // Botões na parte inferior
    }

    /**
     * Define o callback que será chamado quando uma série for adicionada a uma lista.
     * Usado pelo MainFrame para atualizar as abas de lista automaticamente.
     */
    public void setOnListChanged(Runnable r) {
        this.onListChanged = r;
    }

    /**
     * Monta a barra de busca com:
     * - Label "Buscar serie:"
     * - Campo de texto (searchField)
     * - Botão "Buscar"
     *
     * A busca é acionada tanto pelo botão quanto pelo Enter no campo de texto.
     */
    private JPanel buildSearchBar() {
        JPanel bar = new JPanel(new BorderLayout(5, 0));
        bar.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));

        bar.add(new JLabel("Buscar serie: "), BorderLayout.WEST);

        searchField = new JTextField();
        bar.add(searchField, BorderLayout.CENTER);

        JButton btn = new JButton("Buscar");
        btn.addActionListener(e -> doSearch());
        searchField.addActionListener(e -> doSearch()); // Enter também busca

        bar.add(btn, BorderLayout.EAST);
        return bar;
    }

    /**
     * Configura a JTable e a envolve em um JScrollPane.
     * Define comportamentos:
     * - Seleção de apenas uma linha por vez
     * - Colunas não reordenáveis pelo usuário
     * - Duplo clique abre os detalhes
     */
    private JScrollPane buildTable() {
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // Uma linha por vez
        table.getTableHeader().setReorderingAllowed(false);          // Colunas fixas
        table.setRowHeight(22);                                       // Altura das linhas
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);     // Redimensiona colunas automaticamente
        setColumnWidths();

        // Listener de clique do mouse na tabela
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                // Abre detalhes ao dar duplo clique em uma linha
                if (e.getClickCount() == 2) showDetails();
            }
        });

        return new JScrollPane(table); // Envolve a tabela para ter barra de rolagem
    }

    /**
     * Define as larguras preferenciais de cada coluna em pixels.
     * A ordem corresponde às colunas: Nome, Idioma, Gêneros, Nota, Estado, Estreia, Término, Emissora
     */
    private void setColumnWidths() {
        int[] widths = {160, 70, 130, 45, 100, 80, 90, 130};
        for (int i = 0; i < widths.length; i++) {
            table.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);
        }
    }

    /**
     * Monta o painel inferior com os botões de ação e o label de status.
     */
    private JPanel buildActionPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));

        JButton favBtn     = new JButton("+ Favoritos");
        JButton watchedBtn = new JButton("+ Assistidas");
        JButton wantBtn    = new JButton("+ Para Assistir");
        JButton detailsBtn = new JButton("Ver Detalhes");

        // Cada botão chama addToList com o tipo de lista correspondente
        favBtn.addActionListener(e -> addToList(Listas.FAVORITES));
        watchedBtn.addActionListener(e -> addToList(Listas.WATCHED));
        wantBtn.addActionListener(e -> addToList(Listas.WANT_TO_WATCH));
        detailsBtn.addActionListener(e -> showDetails());

        panel.add(favBtn);
        panel.add(watchedBtn);
        panel.add(wantBtn);
        panel.add(detailsBtn);
        panel.add(Box.createHorizontalStrut(10)); // Espaço visual entre botões e status
        panel.add(statusLabel);

        return panel;
    }

    /**
     * Executa a busca na API de forma assíncrona usando SwingWorker.
     *
     * A busca na internet pode demorar vários segundos.
     * Se feita na thread principal do Swing (EDT), a interface travaria durante a busca.
     * O SwingWorker executa o trabalho pesado em uma thread separada e
     * atualiza a interface de volta na EDT quando termina.
     */
    private void doSearch() {
        String query = searchField.getText().trim();

        // Valida que o campo não está vazio
        if (query.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Por favor, insira um nome para buscar.",
                    "Campo vazio", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Atualiza o status e limpa a tabela enquanto a busca acontece
        statusLabel.setText("Buscando...");
        tableModel.setData(java.util.Collections.emptyList());

        // SwingWorker<TipoDoResultado, TipoDeProgressIntermediário>
        new SwingWorker<List<Series>, Void>() {

            /**
             * Executado em uma thread separada (não na EDT).
             * Aqui é onde a chamada de rede acontece.
             */
            @Override
            protected List<Series> doInBackground() throws Exception {
                return service.searchShows(query);
            }

            /**
             * Executado na EDT após doInBackground() terminar.
             * Aqui atualizamos a interface com os resultados.
             */
            @Override
            protected void done() {
                try {
                    List<Series> results = get(); // Pega o resultado de doInBackground()
                    tableModel.setData(results);  // Atualiza a tabela
                    statusLabel.setText(results.isEmpty()
                            ? "Nenhuma serie encontrada."
                            : results.size() + " serie(s) encontrada(s). Duplo-clique para detalhes.");
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                    statusLabel.setText("Busca interrompida.");
                } catch (ExecutionException ex) {
                    // ExecutionException envolve a IOException lançada por searchShows()
                    statusLabel.setText("Erro na busca.");
                    Throwable cause = ex.getCause() != null ? ex.getCause() : ex;
                    JOptionPane.showMessageDialog(Buscar.this,
                            "Erro ao buscar series:\n" + cause.getMessage(),
                            "Erro de conexao", JOptionPane.ERROR_MESSAGE);
                }
            }
        }.execute(); // Inicia a execução assíncrona
    }

    /**
     * Retorna a série selecionada na tabela.
     * Se nenhuma linha estiver selecionada, exibe um aviso e retorna null.
     */
    private Series getSelected() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this,
                    "Selecione uma serie na lista primeiro.",
                    "Nenhuma serie selecionada", JOptionPane.WARNING_MESSAGE);
            return null;
        }
        return tableModel.getSeriesAt(row);
    }

    /**
     * Adiciona a série selecionada a uma lista do usuário.
     * Verifica primeiro se já está na lista para evitar duplicatas.
     */
    private void addToList(Listas type) {
        Series s = getSelected();
        if (s == null) return; // Nenhuma série selecionada

        // Verifica duplicata
        if (service.isInList(s, type)) {
            JOptionPane.showMessageDialog(this,
                    "\"" + s.getName() + "\" ja esta em " + type.getDisplayName() + ".",
                    "Ja adicionada", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        service.addToList(s, type);
        JOptionPane.showMessageDialog(this,
                "\"" + s.getName() + "\" adicionada a " + type.getDisplayName() + "!",
                "Sucesso", JOptionPane.INFORMATION_MESSAGE);

        // Notifica o MainFrame para atualizar as abas de lista
        if (onListChanged != null) onListChanged.run();
    }

    /**
     * Abre o diálogo de detalhes da série selecionada.
     * SwingUtilities.getWindowAncestor() encontra a janela pai (MainFrame)
     * para centralizar o diálogo corretamente.
     */
    private void showDetails() {
        Series s = getSelected();
        if (s == null) return;
        new Detalhes(SwingUtilities.getWindowAncestor(this), s).setVisible(true);
    }
}
