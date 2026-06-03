package app;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;


public class WeatherApp extends JFrame {

    private JTextField txtCidade;
    private JTextArea txtResultado;

    public WeatherApp() {

        setTitle("Consulta de Clima");
        setSize(600, 450);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel topo = new JPanel();
        topo.add(new JLabel("Cidade:"));

        txtCidade = new JTextField(20);
        txtCidade.addActionListener(e -> consultarClima());
        topo.add(txtCidade);

        JButton btnBuscar =  new JButton("Buscar");
        topo.add(btnBuscar);
        add(topo, BorderLayout.NORTH);

        txtResultado = new JTextArea();
        txtResultado.setEditable(false);
        txtResultado.setFont(
                new Font("Monospaced", Font.PLAIN, 14));
        
        add(new JScrollPane(txtResultado),BorderLayout.CENTER);

        btnBuscar.addActionListener(
                e -> consultarClima());

        setVisible(true);
    }

    private void consultarClima() {

        try {

            WeatherService service = new WeatherService();

            String resultado =
                    service.consultarCidade(
                            txtCidade.getText());

            txtResultado.setText(resultado);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(
                    this,
                    "Erro ao consultar cidade:\n"
                            + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(
                WeatherApp::new);
    }
}
