import javax.swing.*;
import java.awt.*;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                iniciarSistema();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null,
                        "Erro crítico ao iniciar o sistema:\n" + e.getMessage() +
                                "\n\nO programa será encerrado.",
                        "Erro fatal", JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }
        });
    }

    private static void iniciarSistema() {

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.out.println("Look nativo indisponível: " + e.getMessage());
        }

        UIManager.put("OptionPane.background", Cores.FUNDO_PAINEL);
        UIManager.put("Panel.background", Cores.FUNDO_PAINEL);
        UIManager.put("OptionPane.messageForeground", Cores.TEXTO_PRINCIPAL);

        PersistenciaJson persistencia = new PersistenciaJson();

        Usuario usuario = realizarLogin(persistencia);

        if (usuario == null) {
            System.exit(0);
        }

        GerenciadorSeries gerenciador = new GerenciadorSeries(usuario, persistencia);
        new TelaPrincipal(usuario, gerenciador);
    }

    private static Usuario realizarLogin(PersistenciaJson persistencia) {

        JPanel painel = new JPanel(new BorderLayout(0, 12));
        painel.setBackground(Cores.FUNDO_PAINEL);
        painel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel lblMensagem = new JLabel(
                "<html><b style='font-size:13px'>📺 SeriesTracker</b><br><br>" +
                        "Informe seu nome ou apelido para entrar:</html>"
        );
        lblMensagem.setForeground(Cores.TEXTO_PRINCIPAL);
        lblMensagem.setFont(Cores.FONTE_NORMAL);

        JTextField campoNome = ComponentesUI.criarCampoTexto("Seu nome ou apelido...");
        campoNome.setPreferredSize(new Dimension(280, 36));

        painel.add(lblMensagem, BorderLayout.NORTH);
        painel.add(campoNome, BorderLayout.CENTER);

        while (true) {
            int opcao = JOptionPane.showConfirmDialog(null, painel,
                    "Login — SeriesTracker", JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.PLAIN_MESSAGE);

            if (opcao != JOptionPane.OK_OPTION) {
                return null;
            }

            String nomeDigitado = campoNome.getText();

            if (nomeDigitado == null || nomeDigitado.trim().isEmpty()) {
                JOptionPane.showMessageDialog(null,
                        "Por favor, informe um nome ou apelido para continuar.",
                        "Nome inválido", JOptionPane.WARNING_MESSAGE);
                continue;
            }

            String nome = nomeDigitado.trim();

            Usuario existente = persistencia.buscarUsuarioPorNome(nome);

            if (existente != null) {
                JOptionPane.showMessageDialog(null,
                        "Bem-vindo(a) de volta, " + existente.getNome() + "! \n" +
                                "Suas séries foram carregadas.",
                        "Bem-vindo!", JOptionPane.INFORMATION_MESSAGE);
                return existente;
            } else {
                int confirmar = JOptionPane.showConfirmDialog(null,
                        "Nenhum perfil encontrado para \"" + nome + "\".\n" +
                                "Deseja criar um novo perfil com esse nome?",
                        "Novo perfil", JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE);

                if (confirmar == JOptionPane.YES_OPTION) {
                    Usuario novo = new Usuario(nome);
                    persistencia.salvarUsuario(novo);
                    JOptionPane.showMessageDialog(null,
                            "Perfil criado com sucesso! Bem-vindo(a), " + nome + "! ",
                            "Cadastro realizado", JOptionPane.INFORMATION_MESSAGE);
                    return novo;
                }
            }
        }
    }
}