package creativeDSLs

inline fun <reified T> List<T>.combine() = println(when(T::class) {
    Int::class -> (this as List<Int>).sum()
    String::class -> (this as List<String>).fold("", String::plus)
    else -> this
})

fun main() {
    listOf<Int>().combine()
    listOf(1,2,3).combine()
    listOf("x","y","z").combine()
    listOf(true, false).combine()
}