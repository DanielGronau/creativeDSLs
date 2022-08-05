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