package ui;

import model.UserData;
import service.StorageService;
import service.TVMazeService;
import service.TVTrackerException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MainWindow extends JFrame {
    private final UserData      userData;
    private final TVMazeService tvMazeService;
    private JLabel      welcomeLabel;
    private ListPanel   favoritesPanel;
    private ListPanel   watchedPanel;
    private ListPanel   wantToWatchPanel;

    public MainWindow(UserData userData, TVMazeService tvMazeService) {
        this.userData      = userData;
        this.tvMazeService = tvMazeService;
        buildUI();
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int choice = JOptionPane.showConfirmDialog(
                    MainWindow.this,
                    "Deseja realmente sair do TV Tracker?",
                    "Sair", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (choice == JOptionPane.YES_OPTION) {
                    saveData();
                    System.exit(0);
                }
            }
        });
    }

    private void buildUI() {
        setTitle("TV Tracker");
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setSize(960, 620);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(0, 0));

        welcomeLabel = new JLabel();
        welcomeLabel.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        welcomeLabel.setFont(welcomeLabel.getFont().deriveFont(Font.BOLD, 14f));
        updateWelcomeLabel();
        add(welcomeLabel, BorderLayout.NORTH);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Buscar", new SearchPanel(this));

        favoritesPanel   = new ListPanel(this, "favorites");
        watchedPanel     = new ListPanel(this, "watched");
        wantToWatchPanel = new ListPanel(this, "wantToWatch");

        tabs.addTab("Favoritos",      favoritesPanel);
        tabs.addTab("Assistidas",     watchedPanel);
        tabs.addTab("Quero Assistir", wantToWatchPanel);

        add(tabs, BorderLayout.CENTER);
    }

    private void updateWelcomeLabel() {
        welcomeLabel.setText("Ola, " + userData.getName() + "!  |  TV Tracker");
    }

    public void onDataChanged() {
        favoritesPanel.refresh();
        watchedPanel.refresh();
        wantToWatchPanel.refresh();
        saveData();
    }

    public void saveData() {
        try {
            StorageService.save(userData);
        } catch (TVTrackerException e) {
            JOptionPane.showMessageDialog(this,
                "Nao foi possivel salvar os dados:\n" + e.getMessage(),
                "Erro ao salvar", JOptionPane.ERROR_MESSAGE);
        }
    }

    public UserData      getUserData()      { return userData; }
    public TVMazeService getTVMazeService() { return tvMazeService; }
}
