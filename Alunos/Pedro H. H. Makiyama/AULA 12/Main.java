import java.util.List;
import java.util.stream.Collectors;
import java.util.Map;
import java.util.function.Function;

public class Main {

    public static void main(String[] args){
        
        //ATV1

        List<Integer> listOfInt = List.of(1, 2, 5, 6, 4, 9, 10, 11);

        List<Integer> evenIntegers = listOfInt.stream().filter(intg -> intg % 2 == 0)
        .collect(Collectors.toList());

        System.out.println(evenIntegers);

        System.out.println();

        //ATV2

        List<String> namesAtv2 = List.of("roberto", "josé", "caio", "vinicius");

        namesAtv2 = namesAtv2.stream().map(name -> name.toUpperCase()).collect(Collectors.toList());

        System.out.println(namesAtv2);

        System.out.println();

        //ATV3

        List<String> namesAtv3 = List.of("se", "talvez", "hoje", "sábado", "se", "quarta", "sábado");

        Map<String, Long> stringCount = namesAtv3.stream()
        .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        System.out.println(stringCount);

        System.out.println();

        //ATV4

        List<Produto> produtos = List.of(
            new Produto("Produto 1", 120.92), 
            new Produto("Produto 2", 90.3),
            new Produto("Produto 3", 452.7), 
            new Produto("Produto 4", 23.4)
        );

        List<Produto> precoMaior100 = produtos.stream().filter(produto -> produto.getPreco() > 100).collect(Collectors.toList());

        precoMaior100.forEach(produto -> System.out.println("Nome: " + produto.getNome() + " - Preço: " + produto.getPreco()));

        System.out.println();

        //ATV5

        double priceSum = produtos.stream().mapToDouble(produto -> produto.getPreco()).sum();

        System.out.println("Soma: " + priceSum);

        System.out.println();

        //ATV6

        List<String> progLanguages = List.of("Java", "Python", "C", "JavaScript", "Ruby");

        progLanguages = progLanguages.stream().sorted((a, b) -> Integer.compare(a.length(), b.length()))
        .collect(Collectors.toList());

        System.out.println(progLanguages);
    }
}   
