import javax.swing.*;
import java.awt.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class Interface extends JFrame {
    
    private JTextField campoCidade;
    private JButton botaoBuscar;
    private JTextArea areaResultado;
    private JLabel labelStatus;
    private RequisicaoApi requisicaoApi;
    private JProgressBar barra;
    
    public Interface() {
        requisicaoApi = new RequisicaoApi();
        initUI();
    }
    
    private void initUI() {
        setTitle("Sistema de Clima");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 550);
        setLocationRelativeTo(null);
        
        JPanel painelPrincipal = new JPanel(new BorderLayout(10, 10));
        painelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JPanel painelBusca = criarPainelBusca();
        JScrollPane painelResultados = criarPainelResultados();
        JPanel painelStatus = criarPainelStatus();
        
        painelPrincipal.add(painelBusca, BorderLayout.NORTH);
        painelPrincipal.add(painelResultados, BorderLayout.CENTER);
        painelPrincipal.add(painelStatus, BorderLayout.SOUTH);
        
        add(painelPrincipal);
        
        mensagemBoasVindas();
        setVisible(true);
    }
    
    private JPanel criarPainelBusca() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Buscar Cidade"));
        
        JLabel labelTitulo = new JLabel("Previsão do Tempo", SwingConstants.CENTER);
        labelTitulo.setFont(new Font("Arial", Font.BOLD, 22));
        
        campoCidade = new JTextField();
        campoCidade.setText("São Paulo, BR");
        campoCidade.setFont(new Font("Arial", Font.PLAIN, 14));
        
        botaoBuscar = new JButton("Buscar Clima");
        botaoBuscar.setFont(new Font("Arial", Font.BOLD, 14));
        botaoBuscar.setBackground(new Color(42, 82, 152));
        botaoBuscar.setForeground(Color.WHITE);
        botaoBuscar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        botaoBuscar.addActionListener(e -> buscarClima());
        campoCidade.addActionListener(e -> buscarClima());
        
        JPanel painelCampoBotao = new JPanel(new BorderLayout(10, 10));
        painelCampoBotao.add(campoCidade, BorderLayout.CENTER);
        painelCampoBotao.add(botaoBuscar, BorderLayout.EAST);
        
        panel.add(labelTitulo, BorderLayout.NORTH);
        panel.add(painelCampoBotao, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JScrollPane criarPainelResultados() {
        areaResultado = new JTextArea();
        areaResultado.setEditable(false);
        areaResultado.setFont(new Font("Monospaced", Font.PLAIN, 12));
        areaResultado.setMargin(new Insets(15, 15, 15, 15));
        areaResultado.setBackground(new Color(240, 244, 250));
        
        JScrollPane scrollPane = new JScrollPane(areaResultado);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Informações do Clima"));
        
        return scrollPane;
    }
    
    private JPanel criarPainelStatus() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        labelStatus = new JLabel("Pronto para buscar");
        labelStatus.setFont(new Font("Arial", Font.ITALIC, 11));
        barra = new JProgressBar();
        barra.setIndeterminate(true);
        barra.setVisible(false);
        panel.add(labelStatus);
        panel.add(barra);
        return panel;
    }
    
    private void buscarClima() {
        String cidade = campoCidade.getText().trim();
        
        if (cidade.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Digite o nome de uma cidade!", 
                "Aviso", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        botaoBuscar.setEnabled(false);
        botaoBuscar.setText("Buscando...");
        labelStatus.setText("Buscando dados para: " + cidade);
        barra.setVisible(true);
        areaResultado.setText("Carregando...\nAguarde um momento.\n");
        
        new SwingWorker<RespostaApi, Void>() {
            @Override
            protected RespostaApi doInBackground() throws Exception {
                return requisicaoApi.buscarClima(cidade);
            }
            
            @Override
            protected void done() {
                try {
                    RespostaApi resposta = get();
                    exibirDados(resposta);
                    labelStatus.setText("Dados atualizados com sucesso!");
                } catch (Exception e) {
                    tratarErro(e);
                } finally {
                    botaoBuscar.setEnabled(true);
                    botaoBuscar.setText("Buscar Clima");
                    barra.setVisible(false);
                }
            }
        }.execute();
    }
    
    private void exibirDados(RespostaApi resposta) {
        StringBuilder sb = new StringBuilder();
        
        sb.append("═".repeat(50)).append("\n");
        sb.append(String.format("  CLIMA EM %s\n", resposta.getResolvedAddress()));
        sb.append("═".repeat(50)).append("\n\n");
        
        if (resposta.getCurrentConditions() != null) {
            long epoch = resposta.getCurrentConditions().getDatetimeEpoch();
            if (epoch > 0) {
                LocalDateTime dataHora = LocalDateTime.ofInstant(Instant.ofEpochSecond(epoch), ZoneId.systemDefault());
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss - dd/MM/yyyy");
                sb.append(String.format("Hora da consulta: %s\n\n", dataHora.format(formatter)));
            }
        }
    
        sb.append(" TEMPERATURAS:\n");
        if (resposta.getCurrentConditions() != null) {
            sb.append(String.format("   • Atual:   %.1f°C\n", resposta.getCurrentConditions().getTemp()));
        }
        if (resposta.getDays() != null && resposta.getDays().length > 0) {
            sb.append(String.format("   • Máxima:  %.1f°C\n", resposta.getDays()[0].getTempmax()));
            sb.append(String.format("   • Mínima:  %.1f°C\n\n", resposta.getDays()[0].getTempmin()));
        }

        if (resposta.getCurrentConditions() != null) {
            sb.append(" UMIDADE:        ").append(resposta.getCurrentConditions().getHumidity()).append("%\n");
            sb.append(" CONDIÇÃO:       ").append(resposta.getCurrentConditions().getConditions()).append("\n\n");
        }
        
        if (resposta.getCurrentConditions() != null) {
            sb.append(" PRECIPITAÇÃO:   ").append(String.format("%.1f", resposta.getCurrentConditions().getPrecip())).append(" mm\n\n");
        }
        
        if (resposta.getCurrentConditions() != null) {
            sb.append(" VENTO:\n");
            sb.append(String.format("   • Velocidade:   %.1f km/h\n", resposta.getCurrentConditions().getWindspeed()));
            sb.append(String.format("   • Direção:      %d° (%s)\n\n", 
                resposta.getCurrentConditions().getWinddir(),
                getDirecaoVentoTexto(resposta.getCurrentConditions().getWinddir())));
        }
        
        sb.append("═".repeat(50)).append("\n");
        
        areaResultado.setText(sb.toString());
    }
    
    private String getDirecaoVentoTexto(int graus) {
        String[] direcoes = {"N", "NE", "L", "SE", "S", "SO", "O", "NO"};
        int index = (int) Math.round((graus % 360) / 45.0) % 8;
        return direcoes[index];
    }
    
    private void tratarErro(Exception e) {
        String mensagem;
        
        if (e instanceof ClimaException) {
            ClimaException ce = (ClimaException) e;
            mensagem = ce.getMensagem();
        } else {
            mensagem = " Erro inesperado!\n\n" + e.getMessage();
        }
        
        JOptionPane.showMessageDialog(this, mensagem, "Erro", JOptionPane.ERROR_MESSAGE);
        areaResultado.setText(" Erro\n\n" + mensagem);
        labelStatus.setText(" Erro na busca");
    }
    
    private void mensagemBoasVindas() {
        String welcome = """
             BEM-VINDO AO SISTEMA DE CLIMA 
            
            Digite o nome de uma cidade e clique em "Buscar Clima".
            
            """;
        areaResultado.setText(welcome);
    }
}