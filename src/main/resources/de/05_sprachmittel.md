# Sprachmittel

Wie schon im Vorwort betont wurde, setzt dieses Buch Grundlagenwissen über Kotlin
voraus. Allerdings werden bei DSLs oft Spachmittel eingesetzt, die ansonsten eher
selten oder in anderer Form verwendet werden. In diesem Kapitel wollen wir einen
kurzen Überblick über wichtige Sprache-Bausteine für DSLs geben.

## Backtick-Bezeichner

Hin und wieder benötigt man "sprechende" Bezeichner in einer DSL, die nicht 
den üblichen Beschränkungen für Bezeichner unterworfen sein sollen. Kotlin erlaubt
fast beliebige Bezeichner, wenn diese in Backticks eingeschlossen werden.
Für die JVM sind alle Zeichen außer <code>\r\n`,.;:\|/[]<></code> erlaubt. 

Ein typischer Anwendungsfall sind Testbibliotheken. Während man sich in 
Java mit Unterstrichen und CamelCase behelfen muss, können in Kotlin 
sprechende Namen für Testfunktionen sehr einfach auf diese Weise
realisiert werden:

```kotlin
@Test
fun `check that the flux capacitor can manage 1,21 gigawatts`() {
   ...
}
```

## Funktionen, Methoden und Konstruktoren

### Benannte Argumente und Argumente mit Standardwerten

Methoden-Argumente können über ihren Namen angesprochen werden, was gerade bei 
längeren Argument-Listen sehr zur Lesbarkeit beiträgt, und oft einen Builder
überflüssig macht. Darüber hinaus können Argumente auch Standardwerte zugewiesen
werden. Beide Sprachmittel ergänzen sich sehr gut. Ein schönes Beispiel sind
die automatisch generierten `copy()`-Methoden in Datenklassen:

```kotlin
data class Person(val firstName: String, val lastName: String, val age: Int) {
    ...
    fun copy(firstName: String = this.firstName, 
             lastName: String = this.lastName,
             age: Int = this.age ) = Person(firstName, lastName, age)
}

val happyBirthday = person.copy(age = person.age + 1)
```
Wie man sieht, werden die aktuellen Werte des Objekts als Standardwerte festgelegt,
und dank benannter Argumente kann man gezielt die zu ändernden Werte herauspicken und
zuweisen, und alle anderen unverändert lassen.

### Syntax für ein abschließendes Lambda-Argument


### Varargs

Varargs sind bereits von Java bekannt, und erlauben, eine variable Anzahl von
Argumenten anzugeben. Allerdings gibt es einige wesentliche Verbesserungen in Kotlin:

Zum einen ist die Syntax durch den spread-Operator `*` eindeutig und erlaubt, 
Einzelargumente und Arrays beliebig zu kombinieren, 
etwa so: `val list = listOf(2, 0, *someArray, 4)`.

Zum anderen kann ein Varargs an beliebiger Stelle stehen, wobei man beim Aufruf in der 
Regel benannte Argumente verwenden muss, um eine eindeutige Zuordnung der nachfolgenden
Parameter sicherzustellen:

```kotlin
fun someMethod(vararg numbers: Int, someString: String) { ... }

someMethod(1, 2, 3, someString = "Hi!")
```
Für DSLs interessant ist die Möglichkeit, ein Vararg als vorletztes Argument vor einem
abschließenden Lambda einsetzen zu können, ohne beim Aufruf benannte Argumente
verwenden zu müssen: 

```kotlin
fun someMethod(someString: String, vararg numbers: Int, block: () -> Unit) { ... }

someMethod("Hi!", 1, 2, 3) {
    ...
}
```

### Property-Syntax

### Operator-Überladung

...

Der Invoke-Operator kann verwendet werden, um die Syntax eines
Funktionsaufrufs zu simulieren. So können sich z.B. Objekte als 
Funktionen "tarnen", aber dabei State speichern:

```kotlin
object count {
    private var n = 1
    operator fun invoke(): Int = n++
}
```

### Inline Funktionen

### Erweiterungs-Funktionen

### Receiver-Funktionen und -Lambdas

### Die @DslMarker-Annotation

## Properties

## Annotationen

## Reflection

