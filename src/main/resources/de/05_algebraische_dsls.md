# Algebraische DSLs

Probleme, bei denen sich die Objekte im weitesten Sinne wie Zahlen verhalten, also
algebraische Strukturen bilden, lassen sich sehr einfach als DSL in Kotlin formulieren.
Dazu gehören komplexe Zahlen, Quaternionen, Vektoren, Matrizen, physikalische
oder monetäre Einheiten und vieles mehr. Die DSL besteht in diesem Fall vor allem
aus einer Gruppe von Operator-Überladungen.

Eine gute Kenntnis von Erweiterungsmethoden und Operator-Überladungen ist in
vielen DSLs unverzichtbar, auch wenn sie nicht oder nur teilweise in die
"algebraische" Kategorie fallen.

## Fallstudie: Eine DSL für Komplexe Zahlen 

Wie im Vorwort angedeutet, will dieses Buch "Spielzeug-Beispiele" vermeiden.
Aus diesem Grund dient die ausgereifte Java-Implementierung von Apache Commons als Basis.
Der Sourcecode der Bibliothek findet sich unter
https://github.com/apache/commons-numbers/tree/master/commons-numbers-complex

Die Frage, wie die DSL aussehen soll, ist schnell beantwortet: Komplexes Zahlen sollen
möglichst so wie die "eingebauten" Zahlen funktionieren. 

Hier eine mögliche Implementierung:

```kotlin
import org.apache.commons.numbers.complex.Complex

operator fun Complex.unaryPlus(): Complex = this

operator fun Complex.unaryMinus(): Complex = this.negate()

operator fun Complex.plus(other: Complex): Complex = add(other)
operator fun Complex.plus(other: Double): Complex = add(other)
operator fun Double.plus(other: Complex): Complex = fromDouble(this).add(this)

operator fun Complex.minus(other: Complex): Complex = subtract(other)
operator fun Complex.minus(other: Double): Complex = subtract(other)
operator fun Double.minus(other: Complex): Complex = fromDouble(this).subtract(other)

operator fun Complex.times(other: Complex): Complex = multiply(other)
operator fun Complex.times(other: Double): Complex = multiply(other)
operator fun Double.times(other: Complex): Complex = fromDouble(this).multiply(other)

operator fun Complex.div(other: Complex): Complex = divide(other)
operator fun Complex.div(other: Double): Complex = divide(other)
operator fun Double.div(other: Complex): Complex = fromDouble(this).divide(other)

operator fun Complex.compareTo(other: Complex): Int = abs().compareTo(other.abs())

private fun fromDouble(d: Double) = Complex.ofCartesian(d, 0.0)
```

Wie man sieht, erlauben Erweiterungsmethoden, die gleichzeitig Operator-Überladungen
sind, eine sehr direkte und kompakte Umsetzung. Die DSL kann wie erwartet verwendet
werden, so können damit Ausdrücke wie `x + y` oder `-2.0*(3.0 + x) - y*y` für
komplexe Zahlen `x` und `y` ausgewertet werden.

Kotlin selbst enthält übrigens ähnliche DSLs für `BigInteger` und `BigDecimal`.

Für eine Übersicht der für eine Überladung verfügbaren Operatoren sei auf
https://kotlinlang.org/docs/operator-overloading.html
verwiesen.
