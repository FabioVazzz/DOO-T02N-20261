package app;

import model.UserData;
import service.StorageService;
import service.TVMazeService;
import service.TVTrackerException;
import ui.MainWindow;
import ui.SetupDialog;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                TVMazeService service = new TVMazeService();
                UserData userData;
                if (StorageService.exists()) {
                    userData = StorageService.load();
                } else {
                    SetupDialog setup = new SetupDialog(null);
                    setup.setVisible(true);
                    if (!setup.isConfirmed()) {
                        System.exit(0);
                        return;
                    }
                    userData = new UserData(setup.getEnteredName());
                    StorageService.save(userData);
                }
                MainWindow window = new MainWindow(userData, service);
                window.setVisible(true);
            } catch (TVTrackerException e) {
                JOptionPane.showMessageDialog(null,
                    e.getMessage(), "Erro ao iniciar", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
