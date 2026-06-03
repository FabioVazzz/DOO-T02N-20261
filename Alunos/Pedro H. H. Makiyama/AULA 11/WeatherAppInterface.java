package com.pedro;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.awt.event.ActionEvent;
import java.time.LocalDate;

public class WeatherAppInterface {

    private final WeatherService weatherService = new WeatherService();
    private final int WIDTH = 650;
    private final int HEIGHT = 350;
    private final Font DEFAULT_FONT = new Font("Arial", Font.PLAIN, 20);
    private JFrame frame;
    private JPanel topPanel;
    private JPanel datePanel;
    private JTextField dateTextField;
    private JButton confirmationButton;
    private JPanel tempsTodayPanel;
    private JLabel tempNowLabel;
    private JLabel minMaxTodayLabel;
    private JPanel bottomPanel;
    private JPanel topOfBottomPanel;
    private JPanel minMaxPanel;
    private JLabel minMaxLabel;
    private JPanel weatherHumidityPanel;
    private JLabel weatherHumidityLabel;
    private JPanel bottomOfBottomPanel;
    private JPanel windPanel;
    private JLabel windLabel;
    private JPanel precipitationPanel;
    private JLabel precipitationLabel;

    public WeatherAppInterface(){

        // Criar componentes da interface
        createFrame();
        createTopPanel();
        createDatePanel();
        createDateTextField();
        createConfirmationButton();
        createTempsTodayPanel();
        createTempNowLabel();
        createMinMaxTodayLabel();
        createBottomPanel();
        createTopOfBottomPanel();                 
        createMinMaxPanel();
        createMinMaxLabel();
        createWeatherHumidityPanel();
        createWeatherHumidityLabel();
        createBottomOfBottomPanel();
        createWindPanel();
        createWindLabel();
        createPrecipitationPanel();
        createPrecipitationLabel();

        // Pegar informações do clima/tempo de hoje e as apresentar na interface
        updateTempsTodayLabels();
    }

    public void displayInterface(){

        frame.setVisible(true);
    }

    private void createFrame(){

        frame = new JFrame("Aplicativo de Clima/Tempo");
        frame.setSize(WIDTH, HEIGHT - 25);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
    }

    private void createTopPanel(){

        topPanel = new JPanel(new BorderLayout());
        topPanel.setPreferredSize(new Dimension(WIDTH, HEIGHT / 4));
        frame.add(topPanel, BorderLayout.NORTH);
    }

    private void createDatePanel(){

        datePanel = new JPanel(new BorderLayout());
        datePanel.setPreferredSize(new Dimension(220, HEIGHT / 4));
        datePanel.setBorder(BorderFactory.createTitledBorder("Data"));
        topPanel.add(datePanel, BorderLayout.WEST);
    }

