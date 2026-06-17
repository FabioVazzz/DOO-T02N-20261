import javax.swing.*;
import java.awt.*;

public class SeriesDetailPanel extends JPanel {
    
    private final User user;
    private final DataService dataService;
    private Series currentSeries;
    private Runnable onListChanged; // callback pra atualizar o ListsPanel

    private JLabel lblName, lblLanguage, lblGenres, lblRating, 
                lblStatus, lblPremiered, lblEnded, lblNetwork;

    public SeriesDetailPanel(User user, DataService dataService, Runnable onListChanged) {
        this.user = user;
        this.dataService = dataService;
        this.onListChanged = onListChanged;
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout(10, 10));
        setPreferredSize(new Dimension(280, 0));
        setBorder(BorderFactory.createTitledBorder("Detalhes"));

        // Painel de informações
        JPanel infoPanel = new JPanel(new GridLayout(0, 1, 0, 4));
        lblName     = new JLabel("Selecione uma série");
        lblLanguage = new JLabel();
        lblGenres   = new JLabel();
        lblRating   = new JLabel();
        lblStatus   = new JLabel();
        lblPremiered= new JLabel();
        lblEnded    = new JLabel();
        lblNetwork  = new JLabel();

        infoPanel.add(lblName);
        infoPanel.add(lblLanguage);
        infoPanel.add(lblGenres);
        infoPanel.add(lblRating);
        infoPanel.add(lblStatus);
        infoPanel.add(lblPremiered);
        infoPanel.add(lblEnded);
        infoPanel.add(lblNetwork);

        // Painel de botões
        JPanel btnPanel = new JPanel(new GridLayout(3, 1, 0, 5));
        JButton btnFavorite  = new JButton("+ Favoritos");
        JButton btnWatched   = new JButton("+ Assistidas");
        JButton btnWatchlist = new JButton("+ Watchlist");

        btnFavorite.addActionListener(e  -> addToList("favorites"));
        btnWatched.addActionListener(e   -> addToList("watched"));
        btnWatchlist.addActionListener(e -> addToList("watchlist"));

        btnPanel.add(btnFavorite);
        btnPanel.add(btnWatched);
        btnPanel.add(btnWatchlist);

        add(new JScrollPane(infoPanel), BorderLayout.CENTER);
        add(btnPanel, BorderLayout.SOUTH);
    }

    // Chamado quando o usuário clica numa série
    private void updateDisplay(Series s) {
        lblName.setText("<html><b>" + nvl(s.name) + "</b></html>");
        lblLanguage.setText("Idioma: " + nvl(s.language));
        lblGenres.setText("<html>Gêneros: " + s.getGenresAsString() + "</html>");
        lblRating.setText("Nota: " + (s.getRatingAverage() > 0
            ? s.getRatingAverage() : "Sem nota"));
        lblStatus.setText("Estado: " + nvl(s.status));
        lblPremiered.setText("Estreia: " + nvl(s.premiered));
        lblEnded.setText("Término: " + nvl(s.ended));
        lblNetwork.setText("Emissora: " + s.getNetworkName());
    }


    public void setSeries(Series s) {
        this.currentSeries = s;
        updateDisplay(s);
    }

    private void addToList(String list) {
    if (currentSeries == null) return;

    boolean added = switch (list) {
        case "favorites" -> user.addFavorite(currentSeries);
        case "watched"   -> user.addWatched(currentSeries);
        case "watchlist" -> user.addWatchlist(currentSeries);
        default -> false;
    };

    if (!added) {
        JOptionPane.showMessageDialog(this, "Série já está nessa lista!","Alerta",JOptionPane.WARNING_MESSAGE);
        return;
    }

    try {
        dataService.save(user);
        if (onListChanged != null) onListChanged.run();
        JOptionPane.showMessageDialog(this, "Adicionado!");
    } catch (Exception ex) {
        JOptionPane.showMessageDialog(this, "Erro ao salvar: " + ex.getMessage(),"Erro",JOptionPane.ERROR_MESSAGE);
    }
}

    private String nvl(String s) {
        return (s != null && !s.isBlank()) ? s : "Não informado";
    }

    public void setOnListChanged(Runnable onListChanged) {
        this.onListChanged = onListChanged;
    }
    public void clear() {
        currentSeries = null;
        lblName.setText("Selecione uma série");
        lblLanguage.setText("");
        lblGenres.setText("");
        lblRating.setText("");
        lblStatus.setText("");
        lblPremiered.setText("");
        lblEnded.setText("");
        lblNetwork.setText("");
    }
}