package creativeDSLs.chapter_05

import org.apache.commons.numbers.complex.Complex

val i = Complex.I

operator fun Complex.unaryPlus() = this

operator fun Complex.unaryMinus() = this.negate()

operator fun Complex.plus(that: Complex) = add(that)
operator fun Complex.plus(that: Double) = add(that)
operator fun Double.plus(that: Complex) = fromDouble(this).add(that)

operator fun Complex.minus(that: Complex) = subtract(that)
operator fun Complex.minus(that: Double) = subtract(that)
operator fun Double.minus(that: Complex) = fromDouble(this).subtract(that)

operator fun Complex.times(that: Complex) = multiply(that)
operator fun Complex.times(that: Double) = multiply(that)
operator fun Double.times(that: Complex) = fromDouble(this).multiply(that)

operator fun Complex.div(that: Complex) = divide(that)
operator fun Complex.div(that: Double) = divide(that)
operator fun Double.div(that: Complex) = fromDouble(this).divide(that)

infix fun Complex.pow(that: Complex) = pow(that)
infix fun Complex.pow(that: Double) = pow(that)

private fun fromDouble(d: Double) = Complex.ofCartesian(d, 0.0)

fun main() {
    val c = (3.0 + 4.0 * i) pow (5.0 + 6.0* i)
    println(c)
    val d = 3.0 + 4.0 * i pow 5.0 + 6.0* i
    println(d)
}