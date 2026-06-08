import java.util.List;
import java.util.Arrays;
import java.util.Map;
import java.util.Comparator;
import java.util.stream.Collectors;

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
        return nome + " - R$ " + preco;
    }
}

public class Main {
    public static void main(String[] args) {

        // ATV1
        List<Integer> numeros = Arrays.asList(12, 7, 25, 18, 30, 9, 26, 15);

        List<Integer> pares = numeros.stream()
                .filter(n -> n % 2 == 0)
                .collect(Collectors.toList());

        System.out.println("ATV1");
        System.out.println(pares);

        // ATV2
        List<String> nomes = Arrays.asList(
                "roberto", "josé", "caio", "vinicius");

        List<String> nomesMaiusculos = nomes.stream()
                .map(String::toUpperCase)
                .collect(Collectors.toList());

        System.out.println("\nATV2");
        System.out.println(nomesMaiusculos);

        // ATV3
        List<String> palavras = Arrays.asList(
                "se", "talvez", "hoje", "sábado", "se", "quarta", "sábado");

        Map<String, Long> contagem = palavras.stream()
                .collect(Collectors.groupingBy(
                        palavra -> palavra,
                        Collectors.counting()));

        System.out.println("\nATV3");
        System.out.println(contagem);

        // ATV4
        List<Produto> produtos = Arrays.asList(
                new Produto("Tulipa", 120.00),
                new Produto("Rosa", 25.00),
                new Produto("Girassol", 150.00),
                new Produto("Lírio", 80.00));

        List<Produto> produtosFiltrados = produtos.stream()
                .filter(produto -> produto.getPreco() > 100)
                .collect(Collectors.toList());

        System.out.println("\nATV4");
        produtosFiltrados.forEach(System.out::println);

        // ATV5
        double somaTotal = produtos.stream()
                .map(Produto::getPreco)
                .reduce(0.0, Double::sum);

        System.out.println("\nATV5");
        System.out.println("R$ " + somaTotal);

        // ATV6
        List<String> linguagens = Arrays.asList(
                "Java", "Python", "C", "JavaScript", "Ruby");

        List<String> ordenadas = linguagens.stream()
                .sorted(Comparator.comparingInt(String::length))
                .collect(Collectors.toList());

        System.out.println("\nATV6");
        System.out.println(ordenadas);
    }
}