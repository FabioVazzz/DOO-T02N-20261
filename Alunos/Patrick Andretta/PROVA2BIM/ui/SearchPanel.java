package ui;

import model.Show;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class SearchPanel extends JPanel {
    private final MainWindow        mainWindow;
    private final JTextField        searchField = new JTextField(25);
    private final JButton           searchBtn   = new JButton("Buscar");
    private final JLabel            statusLabel = new JLabel(" ");
    private final DefaultTableModel tableModel;
    private final JTable            table;
    private final List<Show>        currentResults = new ArrayList<>();
    private boolean                 searching = false;

    private static final String[] COLUMNS = {
        "Nome", "Idioma", "Gêneros", "Nota", "Estado", "Estreia", "Término", "Emissora"
    };

    public SearchPanel(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        this.tableModel = new DefaultTableModel(COLUMNS, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        this.table = new JTable(tableModel);
        buildUI();
    }

    private void buildUI() {
        setLayout(new BorderLayout(6, 6));

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 8));
        top.add(new JLabel("Nome da serie:"));
        top.add(searchField);
        top.add(searchBtn);
        top.add(statusLabel);
        add(top, BorderLayout.NORTH);

        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowHeight(22);
        table.getTableHeader().setReorderingAllowed(false);
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) openDetails();
            }
        });
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel south = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 6));
        JButton detailsBtn = new JButton("Ver Detalhes");
        detailsBtn.addActionListener(e -> openDetails());
        south.add(detailsBtn);
        add(south, BorderLayout.SOUTH);

        searchBtn.addActionListener(e -> performSearch());
        searchField.addActionListener(e -> performSearch());
    }

    private void performSearch() {
        if (searching) {
            return;
        }
        String query = searchField.getText().trim();
        if (query.isBlank()) {
            JOptionPane.showMessageDialog(this,
                "Digite o nome de uma serie.", "Campo vazio", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        searching = true;
        searchBtn.setEnabled(false);
        searchField.setEnabled(false);
        statusLabel.setText("Buscando...");
        tableModel.setRowCount(0);
        currentResults.clear();

        SwingWorker<List<Show>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<Show> doInBackground() throws Exception {
                return mainWindow.getTVMazeService().searchShows(query);
            }

            @Override
            protected void done() {
                searching = false;
                searchBtn.setEnabled(true);
                searchField.setEnabled(true);
                tableModel.setRowCount(0);
                currentResults.clear();
                try {
                    List<Show> results = get();
                    currentResults.addAll(results);
                    for (Show s : results) {
                        String rating = s.getRating() > 0
                            ? String.format(Locale.US, "%.1f", s.getRating()) : "N/A";
                        tableModel.addRow(new Object[]{
                            s.getName(), s.getLanguage(), s.getGenres(), rating,
                            s.getStatus(), s.getPremiereDate(), s.getEndDate(), s.getNetwork()
                        });
                    }
                    statusLabel.setText(results.isEmpty()
                        ? "Nenhum resultado encontrado."
                        : results.size() + " resultado(s) encontrado(s).");
                } catch (ExecutionException e) {
                    Throwable cause = e.getCause();
                    statusLabel.setText("Erro na busca.");
                    JOptionPane.showMessageDialog(SearchPanel.this,
                        cause != null ? cause.getMessage() : e.getMessage(),
                        "Erro de rede", JOptionPane.ERROR_MESSAGE);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    statusLabel.setText("Busca interrompida.");
                }
            }
        };
        worker.execute();
    }

    private void openDetails() {
        int row = table.getSelectedRow();
        if (row < 0 || row >= currentResults.size()) {
            JOptionPane.showMessageDialog(this,
                "Selecione uma serie primeiro.", "Atencao", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        new ShowDetailDialog(mainWindow, currentResults.get(row), mainWindow).setVisible(true);
    }
}
