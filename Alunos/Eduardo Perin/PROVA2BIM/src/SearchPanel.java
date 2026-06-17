import javax.swing.*;
import java.awt.*;

public class SearchPanel extends JPanel {

    private final SeriesDetailPanel detailPanel;

    private JTextField searchField;
    private JList<Series> resultList;
    private DefaultListModel<Series> listModel;
    
    // Guarda referência do worker atual pra cancelar se precisar
    private SwingWorker<java.util.List<Series>, Void> currentWorker;

    public SearchPanel(SeriesDetailPanel detailPanel) {
        this.detailPanel = detailPanel;
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout(10, 10));

        JPanel topPanel = new JPanel(new BorderLayout(5, 0));
        searchField = new JTextField();
        JButton btnSearch = new JButton("Buscar");
        topPanel.add(searchField, BorderLayout.CENTER);
        topPanel.add(btnSearch, BorderLayout.EAST);

        listModel = new DefaultListModel<>();
        resultList = new JList<>(listModel);
        JScrollPane scroll = new JScrollPane(resultList);

        resultList.addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) return;
            Series selected = resultList.getSelectedValue();
            if (selected != null) detailPanel.setSeries(selected);
        });

        JPanel center = new JPanel(new BorderLayout(10, 0));
        center.add(scroll, BorderLayout.CENTER);
        center.add(detailPanel, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);
        add(center, BorderLayout.CENTER);

        btnSearch.addActionListener(e -> search());
        searchField.addActionListener(e -> search());
    }

    private void search() {
        String query = searchField.getText().trim();
        if (query.isEmpty()) return;

        if (currentWorker != null && !currentWorker.isDone()) {
            currentWorker.cancel(true);
        }

        listModel.clear();

        resultList.setEnabled(false);

        Series loading = new Series();
        loading.name = "Buscando...";
        listModel.addElement(loading);

        currentWorker = new SwingWorker<java.util.List<Series>, Void>() {
            @Override
            protected java.util.List<Series> doInBackground() throws Exception {
                return new TvMazeService().search(query);
            }

            @Override
            protected void done() {
                if (isCancelled()) return;

                resultList.setEnabled(true);
                listModel.clear();

                try {
                    java.util.List<Series> results = get();
                    if (results.isEmpty()) {
                        JOptionPane.showMessageDialog(SearchPanel.this,
                            "Nenhuma série encontrada para \"" + query + "\".",
                            "Nenhuma série encontrada", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    for (Series s : results) listModel.addElement(s);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(SearchPanel.this,
                        "Erro na busca: " + ex.getMessage(),
                        "Erro na busca!", JOptionPane.ERROR_MESSAGE);
                }
            }
        };

        currentWorker.execute();
    }
}