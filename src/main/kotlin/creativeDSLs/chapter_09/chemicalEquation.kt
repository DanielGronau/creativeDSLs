package creativeDSLs.chapter_09

sealed interface Part

data class Element(
    val symbol: String,
    val subscript: Int = 1
) : Part {
    override fun toString() = symbol + subscript.oneAsEmpty()
}

data class Group(
    val parts: List<Part>,
    val subscript: Int = 1
) : Part {
    override fun toString() =
        parts.joinToString("", "(", ")") +
                subscript.oneAsEmpty()
}

data class Molecule(
    val coefficient: Int,
    val parts: List<Part>
) {
    constructor(coefficient: Int, vararg parts: Part) :
            this(coefficient, parts.toList())

    override fun toString() = coefficient.oneAsEmpty() +
            parts.joinToString("")
}

enum class Arrow(val symbol: String) {
    IRREVERSIBLE("->"),
    REVERSIBLE("<=>")
}

data class Equation(
    val leftSide: List<Molecule>,
    val arrow: Arrow,
    val rightSide: List<Molecule>
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
    val Ba = Element("Ba")
    val Ba3 = Element("Ba", 3)
    val O = Element("O")
    val O4 = Element("O", 4)
    val H = Element("H")
    val H2 = Element("H", 2)
    val H3 = Element("H", 3)
    val P = Element("P")
    val bariumHydroxide = Molecule(3, Ba, Group(listOf(O, H), 2))
    val phosphoricAcid = Molecule(2, H3, P, O4)
    val water = Molecule(6, H2, O)
    val bariumPhosphate = Molecule(1, Ba3, Group(listOf(P, O4), 2))

    val equation1 = Equation(
        listOf(bariumHydroxide, phosphoricAcid),
        Arrow.IRREVERSIBLE,
        listOf(water, bariumPhosphate)
    )
    println(equation1)
}