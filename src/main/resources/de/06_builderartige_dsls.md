# Builderartige DSLs

Eine häufige Aufgabe ist die Initialisierung eines komplexen, mitunter
verschachtelten Objekts. Die klassische Lösung in Java ist ein mutabler
Builder, der Methodenverkettung (a.k.a. "Fluent Interfaces") benutzt, um
das Setzen der Werte zu vereinfachen, und dann mittels einer terminalen
build-Methode ein oft vollständig oder teilweise immutables Fachobjekt
konstruiert.

In Kotlin kann man sich einen Builder oft sparen, in dem man ausnutzt,
dass die Sprache benannte und Default-Argumente kennt. Und auch bei
komplexeren Problemen lassen sich oft Lösungen finden, die über das
schlichte Builder-Pattern hinausgehen.

## Fallstudie: HttpRequest

Als ein realistisches Beispiel kann `java.net.http.HttpRequest` dienen, dessen
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
für eine ideale Syntax. Natürlich ist immer subjektiv, was als "ideal" angesehen wird,
aber ich hoffe, dass der folgende Vorschlag wenigstens eine Verbesserung 
gegenüber dem Original darstellt:

```kotlin
val request = httpRequest(URI.create("https://acme.com:9876/products")) {
      method = GET
      headers {
        "Content-Type" .. "application/x-www-form-urlencoded; charset=UTF-8"
        "Accept-Encoding" .. "gzip, deflate"
      }
      timeout = 5 * SECONDS
    }
```
Es stellt sich heraus, dass diese Syntax auch so umsetzbar ist.

Dabei ist `httpRequest` eine Methode, die uns unsere Version des Builders zur Verfügung 
stellt, aber Konstruktion und Finalisierung für uns übernimmt. Damit handelt es sich
um eine Anwendung des Loan-Patterns, dass sich in vielen Fällen als nützlich beim
DSL-Design erweist. Die Implementierung der Methode ist trivial:

```kotlin
fun httpRequest(uri: URI, block: HttpRequestBuilder.() -> Unit): HttpRequest =
    HttpRequestBuilder(uri).apply(block).build()
```

Unser Builder hält intern einen "originalen" Builder, und delegiert auch den Aufruf der
`build()`-Methode:

```kotlin
class HttpRequestBuilder(uri: URI) {
    private val peer = HttpRequest.newBuilder(uri)
    ...
    fun build() = peer.build()
}
```     

 
