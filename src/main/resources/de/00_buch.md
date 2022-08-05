
# Kreative DSLs in Kotlin

1. [Vorwort](01_vorwort.md) 
1. [Was ist eine DSL](02_definition.md)
1. [Anforderungsanalyse](03_analyse.md)
1. [DSL-Design-Prozess](04_prozess.md)
1. [Algebraische DSLs](05_algebraische_dsls.md)
1. [Builderartige DSLs](06_builderartige_dsls.md)


## Builderartige DSLs

Eine häufige Aufgabe ist die Initialisierung eines komplexen, mitunter 
verschachtelten Objekts. Die klassische Lösung in Java ist ein mutabler
Builder, der Methodenverkettung (a.k.a. "Fluent Interfaces") benutzt, um
das Setzen der Werte zu vereinfachen, und dann mittels einer terminalen
build-Methode ein oft vollständig oder teilweise immutables Fachobjekt
konstruiert.

In Kotlin kann man sich einen Builder oft sparen, in dem man ausnutzt, 
dass die Sprache benannte und Default-Argumente kennt.

Als einfaches Beispiel kann `java.net.http.HttpRequest` dienen, dessen
Aufruf etwa so aussehen kann:

```kotlin
val request = HttpRequest.newBuilder(URI.create("https://acme.com:9876/products"))
    .GET()
    .header("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
    .header("Accept-Encoding", "gzip, deflate")    
    .timeout(Duration.ofSeconds(5L))
    .build()
```
Das sieht schon nicht schlecht aus, aber es gibt immer noch die störenden Aufrufe
von newBuilder() und build(), und in der Syntax wird nicht deutlich, dass die
Methodenaufrufe eigentlich nur Wertzuweisungen sind. Die Frage ist, ob wir es in
Kotlin besser machen können.

Wenn wir uns an den Prozess zum DSL-Design erinnern, folgt auf die Anforderungsanalyse
(hier wäre das einfach, `HttpRequest` komfortabler zu konstruieren) das Brainstorming
für eine ideale Syntax. Natürlich ist das immer subjektiv, aber ich hoffe, 
dass der folgende Vorschlag als Verbesserung angesehen werden kann:

```kotlin
val request = httpRequest(URI.create("https://acme.com:9876/products")) {
      method = GET
      headers {
        "Content-Type" .. "application/x-www-form-urlencoded; charset=UTF-8"
        "Accept-Encoding" .. "gzip, deflate"
      }
      timeout = Duration.ofSeconds(5L)
    }
```


