# Gabriel Mesomo de Lurde

## Conceito Escolhido 1 - Coroutines

### Timestamp do vídeo que menciona o conceito - 03:16 até 03:20

### O que é?

Coroutines são um recurso da linguagem Kotlin utilizado para executar tarefas assíncronas e concorrentes de forma simples e eficiente, e elas permitem que determinadas operações sejam pausadas e retomadas posteriormente sem bloquear a execução principal a do programa. E diferente das threads tradicionais, as coroutines possuem um custo muito menor de memória e processamento, tornando-se uma solução moderna para aplicações que precisam executar diversas tarefas simultaneamente.

### Para que serve?

As coroutines servem para executar operações demoradas sem travar a aplicação, são muito utilizadas quando o sistema precisa aguardar respostas externas ou realizar tarefas em segundo plano.

Alguns exemplos de utilização são:

- Consultas em bancos de dados;
- Requisições para APIs;
- Download e upload de arquivos;
- Comunicação em rede;
- Processamento assíncrono de informações.

Em aplicações Android por ex, as coroutines ajudam a manter a interface responsiva enquanto tarefas mais pesadas são executadas.

### Como é normalmente utilizado?

Normalmente as coroutines são utilizadas através da biblioteca " kotlinx.coroutines ".

Entre os recursos mais utilizados estão:

- runBlocking
- launch
- async
- delay
- suspend

Essas ferramentas permitem criar fluxos assíncronos de maneira organizada e muito mais simples do que utilizando múltiplas threads manualmente.

### Exemplo de código

```kotlin
import kotlinx.coroutines.*

fun main() = runBlocking {

    println("Iniciando aplicação")

    launch {

        delay(2000)

        println("Dados carregados com sucesso")
    }

    println("Aplicação continua funcionando")
}
```

### Explicação do exemplo

No exemplo acima, uma coroutine é criada utilizando `launch`,  Enquanto ela aguarda dois segundos através do comando `delay`, a aplicação principal continua executando normalmente, e isso demonstra como as coroutines permitem realizar tarefas demoradas sem bloquear a execução do restante do programa.




## Conceito Escolhido 2 - Extension Functions

### Timestamp do vídeo que menciona o conceito - 03:26 até 03:30

### O que é?

Extension Functions são um recurso do Kotlin que permite adicionar novas funcionalidades a classes que já existentem sem alterar o código original dessas classes.
Esse mecanismo possibilita expandir comportamentos de forma simples e elegante, sem a necessidade de criar subclasses ou modificar bibliotecas externas.

### Para que serve?

As Extension Functions servem para tornar o código mais organizado, reutilizável e legível.

Elas são utilizadas para:

- Reaproveitar código;
- Adicionar funcionalidades extras;
- Evitar classes utilitárias desnecessárias;
- Melhorar a legibilidade do projeto;
- Facilitar a manutenção do sistema.

### Como é normalmente utilizado?

Para criar uma Extension Function é necessário informar o tipo que será estendido antes do nome da função.

A sintaxe básica é:

```kotlin
fun Tipo.nomeDaFuncao() {
}
```

Após criada, a função pode ser utilizada como se fosse um método original daquela classe, e esse recurso é muito comum em projetos Android e aplicações desenvolvidas com Kotlin moderno.

### Exemplo de código

```kotlin
fun String.inverterTexto(): String {

    return this.reversed()
}

fun main() {

    val palavra = "Programacao"

    println(palavra.inverterTexto())
}
```

### Explicação do exemplo

No exemplo acima foi criada uma função chamada `inverterTexto()` para o tipo `String`.

Mesmo que a classe `String` originalmente não possua esse método, após a criação da Extension Function qualquer objeto do tipo `String` poderá utilizá-lo normalmente, e ao executar o programa, a palavra será exibida invertida.




## Conclusão

Os conceitos de Coroutines e Extension Functions demonstram algumas das principais vantagens da linguagem Kotlin em relação a outras linguagens modernas. Enquanto as Coroutines simplificam o desenvolvimento de aplicações assíncronas e concorrentes, as Extension Functions, como ja dito permitem expandir funcionalidades existentes de forma elegante e organizada.
Esses recursos são amplamente utilizados no desenvolvimento profissional de aplicações Kotlin, especialmente em projetos Android e sistemas que exigem alto desempenho e manutenção mais simplificada.