package creativeDSLs

import org.apache.commons.numbers.complex.Complex

val i = Complex.I

operator fun Complex.unaryPlus(): Complex = this

operator fun Complex.unaryMinus(): Complex = this.negate()

operator fun Complex.plus(other: Complex): Complex = add(other)
operator fun Complex.plus(other: Double): Complex = add(other)
operator fun Double.plus(other: Complex): Complex = fromDouble(this).add(this)

operator fun Complex.minus(other: Complex): Complex = subtract(other)
operator fun Complex.minus(other: Double): Complex = subtract(other)
operator fun Double.minus(other: Complex): Complex = fromDouble(this).subtract(other)

operator fun Complex.times(other: Complex): Complex = multiply(other)
operator fun Complex.times(other: Double): Complex = multiply(other)
operator fun Double.times(other: Complex): Complex = fromDouble(this).multiply(other)

operator fun Complex.div(other: Complex): Complex = divide(other)
operator fun Complex.div(other: Double): Complex = divide(other)
operator fun Double.div(other: Complex): Complex = fromDouble(this).divide(other)

operator fun Complex.compareTo(other: Complex): Int = abs().compareTo(other.abs())

private fun fromDouble(d: Double) = Complex.ofCartesian(d, 0.0)

