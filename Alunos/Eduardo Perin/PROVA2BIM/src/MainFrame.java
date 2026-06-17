import javax.swing.*;

public class MainFrame extends JFrame {

    private final User user;
    private final DataService dataService;

    public MainFrame(User user, DataService dataService) {
        this.user = user;
        this.dataService = dataService;
        initUI();
    }

    private void initUI() {
        setTitle("TV Tracker - " + user.getName());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);

        SeriesDetailPanel detailForSearch = new SeriesDetailPanel(user, dataService, null);
        SeriesDetailPanel detailForLists  = new SeriesDetailPanel(user, dataService, null);

        ListsPanel listsPanel = new ListsPanel(user, dataService, detailForLists);

        detailForSearch.setOnListChanged(listsPanel::refresh);
        detailForLists.setOnListChanged(listsPanel::refresh);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab(" Buscar", new SearchPanel(detailForSearch));
        tabs.addTab(" Minhas Listas", listsPanel);

        add(tabs);
    }
}