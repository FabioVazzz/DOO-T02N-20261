import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;
import javax.imageio.ImageIO;

public class TelaDetalhes extends JDialog {

    private Serie serie;
    private GerenciadorSeries gerenciador;

    public TelaDetalhes(Frame pai, Serie serie, GerenciadorSeries gerenciador) {
        super(pai, "Detalhes da Série", true);
        this.serie = serie;
        this.gerenciador = gerenciador;

        configurarJanela();
        construirInterface();
        setVisible(true);
    }

    private void configurarJanela() {
        setSize(650, 520);
        setLocationRelativeTo(getOwner());
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        getContentPane().setBackground(Cores.FUNDO_ESCURO);
    }

    private void construirInterface() {
        setLayout(new BorderLayout(0, 0));

        JPanel cabecalho = new JPanel(new BorderLayout());
        cabecalho.setBackground(Cores.FUNDO_PAINEL);
        cabecalho.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel lblNome = new JLabel(serie.getNome());
        lblNome.setFont(Cores.FONTE_TITULO);
        lblNome.setForeground(Cores.DESTAQUE);
        cabecalho.add(lblNome, BorderLayout.CENTER);

        JLabel lblNota = new JLabel(serie.getNotaFormatada());
        lblNota.setFont(Cores.FONTE_SUBTITULO);
        lblNota.setForeground(Cores.VERDE_SUCESSO);
        cabecalho.add(lblNota, BorderLayout.EAST);

        add(cabecalho, BorderLayout.NORTH);

        JPanel centro = new JPanel(new BorderLayout(15, 0));
        centro.setBackground(Cores.FUNDO_ESCURO);
        centro.setBorder(BorderFactory.createEmptyBorder(15, 20, 10, 20));

        JLabel lblImagem = new JLabel();
        lblImagem.setPreferredSize(new Dimension(150, 220));
        lblImagem.setHorizontalAlignment(SwingConstants.CENTER);
        lblImagem.setVerticalAlignment(SwingConstants.CENTER);
        lblImagem.setBackground(Cores.FUNDO_CARD);
        lblImagem.setOpaque(true);
        lblImagem.setBorder(BorderFactory.createLineBorder(Cores.BORDA));
        lblImagem.setText("Sem imagem");
        lblImagem.setForeground(Cores.TEXTO_SECUNDARIO);
        lblImagem.setFont(Cores.FONTE_PEQUENA);

        carregarImagemAsync(lblImagem);

        centro.add(lblImagem, BorderLayout.WEST);

        JPanel painelInfo = new JPanel();
        painelInfo.setLayout(new BoxLayout(painelInfo, BoxLayout.Y_AXIS));
        painelInfo.setBackground(Cores.FUNDO_ESCURO);

        painelInfo.add(criarLinhaInfo("Idioma:", serie.getIdioma()));
        painelInfo.add(Box.createVerticalStrut(8));
        painelInfo.add(criarLinhaInfo("Gêneros:", serie.getGeneros()));
        painelInfo.add(Box.createVerticalStrut(8));
        painelInfo.add(criarLinhaInfo("Estado:", serie.getEstado()));
        painelInfo.add(Box.createVerticalStrut(8));
        painelInfo.add(criarLinhaInfo("Estreia:", serie.getDataEstreia()));
        painelInfo.add(Box.createVerticalStrut(8));
        painelInfo.add(criarLinhaInfo("Término:", serie.getDataTermino()));
        painelInfo.add(Box.createVerticalStrut(8));
        painelInfo.add(criarLinhaInfo("Emissora:", serie.getEmissora()));
        painelInfo.add(Box.createVerticalStrut(12));

        JLabel lblTituloResumo = new JLabel("Resumo:");
        lblTituloResumo.setFont(Cores.FONTE_NEGRITO);
        lblTituloResumo.setForeground(Cores.TEXTO_SECUNDARIO);
        painelInfo.add(lblTituloResumo);
        painelInfo.add(Box.createVerticalStrut(4));

        JTextArea txtResumo = new JTextArea(serie.getResumo());
        txtResumo.setFont(Cores.FONTE_NORMAL);
        txtResumo.setForeground(Cores.TEXTO_PRINCIPAL);
        txtResumo.setBackground(Cores.FUNDO_CARD);
        txtResumo.setLineWrap(true);
        txtResumo.setWrapStyleWord(true);
        txtResumo.setEditable(false);
        txtResumo.setBorder(BorderFactory.createEmptyBorder(6, 8, 6, 8));
        txtResumo.setRows(4);

        JScrollPane scrollResumo = new JScrollPane(txtResumo);
        scrollResumo.setBorder(BorderFactory.createLineBorder(Cores.BORDA));
        scrollResumo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));
        scrollResumo.setAlignmentX(Component.LEFT_ALIGNMENT);
        painelInfo.add(scrollResumo);

        centro.add(painelInfo, BorderLayout.CENTER);
        add(centro, BorderLayout.CENTER);

        JPanel rodape = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 12));
        rodape.setBackground(Cores.FUNDO_PAINEL);
        rodape.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Cores.BORDA));

        JButton btnFavorito = ComponentesUI.criarBotao("Favoritar", true);
        JButton btnAssistida = ComponentesUI.criarBotao("Já Assisti", false);
        JButton btnQuerAssistir = ComponentesUI.criarBotao("Quero Assistir", false);
        JButton btnFechar = ComponentesUI.criarBotao("Fechar", false);

        btnFavorito.addActionListener(e -> {
            boolean adicionou = gerenciador.adicionarFavorito(serie);
            if (adicionou) {
                JOptionPane.showMessageDialog(this,
                        "\"" + serie.getNome() + "\" adicionada aos Favoritos!",
                        "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                        "Esta série já está nos seus Favoritos.",
                        "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        });

        btnAssistida.addActionListener(e -> {
            boolean adicionou = gerenciador.adicionarAssistida(serie);
            if (adicionou) {
                JOptionPane.showMessageDialog(this,
                        "\"" + serie.getNome() + "\" adicionada às Séries Assistidas!",
                        "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                        "Esta série já está nas Séries Assistidas.",
                        "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        });

        btnQuerAssistir.addActionListener(e -> {
            boolean adicionou = gerenciador.adicionarQuerAssistir(serie);
            if (adicionou) {
                JOptionPane.showMessageDialog(this,
                        "\"" + serie.getNome() + "\" adicionada à lista Quero Assistir!",
                        "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                        "Esta série já está na lista Quero Assistir.",
                        "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        });

        btnFechar.addActionListener(e -> dispose());

        rodape.add(btnFavorito);
        rodape.add(btnAssistida);
        rodape.add(btnQuerAssistir);
        rodape.add(btnFechar);

        add(rodape, BorderLayout.SOUTH);
    }

    private JPanel criarLinhaInfo(String rotulo, String valor) {
        JPanel linha = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        linha.setBackground(Cores.FUNDO_ESCURO);
        linha.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblRotulo = new JLabel(rotulo);
        lblRotulo.setFont(Cores.FONTE_NEGRITO);
        lblRotulo.setForeground(Cores.TEXTO_SECUNDARIO);

        JLabel lblValor = new JLabel(valor);
        lblValor.setFont(Cores.FONTE_NORMAL);
        lblValor.setForeground(Cores.TEXTO_PRINCIPAL);

        linha.add(lblRotulo);
        linha.add(lblValor);

        return linha;
    }

    private void carregarImagemAsync(JLabel lblImagem) {
        if (serie.getUrlImagem() == null || serie.getUrlImagem().isEmpty()) {
            return;
        }

        Thread thread = new Thread(() -> {
            try {
                URL url = new URL(serie.getUrlImagem());
                BufferedImage imagem = ImageIO.read(url);
                if (imagem != null) {
                    Image imagemRedim = imagem.getScaledInstance(150, 210, Image.SCALE_SMOOTH);
                    SwingUtilities.invokeLater(() -> {
                        lblImagem.setIcon(new ImageIcon(imagemRedim));
                        lblImagem.setText(null);
                    });
                }
            } catch (Exception e) {
                System.out.println("Não foi possível carregar a imagem: " + e.getMessage());
            }
        });
        thread.setDaemon(true);
        thread.start();
    }
}