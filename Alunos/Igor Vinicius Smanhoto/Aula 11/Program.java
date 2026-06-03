package fag;
import java.util.Scanner;

public class Program {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        System.out.print("Digite a cidade: ");
        String cidade = scanner.nextLine();

        WeatherService service = new WeatherService();

        WeatherData clima =
                service.buscarClima(cidade);

        if (clima != null) {
            System.out.println(clima);
        }

        scanner.close();
    }
}