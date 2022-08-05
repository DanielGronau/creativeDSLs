# Ein Prozess zum DSL-Design

Eine DSL zu designen ist eine spannende Herausforderung, viele Programmierer macht
es Spaß, ihrer Kreativität freien Lauf zu lassen zu können. Das ist auch gut so,
und kann ein DSL nur besser machen. Allerdings darf es dabei nicht dazu kommen,
dass man Anforderungen übersieht oder Best Practices verletzt. Es ist deshalb wichtig,
etwas Struktur in den Design-Prozess zu bringen.


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