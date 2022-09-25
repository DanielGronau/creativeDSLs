package creativeDSLs.chapter_08.equationDSL

import creativeDSLs.chapter_08.*

operator fun Element.get(subscript: Int) =
    apply { require(this.subscript == 1 && subscript > 1) }
        .copy(subscript = subscript)
operator fun Group.get(subscript: Int) =
    apply { require(this.subscript == 1 && subscript > 1) }
        .copy(subscript = subscript)

val Element._2
    get() = this.apply { require(subscript == 1) }.copy(subscript = 2)
val Element._3
    get() = this.apply { require(subscript == 1) }.copy(subscript = 3)
val Element._4
    get() = this.apply { require(subscript == 1) }.copy(subscript = 4)
val Element._5
    get() = this.apply { require(subscript == 1) }.copy(subscript = 5)
val Element._6
    get() = this.apply { require(subscript == 1) }.copy(subscript = 6)
val Element._7
    get() = this.apply { require(subscript == 1) }.copy(subscript = 7)
val Element._8
    get() = this.apply { require(subscript == 1) }.copy(subscript = 8)
val Element._9
    get() = this.apply { require(subscript == 1) }.copy(subscript = 9)

val Group._2
    get() = this.apply { require(subscript == 1) }.copy(subscript = 2)
val Group._3
    get() = this.apply { require(subscript == 1) }.copy(subscript = 3)
val Group._4
    get() = this.apply { require(subscript == 1) }.copy(subscript = 4)
val Group._5
    get() = this.apply { require(subscript == 1) }.copy(subscript = 5)
val Group._6
    get() = this.apply { require(subscript == 1) }.copy(subscript = 6)
val Group._7
    get() = this.apply { require(subscript == 1) }.copy(subscript = 7)
val Group._8
    get() = this.apply { require(subscript == 1) }.copy(subscript = 8)
val Group._9
    get() = this.apply { require(subscript == 1) }.copy(subscript = 9)


operator fun Part.minus(that: Part) = Molecule(1,listOf(this, that))
operator fun Molecule.minus(that: Part) = copy(parts = parts + that)
operator fun Element.rangeTo(that: Part) = Group(listOf(this, that),1)
operator fun Group.rangeTo(that: Part) = copy(parts = parts + that)

operator fun Int.times(that: Molecule) =
    that.apply { require(coefficient == 1 && this@times > 1) }
        .copy(coefficient = this)
operator fun Int.times(that: Part) =
    Molecule(this, listOf(that))
        .apply { require(coefficient > 1) }
operator fun Molecule.plus(that: Molecule) = listOf(this, that)
operator fun Molecule.plus(that: Part) = listOf(this, Molecule(1,listOf(that)))
operator fun Part.plus(that: Molecule) = listOf(Molecule(1,listOf(this)), that)
operator fun List<Molecule>.plus(that: Part) = this + Molecule( 1, listOf( that))

infix fun List<Molecule>.reactsTo(that: List<Molecule>) = Equation(this, that, false)
infix fun Molecule.reactsTo(that: List<Molecule>) = Equation(listOf(this), that, false)
infix fun List<Molecule>.reactsTo(that: Molecule) = Equation(this, listOf(that), false)
infix fun Molecule.reactsTo(that: Molecule) = Equation(listOf(this), listOf(that), false)
infix fun Part.reactsTo(that: List<Molecule>) = Equation(listOf(Molecule(1,listOf(this))), that, false)
infix fun List<Molecule>.reactsTo(that: Part) = Equation(this, listOf(Molecule(1, listOf(that))), false)
infix fun Part.reactsTo(that: Part) = Equation(listOf(Molecule(1,listOf(this))), listOf(Molecule(1,listOf(that))), false)
infix fun Part.reactsTo(that: Molecule) = Equation(listOf(Molecule(1,listOf(this))), listOf(that), false)
infix fun Molecule.reactsTo(that: Part) = Equation(listOf(this), listOf(Molecule(1,listOf(that))), false)

infix fun List<Molecule>.reversibleTo(that: List<Molecule>) = Equation(this, that, false)
infix fun Molecule.reversibleTo(that: List<Molecule>) = Equation(listOf(this), that, false)
infix fun List<Molecule>.reversibleTo(that: Molecule) = Equation(this, listOf(that), false)
infix fun Molecule.reversibleTo(that: Molecule) = Equation(listOf(this), listOf(that), false)
infix fun Part.reversibleTo(that: List<Molecule>) = Equation(listOf(Molecule(1,listOf(this))), that, false)
infix fun List<Molecule>.reversibleTo(that: Part) = Equation(this, listOf(Molecule(1,listOf(that))), false)
infix fun Part.reversibleTo(that: Part) = Equation(listOf(Molecule(1,listOf(this))), listOf(Molecule(1,listOf(that))), false)
infix fun Part.reversibleTo(that: Molecule) = Equation(listOf(Molecule(1, listOf(this))), listOf(that), false)
infix fun Molecule.reversibleTo(that: Part) = Equation(listOf(this), listOf(Molecule(1, listOf(that))), false)

