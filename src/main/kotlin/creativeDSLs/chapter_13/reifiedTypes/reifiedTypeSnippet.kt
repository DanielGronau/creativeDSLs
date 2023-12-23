package creativeDSLs.chapter_13.reifiedTypes

inline fun <reified T> tellYourType(list: List<T>) {
    println(T::class.qualifiedName)
}

fun <T: Any> tellTypeJava(list: List<T>, clazz: Class<T>) {
    println(clazz.kotlin.qualifiedName)
}


fun main() {
    tellYourType(listOf(1, 2, 3))
    tellTypeJava(listOf(1, 2, 3), Int::class.java)

    tellYourType(listOf(listOf(1, 2, 3)))
    tellTypeJava(listOf(listOf(1, 2, 3)), List::class.java)
}