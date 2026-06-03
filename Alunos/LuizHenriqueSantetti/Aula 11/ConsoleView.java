public class ConsoleView {

    public void show(WeatherData w) {

        System.out.println("\n===== TEMPO =====");
        System.out.println("Cidade: " + w.city);
        System.out.println("Atual: " + w.tempInfo.current);
        System.out.println("Máx: " + w.tempInfo.max);
        System.out.println("Mín: " + w.tempInfo.min);
        System.out.println("Umidade: " + w.humidity + "%");
        System.out.println("Condição: " + w.condition);
        System.out.println("Vento: " + w.wind.formatted());
        System.out.println("Chuva: " + w.precipitation + " mm");
    }
}