fun main() {
    val equation2 = 3 * (Ba - (O..H)[2]) + 2 * (H[3] - P - O[4]) reactsTo
            6 * (H[2] - O) + (Ba[3] - (P..O[4])[2])
    println(equation2)

    val equation3 = 2*H[2] + O[2] reversibleTo 2*(H[2]-O)

    val equation4 = H[2] reactsTo 2 * H[2]

    val equation5 = 3 * (Ba - (O..H)._2) + 2 * (H._3 - P - O._4) reactsTo
            6 * (H._2 - O) + (Ba._3 - (P..O._4)._2)
}

val H = Element("H",1)
val He = Element("He",1)
val Li = Element("Li",1)
val Be = Element("Be",1)
val B = Element("B",1)
val C = Element("C",1)
val N = Element("N",1)
val O = Element("O",1)
val F = Element("F",1)
val Ne = Element("Ne",1)
val Na = Element("Na",1)
val Mg = Element("Mg",1)
val Al = Element("Al",1)
val Si = Element("Si",1)
val P = Element("P",1)
val S = Element("S",1)
val Cl = Element("Cl",1)
val Ar = Element("Ar",1)
val K = Element("K",1)
val Ca = Element("Ca",1)
val Sc = Element("Sc",1)
val Ti = Element("Ti",1)
val V = Element("V",1)
val Cr = Element("Cr",1)
val Mn = Element("Mn",1)
val Fe = Element("Fe",1)
val Co = Element("Co",1)
val Ni = Element("Ni",1)
val Cu = Element("Cu",1)
val Zn = Element("Zn",1)
val Ga = Element("Ga",1)
val Ge = Element("Ge",1)
val As = Element("As",1)
val Se = Element("Se",1)
val Br = Element("Br",1)
val Kr = Element("Kr",1)
val Rb = Element("Rb",1)
val Sr = Element("Sr",1)
val Y = Element("Y",1)
val Zr = Element("Zr",1)
val Nb = Element("Nb",1)
val Mo = Element("Mo",1)
val Tc = Element("Tc",1)
val Ru = Element("Ru",1)
val Rh = Element("Rh",1)
val Pd = Element("Pd",1)
val Ag = Element("Ag",1)
val Cd = Element("Cd",1)
val In = Element("In",1)
val Sn = Element("Sn",1)
val Sb = Element("Sb",1)
val Te = Element("Te",1)
val I = Element("I",1)
val Xe = Element("Xe",1)
val Cs = Element("Cs",1)
val Ba = Element("Ba",1)
val La = Element("La",1)
val Ce = Element("Ce",1)
val Pr = Element("Pr",1)
val Nd = Element("Nd",1)
val Pm = Element("Pm",1)
val Sm = Element("Sm",1)
val Eu = Element("Eu",1)
val Gd = Element("Gd",1)
val Tb = Element("Tb",1)
val Dy = Element("Dy",1)
val Ho = Element("Ho",1)
val Er = Element("Er",1)
val Tm = Element("Tm",1)
val Yb = Element("Yb",1)
val Lu = Element("Lu",1)
val Hf = Element("Hf",1)
val Ta = Element("Ta",1)
val W = Element("W",1)
val Re = Element("Re",1)
val Os = Element("Os",1)
val Ir = Element("Ir",1)
val Pt = Element("Pt",1)
val Au = Element("Au",1)
val Hg = Element("Hg",1)
val Tl = Element("Tl",1)
val Pb = Element("Pb",1)
val Bi = Element("Bi",1)
val Po = Element("Po",1)
val At = Element("At",1)
val Rn = Element("Rn",1)
val Fr = Element("Fr",1)
val Ra = Element("Ra",1)
val Ac = Element("Ac",1)
val Th = Element("Th",1)
val Pa = Element("Pa",1)
val U = Element("U",1)
val Np = Element("Np",1)
val Pu = Element("Pu",1)
val Am = Element("Am",1)
val Cm = Element("Cm",1)
val Bk = Element("Bk",1)
val Cf = Element("Cf",1)
val Es = Element("Es",1)
val Fm = Element("Fm",1)
val Md = Element("Md",1)
val No = Element("No",1)
val Lr = Element("Lr",1)
val Rf = Element("Rf",1)
val Db = Element("Db",1)
val Sg = Element("Sg",1)
val Bh = Element("Bh",1)
val Hs = Element("Hs",1)
val Mt = Element("Mt",1)
val Ds = Element("Ds",1)
val Rg = Element("Rg",1)
val Cn = Element("Cn",1)
val Nh = Element("Nh",1)
val Fl = Element("Fl",1)
val Mc = Element("Mc",1)
val Lv = Element("Lv",1)
val Ts = Element("Ts",1)
val Og = Element("Og",1)
