package creativeDSLs.chapter_04

fun varargMethod(vararg numbers: Int, someString: String): Int {
    println("hey")
    return 1
}

val sadd = varargMethod(1, 2, 3, someString = "Hi!")
val asds = varargMethod(
    someString = "Hi!",
    numbers = intArrayOf(1, 2, 3)
)


fun varargAndLambda(someString: String, vararg numbers: Int, block: () -> Unit): Int {  println("hey")
    return 1 }

val sdfs = varargAndLambda("Hi!", 1, 2, 3) {
    println("hey")
}

