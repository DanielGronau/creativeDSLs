
# Kreative DSLs in Kotlin


# Vorwort

Warum noch ein Buch über DSLs in Kotlin? Die Sprache ist für DSLs prädestiniert,
es gibt viele gute praktische Beispiele, und es gibt dementsprechend auch viel
Literatur. Aber ich muss leider sagen, dass mich die entsprechende Literatur 
enttäuscht, und zwar aus zwei Gründen: Sie ist zu stark praxisorientiert, 
und sie ist zu wenig praxisorientiert.

Die starke Praxisorientierung äußert sich darin, dass gar nicht diskutiert wird,
welche Anforderungen an eine DSL gestellt werden. Der Zweck einer DSL mag intuitiv
verständlich sein, das ändert aber nichts daran, dass es eher versteckte Aspekte
beim DSL-Design gibt, die gern übersehen werden.

Die ungenügende Praxisorientierung zeigt sich an den behandelten Beispielen,
die meist so gewählt sind, dass sie sich einfach und pädagogisch wertvoll in
Kotlin umsetzen lassen. Aber die Problembereiche, für die es einfach ist, DSLs zu
schreiben, sind eben in der Praxis in der Minderheit - und es gibt gute Chancen,
dass es gerade dafür schon fertige Bibliotheken gibt. DSL-Design ist ein kreativer
Prozess, und dieses Buch möchte Beispiele präsentieren, die etwas mehr Arbeit
und “out of the box”-Denken erfordern, aber dafür eben auch praxisrelevanter sind.

Dieses Buch möchte diese beiden Lücken schließen. Zum einen enthält es eine
Checkliste an Anforderungen, die auch weniger offensichtliche, aber wichtige
Aspekte beleuchtet, sowie einen Vorschlag zum Ablauf eines Design-Prozesses.
Zum anderen wird ausgehend von den "langweiligen" Standard-Beispielen eine Reihe 
DSLs für immer "sperriger" und herausfordernder werdende Probleme vorgestellt, 
die dem Vorgehen in freier Wildbahn viel näher kommen.

Weiterhin wird der Versuch unternommen, nicht einfach isolierte Beispiele 
vorzustellen, sondern Typen von DSLs zu klassifizieren. So wie Design-Patterns
eine gemeinsame Sprache für erprobte Code-Konstrukte etabliert haben, 
ist es an der Zeit, auch für DSLs allgemein akzeptierte Kategorien einzuführen,
wozu dieses Buch einen Beitrag leisten möchte.

# 

## Was ist eine DSL?

#

## Anforderungen an eine DSL

### Komfortabilität

Die offensichtliche Anforderung an eine DSL, ja ihr eigentlicher Kern, ist die
verbesserte Lesbarkeit und die vereinfachte Erstellung von Fachklassen eines klar
abgegrenzten Problembereichs. Dabei gibt es die Versuchung, dieser Anforderung
alles andere unterzuordnen. Aber wenn man der Ästhetik andere Aspekte unterordnet,
sollte das immer eine bewusste Entscheidung sein.

### Erweiterbarkeit

Eine Falle, in die man gerade beim DSL-Design leicht tappen kann, ist mangelnde
Erweiterbarkeit. Man operiert oft mit einem sehr spezialisierten Satz von
Techniken, und wenn diese eine bestimmte neue Anforderung nicht erfüllen können,
kann das ein DSL unbrauchbar machen. Deshalb sollte man schon im Vorfeld grob 
abschätzen, in welche Richtung sich die DSL nach der ersten Version entwickeln
könnte, und gegebenenfalls flexiblere Techniken wählen, selbst wenn das mit
etwas mehr Aufwand oder auch einer nicht ganz so "schönen" DSL verbunden ist.


### Performance

Normalerweise wird der Geschwindigkeit einer DSL wenig Bedeutung beigemessen.
Trotzdem sollte man immer daran denken, dass eine DSL in der Regel zusätzliche
Operationen oder auch Objekterzeugungen nach sich zieht. Bei Massendaten oder
auch schlecht designten DSLs kann es deshalb durchaus zu Problemen kommen.

### Bedienungssicherheit

Bei der Begeisterung, wie elegant man Dinge mit der neuen DSL ausdrücken kann,
wird oft übersehen, welche Stolperfallen es bei der Benutzung geben kann. Zum
einen kann es sein, dass eine für den Anwender "logisch" aussehende Syntax nicht
das gewünschte Ergebnis liefert, oder dass Dinge in der DSL ausgedrückt werden können,
die eigentlich nicht erlaubt sind - ob jetzt aus fachlicher oder technischer
Sicht. Ursachen können z.B. zu große Flexibilität der Syntax, fehlende
Sanity-Checks oder unklare Aufruf-Prioritäten bei Operatoren sein. Oft werden
solche Probleme erst entdeckt, wenn der Kreis der Benutzer erweitert wird,
und Neulinge mit der DSL experimentieren oder auch "kämpfen".


