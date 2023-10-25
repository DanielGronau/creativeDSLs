package creativeDSLs.chapter_04

inline fun <reified T> List<T>.combine(): Unit = when(T::class) {
    Int::class -> (this as List<Int>).sum()
    String::class -> (this as List<String>).joinToString()
    else -> this.toString()
}.let { println(it) }

fun main() {
    listOf<Int>().combine() // 0
    listOf(1,2,3).combine()  // 6
    listOf("x","y","z").combine() // xyz
    listOf(true, false).combine() // [true, false]
}