package creativeDSLs.chapter_04

import kotlin.math.absoluteValue

fun Int.digits(base: Int = 10): List<Int> =
    generateSequence(this.absoluteValue) {
        (it / base).takeIf { it > 0 }
    }.map { it % base }.toList().reversed()

fun main() {
    println(0.digits())
    println(1729.digits())
    println(1729.digits(2))
    println(1729.digits(16))
}