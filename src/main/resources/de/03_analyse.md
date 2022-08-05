# Anforderungsanalyse

## Komfortabilität

Die offensichtliche Anforderung an eine DSL, ja ihr eigentlicher Kern, ist die
verbesserte Lesbarkeit und die vereinfachte Erstellung von Fachklassen eines klar
abgegrenzten Problembereichs. Dabei gibt es die Versuchung, dieser Anforderung
alles andere unterzuordnen. Aber wenn man der Ästhetik andere Aspekte unterordnet,
sollte das immer eine bewusste Entscheidung sein.

## Bedienungssicherheit

Bei der Begeisterung, wie elegant man Dinge mit der neuen DSL ausdrücken kann,
wird oft übersehen, welche Stolperfallen es bei der Benutzung geben kann. Zum
einen kann es sein, dass eine für den Anwender "logisch" aussehende Syntax nicht
das gewünschte Ergebnis liefert, oder dass Dinge in der DSL ausgedrückt werden können,
die eigentlich nicht erlaubt sind - ob jetzt aus fachlicher oder technischer
Sicht. Ursachen können z.B. zu große Flexibilität der Syntax, fehlende
Sanity-Checks oder unklare Aufruf-Prioritäten bei Operatoren sein. Oft werden
solche Probleme erst entdeckt, wenn der Kreis der Benutzer erweitert wird,
und Neulinge mit der DSL experimentieren oder auch "kämpfen".

## Vollständigkeit

Besonderes Augenmerk sollte darauf gelegt werden, dass auch wirklich alle
erlaubten Konfigurationen mit der DSL ausgedrückt werden können. Dabei gibt
es zwei Fallstricke, nämlich dass Randfälle übersehen wurden, und dass die
Syntax für bestimmte Features nicht orthogonal ist. Ein Beispiel für letzteres
wäre, wenn eine Syntax nicht erlaubt "ist Säugetier" und gleichzeitig "legt Eier"
auszuwählen, obwohl diese Kombination existiert.

## Erweiterbarkeit

Eine Falle, in die man gerade beim DSL-Design leicht tappen kann, ist mangelnde
Erweiterbarkeit. Man operiert oft mit einem sehr spezialisierten Satz von
Techniken, und wenn diese eine bestimmte neue Anforderung nicht erfüllen können,
kann das ein DSL unbrauchbar machen. Deshalb sollte man schon im Vorfeld grob
abschätzen, in welche Richtung sich die DSL nach der ersten Version entwickeln
könnte, und gegebenenfalls flexiblere Techniken wählen, selbst wenn das mit
etwas mehr Aufwand oder auch einer nicht ganz so "schönen" DSL verbunden ist.


## Performance

Normalerweise wird der Geschwindigkeit einer DSL wenig Bedeutung beigemessen.
Trotzdem sollte man immer daran denken, dass eine DSL in der Regel zusätzliche
Operationen oder auch Objekterzeugungen nach sich zieht. Bei Massendaten oder
auch schlecht designten DSLs kann es deshalb durchaus zu Problemen kommen.

## Einhaltung von Best Coding Practices

### Vermeidung von Namensraum-Verschmutzung

DSLs können oft in großen Teilen der Code-Basis verwendet werden, und es ist
großartig, wenn sie stark genutzt werden. Aber mit jeder Verwendung steigt
die Gefahr, dass es zu Name-Clashes kommt. Wenn das DSL z.B. Erweiterungsmethoden
für oft verwendete Klassen wie Strings verwendet, werden solche Probleme
faktisch herausgefordert. Manchmal kommt es auch zu Clashes zwischen zwei
verschiedenen DSLs, die z.B. beide denselben Operator für eine Klasse definieren
möchten. Wenn möglich, sollten DSLs also auf eigenen oder zumindest spezifischen
Datentypen operieren.

### Entkopplung von Fachklassen

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