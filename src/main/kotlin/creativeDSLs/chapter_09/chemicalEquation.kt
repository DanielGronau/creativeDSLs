package creativeDSLs.chapter_09

sealed interface Part

data class Element(
    val symbol: String,
    val subscript: Int
) : Part {
    override fun toString() = symbol + subscript.oneAsEmpty()
}

data class Group(
    val parts: List<Part>,
    val subscript: Int
) : Part {
    override fun toString() =
        parts.joinToString("", "(", ")") +
                subscript.oneAsEmpty()
}

data class Molecule(
    val coefficient: Int,
    val parts: List<Part>
) {
    override fun toString() = coefficient.oneAsEmpty() +
            parts.joinToString("")
}

enum class Arrow(val symbol: String) {
    IRREVERSIBLE("->"),
    REVERSIBLE("<->")
}

data class Equation(
    val leftSide: List<Molecule>,
    val rightSide: List<Molecule>,
    val arrow: Arrow
) {
    override fun toString() = listOf(
        leftSide.joinToString(" + "),
        arrow.symbol,
        rightSide.joinToString(" + ")
    ).joinToString(" ")
}

private fun Int.oneAsEmpty(): String =
    takeIf { this > 1 }?.toString().orEmpty()

fun main() {
    val Ba = Element("Ba", 1)
    val O = Element("O", 1)
    val H = Element("H", 1)
    val P = Element("P", 1)
    val bariumHydroxide = Molecule(3, listOf(Ba, Group(listOf(O, H), 2)))
    val phosphoricAcid = Molecule(2, listOf(Element("H", 3), P, Element("O", 4)))
    val water = Molecule(6, listOf(Element("H", 2), O))
    val bariumPhosphate = Molecule(1, listOf(Element("Ba", 3), Group(listOf(P, Element("O", 4)), 2)))

    val equation1 = Equation(
        listOf(bariumHydroxide, phosphoricAcid),
        listOf(water, bariumPhosphate),
        Arrow.IRREVERSIBLE
    )
    println(equation1)
}