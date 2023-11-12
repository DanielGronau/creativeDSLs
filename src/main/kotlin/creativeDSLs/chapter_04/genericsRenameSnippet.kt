package creativeDSLs.chapter_04

@JvmName("averageInt")
fun average(list: List<Int>): Double =
    list.sum().toDouble() / list.size

@JvmName("averageDouble")
fun average(list: List<Double>): Double =
    list.sum() / list.size

fun main() {
    println(average(listOf(1, 2, 3)))
    println(average(listOf(7.0, 8.0, 9.0)))
}