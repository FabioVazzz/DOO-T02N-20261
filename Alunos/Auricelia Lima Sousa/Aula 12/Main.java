import java.util.*;
import java.util.stream.*;

public class Main {
    public static void main(String[] args) {

        //ATV1
        List<Integer> numeros = Arrays.asList(12, 7, 25, 18, 30, 9, 26, 15);
        List<Integer> pares = numeros.stream()
                .filter(n -> n % 2 == 0)
                .collect(Collectors.toList());
        System.out.println(pares);

        //ATV2
        List<String> nomes = Arrays.asList("roberto", "josé", "caio", "vinicius");
        List<String> nomesMaiusculos = nomes.stream()
                .map(String::toUpperCase)
                .collect(Collectors.toList());
        System.out.println(nomesMaiusculos);

        //ATV3
        List<String> palavras = Arrays.asList("se", "talvez", "hoje", "sábado", "se", "quarta", "sábado");
        Map<String, Long> contagem = palavras.stream()
                .collect(Collectors.groupingBy(p -> p, Collectors.counting()));
        System.out.println(contagem);

        //ATV4
        List<Produto> produtos = Arrays.asList(
                new Produto("Tulipa", 120.00),
                new Produto("Rosa", 25.00),
                new Produto("Girassol", 150.00),
                new Produto("Lirio", 80.00)
        );
        List<Produto> caros = produtos.stream()
                .filter(p -> p.preco > 100.0)
                .collect(Collectors.toList());
        caros.forEach(p -> System.out.println(p.nome + " - R$ " + p.preco));

        //ATV5
        double total = produtos.stream()
                .mapToDouble(p -> p.preco)
                .sum();
        System.out.println("R$ " + total);

        //ATV6
        List<String> linguagens = Arrays.asList("Java", "Python", "C", "JavaScript", "Ruby");
        List<String> ordenadas = linguagens.stream()
                .sorted(Comparator.comparingInt(String::length))
                .collect(Collectors.toList());
        System.out.println(ordenadas);
    }
}

class Produto {
    public String nome;
    public double preco;

    public Produto(String nome, double preco) {
        this.nome = nome;
        this.preco = preco;
    }
}