package creativeDSLs.chapter_09.manual

import java.util.*

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

fun equation(string: String) = parseEquation(string.replace(" ", ""))
    .filter { it.second.isEmpty() }
    .map { it.first }

typealias ParseResult<T> = Optional<Pair<T, String>>

fun parseEquation(string: String): ParseResult<Equation> {
    return parseSide(string).flatMap { (lhs, s1) ->
        parseArrow(s1).flatMap { (arrow, s2) ->
            parseSide(s2).map { (rhs, s3) ->
                Equation(lhs, rhs, arrow == "<->") to s3
            }
        }
    }
}

fun parseSide(string: String): ParseResult<List<Molecule>> {
    val list = mutableListOf<Molecule>()
    var foundPlus: Boolean
    var s = string
    do {
        foundPlus = false
        val moleculePair = parseMolecule(s)
        if (moleculePair.isEmpty) {
            return Optional.empty()
        }
        moleculePair.ifPresent {
            list += it.first
            s = it.second
        }
        parsePattern(s, "+").ifPresent {
            foundPlus = true
            s = it.second
        }
    } while (foundPlus)
    return Optional.of(list.toList() to s)
             .filter{list.isNotEmpty()}
}

fun parseMolecule(string: String): ParseResult<Molecule> {
    var s = string
    var factor = 1
    val parts = mutableListOf<Part>()
    parseNum(string).ifPresent {
        factor = it.first
        s = it.second
    }
    var foundPart: Boolean
    do {
        foundPart = false
        parsePart(s).ifPresent {
            parts += it.first
            s = it.second
            foundPart = true
        }
    } while (foundPart)
    return when {
        parts.isEmpty() -> Optional.empty()
        else -> Optional.of(Molecule(factor, parts) to s)
    }
}

fun parsePart(string: String): ParseResult<Part> =
    Optional.empty<Pair<Part, String>>()
        .or { parseElement(string) }
        .or { parseGroup(string) }

fun parseElement(string: String): ParseResult<Element> = when {
    string.length >= 2 && elements.contains(string.substring(0, 2)) ->
        Optional.of(string.substring(0, 2) to string.substring(2))

    string.length >= 1 && elements.contains(string.substring(0, 1)) ->
        Optional.of(string.substring(0, 1) to string.substring(1))

    else -> Optional.empty()
}.map { (symbol, s) ->
    parseNum(s).map { (count, s1) ->
        Element(symbol, count) to s1
    }.orElse(Element(symbol,1) to s)
}

fun parseGroup(string: String): ParseResult<Group> =
    parsePattern(string, "(").flatMap { (_,s1) ->
        var s = s1
        val parts = mutableListOf<Part>()
        var foundPart: Boolean
        do {
            foundPart = false
            parsePart(s).ifPresent {
                parts += it.first
                s = it.second
                foundPart = true
            }
        } while(foundPart)
        parsePattern(s, ")").map {
            parts to it.second
        }
    }.filter{ (parts, _) -> parts.isNotEmpty()
    }.map { (parts, s) ->
        val count = parseNum(s)
        count.map { (count, s1) ->
            Group(parts, count) to s1
        }.orElse(Group(parts, 1) to s)
    }

fun parseArrow(string: String): ParseResult<String> =
    parsePattern(string, "<->")
        .or { parsePattern(string, "->") }

fun parsePattern(string: String, pattern: String): ParseResult<String> = when {
    string.startsWith(pattern) -> Optional.of(pattern to string.substring(pattern.length))
    else -> Optional.empty()
}

fun parseNum(string: String): ParseResult<Int> {
    var i = 0
    while (string.length > i && string[i].isDigit()) {
        i++
    }
    return when (i) {
        0 -> Optional.empty()
        else -> Optional.of(string.substring(0, i).toInt() to string.substring(i))
    }
}

fun main() {
    val p = equation("3Ba(OH)2 + 2H3PO4 -> 6H2O + Ba3(PO4)2")
    println(p)

    //println(parseGroup("()2"))
}

