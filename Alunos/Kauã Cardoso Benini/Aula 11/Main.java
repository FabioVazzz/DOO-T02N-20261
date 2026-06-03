package com.weather;

import javax.swing.SwingUtilities;

public class Main {

    public static void main(String[] args) {
        // inicia a janela na thread certa (requisito do Swing)
        SwingUtilities.invokeLater(() -> {
            Tela tela = new Tela();
            tela.setVisible(true);
        });
    }
}
