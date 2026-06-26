## Nome:

Pedro Henrique Neu Marchi

## Conceitos escolhidos:

Coroutines e Sealed Classes

* # Coroutines:

## Timestamp do vídeo que menciona o conceito:

03:16 - 03:21 (trecho "Parallel programming made intuitive with coroutines")

## O que é?

Coroutines são um recurso da linguagem Kotlin que facilita a execução de tarefas concorrentes e assíncronas. Elas permitem que várias operações sejam executadas ao mesmo tempo sem a complexidade normalmente associada ao uso de Threads.

## Pra que serve?

São utilizadas para melhorar o desempenho e a responsividade das aplicações, especialmente quando é necessário realizar operações demoradas, como chamadas para APIs, acesso a banco de dados ou processamento de arquivos, sem travar a execução principal do programa.

## Como é normalmente utilizado?

As Coroutines são normalmente utilizadas em aplicações Android, sistemas web e serviços que precisam executar múltiplas tarefas simultaneamente. Elas utilizam funções especiais como `launch`, `async` e `runBlocking`.

## Exemplo de código

```kotlin
import kotlinx.coroutines.*

fun main() = runBlocking {

    launch {
        println("Tarefa executando em paralelo")
    }

    println("Programa principal")
}
```

Neste exemplo, a Coroutine criada com `launch` executa uma tarefa paralelamente ao fluxo principal do programa.

* # Sealed Classes:

## Timestamp do vídeo que menciona o conceito:

01:47 - 01:52 (trecho "controlled inheritance with sealed class")

## O que é?

Sealed Classes são um tipo especial de classe em Kotlin que permite controlar quais classes podem herdar dela. Todas as subclasses devem ser declaradas no mesmo arquivo da classe principal.

## Pra que serve?

Seu principal objetivo é restringir a herança e tornar o código mais seguro e previsível. Isso permite ao compilador conhecer todas as subclasses possíveis e verificar se todos os casos foram tratados corretamente.

## Como é normalmente utilizado?

São muito utilizadas para representar estados de uma aplicação, resultados de operações e tratamento de respostas, onde existe um conjunto limitado de possibilidades.

## Exemplo de código

```kotlin
sealed class Resultado

class Sucesso(val mensagem: String) : Resultado()
class Erro(val codigo: Int) : Resultado()

fun exibir(resultado: Resultado) {
    when (resultado) {
        is Sucesso -> println(resultado.mensagem)
        is Erro -> println("Erro: ${resultado.codigo}")
    }
}
```

Neste exemplo, a classe `Resultado` só pode ser herdada pelas classes definidas no mesmo arquivo, garantindo maior controle sobre a hierarquia de classes.

