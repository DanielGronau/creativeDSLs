
# Was ist eine DSL?

Dazu liefert Wikipedia eine gute, wenn auch etwas hochgestochene Antwort:

> Eine __domänenspezifische Sprache__ (englisch _domain-specific language_, kurz _DSL_) 
> oder __anwendungsspezifische Sprache__ ist eine formale Sprache, die zur 
> Interaktion zwischen Menschen und digital arbeitenden Computern ("Computersprache") 
> für ein bestimmtes Problemfeld (die sogenannte _Domäne_) entworfen und implementiert
> wird. Beim Entwurf einer DSL wird man bemüht sein, einen hohen Grad an 
> Problemspezifität zu erreichen: Die Sprache soll alle Probleme der Domäne darstellen
> können und nichts darstellen können, was außerhalb der Domäne liegt. 
> Dadurch ist sie durch Domänenspezialisten ohne besonderes Zusatzwissen bedienbar.

(aus: https://de.wikipedia.org/wiki/Dom%C3%A4nenspezifische_Sprache)

Dieses Buch behandelt DSLs, die in Kotlin eingebettet sind und sich nur der bereits 
in Kotlin vorhandenen Ausdrucksmöglichkeiten bedienen können. Es handelt sich also
um __interne DSLs__ (auch "eingebettete DSLs"). Der große Vorteil ist natürlich, dass
sich solche DSLs nahtlos in den Rest des Programms einfügen, es müssen z.B. keine 
Dateien eingelesen und geparst werden.

Der Nachteil interner DSLs ist die Beschränkung der Syntax durch die Möglichkeiten
der "Wirtssprache". Kotlin gibt beim DSL-Design einen enormen Freiraum, vor allem
verglichen mit Java. Trotzdem kann es sein, dass man auf unüberwindbare Grenzen stößt,
und dann sollte man nicht vergessen, dass externe DSLs ebenfalls eine Option sind.
Diese sind immer noch aufwendiger zu erstellen, aber der Abstand zu internen DSLs
hat sich durch neue Bibliotheken und verbessertes Tooling in den letzten Jahren deutlich
verringert.

