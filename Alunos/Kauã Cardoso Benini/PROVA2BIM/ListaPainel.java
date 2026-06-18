package com.tvtracker.ui;

import com.tvtracker.model.Listas;
import com.tvtracker.model.Series;
import com.tvtracker.service.SeriesService;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Exibe uma das três listas do usuário (Favoritos, Assistidas ou Para Assistir).
 *
 * Esta mesma classe é reutilizada três vezes no MainFrame, uma para cada tipo de lista.
 * O tipo é definido pelo parâmetro ListType passado no construtor.
 *
 * Funcionalidades:
 * - Exibe as séries da lista em uma tabela
 * - Permite ordenar por diferentes critérios
 * - Permite remover séries da lista
 * - Permite mover séries para outra lista
 * - Permite ver detalhes de uma série
 */
public class ListaPainel extends JPanel {

    // Opções disponíveis no dropdown de ordenação
    private static final String[] SORT_OPTIONS = {
        "Sem ordenacao", "Nome", "Nota", "Estado", "Data de Estreia"
    };

    private final SeriesService service;
    private final Listas listType;       // Qual lista este painel representa
    private final Tabela tableModel;
    private final JTable table;
    private final JComboBox<String> sortCombo; // Dropdown de ordenação
    private final JLabel countLabel;           // Exibe "X série(s)"

    /**
     * Construtor — configura o painel para um tipo específico de lista.
     */
    public ListaPainel(SeriesService service, Listas listType) {
        this.service = service;
        this.listType = listType;
        this.tableModel = new Tabela();
        this.table = new JTable(tableModel);
        this.sortCombo = new JComboBox<>(SORT_OPTIONS);
        this.countLabel = new JLabel("  0 serie(s)");

        setLayout(new BorderLayout(5, 5));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        add(buildTopPanel(),    BorderLayout.NORTH);  // Controles de ordenação
        add(buildTable(),       BorderLayout.CENTER); // Tabela com as séries
        add(buildActionPanel(), BorderLayout.SOUTH);  // Botões de ação

        refresh(); // Carrega os dados iniciais assim que o painel é criado
    }

    /**
     * Recarrega os dados da lista e aplica a ordenação atual.
     * Chamado na criação, ao trocar de aba, e após adicionar/remover séries.
     */
    public void refresh() {
        List<Series> list = service.getList(listType); // Busca a lista do serviço
        String sort = (String) sortCombo.getSelectedItem(); // Ordenação selecionada
        list = service.applySorting(list, sort);            // Aplica a ordenação
        tableModel.setData(list);                           // Atualiza a tabela
        countLabel.setText("  " + list.size() + " serie(s)"); // Atualiza o contador
    }

    /**
     * Monta o painel superior com o dropdown de ordenação e o botão "Aplicar".
     */
    private JPanel buildTopPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));

        JButton applyBtn = new JButton("Aplicar");
        applyBtn.addActionListener(e -> refresh()); // Re-carrega com nova ordenação

        panel.add(new JLabel("Ordenar por:"));
        panel.add(sortCombo);
        panel.add(applyBtn);
        panel.add(countLabel); // Ex: "  12 serie(s)"

        return panel;
    }

    /**
     * Configura e retorna a tabela com barra de rolagem.
     * Comportamento igual ao SearchPanel — mesma estrutura visual.
     */
    private JScrollPane buildTable() {
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getTableHeader().setReorderingAllowed(false);
        table.setRowHeight(22);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        // Define larguras das colunas: Nome, Idioma, Gêneros, Nota, Estado, Estreia, Término, Emissora
        int[] widths = {160, 70, 130, 45, 100, 80, 90, 130};
        for (int i = 0; i < widths.length; i++) {
            table.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);
        }

        // Duplo clique abre os detalhes da série
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) showDetails();
            }
        });

        return new JScrollPane(table);
    }

    /**
     * Monta o painel inferior com os botões de ação.
     *
     * Botões gerados dinamicamente:
     * - "Remover da Lista" — sempre presente
     * - "Adicionar a X" — um para cada lista diferente da atual (gerado por loop)
     * - "Ver Detalhes" — sempre presente
     */
    private JPanel buildActionPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));

        JButton removeBtn  = new JButton("Remover da Lista");
        JButton detailsBtn = new JButton("Ver Detalhes");

        removeBtn.addActionListener(e -> removeSelected());
        detailsBtn.addActionListener(e -> showDetails());

        panel.add(removeBtn);

        // Gera dinamicamente botões "Adicionar a X" para as outras listas
        // Ex: no painel de Favoritos, aparecerão "Adicionar a Assistidas" e "Adicionar a Para Assistir"
        for (Listas other : Listas.values()) {
            if (other != listType) { // Ignora a lista atual
                JButton addOther = new JButton("Adicionar a " + other.getDisplayName());
                final Listas target = other; // Necessário ser final para usar no lambda
                addOther.addActionListener(e -> addToOtherList(target));
                panel.add(addOther);
            }
        }

        panel.add(detailsBtn);
        return panel;
    }

    /**
     * Retorna a série selecionada na tabela, ou null com aviso se nenhuma estiver selecionada.
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
     * Remove a série selecionada desta lista após confirmação do usuário.
     * Pede confirmação com "Sim/Não" antes de remover.
     */
    private void removeSelected() {
        Series s = getSelected();
        if (s == null) return;

        // Diálogo de confirmação antes de remover
        int confirm = JOptionPane.showConfirmDialog(this,
                "Remover \"" + s.getName() + "\" de " + listType.getDisplayName() + "?",
                "Confirmar remocao", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            service.removeFromList(s, listType);
            refresh(); // Atualiza a tabela após remoção
        }
    }

    /**
     * Adiciona a série selecionada a outra lista (sem removê-la desta).
     * Verifica duplicata antes de adicionar.
     *
     * @param target lista de destino
     */
    private void addToOtherList(Listas target) {
        Series s = getSelected();
        if (s == null) return;

        // Verifica se já está na lista de destino
        if (service.isInList(s, target)) {
            JOptionPane.showMessageDialog(this,
                    "\"" + s.getName() + "\" ja esta em " + target.getDisplayName() + ".",
                    "Ja adicionada", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        service.addToList(s, target);
        JOptionPane.showMessageDialog(this,
                "\"" + s.getName() + "\" adicionada a " + target.getDisplayName() + "!",
                "Sucesso", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Abre o diálogo de detalhes da série selecionada.
     */
    private void showDetails() {
        Series s = getSelected();
        if (s == null) return;
        new Detalhes(SwingUtilities.getWindowAncestor(this), s).setVisible(true);
    }
}
