import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;

public class ComponentesUI {

    public static JButton criarBotao(String texto, boolean principal) {
        JButton btn = new JButton(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (getModel().isPressed()) {
                    g2.setColor(principal ? Cores.DESTAQUE.darker() : Cores.FUNDO_CARD.darker());
                } else if (getModel().isRollover()) {
                    g2.setColor(principal ? Cores.DESTAQUE_HOVER : Cores.FUNDO_CARD.brighter());
                } else {
                    g2.setColor(principal ? Cores.DESTAQUE : Cores.FUNDO_CARD);
                }

                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 10, 10));

                g2.setColor(principal ? Cores.DESTAQUE_HOVER : Cores.BORDA);
                g2.setStroke(new BasicStroke(1f));
                g2.draw(new RoundRectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1, 10, 10));

                g2.dispose();
                super.paintComponent(g);
            }
        };

        btn.setFont(Cores.FONTE_BOTAO);
        btn.setForeground(principal ? Color.WHITE : Cores.TEXTO_PRINCIPAL);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(btn.getPreferredSize().width + 20, 36));
        btn.setOpaque(false);

        return btn;
    }

    public static JTextField criarCampoTexto(String placeholder) {
        JTextField campo = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Cores.FUNDO_CAMPO);
                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 8, 8));
                g2.dispose();
                super.paintComponent(g);

                if (getText().isEmpty() && !isFocusOwner()) {
                    Graphics2D g3 = (Graphics2D) g.create();
                    g3.setColor(Cores.TEXTO_SECUNDARIO);
                    g3.setFont(Cores.FONTE_NORMAL);
                    FontMetrics fm = g3.getFontMetrics();
                    int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
                    g3.drawString(placeholder, 10, y);
                    g3.dispose();
                }
            }
        };

        campo.setOpaque(false);
        campo.setFont(Cores.FONTE_NORMAL);
        campo.setForeground(Cores.TEXTO_PRINCIPAL);
        campo.setCaretColor(Cores.DESTAQUE);
        campo.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        campo.setPreferredSize(new Dimension(300, 36));

        return campo;
    }

    public static JLabel criarTitulo(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(Cores.FONTE_TITULO);
        label.setForeground(Cores.TEXTO_PRINCIPAL);
        return label;
    }

    public static JLabel criarLabel(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(Cores.FONTE_NORMAL);
        label.setForeground(Cores.TEXTO_PRINCIPAL);
        return label;
    }

    public static void estilizarLista(JList<Serie> lista) {
        lista.setBackground(Cores.FUNDO_PAINEL);
        lista.setForeground(Cores.TEXTO_PRINCIPAL);
        lista.setFont(Cores.FONTE_NORMAL);
        lista.setSelectionBackground(Cores.DESTAQUE_SUAVE);
        lista.setSelectionForeground(Cores.DESTAQUE);
        lista.setFixedCellHeight(32);
        lista.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
    }

    public static void estilizarScroll(JScrollPane scroll) {
        scroll.setBorder(BorderFactory.createLineBorder(Cores.BORDA));
        scroll.getViewport().setBackground(Cores.FUNDO_PAINEL);
        scroll.getVerticalScrollBar().setBackground(Cores.FUNDO_PAINEL);
    }

    @SuppressWarnings("unchecked")
    public static void estilizarComboBox(JComboBox<?> combo) {
        combo.setBackground(Cores.FUNDO_CAMPO);
        combo.setForeground(Cores.TEXTO_PRINCIPAL);
        combo.setFont(Cores.FONTE_NORMAL);
        combo.setBorder(BorderFactory.createLineBorder(Cores.BORDA));
    }
}