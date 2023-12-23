package creativeDSLs.chapter_13.overloads

fun withDefaultArgs(s: String = "one", i: Int = 42, d: Double, b: Boolean = false) {
    println("$s $i $d $b")
}

@JvmOverloads
fun withOverloading(s: String = "one", i: Int = 42, d: Double, b: Boolean = false) {
    println("$s $i $d $b")
}