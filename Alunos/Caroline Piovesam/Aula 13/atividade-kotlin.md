# Aula 13 - Caroline Piovesam

## Conceito 1

**Conceito escolhido:** Coroutines

**Timestamp do vídeo que menciona o conceito:** 03:16 até 03:20

### O que é?

Coroutines são um mecanismo do Kotlin utilizado para simplificar a execução de tarefas concorrentes e assíncronas. Elas permitem executar várias operações ao mesmo tempo sem a necessidade de criar e gerenciar diversas threads manualmente.

Uma coroutine pode ser pausada e retomada posteriormente sem bloquear a execução do restante do programa. Isso torna o código mais simples, legível e eficiente.

### Pra que serve?

As coroutines são utilizadas principalmente para executar tarefas demoradas sem travar a aplicação. Alguns exemplos são:

- Realizar chamadas para APIs;
- Acessar bancos de dados;
- Ler ou escrever arquivos;
- Executar operações em segundo plano.

Em aplicações Android, por exemplo, as coroutines são muito importantes para manter a interface gráfica responsiva enquanto operações demoradas são realizadas.

### Como é normalmente utilizado?

As coroutines normalmente são utilizadas através da biblioteca `kotlinx.coroutines`, utilizando funções como `runBlocking`, `launch`, `async` e `delay`.

- `runBlocking`: cria um escopo de execução para as coroutines;
- `launch`: inicia uma coroutine sem retornar valor;
- `async`: inicia uma coroutine que retorna um resultado futuramente;
- `delay`: suspende a execução da coroutine sem bloquear a thread.

### Exemplo de código

```kotlin
import kotlinx.coroutines.*

fun main() = runBlocking {

    println("Programa iniciado")

    launch {
        delay(2000)
        println("Dados recebidos da API")
    }

    println("O programa continua executando")

    delay(3000)
}
```

Nesse exemplo, a coroutine executa uma tarefa em paralelo. Enquanto ela aguarda dois segundos para simular a resposta de uma API, o programa principal continua executando normalmente.

## Conceito 2

**Conceito escolhido:** Extension Functions

**Timestamp do vídeo que menciona o conceito:** 03:26 até 03:30

### O que é?

Extension Functions (Funções de Extensão) são um recurso do Kotlin que permite adicionar novos métodos a classes já existentes sem modificar seu código-fonte original e sem utilizar herança.

Esse mecanismo possibilita expandir funcionalidades de uma classe de forma simples, organizada e reutilizável.

### Pra que serve?

As Extension Functions servem para:

- Reaproveitar código;
- Tornar o código mais limpo e legível;
- Evitar a criação de classes utilitárias desnecessárias;
- Adicionar funcionalidades extras a classes existentes.

Esse recurso é muito utilizado no desenvolvimento Android para simplificar operações repetitivas.

### Como é normalmente utilizado?

Para criar uma Extension Function, é preciso informar o tipo que deseja estender antes do nome da função.

Sintaxe geral:

```kotlin
fun Tipo.nomeDaFuncao() {

}
```

Após criada, a função pode ser utilizada como se fosse um método original da própria classe.

### Exemplo de código

```kotlin
fun String.saudacao(): String {
    return "Olá, $this!"
}

fun main() {

    val nome = "Nome"

    println(nome.saudacao())
}
```

Saída:

```text
Olá, Nome!
```

No exemplo acima, foi adicionada uma nova função chamada `saudacao()` à classe `String`. Embora a classe `String` não possua originalmente esse método, qualquer objeto do tipo `String` poderá utilizá-lo após a criação da Extension Function.
