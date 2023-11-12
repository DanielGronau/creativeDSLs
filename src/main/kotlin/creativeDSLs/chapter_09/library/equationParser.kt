package creativeDSLs.chapter_09.library

import com.github.h0tk3y.betterParse.combinators.*
import com.github.h0tk3y.betterParse.grammar.Grammar
import com.github.h0tk3y.betterParse.grammar.parseToEnd
import com.github.h0tk3y.betterParse.grammar.parser
import com.github.h0tk3y.betterParse.lexer.literalToken
import com.github.h0tk3y.betterParse.lexer.regexToken
import com.github.h0tk3y.betterParse.lexer.token
import com.github.h0tk3y.betterParse.parser.Parser
import creativeDSLs.chapter_09.*

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

val equationGrammar = object : Grammar<Equation>() {
    val ws by regexToken("\\s+", ignore = true)
    val irreversible by literalToken("->")
    val reversible by literalToken("<->")
    val plus by literalToken("+")
    val leftPar by literalToken("(")
    val rightPar by literalToken(")")
    val num by regexToken("\\d+")
    val symbol by token { cs, from ->
        when {
            elements.contains("$cs##".substring(from, from + 2)) -> 2
            elements.contains("$cs##".substring(from, from + 1)) -> 1
            else -> 0
        }
    }

    val arrow: Parser<Arrow> by (irreversible asJust Arrow.IRREVERSIBLE) or
            (reversible asJust Arrow.REVERSIBLE)
    val number: Parser<Int> by (num use { text.toInt() })
    val element: Parser<Element> by (symbol and optional(number))
        .map { (s, n) -> Element(s.text, n ?: 1) }
    val group: Parser<Group> by (skip(leftPar) and
            oneOrMore(parser(this::part)) and
            skip(rightPar) and
            optional(number))
        .map { (parts, n) -> Group(parts, n ?: 1) }
    val part: Parser<Part> = element or group
    val molecule: Parser<Molecule> = (optional(number) and oneOrMore(part))
        .map { (n, parts) -> Molecule(n ?: 1, parts) }
    val side: Parser<List<Molecule>> = separated(molecule, plus)
        .map { it.terms }
    override val rootParser: Parser<Equation> by (side and arrow and side)
        .map { (lhs, a, rhs) -> Equation(lhs, rhs, a) }
}

fun main() {
    val eq = equationGrammar.parseToEnd("3Ba(OH)2 + 2H3PO4 -> 6H2O + Ba3(PO4)2")
    println(eq)
}