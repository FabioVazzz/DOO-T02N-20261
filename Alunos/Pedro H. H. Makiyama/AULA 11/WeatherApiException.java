package com.pedro;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class WeatherApiException extends RuntimeException {

    private final JFrame frame = new JFrame();

    public WeatherApiException(String message){
        
        JOptionPane.showMessageDialog(frame, message, "Erro na API!", JOptionPane.ERROR_MESSAGE);
    }
}
