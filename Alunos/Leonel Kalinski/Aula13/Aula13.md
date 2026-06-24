# Nome:Leonel Kalinski


# Conceito 1: Safe Variable Access e Elvis Operator

**Timestamp do vídeo:** 1:05

## O que é?

O **Safe Variable Access (`?.`)** e o **Elvis Operator (`?:`)** são recursos da linguagem Kotlin utilizados para lidar com valores nulos de forma segura.

O operador `?.` permite acessar propriedades ou métodos de um objeto apenas se ele não for nulo. Caso seja nulo, a expressão retorna `null` em vez de gerar uma exceção.

O operador `?:`, chamado de Elvis Operator, permite definir um valor padrão quando uma expressão resulta em `null`.

## Para que serve?

Esses recursos servem para evitar erros de execução relacionados a referências nulas, principalmente a `NullPointerException`, tornando o código mais seguro e legível.

## Como é normalmente utilizado?

São utilizados sempre que existe a possibilidade de uma variável não possuir valor. Isso é muito comum ao trabalhar com entradas de usuários, bancos de dados e APIs.

## Exemplo de código

```kotlin
fun main() {
    val nome: String? = null

    println(nome?.length)

    val tamanho = nome?.length ?: 0
    println(tamanho)
}
```

No exemplo acima, como a variável `nome` é nula, o operador `?.` impede que ocorra um erro. Já o Elvis Operator (`?:`) atribui o valor `0` caso o resultado seja nulo.

---

# Conceito 2: Mutability and Immutability of Collections

**Timestamp do vídeo:** 1:05

## O que é?

Em Kotlin, as coleções podem ser classificadas como **mutáveis** ou **imutáveis**.

Uma coleção imutável não permite alterações após sua criação, enquanto uma coleção mutável permite adicionar, remover ou modificar elementos.

## Para que serve?

A imutabilidade ajuda a evitar modificações acidentais nos dados, tornando o código mais seguro e previsível. As coleções mutáveis são úteis quando é necessário alterar os dados durante a execução do programa.

## Como é normalmente utilizado?

Utiliza-se `listOf()` para criar coleções imutáveis e `mutableListOf()` para criar coleções mutáveis.

## Exemplo de código

```kotlin
fun main() {

    val frutas = listOf("Maçã", "Banana", "Laranja")

    val numeros = mutableListOf(1, 2, 3)

    numeros.add(4)
    numeros.remove(2)

    println(frutas)
    println(numeros)
}
```

Neste exemplo, a lista `frutas` é imutável e não pode ser modificada após sua criação. Já a lista `numeros` é mutável, permitindo adicionar e remover elementos conforme necessário.
```