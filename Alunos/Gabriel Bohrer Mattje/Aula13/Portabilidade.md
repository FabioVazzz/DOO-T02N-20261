# Atividade Extra

**Nome:** Gabriel

---

## Conceito escolhido: Portabilidade

**Timestamp do vídeo que menciona o conceito:** 02:30

### O que é?

Portabilidade é a capacidade de um programa ser executado em diferentes sistemas operacionais sem a necessidade de alterações significativas no código.

### Para que serve?

Ela permite que o mesmo software funcione em ambientes diferentes, reduzindo custos de desenvolvimento e manutenção.

### Como é normalmente utilizada?

Na linguagem Java, o código é compilado para um formato intermediário chamado bytecode. Esse bytecode é executado pela JVM (Java Virtual Machine), que existe para diversos sistemas operacionais, como Windows, Linux e macOS. Isso possibilita o conceito de "Write Once, Run Anywhere" (Escreva uma vez, execute em qualquer lugar). :contentReference[oaicite:0]{index=0}

### Exemplo de código

```java
public class Portabilidade {
    public static void main(String[] args) {
        System.out.println("Este programa pode rodar em diferentes sistemas operacionais usando a JVM.");
    }
}
```

---

## Conceito escolhido: Garbage Collection

**Timestamp do vídeo que menciona o conceito:** 03:02

### O que é?

Garbage Collection é um mecanismo automático de gerenciamento de memória presente na linguagem Java.

### Para que serve?

Ele libera automaticamente a memória ocupada por objetos que não estão mais sendo utilizados pelo programa, evitando desperdício de recursos e reduzindo erros relacionados ao gerenciamento manual de memória.

### Como é normalmente utilizada?

O programador cria objetos normalmente e a JVM monitora quais deles não possuem mais referências. Quando identifica que um objeto não está mais sendo utilizado, o Garbage Collector pode remover esse objeto da memória automaticamente. :contentReference[oaicite:1]{index=1}

### Exemplo de código

```java
public class GarbageCollection {
    public static void main(String[] args) {
        String texto = new String("Exemplo");

        texto = null;

        System.gc();

        System.out.println("Objeto disponível para coleta de lixo.");
    }
}
```