package fag;

import javax.swing.*;

public class ClimaApp {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
            }
            Principal janela = new Principal();
            janela.setVisible(true);
        });
    }
}
