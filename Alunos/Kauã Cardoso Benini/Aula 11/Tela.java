package com.weather;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Tela extends JFrame {

    // campos que o usuario vai preencher
    private JTextField campoCidade;
    private JPasswordField campoChave;

    // labels que vao mostrar o resultado
    private JLabel labelCidade;
    private JLabel labelTemp;
    private JLabel labelSensacao;
    private JLabel labelCondicao;
    private JLabel labelUmidade;
    private JLabel labelVento;
    private JLabel labelChuva;
    private JLabel labelMax;
    private JLabel labelMin;

    // painel que aparece so depois de buscar
    private JPanel painelResultado;

    public Tela() {
        setTitle("App de Clima");
        setSize(480, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // painel principal com fundo escuro
        JPanel painelPrincipal = new JPanel();
        painelPrincipal.setLayout(new BoxLayout(painelPrincipal, BoxLayout.Y_AXIS));
        painelPrincipal.setBackground(new Color(30, 30, 45));
        painelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

        // titulo
        JLabel titulo = new JLabel("Previsão do Tempo");
        titulo.setFont(new Font("Arial", Font.BOLD, 22));
        titulo.setForeground(Color.WHITE);
        titulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        painelPrincipal.add(titulo);

        painelPrincipal.add(Box.createVerticalStrut(5));

        JLabel subtitulo = new JLabel("Visual Crossing API");
        subtitulo.setFont(new Font("Arial", Font.PLAIN, 12));
        subtitulo.setForeground(new Color(150, 150, 180));
        subtitulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        painelPrincipal.add(subtitulo);

        painelPrincipal.add(Box.createVerticalStrut(20));

        // campo chave de api
        JLabel lblChave = new JLabel("Chave de API:");
        lblChave.setForeground(new Color(180, 180, 210));
        lblChave.setFont(new Font("Arial", Font.PLAIN, 13));
        lblChave.setAlignmentX(Component.LEFT_ALIGNMENT);
        painelPrincipal.add(lblChave);

        painelPrincipal.add(Box.createVerticalStrut(5));

        campoChave = new JPasswordField();
        campoChave.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        campoChave.setFont(new Font("Arial", Font.PLAIN, 13));
        campoChave.setBackground(new Color(50, 50, 70));
        campoChave.setForeground(Color.WHITE);
        campoChave.setCaretColor(Color.WHITE);
        campoChave.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(80, 80, 110)),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        campoChave.setAlignmentX(Component.LEFT_ALIGNMENT);
        painelPrincipal.add(campoChave);

        painelPrincipal.add(Box.createVerticalStrut(15));

        // campo cidade
        JLabel lblCidade = new JLabel("Cidade:");
        lblCidade.setForeground(new Color(180, 180, 210));
        lblCidade.setFont(new Font("Arial", Font.PLAIN, 13));
        lblCidade.setAlignmentX(Component.LEFT_ALIGNMENT);
        painelPrincipal.add(lblCidade);

        painelPrincipal.add(Box.createVerticalStrut(5));

        // linha com campo + botao
        JPanel linhaBusca = new JPanel(new BorderLayout(8, 0));
        linhaBusca.setBackground(new Color(30, 30, 45));
        linhaBusca.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        linhaBusca.setAlignmentX(Component.LEFT_ALIGNMENT);

        campoCidade = new JTextField();
        campoCidade.setFont(new Font("Arial", Font.PLAIN, 13));
        campoCidade.setBackground(new Color(50, 50, 70));
        campoCidade.setForeground(Color.WHITE);
        campoCidade.setCaretColor(Color.WHITE);
        campoCidade.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(80, 80, 110)),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        linhaBusca.add(campoCidade, BorderLayout.CENTER);

        JButton botaoBuscar = new JButton("Buscar");
        botaoBuscar.setBackground(new Color(70, 130, 220));
        botaoBuscar.setForeground(Color.WHITE);
        botaoBuscar.setFont(new Font("Arial", Font.BOLD, 13));
        botaoBuscar.setFocusPainted(false);
        botaoBuscar.setBorderPainted(false);
        botaoBuscar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        linhaBusca.add(botaoBuscar, BorderLayout.EAST);

        painelPrincipal.add(linhaBusca);

        painelPrincipal.add(Box.createVerticalStrut(20));

        // painel de resultado (começa escondido)
        painelResultado = new JPanel();
        painelResultado.setLayout(new BoxLayout(painelResultado, BoxLayout.Y_AXIS));
        painelResultado.setBackground(new Color(30, 30, 45));
        painelResultado.setAlignmentX(Component.LEFT_ALIGNMENT);
        painelResultado.setVisible(false);

        // cidade encontrada
        labelCidade = criarLabel("", 16, Font.BOLD, Color.WHITE);
        painelResultado.add(labelCidade);

        painelResultado.add(Box.createVerticalStrut(3));

        labelCondicao = criarLabel("", 13, Font.PLAIN, new Color(160, 160, 200));
        painelResultado.add(labelCondicao);

        painelResultado.add(Box.createVerticalStrut(10));

        // temperatura grande
        labelTemp = criarLabel("", 56, Font.BOLD, Color.WHITE);
        painelResultado.add(labelTemp);

        painelResultado.add(Box.createVerticalStrut(3));

        labelSensacao = criarLabel("", 13, Font.PLAIN, new Color(160, 160, 200));
        painelResultado.add(labelSensacao);

        painelResultado.add(Box.createVerticalStrut(18));

        // linha separadora
        JSeparator sep = new JSeparator();
        sep.setForeground(new Color(60, 60, 85));
        sep.setBackground(new Color(60, 60, 85));
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        painelResultado.add(sep);

        painelResultado.add(Box.createVerticalStrut(15));

        // cards de detalhes
        JPanel painelCards = new JPanel(new GridLayout(1, 3, 10, 0));
        painelCards.setBackground(new Color(30, 30, 45));
        painelCards.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));
        painelCards.setAlignmentX(Component.LEFT_ALIGNMENT);

        labelUmidade = new JLabel("—");
        labelVento = new JLabel("—");
        labelChuva = new JLabel("—");

        painelCards.add(criarCard("Umidade", labelUmidade));
        painelCards.add(criarCard("Precipitação", labelChuva));
        painelCards.add(criarCard("Vento", labelVento));

        painelResultado.add(painelCards);

        painelResultado.add(Box.createVerticalStrut(12));

        // max e min
        JPanel painelMaxMin = new JPanel(new GridLayout(1, 2, 10, 0));
        painelMaxMin.setBackground(new Color(30, 30, 45));
        painelMaxMin.setMaximumSize(new Dimension(Integer.MAX_VALUE, 75));
        painelMaxMin.setAlignmentX(Component.LEFT_ALIGNMENT);

        labelMax = new JLabel("—");
        labelMin = new JLabel("—");

        painelMaxMin.add(criarCard("Máxima", labelMax));
        painelMaxMin.add(criarCard("Mínima", labelMin));

        painelResultado.add(painelMaxMin);

        painelPrincipal.add(painelResultado);

        add(painelPrincipal);

        // acao do botao buscar
        botaoBuscar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarClima();
            }
        });

        // tambem busca ao apertar Enter no campo cidade
        campoCidade.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarClima();
            }
        });
    }

    // monta um card de detalhe (ex: umidade, vento)
    private JPanel criarCard(String titulo, JLabel labelValor) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(new Color(45, 45, 65));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(65, 65, 90)),
                BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));

        JLabel lblTitulo = new JLabel(titulo.toUpperCase());
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 10));
        lblTitulo.setForeground(new Color(120, 120, 160));

        labelValor.setFont(new Font("Arial", Font.BOLD, 16));
        labelValor.setForeground(Color.WHITE);

        card.add(lblTitulo);
        card.add(Box.createVerticalStrut(5));
        card.add(labelValor);

        return card;
    }

    // atalho pra criar JLabel ja formatado
    private JLabel criarLabel(String texto, int tamanho, int estilo, Color cor) {
        JLabel label = new JLabel(texto);
        label.setFont(new Font("Arial", estilo, tamanho));
        label.setForeground(cor);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    // chamado quando o usuario clica em Buscar
    private void buscarClima() {
        String chave = new String(campoChave.getPassword()).trim();
        String cidade = campoCidade.getText().trim();

        if (chave.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Coloque a chave de API!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (cidade.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Digite uma cidade!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // desabilita o botao enquanto busca pra nao clicar 2x
        botaoBuscarRef = (JButton) ((JPanel) campoCidade.getParent()).getComponent(1);
        botaoBuscarRef.setEnabled(false);
        botaoBuscarRef.setText("Buscando...");

        // faz a requisicao em outra thread pra nao travar a tela
        SwingWorker<DadosClima, Void> worker = new SwingWorker<>() {

            @Override
            protected DadosClima doInBackground() throws Exception {
                ApiClima api = new ApiClima(chave);
                return api.buscar(cidade);
            }

            @Override
            protected void done() {
                try {
                    DadosClima dados = get();
                    mostrarResultado(dados);
                } catch (Exception ex) {
                    String msg = ex.getCause() != null ? ex.getCause().getMessage() : ex.getMessage();
                    JOptionPane.showMessageDialog(Tela.this, msg, "Erro", JOptionPane.ERROR_MESSAGE);
                } finally {
                    botaoBuscarRef.setEnabled(true);
                    botaoBuscarRef.setText("Buscar");
                }
            }
        };

        worker.execute();
    }

    // atualiza os labels com os dados recebidos
    private void mostrarResultado(DadosClima d) {
        labelCidade.setText(d.cidade);
        labelCondicao.setText(d.condicao);
        labelTemp.setText(String.format("%.0f°C", d.tempAtual));
        labelSensacao.setText(String.format("Sensação térmica: %.0f°C", d.sensacao));
        labelUmidade.setText(String.format("%.0f%%", d.umidade));
        labelChuva.setText(String.format("%.1f mm", d.chuva));
        labelVento.setText(String.format("%.0f km/h", d.vento));
        labelMax.setText(String.format("%.0f°C", d.tempMax));
        labelMin.setText(String.format("%.0f°C", d.tempMin));

        painelResultado.setVisible(true);
        painelResultado.revalidate();
        painelResultado.repaint();
    }

    // referencia pro botao (precisa acessar dentro do SwingWorker)
    private JButton botaoBuscarRef;
}
