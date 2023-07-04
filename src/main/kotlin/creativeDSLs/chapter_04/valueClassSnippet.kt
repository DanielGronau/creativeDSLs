package creativeDSLs.chapter_04

@JvmInline
value class Kilometers(val value: Double)

@JvmInline
value class Miles(val value: Double)

fun Kilometers.toMiles() : Miles = Miles(this.value * 0.6214)

val marathonMiles = Kilometers(42.195).toMiles() // Miles(value=26.219973)

fun main() {
    println(marathonMiles)
}

