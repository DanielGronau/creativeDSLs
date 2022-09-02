package creativeDSLs

import org.apache.commons.numbers.complex.Complex

val i = Complex.I

operator fun Complex.unaryPlus(): Complex = this

operator fun Complex.unaryMinus(): Complex = this.negate()

operator fun Complex.plus(that: Complex): Complex = add(that)
operator fun Complex.plus(that: Double): Complex = add(that)
operator fun Double.plus(that: Complex): Complex = fromDouble(this).add(that)

operator fun Complex.minus(that: Complex): Complex = subtract(that)
operator fun Complex.minus(that: Double): Complex = subtract(that)
operator fun Double.minus(that: Complex): Complex = fromDouble(this).subtract(that)

operator fun Complex.times(that: Complex): Complex = multiply(that)
operator fun Complex.times(that: Double): Complex = multiply(that)
operator fun Double.times(that: Complex): Complex = fromDouble(this).multiply(that)

operator fun Complex.div(that: Complex): Complex = divide(that)
operator fun Complex.div(that: Double): Complex = divide(that)
operator fun Double.div(that: Complex): Complex = fromDouble(this).divide(that)

private fun fromDouble(d: Double) = Complex.ofCartesian(d, 0.0)

