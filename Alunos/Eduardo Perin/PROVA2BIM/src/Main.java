import javax.swing.*;

public class Main {
    public static void main(String[] args) {

        //  captura qualquer exceção não tratada em qualquer thread
        Thread.setDefaultUncaughtExceptionHandler((thread, ex) ->
            SwingUtilities.invokeLater(() ->
                JOptionPane.showMessageDialog(null,
                    "Erro inesperado: " + ex.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE)
            )
        );

        DataService dataService = new DataService();
        User user = null;

        try {
            user = dataService.load();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro ao carregar dados: " + e.getMessage());
        }

        if (user == null) {
            String name = JOptionPane.showInputDialog(null, "Qual seu nome?",
                "Bem vindo!", JOptionPane.QUESTION_MESSAGE);
            if (name == null || name.isBlank()) System.exit(0);
            user = new User(name.trim());
        }

        final User finalUser = user;
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame(finalUser, dataService);
            frame.setVisible(true);
        });
    }
}