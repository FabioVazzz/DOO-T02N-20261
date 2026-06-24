# Atividade Extra

---

## Conceito 1

**Nome:** Coroutines e Suspending Functions

**Conceito escolhido:** Coroutines (Corrotinas) e Funções de Suspensão em Kotlin

**Timestamp do vídeo que menciona o conceito:** ~3:10
*"Parallel programming / Made intuitive with coroutines / Unleashing the power of suspending functions!"*

---

### O que é?

Coroutines são uma forma de escrever código assíncrono e concorrente de maneira sequencial e legível, sem a complexidade tradicional de threads. Uma **suspending function** (função de suspensão) é uma função marcada com a palavra-chave `suspend`, que pode ser pausada no meio da execução e retomada posteriormente sem bloquear a thread em que está rodando.

### Para que serve?

Servem para executar tarefas que levam tempo — como chamadas de rede, leitura de arquivos ou consultas a bancos de dados — sem travar a interface do usuário ou desperdiçar recursos do sistema com threads bloqueadas. Em vez de criar uma nova thread para cada operação demorada (o que é caro em memória), coroutines são leves e podem existir aos milhares.

### Como é normalmente utilizado?

No Kotlin, coroutines são iniciadas dentro de um **CoroutineScope** usando os builders `launch` (para tarefas sem retorno) ou `async` (para tarefas com retorno). O `runBlocking` é usado principalmente em testes ou na função `main()` para criar um escopo de coroutine de forma síncrona — exatamente como a música canta: `fun main() runBlocking { launch { ... } }`.

### Exemplo de código

```kotlin
import kotlinx.coroutines.*

fun main() = runBlocking {
    launch {
        delay(1000L)
        println("Tarefa 1 concluída!")
    }
    launch {
        delay(500L)
        println("Tarefa 2 concluída!")
    }
    println("Coroutines iniciadas, aguardando...")
}

// Output:
// Coroutines iniciadas, aguardando...
// Tarefa 2 concluída!
// Tarefa 1 concluída!
```

A função `delay()` é uma **suspending function**: ela pausa a coroutine sem bloquear a thread, permitindo que outras coroutines executem nesse intervalo.

---

## Conceito 2

**Nome:** Extension Functions (Funções de Extensão)

**Conceito escolhido:** Extension Functions e Infix Functions em Kotlin

**Timestamp do vídeo que menciona o conceito:** ~3:45
*"Syntax shortcuts / With infix and extension functions / Paving the way to write your custom DSLs!"*

---

### O que é?

**Extension functions** são funções que você define *fora* de uma classe, mas que se comportam como se fossem métodos dessa classe. Você "adiciona" funcionalidades a uma classe existente sem precisar herdar dela ou modificar seu código-fonte. **Infix functions** são um tipo especial de função que pode ser chamada sem os parênteses e o ponto, com uma sintaxe mais próxima da linguagem natural.

### Para que serve?

Extension functions resolvem um problema clássico do Java: a necessidade de criar classes utilitárias cheias de métodos estáticos (como `StringUtils.capitalize(str)`) só para adicionar um comportamento a uma classe que você não controla. Com Kotlin, você adiciona o método diretamente "na classe", tornando o código muito mais limpo e intuitivo. Infix functions tornam certas operações mais legíveis, especialmente em testes e na criação de DSLs.

### Como é normalmente utilizado?

Uma extension function é declarada escrevendo o tipo que você quer estender antes do nome da função (o chamado **receiver type**). Dentro da função, `this` se refere à instância do objeto. Infix functions precisam ter exatamente um parâmetro e são marcadas com a palavra-chave `infix`.

### Exemplo de código

```kotlin
// Extension function: adiciona um método novo à classe String
fun String.isPalindromo(): Boolean {
    return this == this.reversed()
}

// Infix function: sintaxe mais natural para criar pares
infix fun String.com(outro: String): String {
    return "$this e $outro"
}

fun main() {
    val palavra = "arara"
    println(palavra.isPalindromo())

    val dupla = "Rock" com "Metal"
    println(dupla)
}
```

A combinação de extension functions e infix functions é justamente o que permite criar **DSLs (Domain-Specific Languages)** em Kotlin — mini-linguagens internas com uma sintaxe expressiva e personalizada para um domínio específico, como configuração de builds, testes ou interfaces declarativas.
