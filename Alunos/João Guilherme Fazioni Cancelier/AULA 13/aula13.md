# Atividade Extra - Aula 13: Análise Dev-Metal (Java)

**Nome:** João Guilherme Fazioni Cancelier

---

### 📑 Conceito Escolhido 1: Tratamento de Nulos e Valores Padrão (Optional vs Elvis Operator)

* **Timestamp do vídeo:** 0:53 (Slide: *Safe variable access and Elvis operators*)
* **O que é?** 
  Na música é mostrado o "Operador Elvis", que serve para evitar que uma variável fique com um valor nulo e acabe quebrando o sistema. Como no Java nós não temos esse operador, nós usamos uma ferramenta chamada `Optional`.
* **Pra que serve?** 
  Ele serve como uma proteção para o código. Caso o sistema receba um valor nulo por algum motivo, o `Optional` impede o programa de travar e permite que a gente coloque um valor padrão (um valor reserva) para o código continuar rodando.
* **Como é normalmente utilizado?** 
  Nós envolvemos a variável perigosa dentro do `Optional.ofNullable()`. Depois, usamos o comando `.orElse()` para definir qual será o valor padrão caso o dado original venha vazio.
* **Exemplo de código (Java):**
  ```java
  import java.util.Optional;

  public class ExemploElvisJava {
      public static void main(String[] args) {
          String nomeDoEstilo = null; // Dado simulando um retorno nulo

          // .ofNullable aceita nulos e .orElse define o fallback padrão (como o operador ?: )
          String resultado = Optional.ofNullable(nomeDoEstilo)
                                     .orElse("Estilo Padrão: Heavy Metal 🤘");

          System.out.println("Gênero selecionado: " + resultado);
      }
  }
  ```

---

### 📑 Conceito Escolhido 2: Operações e Precisão Financeira (Métodos do BigDecimal vs Operator Overloading)

* **Timestamp do vídeo:** 1:12 (Slide: *Smooth operators overloading*)
* **O que é?** 
  Na música é mostrada a "Sobrecarga de Operadores", que em linguagens como o Kotlin permite sobrescrever os sinais aritméticos normais para fazer contas com objetos. No Java, nós não temos esse recurso com símbolos, mas temos ferramentas para lidar com cálculos que exigem muita precisão.
* **Pra que serve?** 
  Serve para quando precisamos fazer por exemplo operações financeiras e decimais com muita precisão de valores. Se usarmos tipos primitivos comuns (como o `double`), o código pode sofrer erros de arredondamento e precisão. Por isso, o Java proíbe o uso dos sinais matemáticos diretos em objetos e nos obriga a usar métodos de classes especializadas para garantir que o valor final seja exato.
* **Como é normalmente utilizado?** 
  Em vez de usar os sinais tradicionais que poderiam gerar erros de precisão, nós utilizamos os métodos contidos em classes de "Big Numbers", como o `BigDecimal`. Para fazer uma soma precisa, por exemplo, chamamos o método `.add()`.
* **Exemplo de código (Java):**
  ```java
  import java.math.BigDecimal;

  public class AtividadeExtraContas {
      public static void main(String[] args) {
          // Valores financeiros criados com BigDecimal para garantir muita precisão
          BigDecimal precoIngresso = new BigDecimal("150.00");
          BigDecimal taxaConveniencia = new BigDecimal("15.50");

          // O Java não aceita o sinal "+". Usamos o método .add() do Big Number para somar com precisão
          BigDecimal precoFinal = precoIngresso.add(taxaConveniencia);

          System.out.println("Preço final com precisão garantida: R\$ " + precoFinal);
      }
  }
  ```