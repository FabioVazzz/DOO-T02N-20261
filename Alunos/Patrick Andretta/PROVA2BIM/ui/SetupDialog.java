package ui;

import javax.swing.*;
import java.awt.*;

public class SetupDialog extends JDialog {
    private final JTextField nameField = new JTextField(20);
    private boolean confirmed = false;

    public SetupDialog(JFrame parent) {
        super(parent, "Bem-vindo ao TV Tracker", true);
        setSize(420, 190);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        buildUI();
    }

    private void buildUI() {
        setLayout(new BorderLayout(10, 10));

        JLabel title = new JLabel("Configure seu perfil para comecar", SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 15f));
        title.setBorder(BorderFactory.createEmptyBorder(15, 0, 5, 0));
        add(title, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridLayout(1, 2, 8, 8));
        form.setBorder(BorderFactory.createEmptyBorder(8, 30, 8, 30));
        form.add(new JLabel("Nome:"));
        form.add(nameField);
        add(form, BorderLayout.CENTER);

        JButton btn = new JButton("Continuar");
        btn.addActionListener(e -> confirm());
        getRootPane().setDefaultButton(btn);

        JPanel south = new JPanel();
        south.add(btn);
        add(south, BorderLayout.SOUTH);
    }

    private void confirm() {
        if (nameField.getText().isBlank()) {
            JOptionPane.showMessageDialog(this,
                "Por favor, insira seu nome.",
                "Campo obrigatorio", JOptionPane.WARNING_MESSAGE);
            return;
        }
        confirmed = true;
        dispose();
    }

    public boolean isConfirmed()   { return confirmed; }
    public String getEnteredName() { return nameField.getText().trim(); }
}
