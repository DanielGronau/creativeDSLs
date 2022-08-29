package creativeDSLs

infix fun <T> T.shouldBe(expected: T) {
    require(this == expected)
}

fun main() {
    val x = "test"

    x shouldBe "test"
}