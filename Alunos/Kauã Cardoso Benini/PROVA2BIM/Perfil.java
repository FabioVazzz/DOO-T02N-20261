package com.tvtracker.ui;

import com.tvtracker.service.SeriesService;

import javax.swing.*;
import java.awt.*;

/**
 * Janela para o usuário definir ou alterar seu nome.
 */
public class Perfil extends JDialog {

    private final SeriesService service;

    // Campo de texto onde o usuário digita seu nome
    private JTextField nameField;

    /**
     * Construtor — configura e constrói o diálogo.
     */
    public Perfil(Window parent, SeriesService service) {
        // APPLICATION_MODAL bloqueia toda a aplicação enquanto o diálogo estiver aberto
        super(parent, "Configurar Perfil", ModalityType.APPLICATION_MODAL);
        this.service = service;

        setSize(360, 210);
        setLocationRelativeTo(parent); // Centraliza relativo ao pai (ou na tela se null)
        setResizable(false);           // Tamanho fixo

        // DO_NOTHING_ON_CLOSE desabilita o botão X da janela
        // Isso força o usuário a usar o botão "Salvar e Continuar"
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        buildUI(); // Constrói os componentes visuais
    }

    /**
     * Constrói toda a interface do diálogo:
     * - Título de boas-vindas
     * - Instrução
     * - Campo de texto para o nome
     * - Botão "Salvar e Continuar"
     */
    private void buildUI() {
        // Painel principal com GridBagLayout para organizar os componentes
        JPanel content = new JPanel(new GridBagLayout());
        content.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6); // Espaçamento entre elementos
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // ===== TÍTULO =====
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2; // Ocupa 2 colunas
        JLabel title = new JLabel("Bem-vindo ao TV Series Tracker!");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 14f)); // Negrito tamanho 14
        title.setHorizontalAlignment(SwingConstants.CENTER);
        content.add(title, gbc);

        // ===== SUBTÍTULO =====
        gbc.gridy = 1;
        JLabel subtitle = new JLabel("Por favor, informe seu nome ou apelido:");
        subtitle.setHorizontalAlignment(SwingConstants.CENTER);
        content.add(subtitle, gbc);

        // ===== LABEL "Nome/Apelido:" =====
        gbc.gridy = 2; gbc.gridwidth = 1; // Volta para 1 coluna
        gbc.gridx = 0; gbc.weightx = 0;
        content.add(new JLabel("Nome/Apelido:"), gbc);

        // ===== CAMPO DE TEXTO =====
        gbc.gridx = 1; gbc.weightx = 1.0; // Expande horizontalmente

        // Preenche com o nome atual (se já existir, ex: ao editar o perfil)
        String current = service.getUserProfile().getName();
        nameField = new JTextField(current != null ? current : "", 16);
        content.add(nameField, gbc);

        add(content, BorderLayout.CENTER);

        // ===== BOTÃO SALVAR =====
        JButton saveBtn = new JButton("Salvar e Continuar");
        saveBtn.addActionListener(e -> save());

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnPanel.add(saveBtn);
        add(btnPanel, BorderLayout.SOUTH);

        // Enter aciona o botão de salvar
        getRootPane().setDefaultButton(saveBtn);

        // Coloca o cursor no campo de texto automaticamente ao abrir
        nameField.requestFocusInWindow();
    }

    /**
     * Valida e salva o nome digitado pelo usuário.
     * Se o campo estiver vazio, exibe aviso e não fecha.
     * Se válido, salva via serviço e fecha o diálogo.
     */
    private void save() {
        String name = nameField.getText().trim(); // Remove espaços nas bordas

        // Valida que o campo não está vazio
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Por favor, insira um nome ou apelido para continuar.",
                    "Campo obrigatorio", JOptionPane.WARNING_MESSAGE);
            return; // Não fecha — aguarda o usuário preencher
        }

        service.setUserName(name); // Salva o nome no perfil e persiste no JSON
        dispose();                 // Fecha o diálogo e libera memória
    }
}
