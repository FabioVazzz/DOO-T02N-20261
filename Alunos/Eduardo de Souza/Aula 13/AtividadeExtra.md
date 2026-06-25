# Atividade Extra - HelloWorld.java (Nanowar of Steel)

## Nome

Eduardo de Souza

---

## Conceito Escolhido 1: Garbage Collector (GC)

**Timestamp do vídeo:** aproximadamente 03:00

### O que é?

O Garbage Collector (GC) é um mecanismo da JVM responsável por identificar objetos que não estão mais sendo utilizados e liberar automaticamente a memória ocupada por eles.

### Para que serve?

Ele serve para automatizar o gerenciamento de memória, reduzindo a ocorrência de vazamentos de memória e diminuindo a necessidade de intervenção manual do programador.

### Como é normalmente utilizado?

O desenvolvedor cria e utiliza objetos normalmente. Quando um objeto deixa de possuir referências válidas, ele se torna elegível para coleta e pode ser removido pelo Garbage Collector.

### Exemplo de código

```java
public class ExemploGC {
    public static void main(String[] args) {
        String texto = new String("Olá Mundo");

        texto = null; // objeto fica elegível para coleta

        System.gc(); // solicita a execução do GC
    }
}
```

---

## Conceito Escolhido 2: Bytecode

**Timestamp do vídeo:** aproximadamente 01:45

### O que é?

Bytecode é o código intermediário gerado pelo compilador Java após a compilação do código-fonte. Esse código não é executado diretamente pelo sistema operacional, mas pela JVM.

### Para que serve?

Ele permite que programas Java sejam portáveis entre diferentes plataformas, pois o mesmo arquivo compilado pode ser executado em qualquer sistema que possua uma JVM compatível.

### Como é normalmente utilizado?

Ao compilar um arquivo `.java`, o compilador gera um arquivo `.class` contendo o bytecode. Esse arquivo é então interpretado ou compilado em tempo de execução pela JVM.

### Exemplo de código

Código-fonte:

```java
public class HelloWorld {
    public static void main(String[] args) {
        System.out.println("Olá Mundo!");
    }
}
```

Compilação:

```bash
javac HelloWorld.java
```

Execução:

```bash
java HelloWorld
```

Após a compilação, o arquivo `HelloWorld.class` conterá o bytecode que será executado pela JVM.

```
```
