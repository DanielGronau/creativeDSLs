package creativeDSLs.chapter_04

infix fun <T> T.shouldBe(expected: T) {
    require(this == expected)
}

fun testIfExpected(s: String) {
    s.shouldBe("expected") // normal syntax
    s shouldBe "expected" // infix syntax
}