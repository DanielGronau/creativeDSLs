package creativeDSLs.chapter_08

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

operator fun Element.get(count: Int) =
    apply { require(this.count == 1 && count > 1) }
        .copy(count = count)
operator fun Group.get(count: Int) =
    apply { require(this.count == 1 && count > 1) }
        .copy(count = count)

val Element._2
    get() = this.apply { require(count == 1) }.copy(count = 2)
val Element._3
    get() = this.apply { require(count == 1) }.copy(count = 3)
val Element._4
    get() = this.apply { require(count == 1) }.copy(count = 4)
val Element._5
    get() = this.apply { require(count == 1) }.copy(count = 5)
val Element._6
    get() = this.apply { require(count == 1) }.copy(count = 6)
val Element._7
    get() = this.apply { require(count == 1) }.copy(count = 7)
val Element._8
    get() = this.apply { require(count == 1) }.copy(count = 8)
val Element._9
    get() = this.apply { require(count == 1) }.copy(count = 9)

val Group._2
    get() = this.apply { require(count == 1) }.copy(count = 2)
val Group._3
    get() = this.apply { require(count == 1) }.copy(count = 3)
val Group._4
    get() = this.apply { require(count == 1) }.copy(count = 4)
val Group._5
    get() = this.apply { require(count == 1) }.copy(count = 5)
val Group._6
    get() = this.apply { require(count == 1) }.copy(count = 6)
val Group._7
    get() = this.apply { require(count == 1) }.copy(count = 7)
val Group._8
    get() = this.apply { require(count == 1) }.copy(count = 8)
val Group._9
    get() = this.apply { require(count == 1) }.copy(count = 9)


operator fun Part.minus(that: Part) = Molecule(this, that)
operator fun Molecule.minus(that: Part) = copy(parts = parts + that)
operator fun Element.rangeTo(that: Part) = Group(this, that)
operator fun Group.rangeTo(that: Part) = copy(parts = parts + that)

operator fun Int.times(that: Molecule) =
    that.apply { require(factor == 1 && this@times > 1) }
        .copy(factor = this)
operator fun Int.times(that: Part) =
    Molecule(this, that)
        .apply { require(factor > 1) }
operator fun Molecule.plus(that: Molecule) = listOf(this, that)
operator fun Molecule.plus(that: Part) = listOf(this, Molecule(that))
operator fun Part.plus(that: Molecule) = listOf(Molecule(this), that)
operator fun List<Molecule>.plus(that: Part) = this + Molecule( that)

infix fun List<Molecule>.reactsTo(that: List<Molecule>) = Equation(this, that, false)
infix fun Molecule.reactsTo(that: List<Molecule>) = Equation(listOf(this), that, false)
infix fun List<Molecule>.reactsTo(that: Molecule) = Equation(this, listOf(that), false)
infix fun Molecule.reactsTo(that: Molecule) = Equation(listOf(this), listOf(that), false)
infix fun Part.reactsTo(that: List<Molecule>) = Equation(listOf(Molecule(this)), that, false)
infix fun List<Molecule>.reactsTo(that: Part) = Equation(this, listOf(Molecule(that)), false)
infix fun Part.reactsTo(that: Part) = Equation(listOf(Molecule(this)), listOf(Molecule(that)), false)
infix fun Part.reactsTo(that: Molecule) = Equation(listOf(Molecule(this)), listOf(that), false)
infix fun Molecule.reactsTo(that: Part) = Equation(listOf(this), listOf(Molecule(that)), false)

infix fun List<Molecule>.reversibleTo(that: List<Molecule>) = Equation(this, that, false)
infix fun Molecule.reversibleTo(that: List<Molecule>) = Equation(listOf(this), that, false)
infix fun List<Molecule>.reversibleTo(that: Molecule) = Equation(this, listOf(that), false)
infix fun Molecule.reversibleTo(that: Molecule) = Equation(listOf(this), listOf(that), false)
infix fun Part.reversibleTo(that: List<Molecule>) = Equation(listOf(Molecule(1,this)), that, false)
infix fun List<Molecule>.reversibleTo(that: Part) = Equation(this, listOf(Molecule(1,that)), false)
infix fun Part.reversibleTo(that: Part) = Equation(listOf(Molecule(this)), listOf(Molecule(that)), false)
infix fun Part.reversibleTo(that: Molecule) = Equation(listOf(Molecule(this)), listOf(that), false)
infix fun Molecule.reversibleTo(that: Part) = Equation(listOf(this), listOf(Molecule(that)), false)