    private void createDateTextField(){

        dateTextField = new JTextField("01/01/2026");
        dateTextField.setPreferredSize(new Dimension(140, HEIGHT / 4));
        dateTextField.setFont(DEFAULT_FONT);
        dateTextField.setHorizontalAlignment(JTextField.CENTER);
        dateTextField.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 5));
        datePanel.add(dateTextField, BorderLayout.WEST);
    }

    private void createConfirmationButton(){

        confirmationButton = new JButton("OK");
        confirmationButton.setFont(DEFAULT_FONT);
        confirmationButton.setBackground(Color.WHITE);
        confirmationButton.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY,5));

        // Pegar data do dateTextField para fazer request e apresentar dados do tempo/clima do dia especificado
        confirmationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                
                try {
                    
                    String date = LocalDate.parse(dateTextField.getText(), DateTimeFormatter.ofPattern("dd/MM/yyyy")).toString();
                    
                    WeatherDataDay weatherDataDay = weatherService.getWeatherDataDay(date);

                    minMaxLabel.setText("Mín. " + weatherDataDay.getTempmin() + "°C - Máx. " + weatherDataDay.getTempmax() + "°C");

                    weatherHumidityLabel.setText(weatherDataDay.getConditions() + " - " + weatherDataDay.getHumidity() + "%");

                    windLabel.setText("<html>Velocidade: " + weatherDataDay.getWindspeed() + " Km/h<br>Direção: " 
                    + weatherDataDay.getWinddir() + "°</html>");

                    precipitationLabel.setText(weatherDataDay.getPrecip() + " mm");

                    bottomPanel.setBorder(BorderFactory.createTitledBorder(dateTextField.getText()));

                    dateTextField.setText("");
                
                } catch (DateTimeParseException exc) {

                    JOptionPane.showMessageDialog(frame,"Data inválida, use dd/MM/yyyy.",
                    "Erro no input!", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        datePanel.add(confirmationButton, BorderLayout.CENTER);
    }

    private void createTempsTodayPanel(){

        tempsTodayPanel = new JPanel(new BorderLayout());
        tempsTodayPanel.setBorder(BorderFactory.createTitledBorder("Temperaturas em Ubiratã hoje"));
        topPanel.add(tempsTodayPanel, BorderLayout.CENTER);   
    }

    private void createTempNowLabel(){ 

        tempNowLabel = new JLabel("");
        tempNowLabel.setPreferredSize(new Dimension(120, HEIGHT / 4));
        tempNowLabel.setFont(DEFAULT_FONT);
        tempNowLabel.setHorizontalAlignment(JLabel.CENTER);
        tempsTodayPanel.add(tempNowLabel, BorderLayout.WEST);
    }

    private void createMinMaxTodayLabel(){

        minMaxTodayLabel = new JLabel("");
        minMaxTodayLabel.setFont(DEFAULT_FONT);
        minMaxTodayLabel.setHorizontalAlignment(JLabel.CENTER);
        tempsTodayPanel.add(minMaxTodayLabel, BorderLayout.CENTER);
    }

    private void createBottomPanel(){

        bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBorder(BorderFactory.createTitledBorder("dd/mm/aaaa"));
        frame.add(bottomPanel, BorderLayout.CENTER);
    }

    private void createTopOfBottomPanel(){

        topOfBottomPanel = new JPanel(new BorderLayout());
        topOfBottomPanel.setPreferredSize(new Dimension(WIDTH, HEIGHT / 4));
        topOfBottomPanel.setBorder(null);
        bottomPanel.add(topOfBottomPanel, BorderLayout.NORTH);
    }

    private void createMinMaxPanel(){

        minMaxPanel = new JPanel(new GridBagLayout());
        minMaxPanel.setPreferredSize(new Dimension(WIDTH / 2 - 65, HEIGHT / 4));
        minMaxPanel.setBorder(BorderFactory.createTitledBorder("Temperaturas"));
        topOfBottomPanel.add(minMaxPanel, BorderLayout.WEST);
    }

    private void createMinMaxLabel(){

        minMaxLabel= new JLabel("Mín. __._°C - Máx. __._°C");
        minMaxLabel.setFont(DEFAULT_FONT);
        minMaxPanel.add(minMaxLabel);
    }

    private void createWeatherHumidityPanel(){

        weatherHumidityPanel = new JPanel(new GridBagLayout());
        weatherHumidityPanel.setBorder(BorderFactory.createTitledBorder("Clima e umidade do ar"));
        topOfBottomPanel.add(weatherHumidityPanel, BorderLayout.CENTER);
    }

    private void createWeatherHumidityLabel(){

        weatherHumidityLabel = new JLabel("CLIMA - __._%");
        weatherHumidityLabel.setFont(DEFAULT_FONT);
        weatherHumidityPanel.add(weatherHumidityLabel);
    }
    
    private void createBottomOfBottomPanel(){

        bottomOfBottomPanel = new JPanel(new BorderLayout());
        bottomOfBottomPanel.setPreferredSize(new Dimension(WIDTH, HEIGHT / 4));
        bottomOfBottomPanel.setBorder(null);
        bottomPanel.add(bottomOfBottomPanel, BorderLayout.CENTER);
    }

    private void createWindPanel(){

        windPanel = new JPanel(new GridBagLayout());
        windPanel.setPreferredSize(new Dimension(WIDTH / 2, HEIGHT / 4));
        windPanel.setBorder(BorderFactory.createTitledBorder("Vento"));
        bottomOfBottomPanel.add(windPanel, BorderLayout.WEST);
    }

    private void createWindLabel(){

        windLabel = new JLabel("<html>Velocidade: __._ Km/h<br>Direção: ___._°</html>");
        windLabel.setFont(DEFAULT_FONT);
        windPanel.add(windLabel);
    }

    private void createPrecipitationPanel(){
        
        precipitationPanel = new JPanel(new GridBagLayout());
        precipitationPanel.setBorder(BorderFactory.createTitledBorder("Precipitação"));
        bottomOfBottomPanel.add(precipitationPanel, BorderLayout.CENTER);
    }

    private void createPrecipitationLabel(){

        precipitationLabel = new JLabel("__._ mm");
        precipitationLabel.setFont(DEFAULT_FONT);
        precipitationPanel.add(precipitationLabel);
    }

    public JTextField getDateTextField() {
        return dateTextField;
    }

    public void updateTempsTodayLabels(){

        WeatherDataDay weatherDataToday = weatherService.getWeatherDataToday();

        tempNowLabel.setText(weatherDataToday.getTemp() + "°C");
        minMaxTodayLabel.setText("<html>Mín. " + weatherDataToday.getTempmin() + "°C<br>Máx. " + weatherDataToday.getTempmax() +
        "°C</html>");
    }
}
