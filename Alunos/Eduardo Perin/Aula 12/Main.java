import java.util.*;
import java.util.stream.*;

public class Main {

    // Classe Produto para Atv4 e Atv5
    static class Produto {
        private String nome;
        private double preco;

        public Produto(String nome, double preco) {
            this.nome = nome;
            this.preco = preco;
        }

        public String getNome() { return nome; }
        public double getPreco() { return preco; }

        @Override
        public String toString() {
            return nome + " - R$ " + String.format("%.2f", preco);
        }
    }

    public static void main(String[] args) {

        // ATV1
        System.out.println("=== ATV1 - Filtrar números pares ===");
        List<Integer> numeros = List.of(1, 4, 7, 8, 13, 20, 33, 42, 55, 60);
        List<Integer> pares = numeros.stream()
                .filter(n -> n % 2 == 0)
                .collect(Collectors.toList());
        System.out.println("Lista original: " + numeros);
        System.out.println("Números pares: " + pares);

        // ATV2
        System.out.println("\n=== ATV2 - Nomes em maiúsculas ===");
        List<String> nomes = List.of("roberto", "josé", "caio", "vinicius");
        List<String> nomesMaiusculos = nomes.stream()
                .map(String::toUpperCase)
                .collect(Collectors.toList());
        System.out.println("Nomes originais: " + nomes);
        System.out.println("Nomes em maiúsculas: " + nomesMaiusculos);

        // ATV3
        System.out.println("\n=== ATV3 - Contagem de palavras únicas ===");
        List<String> palavras = List.of("se", "talvez", "hoje", "sábado", "se", "quarta", "sábado");
        Map<String, Long> contagemPalavras = palavras.stream()
                .collect(Collectors.groupingBy(p -> p, Collectors.counting()));
        System.out.println("Lista de palavras: " + palavras);
        System.out.println("Contagem:");
        contagemPalavras.forEach((palavra, count) ->
                System.out.println("  \"" + palavra + "\": " + count + " vez(es)"));

        // ATV4
        System.out.println("\n=== ATV4 - Produtos com preço > R$ 100,00 ===");
        List<Produto> produtos = List.of(
                new Produto("Teclado", 150.00),
                new Produto("Mouse", 80.00),
                new Produto("Monitor", 900.00),
                new Produto("Mousepad", 45.00)
        );
        List<Produto> produtosFiltrados = produtos.stream()
                .filter(p -> p.getPreco() > 100.00)
                .collect(Collectors.toList());
        System.out.println("Todos os produtos:");
        produtos.forEach(p -> System.out.println("  " + p));
        System.out.println("Produtos acima de R$ 100,00:");
        produtosFiltrados.forEach(p -> System.out.println("  " + p));

        // ATV5
        System.out.println("\n=== ATV5 - Soma total dos produtos ===");
        double somaTotal = produtos.stream()
                .mapToDouble(Produto::getPreco)
                .sum();
        System.out.println("Soma total de todos os produtos: R$ " + String.format("%.2f", somaTotal));

        // ATV6
        System.out.println("\n=== ATV6 - Linguagens ordenadas por tamanho ===");
        List<String> linguagens = List.of("Java", "Python", "C", "JavaScript", "Ruby");
        List<String> linguagensOrdenadas = linguagens.stream()
                .sorted(Comparator.comparingInt(String::length))
                .collect(Collectors.toList());
        System.out.println("Lista original: " + linguagens);
        System.out.println("Ordenadas por tamanho: " + linguagensOrdenadas);
    }
}