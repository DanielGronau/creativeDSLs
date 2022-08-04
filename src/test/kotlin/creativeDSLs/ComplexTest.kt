package creativeDSLs

import org.apache.commons.numbers.complex.Complex
import org.junit.jupiter.api.Test

class ComplexTest {
    @Test
    fun test() {
        val a = Complex.ofCartesian(3.0, 4.0)
        val b = Complex.ofCartesian(2.0, 1.0)

        println(+a)
        println(-a)
        println(a + b)
        println(a - b)
        println(a * b)
        println(a / b)
        println(a < b)
    }
}