### Einhaltung von Best Coding Practices

#### Vermeidung von Namensraum-Verschmutzung

DSLs können oft in großen Teilen der Code-Basis verwendet werden, und es ist 
großartig, wenn sie stark genutzt werden. Aber mit jeder Verwendung steigt
die Gefahr, dass es zu Name-Clashes kommt. Wenn das DSL z.B. Erweiterungsmethoden
für oft verwendete Klassen wie Strings verwendet, werden solche Probleme
faktisch herausgefordert. Manchmal kommt es auch zu Clashes zwischen zwei
verschiedenen DSLs, die z.B. beide denselben Operator für eine Klasse definieren
möchten. Wenn möglich, sollten DSLs also auf eigenen oder zumindest spezifischen 
Datentypen operieren.

#### Entkopplung von Fachklassen

Beim Schreiben einer DSL kann man den Eindruck gewinnen, dass es ganz
offensichtlich der beste Weg ist, um mit den entsprechenden Entitäten zu
arbeiten. Man beginnt, DSL und unterliegende Fachklassen stark miteinander
zu koppeln. Meiner Meinung nach ist das eine schlechte Idee. Wo immer möglich
sollten die Fachklassen unabhängig von "ihrer" DSL sein. Insbesondere sollte
es stets möglich sein, die Fachklassen auch ohne DSL zu konstruieren. Dafür 
gibt es mehrere Gründe:

* Fachlogik wird mit DSL-Code vermischt
* die API und Gesamtumfang der Fachklasse wird aufgebläht
* das Design der Fachklasse wird durch Rücksicht auf das DSL-Design inflexibel
* die DSL kann zu einem späteren Zeitpunkt obsolet werden
* Codeanalyse- und Codegenerierungs-Tools können Probleme mit einem DSL haben
* die DSL kann für Massendaten "zu langsam" sein

Natürlich gibt es auch Ausnahmen von dieser Regel. Wenn man neue Klassen schreibt,
die bestimmte Operationen unterstützen, die sich ganz natürlich als Operationen
wie "Addition" auffassen lassen, kann es sinnvoll sein, dies gleich in der Klasse
zu implementieren.

# 

## Ein Prozess zum DSL-Design

Eine DSL zu designen ist eine spannende Herausforderung, viele Programmierer macht
es Spaß, ihrer Kreativität freien Lauf zu lassen zu können. Das ist auch gut so,
und kann ein DSL nur besser machen. Allerdings darf es dabei nicht dazu kommen,
dass man Anforderungen übersieht oder Best Practices verletzt. Es ist deshalb wichtig,
etwas Struktur in den Design-Prozess zu bringen.


### Anforderungen erheben

Bevor man mit der eigentlichen DSL loslegt, muss klar sein, welche Aufgaben sie
erfüllen soll, und - genauso wichtig - welche nicht. Die Bedeutung einer klaren
Abgrenzung kann gar nicht hoch genug eingestuft werden. Es ist auch eine Überlegung
wert, ob man einen sehr großen Aufgabenbereich nicht besser mit mehreren ähnlichen,
aber voneinander unabhängige DSLs abdeckt.

Wenn die Aufgabenstellung klar ist, sollten weitere Anforderungen abgeklärt werden.
Eine wichtige Frage ist z.B., wer die DSL nutzen soll, weil das wiederum Einfluss
auf das Komfortlevel und die Bedienungssicherheit hat. Weiterhin sollte geklärt werden,
ob besondere Anforderungen an die Performance bestehen.

### Idealisierte Syntax festlegen

Die Versuchung ist groß, nach der Anforderungsanalyse endlich "loszulegen", besonders
wenn man schon einige Ideen im Hinterkopf hat. Aus meiner Erfahrung hat sich dagegen
bewährt, ganz bewusst die Programmier-Brille abzulegen, möglichst Fach-Experten
hinzuzuziehen, und gemeinsam eine "ideale" DSL-Syntax ohne Rücksicht auf die
Realisierbarkeit zu brainstormen. Wenn man sich nicht einigen kann, ist es auch in
Ordnung, zwei oder drei Entwürfe zu haben.

