# Exercício - Nanowar of Steel: "Kotlin"

> **Música:** Kotlin - Nanowar of Steel
> **Link:** https://www.youtube.com/watch?v=BsfXZjKLT9A
> **Duração:** 4:42

---

## Conceito 1

**Nome:** Kauan Bruno Polla Breuer

**Conceito escolhido:** Coroutines e Suspending Functions

**Timestamp do vídeo que menciona o conceito:** ~2:45 (código) e ~3:15 (slide)

> *"Fun main() runBlocking / Launch / Println("Amazing solo") / Launch / Println("Amazing solo harmonies") / Parallel programming / Made intuitive with coroutines / Unleashing the power of suspending functions!"*

### O que é?

Coroutines são uma forma de escrever código assíncrono e concorrente em Kotlin de maneira sequencial e legível. Em vez de lidar com callbacks ou threads complexas, o programador escreve o código como se fosse síncrono, e o compilador cuida da parte "paralela" por baixo dos panos.

Uma **suspending function** (função suspensa) é uma função marcada com a palavra-chave `suspend`, que pode ser pausada no meio da execução e retomada depois — sem bloquear a thread principal. Isso é o que torna as coroutines possíveis.

### Para que serve?

Coroutines servem para executar operações que demoram (chamadas de rede, leitura de banco de dados, timers) sem travar a aplicação. Em apps Android, por exemplo, uma operação de rede feita na thread principal causaria um crash; com coroutines, essa operação é feita em segundo plano de forma simples e segura.

### Como é normalmente utilizado?

- O bloco `runBlocking` é o ponto de entrada que conecta o mundo coroutine com o código normal.
- `launch` inicia uma coroutine de forma assíncrona (fire-and-forget).
- `async` + `await` é usado quando se quer um resultado de volta.
- `suspend fun` marca funções que podem ser "pausadas".

### Exemplo de código

```kotlin
import kotlinx.coroutines.*

// Função suspensa simulando uma chamada de rede
suspend fun buscarDados(): String {
    delay(1000) // pausa por 1 segundo sem bloquear a thread
    return "Dados recebidos!"
}

fun main() = runBlocking {
    println("Iniciando...")

    // Lança duas coroutines em paralelo
    val job1 = launch {
        val resultado = buscarDados()
        println("Job 1: $resultado")
    }

    val job2 = launch {
        val resultado = buscarDados()
        println("Job 2: $resultado")
    }

    job1.join()
    job2.join()

    println("Tudo finalizado!")
}

// Saída (ambas as buscas ocorrem em paralelo, levando ~1s no total):
// Iniciando...
// Job 1: Dados recebidos!
// Job 2: Dados recebidos!
// Tudo finalizado!
```

---

## Conceito 2

**Nome:** Kauan Bruno Polla Breuer

**Conceito escolhido:** Extension Functions (Funções de Extensão)

**Timestamp do vídeo que menciona o conceito:** ~3:30

> *"Syntax shortcuts / With infix and extension functions / Paving the way to write your custom DSLs!"*

### O que é?

Extension functions são funções que você adiciona a uma classe **já existente**, sem precisar modificar o código-fonte dela e sem usar herança. É como se você "colasse" uma nova função em uma classe de fora.

Em Kotlin, você declara uma extension function escrevendo o nome da classe antes do nome da função: `fun NomeDaClasse.novaFuncao()`.

### Para que serve?

Elas servem para estender o comportamento de classes que você **não controla** (como classes do SDK do Android, do Java ou de bibliotecas de terceiros), mantendo o código organizado e sem criar classes wrapper desnecessárias. Também são a base para criar DSLs (Domain-Specific Languages) em Kotlin.

### Como é normalmente utilizado?

- Adicionar utilitários a tipos primitivos como `String`, `Int` ou `List`.
- Estender classes do Android como `Context` ou `View` sem subclassificá-las.
- Criar uma API mais fluente e legível para um determinado domínio.

### Exemplo de código

```kotlin
// Adicionando uma função à classe String sem modificá-la
fun String.saudar(): String {
    return "Olá, $this!"
}

// Adicionando uma função à classe Int
fun Int.ehPar(): Boolean {
    return this % 2 == 0
}

// Adicionando uma função à classe List<String>
fun List<String>.imprimirTodos() {
    forEach { println("-> $it") }
}

fun main() {
    val nome = "Kotlin"
    println(nome.saudar())       // Olá, Kotlin!

    val numero = 42
    println(numero.ehPar())      // true

    val linguagens = listOf("Kotlin", "Java", "Python")
    linguagens.imprimirTodos()
    // -> Kotlin
    // -> Java
    // -> Python
}
```

> **Observação:** extension functions não quebram o encapsulamento, elas não conseguem acessar membros privados da classe. O Kotlin as resolve em tempo de compilação com base no tipo estático da variável.
