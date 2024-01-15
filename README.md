# Szachy w technologi klient-server
Ten program został stworzony jako projekt zaliczeniowy z Sieci komputerowych 2.
Został napisany w języku Java.


[//]: # (.\gradle-7.3.3-bin\gradle-7.3.3\bin)
[//]: # ($Env:PATH += ";.\gradle-7.3.3-bin\gradle-7.3.3\bin")
<br/>

### Technologie
Aby odpalić grę, musimy mieć zainstalowaną na swoim komputerze:
* Java: 17
* Gradle: 7.3

## Instalacja Gradle

### Linux
> sdk install gradle

> sdk use gradle 7.3

### Windows
Jeśli pobraliśmy paczkę z Gradle do naszego projektu, przechodzimy do zawartego w niej folderu bin:
> cd .\gradle-7.3.3-bin\gradle-7.3.3\bin

Następnie dodajemy ścieżkę do tego pliku w systemie, za pomocą komendy:
> $Env:PATH += ";.\gradle-7.3.3-bin\gradle-7.3.3\bin"

<br/>

## Uruchomienie

### Server (Windows / Linux)
Przechodzimy do folderu, w którym jest plik **build.gradle**.
Budujemy projekt za pomocą polecenia:
> gradle build

Następnie przechodzimy do folderu, w którym stworzył się plik jar i go odpalamy:
> cd build/libs/

> java -jar chess-1.0-SNAPSHOT.jar

<br/>

### Client (Windows/Linux)
Aby odpalić klienta, wystarczy przejść do folderu, w którym jest jego klasa:
> cd src/main/java/code

I go odpalić:
> java ./ChessClient.java

<br/>

## Gra
Po dołączeniu 2 graczy server zaczyna grę.
Gracz, który pierwszy dołączył, wybiera którymi bierkami chce grać.

Aby wykonać ruch, wpisujemy położenie figury, którą chcemy ruszyć, a następnie pole, na które chcemy iść. 
Np. 
> **c1 f4**  

Dobrej zabawy!
 

