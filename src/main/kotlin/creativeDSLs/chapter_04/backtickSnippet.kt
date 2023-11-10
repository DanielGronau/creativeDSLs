package creativeDSLs.chapter_04

fun `check that the slithy toves gyre and gimble in the wabe`() {
    println("works!")
}

@Suppress("INVALID_CHARACTERS")
@JvmName("diamond")
fun `<>`() {
    println("works too!")
}

fun main() {
    `check that the slithy toves gyre and gimble in the wabe`()

    `<>`()
}