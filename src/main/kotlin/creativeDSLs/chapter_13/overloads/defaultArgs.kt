package creativeDSLs.chapter_13.overloads

fun withDefaultArgs(s: String = "one", i: Int = 42, j: Int, b: Boolean = false) {
    println("$s $i $j $b")
}

@JvmOverloads
fun withOverloading(s: String = "one", i: Int = 42, j: Int, b: Boolean = false) {
    println("$s $i $j $b")
}