package creativeDSLs.chapter_09.library

interface Part

data class Element(val symbol: String, val count: Int) : Part {
    constructor(symbol: String) : this(symbol, 1)
    override fun toString() = when (count) {
        1 -> symbol
        else -> symbol + count
    }
}

data class Group(val parts: List<Part>, val count: Int) : Part {
    constructor(vararg parts: Part) : this(parts.asList(), 1)
    override fun toString() = when (count) {
        1 -> parts.joinToString("", "(", ")")
        else -> parts.joinToString("", "(", ")") + count
    }
}

data class Molecule(val factor: Int, val parts: List<Part>) {
    constructor(vararg parts: Part) : this(1, parts.asList())
    constructor(factor: Int, vararg parts: Part) : this(factor, parts.asList())
    override fun toString() = when (factor) {
        1 -> parts.joinToString("")
        else -> "$factor${parts.joinToString("")}"
    }
}

data class Equation(val leftSide: List<Molecule>, val rightSide: List<Molecule>, val reversible: Boolean = false) {
    override fun toString() = leftSide.joinToString(" + ") +
            (if (reversible) " <-> " else " -> ") +
            rightSide.joinToString(" + ")
}

private val elements = setOf(
    "H", "He", "Li", "Be", "B", "C", "N", "O", "F", "Ne", "Na", "Mg", "Al", "Si",
    "P", "S", "Cl", "Ar", "K", "Ca", "Sc", "Ti", "V", "Cr", "Mn", "Fe", "Co",
    "Ni", "Cu", "Zn", "Ga", "Ge", "As", "Se", "Br", "Kr", "Rb", "Sr", "Y", "Zr",
    "Nb", "Mo", "Tc", "Ru", "Rh", "Pd", "Ag", "Cd", "In", "Sn", "Sb", "Te", "I",
    "Xe", "Cs", "Ba", "La", "Ce", "Pr", "Nd", "Pm", "Sm", "Eu", "Gd", "Tb", "Dy",
    "Ho", "Er", "Tm", "Yb", "Lu", "Hf", "Ta", "W", "Re", "Os", "Ir", "Pt", "Au",
    "Hg", "Tl", "Pb", "Bi", "Po", "At", "Rn", "Fr", "Ra", "Ac", "Th", "Pa", "U",
    "Np", "Pu", "Am", "Cm", "Bk", "Cf", "Es", "Fm", "Md", "No", "Lr", "Rf", "Db",
    "Sg", "Bh", "Hs", "Mt", "Ds", "Rg", "Cn", "Nh", "Fl", "Mc", "Lv", "Ts", "Og"
)

fun main() {

}