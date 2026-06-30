package view;

import java.awt.*;
import javax.swing.*;
import model.Usuario;

public class UsuarioDialog extends JDialog {

    private JTextField campoNome;
    private Usuario usuario;

    public UsuarioDialog(Frame parent) {

        super(parent, "Minhas Séries de TV", true);

        setSize(400, 220);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel titulo = new JLabel("📺 Bem-vindo ao sistema!", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 18));

        add(titulo, BorderLayout.NORTH);

        JPanel painelCentro = new JPanel(new GridLayout(4, 1, 5, 5));

        painelCentro.add(new JLabel("Informe seu nome ou apelido:", SwingConstants.CENTER));

        campoNome = new JTextField();
        painelCentro.add(campoNome);

        JButton botaoEntrar = new JButton("Entrar");

        botaoEntrar.addActionListener(e -> {

            String nome = campoNome.getText().trim();

            if (nome.isEmpty()) {

                JOptionPane.showMessageDialog(
                        this,
                        "Informe um nome.",
                        "Aviso",
                        JOptionPane.WARNING_MESSAGE);

                return;
            }

            usuario = new Usuario(nome);

            dispose();

        });

        painelCentro.add(new JLabel());

        painelCentro.add(botaoEntrar);

        add(painelCentro, BorderLayout.CENTER);
    }

    public Usuario getUsuario() {
        return usuario;
    }

}