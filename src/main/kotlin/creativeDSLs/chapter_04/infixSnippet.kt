package creativeDSLs.chapter_04

import kotlin.math.pow

infix fun <T> T.shouldBe(expected: T) {
    require(this == expected)
}

fun testIfExpected(s: String) {
    s.shouldBe("expected") // normal syntax
    s shouldBe "expected" // infix syntax
}

infix fun Double.`^`(exponent: Double) = this.pow(exponent)
infix fun Double.`^`(exponent: Int) = this.pow(exponent)

val x = 1.2 `^` 3