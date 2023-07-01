package creativeDSLs.chapter_09.manual

import creativeDSLs.chapter_11.*

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

sealed interface ParseResult<out T> {
    infix fun or(that: () -> ParseResult<@UnsafeVariance T>): ParseResult<T>
    fun <U> flatMap(body: (T, String) -> ParseResult<U>): ParseResult<U>
    fun filter(cond: (T) -> Boolean): ParseResult<T>
}

class Failure<T> : ParseResult<T> {
    override fun or(that: () -> ParseResult<T>): ParseResult<T> = that()
    override fun <U> flatMap(body: (T, String) -> ParseResult<U>) = Failure<U>()
    override fun filter(cond: (T) -> Boolean): ParseResult<T> = this
    override fun toString() = "Failure"
}

data class Success<T>(val value: T, val remaining: String) : ParseResult<T> {
    override fun or(that: () -> ParseResult<T>): ParseResult<T> = this
    override fun <U> flatMap(body: (T, String) -> ParseResult<U>): ParseResult<U> =
        body(value, remaining)

    override fun filter(cond: (T) -> Boolean): ParseResult<T> = when {
        cond(value) -> this
        else -> Failure()
    }
}

fun <T> successWhen(cond: Boolean, body: () -> Pair<T, String>): ParseResult<T> =
    when {
        cond -> body().run { Success(first, second) }
        else -> Failure()
    }

fun <T> ParseResult<T>.successOrNull(): Success<T>? = when (this) {
    is Success -> this
    else -> null
}

fun <T> sequence(start: ParseResult<T>, step: (String) -> ParseResult<T>): ParseResult<List<T>> =
    Success(
        generateSequence(start.successOrNull()) { last ->
            step(last.remaining).successOrNull()
        }.toList(), ""
    ).filter {
        it.isNotEmpty()
    }.flatMap { list, _ ->
        Success(list.map { it.value }, list.last().remaining)
    }

fun equation(string: String): Equation? =
    parseEquation(string.replace(" ", ""))
        .successOrNull()
        ?.let { result ->
            result.value.takeIf { result.remaining.isEmpty() }
        }

fun parseEquation(string: String): ParseResult<Equation> =
    parseSide(string).flatMap { lhs, s1 ->
        parseArrow(s1).flatMap { arrow, s2 ->
            parseSide(s2).flatMap { rhs, s3 ->
                Success(Equation(lhs, rhs, arrow == "<->"), s3)
            }
        }
    }

fun parseSide(string: String): ParseResult<List<Molecule>> =
    sequence(parseMolecule(string)) { remaining ->
        parsePattern(remaining, "+")
            .flatMap { _, s2 -> parseMolecule(s2) }
    }

fun parseMolecule(string: String): ParseResult<Molecule> =
    (parseNum(string) or { Success(1, string) })
        .flatMap { coefficient, s ->
            sequence(parsePart(s)) { remaining ->
                parsePart(remaining)
            }.flatMap { parts, remaining ->
                Success(Molecule(coefficient, parts), remaining)
            }
        }

fun parsePart(string: String): ParseResult<Part> =
    Failure<Part>() or { parseElement(string) } or { parseGroup(string) }


fun parseElement(string: String): ParseResult<Element> =
    findElement(string, 2).or {
        findElement(string, 1)
    }.flatMap { symbol, s ->
        parseNum(s).flatMap { subscript, s1 ->
            Success(Element(symbol, subscript), s1)
        } or {
            Success(Element(symbol, 1), s)
        }
    }

fun findElement(string: String, charCount: Int): ParseResult<String> =
    successWhen(elements.contains("$string!!".take(charCount))) {
        "$string!!".take(charCount) to string.drop(charCount)
    }

fun parseGroup(string: String): ParseResult<Group> =
    parsePattern(string, "(").flatMap { _, s1 ->
        sequence(parsePart(s1)) { remaining ->
            parsePart(remaining)
        }
    }.flatMap { parts, remaining ->
        parsePattern(remaining, ")")
            .flatMap { _, s3 -> Success(parts, s3) }
    }.flatMap { parts, s ->
        parseNum(s).flatMap { subscript, s1 ->
            Success(Group(parts, subscript), s1)
        } or {
            Success(Group(parts, 1), s)
        }
    }

fun parseArrow(string: String): ParseResult<String> =
    parsePattern(string, "<->") or
            { parsePattern(string, "->") }

fun parsePattern(string: String, pattern: String): ParseResult<String> =
    successWhen(string.startsWith(pattern)) {
        pattern to string.drop(pattern.length)
    }

fun parseNum(string: String): ParseResult<Int> =
    string.takeWhile { it.isDigit() }.length.let { digitCount ->
        successWhen(digitCount > 0) {
            string.take(digitCount).toInt() to
                    string.drop(digitCount)
        }
    }


fun main() {
    val p = equation("3Ba(OH)2 + 2H3PO4 -> 6H2O + Ba3(PO4)2")
    println(p)

    println(parseGroup("()2"))
}

