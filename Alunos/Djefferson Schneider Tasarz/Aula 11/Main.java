import java.util.Scanner;

public class Main {

    private static final String API_KEY = "JXYBJ8DZNMCY7LBU58SYWUY5Q";

    public static void main(String[] args) {
        String apiKey = obterApiKey(args);
        WeatherService servico = new WeatherService(apiKey);

        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("       CONSULTA DE CLIMA — Visual Crossing  ");

            System.out.print("Digite o nome da cidade ");
            String cidade = scanner.nextLine().trim();

            if (cidade.isEmpty()) {
                System.out.println("Nome da cidade não pode ser vazio.");
                return;
            }

            System.out.println("\nBuscando dados para: " + cidade + "...\n");

            WeatherData dados = servico.buscarClima(cidade);

            System.out.println(dados);

        } catch (WeatherApiException e) {
            System.err.println("Erro ao buscar dados climáticos: " + e.getMessage());
        }
    }

    private static String obterApiKey(String[] args) {
        for (String arg : args) {
            if (arg.startsWith("--key=")) {
                return arg.substring("--key=".length());
            }
        }
        return API_KEY;
    }
}