Meist lohnt es sich nicht, die jeweilige Grammatik exakt zu spezifizieren. Es ist
besser, reichlich Beispiele anzugeben, und diese zu kommentieren und auf
Inkonsistenzen zu prüfen. Eine wichtige Information ist, wie die Beispiele jeweils
in Fachklassen übersetzt werden sollen.

Bereits in diesem Schritt sollte man versuchen, möglichst viel Feedback von
potenziellen Nutzern zu bekommen.

### Prototyping

Jetzt ist endlich der Zeitpunkt gekommen zu programmieren. Da für eine Umsetzung
meist viel experimentiert werden muss, und nicht immer klar ist, welche Lösungen wirklich 
tragfähig sind, bietet es sich an, mit einem Prototyp zu beginnen. Der Prototyp
kann dabei von mehreren ähnlichen Features nur eines enthalten
(Spike-Implementierung), Tests und Sicherheitsabfragen weglassen, und auf eine
vollständige Übersetzung in Fachklassen verzichten.

In regelmäßigen Abständen sollte geprüft werden, ob die Syntax des Prototyps
akzeptabel ist, und auch hier ist Feedback von poteziellen Nutzern wertvoll.
Natürlich ist es auch gut, mehrere konkurrierende Prototypen präsentieren zu können.

### Formalisierung und Dokumentation

Hat der Prototyp gezeigt, wie eine realisierbare Syntax aussehen könnte, und
gibt es Übereinstimmung, dass deren Qualität ausreichend ist, kann man damit
beginnen, diese Syntax zu formalisieren. Dabei hängt es ganz von der
Aufgabenstellung ab, wie detailliert die Syntax dokumentiert werden muss - 
von einer Wiki-Seite bis zu einer formalen Grammatik. Noch wichtiger als bei der
idealisierten Syntax ist, wie die Übersetzung in Fachklassen zu erfolgen hat.

Es wird davon abgeraten, diesen Schritt zu überspringen: Ein Prototyp ist
keine Dokumentation, und eine adäquate Dokumentation für ein DSL ist in der
Praxis unverzichtbar.

### Implementierung

Der letzte Schritt ist natürlich die eigentliche Implementierung. Oft lassen sich
Teile des Prototyps wiederverwenden, aber man sollte sich nicht scheuen, von
diesem abzuweichen und flexibleren oder effizienteren Code zu schreiben.
Eventuell können auch Quellcode-Generatoren hilfreich sein, um repetitiven
Code erzeugen zu lassen. Natürlich sollten jetzt im Gegensatz zum Prototypen
allen Best Coding Practices gefolgt werden, also z.B. Tests geschrieben und 
Konsistenz-Checks eingebaut werden.

Falls sich noch kleinere Abweichungen von der Dokumentation ergeben haben, sind
diese nachzupflegen.

## Algebraische DSLs

Probleme, bei denen sich die Objekte im weitesten Sinne wie Zahlen verhalten, also
algebraische Strukturen bilden, lassen sich sehr einfach als DSL in Kotlin formulieren.
Dazu gehören komplexe Zahlen, Quaternionen, Vektoren, Matrizen, physikalische
oder monetäre Einheiten und vieles mehr. Das DSL besteht in diesem Fall vor allem
aus einer Gruppe von Operator-Überladungen.

Eine gute Kenntnis von Erweiterungsmethoden und Operator-Überladungen ist in 
vielen DSLs unverzichtbar, auch wenn sie nicht oder nur teilweise in die
"algebraische" Kategorie fallen. 

### Fallstudie: Ein DSL für Komplexe Zahlen aus Apache Common Numbers

Wie im Vorwort angedeutet, will dieses Buch "Spielzeug-Beispiele" vermeiden.
Aus diesem Grund dient eine ausgereifte Java-Implementierung als Basis.
Der Sourcecode der Bibliothek findet sich unter 
https://github.com/apache/commons-numbers/tree/master/commons-numbers-complex

Hier eine mögliche DSL:

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
sind, eine sehr direkte und kompakte Umsetzung. Man kann diskutieren, ob man eine 
Vergleichsmethode implementieren sollte, es ist aber nicht unüblich, komplexe Zahlen
anhand ihres Absolutwertes zu ordnen.

Die DSL kann wie erwartet verwendet werden, es werden Ausdrücke wie `x + y` oder
`-2.0*(3.0 + x) - y*y` für zwei komplexe Zahlen `x` und `y` korrekt ausgewertet.

Für eine Übersicht der für eine Überladung verfügbaren Operatoren sei auf
https://kotlinlang.org/docs/operator-overloading.html
verwiesen.


