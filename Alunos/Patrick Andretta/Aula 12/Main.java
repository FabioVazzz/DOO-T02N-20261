import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) {

        // ATV1 - Filtrar apenas os numeros pares de uma lista.
        List<Integer> numeros = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        List<Integer> pares = numeros.stream()
                .filter(n -> n % 2 == 0)
                .collect(Collectors.toList());
        System.out.println("ATV1 - Numeros pares: " + pares);

        // ATV2 - Converter todos os nomes para letras maiusculas.
        List<String> nomes = Arrays.asList("roberto", "josé", "caio", "vinicius");
        List<String> nomesMaiusculos = nomes.stream()
                .map(String::toUpperCase)
                .collect(Collectors.toList());
        System.out.println("ATV2 - Nomes em maiusculo: " + nomesMaiusculos);

        // ATV3 - Contar quantas vezes cada palavra unica aparece.
        List<String> palavras = Arrays.asList("se", "talvez", "hoje", "sábado", "se", "quarta", "sábado");
        Map<String, Long> contagem = palavras.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        System.out.println("ATV3 - Contagem de palavras: " + contagem);

        // ATV4 - Filtrar produtos com preco maior que R$ 100,00.
        List<Produto> produtos = Arrays.asList(
                new Produto("Teclado", 89.90),
                new Produto("Monitor", 750.00),
                new Produto("Mouse", 45.50),
                new Produto("Cadeira", 1200.00)
        );
        List<Produto> produtosCaros = produtos.stream()
                .filter(p -> p.getPreco() > 100.00)
                .collect(Collectors.toList());
        System.out.println("ATV4 - Produtos com preco > R$ 100,00: " + produtosCaros);

        // ATV5 - Somar o valor total dos produtos da lista.
        double valorTotal = produtos.stream()
                .mapToDouble(Produto::getPreco)
                .sum();
        System.out.printf("ATV5 - Valor total dos produtos: R$ %.2f%n", valorTotal);

        // ATV6 - Ordenar a lista pelo tamanho da palavra, da menor para a maior.
        List<String> linguagens = Arrays.asList("Java", "Python", "C", "JavaScript", "Ruby");
        List<String> ordenadas = linguagens.stream()
                .sorted(Comparator.comparingInt(String::length))
                .collect(Collectors.toList());
        System.out.println("ATV6 - Ordenadas por tamanho: " + ordenadas);
    }
}

// Classe utilizada na ATV4 e ATV5.
class Produto {
    private String nome;
    private double preco;

    public Produto(String nome, double preco) {
        this.nome = nome;
        this.preco = preco;
    }

    public String getNome() {
        return nome;
    }

    public double getPreco() {
        return preco;
    }

    @Override
    public String toString() {
        return String.format("%s (R$ %.2f)", nome, preco);
    }
}
