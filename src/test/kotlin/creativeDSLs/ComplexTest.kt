package creativeDSLs

import org.apache.commons.numbers.complex.Complex
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class ComplexTest {
    @Test
    fun test() {
        val a = Complex.ofCartesian(3.0, 4.0)
        val b = Complex.ofCartesian(2.0, 1.0)

        assertThat(3.0 + 4.0 * i).isEqualTo(Complex.ofCartesian(3.0, 4.0))
        assertThat(+a).isEqualTo(Complex.ofCartesian(3.0, 4.0))
        assertThat(-a).isEqualTo(Complex.ofCartesian(-3.0, -4.0))
        assertThat(a + b).isEqualTo(Complex.ofCartesian(5.0, 5.0))
        assertThat(a - b).isEqualTo(Complex.ofCartesian(1.0, 3.0))
        assertThat(a * b).isEqualTo(Complex.ofCartesian(2.0, 11.0))
        assertThat(a / b).isEqualTo(Complex.ofCartesian(2.0, 1.0))
        assertThat(a < b).isFalse

        foo("s", 1,2,3){}
        bar("s", 1,2,3, b = "c")
    }

    fun foo(s: String, vararg a: Int, b: () -> Unit) {}

    fun bar(s: String, vararg a: Int, b: String) {}

}