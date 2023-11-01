package creativeDSLs.chapter_05.contextReceiver

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

data class Modulus(val modulus: Long) {
    init {
        require(modulus > 1)
    }
}

@JvmInline
value class Modular(val n: Long)

context(Modulus)
val Long.m
    get() = Modular(remainder(this))

context(Modulus)
val Int.m
    get() = Modular(remainder(this.toLong()))

context(Modulus)
operator fun Modular.plus(that: Modular) = Modular(remainder(this.n + that.n))

context(Modulus)
operator fun Modular.minus(that: Modular) = Modular(remainder(this.n - that.n))

context(Modulus)
operator fun Modular.times(that: Modular) = Modular(remainder(this.n * that.n))

context(Modulus)
operator fun Modular.div(that: Modular) = Modular(remainder(this.n * inverse(that.n)))

context (Modulus)
private fun remainder(n: Long) = when {
    n < 0 -> (n % modulus) + modulus
    else -> n % modulus
}

private data class GcdResult(val gcd: Long, val x: Long, val y: Long)

context(Modulus)
private fun inverse(a: Long): Long =
    extendedGCD(a, modulus)
        .run {
            when (gcd) {
                1L -> remainder(x)
                else -> throw ArithmeticException(
                    "Can't divide by $a (mod $modulus)"
                )
            }
        }

private fun extendedGCD(a: Long, b: Long): GcdResult =
    when (b) {
        0L -> GcdResult(a, 1, 0)
        else -> {
            val result = extendedGCD(b, a % b)
            val x = result.y
            val y = result.x - (a / b) * result.y
            GcdResult(result.gcd, x, y)
        }
    }

context(Modulus)
fun square(n: Modular) = n * n

fun <R> modulus(m: Long, body: context(Modulus) () -> R) =
    with(Modulus(m)) {
        body(this)
    }

fun main() {
    val x = modulus(7) {
        square(3.m + 5.m) + square(3.m - 5.m)
    }
    println(x) // Modular(n=5)
}

@OptIn(ExperimentalContracts::class)
@Suppress("SUBTYPING_BETWEEN_CONTEXT_RECEIVERS")
inline fun <A, B, R> with(a: A, b: B, block: context(A, B) () -> R): R {
    contract {
        callsInPlace(block, InvocationKind.EXACTLY_ONCE)
    }
    return block(a, b)
}