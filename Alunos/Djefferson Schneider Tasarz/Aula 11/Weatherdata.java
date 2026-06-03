public class WeatherData {

    private final String cidade;
    private final double temperaturaAtual;
    private final double temperaturaMaxima;
    private final double temperaturaMinima;
    private final double umidade;
    private final String condicao;
    private final double precipitacao;
    private final double velocidadeVento;
    private final double direcaoVento;

    public WeatherData(
            String cidade,
            double temperaturaAtual,
            double temperaturaMaxima,
            double temperaturaMinima,
            double umidade,
            String condicao,
            double precipitacao,
            double velocidadeVento,
            double direcaoVento) {
        this.cidade = cidade;
        this.temperaturaAtual = temperaturaAtual;
        this.temperaturaMaxima = temperaturaMaxima;
        this.temperaturaMinima = temperaturaMinima;
        this.umidade = umidade;
        this.condicao = condicao;
        this.precipitacao = precipitacao;
        this.velocidadeVento = velocidadeVento;
        this.direcaoVento = direcaoVento;
    }

    public String getCidade() {
        return cidade;
    }

    public double getTemperaturaAtual() {
        return temperaturaAtual;
    }

    public double getTemperaturaMaxima() {
        return temperaturaMaxima;
    }

    public double getTemperaturaMinima() {
        return temperaturaMinima;
    }

    public double getUmidade() {
        return umidade;
    }

    public String getCondicao() {
        return condicao;
    }

    public double getPrecipitacao() {
        return precipitacao;
    }

    public double getVelocidadeVento() {
        return velocidadeVento;
    }

    public double getDirecaoVento() {
        return direcaoVento;
    }

    public String getDirecaoVentoCardeal() {
        String[] direcoes = {"N", "NE", "L", "SE", "S", "SO", "O", "NO"};
        int indice = (int) Math.round(direcaoVento / 45.0) % 8;
        return direcoes[indice];
    }

    @Override
    public String toString() {
        return String.format(
                "Cidade            : %s%n" +
                "Temperatura Atual : %.1f °C%n" +
                "Temp. Máxima      : %.1f °C%n" +
                "Temp. Mínima      : %.1f °C%n" +
                "Umidade           : %.1f %%%n" +
                "Condição          : %s%n" +
                "Precipitação      : %.1f mm%n" +
                "Vento             : %.1f km/h — %s (%.0f°)",
                cidade,
                temperaturaAtual,
                temperaturaMaxima,
                temperaturaMinima,
                umidade,
                condicao,
                precipitacao,
                velocidadeVento,
                getDirecaoVentoCardeal(),
                direcaoVento);
    }
}