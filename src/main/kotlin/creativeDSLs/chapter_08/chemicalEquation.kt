package creativeDSLs.chapter_08

sealed interface Part

data class Element(val symbol: String, val subscript: Int) : Part {
    override fun toString() = when (subscript) {
        1 -> symbol
        else -> symbol + subscript
    }
}

data class Group(val parts: List<Part>, val subscript: Int) : Part {
    override fun toString() = when (subscript) {
        1 -> parts.joinToString("", "(", ")")
        else -> parts.joinToString("", "(", ")") + subscript
    }
}

data class Molecule(val coefficient: Int, val parts: List<Part>) {
    override fun toString() = when (coefficient) {
        1 -> parts.joinToString("")
        else -> "$coefficient${parts.joinToString("")}"
    }
}

data class Equation(val leftSide: List<Molecule>, val rightSide: List<Molecule>, val reversible: Boolean = false) {
    override fun toString() = leftSide.joinToString(" + ") +
            (if (reversible) " <-> " else " -> ") +
            rightSide.joinToString(" + ")
}


fun main() {
    val Ba = Element("Ba",1)
    val O = Element("O",1)
    val H = Element("H",1)
    val P = Element("P",1)
    val bariumHydroxide = Molecule(3, listOf(Ba, Group(listOf(O, H), 2)))
    val phosphoricAcid = Molecule(2, listOf(Element("H",3), P, Element("O",4)))
    val water = Molecule(6, listOf(Element("H",2), O))
    val bariumPhosphate = Molecule(1, listOf( Element("Ba",3), Group(listOf(P, Element("O",4)),2)))

    val equation1 = Equation(
        listOf(bariumHydroxide, phosphoricAcid),
        listOf(water, bariumPhosphate),
        false
    )
    println(equation1)
}