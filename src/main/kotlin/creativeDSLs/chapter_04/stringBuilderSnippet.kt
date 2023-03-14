package creativeDSLs.chapter_04

fun sb(block: StringBuilder.() -> Unit): String =
    StringBuilder()
        .apply { block(this) }
        .toString()

val s: String = sb {
    append("World")
    insert(0, "Hello ")
    append('!')
}