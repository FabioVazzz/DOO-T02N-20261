package fag;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;


public class Principal extends JFrame {
	
	private static final long serialVersionUID = 1L;
    
    private static final Color COR_FUNDO        = new Color(15, 23, 42);
    private static final Color COR_CARD         = new Color(30, 41, 59);
    private static final Color COR_CARD_BORDA   = new Color(51, 65, 85);
    private static final Color COR_DESTAQUE     = new Color(56, 189, 248);
    private static final Color COR_TEXTO        = new Color(226, 232, 240);
    private static final Color COR_TEXTO_FRACO  = new Color(148, 163, 184);
    private static final Color COR_ERRO         = new Color(252, 165, 165);
    private static final Color COR_BOTAO        = new Color(14, 165, 233);
    private static final Color COR_BOTAO_HOVER  = new Color(56, 189, 248);

    private static final Font FONTE_TITULO      = new Font("SansSerif", Font.BOLD, 22);
    private static final Font FONTE_TEMP        = new Font("SansSerif", Font.BOLD, 64);
    private static final Font FONTE_CIDADE      = new Font("SansSerif", Font.BOLD, 18);
    private static final Font FONTE_LABEL       = new Font("SansSerif", Font.PLAIN, 12);
    private static final Font FONTE_VALOR       = new Font("SansSerif", Font.BOLD, 15);
    private static final Font FONTE_CAMPO       = new Font("SansSerif", Font.PLAIN, 14);
    private static final Font FONTE_BOTAO       = new Font("SansSerif", Font.BOLD, 14);

    private JTextField campoCidade;
    private JButton    botaoBuscar;
    private JLabel     lblStatus;

    private JPanel  painelResultado;
    private JLabel  lblCidade;
    private JLabel  lblTemp;
    private JLabel  lblCondicao;
    private JLabel  lblMaxMin;
    private JLabel  lblUmidadeVal;
    private JLabel  lblPrecipVal;
    private JLabel  lblVentoVal;
    private JLabel  lblDirVentoVal;

    private final ServicoClima servico = new ServicoClima();

    public Principal() {
        super("Aplicativo Clima/Tempo");
        configurarJanela();
        construirUI();
    }

