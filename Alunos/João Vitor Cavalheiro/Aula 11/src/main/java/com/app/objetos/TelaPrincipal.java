package com.app.objetos;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class TelaPrincipal extends JFrame {

    private JTextField txtCidade;

    private JTextArea txtResultado;

    private final JButton btnBuscar;

    public TelaPrincipal() {

        setTitle("App Clima/Tempo");

        setSize(600, 400);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLocationRelativeTo(null);

        setLayout(new BorderLayout());

        JPanel painelSuperior = new JPanel();

        painelSuperior.add(
                new JLabel("Pesquisa:"));

        txtCidade = new JTextField("Digite aqui o nome da cidade", 20);

        txtCidade.addFocusListener(new java.awt.event.FocusAdapter() {

            @Override
            public void focusGained(java.awt.event.FocusEvent e) {

                if (txtCidade.getText().equals("Digite aqui o nome da cidade")) {
                    txtCidade.setText("");
                    txtCidade.setForeground(java.awt.Color.BLACK);
                }
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent e) {

                if (txtCidade.getText().trim().isEmpty()) {
                    txtCidade.setText("Digite aqui o nome da cidade");
                    txtCidade.setForeground(java.awt.Color.GRAY);
                }
            }
        });

        painelSuperior.add(txtCidade);

        btnBuscar = new JButton("🌤 Consultar Clima");
        
        getRootPane().setDefaultButton(btnBuscar);

        btnBuscar.setBackground(new java.awt.Color(52, 152, 219));
        btnBuscar.setForeground(java.awt.Color.WHITE);
        btnBuscar.setFocusPainted(false);

        painelSuperior.add(btnBuscar);

        add(painelSuperior,
                BorderLayout.NORTH);

        txtResultado = new JTextArea();
        txtResultado.setFont(
                new java.awt.Font("Dialog", java.awt.Font.PLAIN, 16));

        txtResultado.setLineWrap(true);
        txtResultado.setWrapStyleWord(true);
        txtResultado.setEditable(false);

        txtResultado.setBackground(
        new java.awt.Color(240, 248, 255));

        add(new JScrollPane(txtResultado),
                BorderLayout.CENTER);

        btnBuscar.addActionListener(e -> {

            String cidade = txtCidade.getText();

            ClimaService service = new ClimaService();

            Clima clima = service.buscarClima(cidade);

            if (clima != null) {

                txtResultado.setText(

                        "🌡 Temperatura Atual: "
                                + clima.getTemperaturaAtual() + " °C\n\n"

                                + "🔺 Temperatura Máxima: "
                                + clima.getTemperaturaMaxima() + " °C\n\n"

                                + "🔻 Temperatura Mínima: "
                                + clima.getTemperaturaMinima() + " °C\n\n"

                                + "💧 Umidade: "
                                + clima.getUmidade() + "%\n\n"

                                + "☁ Condição: "
                                + clima.getCondicao() + "\n\n"

                                + "🌧 Precipitação: "
                                + clima.getPrecipitacao() + " mm\n\n"

                                + "💨 Velocidade do Vento: "
                                + clima.getVelocidadeVento() + " km/h\n\n"

                                + "\u21BB Direção do Vento: "
                                + clima.getDirecaoVento() + "°");
            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "Verifique se o nome da cidade foi digitado corretamente.",
                        "Cidade não encontrada",
                        JOptionPane.WARNING_MESSAGE);
            }
        });
    }
}