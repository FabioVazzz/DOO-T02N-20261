package ui;

import model.Show;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.*;
import java.util.List;

public class ListPanel extends JPanel {
    private final MainWindow        mainWindow;
    private final String            listName;
    private final DefaultTableModel tableModel;
    private final JTable            table;
    private final JComboBox<String> sortCombo;

    private static final String[] COLUMNS     = {
        "Nome", "Idioma", "Gêneros", "Nota", "Estado", "Estreia", "Término", "Emissora"
    };
    private static final String[] SORT_OPTIONS = {
        "Nome (A-Z)", "Nota (Maior)", "Status", "Data de Estreia"
    };

    public ListPanel(MainWindow mainWindow, String listName) {
        this.mainWindow = mainWindow;
        this.listName   = listName;
        this.tableModel = new DefaultTableModel(COLUMNS, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        this.table    = new JTable(tableModel);
        this.sortCombo = new JComboBox<>(SORT_OPTIONS);
        buildUI();
        refresh();
    }

    private void buildUI() {
        setLayout(new BorderLayout(6, 6));

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 6));
        top.add(new JLabel("Ordenar por:"));
        top.add(sortCombo);
        sortCombo.addActionListener(e -> refresh());
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
        JButton removeBtn  = new JButton("Remover");
        detailsBtn.addActionListener(e -> openDetails());
        removeBtn.addActionListener(e -> removeSelected());
        south.add(detailsBtn);
        south.add(removeBtn);
        add(south, BorderLayout.SOUTH);
    }

    public void refresh() {
        tableModel.setRowCount(0);
        for (Show s : getSortedCopy()) {
            String rating = s.getRating() > 0
                ? String.format(Locale.US, "%.1f", s.getRating()) : "N/A";
            tableModel.addRow(new Object[]{
                s.getName(), s.getLanguage(), s.getGenres(), rating,
                s.getStatus(), s.getPremiereDate(), s.getEndDate(), s.getNetwork()
            });
        }
    }

    private List<Show> getSortedCopy() {
        List<Show> copy = new ArrayList<>(mainWindow.getUserData().getListByName(listName));
        String sel = (String) sortCombo.getSelectedItem();
        if ("Nome (A-Z)".equals(sel)) {
            copy.sort(Comparator.comparing(Show::getName, String.CASE_INSENSITIVE_ORDER));
        } else if ("Nota (Maior)".equals(sel)) {
            copy.sort(Comparator.comparingDouble(Show::getRating).reversed());
        } else if ("Status".equals(sel)) {
            copy.sort(Comparator.comparing(Show::getStatus));
        } else if ("Data de Estreia".equals(sel)) {
            copy.sort(Comparator.comparing((Show s) ->
                s.getPremiereDate().isEmpty() ? "0000" : s.getPremiereDate()).reversed());
        }
        return copy;
    }

    private void openDetails() {
        Show show = getSelectedShow();
        if (show == null) {
            JOptionPane.showMessageDialog(this,
                "Selecione uma serie primeiro.", "Atencao", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        new ShowDetailDialog(mainWindow, show, mainWindow).setVisible(true);
        refresh();
    }

    private void removeSelected() {
        Show show = getSelectedShow();
        if (show == null) {
            JOptionPane.showMessageDialog(this,
                "Selecione uma serie para remover.", "Atencao", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        int choice = JOptionPane.showConfirmDialog(this,
            "Deseja realmente remover \"" + show.getName() + "\" desta lista?",
            "Confirmar remocao", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            switch (listName) {
                case "favorites"   -> mainWindow.getUserData().removeFromFavorites(show.getId());
                case "watched"     -> mainWindow.getUserData().removeFromWatched(show.getId());
                case "wantToWatch" -> mainWindow.getUserData().removeFromWantToWatch(show.getId());
            }
            mainWindow.onDataChanged();
        }
    }

    private Show getSelectedShow() {
        int row = table.getSelectedRow();
        if (row < 0) return null;
        List<Show> sorted = getSortedCopy();
        return row < sorted.size() ? sorted.get(row) : null;
    }
}
