import java.util.Scanner;

public class WeatherController {

    private final ApiService api = new ApiService();
    private final WeatherParser parser = new WeatherParser();
    private final ConsoleView view = new ConsoleView();

    public void start() {

        Scanner sc = new Scanner(System.in);

        System.out.print("Digite a cidade: ");
        String city = sc.nextLine();

        try {
            String json = api.fetch(city);
            WeatherData data = parser.parse(json);

            view.show(data);

        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }

        sc.close();
    }
}