package com.tvtracker.ui;

import com.tvtracker.model.Series;

import javax.swing.*;
import java.awt.*;

/**
 * DETALHES — janela popup que exibe todas as informações de uma série.
 *
 * É aberta ao dar duplo clique em uma série ou clicar em "Ver Detalhes".
 *
 * Exibe: Nome, Idioma, Gêneros, Nota, Estado, Estreia, Término, Emissora e Sinopse.
 */
public class Detalhes extends JDialog {


    public Detalhes(Window parent, Series series) {
        // Título da janela inclui o nome da série
        // APPLICATION_MODAL bloqueia interação com outras janelas enquanto este diálogo estiver aberto
        super(parent, "Detalhes: " + series.getName(), ModalityType.APPLICATION_MODAL);

        setSize(520, 500);
        setLocationRelativeTo(parent); // Centraliza relativo à janela pai
        setResizable(true);            // Permite redimensionar
        setLayout(new BorderLayout(10, 10));

        // Painel de conteúdo com GridBagLayout (layout flexível para formulários)
        JPanel content = new JPanel(new GridBagLayout());
        content.setBorder(BorderFactory.createEmptyBorder(15, 15, 10, 15));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4); // Espaçamento entre células
        gbc.anchor = GridBagConstraints.NORTHWEST; // Alinha ao topo-esquerdo

        // Adiciona cada campo usando o método auxiliar addField()
        // A variável 'row' controla em qual linha cada campo é inserido
        int row = 0;
        row = addField(content, gbc, row, "Nome:",          series.getName());
        row = addField(content, gbc, row, "Idioma:",        series.getLanguage());
        row = addField(content, gbc, row, "Generos:",       series.getGenresAsString());
        row = addField(content, gbc, row, "Nota:",          series.getRatingAsString());
        row = addField(content, gbc, row, "Estado:",        series.getStatus());
        row = addField(content, gbc, row, "Data de Estreia:",
                series.getPremiered() != null ? series.getPremiered() : "N/A");
        row = addField(content, gbc, row, "Data de Termino:",
                series.getEnded() != null ? series.getEnded() : "Em andamento");
        row = addField(content, gbc, row, "Emissora:",
                series.getNetworkName() != null ? series.getNetworkName() : "N/A");

        // ===== CAMPO ESPECIAL: SINOPSE =====
        // A sinopse usa JTextArea com scroll em vez de JLabel simples,
        // pois pode ser um texto longo que precisa de quebra de linha e rolagem

        // Label "Descricao:" na coluna esquerda
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        JLabel descLabel = new JLabel("Descricao:");
        descLabel.setFont(descLabel.getFont().deriveFont(Font.BOLD));
        content.add(descLabel, gbc);

        // Área de texto com scroll na coluna direita
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.BOTH;  // Expande em ambas as direções
        gbc.weightx = 1.0;                   // Ocupa todo o espaço horizontal disponível
        gbc.weighty = 1.0;                   // Ocupa todo o espaço vertical disponível

        String summaryText = series.getSummary();
        JTextArea summaryArea = new JTextArea(summaryText != null ? summaryText : "N/A");
        summaryArea.setWrapStyleWord(true);  // Quebra linha na palavra (não no meio)
        summaryArea.setLineWrap(true);       // Ativa quebra automática de linha
        summaryArea.setEditable(false);      // Somente leitura
        summaryArea.setBackground(content.getBackground()); // Mesma cor do painel
        summaryArea.setFont(UIManager.getFont("Label.font")); // Mesma fonte dos labels

        JScrollPane summaryScroll = new JScrollPane(summaryArea);
        summaryScroll.setPreferredSize(new Dimension(320, 100)); // Tamanho mínimo da área
        summaryScroll.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        content.add(summaryScroll, gbc);

        // Envolve o conteúdo em JScrollPane para casos de janela muito pequena
        add(new JScrollPane(content), BorderLayout.CENTER);

        // ===== BOTÃO FECHAR =====
        JButton closeBtn = new JButton("Fechar");
        closeBtn.addActionListener(e -> dispose()); // dispose() fecha e libera memória

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.add(closeBtn);
        add(btnPanel, BorderLayout.SOUTH);

        // Define o botão padrão (acionado ao pressionar Enter)
        getRootPane().setDefaultButton(closeBtn);
    }


    private int addField(JPanel panel, GridBagConstraints gbc, int row, String label, String value) {
        // Coluna esquerda: label em negrito
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        gbc.weighty = 0;
        JLabel lbl = new JLabel(label);
        lbl.setFont(lbl.getFont().deriveFont(Font.BOLD)); // Negrito
        panel.add(lbl, gbc);

        // Coluna direita: valor
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL; // Expande horizontalmente
        gbc.weightx = 1.0;
        panel.add(new JLabel(value != null ? value : "N/A"), gbc);

        return row + 1; // Retorna a próxima linha
    }
}
