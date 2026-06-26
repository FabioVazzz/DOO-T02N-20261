### *Guilherme Hofstetter*

# **Operator Overloading** (Sobrecarga de operadores)

## Timestamp: Minuto 1:47

## O que é:
O operator overloading é um recurso de algumas linguagens de programação como C++, Python e Kotlin, que permite redefinir o comportamento de operadores comuns como `+`, `-` ou `/` quando eles são usados com tipos de dados definidos pelo usuário, como classes ou objetos.

## Para que serve? 
Esse recurso serve para deixar o código mais simples, intuitivo e parecido com a forma como pensamos em certas operações no mundo real ou na matemática. Por exemplo, se eu tenho dois objetos que representam pontos, vetores ou valores monetários, pode fazer sentido usar o operador `+` para somar esses objetos, em vez de criar uma função com um nome maior, como `somarPontos()` ou `adicionarValor()`.

## Como é normalmente utilizado?
Normalmente o operator overloading é utilizado dentro de classes, quando queremos definir como um operador deve se comportar ao ser aplicado em objetos daquela classe. Em Kotlin, por exemplo, isso é feito usando a palavra-chave operator junto com funções específicas. Para o operador `+`, usamos a função `plus()`. Assim, quando escrevemos `objeto1 + objeto2`, o Kotlin entende que deve chamar a função `plus()` definida naquela classe.
### Exemplo de código em Kotlin:
```
data class Ponto(val x: Int, val y: Int) {
    operator fun plus(outro: Ponto): Ponto {
        return Ponto(x + outro.x, y + outro.y)
    }
}

fun main() {
    val ponto1 = Ponto(2, 3)
    val ponto2 = Ponto(4, 1)

    val resultado = ponto1 + ponto2

    println(resultado)
}
```


# **Coroutines** (Corrotinas)

## Timestamp: Minuto 3:18

## O que é:
As coroutines são um recurso usado em Kotlin para executar tarefas assíncronas de forma mais simples e organizada. Elas permitem que uma parte do código seja pausada e continuada depois, sem travar o restante do programa. Diferente de uma thread tradicional, uma coroutine é mais leve e pode ser usada para lidar com várias tarefas ao mesmo tempo com menos custo para o sistema.

## Para que serve? 
As coroutines servem para executar tarefas que podem demorar algum tempo sem bloquear o funcionamento principal do programa. Por exemplo, elas podem ser usadas para buscar informações na internet, acessar um banco de dados, carregar arquivos ou esperar uma resposta de uma API. Assim, enquanto uma tarefa está aguardando, o programa pode continuar funcionando normalmente, sem ficar parado ou travado.

## Como é normalmente utilizado? 
As coroutines são utilizadas em situações em que o programa precisa lidar com tarefas assíncronas. Em Kotlin, podemos usar funções como launch, async e delay, que fazem parte da biblioteca kotlinx.coroutines. A função launch é usada para iniciar uma coroutine, enquanto delay simula uma espera sem bloquear a execução da thread principal. Já o runBlocking é usado em exemplos simples para criar um escopo onde as coroutines podem ser executadas.

### Exemplo de código em Kotlin:
```
import kotlinx.coroutines.*

fun main() = runBlocking {
    launch {
        delay(1000L)
        println("Tarefa dentro da coroutine finalizada")
    }

    println("O programa continua executando enquanto a coroutine espera")
}
```