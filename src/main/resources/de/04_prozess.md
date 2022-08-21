# Ein Prozess zum DSL-Design

Eine DSL zu designen ist eine spannende Herausforderung, vielen Programmierer macht
es Spaß, ihrer Kreativität freien Lauf zu lassen zu können. Das ist auch gut so,
und kann ein DSL nur besser machen. Allerdings darf es dabei nicht dazu kommen,
dass man Anforderungen übersieht oder Best Practices verletzt. Es ist deshalb wichtig,
Struktur in den Design-Prozess zu bringen.

## Anforderungen erheben

Bevor man mit der eigentlichen DSL loslegt, muss klar sein, welche Aufgaben sie
erfüllen soll, und - genauso wichtig - welche nicht. Die Bedeutung einer klaren
Abgrenzung kann gar nicht hoch genug eingestuft werden. Es ist auch eine Überlegung
wert, ob man einen sehr großen Aufgabenbereich nicht besser mit mehreren ähnlichen,
aber voneinander unabhängige DSLs abdeckt.

Wenn die Aufgabenstellung klar ist, sollten weitere Anforderungen abgeklärt werden.
Eine wichtige Frage ist z.B., wer die DSL nutzen soll, weil das wiederum Einfluss
auf das Komfortlevel und die Bedienungssicherheit hat. Weiterhin sollte geklärt werden,
ob besondere Anforderungen an die Performance bestehen.

## Idealisierte Syntax festlegen

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

## Prototyping

Jetzt ist endlich der Zeitpunkt gekommen zu programmieren. Da für eine Umsetzung
meist viel experimentiert werden muss, und nicht immer klar ist, welche Lösungen wirklich
tragfähig sind, bietet es sich an, mit einem Prototyp zu beginnen. Der Prototyp
kann dabei von mehreren ähnlichen Features nur eines enthalten
(Spike-Implementierung), Tests und Sicherheitsabfragen weglassen, und auf eine
vollständige Übersetzung in Fachklassen verzichten.

In regelmäßigen Abständen sollte geprüft werden, ob die Syntax des Prototyps
akzeptabel ist, und auch hier ist Feedback von poteziellen Nutzern wertvoll.
Natürlich ist es auch gut, mehrere konkurrierende Prototypen präsentieren zu können.

## Formalisierung und Dokumentation

Hat der Prototyp gezeigt, wie eine realisierbare Syntax aussehen könnte, und
gibt es Übereinstimmung, dass deren Qualität ausreichend ist, kann man damit
beginnen, diese Syntax zu formalisieren. Dabei hängt es ganz von der
Aufgabenstellung ab, wie detailliert die Syntax dokumentiert werden muss -
von einer Wiki-Seite bis zu einer formalen Grammatik. Noch wichtiger als bei der
idealisierten Syntax ist, wie die Übersetzung in Fachklassen zu erfolgen hat.

Es wird davon abgeraten, diesen Schritt zu überspringen: Ein Prototyp ist
keine Dokumentation, und eine adäquate Dokumentation für ein DSL ist in der
Praxis unverzichtbar.

## Implementierung

Der letzte Schritt ist natürlich die eigentliche Implementierung. Oft lassen sich
Teile des Prototyps wiederverwenden, aber man sollte sich nicht scheuen, von
diesem abzuweichen und flexibleren oder effizienteren Code zu schreiben.
Eventuell können auch Quellcode-Generatoren hilfreich sein, um repetitiven
Code erzeugen zu lassen. Natürlich sollten jetzt im Gegensatz zum Prototypen
allen Best Coding Practices gefolgt werden, also z.B. Tests geschrieben und
Konsistenz-Checks eingebaut werden.

Falls sich noch kleinere Abweichungen von der Dokumentation ergeben haben, sind
diese nachzupflegen.


--> checkliste

Vollständigkeit

Besonderes Augenmerk sollte darauf gelegt werden, dass auch wirklich alle
erlaubten Konfigurationen mit der DSL ausgedrückt werden können. Dabei gibt
es zwei Fallstricke, nämlich dass Randfälle übersehen wurden, und dass die
Syntax für bestimmte Features nicht orthogonal ist. Ein Beispiel für letzteres
wäre, wenn eine Syntax nicht erlaubt "ist Säugetier" und gleichzeitig "legt Eier"
auszuwählen, obwohl diese Kombination existiert.

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

### Code Conventions

Ein Punkt, bei dem man bei der DSL-Implementierung oft Abstriche machen muss,
ist die Einhaltung von Code Conventions, etwa Regeln zur Groß- und Kleinschreibung.
Natürlich sollte man Implementierungen bevorzugen, die Code-Konventionen möglichst
wenig verletzen, aber im Kontext einer DSL-Implementierung sind Abweichungen
davon prinzipiell akzeptabel. In diesen Fällen sollte z.B. mittels Kommentaren
dokumentiert werden, dass es sich dabei um eine bewusste Entscheidung gehandelt hat.

