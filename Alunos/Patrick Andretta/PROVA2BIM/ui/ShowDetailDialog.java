package ui;

import model.Show;
import model.UserData;

import javax.swing.*;
import java.awt.*;

public class ShowDetailDialog extends JDialog {
    private final Show       show;
    private final MainWindow mainWindow;

    private JButton addFavBtn,    removeFavBtn;
    private JButton addWatchedBtn, removeWatchedBtn;
    private JButton addWantBtn,   removeWantBtn;

    public ShowDetailDialog(JFrame parent, Show show, MainWindow mainWindow) {
        super(parent, show.getName(), true);
        this.show       = show;
        this.mainWindow = mainWindow;
        setSize(520, 510);
        setLocationRelativeTo(parent);
        buildUI();
    }

    private void buildUI() {
        setLayout(new BorderLayout(8, 8));

        JTextArea detailArea = new JTextArea(show.describe());
        detailArea.setEditable(false);
        detailArea.setLineWrap(true);
        detailArea.setWrapStyleWord(true);
        detailArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
        detailArea.setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 12));
        add(new JScrollPane(detailArea), BorderLayout.CENTER);

        JPanel listPanel = new JPanel(new GridLayout(3, 2, 6, 6));
        listPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(4, 8, 8, 8),
            BorderFactory.createTitledBorder("Minhas listas")));

        addFavBtn        = new JButton("+ Favoritos");
        removeFavBtn     = new JButton("- Favoritos");
        addWatchedBtn    = new JButton("+ Assistidas");
        removeWatchedBtn = new JButton("- Assistidas");
        addWantBtn       = new JButton("+ Quero Assistir");
        removeWantBtn    = new JButton("- Quero Assistir");

        addFavBtn.addActionListener(e        -> onAdd("favorites"));
        removeFavBtn.addActionListener(e     -> onRemove("favorites"));
        addWatchedBtn.addActionListener(e    -> onAdd("watched"));
        removeWatchedBtn.addActionListener(e -> onRemove("watched"));
        addWantBtn.addActionListener(e       -> onAdd("wantToWatch"));
        removeWantBtn.addActionListener(e    -> onRemove("wantToWatch"));

        listPanel.add(addFavBtn);
        listPanel.add(removeFavBtn);
        listPanel.add(addWatchedBtn);
        listPanel.add(removeWatchedBtn);
        listPanel.add(addWantBtn);
        listPanel.add(removeWantBtn);

        add(listPanel, BorderLayout.SOUTH);

        updateButtons();
    }

    private void updateButtons() {
        UserData ud = mainWindow.getUserData();
        int id = show.getId();
        addFavBtn.setEnabled(!ud.isInFavorites(id));
        removeFavBtn.setEnabled(ud.isInFavorites(id));
        addWatchedBtn.setEnabled(!ud.isInWatched(id));
        removeWatchedBtn.setEnabled(ud.isInWatched(id));
        addWantBtn.setEnabled(!ud.isInWantToWatch(id));
        removeWantBtn.setEnabled(ud.isInWantToWatch(id));
    }

    private void onAdd(String listName) {
        UserData ud = mainWindow.getUserData();
        switch (listName) {
            case "favorites"   -> ud.addToFavorites(show);
            case "watched"     -> ud.addToWatched(show);
            case "wantToWatch" -> ud.addToWantToWatch(show);
        }
        mainWindow.onDataChanged();
        updateButtons();
    }

    private void onRemove(String listName) {
        UserData ud = mainWindow.getUserData();
        switch (listName) {
            case "favorites"   -> ud.removeFromFavorites(show.getId());
            case "watched"     -> ud.removeFromWatched(show.getId());
            case "wantToWatch" -> ud.removeFromWantToWatch(show.getId());
        }
        mainWindow.onDataChanged();
        updateButtons();
    }
}
