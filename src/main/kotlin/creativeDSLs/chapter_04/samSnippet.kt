package creativeDSLs.chapter_04

fun interface StringCheck {
    fun check(s: String): Boolean
}

val shortStringCheck = StringCheck { s -> s.length < 10 }