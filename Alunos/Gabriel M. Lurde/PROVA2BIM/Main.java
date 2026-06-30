import javax.swing.*;
import model.Usuario;
import util.JsonUtil;
import view.MainFrame;
import view.UsuarioDialog;

public class Main {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            Usuario usuario = JsonUtil.carregarUsuario();

            if (usuario != null) {

                JOptionPane.showMessageDialog(
                        null,
                        "Bem-vindo novamente, " + usuario.getNome() + "!"
                );

            } else {

                UsuarioDialog dialog = new UsuarioDialog(null);

                dialog.setVisible(true);

                usuario = dialog.getUsuario();

                if (usuario == null) {
                    System.exit(0);
                }

                JOptionPane.showMessageDialog(
                        null,
                        "Bem-vindo, " + usuario.getNome() + "!"
                );

            }

            new MainFrame(usuario);

        });

    }

}