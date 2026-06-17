import javax.swing.*;
import java.awt.*;
import java.util.Comparator;
import java.util.List;

public class ListsPanel extends JPanel {

    private final User user;
    private final DataService dataService;
    private final SeriesDetailPanel detailPanel;

    private JComboBox<String> listSelector;
    private JComboBox<String> sortSelector;
    private DefaultListModel<Series> listModel;
    private JList<Series> seriesList;

    public ListsPanel(User user, DataService dataService, SeriesDetailPanel detailPanel) {
        this.user = user;
        this.dataService = dataService;
        this.detailPanel = detailPanel;
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout(10, 10));

        // Painel de controles no topo
        JPanel topPanel = new JPanel(new GridLayout(1, 2, 10, 0));

        listSelector = new JComboBox<>(new String[]{
            "Favoritos", "Assistidas", "Watchlist"
        });

        sortSelector = new JComboBox<>(new String[]{
            "Nome", "Nota", "Estado", "Data de Estreia"
        });

        topPanel.add(listSelector);
        topPanel.add(sortSelector);

        // Lista
        listModel = new DefaultListModel<>();
        seriesList = new JList<>(listModel);
        JScrollPane scroll = new JScrollPane(seriesList);

        // Ao clicar numa série mostra os detalhes
        seriesList.addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) return;
            Series selected = seriesList.getSelectedValue();
            if (selected != null) detailPanel.setSeries(selected);
        });

        // Painel central com lista e detalhes lado a lado
        JPanel center = new JPanel(new BorderLayout(10, 0));
        center.add(scroll, BorderLayout.CENTER);
        center.add(detailPanel, BorderLayout.EAST);

        // Botão remover
        JButton btnRemove = new JButton("Remover da Lista");
        btnRemove.addActionListener(e -> removeSelected());

        add(topPanel, BorderLayout.NORTH);
        add(center, BorderLayout.CENTER);
        add(btnRemove, BorderLayout.SOUTH);

        listSelector.addActionListener(e -> refresh());
        sortSelector.addActionListener(e -> refresh());

        refresh();
    }

    public void refresh() {
        listModel.clear();

        List<Series> source = getSelectedList();
        Comparator<Series> comparator = getSelectedComparator();

        source.stream()
            .sorted(comparator)
            .forEach(listModel::addElement);
    }

    private List<Series> getSelectedList() {
        return switch (listSelector.getSelectedIndex()) {
            case 1  -> user.getWatched();
            case 2  -> user.getWatchlist();
            default -> user.getFavorites();
        };
    }

    private Comparator<Series> getSelectedComparator() {
        return switch (sortSelector.getSelectedIndex()) {
            case 1 -> Comparator.comparingDouble(Series::getRatingAverage).reversed();
            case 2 -> Comparator.comparing((Series s) -> s.status != null ? s.status : "");
            case 3 -> Comparator.comparing((Series s) -> s.premiered != null ? s.premiered : "");
            default -> Comparator.comparing((Series s) -> s.name != null ? s.name : "");
        };
    }

    private void removeSelected() {
        Series selected = seriesList.getSelectedValue();
        if (selected == null) return;

        switch (listSelector.getSelectedIndex()) {
            case 1 -> user.removeWatched(selected);
            case 2 -> user.removeWatchlist(selected);
            default -> user.removeFavorite(selected);
        }

        try {
            dataService.save(user);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar: " + ex.getMessage());
        }

        refresh();
        seriesList.clearSelection();
        detailPanel.clear(); 
    }
}