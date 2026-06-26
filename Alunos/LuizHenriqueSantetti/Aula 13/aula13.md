# Aula 13 - Atividade Extra

#  Nome: Luiz Henrique Santetti


## Conceito escolhido 01: Elvis Operator

## Timestamp do vídeo que menciona o conceito: 00:52

## O que é?

O Elvis Operator é um operador do Kotlin representado pelos símbolos " ?: ", ele faz parte do sistema de segurança contra valores nulos da linguagem e permite definir um valor alternativo caso uma variável possua valor null.

Curiosidade: Seu nome surgiu porque o símbolo lembra o rosto do cantor Elvis Presley quando observado de lado.

## Pra que serve?

O Elvis Operator serve para evitar erros causados por valores nulos, principalmente o famoso "NullPointerException".
Ele permite que o desenvolvedor forneça um valor padrão para ser utilizado quando uma variável não possui conteúdo válido.

## Como é normalmente utilizado?

É normalmente utilizado em variáveis que podem receber valores nulos, indicadas pelo símbolo "?" em Kotlin.

Quando o valor da variável existe, ele é utilizado normalmente, caso seja null, o Kotlin retorna automaticamente o valor definido após o operador "?:".

## Exemplo de código

```kotlin
fun main() {

    val nomeUsuario: String? = null

    val nomeExibido = nomeUsuario ?: "Visitante"

    println("Bem-vindo, $nomeExibido!")
}
```



## Conceito escolhido 02: Delegation

## Timestamp do vídeo que menciona o conceito: 02:06

## O que é?

Delegation é um recurso do Kotlin que permite transferir determinadas responsabilidades de uma classe para outra, ao invés de implementar toda a funcionalidade diretamente, uma classe pode delegar parte do seu comportamento para um objeto especializado, promovendo reutilização de código e melhor organização do projeto.

Esse conceito segue o princípio de composição, muito utilizado na Programação Orientada a Objetos.

## Pra que serve?

A Delegation serve para reduzir código repetitivo e diminuir o acoplamento entre classes.

Entre suas principais vantagens estão:

- Reutilização de código;
- Melhor organização do sistema;
- Facilidade de manutenção;
- Menor dependência entre componentes;
- Maior flexibilidade para futuras alterações.

## Como é normalmente utilizado?

Em Kotlin, a delegação é frequentemente utilizada através da palavra-chave "by".

Ela permite que uma classe implemente uma interface utilizando automaticamente os métodos de outro objeto que já possui essa implementação.

## Exemplo de código

```kotlin
interface Mensagem {

    fun exibir()
}

class MensagemConsole : Mensagem {

    override fun exibir() {
        println("Mensagem exibida no console.")
    }
}

class Sistema(
    private val mensagem: Mensagem
) : Mensagem by mensagem

fun main() {

    val sistema = Sistema(MensagemConsole())

    sistema.exibir()
}
```


# Conclusão

O Elvis Operator e a Delegation são recursos importantes do Kotlin que ajudam a tornar o código mais seguro, organizado e fácil de manter, enquanto o Elvis Operator auxilia no tratamento de valores nulos de forma simples, a Delegation promove reutilização de código e melhor separação de responsabilidades entre as classes.