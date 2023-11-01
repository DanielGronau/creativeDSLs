package creativeDSLs.chapter_05.classScope

data class Modulus(val modulus: Long) {
    init {
        require(modulus > 1)
    }

    @JvmInline
    value class Modular(val n: Long)

    val Long.m
        get() = Modular(remainder(this))

    val Int.m
        get() = Modular(remainder(this.toLong()))

    operator fun Modular.plus(that: Modular) = Modular(remainder(this.n + that.n))

    operator fun Modular.minus(that: Modular) = Modular(remainder(this.n - that.n))

    operator fun Modular.times(that: Modular) = Modular(remainder(this.n * that.n))

    operator fun Modular.div(that: Modular) = Modular(remainder(this.n * inverse(that.n)))

    private fun remainder(n: Long) = when {
        n < 0 -> (n % modulus) + modulus
        else -> n % modulus
    }

    private data class GcdResult(val gcd: Long, val x: Long, val y: Long)

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
}

fun <R> modulus(m: Long, body: Modulus.() -> R) =
    with(Modulus(m)) {
        body()
    }

fun main() {
    val x = modulus(7) {
        val a = 3.m + 5.m // Modular(n=1)
        val b = 3.m - 5.m // Modular(n=5)
        val c = 3.m * 5.m // Modular(n=1)
        val d = 3.m / 5.m // Modular(n=2)
        a + b + c + d
    }
    println(x) // Modular(n=2)

    modulus(10) {
        println(3.m + 5.m)
        println(3.m - 5.m)
        println(3.m * 5.m)
        //println(3.m / 5.m)
        println(3.m / 7.m)
    }
}