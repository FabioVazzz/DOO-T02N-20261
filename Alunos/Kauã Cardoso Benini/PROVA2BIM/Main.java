package com.tvtracker;

// Importações das camadas do projeto
import com.tvtracker.persistence.JsonPersistence; // Responsável por salvar/carregar dados em JSON
import com.tvtracker.service.SeriesService;        // Camada de serviço (regras de negócio)
import com.tvtracker.ui.MainFrame;                 // Janela principal do programa
import com.tvtracker.ui.Perfil;         // Janela de configuração do perfil do usuário

import javax.swing.*;


public class Main {

    public static void main(String[] args) {


        SwingUtilities.invokeLater(() -> {


            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {

            }

            // Cria a camada de persistência
            JsonPersistence persistence = new JsonPersistence();

            // Cria o serviço principal, que recebe a persistência e gerencia os dados
            SeriesService service = new SeriesService(persistence);

            // Verifica se o usuário já tem um nome cadastrado
            String name = service.getUserProfile().getName();
            if (name == null || name.trim().isEmpty()) {

                // Se não tiver nome, abre a janela de perfil para o usuário se identificar
                Perfil dialog = new Perfil(null, service);
                dialog.setVisible(true);

                // Se o usuário fechar sem digitar nada, define um nome padrão
                if (service.getUserProfile().getName() == null ||
                        service.getUserProfile().getName().trim().isEmpty()) {
                    service.setUserName("Usuario");
                }
            }

            // Cria e exibe a janela principal do programa
            MainFrame frame = new MainFrame(service);
            frame.setVisible(true);
        });
    }
}
