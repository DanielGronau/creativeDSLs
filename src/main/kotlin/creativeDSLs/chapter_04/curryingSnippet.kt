package creativeDSLs.chapter_04

fun someFun(i: Int, s: String): String = s.repeat(i)

val <A, B, R> ((A, B) -> R).curry: (A) -> (B) -> R
    get() = { a -> { b -> this@curry(a, b) } }

fun main() {
    val sf3 = ::someFun.curry(3)

    println(sf3("Abc"))
    println(sf3("x"))


    val sf3Fun = ::someFun.curryFun()(3)
    println(sf3Fun("Abc"))
    println(sf3Fun("x"))
}

fun <A, B, R> ((A, B) -> R).curryFun(): (A) -> (B) -> R =
    { a -> { b -> this@curryFun(a, b) } }
