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

fun parseSide(string: String): ParseResult<List<Molecule>> =
    Optional.of(
        generateSequence(parseMolecule(string).orElse(null)) { (_, s1) ->
            parsePattern(s1, "+")
                .flatMap { (_, s2) -> parseMolecule(s2) }
                .orElse(null)
        }.toList()
    ).filter {
        it.isNotEmpty()
    }.map { list ->
        list.map { it.first } to list.last().second
    }

fun parseMolecule(string: String): ParseResult<Molecule> =
    parseNum(string).or {
        Optional.of(1 to string)
    }.flatMap { (factor, s) ->
        Optional.of(
            generateSequence(parsePart(s).orElse(null)) { (_, s1) ->
                parsePart(s1).orElse(null)
            }.toList()
        ).filter {
            it.isNotEmpty()
        }.map { parts ->
            Molecule(factor, parts.map { it.first }) to parts.last().second
        }
    }

fun parsePart(string: String): ParseResult<Part> =
    Optional.empty<Pair<Part, String>>()
        .or { parseElement(string) }
        .or { parseGroup(string) }

fun parseElement(string: String): ParseResult<Element> =
    findElement(string, 2).or {
        findElement(string, 1)
    }.map { (symbol, s) ->
        parseNum(s).map { (count, s1) ->
            Element(symbol, count) to s1
        }.orElseGet {
            Element(symbol, 1) to s
        }
    }

fun findElement(string: String, charCount: Int): ParseResult<String> =
    Optional.of(
        "$string!!".substring(0, charCount)
    ).filter {
        elements.contains("$string!!".substring(0, charCount))
    }.map { symbol ->
        symbol to string.substring(charCount)
    }

fun parseGroup(string: String): ParseResult<Group> =
    parsePattern(string, "(").map { (_, s1) ->
        generateSequence(parsePart(s1).orElse(null)) { (_, s2) ->
            parsePart(s2).orElse(null)
        }.toList()
    }.filter {
        it.isNotEmpty()
    }.flatMap { parts ->
        parsePattern(parts.last().second, ")").map { (_, s3) ->
            parts.map { it.first } to s3
        }
    }.map { (parts, s) ->
        parseNum(s).map { (count, s1) ->
            Group(parts, count) to s1
        }.orElseGet {
            Group(parts, 1) to s
        }
    }

fun parseArrow(string: String): ParseResult<String> =
    parsePattern(string, "<->")
        .or { parsePattern(string, "->") }

fun parsePattern(string: String, pattern: String): ParseResult<String> = when {
    string.startsWith(pattern) -> Optional.of(pattern to string.substring(pattern.length))
    else -> Optional.empty()
}

fun parseNum(string: String): ParseResult<Int> =
    Optional.of(
        string.takeWhile { it.isDigit() }.length
    ).filter { digitCount ->
        digitCount > 0
    }.map { digitCount ->
        string.substring(0, digitCount).toInt() to string.substring(digitCount)
    }

fun main() {
    val p = equation("3Ba(OH)2 + 2H3PO4 -> 6H2O + Ba3(PO4)2")
    println(p)

    //println(parseGroup("()2"))
}

