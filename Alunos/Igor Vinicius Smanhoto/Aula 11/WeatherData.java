package fag;


public class WeatherData {

    private double temperatura;
    private double temperaturaMaxima;
    private double temperaturaMinima;
    private double umidade;
    private String condicao;
    private double precipitacao;
    private double velocidadeVento;
    private String direcaoVento;

    public WeatherData(double temperatura,
                       double temperaturaMaxima,
                       double temperaturaMinima,
                       double umidade,
                       String condicao,
                       double precipitacao,
                       double velocidadeVento,
                       String direcaoVento) {

        this.temperatura = temperatura;
        this.temperaturaMaxima = temperaturaMaxima;
        this.temperaturaMinima = temperaturaMinima;
        this.umidade = umidade;
        this.condicao = condicao;
        this.precipitacao = precipitacao;
        this.velocidadeVento = velocidadeVento;
        this.direcaoVento = direcaoVento;
    }

    @Override
    public String toString() {
        return """
                ===== CLIMA ATUAL =====
                Temperatura: %.1f°C
                Máxima: %.1f°C
                Mínima: %.1f°C
                Umidade: %.1f%%
                Condição: %s
                Precipitação: %.1f mm
                Velocidade do vento: %.1f km/h
                Direção do vento: %s
                """.formatted(
                        temperatura,
                        temperaturaMaxima,
                        temperaturaMinima,
                        umidade,
                        condicao,
                        precipitacao,
                        velocidadeVento,
                        direcaoVento
                );
    }
}