    private void configurarJanela() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(480, 680);
        setMinimumSize(new Dimension(420, 580));
        setLocationRelativeTo(null);
        getContentPane().setBackground(COR_FUNDO);
    }

    private void construirUI() {
        JPanel raiz = new JPanel();
        raiz.setLayout(new BorderLayout(0, 0));
        raiz.setBackground(COR_FUNDO);
        raiz.setBorder(new EmptyBorder(24, 24, 24, 24));
        setContentPane(raiz);

        raiz.add(criarCabecalho(),     BorderLayout.NORTH);
        raiz.add(criarPainelBusca(),   BorderLayout.CENTER);
    }

    private JPanel criarCabecalho() {
        JPanel painel = new JPanel(new BorderLayout());
        painel.setBackground(COR_FUNDO);
        painel.setBorder(new EmptyBorder(0, 0, 20, 0));

        JLabel titulo = new JLabel("☁  Clima/Tempo", SwingConstants.CENTER);
        titulo.setFont(FONTE_TITULO);
        titulo.setForeground(COR_DESTAQUE);
        painel.add(titulo, BorderLayout.CENTER);
        return painel;
    }

    private JPanel criarPainelBusca() {
        JPanel painel = new JPanel();
        painel.setLayout(new BoxLayout(painel, BoxLayout.Y_AXIS));
        painel.setBackground(COR_FUNDO);

        JPanel linhaBusca = new JPanel(new BorderLayout(8, 0));
        linhaBusca.setBackground(COR_FUNDO);
        linhaBusca.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));

        campoCidade = new JTextField("Cascavel,BR");
        campoCidade.setFont(FONTE_CAMPO);
        campoCidade.setForeground(COR_TEXTO);
        campoCidade.setBackground(COR_CARD);
        campoCidade.setCaretColor(COR_DESTAQUE);
        campoCidade.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(COR_CARD_BORDA, 1, true),
                new EmptyBorder(8, 12, 8, 12)));
        campoCidade.addActionListener(e -> realizarBusca());

        botaoBuscar = criarBotao("Buscar");
        botaoBuscar.addActionListener(e -> realizarBusca());

        linhaBusca.add(campoCidade, BorderLayout.CENTER);
        linhaBusca.add(botaoBuscar, BorderLayout.EAST);

        lblStatus = new JLabel(" ");
        lblStatus.setFont(FONTE_LABEL);
        lblStatus.setForeground(COR_ERRO);
        lblStatus.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblStatus.setBorder(new EmptyBorder(6, 0, 0, 0));

        painelResultado = criarPainelResultado();
        painelResultado.setVisible(false);

        painel.add(linhaBusca);
        painel.add(lblStatus);
        painel.add(Box.createVerticalStrut(16));
        painel.add(painelResultado);

        return painel;
    }

    private JPanel criarPainelResultado() {
        JPanel card = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(COR_CARD);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 16, 16));
                g2.setColor(COR_CARD_BORDA);
                g2.draw(new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, 16, 16));
                g2.dispose();
            }
        };
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(new Color(0, 0, 0, 0));
        card.setOpaque(false);
        card.setBorder(new EmptyBorder(24, 28, 24, 28));

        lblCidade = criarLabel("", FONTE_CIDADE, COR_TEXTO);
        lblCidade.setAlignmentX(Component.CENTER_ALIGNMENT);

        lblTemp = criarLabel("", FONTE_TEMP, COR_DESTAQUE);
        lblTemp.setAlignmentX(Component.CENTER_ALIGNMENT);

        lblCondicao = criarLabel("", FONTE_VALOR, COR_TEXTO_FRACO);
        lblCondicao.setAlignmentX(Component.CENTER_ALIGNMENT);

        lblMaxMin = criarLabel("", FONTE_LABEL, COR_TEXTO_FRACO);
        lblMaxMin.setAlignmentX(Component.CENTER_ALIGNMENT);

        JSeparator sep = new JSeparator();
        sep.setForeground(COR_CARD_BORDA);
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));

        JPanel grade = criarGradeDetalhes();

        card.add(lblCidade);
        card.add(Box.createVerticalStrut(4));
        card.add(lblTemp);
        card.add(Box.createVerticalStrut(4));
        card.add(lblCondicao);
        card.add(Box.createVerticalStrut(2));
        card.add(lblMaxMin);
        card.add(Box.createVerticalStrut(20));
        card.add(sep);
        card.add(Box.createVerticalStrut(20));
        card.add(grade);

        return card;
    }

    private JPanel criarGradeDetalhes() {
        JPanel grade = new JPanel(new GridLayout(2, 4, 12, 16));
        grade.setOpaque(false);

        lblUmidadeVal  = criarLabel("--",   FONTE_VALOR, COR_TEXTO);
        lblPrecipVal   = criarLabel("--",   FONTE_VALOR, COR_TEXTO);
        lblVentoVal    = criarLabel("--",   FONTE_VALOR, COR_TEXTO);
        lblDirVentoVal = criarLabel("--",   FONTE_VALOR, COR_TEXTO);

        grade.add(criarColunaDetalhe("💧 Umidade",      lblUmidadeVal));
        grade.add(criarColunaDetalhe("🌧 Precipitação", lblPrecipVal));
        grade.add(criarColunaDetalhe("💨 Vento",        lblVentoVal));
        grade.add(criarColunaDetalhe("🧭 Direção",      lblDirVentoVal));

        return grade;
    }

    private JPanel criarColunaDetalhe(String rotulo, JLabel lblValor) {
        JPanel col = new JPanel();
        col.setLayout(new BoxLayout(col, BoxLayout.Y_AXIS));
        col.setOpaque(false);

        JLabel lblRotulo = criarLabel(rotulo, FONTE_LABEL, COR_TEXTO_FRACO);
        lblRotulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblValor.setAlignmentX(Component.CENTER_ALIGNMENT);

        col.add(lblRotulo);
        col.add(Box.createVerticalStrut(4));
        col.add(lblValor);
        return col;
    }

    private void realizarBusca() {
        String cidade = campoCidade.getText().trim();
        if (cidade.isEmpty()) {
            lblStatus.setForeground(COR_ERRO);
            lblStatus.setText("Digite o nome de uma cidade.");
            return;
        }

        lblStatus.setForeground(COR_TEXTO_FRACO);
        lblStatus.setText("Buscando dados...");
        botaoBuscar.setEnabled(false);
        painelResultado.setVisible(false);

        SwingWorker<DadosClima, Void> worker = new SwingWorker<>() {
            @Override
            protected DadosClima doInBackground() throws Exception {
                return servico.buscarClima(cidade);
            }

            @Override
            protected void done() {
                botaoBuscar.setEnabled(true);
                try {
                    DadosClima dados = get();
                    exibirResultado(dados);
                    lblStatus.setText(" ");
                } catch (Exception ex) {
                    lblStatus.setForeground(COR_ERRO);
                    String msg = ex.getCause() != null ? ex.getCause().getMessage() : ex.getMessage();
                    lblStatus.setText("Erro: " + msg);
                }
            }
        };
        worker.execute();
    }

    private void exibirResultado(DadosClima dados) {
        lblCidade.setText(dados.getCidade());
        lblTemp.setText(String.format("%.1f°C", dados.getTempAtual()));
        lblCondicao.setText(dados.getCondicao());
        lblMaxMin.setText(String.format("↑ %.1f°C   ↓ %.1f°C",
                dados.getTempMaxima(), dados.getTempMinima()));
        lblUmidadeVal.setText(String.format("%.0f%%",   dados.getUmidade()));
        lblPrecipVal.setText(String.format("%.1f mm",   dados.getPrecipitacao()));
        lblVentoVal.setText(String.format("%.1f km/h",  dados.getVelocidadeVento()));
        lblDirVentoVal.setText(dados.getDirecaoVento());

        painelResultado.setVisible(true);
        pack();
        setLocationRelativeTo(null);
    }

    private JLabel criarLabel(String texto, Font fonte, Color cor) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(fonte);
        lbl.setForeground(cor);
        return lbl;
    }

    private JButton criarBotao(String texto) {
        JButton btn = new JButton(texto) {
            private boolean hover = false;
            {
                addMouseListener(new MouseAdapter() {
                    public void mouseEntered(MouseEvent e) { hover = true;  repaint(); }
                    public void mouseExited (MouseEvent e) { hover = false; repaint(); }
                });
            }
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(hover ? COR_BOTAO_HOVER : COR_BOTAO);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 10, 10));
                g2.setColor(Color.WHITE);
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth()  - fm.stringWidth(getText())) / 2;
                int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2.drawString(getText(), x, y);
                g2.dispose();
            }
        };
        btn.setFont(FONTE_BOTAO);
        btn.setForeground(Color.WHITE);
        btn.setPreferredSize(new Dimension(110, 42));
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }
}