fun main() {
    val bariumHydroxide = Molecule(3, Ba, Group(O, H)[2])
    val phosphoricAcid = Molecule(2, H[3], P, O[4])
    val water = Molecule(6, H[2], O)
    val bariumPhosphate = Molecule(Ba[3], Group(P, O[4])[2])

    val equation1 = Equation(
        listOf(bariumHydroxide, phosphoricAcid),
        listOf(water, bariumPhosphate),
        false
    )
    println(equation1)

    val equation2 = 3 * (Ba - (O..H)[2]) + 2 * (H[3] - P - O[4]) reactsTo
            6 * (H[2] - O) + (Ba[3] - (P..O[4])[2])
    println(equation2)

    val equation3 = 2*H[2] + O[2] reversibleTo 2*(H[2]-O)

    val equation4 = H[2] reactsTo 2 * H[2]

    val equation5 = 3 * (Ba - (O..H)._2) + 2 * (H._3 - P - O._4) reactsTo
            6 * (H._2 - O) + (Ba._3 - (P..O._4)._2)
}

val H = Element("H")
val He = Element("He")
val Li = Element("Li")
val Be = Element("Be")
val B = Element("B")
val C = Element("C")
val N = Element("N")
val O = Element("O")
val F = Element("F")
val Ne = Element("Ne")
val Na = Element("Na")
val Mg = Element("Mg")
val Al = Element("Al")
val Si = Element("Si")
val P = Element("P")
val S = Element("S")
val Cl = Element("Cl")
val Ar = Element("Ar")
val K = Element("K")
val Ca = Element("Ca")
val Sc = Element("Sc")
val Ti = Element("Ti")
val V = Element("V")
val Cr = Element("Cr")
val Mn = Element("Mn")
val Fe = Element("Fe")
val Co = Element("Co")
val Ni = Element("Ni")
val Cu = Element("Cu")
val Zn = Element("Zn")
val Ga = Element("Ga")
val Ge = Element("Ge")
val As = Element("As")
val Se = Element("Se")
val Br = Element("Br")
val Kr = Element("Kr")
val Rb = Element("Rb")
val Sr = Element("Sr")
val Y = Element("Y")
val Zr = Element("Zr")
val Nb = Element("Nb")
val Mo = Element("Mo")
val Tc = Element("Tc")
val Ru = Element("Ru")
val Rh = Element("Rh")
val Pd = Element("Pd")
val Ag = Element("Ag")
val Cd = Element("Cd")
val In = Element("In")
val Sn = Element("Sn")
val Sb = Element("Sb")
val Te = Element("Te")
val I = Element("I")
val Xe = Element("Xe")
val Cs = Element("Cs")
val Ba = Element("Ba")
val La = Element("La")
val Ce = Element("Ce")
val Pr = Element("Pr")
val Nd = Element("Nd")
val Pm = Element("Pm")
val Sm = Element("Sm")
val Eu = Element("Eu")
val Gd = Element("Gd")
val Tb = Element("Tb")
val Dy = Element("Dy")
val Ho = Element("Ho")
val Er = Element("Er")
val Tm = Element("Tm")
val Yb = Element("Yb")
val Lu = Element("Lu")
val Hf = Element("Hf")
val Ta = Element("Ta")
val W = Element("W")
val Re = Element("Re")
val Os = Element("Os")
val Ir = Element("Ir")
val Pt = Element("Pt")
val Au = Element("Au")
val Hg = Element("Hg")
val Tl = Element("Tl")
val Pb = Element("Pb")
val Bi = Element("Bi")
val Po = Element("Po")
val At = Element("At")
val Rn = Element("Rn")
val Fr = Element("Fr")
val Ra = Element("Ra")
val Ac = Element("Ac")
val Th = Element("Th")
val Pa = Element("Pa")
val U = Element("U")
val Np = Element("Np")
val Pu = Element("Pu")
val Am = Element("Am")
val Cm = Element("Cm")
val Bk = Element("Bk")
val Cf = Element("Cf")
val Es = Element("Es")
val Fm = Element("Fm")
val Md = Element("Md")
val No = Element("No")
val Lr = Element("Lr")
val Rf = Element("Rf")
val Db = Element("Db")
val Sg = Element("Sg")
val Bh = Element("Bh")
val Hs = Element("Hs")
val Mt = Element("Mt")
val Ds = Element("Ds")
val Rg = Element("Rg")
val Cn = Element("Cn")
val Nh = Element("Nh")
val Fl = Element("Fl")
val Mc = Element("Mc")
val Lv = Element("Lv")
val Ts = Element("Ts")
val Og = Element("Og")
