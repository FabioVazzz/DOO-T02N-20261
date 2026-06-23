# String Templating & Elvis Operator

**Nome:** Andrei Macedo 

**Tema Escolhido:** String Templating (Interpolação de Strings) e Elvis Operator.

---

## Conceito 1: String Templating (Interpolação de Strings)

### O Que é? Pra que serve? E como é normalmente utilizado? 

É uma funcionalidade sintática que permite injetar variáveis ou expressões lógicas diretamente dentro de textos literais (Strings) sem a necessidade de concatenação manual, servindo para tornar o código muito mais limpo, legível e reduzir a verbosidade. No Java tradicional, construir textos dinâmicos costuma exigir vários operadores + ou o uso de classes como StringBuilder.

A estrutura é utilizada inserindo o caractere \$ antes do nome de uma variável dentro das aspas. Caso necessário colocar uma expressão ou lógica (como chamar um método), envolve-se a expressão com chaves: ${expressão}.

### Exemplo de código
```código```
```Kotlin
fun main() {
    val totalItens = 3
    val precoItem = 15.0

    println("Você tem $totalItens itens no carrinho.")
    println("O valor total da sua compra é: R\$ ${totalItens * precoItem}")
} 
```
**Timestamp do vídeo que menciona o conceito:** 00:01:40

---

## Conceito 2: Elvis Operator

### O Que é? Pra que serve? E como é normalmente utilizado? 

É um operador binário que faz parte do sistema de Null Safety (Segurança de Nulos) do Kotlin. Ele avalia a expressão da esquerda: se ela não for nula, o programa a utiliza; se for nula, o programa retorna o valor definido à direita. Esse operador serve para evitar o famoso erro de sistema NullPointerException de forma extremamente concisa, permitindo definir um valor padrão de "escape" (fallback) sem precisar abrir blocos repetitivos de if/else, e normalmente é  utilizado logo após uma variável ou chamada de método que possui um tipo anulável (marcado com ?), seguido pelo valor padrão caso o nulo aconteça.

### Exemplo de código
```Código```
```Kotlin 
fun main() {
    val quantidadeEmEstoque: Int? = null // O estoque veio nulo (sem informação)

    // Se a quantidade for nula, o operador Elvis garante o valor padrão de 0 itens
    val itensDisponiveis = quantidadeEmEstoque ?: 0

    println("Quantidade de itens disponíveis para venda: $itensDisponiveis")
}
```
**Timestamp do vídeo que menciona o conceito:** 00:00:51

