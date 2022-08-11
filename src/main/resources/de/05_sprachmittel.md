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



## Funktionen und Methoden

### Standard- und benannte Argumente

### Varargs

### Syntax für das letzte Lambda-Argument 

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

